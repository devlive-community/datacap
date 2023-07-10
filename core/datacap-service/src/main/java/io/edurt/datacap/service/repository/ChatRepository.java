package io.edurt.datacap.service.repository;

import io.edurt.datacap.service.entity.ChatEntity;
import io.edurt.datacap.service.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ChatRepository
        extends PagingAndSortingRepository<ChatEntity, Long>
{
    Page<ChatEntity> findAllByUser(UserEntity user, Pageable pageable);
}
