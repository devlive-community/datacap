package io.edurt.datacap.service.service;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.body.UserQuestionBody;
import io.edurt.datacap.service.entity.MessageEntity;

import java.util.List;

public interface MessageService
        extends BaseService<MessageEntity>
{
    CommonResponse<List<MessageEntity>> getMessageByChatAndUser(Long chatId);

    CommonResponse<MessageEntity> aiReply(UserQuestionBody configure);
}
