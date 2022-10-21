package io.edurt.datacap.server.authorize;

import io.edurt.datacap.server.body.BaseUserBody;
import io.edurt.datacap.server.common.ServiceState;
import io.edurt.datacap.server.entity.UserEntity;
import io.edurt.datacap.server.security.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service(value = "userAuthorize")
public class UserAuthorize
{
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
}
