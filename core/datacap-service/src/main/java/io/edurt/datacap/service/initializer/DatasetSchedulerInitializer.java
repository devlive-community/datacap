package io.edurt.datacap.service.initializer;

import com.google.inject.Injector;
import io.edurt.datacap.common.utils.SpiUtils;
import io.edurt.datacap.scheduler.SchedulerRequest;
import io.edurt.datacap.service.enums.SyncMode;
import io.edurt.datacap.service.initializer.job.DatasetJob;
import io.edurt.datacap.service.repository.DataSetRepository;
import io.edurt.datacap.service.service.DataSetService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DatasetSchedulerInitializer
        implements CommandLineRunner
{
    private final Injector injector;
    private final DataSetRepository repository;
    private final DataSetService service;
    private final Scheduler scheduler;

    public DatasetSchedulerInitializer(Injector injector, DataSetRepository repository, DataSetService service, Scheduler scheduler)
    {
        this.injector = injector;
        this.repository = repository;
        this.service = service;
        this.scheduler = scheduler;
    }

    @Override
    public void run(String... args)
            throws Exception
    {
        log.info("Start dataset scheduler initializer");
        this.repository.findAllBySyncMode(SyncMode.TIMING)
                .forEach(item -> {
                    log.info("Dataset [ {} ] will be scheduled", item.getName());
                    SpiUtils.findSchedule(this.injector, item.getScheduler())
                            .ifPresent(scheduler -> {
                                SchedulerRequest request = new SchedulerRequest();
                                request.setName(item.getId().toString());
                                request.setGroup("datacap");
                                request.setExpression(item.getExpression());
                                request.setJobId(String.valueOf(item.getId()));
                                request.setCreateBeforeDelete(true);
                                if (scheduler.name().equals("Default")) {
                                    request.setJob(new DatasetJob());
                                    request.setScheduler(this.scheduler);
                                }
                                scheduler.initialize(request);
                            });
                });
        log.info("End dataset scheduler initializer");
    }
}
