package io.edurt.datacap.service.service;

import io.edurt.datacap.common.enums.ServiceState;
import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.common.utils.CodeUtils;
import io.edurt.datacap.common.utils.NullAwareBeanUtils;
import io.edurt.datacap.common.utils.ReflectionUtils;
import io.edurt.datacap.service.SelfException;
import io.edurt.datacap.service.adapter.PageRequestAdapter;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.entity.BaseEntity;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.entity.UserEntity;
import io.edurt.datacap.service.repository.BaseRepository;
import io.edurt.datacap.service.security.UserDetailsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;

public interface BaseService<T extends BaseEntity>
{
    default CommonResponse<PageEntity<T>> getAll(BaseRepository repository, FilterBody filter)
    {
        Pageable pageable = PageRequestAdapter.of(filter);
        return CommonResponse.success(PageEntity.build(repository.findAll(pageable)));
    }

    default CommonResponse<T> getById(BaseRepository repository, Long id)
    {
        return CommonResponse.success(repository.findById(id));
    }

    default CommonResponse<T> saveOrUpdate(BaseRepository repository, T configure)
    {
        if (configure.getId() != null) {
            repository.findById(configure.getId())
                    .ifPresent(value -> NullAwareBeanUtils.copyNullProperties(value, configure));
        }
        if (ReflectionUtils.hasField(configure, "user")) {
            ReflectionUtils.setFieldValue(configure, "user", UserDetailsService.getUser());
        }
        if (StringUtils.isEmpty(configure.getCode())) {
            configure.setCode(CodeUtils.generateCode(false));
        }
        return CommonResponse.success(repository.save(configure));
    }

    default CommonResponse<Long> deleteById(BaseRepository repository, Long id)
    {
        repository.deleteById(id);
        return CommonResponse.success(id);
    }

    default CommonResponse<T> getByCode(BaseRepository repository, String code)
    {
        return (CommonResponse<T>) repository.findByCode(code)
                .map(value -> validatorUser(value))
                .orElseGet(() -> CommonResponse.failure(String.format("Resource [ %s ] not found", code)));
    }

    default CommonResponse<T> validatorUser(Object value)
    {
        if (ReflectionUtils.hasField(value, "user")) {
            UserEntity originalUser = (UserEntity) ReflectionUtils.getFieldValue(value, "user");
            UserEntity loginUser = UserDetailsService.getUser();
            if (!originalUser.getId().equals(loginUser.getId())) {
                return CommonResponse.failure(ServiceState.USER_UNAUTHORIZED);
            }
        }
        return CommonResponse.success(value);
    }

    default void isSelf(Object value)
    {
        if (ReflectionUtils.hasField(value, "user")) {
            UserEntity originalUser = (UserEntity) ReflectionUtils.getFieldValue(value, "user");
            UserEntity loginUser = UserDetailsService.getUser();
            if (!originalUser.getId().equals(loginUser.getId())) {
                throw new SelfException(String.format("Resource [ %s ] not found", value.getClass().getName()));
            }
        }
    }
}
