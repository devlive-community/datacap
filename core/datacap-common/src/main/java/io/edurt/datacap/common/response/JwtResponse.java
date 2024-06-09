package io.edurt.datacap.common.response;

import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@ToString
@NoArgsConstructor
public class JwtResponse
{
    private String token;
    private Long id;
    private String username;
    private List<String> roles;
    private String avatar;

    public JwtResponse(String accessToken, Long id, String username, List<String> roles, String avatar)
    {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        // Use an immutable list to ensure that the roles property cannot be modified externally
        this.roles = Collections.unmodifiableList(roles);
        this.avatar = avatar;
    }
}
