package io.edurt.datacap.server.body.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserRole
{
    private Long userId;
    private List<Long> roles;
}
