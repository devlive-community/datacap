package io.edurt.datacap.security;

import io.edurt.datacap.service.initializer.InitializerConfigure;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebAppConfigure
        implements WebMvcConfigurer
{
    private final InitializerConfigure initializer;

    public WebAppConfigure(InitializerConfigure initializer)
    {
        this.initializer = initializer;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:" + initializer.getDataHome() + "/");
    }
}
