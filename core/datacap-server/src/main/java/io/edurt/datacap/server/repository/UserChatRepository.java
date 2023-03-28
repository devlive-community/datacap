package io.edurt.datacap.server.repository;

import io.edurt.datacap.server.entity.UserChatEntity;
import io.edurt.datacap.server.entity.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface UserChatRepository
        extends PagingAndSortingRepository<UserChatEntity, Long>
{
    List<UserChatEntity> findTop5ByUserOrderByIdDesc(UserEntity user);
}
