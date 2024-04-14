package io.edurt.datacap.service.repository;

import io.edurt.datacap.service.entity.SnippetEntity;
import io.edurt.datacap.service.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SnippetRepository
        extends BaseRepository<SnippetEntity, Long>
{
    Page<SnippetEntity> findAllByUser(UserEntity user, Pageable pageable);
}
