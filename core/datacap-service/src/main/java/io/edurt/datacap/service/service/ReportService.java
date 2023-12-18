package io.edurt.datacap.service.service;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.entity.ReportEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ReportService
        extends BaseService<ReportEntity>
{
    CommonResponse<PageEntity<ReportEntity>> getAll(FilterBody filter);

    CommonResponse<ReportEntity> saveOrUpdate(PagingAndSortingRepository repository, ReportEntity configure);
}
