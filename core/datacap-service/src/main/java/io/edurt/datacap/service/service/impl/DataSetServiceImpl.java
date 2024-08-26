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
import io.edurt.datacap.common.sql.configure.SqlOperator;
import io.edurt.datacap.common.sql.configure.SqlOrder;
import io.edurt.datacap.common.sql.configure.SqlType;
import io.edurt.datacap.common.utils.SpiUtils;
import io.edurt.datacap.executor.Executor;
import io.edurt.datacap.executor.ExecutorUtils;
import io.edurt.datacap.executor.common.RunEngine;
import io.edurt.datacap.executor.common.RunMode;
import io.edurt.datacap.executor.common.RunProtocol;
import io.edurt.datacap.executor.common.RunState;
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
import io.edurt.datacap.service.entity.DatasetHistoryEntity;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.entity.SourceEntity;
import io.edurt.datacap.service.entity.UserEntity;
import io.edurt.datacap.service.enums.ColumnMode;
import io.edurt.datacap.service.enums.ColumnType;
import io.edurt.datacap.service.enums.CreatedMode;
import io.edurt.datacap.service.enums.QueryMode;
import io.edurt.datacap.service.enums.SyncMode;
import io.edurt.datacap.service.initializer.InitializerConfigure;
import io.edurt.datacap.service.initializer.job.DatasetJob;
import io.edurt.datacap.service.model.CreatedModel;
import io.edurt.datacap.service.repository.BaseRepository;
import io.edurt.datacap.service.repository.DataSetColumnRepository;
import io.edurt.datacap.service.repository.DataSetRepository;
import io.edurt.datacap.service.repository.DatasetHistoryRepository;
import io.edurt.datacap.service.security.UserDetailsService;
import io.edurt.datacap.service.service.DataSetService;
import io.edurt.datacap.spi.Plugin;
import io.edurt.datacap.spi.PluginType;
import io.edurt.datacap.spi.model.Configure;
import io.edurt.datacap.spi.model.Response;
import io.edurt.datacap.sql.EngineType;
import io.edurt.datacap.sql.builder.ColumnBuilder;
import io.edurt.datacap.sql.builder.TableBuilder;
import io.edurt.datacap.sql.model.Column;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Service
@Slf4j
@SuppressFBWarnings(value = {"REC_CATCH_EXCEPTION", "NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE", "EI_EXPOSE_REP2"})
public class DataSetServiceImpl
        implements DataSetService
{
    public final DataSetColumnRepository columnRepository;
    private final DataSetRepository repository;
    private final DatasetHistoryRepository historyRepository;
    private final Injector injector;
    private final InitializerConfigure initializerConfigure;
    private final org.quartz.Scheduler scheduler;
    private final Environment environment;

    public DataSetServiceImpl(DataSetRepository repository, DataSetColumnRepository columnRepository, DatasetHistoryRepository historyRepository, Injector injector, InitializerConfigure initializerConfigure, org.quartz.Scheduler scheduler, Environment environment)
    {
        this.repository = repository;
        this.columnRepository = columnRepository;
        this.historyRepository = historyRepository;
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
    public CommonResponse<Set<DataSetColumnEntity>> getColumnsByCode(String code)
    {
        Optional<DataSetEntity> entity = repository.findByCode(code);
        return entity.map(item -> {
                    isSelf(item);
                    return CommonResponse.success(columnRepository.findAllByDatasetOrderByPositionAsc(item));
                })
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
    public CommonResponse<Boolean> clearData(String code)
    {
        return repository.findByCode(code)
                .map(item -> {
                    ExecutorService service = Executors.newSingleThreadExecutor();
                    service.submit(() -> clearData(item, service));
                    return CommonResponse.success(true);
                })
                .orElseGet(() -> CommonResponse.failure(String.format("DataSet [ %s ] not found", code)));
    }

    @Override
    public CommonResponse<Object> adhoc(String code, Adhoc configure)
    {
        return repository.findByCode(code)
                .map(item -> {
                    String database = initializerConfigure.getDataSetConfigure().getDatabase();
                    List<SqlColumn> columns = Lists.newArrayList();
                    List<SqlColumn> groupBy = Lists.newArrayList();
                    List<SqlColumn> orderBy = Lists.newArrayList();
                    List<SqlColumn> wheres = Lists.newArrayList();
                    configure.getColumns()
                            .forEach(column -> columnRepository.findById(column.getId())
                                    .ifPresent(entity -> {
                                        AtomicReference<String> columnAlias = new AtomicReference<>(null);
                                        AtomicReference<String> expression = new AtomicReference<>(null);
                                        AtomicReference<String> columnName = new AtomicReference<>(entity.getName());
                                        if (StringUtils.isNotEmpty(column.getFunction())) {
                                            columnName.set(column.getFunction());
                                        }
                                        configure.getColumns().stream()
                                                .filter(it -> it.getId().equals(entity.getId()))
                                                .findFirst()
                                                .ifPresent(it -> {
                                                    expression.set(it.getExpression());
                                                    columnAlias.set(it.getAlias());
                                                });
                                        SqlColumn sqlColumn = SqlColumn.builder()
                                                .column(columnName.get())
                                                .expression(expression.get())
                                                .alias(columnAlias.get())
                                                .build();
                                        if (!column.getMode().equals(ColumnMode.FILTER)) {
                                            if (column.getMode().equals(ColumnMode.DIMENSION)) {
                                                sqlColumn.setExpression(null);
                                            }
                                            columns.add(sqlColumn);
                                        }
                                        // Only dimensions are added to GROUP BY
                                        if (entity.getMode().equals(ColumnMode.DIMENSION)) {
                                            if (!column.getMode().equals(ColumnMode.FILTER)) {
                                                groupBy.add(SqlColumn.builder()
                                                        .column(columnName.get())
                                                        .build());
                                            }
                                        }
                                        // Add to Sort Sequence
                                        if (StringUtils.isNotEmpty(column.getOrder())) {
                                            sqlColumn.setOrder(SqlOrder.valueOf(column.getOrder()));
                                            orderBy.add(sqlColumn);
                                        }
                                        // Add to where
                                        if (column.getMode().equals(ColumnMode.FILTER) && column.getExpression() != null) {
                                            sqlColumn.setOperator(SqlOperator.valueOf(column.getExpression()));
                                            sqlColumn.setValue(column.getValue());
                                            wheres.add(sqlColumn);
                                        }
                                    }));

                    SqlBody body = SqlBody.builder()
                            .type(SqlType.SELECT)
                            .database(database)
                            .table(item.getTableName())
                            .columns(columns)
                            .groups(groupBy)
                            .orders(orderBy)
                            .where(wheres)
                            .condition(" AND ")
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
                    targetConfigure.setInjector(injector);
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
    public CommonResponse<Object> getHistory(String code, FilterBody filter)
    {
        return repository.findByCode(code)
                .map(item -> {
                    Pageable pageable = PageRequestAdapter.of(filter);
                    return CommonResponse.success(PageEntity.build(historyRepository.findAllByDatasetOrderByCreateTimeDesc(item, pageable)));
                })
                .orElseGet(() -> CommonResponse.failure(String.format("DataSet [ %s ] not found", code)));
    }

    @Override
    public CommonResponse<PageEntity<DataSetEntity>> getAll(BaseRepository pagingAndSortingRepository, FilterBody filter)
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
                return "Bigint";
            case NUMBER_SIGNED:
                return "UInt64";
            case BOOLEAN:
                return "Boolean";
            case DATETIME:
                return "DateTime";
            case STRING:
            default:
                return "String";
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
        targetConfigure.setInjector(injector);
        return targetConfigure;
    }

    private void startBuild(DataSetEntity entity, boolean rebuildColumn)
    {
        if (entity.getId() == null) {
            entity.setCode(UUID.randomUUID().toString());
            String tablePrefix = initializerConfigure.getDataSetConfigure().getTablePrefix();
            String tableName = String.format("%s%s", tablePrefix, UUID.randomUUID().toString().replace("-", ""));
            entity.setTableName(tableName);
        }
        log.info("Start build metadata for dataset [ {} ] id [ {} ]", entity.getName(), entity.getId());
        createMetadata(entity, rebuildColumn);
    }

    private void createMetadata(DataSetEntity entity, boolean rebuildColumn)
    {
        Set<DataSetColumnEntity> targetColumns = entity.getColumns();
        Set<CreatedModel> createdModels = this.createdModeProcess(targetColumns, entity);
        try {
            repository.save(entity);
            if (rebuildColumn) {
                entity.getColumns()
                        .forEach(item -> item.setDataset(DataSetEntity.builder().id(entity.getId()).build()));
                columnRepository.saveAll(entity.getColumns());

                List<DataSetColumnEntity> originalColumns = columnRepository.findAllByDataset(entity);
                List<DataSetColumnEntity> columnsNotInEntity = originalColumns.stream()
                        .filter(originalColumn -> entity.getColumns().stream()
                                .noneMatch(addedColumn -> addedColumn.getId().equals(originalColumn.getId())))
                        .collect(Collectors.toList());
                columnRepository.deleteAll(columnsNotInEntity);
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
                Optional<CreatedModel> createdModel = createdModels.stream()
                        .filter(item -> item.getMode().equals(CreatedMode.CREATE_TABLE))
                        .findFirst();
                if (createdModel.isPresent()) {
                    createTable(entity);
                }
                else {
                    createAndModifyColumn(entity, createdModels);
                }
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
            String originTableName = entity.getTableName();
            String tableDefaultEngine = initializerConfigure.getDataSetConfigure().getTableDefaultEngine();

            List<Column> columns = Lists.newArrayList();
            List<DataSetColumnEntity> columnEntities = columnRepository.findAllByDataset(entity);
            columnEntities.stream()
                    .filter(item -> !item.isVirtualColumn())
                    .forEach(item -> {
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
            TableBuilder.Companion.PRIMARY_KEY(columnEntities.stream().filter(DataSetColumnEntity::isPrimaryKey).map(DataSetColumnEntity::getName).collect(Collectors.toList()));
            TableBuilder.Companion.SAMPLING_KEY(columnEntities.stream().filter(DataSetColumnEntity::isSamplingKey).map(DataSetColumnEntity::getName).collect(Collectors.toList()));
            if (entity.getLifeCycleColumn() != null
                    && entity.getLifeCycle() != null
                    && entity.getLifeCycleType() != null) {
                TableBuilder.Companion.ADD_LIFECYCLE(String.format("`%s` + INTERVAL %s %s", entity.getLifeCycleColumn(), entity.getLifeCycle(), entity.getLifeCycleType()));
            }
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
                flushSyncData(entity);
            }
        }
    }

    private void createAndModifyColumn(DataSetEntity entity, Set<CreatedModel> createdModels)
    {
        try {
            String tableName = String.format("`%s`.`%s`", initializerConfigure.getDataSetConfigure().getDatabase(), entity.getTableName());

            List<Column> createColumns = createdModels.stream()
                    .filter(item -> item.getMode().equals(CreatedMode.CREATE_COLUMN))
                    .map(this::formatColumn)
                    .collect(Collectors.toList());
            if (!createColumns.isEmpty()) {
                ColumnBuilder.Companion.BEGIN();
                ColumnBuilder.Companion.CREATE_COLUMN(tableName);
                ColumnBuilder.Companion.COLUMNS(createColumns.stream().map(Column::toColumnVar).collect(Collectors.toList()));
                ColumnBuilder.Companion.FORMAT_ENGINE(EngineType.CLICKHOUSE);
                String sql = ColumnBuilder.Companion.SQL();
                Plugin plugin = getOutputPlugin();
                SourceEntity source = getOutputSource();
                plugin.connect(source.toConfigure());
                Response response = plugin.execute(sql);
                Preconditions.checkArgument(response.getIsSuccessful(), response.getMessage());
                log.info("Create column sql \n {} \n on dataset [ {} ] id [ {} ]", sql, entity.getName(), entity.getId());
            }

            List<Column> modifyColumns = createdModels.stream()
                    .filter(item -> item.getMode().equals(CreatedMode.MODIFY_COLUMN))
                    .map(this::formatColumn)
                    .collect(Collectors.toList());
            if (!modifyColumns.isEmpty()) {
                ColumnBuilder.Companion.BEGIN();
                ColumnBuilder.Companion.MODIFY_COLUMN(tableName);
                ColumnBuilder.Companion.COLUMNS(modifyColumns.stream().map(Column::toColumnVar).collect(Collectors.toList()));
                ColumnBuilder.Companion.FORMAT_ENGINE(EngineType.CLICKHOUSE);
                String sql = ColumnBuilder.Companion.SQL();
                log.info("Modify column sql \n {} \n on dataset [ {} ] id [ {} ]", sql, entity.getName(), entity.getId());
                Plugin plugin = getOutputPlugin();
                SourceEntity source = getOutputSource();
                plugin.connect(source.toConfigure());
                Response response = plugin.execute(sql);
                Preconditions.checkArgument(response.getIsSuccessful(), response.getMessage());
            }

            createdModels.stream()
                    .filter(item -> item.getMode().equals(CreatedMode.MODIFY_LIFECYCLE))
                    .findFirst()
                    .ifPresent(item -> {
                        TableBuilder.Companion.BEGIN();
                        TableBuilder.Companion.MODIFY_LIFECYCLE(tableName);
                        TableBuilder.Companion.LIFECYCLE(String.format("`%s` + INTERVAL %s %s", item.getColumn().getName(), item.getColumn().getLength(), item.getColumn().getDefaultValue()));
                        String sql = TableBuilder.Companion.SQL();
                        log.info("Modify lifecycle sql \n {} \n on dataset [ {} ] id [ {} ]", sql, entity.getName(), entity.getId());
                        Plugin plugin = getOutputPlugin();
                        SourceEntity source = getOutputSource();
                        plugin.connect(source.toConfigure());
                        Response response = plugin.execute(sql);
                        Preconditions.checkArgument(response.getIsSuccessful(), response.getMessage());
                    });

            completeState(entity, DataSetState.TABLE_SUCCESS);
        }
        catch (Exception e) {
            log.warn("Modify dataset [ {} ] id [ {} ] add column failed", entity.getName(), entity.getId(), e);
            completeState(entity, DataSetState.TABLE_FAILED);
            entity.setMessage(e.getMessage());
        }
        finally {
            repository.save(entity);
            DataSetState state = entity.getState().get(entity.getState().size() - 1);
            if (state.equals(DataSetState.TABLE_SUCCESS)) {
                flushSyncData(entity);
            }
        }
    }

    private void syncData(DataSetEntity entity, ExecutorService service)
    {
        DatasetHistoryEntity history = new DatasetHistoryEntity();
        try {
            SourceEntity source = entity.getSource();
            Optional<Plugin> pluginOptional = PluginUtils.getPluginByNameAndType(injector, source.getType(), source.getProtocol());
            if (pluginOptional.isEmpty()) {
                throw new IllegalArgumentException(String.format("Plugin [ %s ] not found", initializerConfigure.getDataSetConfigure().getType()));
            }

            history.setState(RunState.CREATED);
            history.setCreateTime(new Date());
            history.setQuery(entity.getQuery());
            history.setDataset(entity);
            historyRepository.save(history);

            Executor executor = ExecutorUtils.findOne(this.injector, entity.getExecutor());
            Plugin inputPlugin = pluginOptional.get();
            Set<OriginColumn> originColumns = columnRepository.findAllByDataset(entity)
                    .stream()
                    .filter(item -> !item.isVirtualColumn())
                    .map(item -> new OriginColumn(item.getName(), item.getOriginal()))
                    .collect(Collectors.toSet());
            String database = initializerConfigure.getDataSetConfigure().getDatabase();
            Properties originInputProperties = new Properties();
            originInputProperties.put("driver", inputPlugin.driver());
            PipelineFieldBody inputFieldBody = ConfigureUtils.convertFieldBody(source, entity.getExecutor(), IConfigurePipelineType.INPUT, environment, originInputProperties);
            Properties inputProperties = ConfigureUtils.convertProperties(source, environment,
                    IConfigurePipelineType.INPUT, entity.getExecutor(), entity.getQuery(), inputFieldBody);
            Set<String> inputOptions = ConfigureUtils.convertOptions(source, environment, entity.getExecutor(), IConfigurePipelineType.INPUT);
            Configure inputConfigure = source.toConfigure();
            inputConfigure.setInjector(injector);
            ExecutorConfigure input = new ExecutorConfigure(source.getType(), inputProperties, inputOptions, RunProtocol.valueOf(source.getProtocol()),
                    inputPlugin, entity.getQuery(), database, entity.getTableName(), inputConfigure, originColumns);

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
                    workHome, this.injector, 600,
                    RunWay.valueOf(environment.getProperty("datacap.executor.way")),
                    RunMode.valueOf(environment.getProperty("datacap.executor.mode")),
                    environment.getProperty("datacap.executor.startScript"),
                    RunEngine.valueOf(environment.getProperty("datacap.executor.engine")));

            history.setState(RunState.RUNNING);
            historyRepository.save(history);
            ExecutorResponse response = executor.start(request);
            history.setUpdateTime(new Date());
            history.setElapsed((history.getUpdateTime().getTime() - history.getCreateTime().getTime()) / 1000);
            history.setMode(QueryMode.SYNC);
            history.setCount(response.getCount());
            history.setState(response.getState());
            Preconditions.checkArgument(response.getSuccessful(), response.getMessage());

            this.flushTableMetadata(entity, outputPlugin, database, requireNonNull(output.getOriginConfigure()));
        }
        catch (Exception e) {
            log.warn("Sync data for dataset [ {} ] failed", entity.getName(), e);
            history.setState(RunState.FAILURE);
            history.setMessage(e.getMessage());
        }
        finally {
            historyRepository.save(history);
            service.shutdownNow();
        }
    }

    /**
     * Flushes the synchronous data for the given DataSetEntity.
     *
     * @param entity the DataSetEntity to flush
     */
    private void flushSyncData(DataSetEntity entity)
    {
        log.info("Start schedule for dataset [ {} ] id [ {} ]", entity.getName(), entity.getId());
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

    private void clearData(DataSetEntity entity, ExecutorService service)
    {
        try {
            Plugin plugin = PluginUtils.getPluginByNameAndType(injector, initializerConfigure.getDataSetConfigure().getType(), PluginType.JDBC.name()).orElseGet(null);
            SourceEntity source = new SourceEntity();
            source.setType(initializerConfigure.getDataSetConfigure().getType());
            source.setDatabase(initializerConfigure.getDataSetConfigure().getDatabase());
            source.setHost(initializerConfigure.getDataSetConfigure().getHost());
            source.setPort(Integer.valueOf(initializerConfigure.getDataSetConfigure().getPort()));
            source.setUsername(initializerConfigure.getDataSetConfigure().getUsername());
            source.setPassword(initializerConfigure.getDataSetConfigure().getPassword());
            source.setProtocol(PluginType.JDBC.name());

            plugin.connect(source.toConfigure());
            SqlBody body = SqlBody.builder()
                    .type(SqlType.TRUNCATE)
                    .database(initializerConfigure.getDataSetConfigure().getDatabase())
                    .table(entity.getTableName())
                    .build();
            String sql = new SqlBuilder(body).getSql();
            log.info("Clear data for dataset [ {} ] id [ {} ] sql \n {}", entity.getName(), entity.getId(), sql);
            Response response = plugin.execute(sql);
            Preconditions.checkArgument(response.getIsSuccessful(), response.getMessage());
            this.flushTableMetadata(entity, plugin, initializerConfigure.getDataSetConfigure().getDatabase(), source.toConfigure());
        }
        catch (Exception e) {
            log.warn("Clear data for dataset [ {} ] failed", entity.getName(), e);
        }
        finally {
            service.shutdownNow();
        }
    }

    /**
     * Flushes the table metadata for a given dataset entity using the specified plugin and database configuration.
     *
     * @param entity the dataset entity to flush
     * @param plugin the plugin used to connect
     * @param database the database name
     * @param configure the configuration settings
     */
    private void flushTableMetadata(DataSetEntity entity, Plugin plugin, String database, Configure configure)
    {
        // Get the total number of rows and the total size of the dataset
        log.info("Get the total number of rows and the total size of the dataset [ {} ]", entity.getName());
        SqlBuilder builder = new SqlBuilder(SqlBody.builder()
                .type(SqlType.SELECT)
                .database("system")
                .table("parts")
                .columns(Lists.newArrayList(SqlColumn.builder().column("SUM(rows) AS totalCount").build(),
                        SqlColumn.builder().column("formatReadableSize(SUM(data_compressed_bytes)) AS totalSize").build()))
                .where(Lists.newArrayList(SqlColumn.builder().column("database").operator(SqlOperator.EQ).value(database).build(),
                        SqlColumn.builder().column("table").operator(SqlOperator.EQ).value(entity.getTableName()).build(),
                        SqlColumn.builder().column("active").operator(SqlOperator.EQ).value("1").build()))
                .condition(" AND ")
                .build());
        configure.setFormat("None");
        configure.setInjector(injector);
        plugin.connect(configure);
        Response outputResponse = plugin.execute(builder.getSql());
        if (outputResponse.getIsSuccessful()) {
            Object columnData = outputResponse.getColumns().get(0);
            if (columnData instanceof List<?>) {
                @SuppressWarnings("unchecked")
                List<Object> data = (List<Object>) columnData;
                if (!data.isEmpty()) {
                    entity.setTotalRows(data.get(0).toString());
                    entity.setTotalSize(data.get(1).toString());
                    repository.save(entity);
                }
            }
            else {
                log.warn("The response data is not a list type");
            }
        }
    }

    private Set<CreatedModel> createdModeProcess(Set<DataSetColumnEntity> targetColumns, DataSetEntity entity)
    {
        Set<CreatedModel> models = Sets.newHashSet();
        try {
            // If the id tag data is not set to new
            if (entity.getId() == null) {
                models.add(new CreatedModel(null, CreatedMode.CREATE_TABLE));
                return models;
            }

            // Check whether there is a data table bound to the current dataset
            if (!checkTableExists(entity)) {
                models.add(new CreatedModel(null, CreatedMode.CREATE_TABLE));
                return models;
            }

            List<DataSetColumnEntity> sourceColumns = columnRepository.findAllByDataset(entity)
                    .stream()
                    .filter(item -> !item.isVirtualColumn())
                    .collect(Collectors.toList());
            targetColumns = targetColumns.stream()
                    .filter(item -> !item.isVirtualColumn())
                    .collect(Collectors.toSet());
            for (DataSetColumnEntity sourceColumn : sourceColumns) {
                Optional<DataSetColumnEntity> filterColumn = targetColumns.stream()
                        .filter(item -> item.getId().equals(sourceColumn.getId()))
                        .findFirst();
                if (filterColumn.isEmpty()) {
                    models.add(new CreatedModel(sourceColumn, CreatedMode.CREATE_COLUMN));
                }
                else {
                    boolean matchFound = targetColumns.stream()
                            .filter(item -> item.getId().equals(sourceColumn.getId()))
                            .anyMatch(targetColumn ->
                                    targetColumn.getType().equals(sourceColumn.getType()) && targetColumn.getName().equals(sourceColumn.getName()));
                    if (!matchFound) {
                        models.add(new CreatedModel(filterColumn.get(), CreatedMode.MODIFY_COLUMN));
                    }
                }
            }

            if (entity.getLifeCycleColumn() != null) {
                DataSetColumnEntity column = DataSetColumnEntity.builder()
                        .name(entity.getLifeCycleColumn())
                        .length(Integer.parseInt(entity.getLifeCycle()))
                        .defaultValue(entity.getLifeCycleType())
                        .build();
                models.add(new CreatedModel(column, CreatedMode.MODIFY_LIFECYCLE));
            }
        }
        catch (Exception e) {
            log.warn("Get create model for dataset [ {} ] id [ {} ] failed", entity.getName(), entity.getId(), e);
        }
        return models;
    }

    /**
     * A function to check if a table exists in the database.
     *
     * @param entity the DataSetEntity to check
     * @return true if the table exists, false otherwise
     */
    private boolean checkTableExists(DataSetEntity entity)
    {
        try {
            Plugin plugin = getOutputPlugin();
            SourceEntity source = getOutputSource();
            Configure configure = source.toConfigure();
            configure.setInjector(injector);
            plugin.connect(configure);
            String sql = String.format("SHOW CREATE TABLE `%s`.`%s`", initializerConfigure.getDataSetConfigure().getDatabase(), entity.getTableName());
            log.info("Check table exists for dataset [ {} ] id [ {} ] sql \n {}", entity.getName(), entity.getId(), sql);
            Response response = plugin.execute(sql);
            Preconditions.checkArgument(response.getIsSuccessful(), response.getMessage());
            return true;
        }
        catch (Exception e) {
            log.warn("Check table exists for dataset [ {} ] failed", entity.getName(), e);
            return false;
        }
    }

    /**
     * Retrieves the output plugin using the injector and initializer configuration data set type.
     *
     * @return an instance of the output plugin, or null if not found
     */
    private Plugin getOutputPlugin()
    {
        return PluginUtils.getPluginByNameAndType(injector, initializerConfigure.getDataSetConfigure().getType(), PluginType.JDBC.name()).orElseGet(null);
    }

    /**
     * Retrieves the output source entity with the configured data set properties.
     *
     * @return the output source entity with the configured data set properties
     */
    private SourceEntity getOutputSource()
    {
        SourceEntity source = new SourceEntity();
        source.setType(initializerConfigure.getDataSetConfigure().getType());
        source.setDatabase(initializerConfigure.getDataSetConfigure().getDatabase());
        source.setHost(initializerConfigure.getDataSetConfigure().getHost());
        source.setPort(Integer.valueOf(initializerConfigure.getDataSetConfigure().getPort()));
        source.setUsername(initializerConfigure.getDataSetConfigure().getUsername());
        source.setPassword(initializerConfigure.getDataSetConfigure().getPassword());
        source.setProtocol(PluginType.JDBC.name());
        return source;
    }

    /**
     * Formats the given CreatedModel item into a Column object.
     *
     * @param item the CreatedModel item to be formatted
     * @return the formatted Column object
     */
    private Column formatColumn(CreatedModel item)
    {
        Column column = new Column();
        column.setName(item.getColumn().getName());
        column.setType(getColumnType(item.getColumn().getType()));
        column.setComment(item.getColumn().getComment());
        column.setLength(item.getColumn().getLength());
        column.setNullable(item.getColumn().isNullable());
        column.setDefaultValue(item.getColumn().getDefaultValue());
        column.setPrimaryKey(item.getColumn().isPrimaryKey());
        return column;
    }
}
