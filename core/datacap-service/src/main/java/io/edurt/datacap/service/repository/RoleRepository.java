package io.edurt.datacap.service.repository;

import io.edurt.datacap.service.entity.RoleEntity;

import java.util.Optional;

public interface RoleRepository
        extends BaseRepository<RoleEntity, Long>
{
    Optional<RoleEntity> findByName(String name);
}
