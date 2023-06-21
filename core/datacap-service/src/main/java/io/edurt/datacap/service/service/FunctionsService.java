package io.edurt.datacap.service.service;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.body.FunctionsImportBody;
import io.edurt.datacap.service.entity.FunctionsEntity;
import io.edurt.datacap.service.entity.PageEntity;

public interface FunctionsService
{
    CommonResponse<FunctionsEntity> saveOrUpdate(FunctionsEntity configure);

    CommonResponse<PageEntity<FunctionsEntity>> getAllByFilter(FilterBody filter);

    CommonResponse<FunctionsEntity> getById(Long id);

    CommonResponse<Object> batchImport(FunctionsImportBody configure);

    CommonResponse<PageEntity<FunctionsEntity>> getAllByPlugin(String plugin);
}
