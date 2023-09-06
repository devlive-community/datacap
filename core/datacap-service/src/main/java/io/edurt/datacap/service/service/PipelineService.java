package io.edurt.datacap.service.service;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.body.PipelineBody;

import java.util.List;

public interface PipelineService
        extends BaseService
{
    CommonResponse<Object> submit(PipelineBody configure);

    CommonResponse<Boolean> stop(Long id);

    CommonResponse<List<String>> log(Long id);
}
