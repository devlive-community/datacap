package io.edurt.datacap.service.repository;

import io.edurt.datacap.service.entity.TemplateEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TemplateSqlRepository
        extends PagingAndSortingRepository<TemplateEntity, Long>
{
    List<TemplateEntity> findByName(String name);

    TemplateEntity findByNameAndPluginContaining(String name, String word);
}
