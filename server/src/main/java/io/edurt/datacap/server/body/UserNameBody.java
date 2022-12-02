package io.edurt.datacap.server.body;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserNameBody
{
    @NotBlank
    private String password;
    @NotBlank
    private String oldUsername;
    @NotBlank
    private String newUsername;
}
