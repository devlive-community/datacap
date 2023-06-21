package io.edurt.datacap.service.service.impl;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.service.ScheduledTaskService;
import org.springframework.stereotype.Service;

@Service
public class ScheduledTaskServiceImpl
        implements ScheduledTaskService
{
    @Override
    public CommonResponse<Long> delete(Long id)
    {
        throw new UnsupportedOperationException("Not implemented");
    }
}
