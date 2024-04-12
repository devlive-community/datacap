package io.edurt.datacap.service.repository;

import io.edurt.datacap.service.entity.ChatEntity;
import io.edurt.datacap.service.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatRepository
        extends BaseRepository<ChatEntity, Long>
{
    Page<ChatEntity> findAllByUser(UserEntity user, Pageable pageable);
}
