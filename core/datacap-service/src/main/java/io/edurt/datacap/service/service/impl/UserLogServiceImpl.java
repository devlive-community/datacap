package io.edurt.datacap.service.service.impl;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.adapter.PageRequestAdapter;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.entity.UserEntity;
import io.edurt.datacap.service.entity.UserLogEntity;
import io.edurt.datacap.service.repository.UserLogRepository;
import io.edurt.datacap.service.security.UserDetailsService;
import io.edurt.datacap.service.service.UserLogService;
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
    public CommonResponse<PageEntity<UserLogEntity>> getAllByFilter(FilterBody filter)
    {
        Pageable pageable = PageRequestAdapter.of(filter);
        UserEntity user = UserDetailsService.getUser();
        return CommonResponse.success(PageEntity.build(this.userLogRepository.findAllByUserOrderByCreateTimeDesc(user, pageable)));
    }
}
