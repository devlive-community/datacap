package io.edurt.datacap.server.controller;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.body.TableFilter;
import io.edurt.datacap.service.entity.metadata.TableEntity;
import io.edurt.datacap.service.repository.metadata.TableRepository;
import io.edurt.datacap.service.service.TableService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping(value = "/api/v1/table")
public class TableController
        extends BaseController<TableEntity>
{
    private final TableRepository repository;
    private final TableService service;

    public TableController(TableRepository repository, TableService service)
    {
        super(repository, service);
        this.repository = repository;
        this.service = service;
    }

    @PostMapping(value = "database/{id}")
    public CommonResponse<List<TableEntity>> fetchByDatabase(@PathVariable Long id)
    {
        return this.service.getAllByDatabase(id);
    }

    @PostMapping(value = "{id}")
    public CommonResponse<Object> fetchDataById(@PathVariable Long id, @RequestBody TableFilter configure)
    {
        return this.service.getDataById(id, configure);
    }
}
