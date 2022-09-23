package io.edurt.datacap.server.controller;

import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.entity.PageEntity;
import io.edurt.datacap.server.entity.PluginEntity;
import io.edurt.datacap.server.entity.SourceEntity;
import io.edurt.datacap.server.service.SourceService;
import io.edurt.datacap.server.validation.ValidationGroup;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping(value = "/api/v1/source")
public class SourceController
{
    private final SourceService sourceService;

    public SourceController(SourceService sourceService)
    {
        this.sourceService = sourceService;
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public Response<SourceEntity> save(@RequestBody @Validated(ValidationGroup.Crud.Create.class) SourceEntity configure)
    {
        return this.sourceService.saveOrUpdate(configure);
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public Response<SourceEntity> update(@RequestBody @Validated(ValidationGroup.Crud.Update.class) SourceEntity configure)
    {
        return this.sourceService.saveOrUpdate(configure);
    }

    @GetMapping
    public Response<PageEntity<SourceEntity>> getAll(@RequestParam(value = "page", defaultValue = "1") int start,
            @RequestParam(value = "size", defaultValue = "10") int end)
    {
        return this.sourceService.getAll(start, end);
    }

    @DeleteMapping(value = "{id}")
    public Response<Long> delete(@PathVariable(value = "id") Long id)
    {
        return this.sourceService.delete(id);
    }

    @PostMapping(value = "test", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Response<Object> testConnection(@RequestBody @Validated(ValidationGroup.Crud.Create.class) SourceEntity configure)
    {
        return this.sourceService.testConnection(configure);
    }

    @GetMapping(value = "{id}")
    public Response<SourceEntity> getInfo(@PathVariable(value = "id") Long id)
    {
        return this.sourceService.getById(id);
    }

    @GetMapping(value = "plugins")
    public Response<List<PluginEntity>> getPlugins()
    {
        return this.sourceService.getPlugins();
    }
}
