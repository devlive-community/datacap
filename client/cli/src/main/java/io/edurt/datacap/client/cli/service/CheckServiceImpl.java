package io.edurt.datacap.client.cli.service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.client.cli.Support;
import io.edurt.datacap.client.cli.response.CredentialsResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.shell.Availability;
import org.springframework.stereotype.Service;

@Service
@SuppressFBWarnings(value = {"NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE"},
        justification = "I prefer to suppress these FindBugs warnings")
public class CheckServiceImpl
        implements CheckService
{

    private final CacheManager cacheManager;

    public CheckServiceImpl(CacheManager cacheManager)
    {
        this.cacheManager = cacheManager;
    }

    @Override
    public Availability availabilityCheck()
    {
        Cache.ValueWrapper valueWrapper = this.cacheManager.getCache(Support.cache).get(Support.credentials);
        if (ObjectUtils.isNotEmpty(valueWrapper)) {
            CredentialsResponse credentialsResponse = (CredentialsResponse) valueWrapper.get();
            if (credentialsResponse.isConnect()) {
                return Availability.available();
            }
        }
        return Availability.unavailable("You are not connected");
    }
}
