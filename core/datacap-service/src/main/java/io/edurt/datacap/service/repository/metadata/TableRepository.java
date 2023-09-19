package io.edurt.datacap.service.repository.metadata;

import io.edurt.datacap.service.entity.metadata.DatabaseEntity;
import io.edurt.datacap.service.entity.metadata.TableEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TableRepository
        extends PagingAndSortingRepository<TableEntity, Long>
{
    /**
     * Retrieves a list of TableEntity objects that belong to the specified DatabaseEntity.
     *
     * @param database the DatabaseEntity to filter the TableEntity objects by
     * @return a list of TableEntity objects that belong to the specified DatabaseEntity
     */
    List<TableEntity> findAllByDatabase(DatabaseEntity database);
}
