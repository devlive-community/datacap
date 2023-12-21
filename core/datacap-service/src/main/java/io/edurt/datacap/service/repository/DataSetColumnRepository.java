package io.edurt.datacap.service.repository;

import io.edurt.datacap.service.entity.DataSetColumnEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DataSetColumnRepository
        extends PagingAndSortingRepository<DataSetColumnEntity, Long>
{
}
