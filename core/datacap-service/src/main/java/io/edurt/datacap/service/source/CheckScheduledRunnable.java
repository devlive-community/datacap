package io.edurt.datacap.service.source;

import com.google.inject.Injector;
import io.edurt.datacap.schedule.ScheduledRunnable;
import io.edurt.datacap.service.common.PluginUtils;
import io.edurt.datacap.service.entity.SourceEntity;
import io.edurt.datacap.service.repository.SourceRepository;
import io.edurt.datacap.spi.Plugin;
import io.edurt.datacap.spi.model.Configure;
import io.edurt.datacap.spi.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
public class CheckScheduledRunnable
        extends ScheduledRunnable
{
    private final Injector injector;
    private final SourceRepository sourceRepository;

    public CheckScheduledRunnable(String name, Injector injector, SourceRepository sourceRepository)
    {
        super(name);
        this.injector = injector;
        this.sourceRepository = sourceRepository;
    }

    @Override
    public void run()
    {
        log.info("==================== {} started =================", this.getName());
        this.sourceRepository.findAll()
                .forEach(entity -> {
                    log.info("Before check source {}", entity.getName());
                    Optional<Plugin> pluginOptional = PluginUtils.getPluginByNameAndType(this.injector, entity.getType(), entity.getProtocol());
                    if (!pluginOptional.isPresent()) {
                        log.warn("Check scheduled task <{}> source {} protocol {} is not available", this.getName(), entity.getType(), entity.getProtocol());
                    }
                    else {
                        Plugin plugin = pluginOptional.get();
                        plugin.connect(getConfigure(entity));
                        Response response = plugin.execute(plugin.validator());
                        if (response.getIsSuccessful()) {
                            entity.setAvailable(true);
                            if (response.getColumns().size() == 0) {
                                entity.setVersion("-");
                            }
                            else {
                                if (response.getColumns().get(0) instanceof ArrayList) {
                                    ArrayList versions = (ArrayList) response.getColumns().get(0);
                                    entity.setVersion(versions.get(0).toString());
                                }
                                else {
                                    entity.setVersion(response.getColumns().get(0).toString());
                                }
                            }
                        }
                        else {
                            entity.setAvailable(false);
                            entity.setVersion(null);
                            entity.setMessage(response.getMessage());
                        }
                        this.sourceRepository.save(entity);
                    }
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
}
