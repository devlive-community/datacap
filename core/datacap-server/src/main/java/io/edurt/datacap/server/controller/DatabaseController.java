package io.edurt.datacap.server.controller;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.entity.DatabaseEntity;
import io.edurt.datacap.service.repository.metadata.DatabaseRepository;
import io.edurt.datacap.service.service.DatabaseService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/api/v1/database")
public class DatabaseController
        extends BaseController<DatabaseEntity>
{
    private final DatabaseRepository repository;
    private final DatabaseService service;

    public DatabaseController(DatabaseRepository repository, DatabaseService service)
    {
        super(repository, service);
        this.repository = repository;
        this.service = service;
    }

    @PostMapping(value = "source/{code}")
    public CommonResponse<Object> fetchBySource(@PathVariable String code)
    {
        return this.service.getAllBySource(code);
    }
}
