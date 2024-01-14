package io.edurt.datacap.server.scheduled.source;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Maps;
import com.google.inject.Injector;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.common.enums.NodeType;
import io.edurt.datacap.common.enums.Type;
import io.edurt.datacap.common.utils.JsonUtils;
import io.edurt.datacap.schedule.ScheduledRunnable;
import io.edurt.datacap.service.common.PluginUtils;
import io.edurt.datacap.service.entity.ScheduledEntity;
import io.edurt.datacap.service.entity.ScheduledHistoryEntity;
import io.edurt.datacap.service.entity.SourceEntity;
import io.edurt.datacap.service.entity.TemplateSqlEntity;
import io.edurt.datacap.service.entity.metadata.ColumnEntity;
import io.edurt.datacap.service.entity.metadata.DatabaseEntity;
import io.edurt.datacap.service.entity.metadata.TableEntity;
import io.edurt.datacap.service.itransient.SqlConfigure;
import io.edurt.datacap.service.repository.ScheduledHistoryRepository;
import io.edurt.datacap.service.repository.SourceRepository;
import io.edurt.datacap.service.repository.TemplateSqlRepository;
import io.edurt.datacap.service.repository.metadata.ColumnRepository;
import io.edurt.datacap.service.repository.metadata.DatabaseRepository;
import io.edurt.datacap.service.repository.metadata.TableRepository;
import io.edurt.datacap.spi.Plugin;
import io.edurt.datacap.spi.executor.PipelineState;
import io.edurt.datacap.spi.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@SuppressFBWarnings(value = {"REC_CATCH_EXCEPTION"})
public class SyncMetadataScheduledRunnable
        extends ScheduledRunnable
{
    private final Injector injector;
    private final SourceRepository sourceHandler;
    private final DatabaseRepository databaseHandler;
    private final TableRepository tableHandler;
    private final ColumnRepository columnHandler;
    private final TemplateSqlRepository templateHandler;
    private final ScheduledHistoryRepository scheduledHistoryHandler;
    private final ScheduledEntity scheduledEntity;

    public SyncMetadataScheduledRunnable(String name, Injector injector, SourceRepository sourceHandler, DatabaseRepository databaseHandler, TableRepository tableHandler, ColumnRepository columnHandler, TemplateSqlRepository templateHandler, ScheduledHistoryRepository scheduledHistoryHandler, ScheduledEntity scheduledEntity)
    {
        super(name);
        this.injector = injector;
        this.sourceHandler = sourceHandler;
        this.databaseHandler = databaseHandler;
        this.tableHandler = tableHandler;
        this.columnHandler = columnHandler;
        this.templateHandler = templateHandler;
        this.scheduledHistoryHandler = scheduledHistoryHandler;
        this.scheduledEntity = scheduledEntity;
    }

    /**
     * Executes the run method of the Runnable interface.
     */
    @Override
    public void run()
    {
        sourceHandler.findAll()
                .stream()
                .parallel()
                .forEach(entity -> {
                    AtomicInteger databaseAddedCount = new AtomicInteger(0);
                    AtomicInteger databaseUpdatedCount = new AtomicInteger(0);
                    AtomicInteger databaseRemovedCount = new AtomicInteger(0);
                    AtomicInteger tableAddedCount = new AtomicInteger(0);
                    AtomicInteger tableUpdatedCount = new AtomicInteger(0);
                    AtomicInteger tableRemovedCount = new AtomicInteger(0);
                    AtomicInteger columnAddedCount = new AtomicInteger(0);
                    AtomicInteger columnUpdatedCount = new AtomicInteger(0);
                    AtomicInteger columnRemovedCount = new AtomicInteger(0);
                    Map<String, DatabaseEntity> databaseCache = Maps.newHashMap();
                    Map<String, List<TableEntity>> databaseTableCache = Maps.newHashMap();
                    Map<String, TableEntity> tableCache = Maps.newHashMap();
                    Map<String, List<ColumnEntity>> tableColumnCache = Maps.newHashMap();
                    ScheduledHistoryEntity scheduledHistory = ScheduledHistoryEntity.builder()
                            .name(String.format("Sync source [ %s ]", entity.getName()))
                            .scheduled(scheduledEntity)
                            .source(entity)
                            .state(PipelineState.RUNNING)
                            .build();
                    scheduledHistoryHandler.save(scheduledHistory);
                    log.info("==================== Sync metadata  [ {} ] started =================", entity.getName());
                    Optional<Plugin> pluginOptional = PluginUtils.getPluginByNameAndType(this.injector, entity.getType(), entity.getProtocol());
                    if (!pluginOptional.isPresent()) {
                        log.warn("The scheduled task [ {} ] source [ {} ] protocol [ {} ] is not available", this.getName(), entity.getType(), entity.getProtocol());
                    }
                    else {
                        try {
                            Plugin plugin = pluginOptional.get();
                            plugin.connect(entity.toConfigure());
                            Response response = plugin.execute(plugin.validator());
                            if (!response.getIsSuccessful()) {
                                log.error("The scheduled task [ {} ] source [ {} ] not available", this.getName(), entity.getName());
                            }
                            else {
                                this.startSyncDatabase(entity,
                                        plugin,
                                        databaseCache,
                                        databaseTableCache,
                                        tableCache,
                                        tableColumnCache,
                                        databaseAddedCount,
                                        databaseUpdatedCount,
                                        databaseRemovedCount,
                                        tableAddedCount,
                                        tableUpdatedCount,
                                        tableRemovedCount,
                                        columnAddedCount,
                                        columnUpdatedCount,
                                        columnRemovedCount);
                            }
                        }
                        catch (Exception e) {
                            log.error("The scheduled task [ {} ] source [ {} ] not available ", this.getName(), entity.getName(), e);
                        }
                    }
                    log.info("==================== Sync metadata  [ {} ] finished =================", entity.getName());
                    Properties info = new Properties();
                    info.put("databaseAddedCount", databaseAddedCount.get());
                    info.put("databaseUpdatedCount", databaseUpdatedCount.get());
                    info.put("databaseRemovedCount", databaseRemovedCount.get());
                    info.put("tableAddedCount", tableAddedCount.get());
                    info.put("tableUpdatedCount", tableUpdatedCount.get());
                    info.put("tableRemovedCount", tableRemovedCount.get());
                    info.put("columnAddedCount", columnAddedCount.get());
                    info.put("columnUpdatedCount", columnUpdatedCount.get());
                    info.put("columnRemovedCount", columnRemovedCount.get());
                    scheduledHistory.setInfo(info);
                    scheduledHistory.setState(PipelineState.SUCCESS);
                    scheduledHistoryHandler.save(scheduledHistory);
                    databaseCache.clear();
                    databaseTableCache.clear();
                    tableCache.clear();
                    tableColumnCache.clear();
                });
    }

    /**
     * Retrieves the SQL context for the given TemplateSqlEntity and map of key-value pairs.
     *
     * @param entity the TemplateSqlEntity object containing the SQL configuration and content
     * @param map the map of key-value pairs used for replacing expressions in the SQL content
     * @return the SQL content with the expressions replaced by the corresponding values in the map
     */
    private String getSqlContext(TemplateSqlEntity entity, Map<String, String> map)
    {
        try {
            if (ObjectUtils.isNotEmpty(entity.getConfigure())) {
                final String[] content = {entity.getContent()};
                List<LinkedHashMap> configures = JsonUtils.objectmapper.readValue(entity.getConfigure(), List.class);
                map.entrySet()
                        .forEach(value -> {
                            Optional<SqlConfigure> sqlConfigure = configures.stream()
                                    .filter(v -> String.valueOf(v.get("column")).equalsIgnoreCase(value.getKey()))
                                    .map(v -> {
                                        SqlConfigure configure = new SqlConfigure();
                                        configure.setColumn(String.valueOf(v.get("column")));
                                        configure.setType(Type.valueOf(String.valueOf(v.get("type"))));
                                        configure.setExpression(String.valueOf(v.get("expression")));
                                        return configure;
                                    })
                                    .findFirst();
                            if (sqlConfigure.isPresent()) {
                                content[0] = content[0].replace(sqlConfigure.get().getExpression(), String.valueOf(value.getValue()));
                            }
                        });
                return content[0];
            }
        }
        catch (Exception e) {
            log.warn("Failed to convert [ {} ] sql context", entity.getName());
            return entity.getContent();
        }
        return entity.getContent();
    }

    /**
     * Retrieves the text value of a specific node in a JSON object.
     *
     * @param json the JSON object to retrieve the node from
     * @param key the type of node to retrieve
     * @return the text value of the specified node, or null if the node does not exist
     */
    private String getNodeText(Object json, NodeType key)
    {
        ObjectNode objectNode = (ObjectNode) json;
        if (objectNode != null) {
            JsonNode jsonNode = objectNode.get(String.join("_", key.getValue(), "NODE"));
            if (jsonNode != null) {
                return jsonNode.asText();
            }
        }
        return null;
    }

    /**
     * Retrieves a TemplateSqlEntity object based on the given template name and SourceEntity.
     *
     * @param templateName the name of the template to retrieve
     * @param entity the SourceEntity object to use for filtering
     * @return the TemplateSqlEntity object that matches the given criteria
     */
    private TemplateSqlEntity getTemplate(String templateName, SourceEntity entity)
    {
        return templateHandler.findByNameAndPluginContaining(templateName, entity.getType());
    }

    /**
     * Starts the synchronization of the database.
     *
     * @param entity the SourceEntity object representing the entity
     * @param plugin the Plugin object representing the plugin
     * @param databaseCache the Map object representing the database cache
     * @param databaseTableCache the Map object representing the database table cache
     * @param tableCache the Map object representing the table cache
     * @param tableColumnCache the Map object representing the table column cache
     * @param databaseAddedCount the AtomicInteger object representing the count of added databases
     * @param databaseUpdatedCount the AtomicInteger object representing the count of updated databases
     * @param databaseRemovedCount the AtomicInteger object representing the count of removed databases
     * @param tableAddedCount the AtomicInteger object representing the count of added tables
     * @param tableUpdatedCount the AtomicInteger object representing the count of updated tables
     * @param tableRemovedCount the AtomicInteger object representing the count of removed tables
     * @param columnAddedCount the AtomicInteger object representing the count of added columns
     * @param columnUpdatedCount the AtomicInteger object representing the count of updated columns
     * @param columnRemovedCount the AtomicInteger object representing the count of removed columns
     */
    private void startSyncDatabase(SourceEntity entity,
            Plugin plugin,
            Map<String, DatabaseEntity> databaseCache,
            Map<String, List<TableEntity>> databaseTableCache,
            Map<String, TableEntity> tableCache,
            Map<String, List<ColumnEntity>> tableColumnCache,
            AtomicInteger databaseAddedCount,
            AtomicInteger databaseUpdatedCount,
            AtomicInteger databaseRemovedCount,
            AtomicInteger tableAddedCount,
            AtomicInteger tableUpdatedCount,
            AtomicInteger tableRemovedCount,
            AtomicInteger columnAddedCount,
            AtomicInteger columnUpdatedCount,
            AtomicInteger columnRemovedCount)
    {
        String templateName = "SYSTEM_FOR_GET_ALL_DATABASES";
        TemplateSqlEntity template = getTemplate(templateName, entity);
        if (template == null) {
            log.warn("The scheduled task [ {} ] source [ {} ] protocol [ {} ] template [ {} ] is not available, skip sync database", this.getName(), entity.getType(), entity.getProtocol(), templateName);
        }
        else {
            plugin.connect(entity.toConfigure());
            Response response = plugin.execute(getSqlContext(template, null));
            if (!response.getIsSuccessful()) {
                log.error("The scheduled task [ {} ] source [ {} ] protocol [ {} ] sync metadata  [ {} ] failed", this.getName(), entity.getType(), entity.getProtocol(), response.getMessage());
            }
            else {
                List<DatabaseEntity> origin = databaseHandler.findAllBySource(entity);
                List<DatabaseEntity> entities = response.getColumns()
                        .stream()
                        .map(item -> {
                            DatabaseEntity database = DatabaseEntity.builder()
                                    .name(getNodeText(item, NodeType.SCHEMA))
                                    .catalog(getNodeText(item, NodeType.CATALOG))
                                    .description(String.format("[ %s ] of [ %s ]", getNodeText(item, NodeType.SCHEMA), item, getNodeText(item, NodeType.CATALOG)))
                                    .source(entity)
                                    .build();
                            Optional<DatabaseEntity> optionalDatabase = origin.stream()
                                    .filter(node -> node.getName().equals(database.getName()))
                                    .findAny();
                            if (optionalDatabase.isPresent()) {
                                database.setId(optionalDatabase.get().getId());
                                database.setCreateTime(optionalDatabase.get().getCreateTime());
                                databaseUpdatedCount.addAndGet(1);
                            }
                            else {
                                databaseAddedCount.addAndGet(1);
                            }
                            return database;
                        })
                        .collect(Collectors.toList());
                // Write the new data retrieved to the database
                log.info("Added database size [ {} ] to source [ {} ]", entities.size(), entity.getName());
                databaseHandler.saveAll(entities);
                entities.forEach(item -> {
                    String key = String.format("%s_%s", item.getCatalog(), item.getName());
                    databaseCache.put(key, item);
                    databaseTableCache.put(key, this.tableHandler.findSimpleAllByDatabase(item));
                });
                // Delete invalid data that no longer exists
                List<DatabaseEntity> deleteEntities = origin.stream()
                        .filter(node -> entities.stream().noneMatch(item -> node.getName().equals(item.getName())))
                        .collect(Collectors.toList());
                log.info("Removed database size [ {} ] from source [ {} ]", deleteEntities.size(), entity.getName());
                databaseHandler.deleteAll(deleteEntities);
                databaseRemovedCount.addAndGet(deleteEntities.size());
            }
            this.startSyncTable(entity,
                    plugin,
                    databaseCache,
                    databaseTableCache,
                    tableCache,
                    tableColumnCache,
                    tableAddedCount,
                    tableUpdatedCount,
                    tableRemovedCount,
                    columnAddedCount,
                    columnUpdatedCount,
                    columnRemovedCount);
        }
    }

    /**
     * Starts the synchronization of a table.
     *
     * @param entity the source entity
     * @param plugin the plugin
     * @param databaseCache the database cache
     * @param databaseTableCache the database table cache
     * @param tableCache the table cache
     * @param tableColumnCache the table column cache
     * @param tableAddedCount the table added count
     * @param tableUpdatedCount the table updated count
     * @param tableRemovedCount the table removed count
     * @param columnAddedCount the column added count
     * @param columnUpdatedCount the column updated count
     * @param columnRemovedCount the column removed count
     */
    private void startSyncTable(SourceEntity entity,
            Plugin plugin,
            Map<String, DatabaseEntity> databaseCache,
            Map<String, List<TableEntity>> databaseTableCache,
            Map<String, TableEntity> tableCache,
            Map<String, List<ColumnEntity>> tableColumnCache,
            AtomicInteger tableAddedCount,
            AtomicInteger tableUpdatedCount,
            AtomicInteger tableRemovedCount,
            AtomicInteger columnAddedCount,
            AtomicInteger columnUpdatedCount,
            AtomicInteger columnRemovedCount)
    {
        String templateName = "SYSTEM_FOR_GET_ALL_TABLES";
        TemplateSqlEntity template = getTemplate(templateName, entity);
        if (template == null) {
            log.warn("The scheduled task [ {} ] source [ {} ] protocol [ {} ] template [ {} ] is not available, skip sync table", this.getName(), entity.getType(), entity.getProtocol(), templateName);
        }
        else {
            plugin.connect(entity.toConfigure());
            Response response = plugin.execute(getSqlContext(template, null));
            if (!response.getIsSuccessful()) {
                log.error("The scheduled task [ {} ] source [ {} ] protocol [ {} ] sync metadata tables  [ {} ] failed", this.getName(), entity.getType(), entity.getProtocol(), response.getMessage());
            }
            else {
                List<TableEntity> entities = response.getColumns()
                        .stream()
                        .map(item -> {
                            String key = String.format("%s_%s", getNodeText(item, NodeType.CATALOG), getNodeText(item, NodeType.SCHEMA));
                            DatabaseEntity database = databaseCache.get(key);
                            String name = getNodeText(item, NodeType.TABLE);
                            TableEntity table = TableEntity.builder()
                                    .name(name)
                                    .description(String.format("Table [ %s ] of database [ %s ] ", name, getNodeText(item, NodeType.SCHEMA)))
                                    .type(getNodeText(item, NodeType.TYPE))
                                    .engine(getNodeText(item, NodeType.ENGINE))
                                    .format(getNodeText(item, NodeType.FORMAT))
                                    .inCreateTime(getNodeText(item, NodeType.CREATE_TIME))
                                    .inUpdateTime(getNodeText(item, NodeType.UPDATE_TIME))
                                    .collation(getNodeText(item, NodeType.COLLATION))
                                    .rows(getNodeText(item, NodeType.ROWS))
                                    .comment(getNodeText(item, NodeType.COMMENT))
                                    .avgRowLength(getNodeText(item, NodeType.AVG_ROW))
                                    .dataLength(getNodeText(item, NodeType.DATA))
                                    .indexLength(getNodeText(item, NodeType.INDEX))
                                    .autoIncrement(getNodeText(item, NodeType.AUTO_INCREMENT))
                                    .database(database)
                                    .build();
                            return table;
                        })
                        .collect(Collectors.toList());

                Map<String, List<TableEntity>> groupEntities = entities
                        .stream()
                        .collect(Collectors.groupingBy(item -> String.format("%s_%s", item.getDatabase().getCatalog(), item.getDatabase().getName())));

                groupEntities.forEach((key, groupItem) -> {
                    // Detect data that needs to be updated
                    List<TableEntity> origin = databaseTableCache.get(key);
                    groupItem.forEach(item -> {
                        Optional<TableEntity> optionalTable = origin.stream()
                                .filter(node -> node.getName().equals(item.getName()))
                                .findAny();
                        if (optionalTable.isPresent()) {
                            TableEntity node = optionalTable.get();
                            item.setId(node.getId());
                            item.setCreateTime(node.getCreateTime());
                            tableUpdatedCount.addAndGet(1);
                        }
                        else {
                            tableAddedCount.addAndGet(1);
                        }
                    });

                    log.info("Added table size [ {} ] to database [ {} ]", groupItem.size(), key);
                    tableHandler.saveAll(groupItem);
                    groupItem.forEach(item -> {
                        String tableCacheKey = String.format("%s_%s", item.getDatabase().getName(), item.getName());
                        tableCache.put(tableCacheKey, item);
                        tableColumnCache.put(tableCacheKey, this.columnHandler.findSimpleAllByTable(item));
                    });

                    List<TableEntity> deleteEntities = origin.stream()
                            .filter(node -> groupItem.stream().noneMatch(item -> node.getName().equals(item.getName())))
                            .collect(Collectors.toList());
                    log.info("Removed table size [ {} ] from database [ {} ]", deleteEntities.size(), key);
                    tableHandler.deleteAll(deleteEntities);
                    tableRemovedCount.addAndGet(deleteEntities.size());
                });

                this.startSyncColumn(entity, plugin, tableCache, tableColumnCache, columnAddedCount, columnUpdatedCount, columnRemovedCount);
            }
        }
    }

    /**
     * Synchronizes the columns of a source entity with the corresponding table in the database.
     *
     * @param entity the source entity to sync the columns for
     * @param plugin the plugin to connect to the database
     * @param tableCache a cache of table entities for efficient lookup
     * @param tableColumnCache a cache of column entities for efficient lookup
     * @param columnAddedCount an atomic counter for tracking the number of columns added
     * @param columnUpdatedCount an atomic counter for tracking the number of columns updated
     * @param columnRemovedCount an atomic counter for tracking the number of columns removed
     */
    private void startSyncColumn(SourceEntity entity,
            Plugin plugin,
            Map<String, TableEntity> tableCache,
            Map<String, List<ColumnEntity>> tableColumnCache,
            AtomicInteger columnAddedCount,
            AtomicInteger columnUpdatedCount,
            AtomicInteger columnRemovedCount)
    {
        String templateName = "SYSTEM_FOR_GET_ALL_COLUMNS";
        TemplateSqlEntity template = getTemplate(templateName, entity);
        if (template == null) {
            log.warn("The scheduled task [ {} ] source [ {} ] protocol [ {} ] template [ {} ] is not available, skip sync column", this.getName(), entity.getType(), entity.getProtocol(), templateName);
        }
        else {
            plugin.connect(entity.toConfigure());
            Response response = plugin.execute(getSqlContext(template, null));
            if (!response.getIsSuccessful()) {
                log.error("The scheduled task [ {} ] source [ {} ] protocol [ {} ] sync metadata columns  [ {} ] failed", this.getName(), entity.getType(), entity.getProtocol(), response.getMessage());
            }
            else {
                List<ColumnEntity> entities = response.getColumns()
                        .stream()
                        .map(item -> {
                            String key = String.format("%s_%s", getNodeText(item, NodeType.CATALOG), getNodeText(item, NodeType.SCHEMA));
                            TableEntity table = tableCache.get(key);
                            String name = getNodeText(item, NodeType.COLUMN);
                            ColumnEntity column = ColumnEntity.builder()
                                    .name(name)
                                    .description(String.format("Table [ %s ] of column [ %s ] ", table.getName(), name))
                                    .type(getNodeText(item, NodeType.COLUMN_TYPE))
                                    .comment(getNodeText(item, NodeType.COMMENT))
                                    .defaultValue(getNodeText(item, NodeType.DEFAULT))
                                    .position(getNodeText(item, NodeType.POSITION))
                                    .maximumLength(getNodeText(item, NodeType.MAXIMUM_LENGTH))
                                    .collation(getNodeText(item, NodeType.COLLATION))
                                    .isKey(getNodeText(item, NodeType.KEY))
                                    .privileges(getNodeText(item, NodeType.FORMAT))
                                    .dataType(getNodeText(item, NodeType.DATA_TYPE))
                                    .extra(getNodeText(item, NodeType.EXTRA))
                                    .isNullable(getNodeText(item, NodeType.NULLABLE))
                                    .table(table)
                                    .build();
                            return column;
                        })
                        .collect(Collectors.toList());

                Map<String, List<ColumnEntity>> groupEntities = entities
                        .stream()
                        .collect(Collectors.groupingBy(item -> String.format("%s_%s", item.getTable().getDatabase().getName(), item.getTable().getName())));

                groupEntities.forEach((key, groupItem) -> {
                    // Detect data that needs to be updated
                    List<ColumnEntity> origin = tableColumnCache.get(key);
                    groupItem.forEach(item -> {
                        Optional<ColumnEntity> optionalColumn = origin.stream()
                                .filter(node -> node.getName().equals(item.getName()))
                                .findAny();
                        if (optionalColumn.isPresent()) {
                            ColumnEntity node = optionalColumn.get();
                            item.setId(node.getId());
                            item.setCreateTime(node.getCreateTime());
                            columnUpdatedCount.addAndGet(1);
                        }
                        else {
                            columnAddedCount.addAndGet(1);
                        }
                    });

                    log.info("Added column size [ {} ] to table [ {} ]", groupItem.size(), key);
                    columnHandler.saveAll(groupItem);

                    List<ColumnEntity> deleteEntities = origin.stream()
                            .filter(node -> groupItem.stream().noneMatch(item -> node.getName().equals(item.getName())))
                            .collect(Collectors.toList());
                    log.info("Removed column size [ {} ] from table [ {} ]", deleteEntities.size(), key);
                    columnHandler.deleteAll(deleteEntities);
                    columnRemovedCount.addAndGet(deleteEntities.size());
                });
            }
        }
    }
}
