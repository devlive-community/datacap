package io.edurt.datacap.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse
{
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private List<String> roles;
    private String avatar;

    public JwtResponse(String accessToken, Long id, String username, List<String> roles, String avatar)
    {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.roles = roles;
        this.avatar = avatar;
    }
}
