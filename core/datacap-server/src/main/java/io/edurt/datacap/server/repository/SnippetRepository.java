package io.edurt.datacap.server.repository;

import io.edurt.datacap.server.entity.SnippetEntity;
import io.edurt.datacap.server.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SnippetRepository
        extends PagingAndSortingRepository<SnippetEntity, Long>
{
    Page<SnippetEntity> findAllByUser(UserEntity user, Pageable pageable);
}
