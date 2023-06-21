package io.edurt.datacap.server.scheduled;

import com.google.inject.Injector;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.common.enums.Type;
import io.edurt.datacap.common.utils.JsonUtils;
import io.edurt.datacap.schedule.ScheduledRunnable;
import io.edurt.datacap.service.common.PluginUtils;
import io.edurt.datacap.service.entity.SourceEntity;
import io.edurt.datacap.service.entity.TemplateSqlEntity;
import io.edurt.datacap.service.itransient.SqlConfigure;
import io.edurt.datacap.service.repository.SourceRepository;
import io.edurt.datacap.service.repository.TemplateSqlRepository;
import io.edurt.datacap.spi.Plugin;
import io.edurt.datacap.spi.model.Configure;
import io.edurt.datacap.spi.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@SuppressFBWarnings(value = "REC_CATCH_EXCEPTION")
public class SourceScheduledRunnable
        extends ScheduledRunnable
{
    public static final String DATABASE = "database";
    public static final String TABLE = "table";
    private static final String GET_ALL_DATABASES = "getAllDatabase";
    private static final String GET_ALL_TABLES = "getAllTablesFromDatabase";
    private static final String GET_ALL_COLUMNS = "getAllColumnsFromDatabaseAndTable";

    private final Injector injector;
    private final SourceRepository sourceRepository;
    private final TemplateSqlRepository templateSqlRepository;
    private final RedisTemplate redisTemplate;
    private final Environment environment;

    private int maxSuggestions;

    public SourceScheduledRunnable(String name, Injector injector, SourceRepository sourceRepository, TemplateSqlRepository templateSqlRepository, RedisTemplate redisTemplate, Environment environment)
    {
        super(name);
        this.injector = injector;
        this.sourceRepository = sourceRepository;
        this.templateSqlRepository = templateSqlRepository;
        this.redisTemplate = redisTemplate;
        this.environment = environment;
        this.maxSuggestions = Integer.parseInt(environment.getProperty("datacap.editor.sugs.maxSize"));
    }

    @Override
    public void run()
    {
        this.sourceRepository.findAll().forEach(entity -> {
            log.info("==================== {} ---> {} started =================", this.getName(), entity.getName());
            Optional<Plugin> pluginOptional = PluginUtils.getPluginByNameAndType(this.injector, entity.getType(), entity.getProtocol());
            if (!pluginOptional.isPresent()) {
                log.warn("The scheduled task <{}> source {} protocol {} is not available", this.getName(), entity.getType(), entity.getProtocol());
            }
            else {
                long startTime = System.currentTimeMillis();
                String name = String.format("%s_%s", entity.getName(), entity.getId());
                log.info("The scheduled task <{}> execution start - child：{} start with {}", this.getName(), name, startTime);
                // Get all databases
                TemplateSqlEntity getAllDatabaseEntity = this.templateSqlRepository.findByNameAndPluginContaining(GET_ALL_DATABASES, entity.getType());
                if (ObjectUtils.isEmpty(getAllDatabaseEntity)) {
                    log.warn("The scheduled task {} template {} is not available", this.getName(), entity.getName(), GET_ALL_DATABASES);
                }
                else {
                    // Clear redis cache
                    String key = String.join("_", entity.getType(), entity.getId().toString());
                    long counter = redisTemplate.opsForSet().size(key);
                    redisTemplate.delete(key);
                    log.info("The scheduled task {} child {} type {} find keys counter: {} is cleaned", this.getName(), entity.getName(), entity.getType(), counter);
                    this.processDatabase(entity, getContent(getAllDatabaseEntity, null), pluginOptional.get(), DATABASE, key);
                }
                long endTime = System.currentTimeMillis();
                long times = endTime - startTime;
                log.info("The scheduled task <{}> execution end - child：{} end with {} consuming：{} millisecond", this.getName(), name, endTime, times);
            }
            log.info("==================== {} ---> {} end =================", this.getName(), entity.getName());
        });
    }

    private Configure getConfigure(SourceEntity entity)
    {
        Configure configure = new Configure();
        configure.setHost(entity.getHost());
        configure.setPort(entity.getPort());
        configure.setUsername(Optional.ofNullable(entity.getUsername()));
        configure.setPassword(Optional.ofNullable(entity.getPassword()));
        Optional<String> database = StringUtils.isNotEmpty(entity.getDatabase()) ? Optional.ofNullable(entity.getDatabase()) : Optional.empty();
        configure.setDatabase(database);
        configure.setSsl(Optional.ofNullable(entity.getSsl()));
        configure.setEnv(Optional.ofNullable(entity.getConfigures()));
        return configure;
    }

    private String getContent(TemplateSqlEntity entity, Map<String, String> configure)
    {
        try {
            if (ObjectUtils.isNotEmpty(entity.getConfigure())) {
                final String[] content = {entity.getContent()};
                List<LinkedHashMap> configures = JsonUtils.objectmapper.readValue(entity.getConfigure(), List.class);
                configure.entrySet().forEach(value -> {
                    Optional<SqlConfigure> sqlConfigure = configures.stream().filter(v -> String.valueOf(v.get("column")).equalsIgnoreCase(value.getKey())).map(v -> {
                        SqlConfigure configure1 = new SqlConfigure();
                        configure1.setColumn(v.get("column").toString());
                        configure1.setType(Type.valueOf(String.valueOf(v.get("type"))));
                        configure1.setExpression(String.valueOf(v.get("expression")));
                        return configure1;
                    }).findFirst();
                    if (sqlConfigure.isPresent()) {
                        content[0] = content[0].replace(sqlConfigure.get().getExpression(), String.valueOf(value.getValue()));
                    }
                });
                return content[0];
            }
        }
        catch (Exception e) {
            log.warn("Failed to analysis");
            return entity.getContent();
        }
        return entity.getContent();
    }

    /**
     * Get all databases of the configuration data source and buffer them to Redis
     */
    private void processDatabase(SourceEntity entity, String content, Plugin plugin, String type, String key)
    {
        plugin.connect(getConfigure(entity));
        Response response = plugin.execute(content);
        if (response.getIsSuccessful()) {
            TemplateSqlEntity getAllTableEntity = templateSqlRepository.findByNameAndPluginContaining(GET_ALL_TABLES, entity.getType());
            if (ObjectUtils.isEmpty(getAllTableEntity)) {
                log.warn("The scheduled task {} template {} is not available", this.getName(), entity.getName(), GET_ALL_TABLES);
            }
            else {
                response.getColumns()
                        .stream()
                        .limit(maxSuggestions)
                        .forEach(column -> {
                            String database = ((List<String>) column).get(0);
                            log.info("The scheduled task {} child {} sync data from source : {}", this.getName(), entity.getName(), database);
                            Map<String, String> configure = new HashMap<>();
                            configure.put("database", database);
                            this.processTable(entity, this.getContent(getAllTableEntity, configure), database, plugin, key);
                        });
            }
        }
        else {
            log.warn("The scheduled task {} child {} type {} sync data from source is failed {}", this.getName(), entity.getName(), type, response.getMessage());
        }
        plugin.destroy();
    }

    private void processTable(SourceEntity entity, String content, String database, Plugin plugin, String key)
    {
        plugin.connect(getConfigure(entity));
        Response response = plugin.execute(content);
        if (response.getIsSuccessful()) {
            TemplateSqlEntity getAllChildEntity = templateSqlRepository.findByNameAndPluginContaining(GET_ALL_COLUMNS, entity.getType());
            if (ObjectUtils.isEmpty(getAllChildEntity)) {
                log.warn("The scheduled task {} template {} is not available", this.getName(), entity.getName(), GET_ALL_COLUMNS);
            }
            else {
                response.getColumns()
                        .stream()
                        .limit(maxSuggestions)
                        .forEach(column -> {
                            String table = ((List<String>) column).get(0);
                            log.info("The scheduled task {} child {} database {} sync data from source is : {}", this.getName(), entity.getName(), database, table);
                            Map<String, String> configure = new HashMap<>();
                            configure.put("table", String.join(".", database, table));
                            this.processColumn(entity, this.getContent(getAllChildEntity, configure), database, table, plugin, key);
                        });
            }
        }
        else {
            log.warn("The scheduled task {} child {} database {} sync data from source is failed {}", this.getName(), entity.getName(), database, response.getMessage());
        }
        plugin.destroy();
    }

    private void processColumn(SourceEntity entity, String content, String database, String table, Plugin plugin, String key)
    {
        plugin.connect(getConfigure(entity));
        Response response = plugin.execute(content);
        if (response.getIsSuccessful()) {
            TemplateSqlEntity getAllChildEntity = templateSqlRepository.findByNameAndPluginContaining(GET_ALL_COLUMNS, entity.getType());
            if (ObjectUtils.isEmpty(getAllChildEntity)) {
                log.warn("The scheduled task {} template {} is not available", this.getName(), entity.getName(), GET_ALL_COLUMNS);
            }
            else {
                response.getColumns()
                        .stream()
                        .limit(maxSuggestions)
                        .forEach(column -> {
                            String value = ((List<String>) column).get(0);
                            log.info("The scheduled task {} child {} database {} table {} sync data from source is : {}", this.getName(), entity.getName(), database, table, value);
                            redisTemplate.opsForSet().add(key, String.join(".", database, table, value));
                        });
            }
        }
        else {
            log.warn("The scheduled task {} child {} database {} table {} sync data from source is failed {}", this.getName(), entity.getName(), database, table, response.getMessage());
        }
        plugin.destroy();
    }
}
