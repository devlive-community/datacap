package io.edurt.datacap.service.service.impl;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.entity.ColumnEntity;
import io.edurt.datacap.service.entity.TableEntity;
import io.edurt.datacap.service.repository.metadata.ColumnRepository;
import io.edurt.datacap.service.service.ColumnService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColumnServiceImpl
        implements ColumnService
{
    private final ColumnRepository repository;

    public ColumnServiceImpl(ColumnRepository repository)
    {
        this.repository = repository;
    }

    @Override
    public CommonResponse<List<ColumnEntity>> getAllByTable(Long id)
    {
        TableEntity table = TableEntity.builder()
                .id(id)
                .build();
        return CommonResponse.success(this.repository.findAllByTable(table));
    }
}
