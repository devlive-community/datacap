package io.edurt.datacap.server.controller.user.v2;

import io.edurt.datacap.server.body.SourceBody;
import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.service.SourceService;
import io.edurt.datacap.server.validation.ValidationGroup;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/api/v2/source")
public class SourceV2Controller
{
    private final SourceService sourceService;

    public SourceV2Controller(SourceService sourceService)
    {
        this.sourceService = sourceService;
    }

    @PostMapping(value = "test", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Response<Object> testConnectionV2(@RequestBody @Validated(ValidationGroup.Crud.Create.class) SourceBody configure)
    {
        return this.sourceService.testConnectionV2(configure);
    }
}
