package io.edurt.datacap.service.service;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.entity.metadata.TableEntity;

import java.util.List;

public interface TableService
        extends BaseService<TableEntity>
{
    /**
     * Retrieves all records from the specified database.
     *
     * @param id the ID of the database
     * @return a common response containing a list of table entities
     */
    CommonResponse<List<TableEntity>> getAllByDatabase(Long id);
}
