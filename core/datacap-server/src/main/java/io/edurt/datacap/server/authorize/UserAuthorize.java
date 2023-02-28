package io.edurt.datacap.server.authorize;

import io.edurt.datacap.server.body.BaseUserBody;
import io.edurt.datacap.server.common.ServiceState;
import io.edurt.datacap.server.entity.UserEntity;
import io.edurt.datacap.server.repository.SourceRepository;
import io.edurt.datacap.server.security.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service(value = "userAuthorize")
public class UserAuthorize
{
    private final SourceRepository sourceRepository;

    public UserAuthorize(SourceRepository sourceRepository)
    {
        this.sourceRepository = sourceRepository;
    }

    public boolean validateUser(BaseUserBody configure)
            throws UserNotEqualsException
    {
        try {
            UserEntity loginUser = UserDetailsService.getUser();
            if (Objects.equals(loginUser.getId(), configure.getUserId())) {
                return true;
            }
            throw new UserNotEqualsException(ServiceState.USER_UNAUTHORIZED.getValue());
        }
        catch (Exception ex) {
            throw new UserNotEqualsException(ServiceState.USER_UNAUTHORIZED.getValue());
        }
    }

    public boolean validateSource(Long id)
            throws UserNotEqualsException
    {
        try {
            UserEntity loginUser = UserDetailsService.getUser();
            if (Objects.equals(loginUser.getId(), this.sourceRepository.findById(id).get().getUser().getId())) {
                return true;
            }
            throw new UserNotEqualsException(ServiceState.USER_UNAUTHORIZED.getValue());
        }
        catch (Exception ex) {
            throw new UserNotEqualsException(ServiceState.USER_UNAUTHORIZED.getValue());
        }
    }
}
