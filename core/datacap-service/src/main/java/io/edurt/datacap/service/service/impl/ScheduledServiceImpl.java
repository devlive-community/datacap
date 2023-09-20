package io.edurt.datacap.service.service.impl;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.adapter.PageRequestAdapter;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.entity.ScheduledEntity;
import io.edurt.datacap.service.entity.ScheduledHistoryEntity;
import io.edurt.datacap.service.repository.ScheduledHistoryRepository;
import io.edurt.datacap.service.service.ScheduledService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ScheduledServiceImpl
        implements ScheduledService
{
    private final ScheduledHistoryRepository historyRepository;

    public ScheduledServiceImpl(ScheduledHistoryRepository historyRepository)
    {
        this.historyRepository = historyRepository;
    }

    @Override
    public CommonResponse<PageEntity<ScheduledHistoryEntity>> getHistory(Long id, FilterBody filter)
    {
        ScheduledEntity entity = ScheduledEntity.builder()
                .id(id)
                .build();
        Pageable pageable = PageRequestAdapter.of(filter);
        return CommonResponse.success(PageEntity.build(historyRepository.findAllByScheduledOrderByIdDesc(entity, pageable)));
    }
}
