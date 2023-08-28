package io.edurt.datacap.server.configure;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class InitializerConfigure
{
    @Getter
    @Value(value = "${datacap.registration.enable}")
    private Boolean registrationEnable;

    @Getter
    @Value(value = "${datacap.captcha.enable}")
    private Boolean captchaEnable;

    /**
     * Initializes the function.
     */
    @PostConstruct
    public void init()
    {
        if (ObjectUtils.isEmpty(registrationEnable)) {
            this.registrationEnable = Boolean.TRUE;
        }
        log.info("Datacap registration enable: {}", this.registrationEnable);
        if (ObjectUtils.isEmpty(captchaEnable)) {
            captchaEnable = Boolean.TRUE;
        }
        log.info("Datacap captcha enable: {}", this.captchaEnable);
    }
}
