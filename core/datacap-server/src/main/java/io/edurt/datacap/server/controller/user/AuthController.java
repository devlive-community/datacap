package io.edurt.datacap.server.controller.user;

import io.edurt.datacap.captcha.entity.ResultEntity;
import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.body.AuthBody;
import io.edurt.datacap.service.entity.UserEntity;
import io.edurt.datacap.service.initializer.InitializerConfigure;
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

    /**
     * Validates the captcha.
     *
     * @param configure the authentication body containing captcha information
     * @return the common response object representing the result of the validation
     */
    private CommonResponse<Object> validateCaptcha(AuthBody configure)
    {
        if (initializer.getCaptchaEnable()) {
            if (ObjectUtils.isEmpty(configure.getCaptcha())) {
                return CommonResponse.failure("Captcha must not be empty");
            }
            ResultEntity entity = initializer.getCache().getIfPresent(configure.getTimestamp());
            if (entity == null) {
                return CommonResponse.failure("Captcha does not exists");
            }
            if (!entity.getResult().equals(configure.getCaptcha())) {
                initializer.getCache().invalidate(configure.getTimestamp());
                return CommonResponse.failure("The calculation result is incorrect");
            }
        }
        return null;
    }

    @PostMapping("/signin")
    public CommonResponse<Object> authenticateUser(@RequestBody @Validated(ValidationGroup.Crud.Auth.class) AuthBody configure)
    {
        CommonResponse<Object> captchaResponse = validateCaptcha(configure);
        if (captchaResponse != null) {
            return captchaResponse;
        }

        UserEntity user = new UserEntity();
        user.setUsername(configure.getUsername());
        user.setPassword(configure.getPassword());
        return this.userService.authenticate(user);
    }

    @PostMapping("/signup")
    public CommonResponse<?> registerUser(@RequestBody @Validated(ValidationGroup.Crud.Create.class) AuthBody configure)
    {
        if (!initializer.getRegistrationEnable()) {
            return CommonResponse.failure("Registration is disabled");
        }

        CommonResponse<?> captchaResponse = validateCaptcha(configure);
        if (captchaResponse != null) {
            return captchaResponse;
        }

        UserEntity user = new UserEntity();
        user.setUsername(configure.getUsername());
        user.setPassword(configure.getPassword());
        return this.userService.saveOrUpdate(user);
    }
}
