package io.edurt.datacap.service.service;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.entity.metadata.ColumnEntity;

import java.util.List;

public interface ColumnService
        extends BaseService<ColumnEntity>
{
    /**
     * Retrieves all the column entities associated with a specific table.
     *
     * @param id the ID of the table
     * @return a common response containing a list of column entities
     */
    CommonResponse<List<ColumnEntity>> getAllByTable(Long id);
}
