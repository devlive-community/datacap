package io.edurt.datacap.server.configure;

import com.google.inject.Injector;
import io.edurt.datacap.schedule.ScheduledCronRegistrar;
import io.edurt.datacap.server.repository.ScheduledTaskRepository;
import io.edurt.datacap.server.repository.SourceRepository;
import io.edurt.datacap.server.repository.TemplateSqlRepository;
import io.edurt.datacap.server.scheduled.SourceScheduledRunnable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
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
    private final Environment environment;

    public ScheduleTaskRunnerConfigure(Injector injector, ScheduledTaskRepository scheduledTaskRepository, SourceRepository sourceRepository, TemplateSqlRepository templateSqlRepository, ScheduledCronRegistrar scheduledCronRegistrar, RedisTemplate redisTemplate, Environment environment)
    {
        this.injector = injector;
        this.scheduledTaskRepository = scheduledTaskRepository;
        this.sourceRepository = sourceRepository;
        this.templateSqlRepository = templateSqlRepository;
        this.scheduledCronRegistrar = scheduledCronRegistrar;
        this.redisTemplate = redisTemplate;
        this.environment = environment;
    }

    @Override
    public void run(String... args)
    {
        this.scheduledTaskRepository.findAllByActiveIsTrueAndIsSystemIsTrue().forEach(task -> {
            log.info("Add new task " + task.getName() + " to scheduler");
            SourceScheduledRunnable scheduled = new SourceScheduledRunnable(task.getName(), this.injector, this.sourceRepository, templateSqlRepository, redisTemplate, environment);
            this.scheduledCronRegistrar.addCronTask(scheduled, task.getExpression());
        });
    }
}
