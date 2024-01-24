package io.edurt.datacap.service.repository;

import io.edurt.datacap.service.entity.DataSetEntity;
import io.edurt.datacap.service.entity.DatasetHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DatasetHistoryRepository
        extends PagingAndSortingRepository<DatasetHistoryEntity, Long>
{
    Page<DatasetHistoryEntity> findAllByDatasetOrderByCreateTimeDesc(DataSetEntity dataSet, Pageable pageable);
}
