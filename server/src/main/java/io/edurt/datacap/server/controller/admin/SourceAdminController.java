package io.edurt.datacap.server.controller.admin;

import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.service.SourceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/source/admin")
public class SourceAdminController
{
    private final SourceService sourceService;

    public SourceAdminController(SourceService sourceService)
    {
        this.sourceService = sourceService;
    }

    @GetMapping(value = "count")
    public Response<Long> count()
    {
        return this.sourceService.count();
    }
}
