package io.edurt.datacap.service.repository;

import io.edurt.datacap.service.entity.UserChatEntity;
import io.edurt.datacap.service.entity.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface UserChatRepository
        extends PagingAndSortingRepository<UserChatEntity, Long>
{
    List<UserChatEntity> findTop5ByUserOrderByIdDesc(UserEntity user);
}
