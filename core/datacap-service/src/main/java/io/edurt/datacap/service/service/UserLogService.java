package io.edurt.datacap.service.service;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.entity.UserLogEntity;

public interface UserLogService
{
    CommonResponse<PageEntity<UserLogEntity>> getAllByFilter(FilterBody filter);
}
