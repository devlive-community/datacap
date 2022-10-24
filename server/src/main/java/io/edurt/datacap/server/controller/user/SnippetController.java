package io.edurt.datacap.server.controller.user;

import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.entity.PageEntity;
import io.edurt.datacap.server.entity.SnippetEntity;
import io.edurt.datacap.server.service.SnippetService;
import io.edurt.datacap.server.validation.ValidationGroup;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
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

@RestController()
@RequestMapping(value = "/api/v1/snippet")
public class SnippetController
{
    private final SnippetService snippetService;

    public SnippetController(SnippetService snippetService)
    {
        this.snippetService = snippetService;
    }

    @GetMapping
    public Response<PageEntity<SnippetEntity>> getAll(@RequestParam(value = "page", defaultValue = "1") int start,
            @RequestParam(value = "size", defaultValue = "10") int end)
    {
        return this.snippetService.getAll(start, end);
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public Response<SnippetEntity> save(@RequestBody @Validated(ValidationGroup.Crud.Create.class) SnippetEntity configure)
    {
        return this.snippetService.saveOrUpdate(configure);
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public Response<SnippetEntity> update(@RequestBody @Validated(ValidationGroup.Crud.Update.class) SnippetEntity configure)
    {
        return this.snippetService.saveOrUpdate(configure);
    }

    @PreAuthorize(value = "@snippetAuthorize.validateById(#id)")
    @DeleteMapping(value = "{id}")
    public Response<Long> delete(@PathVariable(value = "id") Long id)
    {
        return this.snippetService.delete(id);
    }
}
