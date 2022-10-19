package io.edurt.datacap.server.repository;

import io.edurt.datacap.server.entity.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository
        extends PagingAndSortingRepository<UserEntity, Long>
{
    Optional<UserEntity> findByUsername(String username);
}
