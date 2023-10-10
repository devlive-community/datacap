package io.edurt.datacap.service.repository.metadata;

import io.edurt.datacap.service.entity.metadata.DatabaseEntity;
import io.edurt.datacap.service.entity.metadata.TableEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

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

    /**
     * Finds all table entities associated with the specified database.
     *
     * @param database the database entity to search for
     * @return a list of table entities
     */
    @Query(value = "SELECT new TableEntity(t.id, t.name, t.createTime) " +
            "FROM TableEntity AS t " +
            "WHERE t.database = :database")
    List<TableEntity> findSimpleAllByDatabase(@Param(value = "database") DatabaseEntity database);
}
