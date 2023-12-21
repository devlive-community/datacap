package io.edurt.datacap.server.controller;

import io.edurt.datacap.service.entity.DataSetEntity;
import io.edurt.datacap.service.repository.DataSetRepository;
import io.edurt.datacap.service.service.DataSetService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/api/v1/dataset")
public class DataSetController
        extends BaseController<DataSetEntity>
{
    private final DataSetRepository repository;
    private final DataSetService service;

    protected DataSetController(DataSetRepository repository, DataSetService service)
    {
        super(repository, service);
        this.repository = repository;
        this.service = service;
    }
}
