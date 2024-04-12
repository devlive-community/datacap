package io.edurt.datacap.service.service;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.body.SharedSourceBody;
import io.edurt.datacap.service.body.SourceBody;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.entity.PluginEntity;
import io.edurt.datacap.service.entity.ScheduledHistoryEntity;
import io.edurt.datacap.service.entity.SourceEntity;

import java.util.List;
import java.util.Map;

public interface SourceService
        extends BaseService<SourceEntity>
{
    @Deprecated
    CommonResponse<SourceEntity> saveOrUpdate(SourceEntity configure);

    CommonResponse<Long> delete(Long id);

    @Deprecated
    CommonResponse<Object> testConnection(SourceEntity configure);

    CommonResponse<SourceEntity> getByCode(String code);

    CommonResponse<Map<String, List<PluginEntity>>> getPlugins();

    CommonResponse<Long> count();

    CommonResponse<Object> shared(SharedSourceBody configure);

    CommonResponse<Object> testConnectionV2(SourceBody configure);

    CommonResponse<SourceEntity> saveOrUpdateV2(SourceBody configure);

    CommonResponse<PageEntity<ScheduledHistoryEntity>> getHistory(Long id, FilterBody filter);

    CommonResponse<SourceEntity> syncMetadata(Long id);
}
