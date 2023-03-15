package io.edurt.datacap.server.service;

import io.edurt.datacap.server.body.PipelineBody;
import io.edurt.datacap.server.common.Response;

public interface PipelineService
        extends BaseService
{
    Response<Object> submit(PipelineBody configure);
}
