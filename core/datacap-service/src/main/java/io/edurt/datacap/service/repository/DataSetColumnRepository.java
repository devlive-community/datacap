package io.edurt.datacap.service.repository;

import io.edurt.datacap.service.entity.DataSetColumnEntity;
import io.edurt.datacap.service.entity.DataSetEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface DataSetColumnRepository
        extends PagingAndSortingRepository<DataSetColumnEntity, Long>
{
    List<DataSetColumnEntity> findAllByDataset(DataSetEntity dataset);

    List<DataSetColumnEntity> findAllByDatasetOrderByPositionAsc(DataSetEntity dataset);
}
