package io.edurt.datacap.service.service.impl;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.entity.metadata.DatabaseEntity;
import io.edurt.datacap.service.entity.metadata.TableEntity;
import io.edurt.datacap.service.repository.metadata.TableRepository;
import io.edurt.datacap.service.service.TableService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableServiceImpl
        implements TableService
{
    private final TableRepository repository;

    public TableServiceImpl(TableRepository repository)
    {
        this.repository = repository;
    }

    @Override
    public CommonResponse<List<TableEntity>> getAllByDatabase(Long id)
    {
        DatabaseEntity database = DatabaseEntity.builder()
                .id(id)
                .build();
        return CommonResponse.success(this.repository.findAllByDatabase(database));
    }
}
