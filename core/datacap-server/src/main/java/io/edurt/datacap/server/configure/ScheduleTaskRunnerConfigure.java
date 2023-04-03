package io.edurt.datacap.server.configure;

import com.google.inject.Injector;
import io.edurt.datacap.schedule.ScheduledCronRegistrar;
import io.edurt.datacap.server.repository.ScheduledTaskRepository;
import io.edurt.datacap.server.repository.SourceRepository;
import io.edurt.datacap.server.repository.TemplateSqlRepository;
import io.edurt.datacap.server.scheduled.SourceScheduledRunnable;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ScheduleTaskRunnerConfigure
        implements CommandLineRunner
{
    private final Injector injector;
    private final ScheduledTaskRepository scheduledTaskRepository;
    private final SourceRepository sourceRepository;
    private final TemplateSqlRepository templateSqlRepository;
    private final ScheduledCronRegistrar scheduledCronRegistrar;
    private final RedisTemplate redisTemplate;

    public ScheduleTaskRunnerConfigure(Injector injector, ScheduledTaskRepository scheduledTaskRepository, SourceRepository sourceRepository, TemplateSqlRepository templateSqlRepository, ScheduledCronRegistrar scheduledCronRegistrar, RedisTemplate redisTemplate)
    {
        this.injector = injector;
        this.scheduledTaskRepository = scheduledTaskRepository;
        this.sourceRepository = sourceRepository;
        this.templateSqlRepository = templateSqlRepository;
        this.scheduledCronRegistrar = scheduledCronRegistrar;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void run(String... args)
    {
        this.scheduledTaskRepository.findAllByActiveIsTrueAndIsSystemIsTrue().forEach(task -> {
            SourceScheduledRunnable scheduled = new SourceScheduledRunnable(task.getName(), this.injector, this.sourceRepository, templateSqlRepository, redisTemplate);
            this.scheduledCronRegistrar.addCronTask(scheduled, task.getExpression());
        });
    }
}
