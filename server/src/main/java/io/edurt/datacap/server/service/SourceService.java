package io.edurt.datacap.server.service;

import io.edurt.datacap.server.body.SharedSourceBody;
import io.edurt.datacap.server.body.SourceBody;
import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.entity.PageEntity;
import io.edurt.datacap.server.entity.PluginEntity;
import io.edurt.datacap.server.entity.SourceEntity;

import java.util.List;
import java.util.Map;

public interface SourceService
{
    Response<SourceEntity> saveOrUpdate(SourceEntity configure);

    Response<PageEntity<SourceEntity>> getAll(int offset, int limit);

    Response<Long> delete(Long id);

    @Deprecated
    Response<Object> testConnection(SourceEntity configure);

    Response<SourceEntity> getById(Long id);

    Response<Map<String, List<PluginEntity>>> getPlugins();

    Response<Long> count();

    Response<Object> shared(SharedSourceBody configure);

    Response<Object> testConnectionV2(SourceBody configure);
}
