package io.edurt.datacap.server.controller.admin;

import io.edurt.datacap.server.body.FilterBody;
import io.edurt.datacap.server.body.TemplateSqlBody;
import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.entity.PageEntity;
import io.edurt.datacap.server.entity.TemplateSqlEntity;
import io.edurt.datacap.server.service.TemplateSqlService;
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
@RequestMapping(value = "/api/v1/admin/template/sql")
public class TemplateSqlController
{
    private final TemplateSqlService templateSqlService;

    public TemplateSqlController(TemplateSqlService templateSqlService)
    {
        this.templateSqlService = templateSqlService;
    }

    @PostMapping(value = "list")
    public Response<PageEntity<TemplateSqlEntity>> getAllByFilter(@RequestBody FilterBody filter)
    {
        return this.templateSqlService.getAllByFilter(filter);
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public Response<TemplateSqlEntity> save(@RequestBody @Validated(ValidationGroup.Crud.Create.class) TemplateSqlEntity configure)
    {
        return this.templateSqlService.saveOrUpdate(configure);
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public Response<TemplateSqlEntity> update(@RequestBody @Validated(ValidationGroup.Crud.Update.class) TemplateSqlEntity configure)
    {
        return this.templateSqlService.saveOrUpdate(configure);
    }

    @GetMapping(value = "{id}")
    public Response<TemplateSqlEntity> getInfo(@PathVariable(value = "id") Long id)
    {
        return this.templateSqlService.getById(id);
    }

    @PostMapping(value = "execute")
    public Response<Object> execute(@RequestBody TemplateSqlBody configure)
    {
        return this.templateSqlService.execute(configure);
    }
}
