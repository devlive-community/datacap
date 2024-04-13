package io.edurt.datacap.server.controller;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.body.FunctionsImportBody;
import io.edurt.datacap.service.entity.FunctionEntity;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.repository.FunctionsRepository;
import io.edurt.datacap.service.service.FunctionService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = {"/api/v1/admin/function", "/api/v1/function"})
public class FunctionController
        extends BaseController<FunctionEntity>
{
    private final FunctionsRepository repository;
    private final FunctionService service;

    public FunctionController(FunctionsRepository repository, FunctionService service)
    {
        super(repository, service);
        this.repository = repository;
        this.service = service;
    }

    @PutMapping(value = "import")
    public CommonResponse<Object> batchImport(@RequestBody @Validated FunctionsImportBody configure)
    {
        return this.service.batchImport(configure);
    }

    @GetMapping(value = "list/{plugin}")
    public CommonResponse<PageEntity<FunctionEntity>> getAllByPlugin(@PathVariable(value = "plugin") String plugin)
    {
        return this.service.getAllByPlugin(plugin);
    }
}
