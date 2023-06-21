package io.edurt.datacap.service.service;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.body.ExecuteDslBody;
import io.edurt.datacap.service.entity.ExecuteEntity;

public interface ExecuteService
{
    CommonResponse<Object> execute(ExecuteEntity configure);

    CommonResponse<Object> execute(ExecuteDslBody configure);
}
