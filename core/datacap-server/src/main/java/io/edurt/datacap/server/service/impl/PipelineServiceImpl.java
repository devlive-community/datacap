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
import io.edurt.datacap.server.repository.SourceRepository;
import io.edurt.datacap.server.service.PipelineService;
import io.edurt.datacap.spi.executor.Executor;
import io.edurt.datacap.spi.executor.Pipeline;
import io.edurt.datacap.spi.executor.PipelineField;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

@Service
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
        Optional<SourceEntity> fromSourceOptional = repository.findById(configure.getFrom());
        Optional<SourceEntity> toSourceOptional = repository.findById(configure.getTo());

        if (fromSourceOptional.isPresent() && toSourceOptional.isPresent()) {
            SourceEntity fromSource = fromSourceOptional.get();
            IConfigure fromConfigure = PluginCommon.loadConfigure(fromSource.getProtocol(), fromSource.getType(), fromSource.getType(), environment);
            Optional<IConfigureExecutor> fromConfigureExecutor = fromConfigure.getPipelines()
                    .stream()
                    .filter(v -> v.getExecutor().equals(configure.getExecutor()))
                    .findFirst();
            if (!fromConfigureExecutor.isPresent()) {
                return Response.failure(ServiceState.PLUGIN_NOT_FOUND);
            }

            SourceEntity toSource = toSourceOptional.get();
            IConfigure toConfigure = PluginCommon.loadConfigure(toSource.getProtocol(), toSource.getType(), toSource.getType(), environment);
            Optional<IConfigureExecutor> toConfigureExecutor = toConfigure.getPipelines()
                    .stream()
                    .filter(v -> v.getExecutor().equals(configure.getExecutor()))
                    .findFirst();
            if (!toConfigureExecutor.isPresent()) {
                return Response.failure(ServiceState.PLUGIN_NOT_FOUND);
            }

            Optional<Executor> executorOptional = injector.getInstance(Key.get(new TypeLiteral<Set<Executor>>() {}))
                    .stream()
                    .filter(executor -> executor.name().equals(configure.getExecutor()))
                    .findFirst();

            // FROM source
            Properties fromProperties = new Properties();
            Properties fromBeanProperties = BeanToPropertiesCommon.convertBeanToProperties(fromSource);
            fromConfigureExecutor.get().getFields().forEach(pipelineField -> fromProperties.put(pipelineField.getField(), fromBeanProperties.get(pipelineField.getField())));
            PipelineField fromField = PipelineField.builder()
                    .type(fromSource.getType())
                    .configure(fromProperties)
                    .build();

            // TO source
            Properties toProperties = new Properties();
            Properties toBeanProperties = BeanToPropertiesCommon.convertBeanToProperties(toSource);
            toConfigureExecutor.get().getFields().forEach(pipelineField -> toProperties.put(pipelineField.getField(), toBeanProperties.get(pipelineField.getField())));
            PipelineField toField = PipelineField.builder()
                    .type(toSource.getType())
                    .configure(toProperties)
                    .build();

            Pipeline pipeline = Pipeline.builder()
                    .work(System.getenv("user.dir") + "/" + UUID.randomUUID().toString())
                    .home(environment.getProperty("datacap.executor.seatunnel.home"))
                    .from(fromField)
                    .to(toField)
                    .build();
            executorOptional.get().start(pipeline);
            return Response.success(null);
        }
        return Response.failure(ServiceState.SOURCE_NOT_FOUND);
    }
}
