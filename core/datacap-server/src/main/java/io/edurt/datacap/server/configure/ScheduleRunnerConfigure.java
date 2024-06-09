package io.edurt.datacap.server.configure;

import com.google.inject.Injector;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.schedule.ScheduledCronRegistrar;
import io.edurt.datacap.service.repository.ScheduledRepository;
import io.edurt.datacap.service.repository.SourceRepository;
import io.edurt.datacap.service.service.SourceService;
import io.edurt.datacap.service.source.CheckScheduledRunnable;
import io.edurt.datacap.service.source.SyncMetadataScheduledRunnable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@SuppressFBWarnings(value = {"EI_EXPOSE_REP2"})
public class ScheduleRunnerConfigure
        implements CommandLineRunner
{
    private final Injector injector;
    private final ScheduledRepository scheduledRepository;
    private final SourceRepository sourceRepository;
    private final SourceService sourceService;
    private final ScheduledCronRegistrar scheduledCronRegistrar;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public ScheduleRunnerConfigure(Injector injector, ScheduledRepository scheduledRepository, SourceRepository sourceRepository, SourceService sourceService, ScheduledCronRegistrar scheduledCronRegistrar)
    {
        this.injector = injector;
        this.scheduledRepository = scheduledRepository;
        this.sourceRepository = sourceRepository;
        this.sourceService = sourceService;
        this.scheduledCronRegistrar = scheduledCronRegistrar;
    }

    @Override
    public void run(String... args)
    {
        this.scheduledRepository.findAllByActiveIsTrueAndIsSystemIsTrue()
                .forEach(task -> {
                    log.info("Add new task [ {} ] to scheduler", task.getName());
                    switch (task.getType()) {
                        case SOURCE_SYNCHRONIZE:
                            SyncMetadataScheduledRunnable syncMetadataScheduledRunnable = new SyncMetadataScheduledRunnable(task.getName(), injector, sourceRepository, sourceService);
                            this.scheduledCronRegistrar.addCronTask(syncMetadataScheduledRunnable, task.getExpression());
                            executorService.submit(syncMetadataScheduledRunnable);
                            break;
                        case SOURCE_CHECK:
                            CheckScheduledRunnable checkScheduledRunnable = new CheckScheduledRunnable(task.getName(), this.injector, this.sourceRepository);
                            this.scheduledCronRegistrar.addCronTask(checkScheduledRunnable, task.getExpression());
                            executorService.submit(checkScheduledRunnable);
                            break;
                        default:
                            log.warn("Unsupported task type [ {} ]", task.getType());
                    }
                });
    }
}
