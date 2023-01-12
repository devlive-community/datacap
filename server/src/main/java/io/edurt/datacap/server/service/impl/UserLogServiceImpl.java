package io.edurt.datacap.server.service.impl;

import io.edurt.datacap.server.adapter.PageRequestAdapter;
import io.edurt.datacap.server.body.FilterBody;
import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.entity.PageEntity;
import io.edurt.datacap.server.entity.UserEntity;
import io.edurt.datacap.server.entity.UserLogEntity;
import io.edurt.datacap.server.repository.UserLogRepository;
import io.edurt.datacap.server.security.UserDetailsService;
import io.edurt.datacap.server.service.UserLogService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserLogServiceImpl
        implements UserLogService
{
    private final UserLogRepository userLogRepository;

    public UserLogServiceImpl(UserLogRepository userLogRepository)
    {
        this.userLogRepository = userLogRepository;
    }

    @Override
    public Response<PageEntity<UserLogEntity>> getAllByFilter(FilterBody filter)
    {
        Pageable pageable = PageRequestAdapter.of(filter);
        UserEntity user = UserDetailsService.getUser();
        return Response.success(PageEntity.build(this.userLogRepository.findAllByUserOrderByCreateTimeDesc(user, pageable)));
    }
}
