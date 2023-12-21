package io.edurt.datacap.service.service.impl;

import com.google.common.collect.Lists;
import com.google.inject.Injector;
import io.edurt.datacap.common.enums.DataSetState;
import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.adapter.PageRequestAdapter;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.common.PluginUtils;
import io.edurt.datacap.service.entity.DataSetColumnEntity;
import io.edurt.datacap.service.entity.DataSetEntity;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.entity.UserEntity;
import io.edurt.datacap.service.enums.ColumnType;
import io.edurt.datacap.service.initializer.InitializerConfigure;
import io.edurt.datacap.service.repository.DataSetColumnRepository;
import io.edurt.datacap.service.repository.DataSetRepository;
import io.edurt.datacap.service.security.UserDetailsService;
import io.edurt.datacap.service.service.DataSetService;
import io.edurt.datacap.spi.Plugin;
import io.edurt.datacap.spi.PluginType;
import io.edurt.datacap.spi.model.Configure;
import io.edurt.datacap.spi.model.Response;
import io.edurt.datacap.sql.builder.TableBuilder;
import io.edurt.datacap.sql.model.Column;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DataSetServiceImpl
        implements DataSetService
{
    private final DataSetRepository repository;
    public final DataSetColumnRepository columnRepository;
    private final Injector injector;
    private final InitializerConfigure initializerConfigure;

    public DataSetServiceImpl(DataSetRepository repository, DataSetColumnRepository columnRepository, Injector injector, InitializerConfigure initializerConfigure)
    {
        this.repository = repository;
        this.columnRepository = columnRepository;
        this.injector = injector;
        this.initializerConfigure = initializerConfigure;
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

    private void startBuild(DataSetEntity entity, boolean rebuildColumn)
    {
        switch (entity.getState().get(entity.getState().size() - 1)) {
            case METADATA_START:
            case METADATA_FAILED:
                createMetadata(entity, rebuildColumn);
                break;
            case METADATA_SUCCESS:
            case TABLE_START:
            case TABLE_FAILED:
                createTable(entity);
                break;
        }
    }

    private void createMetadata(DataSetEntity entity, boolean rebuildColumn)
    {
        try {
            repository.save(entity);
            if (rebuildColumn) {
                entity.getColumns()
                        .stream()
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
            startBuild(entity, rebuildColumn);
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
            TableBuilder.Companion.COLUMNS(columns.stream().map(item -> item.toColumnVar()).collect(Collectors.toList()));
            TableBuilder.Companion.ENGINE(tableDefaultEngine);
            TableBuilder.Companion.ORDER_BY(columnEntities.stream().filter(DataSetColumnEntity::isOrderByKey).map(DataSetColumnEntity::getName).collect(Collectors.toList()));
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
            if (response.getIsSuccessful()) {
                entity.setTableName(originTableName);
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
        }
    }
}
