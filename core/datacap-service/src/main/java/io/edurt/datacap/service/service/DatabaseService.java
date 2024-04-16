package io.edurt.datacap.service.service;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.entity.DatabaseEntity;

public interface DatabaseService
        extends BaseService<DatabaseEntity>
{
    /**
     * Retrieves a list of DatabaseEntity objects based on the provided id.
     *
     * @param code the code used to filter the results
     * @return a list of DatabaseEntity objects that match the id
     */
    CommonResponse<Object> getAllBySource(String code);
}
