package io.edurt.datacap.service.service;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.entity.DataSetEntity;
import io.edurt.datacap.service.entity.PageEntity;

public interface DataSetService
        extends BaseService<DataSetEntity>
{
    CommonResponse<PageEntity<DataSetEntity>> getAll(FilterBody filter);
}
