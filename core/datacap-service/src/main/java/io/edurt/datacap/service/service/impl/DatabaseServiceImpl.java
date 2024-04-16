package io.edurt.datacap.service.service.impl;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.repository.SourceRepository;
import io.edurt.datacap.service.repository.metadata.DatabaseRepository;
import io.edurt.datacap.service.service.DatabaseService;
import org.springframework.stereotype.Service;

@Service
public class DatabaseServiceImpl
        implements DatabaseService
{
    private final DatabaseRepository repository;
    private final SourceRepository sourceRepository;

    public DatabaseServiceImpl(DatabaseRepository repository, SourceRepository sourceRepository)
    {
        this.repository = repository;
        this.sourceRepository = sourceRepository;
    }

    @Override
    public CommonResponse<Object> getAllBySource(String code)
    {
        return sourceRepository.findByCode(code)
                .map(value -> CommonResponse.success(this.repository.findAllBySource(value)))
                .orElse(CommonResponse.failure(String.format("Source [ %s ] not found", code)));
    }
}
