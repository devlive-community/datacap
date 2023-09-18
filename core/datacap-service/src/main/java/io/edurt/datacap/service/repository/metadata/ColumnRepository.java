package io.edurt.datacap.service.repository.metadata;

import io.edurt.datacap.service.entity.metadata.ColumnEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ColumnRepository
        extends PagingAndSortingRepository<ColumnEntity, Long>
{
}
