package io.edurt.datacap.server.runner;

import io.edurt.datacap.service.entity.PipelineEntity;
import io.edurt.datacap.service.repository.PipelineRepository;
import io.edurt.datacap.service.service.PipelineService;
import io.edurt.datacap.spi.executor.PipelineState;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.List;

@Slf4j
@Component
public class PipelineResetRunner
        implements ApplicationRunner
{
    @Value(value = "${datacap.pipeline.reset}")
    private String state;

    private final EntityManager entityManager;
    private final PipelineRepository repository;
    private final PipelineService service;

    public PipelineResetRunner(EntityManager entityManager, PipelineRepository repository, PipelineService service)
    {
        this.entityManager = entityManager;
        this.repository = repository;
        this.service = service;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args)
    {
        PipelineState resetState = EnumUtils.getEnum(PipelineState.class, state);
        if (resetState == null) {
            log.warn("Pipeline reset state is not supported: [ {} ] Reset to [ STOPPED ]", state);
            resetState = PipelineState.STOPPED;
        }

        List<PipelineEntity> pipelines = repository.findAllByStateIs(PipelineState.RUNNING);
        for (PipelineEntity pipeline : pipelines) {
            log.info("Reset pipeline [ {} ] user [ {} ]", pipeline.getName(), pipeline.getUser().getUsername());
            pipeline.setState(resetState);
            // If it is reset to the running state, execute it again
            if (resetState.equals(PipelineState.RUNNING)
                    || resetState.equals(PipelineState.CREATED)
                    || resetState.equals(PipelineState.QUEUE)) {
                service.submit(pipeline.entityToBody());
            }
            else {
                pipeline.setMessage("Reset to [ " + resetState + " ]");
                repository.save(pipeline);
            }
            entityManager.flush();
        }
        entityManager.clear();
    }
}
