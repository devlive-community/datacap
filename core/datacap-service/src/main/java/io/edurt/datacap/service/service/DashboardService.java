package io.edurt.datacap.service.service;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.entity.DashboardEntity;
import io.edurt.datacap.service.entity.PageEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DashboardService
        extends BaseService<DashboardEntity>
{
    CommonResponse<PageEntity<DashboardEntity>> getAll(FilterBody filter);

    CommonResponse<DashboardEntity> saveOrUpdate(PagingAndSortingRepository repository, DashboardEntity configure);
}
