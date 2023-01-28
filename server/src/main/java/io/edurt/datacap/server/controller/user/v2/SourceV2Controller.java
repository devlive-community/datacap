package io.edurt.datacap.server.controller.user.v2;

import io.edurt.datacap.server.body.SourceBody;
import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.entity.SourceEntity;
import io.edurt.datacap.server.service.SourceService;
import io.edurt.datacap.server.validation.ValidationGroup;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public Response<SourceEntity> save(@RequestBody @Validated(ValidationGroup.Crud.Create.class) SourceBody configure)
    {
        return this.sourceService.saveOrUpdateV2(configure);
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public Response<SourceEntity> update(@RequestBody @Validated(ValidationGroup.Crud.Update.class) SourceBody configure)
    {
        return this.sourceService.saveOrUpdateV2(configure);
    }

    @GetMapping(value = "{id}")
    public Response<SourceEntity> getInfo(@PathVariable(value = "id") Long id)
    {
        return this.sourceService.getByIdV2(id);
    }
}
