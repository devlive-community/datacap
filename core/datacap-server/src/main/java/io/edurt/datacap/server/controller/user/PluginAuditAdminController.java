package io.edurt.datacap.server.controller.user;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.activity.HeatmapActivity;
import io.edurt.datacap.service.itransient.ContributionRadar;
import io.edurt.datacap.service.service.PluginAuditService;
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
    public CommonResponse<Long> count()
    {
        return this.pluginAuditService.count();
    }

    @GetMapping(value = "contribution")
    public CommonResponse<List<HeatmapActivity>> contribution()
    {
        return this.pluginAuditService.getAllContribution();
    }

    @GetMapping(value = "radar")
    public CommonResponse<List<ContributionRadar>> radar()
    {
        return this.pluginAuditService.getContributionRadar();
    }
}
