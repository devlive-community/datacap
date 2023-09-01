package io.edurt.datacap.service.service.impl;

import com.google.common.io.Files;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.common.enums.ServiceState;
import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.common.utils.JsonUtils;
import io.edurt.datacap.service.adapter.PageRequestAdapter;
import io.edurt.datacap.service.body.SharedSourceBody;
import io.edurt.datacap.service.body.SourceBody;
import io.edurt.datacap.service.common.ConfigureUtils;
import io.edurt.datacap.service.common.PluginUtils;
import io.edurt.datacap.service.configure.IConfigure;
import io.edurt.datacap.service.configure.IConfigureField;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.entity.PluginEntity;
import io.edurt.datacap.service.entity.SourceEntity;
import io.edurt.datacap.service.entity.UserEntity;
import io.edurt.datacap.service.repository.SourceRepository;
import io.edurt.datacap.service.repository.UserRepository;
import io.edurt.datacap.service.security.UserDetailsService;
import io.edurt.datacap.service.service.SourceService;
import io.edurt.datacap.spi.FormatType;
import io.edurt.datacap.spi.Plugin;
import io.edurt.datacap.spi.model.Configure;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@SuppressFBWarnings(value = {"RV_RETURN_VALUE_IGNORED_BAD_PRACTICE"})
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
    public CommonResponse<SourceEntity> saveOrUpdate(SourceEntity configure)
    {
        configure.setConfigure(JsonUtils.toJSON(configure.getConfigures()));
        configure.setUser(UserDetailsService.getUser());
        return CommonResponse.success(this.sourceRepository.save(configure));
    }

    @Override
    public CommonResponse<PageEntity<SourceEntity>> getAll(int offset, int limit)
    {
        Pageable pageable = PageRequestAdapter.of(offset, limit);
        UserEntity user = UserDetailsService.getUser();
        Page<SourceEntity> page = this.sourceRepository.findAllByUserOrPublishIsTrue(user, pageable);
        // Populate pipeline configuration information
        page.getContent()
                .stream()
                .forEach(item -> {
                    IConfigure fromConfigure = PluginUtils.loadYamlConfigure(item.getProtocol(), item.getType(), item.getType(), environment);
                    if (fromConfigure != null) {
                        item.setPipelines(fromConfigure.getPipelines());
                    }
                });
        return CommonResponse.success(PageEntity.build(page));
    }

    @SneakyThrows
    @Override
    public CommonResponse<Long> delete(Long id)
    {
        Optional<SourceEntity> entityOptional = this.sourceRepository.findById(id);
        if (entityOptional.isPresent()) {
            SourceEntity source = entityOptional.get();
            if (source.isUsedConfig()) {
                String configHome = environment.getProperty("datacap.config.data");
                if (StringUtils.isEmpty(configHome)) {
                    configHome = String.join(File.separator, System.getProperty("user.dir"), "config");
                }
                String destination = String.join(File.separator, configHome, source.getUser().getUsername(), source.getType(), String.valueOf(source.getId()));
                FileUtils.deleteDirectory(new File(destination));
            }
            this.sourceRepository.deleteById(id);
        }
        return CommonResponse.success(id);
    }

    @Deprecated
    @Override
    public CommonResponse<Object> testConnection(SourceEntity configure)
    {
        Optional<Plugin> pluginOptional = PluginUtils.getPluginByNameAndType(this.injector, configure.getType(), configure.getProtocol());
        if (!pluginOptional.isPresent()) {
            return CommonResponse.failure(ServiceState.PLUGIN_NOT_FOUND);
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
            return CommonResponse.success(response);
        }
        return CommonResponse.failure(ServiceState.PLUGIN_EXECUTE_FAILED, response.getMessage());
    }

    @Override
    public CommonResponse<SourceEntity> getById(Long id)
    {
        return CommonResponse.success(this.sourceRepository.findById(id));
    }

    @Override
    public CommonResponse<Map<String, List<PluginEntity>>> getPlugins()
    {
        Map<String, List<PluginEntity>> pluginMap = new ConcurrentHashMap<>();
        this.injector.getInstance(Key.get(new TypeLiteral<Set<Plugin>>() {})).stream().forEach(plugin -> {
            PluginEntity entity = new PluginEntity();
            entity.setName(plugin.name());
            entity.setDescription(plugin.description());
            entity.setType(plugin.type().name());
            entity.setConfigure(PluginUtils.loadYamlConfigure(plugin.type().name(), plugin.name(), plugin.name(), environment));
            List<PluginEntity> plugins = pluginMap.get(plugin.type().name());
            if (ObjectUtils.isEmpty(plugins)) {
                plugins = new ArrayList<>();
            }
            plugins.add(entity);
            pluginMap.put(plugin.type().name(), plugins);
        });
        return CommonResponse.success(pluginMap);
    }

    @Override
    public CommonResponse<Long> count()
    {
        return CommonResponse.success(this.sourceRepository.countByUserOrPublishIsTrue(UserDetailsService.getUser()));
    }

    @Override
    public CommonResponse<Object> shared(SharedSourceBody configure)
    {
        Optional<SourceEntity> sourceOptional = this.sourceRepository.findById(configure.getSourceId());
        if (!sourceOptional.isPresent()) {
            return CommonResponse.failure(ServiceState.SOURCE_NOT_FOUND);
        }

        Optional<UserEntity> userOptional = this.userRepository.findById(configure.getUserId());
        if (!userOptional.isPresent()) {
            return CommonResponse.failure(ServiceState.USER_NOT_FOUND);
        }

        SourceEntity source = sourceOptional.get();
        source.setUser(userOptional.get());
        source.setPublish(configure.getPublish());
        return CommonResponse.success(this.sourceRepository.save(source));
    }

    @Override
    public CommonResponse<Object> testConnectionV2(SourceBody configure)
    {
        Optional<Plugin> pluginOptional = PluginUtils.getPluginByNameAndType(this.injector, configure.getName(), configure.getType());
        if (!pluginOptional.isPresent()) {
            return CommonResponse.failure(ServiceState.PLUGIN_NOT_FOUND);
        }

        // Check configure
        IConfigure iConfigure = PluginUtils.loadYamlConfigure(configure.getType(), configure.getName(), configure.getName(), environment);
        if (ObjectUtils.isEmpty(iConfigure) || iConfigure.getConfigures().size() != configure.getConfigure().getConfigures().size()) {
            return CommonResponse.failure(ServiceState.PLUGIN_CONFIGURE_MISMATCH);
        }

        // Filter required
        List<IConfigureField> requiredMismatchConfigures = configure.getConfigure().getConfigures().stream().filter(v -> v.isRequired()).filter(v -> ObjectUtils.isEmpty(v.getValue())).collect(Collectors.toList());
        if (requiredMismatchConfigures.size() > 0) {
            return CommonResponse.failure(ServiceState.PLUGIN_CONFIGURE_REQUIRED, ConfigureUtils.preparedMessage(requiredMismatchConfigures));
        }

        Plugin plugin = pluginOptional.get();
        // The filter parameter value is null data
        List<IConfigureField> applyConfigures = ConfigureUtils.filterNotEmpty(configure.getConfigure().getConfigures());
        Configure _configure = ConfigureUtils.preparedConfigure(applyConfigures);
        // Adapter file configure
        if (_configure.isUsedConfig()) {
            String cacheHome = environment.getProperty("datacap.cache.data");
            if (StringUtils.isEmpty(cacheHome)) {
                cacheHome = String.join(File.separator, System.getProperty("user.dir"), "cache");
            }
            _configure.setHome(cacheHome);
            _configure.setUsername(Optional.of(UserDetailsService.getUser().getUsername()));
        }
        plugin.connect(_configure);
        io.edurt.datacap.spi.model.Response response = plugin.execute(plugin.validator());
        if (response.getIsSuccessful()) {
            plugin.destroy();
            return CommonResponse.success(response);
        }
        return CommonResponse.failure(ServiceState.PLUGIN_EXECUTE_FAILED, response.getMessage());
    }

    @Override
    public CommonResponse<SourceEntity> saveOrUpdateV2(SourceBody configure)
    {
        Optional<Plugin> pluginOptional = PluginUtils.getPluginByNameAndType(this.injector, configure.getName(), configure.getType());
        if (!pluginOptional.isPresent()) {
            return CommonResponse.failure(ServiceState.PLUGIN_NOT_FOUND);
        }

        // Check configure
        IConfigure iConfigure = PluginUtils.loadYamlConfigure(configure.getType(), configure.getName(), configure.getName(), environment);
        if (ObjectUtils.isEmpty(iConfigure) || iConfigure.getConfigures().size() != configure.getConfigure().getConfigures().size()) {
            return CommonResponse.failure(ServiceState.PLUGIN_CONFIGURE_MISMATCH);
        }

        // Filter required
        List<IConfigureField> requiredMismatchConfigures = configure.getConfigure().getConfigures().stream().filter(v -> v.isRequired()).filter(v -> ObjectUtils.isEmpty(v.getValue())).collect(Collectors.toList());
        if (requiredMismatchConfigures.size() > 0) {
            return CommonResponse.failure(ServiceState.PLUGIN_CONFIGURE_REQUIRED, ConfigureUtils.preparedMessage(requiredMismatchConfigures));
        }

        // The filter parameter value is null data
        List<IConfigureField> applyConfigures = ConfigureUtils.filterNotEmpty(configure.getConfigure().getConfigures());
        SourceEntity source = ConfigureUtils.preparedSourceEntity(applyConfigures);
        source.setProtocol(configure.getType());
        source.setType(configure.getName());
        source.setUser(UserDetailsService.getUser());
        if (ObjectUtils.isNotEmpty(configure.getId()) && configure.getId() > 0) {
            source.setId(configure.getId());
        }
        if (StringUtils.isNotEmpty(configure.getVersion())) {
            source.setVersion(configure.getVersion());
            source.setAvailable(true);
        }
        else {
            source.setAvailable(false);
        }
        this.sourceRepository.save(source);
        // Copy file to local data
        if (source.isUsedConfig()) {
            String cacheHome = environment.getProperty("datacap.cache.data");
            if (StringUtils.isEmpty(cacheHome)) {
                cacheHome = String.join(File.separator, System.getProperty("user.dir"), "cache");
            }
            String configHome = environment.getProperty("datacap.config.data");
            if (StringUtils.isEmpty(configHome)) {
                configHome = String.join(File.separator, System.getProperty("user.dir"), "config");
            }
            File sourceFile = new File(String.join(File.separator, cacheHome, source.getUser().getUsername(), source.getType()));
            String destination = String.join(File.separator, configHome, source.getUser().getUsername(), source.getType(), String.valueOf(source.getId()));
            File directory = new File(destination);
            try {
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                for (File file : sourceFile.listFiles()) {
                    Files.copy(file, new File(String.join(File.separator, destination, file.getName())));
                }
                FileUtils.deleteDirectory(sourceFile);
            }
            catch (Exception e) {
                return CommonResponse.failure("Copy failed: " + e.getMessage());
            }
        }
        return CommonResponse.success(source);
    }

    @Override
    public CommonResponse<SourceEntity> getByIdV2(Long id)
    {
        Optional<SourceEntity> optionalSource = this.sourceRepository.findById(id);
        if (!optionalSource.isPresent()) {
            return CommonResponse.failure(ServiceState.SOURCE_NOT_FOUND);
        }
        SourceEntity entity = optionalSource.get();
        SourceBody configure = new SourceBody();
        configure.setId(id);
        configure.setName(entity.getType());
        configure.setType(entity.getProtocol());
        // Load default configure
        IConfigure iConfigure = PluginUtils.loadYamlConfigure(configure.getType(), configure.getName(), configure.getName(), environment);
        configure.setConfigure(ConfigureUtils.preparedConfigure(iConfigure, entity));
        entity.setSchema(iConfigure);
        return CommonResponse.success(entity);
    }
}
