package io.edurt.datacap.service.initializer;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.captcha.entity.ResultEntity;
import io.edurt.datacap.service.entity.PipelineEntity;
import io.edurt.datacap.service.loader.CaptchaCacheLoader;
import io.edurt.datacap.service.security.UserDetailsService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.io.File;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
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

    @Value(value = "${datacap.pipeline.maxRunning}")
    private Integer maxRunning;

    @Value(value = "${datacap.pipeline.maxQueue}")
    private Integer maxQueue;

    @Getter
    @Value(value = "${datacap.experimental.autoLimit}")
    private Boolean autoLimit;

    @Getter
    @Value(value = "${datacap.experimental.data}")
    private String dataHome;

    @Getter
    @Value(value = "${datacap.experimental.avatarPath}")
    private String avatarPath;

    @Getter
    @Value(value = "${datacap.parser.sql.defaultEngine}")
    private String sqlParserDefaultEngine;

    @Getter
    private LoadingCache<Long, ResultEntity> cache;

    @Getter
    private BlockingQueue<PipelineEntity> taskQueue;

    @Getter
    private Map<String, ExecutorService> taskExecutors;

    @Getter
    private final FsConfigure fsConfigure;

    @Getter
    private final DataSetConfigure dataSetConfigure;

    public InitializerConfigure(FsConfigure fsConfigure, DataSetConfigure dataSetConfigure)
    {
        this.fsConfigure = fsConfigure;
        this.dataSetConfigure = dataSetConfigure;
    }

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

        if (ObjectUtils.isEmpty(maxRunning)) {
            maxRunning = 100;
        }
        log.info("Datacap pipeline max running: {}", this.maxRunning);

        if (ObjectUtils.isEmpty(maxQueue)) {
            maxQueue = 200;
        }
        log.info("Datacap pipeline max queue: {}", this.maxQueue);

        if (ObjectUtils.isEmpty(autoLimit)) {
            autoLimit = Boolean.TRUE;
        }
        log.info("Datacap experimental auto limit: [ {} ]", this.autoLimit);

        if (this.dataHome.toLowerCase().contains("{user.dir}")) {
            this.dataHome = this.dataHome.replace("{user.dir}", System.getProperty("user.dir"));
        }

        this.avatarPath = String.join(File.separator, this.dataHome, this.avatarPath);
        log.info("Datacap experimental avatar path: [ {} ]", this.avatarPath);

        log.info("=========== Datacap experimental fs configure ===========");
        log.info("fs type [ {} ]", fsConfigure.getType());
        log.info("fs access [ {} ]", fsConfigure.getAccess());
        log.info("fs secret [ {} ]", fsConfigure.getSecret());
        log.info("fs endpoint [ {} ]", fsConfigure.getEndpoint());
        log.info("fs bucket [ {} ]", fsConfigure.getBucket());

        log.info("=========== Datacap sql parser configure ===========");
        log.info("sql parser default engine [ {} ]", this.sqlParserDefaultEngine);

        this.taskQueue = new LinkedBlockingQueue<>(this.maxQueue);
        this.taskExecutors = Maps.newConcurrentMap();
    }

    /**
     * Check if the task queue is full.
     *
     * @return true if the task queue is full, false otherwise
     */
    public boolean isQueueFull()
    {
        if (this.taskQueue.size() >= this.maxQueue) {
            return true;
        }
        return false;
    }

    public String getAvatarPath()
    {
        if (StringUtils.isNotEmpty(fsConfigure.getEndpoint())) {
            return fsConfigure.getEndpoint();
        }
        return avatarPath.replace("{username}", UserDetailsService.getUser().getUsername());
    }

    /**
     * Checks if the task is ready for submission.
     *
     * @return true if the number of task executors is equal to or greater than the maximum allowed running tasks, false otherwise
     */
    public boolean isSubmit()
    {
        if (this.taskExecutors.size() >= this.maxRunning) {
            return true;
        }
        return false;
    }
}
