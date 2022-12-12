package io.edurt.datacap.server.service;

import io.edurt.datacap.server.body.FilterBody;
import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.entity.FunctionsEntity;
import io.edurt.datacap.server.entity.PageEntity;

public interface FunctionsService
{
    Response<FunctionsEntity> saveOrUpdate(FunctionsEntity configure);

    Response<PageEntity<FunctionsEntity>> getAllByFilter(FilterBody filter);

    Response<FunctionsEntity> getById(Long id);
}
