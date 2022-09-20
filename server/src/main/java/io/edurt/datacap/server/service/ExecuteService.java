package io.edurt.datacap.server.service;

import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.entity.ExecuteEntity;

public interface ExecuteService
{
    Response<Object> execute(ExecuteEntity configure);
}
