package io.edurt.datacap.server.controller;

import io.edurt.datacap.server.common.JwtResponse;
import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.entity.UserEntity;
import io.edurt.datacap.server.service.UserService;
import io.edurt.datacap.server.validation.ValidationGroup;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController
{
    private final UserService userService;

    public AuthController(UserService userService)
    {
        this.userService = userService;
    }

    @PostMapping("/signin")
    public Response<JwtResponse> authenticateUser(@RequestBody @Validated(ValidationGroup.Crud.Auth.class) UserEntity configure)
    {
        return this.userService.authenticate(configure);
    }

    @PostMapping("/signup")
    public Response<?> registerUser(@RequestBody @Validated(ValidationGroup.Crud.Create.class) UserEntity configure)
    {
        return this.userService.saveOrUpdate(configure);
    }
}
