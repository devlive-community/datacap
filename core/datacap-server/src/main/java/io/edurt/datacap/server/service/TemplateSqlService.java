package io.edurt.datacap.server.service;

import io.edurt.datacap.server.body.FilterBody;
import io.edurt.datacap.server.body.TemplateSqlBody;
import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.entity.PageEntity;
import io.edurt.datacap.server.entity.TemplateSqlEntity;

public interface TemplateSqlService
{
    Response<TemplateSqlEntity> saveOrUpdate(TemplateSqlEntity configure);

    Response<PageEntity<TemplateSqlEntity>> getAllByFilter(FilterBody filter);

    Response<TemplateSqlEntity> getById(Long id);

    Response<Object> execute(TemplateSqlBody configure);
}
