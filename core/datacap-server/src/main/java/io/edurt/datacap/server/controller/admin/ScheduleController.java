package io.edurt.datacap.server.controller.admin;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.entity.ScheduledEntity;
import io.edurt.datacap.service.entity.ScheduledHistoryEntity;
import io.edurt.datacap.service.repository.ScheduledRepository;
import io.edurt.datacap.service.service.ScheduledService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/api/v1/schedule", "/api/v1/admin/schedule"})
public class ScheduleController
{
    private final ScheduledRepository scheduledRepository;
    private final ScheduledService scheduledService;

    public ScheduleController(ScheduledRepository scheduledRepository, ScheduledService scheduledService)
    {
        this.scheduledRepository = scheduledRepository;
        this.scheduledService = scheduledService;
    }

    @PostMapping(value = "list")
    public CommonResponse<PageEntity<ScheduledEntity>> getAllByFilter(@RequestBody FilterBody filter)
    {
        return scheduledService.getAll(scheduledRepository, filter);
    }

    @PostMapping(value = "{id}/history")
    public CommonResponse<PageEntity<ScheduledHistoryEntity>> getHistory(@PathVariable(value = "id") Long id,
            @RequestBody FilterBody filter)
    {
        return scheduledService.getHistory(id, filter);
    }
}
