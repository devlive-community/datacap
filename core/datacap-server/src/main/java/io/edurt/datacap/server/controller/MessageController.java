package io.edurt.datacap.server.controller;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.body.UserQuestionBody;
import io.edurt.datacap.service.entity.MessageEntity;
import io.edurt.datacap.service.repository.MessageRepository;
import io.edurt.datacap.service.service.MessageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/api/v1/message")
public class MessageController
        extends BaseController<MessageEntity>
{
    private final MessageRepository repository;
    private final MessageService service;

    public MessageController(MessageRepository repository, MessageService service)
    {
        super(repository, service);
        this.repository = repository;
        this.service = service;
    }

    @PostMapping(value = "ai/reply")
    public CommonResponse<MessageEntity> aiReply(@RequestBody UserQuestionBody configure)
    {
        return this.service.aiReply(configure);
    }
}
