package io.edurt.datacap.service.service;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.entity.SnippetEntity;

public interface SnippetService
{
    CommonResponse<SnippetEntity> saveOrUpdate(SnippetEntity configure);

    CommonResponse<PageEntity<SnippetEntity>> getAll(int offset, int limit);

    CommonResponse<PageEntity<SnippetEntity>> getAll(FilterBody filter);

    CommonResponse<Long> delete(Long id);

    CommonResponse<SnippetEntity> getById(Long id);
}
