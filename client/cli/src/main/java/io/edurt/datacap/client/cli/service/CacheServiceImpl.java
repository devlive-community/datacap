package io.edurt.datacap.client.cli.service;

import com.google.gson.internal.LinkedTreeMap;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.client.cli.Support;
import io.edurt.datacap.client.cli.http.HttpConfigure;
import io.edurt.datacap.client.cli.response.CredentialsResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
@SuppressFBWarnings(value = {"NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE"},
        justification = "I prefer to suppress these FindBugs warnings")
public class CacheServiceImpl
        implements CacheService
{
    private final CacheManager cacheManager;

    public CacheServiceImpl(CacheManager cacheManager)
    {
        this.cacheManager = cacheManager;
    }

    @Override
    public HttpConfigure getConfigure()
    {
        HttpConfigure configure = null;
        Cache.ValueWrapper valueWrapper = this.cacheManager.getCache(Support.cache).get(Support.credentials);
        if (ObjectUtils.isNotEmpty(valueWrapper)) {
            CredentialsResponse credentialsResponse = (CredentialsResponse) valueWrapper.get();
            if (credentialsResponse.isConnect()) {
                configure = (HttpConfigure) this.cacheManager.getCache(Support.cache).get(Support.configure).get();
                LinkedTreeMap<String, String> map = (LinkedTreeMap) credentialsResponse.getData();
                configure.setToken(map.get("token"));
            }
        }
        return configure;
    }
}
