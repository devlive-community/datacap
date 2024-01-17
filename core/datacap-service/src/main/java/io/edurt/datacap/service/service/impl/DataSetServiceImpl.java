package io.edurt.datacap.service.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.common.enums.DataSetState;
import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.common.sql.SqlBuilder;
import io.edurt.datacap.common.sql.configure.SqlBody;
import io.edurt.datacap.common.sql.configure.SqlColumn;
import io.edurt.datacap.common.sql.configure.SqlType;
import io.edurt.datacap.common.utils.SpiUtils;
import io.edurt.datacap.executor.Executor;
import io.edurt.datacap.executor.ExecutorUtils;
import io.edurt.datacap.executor.common.RunMode;
import io.edurt.datacap.executor.common.RunProtocol;
import io.edurt.datacap.executor.common.RunWay;
import io.edurt.datacap.executor.configure.ExecutorConfigure;
import io.edurt.datacap.executor.configure.ExecutorRequest;
import io.edurt.datacap.executor.configure.ExecutorResponse;
import io.edurt.datacap.executor.configure.OriginColumn;
import io.edurt.datacap.scheduler.Scheduler;
import io.edurt.datacap.scheduler.SchedulerRequest;
import io.edurt.datacap.service.adapter.PageRequestAdapter;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.body.PipelineFieldBody;
import io.edurt.datacap.service.body.adhoc.Adhoc;
import io.edurt.datacap.service.common.ConfigureUtils;
import io.edurt.datacap.service.common.FolderUtils;
import io.edurt.datacap.service.common.PluginUtils;
import io.edurt.datacap.service.configure.IConfigurePipelineType;
import io.edurt.datacap.service.entity.DataSetColumnEntity;
import io.edurt.datacap.service.entity.DataSetEntity;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.entity.SourceEntity;
import io.edurt.datacap.service.entity.UserEntity;
import io.edurt.datacap.service.enums.ColumnMode;
import io.edurt.datacap.service.enums.ColumnType;
import io.edurt.datacap.service.enums.SyncMode;
import io.edurt.datacap.service.initializer.InitializerConfigure;
import io.edurt.datacap.service.initializer.job.DatasetJob;
import io.edurt.datacap.service.repository.DataSetColumnRepository;
import io.edurt.datacap.service.repository.DataSetRepository;
import io.edurt.datacap.service.security.UserDetailsService;
import io.edurt.datacap.service.service.DataSetService;
import io.edurt.datacap.spi.FormatType;
import io.edurt.datacap.spi.Plugin;
import io.edurt.datacap.spi.PluginType;
import io.edurt.datacap.spi.model.Configure;
import io.edurt.datacap.spi.model.Response;
import io.edurt.datacap.sql.builder.TableBuilder;
import io.edurt.datacap.sql.model.Column;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
@SuppressFBWarnings(value = {"REC_CATCH_EXCEPTION"})
public class DataSetServiceImpl
        implements DataSetService
{
    public final DataSetColumnRepository columnRepository;
    private final DataSetRepository repository;
    private final Injector injector;
    private final InitializerConfigure initializerConfigure;
    private final org.quartz.Scheduler scheduler;
    private final Environment environment;

    public DataSetServiceImpl(DataSetRepository repository, DataSetColumnRepository columnRepository, Injector injector, InitializerConfigure initializerConfigure, org.quartz.Scheduler scheduler, Environment environment)
    {
        this.repository = repository;
        this.columnRepository = columnRepository;
        this.injector = injector;
        this.initializerConfigure = initializerConfigure;
        this.scheduler = scheduler;
        this.environment = environment;
    }

    @Transactional
    public CommonResponse<DataSetEntity> saveOrUpdate(DataSetEntity configure)
    {
        UserEntity user = UserDetailsService.getUser();
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(() -> {
            configure.setUser(user);
            completeState(configure, DataSetState.METADATA_START);
            startBuild(configure, true);
        });
        return CommonResponse.success(configure);
    }

    @Override
    public CommonResponse<DataSetEntity> rebuild(Long id)
    {
        Optional<DataSetEntity> entity = repository.findById(id);
        if (!entity.isPresent()) {
            return CommonResponse.failure(String.format("DataSet [ %s ] not found", id));
        }
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(() -> startBuild(entity.get(), false));
        return CommonResponse.success(entity);
    }

    @Override
    public CommonResponse<Set<DataSetColumnEntity>> getColumns(Long id)
    {
        Optional<DataSetEntity> entity = repository.findById(id);
        if (!entity.isPresent()) {
            return CommonResponse.failure(String.format("DataSet [ %s ] not found", id));
        }
        return CommonResponse.success(columnRepository.findAllByDataset(entity.get()));
    }

    @Override
    public CommonResponse<Set<DataSetColumnEntity>> getColumnsByCode(String code)
    {
        Optional<DataSetEntity> entity = repository.findByCode(code);
        return entity.map(item -> CommonResponse.success(columnRepository.findAllByDataset(item)))
                .orElseGet(() -> CommonResponse.failure(String.format("DataSet [ %s ] not found", code)));
    }

    @Override
    public CommonResponse<Boolean> syncData(Long id)
    {
        Optional<DataSetEntity> entityOptional = repository.findById(id);
        if (!entityOptional.isPresent()) {
            return CommonResponse.failure(String.format("DataSet [ %s ] not found", id));
        }
        ExecutorService service = Executors.newSingleThreadExecutor();
        DataSetEntity entity = entityOptional.get();
        service.submit(() -> syncData(entity, service));
        return CommonResponse.success(true);
    }

    @Override
    public CommonResponse<Object> adhoc(String code, Adhoc configure)
    {
        return repository.findByCode(code)
                .map(item -> {
                    String database = initializerConfigure.getDataSetConfigure().getDatabase();
                    List<SqlColumn> columns = Lists.newArrayList();
                    List<SqlColumn> groupBy = Lists.newArrayList();
                    configure.getColumns()
                            .forEach(column -> columnRepository.findById(column.getId())
                                    .ifPresent(entity -> {
                                        String columnName = entity.getName();
                                        AtomicReference<String> columnAlias = new AtomicReference<>(null);
                                        AtomicReference<String> expression = new AtomicReference<>(null);
                                        configure.getColumns().stream()
                                                .filter(it -> it.getId().equals(entity.getId()))
                                                .findFirst()
                                                .ifPresent(it -> {
                                                    expression.set(it.getExpression());
                                                    columnAlias.set(it.getAlias());
                                                });
                                        SqlColumn sqlColumn = SqlColumn.builder()
                                                .column(columnName)
                                                .expression(expression.get())
                                                .alias(columnAlias.get())
                                                .build();
                                        columns.add(sqlColumn);
                                        // Only dimensions are added to GROUP BY
                                        if (entity.getMode().equals(ColumnMode.DIMENSION)) {
                                            groupBy.add(SqlColumn.builder()
                                                    .column(columnName)
                                                    .build());
                                        }
                                    }));

                    if (configure.getGroups() != null) {
                        configure.getGroups()
                                .forEach(column -> columnRepository.findById(column.getId())
                                        .ifPresent(entity -> {
                                            SqlColumn sqlColumn = SqlColumn.builder()
                                                    .column(entity.getName())
                                                    .build();
                                            groupBy.add(sqlColumn);
                                            // Add columns from GROUP BY to the query column
                                            columns.add(sqlColumn);
                                        }));
                    }

                    SqlBody body = SqlBody.builder()
                            .type(SqlType.SELECT)
                            .database(database)
                            .table(item.getTableName())
                            .columns(columns)
                            .groups(groupBy)
                            .limit(configure.getLimit())
                            .build();
                    String sql = new SqlBuilder(body).getSql();
                    log.info("Execute SQL: {} for DataSet [ {} ]", sql, code);

                    Optional<Plugin> pluginOptional = PluginUtils.getPluginByNameAndType(injector, initializerConfigure.getDataSetConfigure().getType(), PluginType.JDBC.name());
                    if (!pluginOptional.isPresent()) {
                        throw new IllegalArgumentException(String.format("Plugin [ %s ] not found", initializerConfigure.getDataSetConfigure().getType()));
                    }
                    Plugin plugin = pluginOptional.get();
                    Configure targetConfigure = new Configure();
                    targetConfigure.setHost(initializerConfigure.getDataSetConfigure().getHost());
                    targetConfigure.setPort(Integer.valueOf(initializerConfigure.getDataSetConfigure().getPort()));
                    targetConfigure.setUsername(Optional.ofNullable(initializerConfigure.getDataSetConfigure().getUsername()));
                    targetConfigure.setPassword(Optional.ofNullable(initializerConfigure.getDataSetConfigure().getPassword()));
                    targetConfigure.setDatabase(Optional.ofNullable(database));
                    targetConfigure.setFormat(FormatType.JSON);
                    plugin.connect(targetConfigure);
                    Response response = plugin.execute(sql);
                    response.setContent(sql);
                    return CommonResponse.success(response);
                })
                .orElseGet(() -> CommonResponse.failure(String.format("DataSet [ %s ] not found", code)));
    }

    @Override
    public CommonResponse<Set<String>> getActuators()
    {
        Set<String> actuators = Sets.newHashSet();
        this.injector.getInstance(Key.get(new TypeLiteral<Set<Scheduler>>() {}))
                .forEach(item -> actuators.add(item.name()));
        return CommonResponse.success(actuators);
    }

    @Override
    public CommonResponse<DataSetEntity> getInfo(String code)
    {
        return repository.findByCode(code)
                .map(CommonResponse::success)
                .orElseGet(() -> CommonResponse.failure(String.format("DataSet [ %s ] not found", code)));
    }

    @Override
    public CommonResponse<PageEntity<DataSetEntity>> getAll(PagingAndSortingRepository pagingAndSortingRepository, FilterBody filter)
    {
        Pageable pageable = PageRequestAdapter.of(filter);
        return CommonResponse.success(PageEntity.build(repository.findAllByUser(UserDetailsService.getUser(), pageable)));
    }

    private void completeState(DataSetEntity entity, DataSetState state)
    {
        List<DataSetState> sourceStates = entity.getState();
        List<DataSetState> states = Lists.newArrayList();
        if (sourceStates != null) {
            states = sourceStates;
            states = states.stream().filter(item -> !item.name().startsWith(state.name().split("_")[0])).collect(Collectors.toList());
        }
        states.add(state);
        entity.setState(states);
    }

    private String getColumnType(ColumnType type)
    {
        switch (type) {
            case NUMBER:
                return "bigint";
            case BOOLEAN:
                return "boolean";
            case STRING:
            default:
                return "varchar";
        }
    }

    private Configure getConfigure(String database)
    {
        Configure targetConfigure = new Configure();
        targetConfigure.setHost(initializerConfigure.getDataSetConfigure().getHost());
        targetConfigure.setPort(Integer.valueOf(initializerConfigure.getDataSetConfigure().getPort()));
        targetConfigure.setUsername(Optional.ofNullable(initializerConfigure.getDataSetConfigure().getUsername()));
        targetConfigure.setPassword(Optional.ofNullable(initializerConfigure.getDataSetConfigure().getPassword()));
        targetConfigure.setDatabase(Optional.ofNullable(database));
        return targetConfigure;
    }

    private void startBuild(DataSetEntity entity, boolean rebuildColumn)
    {
        if (entity.getId() == null) {
            entity.setCode(UUID.randomUUID().toString());
        }
        DataSetState state = entity.getState().get(entity.getState().size() - 1);
        if (state.equals(DataSetState.METADATA_START) || state.equals(DataSetState.METADATA_FAILED)) {
            log.info("Start build metadata for dataset [ {} ]", entity.getName());
            createMetadata(entity, rebuildColumn);
        }
        throw new IllegalArgumentException(String.format("Invalid state [ %s ]", state));
    }

    private void createMetadata(DataSetEntity entity, boolean rebuildColumn)
    {
        try {
            repository.save(entity);
            if (rebuildColumn) {
                entity.getColumns()
                        .forEach(item -> item.setDataset(DataSetEntity.builder().id(entity.getId()).build()));
                columnRepository.saveAll(entity.getColumns());
            }
            completeState(entity, DataSetState.METADATA_SUCCESS);
        }
        catch (Exception e) {
            log.warn("Create dataset [ {} ] ", entity.getName(), e);
            completeState(entity, DataSetState.METADATA_FAILED);
            entity.setMessage(e.getMessage());
        }
        finally {
            repository.save(entity);
            DataSetState state = entity.getState().get(entity.getState().size() - 1);
            if (state.equals(DataSetState.METADATA_SUCCESS)
                    || state.equals(DataSetState.TABLE_START)
                    || state.equals(DataSetState.TABLE_FAILED)) {
                log.info("Start build table for dataset [ {} ]", entity.getName());
                createTable(entity);
            }
        }
    }

    private void createTable(DataSetEntity entity)
    {
        try {
            Optional<Plugin> pluginOptional = PluginUtils.getPluginByNameAndType(injector, initializerConfigure.getDataSetConfigure().getType(), PluginType.JDBC.name());
            if (!pluginOptional.isPresent()) {
                throw new IllegalArgumentException(String.format("Plugin [ %s ] not found", initializerConfigure.getDataSetConfigure().getType()));
            }
            Plugin plugin = pluginOptional.get();
            String database = initializerConfigure.getDataSetConfigure().getDatabase();
            String tablePrefix = initializerConfigure.getDataSetConfigure().getTablePrefix();
            String originTableName = String.format("%s%s", tablePrefix, UUID.randomUUID().toString().replace("-", ""));
            String tableDefaultEngine = initializerConfigure.getDataSetConfigure().getTableDefaultEngine();

            List<Column> columns = Lists.newArrayList();
            List<DataSetColumnEntity> columnEntities = columnRepository.findAllByDataset(entity);
            columnEntities.forEach(item -> {
                Column column = new Column();
                column.setName(item.getName());
                column.setType(getColumnType(item.getType()));
                column.setComment(item.getComment());
                column.setLength(item.getLength());
                column.setNullable(item.isNullable());
                column.setDefaultValue(item.getDefaultValue());
                columns.add(column);
            });

            TableBuilder.Companion.BEGIN();
            TableBuilder.Companion.CREATE_TABLE(String.format("`%s`.`%s`", database, originTableName));
            TableBuilder.Companion.COLUMNS(columns.stream().map(Column::toColumnVar).collect(Collectors.toList()));
            TableBuilder.Companion.ENGINE(tableDefaultEngine);
            TableBuilder.Companion.ORDER_BY(columnEntities.stream().filter(DataSetColumnEntity::isOrderByKey).map(DataSetColumnEntity::getName).collect(Collectors.toList()));
            TableBuilder.Companion.PARTITION_BY(columnEntities.stream().filter(DataSetColumnEntity::isPartitionKey).map(DataSetColumnEntity::getName).collect(Collectors.toList()));
            String sql = TableBuilder.Companion.SQL();
            log.info("Create table sql \n {} \n on dataset [ {} ]", sql, entity.getName());

            Configure targetConfigure = new Configure();
            targetConfigure.setHost(initializerConfigure.getDataSetConfigure().getHost());
            targetConfigure.setPort(Integer.valueOf(initializerConfigure.getDataSetConfigure().getPort()));
            targetConfigure.setUsername(Optional.ofNullable(initializerConfigure.getDataSetConfigure().getUsername()));
            targetConfigure.setPassword(Optional.ofNullable(initializerConfigure.getDataSetConfigure().getPassword()));
            targetConfigure.setDatabase(Optional.ofNullable(database));
            plugin.connect(targetConfigure);
            Response response = plugin.execute(sql);
            plugin.destroy();
            if (response.getIsSuccessful()) {
                entity.setTableName(originTableName);
                entity.setMessage(null);
                completeState(entity, DataSetState.TABLE_SUCCESS);
            }
            else {
                throw new RuntimeException(response.getMessage());
            }
        }
        catch (Exception e) {
            log.warn("Create dataset [ {} ] ", entity.getName(), e);
            completeState(entity, DataSetState.TABLE_FAILED);
            entity.setMessage(e.getMessage());
        }
        finally {
            repository.save(entity);
            DataSetState state = entity.getState().get(entity.getState().size() - 1);
            if (state.equals(DataSetState.TABLE_SUCCESS)) {
                log.info("Start schedule for dataset [ {} ]", entity.getName());
                if (entity.getSyncMode().equals(SyncMode.TIMING)) {
                    SpiUtils.findSchedule(this.injector, entity.getScheduler())
                            .ifPresent(scheduler -> {
                                SchedulerRequest request = new SchedulerRequest();
                                request.setName(entity.getId().toString());
                                request.setGroup("datacap");
                                request.setExpression(entity.getExpression());
                                request.setJobId(String.valueOf(entity.getId()));
                                request.setCreateBeforeDelete(true);
                                if (scheduler.name().equals("Default")) {
                                    request.setJob(new DatasetJob());
                                    request.setScheduler(this.scheduler);
                                }
                                scheduler.initialize(request);
                            });
                }
                else {
                    SpiUtils.findSchedule(this.injector, entity.getScheduler())
                            .ifPresent(scheduler -> {
                                SchedulerRequest request = new SchedulerRequest();
                                request.setName(entity.getId().toString());
                                request.setGroup("datacap");
                                if (scheduler.name().equals("Default")) {
                                    request.setScheduler(this.scheduler);
                                }
                                scheduler.stop(request);
                            });
                }
            }
        }
    }

    private void syncData(DataSetEntity entity, ExecutorService service)
    {
        try {
            SourceEntity source = entity.getSource();
            Optional<Plugin> pluginOptional = PluginUtils.getPluginByNameAndType(injector, source.getType(), source.getProtocol());
            if (!pluginOptional.isPresent()) {
                throw new IllegalArgumentException(String.format("Plugin [ %s ] not found", initializerConfigure.getDataSetConfigure().getType()));
            }

            Executor executor = ExecutorUtils.findOne(this.injector, entity.getExecutor());

            Plugin inputPlugin = pluginOptional.get();
            Set<OriginColumn> originColumns = columnRepository.findAllByDataset(entity)
                    .stream()
                    .map(item -> new OriginColumn(item.getName(), item.getOriginal()))
                    .collect(Collectors.toSet());
            String database = initializerConfigure.getDataSetConfigure().getDatabase();
            Properties originInputProperties = new Properties();
            originInputProperties.put("driver", inputPlugin.driver());
            PipelineFieldBody inputFieldBody = ConfigureUtils.convertFieldBody(source, entity.getExecutor(), IConfigurePipelineType.INPUT, environment, originInputProperties);
            Properties inputProperties = ConfigureUtils.convertProperties(source, environment,
                    IConfigurePipelineType.INPUT, entity.getExecutor(), entity.getQuery(), inputFieldBody);
            Set<String> inputOptions = ConfigureUtils.convertOptions(source, environment, entity.getExecutor(), IConfigurePipelineType.INPUT);
            ExecutorConfigure input = new ExecutorConfigure(source.getType(), inputProperties, inputOptions, RunProtocol.valueOf(source.getProtocol()),
                    inputPlugin, entity.getQuery(), database, entity.getTableName(), source.toConfigure(), originColumns);

            Plugin outputPlugin = PluginUtils.getPluginByNameAndType(injector, initializerConfigure.getDataSetConfigure().getType(), PluginType.JDBC.name()).orElseGet(null);
            SourceEntity outputSource = new SourceEntity();
            outputSource.setType(initializerConfigure.getDataSetConfigure().getType());
            outputSource.setDatabase(initializerConfigure.getDataSetConfigure().getDatabase());
            outputSource.setHost(initializerConfigure.getDataSetConfigure().getHost());
            outputSource.setPort(Integer.valueOf(initializerConfigure.getDataSetConfigure().getPort()));
            outputSource.setUsername(initializerConfigure.getDataSetConfigure().getUsername());
            outputSource.setPassword(initializerConfigure.getDataSetConfigure().getPassword());
            outputSource.setProtocol(PluginType.JDBC.name());
            Properties originOutputProperties = new Properties();
            List<String> fields = Lists.newArrayList();
            columnRepository.findAllByDataset(entity)
                    .forEach(item -> fields.add(item.getName()));
            originOutputProperties.put("fields", String.join("\n", fields));
            originOutputProperties.put("database", database);
            originOutputProperties.put("table", entity.getTableName());
            PipelineFieldBody outputFieldBody = ConfigureUtils.convertFieldBody(outputSource, entity.getExecutor(), IConfigurePipelineType.OUTPUT, environment, originOutputProperties);
            Properties outputProperties = ConfigureUtils.convertProperties(outputSource, environment,
                    IConfigurePipelineType.OUTPUT, entity.getExecutor(), entity.getQuery(), outputFieldBody);
            Set<String> outputOptions = ConfigureUtils.convertOptions(outputSource, environment, entity.getExecutor(), IConfigurePipelineType.OUTPUT);
            ExecutorConfigure output = new ExecutorConfigure(outputSource.getType(), outputProperties, outputOptions, RunProtocol.NONE,
                    outputPlugin, null, null, null, getConfigure(database), Sets.newHashSet());

            String taskName = DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmssSSS");
            String workHome = FolderUtils.getWorkHome(initializerConfigure.getDataHome(), entity.getUser().getUsername(), String.join(File.separator, "dataset", entity.getExecutor().toLowerCase(), taskName));
            ExecutorRequest request = new ExecutorRequest(taskName, entity.getUser().getUsername(), input, output,
                    environment.getProperty(String.format("datacap.executor.%s.home", entity.getExecutor().toLowerCase())),
                    workHome, this.injector, 600, RunWay.LOCAL, RunMode.CLIENT);
            ExecutorResponse response = executor.start(request);
            Preconditions.checkArgument(response.getSuccessful(), response.getMessage());
        }
        catch (Exception e) {
            log.warn("Sync data for dataset [ {} ] failed", entity.getName(), e);
        }
        finally {
            service.shutdownNow();
        }
    }
}
