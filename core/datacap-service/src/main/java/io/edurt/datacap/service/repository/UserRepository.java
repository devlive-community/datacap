package io.edurt.datacap.service.repository;

import io.edurt.datacap.service.entity.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository
        extends PagingAndSortingRepository<UserEntity, Long>
{
    Optional<UserEntity> findByUsername(String username);
}
