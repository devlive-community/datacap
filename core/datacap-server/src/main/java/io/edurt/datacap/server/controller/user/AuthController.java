package io.edurt.datacap.server.controller.user;

import io.edurt.datacap.captcha.entity.ResultEntity;
import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.common.response.JwtResponse;
import io.edurt.datacap.server.configure.InitializerConfigure;
import io.edurt.datacap.service.body.AuthBody;
import io.edurt.datacap.service.entity.UserEntity;
import io.edurt.datacap.service.service.UserService;
import io.edurt.datacap.service.validation.ValidationGroup;
import org.apache.commons.lang3.ObjectUtils;
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
    private final InitializerConfigure initializer;

    public AuthController(UserService userService, InitializerConfigure initializer)
    {
        this.userService = userService;
        this.initializer = initializer;
    }

    @PostMapping("/signin")
    public CommonResponse<JwtResponse> authenticateUser(@RequestBody @Validated(ValidationGroup.Crud.Auth.class) AuthBody configure)
    {
        if (initializer.getCaptchaEnable()) {
            if (ObjectUtils.isEmpty(configure.getCaptcha())) {
                return CommonResponse.failure("Captcha must not empty");
            }
            ResultEntity entity = initializer.getCache().getIfPresent(configure.getTimestamp());
            if (entity == null) {
                return CommonResponse.failure("Captcha not exists");
            }
            if (entity.getResult() != configure.getCaptcha()) {
                initializer.getCache().invalidate(configure.getTimestamp());
                return CommonResponse.failure("The calculation result is incorrect");
            }
        }

        UserEntity user = new UserEntity();
        user.setUsername(configure.getUsername());
        user.setPassword(configure.getPassword());
        return this.userService.authenticate(user);
    }

    @PostMapping("/signup")
    public CommonResponse<?> registerUser(@RequestBody @Validated(ValidationGroup.Crud.Create.class) AuthBody configure)
    {
        UserEntity user = new UserEntity();
        user.setUsername(configure.getUsername());
        user.setPassword(configure.getPassword());
        return this.userService.saveOrUpdate(user);
    }
}
