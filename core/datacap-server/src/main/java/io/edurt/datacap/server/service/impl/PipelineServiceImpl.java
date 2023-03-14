package io.edurt.datacap.server.service.impl;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import io.edurt.datacap.server.body.PipelineBody;
import io.edurt.datacap.server.common.BeanToPropertiesCommon;
import io.edurt.datacap.server.common.PluginCommon;
import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.common.ServiceState;
import io.edurt.datacap.server.entity.SourceEntity;
import io.edurt.datacap.server.plugin.configure.IConfigure;
import io.edurt.datacap.server.plugin.configure.IConfigureExecutor;
import io.edurt.datacap.server.plugin.configure.IConfigureExecutorField;
import io.edurt.datacap.server.plugin.configure.IConfigurePipelineType;
import io.edurt.datacap.server.repository.SourceRepository;
import io.edurt.datacap.server.security.UserDetailsService;
import io.edurt.datacap.server.service.PipelineService;
import io.edurt.datacap.spi.executor.Executor;
import io.edurt.datacap.spi.executor.Pipeline;
import io.edurt.datacap.spi.executor.PipelineField;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

@Service
@Slf4j
public class PipelineServiceImpl
        implements PipelineService
{
    private final SourceRepository repository;
    private final Injector injector;
    private final Environment environment;

    public PipelineServiceImpl(SourceRepository repository, Injector injector, Environment environment)
    {
        this.repository = repository;
        this.injector = injector;
        this.environment = environment;
    }

    @Override
    public Response<Object> submit(PipelineBody configure)
    {
        Optional<SourceEntity> fromSourceOptional = repository.findById(configure.getFrom().getId());
        Optional<SourceEntity> toSourceOptional = repository.findById(configure.getTo().getId());

        if (fromSourceOptional.isPresent() && toSourceOptional.isPresent()) {
            SourceEntity fromSource = fromSourceOptional.get();
            IConfigure fromConfigure = PluginCommon.loadYamlConfigure(fromSource.getProtocol(), fromSource.getType(), fromSource.getType(), environment);
            // Check if Pipeline is supported
            if (ObjectUtils.isEmpty(fromConfigure.getPipelines())) {
                String message = String.format("Source %s is not supported pipeline, type %s", fromSource.getId(), fromSource.getType());
                return Response.failure(ServiceState.SOURCE_NOT_SUPPORTED_PIPELINE, message);
            }
            Optional<IConfigureExecutor> fromConfigureExecutor = fromConfigure.getPipelines()
                    .stream()
                    .filter(v -> v.getExecutor().equals(configure.getExecutor()) && v.getType().equals(IConfigurePipelineType.SOURCE))
                    .findFirst();
            if (!fromConfigureExecutor.isPresent()) {
                String message = String.format("Source %s type %s is not supported pipeline type %s", fromSource.getId(), fromSource.getType(), IConfigurePipelineType.SOURCE);
                return Response.failure(ServiceState.SOURCE_NOT_SUPPORTED_PIPELINE_TYPE, message);
            }

            SourceEntity toSource = toSourceOptional.get();
            IConfigure toConfigure = PluginCommon.loadYamlConfigure(toSource.getProtocol(), toSource.getType(), toSource.getType(), environment);
            if (ObjectUtils.isEmpty(toConfigure.getPipelines())) {
                String message = String.format("Source %s is not supported pipeline, type %s", toSource.getId(), toSource.getType());
                return Response.failure(ServiceState.SOURCE_NOT_SUPPORTED_PIPELINE, message);
            }
            Optional<IConfigureExecutor> toConfigureExecutor = toConfigure.getPipelines()
                    .stream()
                    .filter(v -> v.getExecutor().equals(configure.getExecutor()) && v.getType().equals(IConfigurePipelineType.SINK))
                    .findFirst();
            if (!toConfigureExecutor.isPresent()) {
                String message = String.format("Source %s type %s is not supported pipeline type %s", toSource.getId(), toSource.getType(), IConfigurePipelineType.SINK);
                return Response.failure(ServiceState.SOURCE_NOT_SUPPORTED_PIPELINE_TYPE, message);
            }

            Optional<Executor> executorOptional = injector.getInstance(Key.get(new TypeLiteral<Set<Executor>>() {}))
                    .stream()
                    .filter(executor -> executor.name().equals(configure.getExecutor()))
                    .findFirst();

            // FROM source
            Properties fromOriginProperties = configure.getFrom().getConfigures();
            fromOriginProperties.setProperty("context", configure.getContent());
            Properties fromProperties = this.merge(fromSource, fromConfigureExecutor.get().getFields(), fromOriginProperties);
            Set<String> fromOptions = new HashSet<>();
            fromConfigureExecutor.get()
                    .getFields()
                    .stream()
                    .filter(v -> v.isRequired())
                    .forEach(v -> fromOptions.add(v.getField()));
            PipelineField fromField = PipelineField.builder()
                    .type(fromSource.getType())
                    .configure(fromProperties)
                    .supportOptions(fromOptions)
                    .build();

            // TO source
            Properties toOriginProperties = configure.getTo().getConfigures();
            Properties toProperties = this.merge(toSource, toConfigureExecutor.get().getFields(), toOriginProperties);
            Set<String> toOptions = new HashSet<>();
            toConfigureExecutor.get()
                    .getFields()
                    .stream()
                    .filter(v -> v.isRequired())
                    .forEach(v -> toOptions.add(v.getField()));
            PipelineField toField = PipelineField.builder()
                    .type(toSource.getType())
                    .configure(toProperties)
                    .supportOptions(toOptions)
                    .build();

            String executorHome = environment.getProperty("datacap.executor.data");
            if (StringUtils.isEmpty(executorHome)) {
                executorHome = String.join(File.separator, System.getProperty("user.dir"), "data");
            }

            String username = UserDetailsService.getUser().getUsername();
            String pipelineHome = DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmssSSS");
            String work = String.join(File.separator, executorHome, username, pipelineHome);
            String pipelineName = String.join("_", username, configure.getExecutor().toLowerCase(), pipelineHome);
            try {
                FileUtils.forceMkdir(new File(work));
            }
            catch (Exception e) {
                log.warn("Failed to create temporary directory", e);
            }
            Pipeline pipeline = Pipeline.builder()
                    .work(work)
                    .home(environment.getProperty(String.format("datacap.executor.%s.home", configure.getExecutor().toLowerCase(Locale.ROOT))))
                    .pipelineName(pipelineName)
                    .username(UserDetailsService.getUser().getUsername())
                    .from(fromField)
                    .to(toField)
                    .timeout(600)
                    .build();
            executorOptional.get().start(pipeline);
            return Response.success(null);
        }
        return Response.failure(ServiceState.SOURCE_NOT_FOUND);
    }

    private Properties merge(SourceEntity entity, List<IConfigureExecutorField> fields, Properties configure)
    {
        Properties properties = new Properties();
        Properties convertBeanProperties = BeanToPropertiesCommon.convertBeanToProperties(entity);
        for (IConfigureExecutorField field : fields) {
            if (field.isOverride()) {
                this.setProperty(field, properties, configure);
            }
            else {
                this.setProperty(field, properties, convertBeanProperties);
            }
        }
        return properties;
    }

    private void setProperty(IConfigureExecutorField field, Properties properties, Properties configure)
    {
        Object value = "";
        if (ObjectUtils.isNotEmpty(field.getOrigin())) {
            if (ObjectUtils.isNotEmpty(configure.get(field.getOrigin()))) {
                value = configure.get(field.getOrigin());
            }
        }
        else {
            if (ObjectUtils.isNotEmpty(configure.get(field.getField()))) {
                value = configure.get(field.getField());
            }
        }
        properties.put(field.getField(), value);
    }
}
