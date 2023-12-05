package io.edurt.datacap.fs;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Set;

@Slf4j
public class LocalFsTest
{
    private Injector injector;
    private FsRequest request;

    @Before
    public void before()
    {
        injector = Guice.createInjector(new FsManager());
        request = FsRequest.builder()
                .access(null)
                .secret(null)
                .endpoint(String.join(File.separator, System.getProperty("user.dir"), "data"))
                .bucket("tmp")
                .localPath(String.join(File.separator, System.getProperty("user.dir"), "src/main/java/io/edurt/datacap/fs/LocalFs.java"))
                .fileName("LocalFs.java")
                .build();
    }

    @Test
    public void test()
    {
        Set<Fs> sets = injector.getInstance(Key.get(new TypeLiteral<Set<Fs>>() {}));
        Assert.assertEquals("Local", sets.stream().findFirst().get().name());
    }

    @Test
    public void writer()
    {
        Optional<Fs> optional = injector.getInstance(Key.get(new TypeLiteral<Set<Fs>>() {})).stream().findFirst();
        if (optional.isPresent()) {
            Assert.assertEquals("Local", optional.get().name());
        }

        FsResponse response = optional.get().writer(request);
        Assert.assertEquals(true, response.isSuccessful());
    }

    @Test
    public void reader()
    {
        Optional<Fs> optional = injector.getInstance(Key.get(new TypeLiteral<Set<Fs>>() {})).stream().findFirst();
        if (optional.isPresent()) {
            Assert.assertEquals("Local", optional.get().name());
        }

        FsResponse response = optional.get().reader(request);
        Assert.assertEquals(true, response.isSuccessful());

        log.info("====== [ {} ] ======", response.getRemote());
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getContext(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                log.info(line);
            }
        }
        catch (IOException e) {
            log.error("Reader error", e);
        }
    }
}