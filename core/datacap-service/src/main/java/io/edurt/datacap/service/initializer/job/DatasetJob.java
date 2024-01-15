package io.edurt.datacap.service.initializer.job;

import io.edurt.datacap.service.service.DataSetService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DatasetJob
        extends QuartzJobBean
{
    private DataSetService service;

    @Autowired
    public void setService(DataSetService service)
    {
        this.service = service;
    }

    @Override
    protected void executeInternal(JobExecutionContext context)
    {
        Long id = Long.valueOf(context.getJobDetail().getJobDataMap().get("id").toString());
        log.info("Job [ {} ] run time [ {} ]", id, context.getFireTime().getTime());
        this.service.syncData(id);
    }
}
