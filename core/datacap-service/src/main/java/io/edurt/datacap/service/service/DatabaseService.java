package io.edurt.datacap.service.service;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.entity.metadata.DatabaseEntity;

import java.util.List;

public interface DatabaseService
        extends BaseService<DatabaseEntity>
{
    /**
     * Retrieves a list of DatabaseEntity objects based on the provided id.
     *
     * @param id the id used to filter the results
     * @return a list of DatabaseEntity objects that match the id
     */
    CommonResponse<List<DatabaseEntity>> getAllBySource(Long id);
}
