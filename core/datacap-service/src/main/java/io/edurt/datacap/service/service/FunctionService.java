package io.edurt.datacap.service.service;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.body.FunctionsImportBody;
import io.edurt.datacap.service.entity.FunctionEntity;
import io.edurt.datacap.service.entity.PageEntity;

public interface FunctionService
        extends BaseService<FunctionEntity>
{
    CommonResponse<PageEntity<FunctionEntity>> getAllByFilter(FilterBody filter);

    CommonResponse<FunctionEntity> getById(Long id);

    CommonResponse<Object> batchImport(FunctionsImportBody configure);

    CommonResponse<PageEntity<FunctionEntity>> getAllByPlugin(String plugin);
}
