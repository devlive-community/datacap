package io.edurt.datacap.server.controller;

import com.google.common.collect.Maps;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.executor.Executor;
import io.edurt.datacap.scheduler.Scheduler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/plugin")
public class PluginController
{
    private final Injector injector;

    public PluginController(Injector injector)
    {
        this.injector = injector;
    }

    @GetMapping
    public CommonResponse<Map<String, Set<String>>> getPlugins()
    {
        Map<String, Set<String>> plugins = Maps.newHashMap();
        Set<String> executors = injector.getInstance(Key.get(new TypeLiteral<Set<Executor>>() {}))
                .stream()
                .map(Executor::name)
                .collect(Collectors.toSet());
        plugins.put("executor", executors);

        Set<String> schedulers = injector.getInstance(Key.get(new TypeLiteral<Set<Scheduler>>() {}))
                .stream()
                .map(Scheduler::name)
                .collect(Collectors.toSet());
        plugins.put("scheduler", schedulers);
        return CommonResponse.success(plugins);
    }
}
