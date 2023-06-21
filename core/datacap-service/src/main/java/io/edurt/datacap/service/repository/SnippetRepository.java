package io.edurt.datacap.service.repository;

import io.edurt.datacap.service.entity.SnippetEntity;
import io.edurt.datacap.service.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SnippetRepository
        extends PagingAndSortingRepository<SnippetEntity, Long>
{
    Page<SnippetEntity> findAllByUser(UserEntity user, Pageable pageable);
}
