package io.edurt.datacap.service.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
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
import io.edurt.datacap.service.initializer.InitializerConfigure;
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
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
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
    private final SourceRepository sourceRepository;
    private final Injector injector;
    private final Environment environment;
    private final PipelineRepository repository;
    private final InitializerConfigure initializer;

    public PipelineServiceImpl(SourceRepository sourceRepository, Injector injector, Environment environment, PipelineRepository repository, InitializerConfigure initializer)
    {
        this.sourceRepository = sourceRepository;
        this.injector = injector;
        this.environment = environment;
        this.repository = repository;
        this.initializer = initializer;
    }

    @Override
    public CommonResponse<Object> submit(PipelineBody configure)
    {
        Optional<SourceEntity> fromSourceOptional = sourceRepository.findById(configure.getFrom().getId());
        if (!fromSourceOptional.isPresent()) {
            return CommonResponse.failure(String.format("From source [ %s ] not found", configure.getFrom().getId()));
        }

        Optional<SourceEntity> toSourceOptional = sourceRepository.findById(configure.getTo().getId());
        if (!toSourceOptional.isPresent()) {
            return CommonResponse.failure(String.format("To source [ %s ] not found", configure.getTo().getId()));
        }

        SourceEntity fromSource = fromSourceOptional.get();
        IConfigure fromConfigure = PluginUtils.loadYamlConfigure(fromSource.getProtocol(), fromSource.getType(), fromSource.getType(), environment);
        // Check if Pipeline is supported
        if (ObjectUtils.isEmpty(fromConfigure.getPipelines())) {
            String message = String.format("From source [ %s ] is not supported pipeline, type [ %s ]", fromSource.getId(), fromSource.getType());
            return CommonResponse.failure(ServiceState.SOURCE_NOT_SUPPORTED_PIPELINE, message);
        }
        Optional<IConfigureExecutor> fromConfigureExecutor = fromConfigure.getPipelines()
                .stream()
                .filter(v -> v.getExecutor().equals(configure.getExecutor()) && v.getType().equals(IConfigurePipelineType.SOURCE))
                .findFirst();
        if (!fromConfigureExecutor.isPresent()) {
            String message = String.format("From source [ %s ] type [ %s ] is not supported pipeline type [ %s ]", fromSource.getId(), fromSource.getType(), IConfigurePipelineType.SOURCE);
            return CommonResponse.failure(ServiceState.SOURCE_NOT_SUPPORTED_PIPELINE_TYPE, message);
        }

        SourceEntity toSource = toSourceOptional.get();
        IConfigure toConfigure = PluginUtils.loadYamlConfigure(toSource.getProtocol(), toSource.getType(), toSource.getType(), environment);
        if (ObjectUtils.isEmpty(toConfigure.getPipelines())) {
            String message = String.format("To source [ %s ] is not supported pipeline, type [ %s ]", toSource.getId(), toSource.getType());
            return CommonResponse.failure(ServiceState.SOURCE_NOT_SUPPORTED_PIPELINE, message);
        }
        Optional<IConfigureExecutor> toConfigureExecutor = toConfigure.getPipelines()
                .stream()
                .filter(v -> v.getExecutor().equals(configure.getExecutor()) && v.getType().equals(IConfigurePipelineType.SINK))
                .findFirst();
        if (!toConfigureExecutor.isPresent()) {
            String message = String.format("To source [ %s ] type [ %s ] is not supported pipeline type [ %s ]", toSource.getId(), toSource.getType(), IConfigurePipelineType.SINK);
            return CommonResponse.failure(ServiceState.SOURCE_NOT_SUPPORTED_PIPELINE_TYPE, message);
        }

        PipelineEntity pipelineEntity = new PipelineEntity();

        // FROM source
        Properties fromOriginProperties = configure.getFrom().getConfigures();
        if (!fromOriginProperties.containsKey("context")) {
            fromOriginProperties.setProperty("context", configure.getContent());
        }
        Properties fromProperties = this.merge(fromSource, fromConfigureExecutor.get().getFields(), fromOriginProperties);
        Set<String> fromOptions = Sets.newHashSet();
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
        Set<String> toOptions = Sets.newHashSet();
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
        if (ObjectUtils.isNotEmpty(configure.getId())) {
            pipelineEntity = this.repository.findById(configure.getId()).get();
        }
        else {
            String executorHome = environment.getProperty("datacap.executor.data");
            if (StringUtils.isEmpty(executorHome)) {
                executorHome = String.join(File.separator, System.getProperty("user.dir"), "data");
            }
            String username = UserDetailsService.getUser().getUsername();
            String pipelineHome = DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmssSSS");
            String work = String.join(File.separator, executorHome, username, pipelineHome);
            String pipelineName = String.join("_",
                    username,
                    configure.getExecutor().toLowerCase(),
                    "from",
                    String.valueOf(fromSource.getId()),
                    "to",
                    String.valueOf(toSource.getId()),
                    pipelineHome);
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
        }

        String pipelineName = pipelineEntity.getName();
        String work = pipelineEntity.getWork();
        if (initializer.isSubmit()) {
            log.info("Pipeline containers is full, submit to queue [ {} ]", pipelineName);
            pipelineEntity.setState(PipelineState.QUEUE);
            repository.save(pipelineEntity);
            if (initializer.getTaskQueue().offer(pipelineEntity)) {
                log.info("Pipeline containers is full, submit to executor [ {} ]", pipelineName);
            }
        }
        else {
            log.info("Pipeline containers is not full, submit to executor [ {} ]", pipelineName);
            pipelineEntity.setState(PipelineState.RUNNING);
            repository.save(pipelineEntity);
            Optional<Executor> executorOptional = injector.getInstance(Key.get(new TypeLiteral<Set<Executor>>() {}))
                    .stream()
                    .filter(executor -> executor.name().equals(configure.getExecutor()))
                    .findFirst();

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

            final ExecutorService executorService = Executors.newCachedThreadPool();
            PipelineEntity finalPipelineEntity = pipelineEntity;
            executorService.submit(() -> {
                initializer.getTaskExecutors()
                        .put(pipelineName, executorService);
                PipelineResponse response = executorOptional.get()
                        .start(pipeline);
                finalPipelineEntity.setEndTime(new Timestamp(System.currentTimeMillis()));
                finalPipelineEntity.setState(response.getState());
                finalPipelineEntity.setMessage(response.getMessage());
                finalPipelineEntity.setElapsed(finalPipelineEntity.getElapsed());
                repository.save(finalPipelineEntity);
                initializer.getTaskExecutors()
                        .remove(pipelineName);
                executorService.shutdownNow();
                log.info("Pipeline [ {} ] finished", pipelineName);

                PipelineEntity entity = initializer.getTaskQueue()
                        .poll();
                if (ObjectUtils.isNotEmpty(entity)) {
                    log.info("Extract tasks from the queue [ {} ] and start execution", entity.getName());
                    this.submit(entity.entityToBody());
                }
                else {
                    log.warn("The queue extraction task failed. Please check whether there are tasks in the queue. The current number of queue tasks: [ {} ]", initializer.getTaskQueue().size());
                }
            });
        }
        return CommonResponse.success(pipelineEntity.getId());
    }

    @Override
    public CommonResponse<Boolean> stop(Long id)
    {
        Optional<PipelineEntity> pipelineOptional = this.repository.findById(id);
        if (!pipelineOptional.isPresent()) {
            return CommonResponse.failure(String.format("Pipeline [ %s ] not found", id));
        }

        PipelineEntity entity = pipelineOptional.get();
        if (entity.getState().equals(PipelineState.STOPPED)
                || entity.getState().equals(PipelineState.FAILURE)
                || entity.getState().equals(PipelineState.SUCCESS)
                || entity.getState().equals(PipelineState.TIMEOUT)) {
            return CommonResponse.failure(String.format("Pipeline [ %s ] is already stopped", entity.getName()));
        }

        ExecutorService service = initializer.getTaskExecutors()
                .get(entity.getName());
        if (service != null) {
            service.shutdownNow();
        }
        entity.setState(PipelineState.STOPPED);
        entity.setMessage(null);
        this.repository.save(entity);

        // Consume queue data for execution
        if (initializer.getTaskQueue().size() > 0) {
            PipelineEntity queueEntity = initializer.getTaskQueue()
                    .poll();
            if (queueEntity != null) {
                this.submit(entity.entityToBody());
            }
        }
        return CommonResponse.success(true);
    }

    /**
     * Retrieves the log for a given pipeline ID.
     *
     * @param id the ID of the pipeline
     * @return a response containing the log lines as a list of strings
     */
    @Override
    public CommonResponse<List<String>> log(Long id)
    {
        Optional<PipelineEntity> pipelineOptional = this.repository.findById(id);
        if (!pipelineOptional.isPresent()) {
            return CommonResponse.failure(String.format("Pipeline [ %s ] not found", id));
        }

        PipelineEntity entity = pipelineOptional.get();
        if (entity.getState().equals(PipelineState.QUEUE)
                || entity.getState().equals(PipelineState.CREATED)) {
            return CommonResponse.failure(String.format("Pipeline [ %s ] is not running", entity.getName()));
        }

        List<String> lines = Lists.newArrayList();
        try (FileInputStream stream = new FileInputStream(new File(String.format("%s/%s.log", entity.getWork(), entity.getName())))) {
            IOUtils.readLines(stream, "UTF-8")
                    .forEach(lines::add);
        }
        catch (IOException e) {
            log.error("Failed to read pipeline [ {} ] log ", entity.getName(), e);
        }
        return CommonResponse.success(lines);
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
        Object value = "None";
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
