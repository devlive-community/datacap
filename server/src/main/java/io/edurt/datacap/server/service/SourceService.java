package io.edurt.datacap.server.service;

import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.entity.PageEntity;
import io.edurt.datacap.server.entity.SourceEntity;

public interface SourceService
{
    Response<SourceEntity> saveOrUpdate(SourceEntity configure);

    Response<PageEntity<SourceEntity>> getAll(int offset, int limit);

    Response<Long> delete(Long id);

    Response<Object> testConnection(SourceEntity configure);
}
