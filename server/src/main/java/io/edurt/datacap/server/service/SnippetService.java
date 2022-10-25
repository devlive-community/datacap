package io.edurt.datacap.server.service;

import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.entity.PageEntity;
import io.edurt.datacap.server.entity.SnippetEntity;

public interface SnippetService
{
    Response<SnippetEntity> saveOrUpdate(SnippetEntity configure);

    Response<PageEntity<SnippetEntity>> getAll(int offset, int limit);

    Response<Long> delete(Long id);

    Response<SnippetEntity> getById(Long id);
}
