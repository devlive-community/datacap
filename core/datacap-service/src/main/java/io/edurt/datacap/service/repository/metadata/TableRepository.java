package io.edurt.datacap.service.repository.metadata;

import io.edurt.datacap.service.entity.metadata.TableEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TableRepository
        extends PagingAndSortingRepository<TableEntity, Long>
{
}
