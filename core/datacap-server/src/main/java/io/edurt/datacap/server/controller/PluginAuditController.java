package io.edurt.datacap.server.controller;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.entity.PluginAuditEntity;
import io.edurt.datacap.service.repository.PluginAuditRepository;
import io.edurt.datacap.service.service.PluginAuditService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/api/v1/audit/plugin")
public class PluginAuditController
        extends BaseController<PluginAuditEntity>
{
    private final PluginAuditRepository repository;
    private final PluginAuditService service;

    public PluginAuditController(PluginAuditRepository repository, PluginAuditService service)
    {
        super(repository, service);
        this.repository = repository;
        this.service = service;
    }

    @GetMapping(value = "data/{code}")
    public CommonResponse<Object> getData(@PathVariable String code)
    {
        return service.getData(code);
    }
}
