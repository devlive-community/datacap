package io.edurt.datacap.server.controller.admin;

import io.edurt.datacap.server.body.FilterBody;
import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.entity.PageEntity;
import io.edurt.datacap.server.entity.ScheduledTaskEntity;
import io.edurt.datacap.server.repository.ScheduledTaskRepository;
import io.edurt.datacap.server.service.ScheduledTaskService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/admin/schedule")
public class ScheduleTaskController
{
    private final ScheduledTaskRepository scheduledTaskRepository;
    private final ScheduledTaskService scheduledTaskService;

    public ScheduleTaskController(ScheduledTaskRepository scheduledTaskRepository, ScheduledTaskService scheduledTaskService)
    {
        this.scheduledTaskRepository = scheduledTaskRepository;
        this.scheduledTaskService = scheduledTaskService;
    }

    @PostMapping(value = "list")
    public Response<PageEntity<ScheduledTaskEntity>> getAllByFilter(@RequestBody FilterBody filter)
    {
        return scheduledTaskService.getAll(scheduledTaskRepository, filter);
    }
}
