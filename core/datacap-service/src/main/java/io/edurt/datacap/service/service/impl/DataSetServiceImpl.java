package io.edurt.datacap.service.service.impl;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.adapter.PageRequestAdapter;
import io.edurt.datacap.service.body.DataSetBody;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.entity.DataSetEntity;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.repository.DataSetColumnRepository;
import io.edurt.datacap.service.repository.DataSetRepository;
import io.edurt.datacap.service.security.UserDetailsService;
import io.edurt.datacap.service.service.DataSetService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class DataSetServiceImpl
        implements DataSetService
{
    private final DataSetRepository repository;
    public final DataSetColumnRepository columnRepository;

    public DataSetServiceImpl(DataSetRepository repository, DataSetColumnRepository columnRepository)
    {
        this.repository = repository;
        this.columnRepository = columnRepository;
    }

    @Transactional
    public CommonResponse<DataSetEntity> saveOrUpdate(DataSetBody configure)
    {
        DataSetEntity entity = DataSetEntity.builder()
                .name(configure.getName())
                .query(configure.getQuery())
                .user(UserDetailsService.getUser())
                .source(configure.getSource())
                .description(configure.getDescription())
                .syncMode(configure.getSyncMode())
                .syncValue(configure.getSyncValue())
                .build();
        repository.save(entity);
        configure.getColumns().stream().forEach(item -> item.setDataset(DataSetEntity.builder().id(entity.getId()).build()));
        columnRepository.saveAll(configure.getColumns());
        return CommonResponse.success(configure);
    }

    @Override
    public CommonResponse<PageEntity<DataSetEntity>> getAll(PagingAndSortingRepository pagingAndSortingRepository, FilterBody filter)
    {
        Pageable pageable = PageRequestAdapter.of(filter);
        return CommonResponse.success(PageEntity.build(repository.findAllByUser(UserDetailsService.getUser(), pageable)));
    }
}
