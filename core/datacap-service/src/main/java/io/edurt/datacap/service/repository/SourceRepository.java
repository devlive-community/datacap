package io.edurt.datacap.service.repository;

import io.edurt.datacap.service.entity.SourceEntity;
import io.edurt.datacap.service.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface SourceRepository
        extends PagingAndSortingRepository<SourceEntity, Long>
{
    List<SourceEntity> findAll();

    SourceEntity findByName(String name);

    Page<SourceEntity> findAllByUserOrPublishIsTrue(UserEntity user, Pageable pageable);

    Long countByUserOrPublishIsTrue(UserEntity user);
}
