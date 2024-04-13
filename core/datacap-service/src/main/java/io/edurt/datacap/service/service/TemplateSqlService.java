package io.edurt.datacap.service.service;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.body.TemplateSqlBody;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.entity.TemplateEntity;

public interface TemplateSqlService
{
    CommonResponse<TemplateEntity> saveOrUpdate(TemplateEntity configure);

    CommonResponse<PageEntity<TemplateEntity>> getAllByFilter(FilterBody filter);

    CommonResponse<TemplateEntity> getById(Long id);

    CommonResponse<Object> execute(TemplateSqlBody configure);
}
