package io.edurt.datacap.server.controller.user;

import io.edurt.datacap.server.body.UserPasswordBody;
import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.entity.UserEntity;
import io.edurt.datacap.server.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/user")
public class UserController
{
    private final UserService userService;

    public UserController(UserService userService)
    {
        this.userService = userService;
    }

    @GetMapping(value = {"{id}", ""})
    public Response<UserEntity> info(@PathVariable(required = false) Long id)
    {
        return this.userService.info(id);
    }

    @PutMapping(value = "changePassword")
    public Response<Long> changePassword(@Validated @RequestBody UserPasswordBody configure)
    {
        return this.userService.changePassword(configure);
    }
}
