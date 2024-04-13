package io.edurt.datacap.service.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface BaseRepository<T, ID>
        extends PagingAndSortingRepository<T, ID>
{
    Optional<T> findByCode(String code);
}
