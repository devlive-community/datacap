package io.edurt.datacap.service.repository;

import io.edurt.datacap.service.entity.TemplateSqlEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TemplateSqlRepository
        extends PagingAndSortingRepository<TemplateSqlEntity, Long>
{
    List<TemplateSqlEntity> findByName(String name);

    TemplateSqlEntity findByNameAndPluginContaining(String name, String word);
}
