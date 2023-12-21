package io.edurt.datacap.service.service;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.body.DataSetBody;
import io.edurt.datacap.service.entity.DataSetEntity;

public interface DataSetService
        extends BaseService<DataSetEntity>
{
    CommonResponse<DataSetEntity> saveOrUpdate(DataSetBody configure);
}
