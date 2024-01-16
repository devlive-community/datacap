package io.edurt.datacap.server.configure;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.edurt.datacap.fs.FsManager;
import io.edurt.datacap.parser.ParserManager;
import io.edurt.datacap.scheduler.ScheduleManager;
import io.edurt.datacap.spi.PluginLoader;
import io.edurt.datacap.spi.executor.ExecutorManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PluginConfigure
{
    @Bean
    public Injector injector()
    {
        return Guice.createInjector(new PluginLoader(),
                new ExecutorManager(),
                new FsManager(),
                new ParserManager(),
                new ScheduleManager(),
                new io.edurt.datacap.executor.ExecutorManager());
    }
}
