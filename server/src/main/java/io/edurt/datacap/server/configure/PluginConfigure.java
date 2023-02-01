package io.edurt.datacap.server.configure;

import com.google.inject.Guice;
import com.google.inject.Injector;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.spi.PluginLoader;
import io.edurt.datacap.spi.PluginModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ServiceLoader;

@Slf4j
@Configuration
@SuppressFBWarnings(value = {"NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE"},
        justification = "I prefer to suppress these FindBugs warnings")
public class PluginConfigure
{
    @Resource
    private Environment environment;

    @Bean
    public Injector injector()
    {
        PluginLoader loader = new PluginLoader();
        try {
            loadModules(environment.getProperty("plugin-dir"), loader);
        }
        catch (MalformedURLException e) {
            log.error("Loading plugin error", e);
            throw new RuntimeException(e);
        }
        Injector injector = Guice.createInjector(loader);
        return injector;
    }

    private static void loadModules(String path, PluginLoader loader)
            throws MalformedURLException
    {
        File dir = new File(path);
        File[] files = dir.listFiles();
        if (files.length == 0) {
            log.info("Not found plugin");
            return;
        }
        URL[] urls = new URL[files.length];
        boolean loading = false;
        for (int i = 0; i < urls.length; i++) {
            if (files[i].isDirectory()) {
                log.info("========== {} ==========", files[i].getName());
                loadModules(files[i].getAbsolutePath(), loader);
            }
            else {
                // Only load *.jar
                if (files[i].getName().endsWith(".jar")) {
                    loading = true;
                    urls[i] = files[i].toURI().toURL();
                    log.info("Loading file <{}>", files[i].getName());
                }
                else {
                    log.info("Skip file <{}>", files[i].getName());
                }
            }
        }
        if (loading) {
            URLClassLoader classLoader = new URLClassLoader(urls);
            Iterable<PluginModule> modules = ServiceLoader.load(PluginModule.class, classLoader);
            loader.addPlugin(modules);
        }
    }
}
