package io.edurt.datacap.server.service.impl;

import com.google.inject.Injector;
import io.edurt.datacap.server.audit.AuditPlugin;
import io.edurt.datacap.server.common.PluginCommon;
import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.common.ServiceState;
import io.edurt.datacap.server.entity.ExecuteEntity;
import io.edurt.datacap.server.entity.SourceEntity;
import io.edurt.datacap.server.repository.SourceRepository;
import io.edurt.datacap.server.service.ExecuteService;
import io.edurt.datacap.spi.Plugin;
import io.edurt.datacap.spi.model.Configure;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExecuteServiceImpl
        implements ExecuteService
{
    private final Injector injector;
    private final SourceRepository sourceRepository;

    public ExecuteServiceImpl(Injector injector, SourceRepository sourceRepository)
    {
        this.injector = injector;
        this.sourceRepository = sourceRepository;
    }

    @AuditPlugin
    @Override
    public Response<Object> execute(ExecuteEntity configure)
    {
        Optional<SourceEntity> entityOptional = this.sourceRepository.findById(Long.valueOf(configure.getName()));
        if (!entityOptional.isPresent()) {
            return Response.failure(ServiceState.SOURCE_NOT_FOUND);
        }

        SourceEntity entity = entityOptional.get();
        Optional<Plugin> pluginOptional = PluginCommon.getPluginByName(this.injector, entity.getType());
        if (!pluginOptional.isPresent()) {
            return Response.failure(ServiceState.PLUGIN_NOT_FOUND);
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
        plugin.connect(_configure);
        io.edurt.datacap.spi.model.Response response = plugin.execute(configure.getContent());
        plugin.destroy();
        if (response.getIsSuccessful()) {
            return Response.success(response);
        }
        return Response.failure(ServiceState.PLUGIN_EXECUTE_FAILED, response.getMessage());
    }
}
