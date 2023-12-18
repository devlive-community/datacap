package io.edurt.datacap.service.service;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.entity.ReportEntity;

public interface ReportService
        extends BaseService<ReportEntity>
{
    CommonResponse<PageEntity<ReportEntity>> getAll(FilterBody filter);
}
