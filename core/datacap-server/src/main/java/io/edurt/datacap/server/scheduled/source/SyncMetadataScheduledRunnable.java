package io.edurt.datacap.server.scheduled.source;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Maps;
import com.google.inject.Injector;
import io.edurt.datacap.common.enums.NodeType;
import io.edurt.datacap.common.enums.Type;
import io.edurt.datacap.common.utils.JsonUtils;
import io.edurt.datacap.schedule.ScheduledRunnable;
import io.edurt.datacap.service.common.PluginUtils;
import io.edurt.datacap.service.entity.SourceEntity;
import io.edurt.datacap.service.entity.TemplateSqlEntity;
import io.edurt.datacap.service.entity.metadata.DatabaseEntity;
import io.edurt.datacap.service.entity.metadata.TableEntity;
import io.edurt.datacap.service.itransient.SqlConfigure;
import io.edurt.datacap.service.repository.SourceRepository;
import io.edurt.datacap.service.repository.TemplateSqlRepository;
import io.edurt.datacap.service.repository.metadata.ColumnRepository;
import io.edurt.datacap.service.repository.metadata.DatabaseRepository;
import io.edurt.datacap.service.repository.metadata.TableRepository;
import io.edurt.datacap.spi.Plugin;
import io.edurt.datacap.spi.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class SyncMetadataScheduledRunnable
        extends ScheduledRunnable
{
    private final Injector injector;
    private final SourceRepository sourceHandler;
    private final DatabaseRepository databaseHandler;
    private final TableRepository tableHandler;
    private final ColumnRepository columnHandler;
    private final TemplateSqlRepository templateHandler;

    public SyncMetadataScheduledRunnable(String name, Injector injector, SourceRepository sourceHandler, DatabaseRepository databaseHandler, TableRepository tableHandler, ColumnRepository columnHandler, TemplateSqlRepository templateHandler)
    {
        super(name);
        this.injector = injector;
        this.sourceHandler = sourceHandler;
        this.databaseHandler = databaseHandler;
        this.tableHandler = tableHandler;
        this.columnHandler = columnHandler;
        this.templateHandler = templateHandler;
    }

    /**
     * Executes the run method of the Runnable interface.
     */
    @Override
    public void run()
    {
        sourceHandler.findAll()
                .forEach(entity -> {
                    log.info("==================== Sync metadata  [ {} ] started =================", entity.getName());
                    Optional<Plugin> pluginOptional = PluginUtils.getPluginByNameAndType(this.injector, entity.getType(), entity.getProtocol());
                    if (!pluginOptional.isPresent()) {
                        log.warn("The scheduled task [ {} ] source [ {} ] protocol [ {} ] is not available", this.getName(), entity.getType(), entity.getProtocol());
                    }
                    else {
                        this.startSyncDatabase(entity, pluginOptional.get());
                    }
                    log.info("==================== Sync metadata  [ {} ] finished =================", entity.getName());
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
     * Starts the synchronization of the database.
     *
     * @param entity the source entity to sync
     * @param plugin the plugin used for synchronization
     */
    private void startSyncDatabase(SourceEntity entity, Plugin plugin)
    {
        String templateName = "SYSTEM_FOR_GET_ALL_DATABASES";
        TemplateSqlEntity template = templateHandler.findByNameAndPluginContaining(templateName, entity.getType());
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
                List<Map<NodeType, String>> maps = response.getColumns()
                        .stream()
                        .map(item -> {
                            Map<NodeType, String> map = Maps.newConcurrentMap();
                            String schema = getNodeText(item, NodeType.SCHEMA);
                            map.put(NodeType.SCHEMA, schema);
                            String catalog = getNodeText(item, NodeType.CATALOG);
                            map.put(NodeType.CATALOG, catalog);
                            String charset = getNodeText(item, NodeType.CHARSET);
                            map.put(NodeType.CHARSET, charset);
                            return map;
                        })
                        .collect(Collectors.toList());
                // Write the new data retrieved to the database
                maps.stream()
                        .filter(item -> {
                            Optional<DatabaseEntity> optionalDatabase = origin.stream()
                                    .filter(node -> node.getName().equals(item.get(NodeType.SCHEMA)) && node.getCatalog().equals(item.get(NodeType.CATALOG)))
                                    .findAny();
                            if (optionalDatabase.isPresent()) {
                                this.startSyncTable(entity, optionalDatabase.get(), plugin);
                                return false;
                            }
                            else {
                                return true;
                            }
                        })
                        .forEach(item -> {
                            DatabaseEntity database = DatabaseEntity.builder()
                                    .name(item.get(NodeType.SCHEMA))
                                    .catalog(item.get(NodeType.CATALOG))
                                    .description(String.format("[ %s ] of [ %s ] charset [ %s ]", item.get(NodeType.SCHEMA), item.get(NodeType.CATALOG), item.get(NodeType.CHARSET)))
                                    .source(entity)
                                    .build();
                            log.info("Added catalog [ {} ] schema [ {} ] to source [ {} ]", database.getCatalog(), database.getName(), entity.getName());
                            databaseHandler.save(database);
                            log.info("==================== Sync metadata table  [ {} ] started =================", database.getName());
                            this.startSyncTable(entity, database, plugin);
                            log.info("==================== Sync metadata table  [ {} ] end =================", database.getName());
                        });
                // Delete invalid data that no longer exists
                origin.stream()
                        .filter(node -> maps.stream().noneMatch(item -> node.getName().equals(item.get(NodeType.SCHEMA)) && node.getCatalog().equals(item.get(NodeType.CATALOG))))
                        .forEach(item -> {
                            log.info("Removed catalog [ {} ] schema [ {} ] to source [ {} ]", item.getCatalog(), item.getName(), entity.getName());
                            databaseHandler.delete(item);
                        });
            }
        }
    }

    private void startSyncTable(SourceEntity entity, DatabaseEntity database, Plugin plugin)
    {
        String templateName = "SYSTEM_FOR_GET_ALL_TABLES";
        TemplateSqlEntity template = templateHandler.findByNameAndPluginContaining(templateName, entity.getType());
        if (template == null) {
            log.warn("The scheduled task [ {} ] source [ {} ] protocol [ {} ] template [ {} ] is not available, skip sync table", this.getName(), entity.getType(), entity.getProtocol(), templateName);
        }
        else {
            plugin.connect(entity.toConfigure());
            Map<String, String> configure = Maps.newConcurrentMap();
            configure.put("database", database.getName());
            Response response = plugin.execute(getSqlContext(template, configure));
            if (!response.getIsSuccessful()) {
                log.error("The scheduled task [ {} ] source [ {} ] protocol [ {} ] sync metadata tables  [ {} ] failed", this.getName(), entity.getType(), entity.getProtocol(), response.getMessage());
            }
            else {
                List<TableEntity> origin = tableHandler.findAllByDatabase(database);
                List<TableEntity> maps = response.getColumns()
                        .stream()
                        .map(item -> {
                            String name = getNodeText(item, NodeType.TABLE);
                            return TableEntity.builder()
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
                                    .database(database)
                                    .build();
                        })
                        .collect(Collectors.toList());
                maps.stream()
                        .filter(item -> {
                            Optional<TableEntity> optionalTable = origin.stream()
                                    .filter(node -> node.getName().equals(item.getName()))
                                    .findAny();
                            if (optionalTable.isPresent()) {
                                return false;
                            }
                            else {
                                return true;
                            }
                        })
                        .forEach(item -> {
                            log.info("Added table [ {} ] engine [ {} ] to database [ {} ]", item.getName(), item.getEngine(), database.getName());
                            tableHandler.save(item);
                        });
                origin.stream()
                        .filter(node -> maps.stream().noneMatch(item -> node.getName().equals(item.getName())))
                        .forEach(item -> {
                            log.info("Removed table [ {} ] engine [ {} ] to database [ {} ]", item.getName(), item.getEngine(), database.getName());
                            tableHandler.delete(item);
                        });
            }
        }
    }
}
