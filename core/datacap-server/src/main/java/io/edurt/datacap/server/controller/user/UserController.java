package io.edurt.datacap.server.controller.user;

import io.edurt.datacap.server.body.FilterBody;
import io.edurt.datacap.server.body.UserNameBody;
import io.edurt.datacap.server.body.UserPasswordBody;
import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.entity.PageEntity;
import io.edurt.datacap.server.entity.UserEntity;
import io.edurt.datacap.server.entity.UserLogEntity;
import io.edurt.datacap.server.service.UserLogService;
import io.edurt.datacap.server.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/user")
public class UserController
{
    private final UserService userService;
    private final UserLogService userLogService;

    public UserController(UserService userService, UserLogService userLogService)
    {
        this.userService = userService;
        this.userLogService = userLogService;
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

    @PutMapping(value = "changeUsername")
    public Response<Long> changeUsername(@Validated @RequestBody UserNameBody configure)
    {
        return this.userService.changeUsername(configure);
    }

    @PostMapping(value = "log")
    public Response<PageEntity<UserLogEntity>> getAllLogByFilter(@RequestBody FilterBody filter)
    {
        return this.userLogService.getAllByFilter(filter);
    }
}
