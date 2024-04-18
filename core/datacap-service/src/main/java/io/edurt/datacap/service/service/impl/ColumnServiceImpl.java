package io.edurt.datacap.service.service.impl;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.entity.ColumnEntity;
import io.edurt.datacap.service.repository.metadata.ColumnRepository;
import io.edurt.datacap.service.repository.metadata.TableRepository;
import io.edurt.datacap.service.service.ColumnService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColumnServiceImpl
        implements ColumnService
{
    private final TableRepository tableRepository;
    private final ColumnRepository repository;

    public ColumnServiceImpl(TableRepository tableRepository, ColumnRepository repository)
    {
        this.tableRepository = tableRepository;
        this.repository = repository;
    }

    @Override
    public CommonResponse<List<ColumnEntity>> getAllByTable(String code)
    {
        return tableRepository.findByCode(code)
                .map(value -> CommonResponse.success(repository.findAllByTable(value)))
                .orElseGet(() -> CommonResponse.failure(String.format("Table [ %s ] not found", code)));
    }
}
