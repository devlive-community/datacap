package io.edurt.datacap.service.service.impl;

import io.edurt.datacap.service.repository.DataSetRepository;
import io.edurt.datacap.service.service.DataSetColumnService;
import org.springframework.stereotype.Service;

@Service
public class DataSetColumnServiceImpl
        implements DataSetColumnService
{
    private final DataSetRepository repository;

    public DataSetColumnServiceImpl(DataSetRepository repository)
    {
        this.repository = repository;
    }
}
