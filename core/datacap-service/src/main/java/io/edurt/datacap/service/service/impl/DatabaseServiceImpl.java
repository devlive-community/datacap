package io.edurt.datacap.service.service.impl;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.entity.SourceEntity;
import io.edurt.datacap.service.entity.metadata.DatabaseEntity;
import io.edurt.datacap.service.repository.metadata.DatabaseRepository;
import io.edurt.datacap.service.service.DatabaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseServiceImpl
        implements DatabaseService
{
    private final DatabaseRepository repository;

    public DatabaseServiceImpl(DatabaseRepository repository)
    {
        this.repository = repository;
    }

    @Override
    public CommonResponse<List<DatabaseEntity>> getAllBySource(Long id)
    {
        SourceEntity source = SourceEntity.builder()
                .id(id)
                .build();
        return CommonResponse.success(this.repository.findAllBySource(source));
    }
}
