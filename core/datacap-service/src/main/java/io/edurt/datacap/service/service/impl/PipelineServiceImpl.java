package io.edurt.datacap.service.service.impl;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import io.edurt.datacap.common.enums.ServiceState;
import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.common.utils.BeanToPropertiesUtils;
import io.edurt.datacap.service.body.PipelineBody;
import io.edurt.datacap.service.common.PluginUtils;
import io.edurt.datacap.service.configure.IConfigure;
import io.edurt.datacap.service.configure.IConfigureExecutor;
import io.edurt.datacap.service.configure.IConfigureExecutorField;
import io.edurt.datacap.service.configure.IConfigurePipelineType;
import io.edurt.datacap.service.entity.PipelineEntity;
import io.edurt.datacap.service.entity.SourceEntity;
import io.edurt.datacap.service.repository.PipelineRepository;
import io.edurt.datacap.service.repository.SourceRepository;
import io.edurt.datacap.service.security.UserDetailsService;
import io.edurt.datacap.service.service.PipelineService;
import io.edurt.datacap.spi.executor.Executor;
import io.edurt.datacap.spi.executor.Pipeline;
import io.edurt.datacap.spi.executor.PipelineField;
import io.edurt.datacap.spi.executor.PipelineResponse;
import io.edurt.datacap.spi.executor.PipelineState;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class PipelineServiceImpl
        implements PipelineService
{
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final SourceRepository repository;
    private final Injector injector;
    private final Environment environment;
    private final PipelineRepository pipelineRepository;

    public PipelineServiceImpl(SourceRepository repository, Injector injector, Environment environment, PipelineRepository pipelineRepository)
    {
        this.repository = repository;
        this.injector = injector;
        this.environment = environment;
        this.pipelineRepository = pipelineRepository;
    }

    @Override
    public CommonResponse<Object> submit(PipelineBody configure)
    {
        Optional<SourceEntity> fromSourceOptional = repository.findById(configure.getFrom().getId());
        Optional<SourceEntity> toSourceOptional = repository.findById(configure.getTo().getId());

        if (fromSourceOptional.isPresent() && toSourceOptional.isPresent()) {
            SourceEntity fromSource = fromSourceOptional.get();
            IConfigure fromConfigure = PluginUtils.loadYamlConfigure(fromSource.getProtocol(), fromSource.getType(), fromSource.getType(), environment);
            // Check if Pipeline is supported
            if (ObjectUtils.isEmpty(fromConfigure.getPipelines())) {
                String message = String.format("Source %s is not supported pipeline, type %s", fromSource.getId(), fromSource.getType());
                return CommonResponse.failure(ServiceState.SOURCE_NOT_SUPPORTED_PIPELINE, message);
            }
            Optional<IConfigureExecutor> fromConfigureExecutor = fromConfigure.getPipelines()
                    .stream()
                    .filter(v -> v.getExecutor().equals(configure.getExecutor()) && v.getType().equals(IConfigurePipelineType.SOURCE))
                    .findFirst();
            if (!fromConfigureExecutor.isPresent()) {
                String message = String.format("Source %s type %s is not supported pipeline type %s", fromSource.getId(), fromSource.getType(), IConfigurePipelineType.SOURCE);
                return CommonResponse.failure(ServiceState.SOURCE_NOT_SUPPORTED_PIPELINE_TYPE, message);
            }

            SourceEntity toSource = toSourceOptional.get();
            IConfigure toConfigure = PluginUtils.loadYamlConfigure(toSource.getProtocol(), toSource.getType(), toSource.getType(), environment);
            if (ObjectUtils.isEmpty(toConfigure.getPipelines())) {
                String message = String.format("Source %s is not supported pipeline, type %s", toSource.getId(), toSource.getType());
                return CommonResponse.failure(ServiceState.SOURCE_NOT_SUPPORTED_PIPELINE, message);
            }
            Optional<IConfigureExecutor> toConfigureExecutor = toConfigure.getPipelines()
                    .stream()
                    .filter(v -> v.getExecutor().equals(configure.getExecutor()) && v.getType().equals(IConfigurePipelineType.SINK))
                    .findFirst();
            if (!toConfigureExecutor.isPresent()) {
                String message = String.format("Source %s type %s is not supported pipeline type %s", toSource.getId(), toSource.getType(), IConfigurePipelineType.SINK);
                return CommonResponse.failure(ServiceState.SOURCE_NOT_SUPPORTED_PIPELINE_TYPE, message);
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
            String pipelineName = String.join("_", username, configure.getExecutor().toLowerCase(), "from", String.valueOf(fromSource.getId()), "to", String.valueOf(toSource.getId()), pipelineHome);
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

            PipelineEntity pipelineEntity = new PipelineEntity();
            pipelineEntity.setName(pipelineName);
            pipelineEntity.setContent(configure.getContent());
            pipelineEntity.setState(PipelineState.CREATED);
            pipelineEntity.setWork(work);
            pipelineEntity.setStartTime(new Timestamp(System.currentTimeMillis()));
            pipelineEntity.setUser(UserDetailsService.getUser());
            pipelineEntity.setFrom(fromSource);
            pipelineEntity.setFromConfigures(fromProperties);
            pipelineEntity.setTo(toSource);
            pipelineEntity.setToConfigures(toProperties);
            pipelineRepository.save(pipelineEntity);

            executorService.submit(() -> {
                pipelineEntity.setState(PipelineState.RUNNING);
                pipelineRepository.save(pipelineEntity);

                PipelineResponse response = executorOptional.get().start(pipeline);
                pipelineEntity.setEndTime(new Timestamp(System.currentTimeMillis()));
                pipelineEntity.setState(response.getState());
                pipelineEntity.setMessage(response.getMessage());
                pipelineEntity.setElapsed(pipelineEntity.getElapsed());
                pipelineRepository.save(pipelineEntity);
            });
            return CommonResponse.success(pipelineEntity.getId());
        }
        return CommonResponse.failure(ServiceState.SOURCE_NOT_FOUND);
    }

    private Properties merge(SourceEntity entity, List<IConfigureExecutorField> fields, Properties configure)
    {
        Properties properties = new Properties();
        Properties convertBeanProperties = BeanToPropertiesUtils.convertBeanToProperties(entity);
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
            String[] split = String.valueOf(field.getOrigin()).split("\\|");
            if (split.length > 1) {
                value = String.join(":", String.valueOf(configure.get(split[0])), String.valueOf(configure.get(split[1])));
            }
            else {
                if (ObjectUtils.isNotEmpty(configure.get(field.getOrigin()))) {
                    value = configure.get(field.getOrigin());
                }
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
