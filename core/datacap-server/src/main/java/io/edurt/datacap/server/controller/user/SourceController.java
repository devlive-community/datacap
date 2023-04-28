package io.edurt.datacap.server.controller.user;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.server.body.SharedSourceBody;
import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.entity.PageEntity;
import io.edurt.datacap.server.entity.PluginEntity;
import io.edurt.datacap.server.entity.SourceEntity;
import io.edurt.datacap.server.entity.UserEntity;
import io.edurt.datacap.server.security.UserDetailsService;
import io.edurt.datacap.server.service.SourceService;
import io.edurt.datacap.server.validation.ValidationGroup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@SuppressFBWarnings(value = {"RV_RETURN_VALUE_IGNORED_BAD_PRACTICE"})
@RestController()
@RequestMapping(value = "/api/v1/source")
@Slf4j
public class SourceController
{
    private final SourceService sourceService;
    private final Environment environment;

    public SourceController(SourceService sourceService, Environment environment)
    {
        this.sourceService = sourceService;
        this.environment = environment;
    }

    @Deprecated
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public Response<SourceEntity> save(@RequestBody @Validated(ValidationGroup.Crud.Create.class) SourceEntity configure)
    {
        return this.sourceService.saveOrUpdate(configure);
    }

    @Deprecated
    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public Response<SourceEntity> update(@RequestBody @Validated(ValidationGroup.Crud.Update.class) SourceEntity configure)
    {
        return this.sourceService.saveOrUpdate(configure);
    }

    @GetMapping
    public Response<PageEntity<SourceEntity>> getAll(@RequestParam(value = "page", defaultValue = "1") int start, @RequestParam(value = "size", defaultValue = "10") int end)
    {
        return this.sourceService.getAll(start, end);
    }

    @PreAuthorize(value = "@userAuthorize.validateSource(#id)")
    @DeleteMapping(value = "{id}")
    public Response<Long> delete(@PathVariable(value = "id") Long id)
    {
        return this.sourceService.delete(id);
    }

    @PostMapping(value = "test", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Response<Object> testConnection(@RequestBody @Validated(ValidationGroup.Crud.Create.class) SourceEntity configure)
    {
        return this.sourceService.testConnection(configure);
    }

    @GetMapping(value = "{id}")
    public Response<SourceEntity> getInfo(@PathVariable(value = "id") Long id)
    {
        return this.sourceService.getById(id);
    }

    @GetMapping(value = "plugins")
    public Response<Map<String, List<PluginEntity>>> getPlugins()
    {
        return this.sourceService.getPlugins();
    }

    @PreAuthorize(value = "@userAuthorize.validateUser(#configure)")
    @PutMapping(value = "shared")
    public Response<Object> shared(@RequestBody SharedSourceBody configure)
    {
        return this.sourceService.shared(configure);
    }

    @SneakyThrows
    @PostMapping("uploadFile")
    public Response<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestHeader("PluginType") String pluginType)
    {
        UserEntity user = UserDetailsService.getUser();

        String cacheHome = environment.getProperty("datacap.cache.data");
        if (StringUtils.isEmpty(cacheHome)) {
            cacheHome = String.join(File.separator, System.getProperty("user.dir"), "cache");
        }
        String userCacheHome = String.join(File.separator, cacheHome, user.getUsername(), pluginType);

        String originalFilename = file.getOriginalFilename();
        File targetFile = new File(String.join(File.separator, userCacheHome, originalFilename));
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }
        else {
            // If you already have cache files, clean and delete all files
            File[] files = targetFile.getParentFile().listFiles();
            for (File f : files) {
                log.info("Removing cache file {} state {}", f.getName(), f.delete());
            }
        }

        try {
            file.transferTo(targetFile);
        }
        catch (IOException e) {
            log.warn("File upload exception on user {} by type {} ", user.getUsername(), pluginType, e);
        }
        return Response.success(targetFile.getPath());
    }
}
