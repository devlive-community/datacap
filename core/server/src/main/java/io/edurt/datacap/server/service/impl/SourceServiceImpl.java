package io.edurt.datacap.server.service.impl;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import io.edurt.datacap.server.adapter.PageRequestAdapter;
import io.edurt.datacap.server.body.SharedSourceBody;
import io.edurt.datacap.server.body.SourceBody;
import io.edurt.datacap.server.common.IConfigureCommon;
import io.edurt.datacap.server.common.JSON;
import io.edurt.datacap.server.common.PluginCommon;
import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.common.ServiceState;
import io.edurt.datacap.server.entity.PageEntity;
import io.edurt.datacap.server.entity.PluginEntity;
import io.edurt.datacap.server.entity.SourceEntity;
import io.edurt.datacap.server.entity.UserEntity;
import io.edurt.datacap.server.plugin.configure.IConfigure;
import io.edurt.datacap.server.plugin.configure.IConfigureField;
import io.edurt.datacap.server.repository.SourceRepository;
import io.edurt.datacap.server.repository.UserRepository;
import io.edurt.datacap.server.security.UserDetailsService;
import io.edurt.datacap.server.service.SourceService;
import io.edurt.datacap.spi.FormatType;
import io.edurt.datacap.spi.Plugin;
import io.edurt.datacap.spi.model.Configure;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class SourceServiceImpl
        implements SourceService
{
    private final SourceRepository sourceRepository;
    private final UserRepository userRepository;
    private final Injector injector;
    private final Environment environment;

    public SourceServiceImpl(SourceRepository sourceRepository, UserRepository userRepository, Injector injector, Environment environment)
    {
        this.sourceRepository = sourceRepository;
        this.userRepository = userRepository;
        this.injector = injector;
        this.environment = environment;
    }

    @Deprecated
    @Override
    public Response<SourceEntity> saveOrUpdate(SourceEntity configure)
    {
        configure.setConfigure(JSON.toJSON(configure.getConfigures()));
        configure.setUser(UserDetailsService.getUser());
        return Response.success(this.sourceRepository.save(configure));
    }

    @Override
    public Response<PageEntity<SourceEntity>> getAll(int offset, int limit)
    {
        Pageable pageable = PageRequestAdapter.of(offset, limit);
        UserEntity user = UserDetailsService.getUser();
        return Response.success(PageEntity.build(this.sourceRepository.findAllByUserOrPublishIsTrue(user, pageable)));
    }

    @Override
    public Response<Long> delete(Long id)
    {
        this.sourceRepository.deleteById(id);
        return Response.success(id);
    }

    @Deprecated
    @Override
    public Response<Object> testConnection(SourceEntity configure)
    {
        Optional<Plugin> pluginOptional = PluginCommon.getPluginByNameAndType(this.injector, configure.getType(), configure.getProtocol());
        if (!pluginOptional.isPresent()) {
            return Response.failure(ServiceState.PLUGIN_NOT_FOUND);
        }

        Configure _configure = new Configure();
        Plugin plugin = pluginOptional.get();
        _configure.setHost(configure.getHost());
        _configure.setPort(configure.getPort());
        _configure.setUsername(Optional.ofNullable(configure.getUsername()));
        _configure.setPassword(Optional.ofNullable(configure.getPassword()));
        Optional<String> _database = StringUtils.isNotEmpty(configure.getDatabase()) ? Optional.ofNullable(configure.getDatabase()) : Optional.empty();
        _configure.setDatabase(_database);
        _configure.setEnv(Optional.ofNullable(configure.getConfigures()));
        _configure.setSsl(Optional.ofNullable(configure.getSsl()));
        _configure.setFormat(FormatType.JSON);
        plugin.connect(_configure);
        io.edurt.datacap.spi.model.Response response = plugin.execute(plugin.validator());
        plugin.destroy();
        if (response.getIsSuccessful()) {
            return Response.success(response);
        }
        return Response.failure(ServiceState.PLUGIN_EXECUTE_FAILED, response.getMessage());
    }

    @Override
    public Response<SourceEntity> getById(Long id)
    {
        return Response.success(this.sourceRepository.findById(id));
    }

    @Override
    public Response<Map<String, List<PluginEntity>>> getPlugins()
    {
        Map<String, List<PluginEntity>> pluginMap = new ConcurrentHashMap<>();
        this.injector.getInstance(Key.get(new TypeLiteral<Set<Plugin>>() {})).stream().forEach(plugin -> {
            PluginEntity entity = new PluginEntity();
            entity.setName(plugin.name());
            entity.setDescription(plugin.description());
            entity.setType(plugin.type().name());
            entity.setConfigure(PluginCommon.loadConfigure(plugin.type().name(), plugin.name(), plugin.name(), environment));
            List<PluginEntity> plugins = pluginMap.get(plugin.type().name());
            if (ObjectUtils.isEmpty(plugins)) {
                plugins = new ArrayList<>();
            }
            plugins.add(entity);
            pluginMap.put(plugin.type().name(), plugins);
        });
        return Response.success(pluginMap);
    }

    @Override
    public Response<Long> count()
    {
        return Response.success(this.sourceRepository.countByUserOrPublishIsTrue(UserDetailsService.getUser()));
    }

    @Override
    public Response<Object> shared(SharedSourceBody configure)
    {
        Optional<SourceEntity> sourceOptional = this.sourceRepository.findById(configure.getSourceId());
        if (!sourceOptional.isPresent()) {
            return Response.failure(ServiceState.SOURCE_NOT_FOUND);
        }

        Optional<UserEntity> userOptional = this.userRepository.findById(configure.getUserId());
        if (!userOptional.isPresent()) {
            return Response.failure(ServiceState.USER_NOT_FOUND);
        }

        SourceEntity source = sourceOptional.get();
        source.setUser(userOptional.get());
        source.setPublish(configure.getPublish());
        return Response.success(this.sourceRepository.save(source));
    }

    @Override
    public Response<Object> testConnectionV2(SourceBody configure)
    {
        Optional<Plugin> pluginOptional = PluginCommon.getPluginByNameAndType(this.injector, configure.getName(), configure.getType());
        if (!pluginOptional.isPresent()) {
            return Response.failure(ServiceState.PLUGIN_NOT_FOUND);
        }

        // Check configure
        IConfigure iConfigure = PluginCommon.loadConfigure(configure.getType(), configure.getName(), configure.getName(), environment);
        if (ObjectUtils.isEmpty(iConfigure) || iConfigure.getConfigures().size() != configure.getConfigure().getConfigures().size()) {
            return Response.failure(ServiceState.PLUGIN_CONFIGURE_MISMATCH);
        }

        // Filter required
        List<IConfigureField> requiredMismatchConfigures = configure.getConfigure().getConfigures().stream().filter(v -> v.isRequired()).filter(v -> ObjectUtils.isEmpty(v.getValue())).collect(Collectors.toList());
        if (requiredMismatchConfigures.size() > 0) {
            return Response.failure(ServiceState.PLUGIN_CONFIGURE_REQUIRED, IConfigureCommon.preparedMessage(requiredMismatchConfigures));
        }

        Plugin plugin = pluginOptional.get();
        // The filter parameter value is null data
        List<IConfigureField> applyConfigures = IConfigureCommon.filterNotEmpty(configure.getConfigure().getConfigures());
        Configure _configure = IConfigureCommon.preparedConfigure(applyConfigures);
        plugin.connect(_configure);
        io.edurt.datacap.spi.model.Response response = plugin.execute(plugin.validator());
        if (response.getIsSuccessful()) {
            plugin.destroy();
            return Response.success(response);
        }
        return Response.failure(ServiceState.PLUGIN_EXECUTE_FAILED, response.getMessage());
    }

    @Override
    public Response<SourceEntity> saveOrUpdateV2(SourceBody configure)
    {
        Optional<Plugin> pluginOptional = PluginCommon.getPluginByNameAndType(this.injector, configure.getName(), configure.getType());
        if (!pluginOptional.isPresent()) {
            return Response.failure(ServiceState.PLUGIN_NOT_FOUND);
        }

        // Check configure
        IConfigure iConfigure = PluginCommon.loadConfigure(configure.getType(), configure.getName(), configure.getName(), environment);
        if (ObjectUtils.isEmpty(iConfigure) || iConfigure.getConfigures().size() != configure.getConfigure().getConfigures().size()) {
            return Response.failure(ServiceState.PLUGIN_CONFIGURE_MISMATCH);
        }

        // Filter required
        List<IConfigureField> requiredMismatchConfigures = configure.getConfigure().getConfigures().stream().filter(v -> v.isRequired()).filter(v -> ObjectUtils.isEmpty(v.getValue())).collect(Collectors.toList());
        if (requiredMismatchConfigures.size() > 0) {
            return Response.failure(ServiceState.PLUGIN_CONFIGURE_REQUIRED, IConfigureCommon.preparedMessage(requiredMismatchConfigures));
        }

        // The filter parameter value is null data
        List<IConfigureField> applyConfigures = IConfigureCommon.filterNotEmpty(configure.getConfigure().getConfigures());
        SourceEntity source = IConfigureCommon.preparedSourceEntity(applyConfigures);
        source.setProtocol(configure.getType());
        source.setType(configure.getName());
        source.setUser(UserDetailsService.getUser());
        if (ObjectUtils.isNotEmpty(configure.getId())) {
            source.setId(configure.getId());
        }
        return Response.success(this.sourceRepository.save(source));
    }

    @Override
    public Response<SourceEntity> getByIdV2(Long id)
    {
        Optional<SourceEntity> optionalSource = this.sourceRepository.findById(id);
        if (!optionalSource.isPresent()) {
            return Response.failure(ServiceState.SOURCE_NOT_FOUND);
        }
        SourceEntity entity = optionalSource.get();
        SourceBody configure = new SourceBody();
        configure.setId(id);
        configure.setName(entity.getType());
        configure.setType(entity.getProtocol());
        // Load default configure
        IConfigure iConfigure = PluginCommon.loadConfigure(configure.getType(), configure.getName(), configure.getName(), environment);
        configure.setConfigure(IConfigureCommon.preparedConfigure(iConfigure, entity));
        entity.setSchema(iConfigure);
        return Response.success(entity);
    }
}
