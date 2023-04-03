package io.edurt.datacap.server.service.impl;

import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.service.ScheduledTaskService;
import org.springframework.stereotype.Service;

@Service
public class ScheduledTaskServiceImpl
        implements ScheduledTaskService
{
    @Override
    public Response<Long> delete(Long id)
    {
        throw new UnsupportedOperationException("Not implemented");
    }
}
