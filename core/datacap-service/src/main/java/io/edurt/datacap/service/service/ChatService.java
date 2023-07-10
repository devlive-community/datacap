package io.edurt.datacap.service.service;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.entity.ChatEntity;
import io.edurt.datacap.service.entity.PageEntity;

public interface ChatService
        extends BaseService<ChatEntity>
{
    CommonResponse<PageEntity<ChatEntity>> getAllByUser(FilterBody filter);
}
