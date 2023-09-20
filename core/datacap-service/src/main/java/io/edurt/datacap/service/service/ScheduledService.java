package io.edurt.datacap.service.service;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.entity.ScheduledEntity;
import io.edurt.datacap.service.entity.ScheduledHistoryEntity;

public interface ScheduledService
        extends BaseService<ScheduledEntity>
{
    CommonResponse<PageEntity<ScheduledHistoryEntity>> getHistory(Long id, FilterBody filter);
}
