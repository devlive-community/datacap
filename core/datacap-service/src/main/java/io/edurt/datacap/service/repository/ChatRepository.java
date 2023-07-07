package io.edurt.datacap.service.repository;

import io.edurt.datacap.service.entity.ChatEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ChatRepository
        extends PagingAndSortingRepository<ChatEntity, Long>
{
}
