package io.edurt.datacap.server.controller.admin;

import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.service.PluginAuditService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
