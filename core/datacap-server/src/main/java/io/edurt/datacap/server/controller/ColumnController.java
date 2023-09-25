package io.edurt.datacap.server.controller;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.entity.metadata.ColumnEntity;
import io.edurt.datacap.service.repository.metadata.ColumnRepository;
import io.edurt.datacap.service.service.ColumnService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping(value = "/api/v1/column")
public class ColumnController
        extends BaseController<ColumnEntity>
{
    private final ColumnRepository repository;
    private final ColumnService service;

    public ColumnController(ColumnRepository repository, ColumnService service)
    {
        super(repository, service);
        this.repository = repository;
        this.service = service;
    }

    @PostMapping(value = "table/{id}")
    public CommonResponse<List<ColumnEntity>> fetchByTable(@PathVariable Long id)
    {
        return this.service.getAllByTable(id);
    }
}
