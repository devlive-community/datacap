package io.edurt.datacap.server.controller.user;

import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.itransient.ContributionHistory;
import io.edurt.datacap.server.service.PluginAuditService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping(value = "/api/v1/audit/plugin/admin")
public class PluginAuditAdminController
{
    private final PluginAuditService pluginAuditService;

    public PluginAuditAdminController(PluginAuditService pluginAuditService)
    {
        this.pluginAuditService = pluginAuditService;
    }

    @GetMapping(value = "count")
    public Response<Long> count()
    {
        return this.pluginAuditService.count();
    }

    @GetMapping(value = "contribution")
    public Response<List<ContributionHistory>> contribution()
    {
        return this.pluginAuditService.getAllContribution();
    }
}
