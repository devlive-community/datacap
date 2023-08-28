package io.edurt.datacap.server.configure;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import io.edurt.datacap.captcha.entity.ResultEntity;
import io.edurt.datacap.server.loader.CaptchaCacheLoader;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.util.concurrent.TimeUnit;

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

    @Getter
    @Value(value = "${datacap.cache.maximum}")
    private Long cacheMaximum;

    @Getter
    @Value(value = "${datacap.cache.expiration}")
    private Long cacheExpiration;

    @Getter
    private LoadingCache<Long, ResultEntity> cache;

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

        if (ObjectUtils.isEmpty(cacheMaximum)) {
            cacheMaximum = 10000L;
        }
        log.info("Datacap cache maximum: {}", this.cacheMaximum);
        if (ObjectUtils.isEmpty(cacheExpiration)) {
            cacheExpiration = 2L;
        }
        log.info("Datacap cache expiration: {}", this.cacheExpiration);
        if (ObjectUtils.isEmpty(cache) && captchaEnable) {
            cache = CacheBuilder.newBuilder()
                    .expireAfterWrite(cacheExpiration, TimeUnit.MINUTES)
                    .maximumSize(cacheMaximum)
                    .build(new CaptchaCacheLoader());
        }
    }
}
