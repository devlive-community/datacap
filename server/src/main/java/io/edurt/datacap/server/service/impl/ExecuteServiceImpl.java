package io.edurt.datacap.server.service.impl;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import io.edurt.datacap.server.audit.AuditPlugin;
import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.common.ServiceState;
import io.edurt.datacap.server.entity.ExecuteEntity;
import io.edurt.datacap.server.entity.SourceEntity;
import io.edurt.datacap.server.repository.SourceRepository;
import io.edurt.datacap.server.service.ExecuteService;
import io.edurt.datacap.spi.Plugin;
import io.edurt.datacap.spi.model.Configure;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

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
        SourceEntity entity = this.sourceRepository.findByName(configure.getName());
        if (ObjectUtils.isEmpty(entity)) {
            return Response.failure(ServiceState.SOURCE_NOT_FOUND);
        }

        Optional<Plugin> pluginOptional = this.injector.getInstance(Key.get(new TypeLiteral<Set<Plugin>>() {}))
                .stream()
                .filter(plugin -> plugin.name().equalsIgnoreCase(entity.getType()))
                .findFirst();
        if (!pluginOptional.isPresent()) {
            return Response.failure(ServiceState.PLUGIN_NOT_FOUND);
        }

        Configure _configure = new Configure();
        Plugin plugin = pluginOptional.get();
        _configure.setHost(entity.getHost());
        _configure.setPort(entity.getPort());
        _configure.setUsername(Optional.ofNullable(entity.getUsername()));
        _configure.setPassword(Optional.ofNullable(entity.getPassword()));
        _configure.setDatabase(Optional.ofNullable(entity.getDatabase()));
        _configure.setEnv(Optional.ofNullable(configure.getEnv()));
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
