package io.edurt.datacap.service.service.impl;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.adapter.PageRequestAdapter;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.entity.ChatEntity;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.repository.ChatRepository;
import io.edurt.datacap.service.service.ChatService;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl
        implements ChatService
{
    private final ChatRepository repository;

    public ChatServiceImpl(ChatRepository repository)
    {
        this.repository = repository;
    }

    @Override
    public CommonResponse<PageEntity<ChatEntity>> getAll(PagingAndSortingRepository repository, FilterBody filter)
    {
        return CommonResponse.success(PageEntity.build(repository.findAll(PageRequestAdapter.of(filter))));
    }
}
