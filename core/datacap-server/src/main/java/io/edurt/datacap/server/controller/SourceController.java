package io.edurt.datacap.server.controller;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.body.SourceBody;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.entity.ScheduledHistoryEntity;
import io.edurt.datacap.service.entity.SourceEntity;
import io.edurt.datacap.service.repository.SourceRepository;
import io.edurt.datacap.service.service.SourceService;
import io.edurt.datacap.service.validation.ValidationGroup;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "sourceControllerV2")
@RequestMapping(value = "/api/v2/source")
public class SourceController
        extends BaseController<SourceEntity>
{
    private final SourceRepository repository;
    private final SourceService service;

    public SourceController(SourceRepository repository, SourceService service)
    {
        super(repository, service);
        this.repository = repository;
        this.service = service;
    }

    @PostMapping(value = "test", produces = {MediaType.APPLICATION_JSON_VALUE})
    public CommonResponse<Object> testConnectionV2(@RequestBody @Validated(ValidationGroup.Crud.Create.class) SourceBody configure)
    {
        return this.service.testConnectionV2(configure);
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public CommonResponse<SourceEntity> save(@RequestBody @Validated(ValidationGroup.Crud.Create.class) SourceBody configure)
    {
        return this.service.saveOrUpdateV2(configure);
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public CommonResponse<SourceEntity> update(@RequestBody @Validated(ValidationGroup.Crud.Update.class) SourceBody configure)
    {
        return this.service.saveOrUpdateV2(configure);
    }

    @GetMapping(value = "code/{code}")
    public CommonResponse<SourceEntity> getByCode(@PathVariable(value = "code") String code)
    {
        return this.service.getByCode(code);
    }

    @PostMapping(value = "getHistory/{id}")
    public CommonResponse<PageEntity<ScheduledHistoryEntity>> getHistory(@PathVariable(value = "id") Long id, @RequestBody FilterBody filter)
    {
        return this.service.getHistory(id, filter);
    }

    @PutMapping(value = "syncMetadata/{id}")
    public CommonResponse<SourceEntity> syncMetadata(@PathVariable(value = "id") Long id)
    {
        return this.service.syncMetadata(id);
    }
}
