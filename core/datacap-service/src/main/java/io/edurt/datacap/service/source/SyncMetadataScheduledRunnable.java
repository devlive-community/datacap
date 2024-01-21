package io.edurt.datacap.service.source;

import com.google.inject.Injector;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.schedule.ScheduledRunnable;
import io.edurt.datacap.service.repository.SourceRepository;
import io.edurt.datacap.service.service.SourceService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressFBWarnings(value = {"REC_CATCH_EXCEPTION"})
public class SyncMetadataScheduledRunnable
        extends ScheduledRunnable
{
    private final Injector injector;
    private final SourceRepository sourceHandler;
    private final SourceService sourceService;

    public SyncMetadataScheduledRunnable(String name, Injector injector, SourceRepository sourceHandler, SourceService sourceService)
    {
        super(name);
        this.injector = injector;
        this.sourceHandler = sourceHandler;
        this.sourceService = sourceService;
    }

    /**
     * Executes the run method of the Runnable interface.
     */
    @Override
    public void run()
    {
        sourceHandler.findAll()
                .stream()
                .parallel()
                .forEach(entity -> sourceService.syncMetadata(entity.getId()));
    }
}
