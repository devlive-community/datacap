package io.edurt.datacap.service.repository;

import io.edurt.datacap.service.entity.DataSetEntity;
import io.edurt.datacap.service.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface DataSetRepository
        extends PagingAndSortingRepository<DataSetEntity, Long>
{
    Page<DataSetEntity> findAllByUser(UserEntity user, Pageable pageable);

    Optional<DataSetEntity> findByCode(String code);
}
