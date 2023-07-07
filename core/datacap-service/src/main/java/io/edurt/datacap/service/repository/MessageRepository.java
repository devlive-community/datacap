package io.edurt.datacap.service.repository;

import io.edurt.datacap.service.entity.ChatEntity;
import io.edurt.datacap.service.entity.MessageEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface MessageRepository
        extends PagingAndSortingRepository<MessageEntity, Long>
{
    List<MessageEntity> findAllByChatOrderByCreateTimeAsc(ChatEntity chat);
}
