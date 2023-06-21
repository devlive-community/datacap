package io.edurt.datacap.service.service;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.body.TemplateSqlBody;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.entity.TemplateSqlEntity;

public interface TemplateSqlService
{
    CommonResponse<TemplateSqlEntity> saveOrUpdate(TemplateSqlEntity configure);

    CommonResponse<PageEntity<TemplateSqlEntity>> getAllByFilter(FilterBody filter);

    CommonResponse<TemplateSqlEntity> getById(Long id);

    CommonResponse<Object> execute(TemplateSqlBody configure);
}
