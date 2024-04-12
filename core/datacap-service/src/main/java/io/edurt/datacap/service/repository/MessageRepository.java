package io.edurt.datacap.service.repository;

import io.edurt.datacap.service.entity.ChatEntity;
import io.edurt.datacap.service.entity.MessageEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository
        extends BaseRepository<MessageEntity, Long>
{
    List<MessageEntity> findAllByChatOrderByCreateTimeAsc(ChatEntity chat);

    @Query(value = "SELECT m " +
            "FROM MessageEntity AS m " +
            "WHERE m.chat = ?1 AND m.id < ?2 " +
            "ORDER BY m.createTime DESC")
    List<MessageEntity> findTopByChatOrderByCreateTimeDesc(ChatEntity chat, long id, Pageable pageable);
}
