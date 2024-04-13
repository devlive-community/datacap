package io.edurt.datacap.server.controller.admin;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.body.TemplateSqlBody;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.entity.TemplateEntity;
import io.edurt.datacap.service.service.TemplateSqlService;
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

@RestController()
@RequestMapping(value = {"/api/v1/admin/template/sql", "/api/v1/template"})
public class TemplateSqlController
{
    private final TemplateSqlService templateSqlService;

    public TemplateSqlController(TemplateSqlService templateSqlService)
    {
        this.templateSqlService = templateSqlService;
    }

    @PostMapping(value = "list")
    public CommonResponse<PageEntity<TemplateEntity>> getAllByFilter(@RequestBody FilterBody filter)
    {
        return this.templateSqlService.getAllByFilter(filter);
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public CommonResponse<TemplateEntity> save(@RequestBody @Validated(ValidationGroup.Crud.Create.class) TemplateEntity configure)
    {
        return this.templateSqlService.saveOrUpdate(configure);
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public CommonResponse<TemplateEntity> update(@RequestBody @Validated(ValidationGroup.Crud.Update.class) TemplateEntity configure)
    {
        return this.templateSqlService.saveOrUpdate(configure);
    }

    @GetMapping(value = "{id}")
    public CommonResponse<TemplateEntity> getInfo(@PathVariable(value = "id") Long id)
    {
        return this.templateSqlService.getById(id);
    }

    @PostMapping(value = "execute")
    public CommonResponse<Object> execute(@RequestBody TemplateSqlBody configure)
    {
        return this.templateSqlService.execute(configure);
    }
}
