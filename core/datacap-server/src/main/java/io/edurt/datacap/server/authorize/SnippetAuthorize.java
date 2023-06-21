package io.edurt.datacap.server.authorize;

import io.edurt.datacap.common.enums.ServiceState;
import io.edurt.datacap.service.entity.UserEntity;
import io.edurt.datacap.service.repository.SnippetRepository;
import io.edurt.datacap.service.security.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service(value = "snippetAuthorize")
public class SnippetAuthorize
{
    private final SnippetRepository snippetRepository;

    public SnippetAuthorize(SnippetRepository snippetRepository)
    {
        this.snippetRepository = snippetRepository;
    }

    public boolean validateById(Long id)
            throws UserNotEqualsException
    {
        try {
            UserEntity loginUser = UserDetailsService.getUser();
            if (Objects.equals(loginUser.getId(), this.snippetRepository.findById(id).get().getUser().getId())) {
                return true;
            }
            throw new UserNotEqualsException(ServiceState.USER_UNAUTHORIZED.getValue());
        }
        catch (Exception ex) {
            throw new UserNotEqualsException(ServiceState.USER_UNAUTHORIZED.getValue());
        }
    }
}
