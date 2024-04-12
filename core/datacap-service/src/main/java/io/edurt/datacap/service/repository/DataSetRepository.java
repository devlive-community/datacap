package io.edurt.datacap.service.repository;

import io.edurt.datacap.service.entity.DataSetEntity;
import io.edurt.datacap.service.entity.UserEntity;
import io.edurt.datacap.service.enums.SyncMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface DataSetRepository
        extends BaseRepository<DataSetEntity, Long>
{
    Page<DataSetEntity> findAllByUser(UserEntity user, Pageable pageable);

    Optional<DataSetEntity> findByCode(String code);

    @Query("SELECT d " +
            "FROM DataSetEntity d " +
            "WHERE d.syncMode = ?1")
    Set<DataSetEntity> findAllBySyncMode(SyncMode mode);
}
