package io.edurt.datacap.common.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@ToString
@NoArgsConstructor
public class JwtResponse
{
    @Getter
    private String type = "Bearer";
    @Getter
    private String token;
    @Getter
    private Long id;
    @Getter
    private String username;
    @Getter
    private List<String> roles;
    @Getter
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
