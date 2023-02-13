package io.edurt.datacap.server.service;

import io.edurt.datacap.server.body.FilterBody;
import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.entity.PageEntity;
import io.edurt.datacap.server.entity.UserLogEntity;

public interface UserLogService
{
    Response<PageEntity<UserLogEntity>> getAllByFilter(FilterBody filter);
}
