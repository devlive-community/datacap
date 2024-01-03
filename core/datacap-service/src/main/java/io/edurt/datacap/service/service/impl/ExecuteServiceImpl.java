package io.edurt.datacap.service.service.impl;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import io.edurt.datacap.common.enums.ServiceState;
import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.common.sql.SqlBuilder;
import io.edurt.datacap.common.sql.configure.SqlBody;
import io.edurt.datacap.common.sql.configure.SqlType;
import io.edurt.datacap.parser.ParserResponse;
import io.edurt.datacap.parser.SqlParser;
import io.edurt.datacap.parser.type.StatementType;
import io.edurt.datacap.service.audit.AuditPlugin;
import io.edurt.datacap.service.body.ExecuteDslBody;
import io.edurt.datacap.service.common.PluginUtils;
import io.edurt.datacap.service.entity.ExecuteEntity;
import io.edurt.datacap.service.entity.SourceEntity;
import io.edurt.datacap.service.initializer.InitializerConfigure;
import io.edurt.datacap.service.repository.SourceRepository;
import io.edurt.datacap.service.service.ExecuteService;
import io.edurt.datacap.spi.Plugin;
import io.edurt.datacap.spi.model.Configure;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Optional;
import java.util.Set;

@Service
public class ExecuteServiceImpl
        implements ExecuteService
{
    private final Injector injector;
    private final SourceRepository sourceRepository;
    private final Environment environment;
    private final InitializerConfigure initializerConfigure;

    public ExecuteServiceImpl(Injector injector, SourceRepository sourceRepository, Environment environment, InitializerConfigure initializerConfigure)
    {
        this.injector = injector;
        this.sourceRepository = sourceRepository;
        this.environment = environment;
        this.initializerConfigure = initializerConfigure;
    }

    @AuditPlugin
    @Override
    public CommonResponse<Object> execute(ExecuteEntity configure)
    {
        Optional<SourceEntity> entityOptional = this.sourceRepository.findById(Long.valueOf(configure.getName()));
        if (!entityOptional.isPresent()) {
            return CommonResponse.failure(ServiceState.SOURCE_NOT_FOUND);
        }

        SourceEntity entity = entityOptional.get();
        Optional<Plugin> pluginOptional = PluginUtils.getPluginByNameAndType(this.injector, entity.getType(), entity.getProtocol());
        if (!pluginOptional.isPresent()) {
            return CommonResponse.failure(ServiceState.PLUGIN_NOT_FOUND);
        }

        Configure _configure = new Configure();
        Plugin plugin = pluginOptional.get();
        _configure.setHost(entity.getHost());
        _configure.setPort(entity.getPort());
        _configure.setUsername(Optional.ofNullable(entity.getUsername()));
        _configure.setPassword(Optional.ofNullable(entity.getPassword()));
        Optional<String> _database = StringUtils.isNotEmpty(entity.getDatabase()) ? Optional.ofNullable(entity.getDatabase()) : Optional.empty();
        _configure.setDatabase(_database);
        _configure.setSsl(Optional.ofNullable(entity.getSsl()));
        _configure.setEnv(Optional.ofNullable(entity.getConfigures()));
        _configure.setFormat(configure.getFormat());
        _configure.setUsedConfig(entity.isUsedConfig());
        if (entity.isUsedConfig()) {
            _configure.setUsername(Optional.of(entity.getUser().getUsername()));
            String configHome = environment.getProperty("datacap.config.data");
            if (StringUtils.isEmpty(configHome)) {
                configHome = String.join(File.separator, System.getProperty("user.dir"), "config");
            }
            _configure.setHome(configHome);
            _configure.setId(String.valueOf(entity.getId()));
        }
        plugin.connect(_configure);

        if (initializerConfigure.getAutoLimit()) {
            Optional<SqlParser> parserOptional = this.injector.getInstance(Key.get(new TypeLiteral<Set<SqlParser>>() {}))
                    .stream()
                    .filter(parser -> parser.name().equalsIgnoreCase(plugin.name()))
                    .findFirst();
            ParserResponse response = parserOptional.orElse(injector.getInstance(Key.get(new TypeLiteral<Set<SqlParser>>() {}))
                            .stream()
                            .filter(parser -> parser.name().equalsIgnoreCase(initializerConfigure.getSqlParserDefaultEngine())).findFirst().get())
                    .parse(configure.getContent());

            if (response.isParser() && response.getType().equals(StatementType.SELECT) && response.getTable().getLimit() == null) {
                configure.setContent(String.format("%s%nLIMIT %s", configure.getContent(), configure.getLimit()));
            }
        }

        io.edurt.datacap.spi.model.Response response = plugin.execute(configure.getContent());
        response.setContent(configure.getContent());
        plugin.destroy();
        if (response.getIsSuccessful()) {
            return CommonResponse.success(response);
        }
        return CommonResponse.failure(ServiceState.PLUGIN_EXECUTE_FAILED, response.getMessage());
    }

    @Override
    public CommonResponse<Object> execute(ExecuteDslBody configure)
    {
        ExecuteEntity executeEntity = new ExecuteEntity();
        executeEntity.setEnv(configure.getEnv());
        executeEntity.setName(configure.getName());
        executeEntity.setFormat(configure.getFormat());
        SqlBody body = configure.getConfigure();
        body.setType(SqlType.SELECT);
        SqlBuilder builder = new SqlBuilder(body);
        executeEntity.setContent(builder.getSql());
        return this.execute(executeEntity);
    }
}
