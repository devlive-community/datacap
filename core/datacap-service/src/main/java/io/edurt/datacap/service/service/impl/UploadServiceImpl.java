package io.edurt.datacap.service.service.impl;

import com.google.inject.Injector;
import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.common.utils.SpiUtils;
import io.edurt.datacap.common.utils.UrlUtils;
import io.edurt.datacap.fs.FsRequest;
import io.edurt.datacap.fs.FsResponse;
import io.edurt.datacap.service.body.UploadBody;
import io.edurt.datacap.service.entity.convert.AvatarEntity;
import io.edurt.datacap.service.enums.UploadMode;
import io.edurt.datacap.service.initializer.InitializerConfigure;
import io.edurt.datacap.service.repository.DashboardRepository;
import io.edurt.datacap.service.security.UserDetailsService;
import io.edurt.datacap.service.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

@Slf4j
@Service
public class UploadServiceImpl
        implements UploadService
{
    private final Injector injector;
    private final HttpServletRequest request;
    private final DashboardRepository dashboard;
    private final InitializerConfigure initializer;

    public UploadServiceImpl(Injector injector, HttpServletRequest request, DashboardRepository dashboard, InitializerConfigure initializer)
    {
        this.injector = injector;
        this.request = request;
        this.dashboard = dashboard;
        this.initializer = initializer;
    }

    @Override
    public CommonResponse upload(UploadBody configure)
    {
        if (configure.getMode().equals(UploadMode.DASHBOARD)) {
            return dashboard.findByCode(configure.getCode())
                    .map(value -> {
                        try {
                            FsRequest fsRequest = getFsRequest(configure.getFile(), configure);
                            SpiUtils.findFs(injector, initializer.getFsConfigure().getType())
                                    .ifPresent(fs -> {
                                        FsResponse response = fs.writer(fsRequest);
                                        AvatarEntity entity = AvatarEntity.builder()
                                                .path(response.getRemote())
                                                .type(initializer.getFsConfigure().getType())
                                                .build();
                                        if (initializer.getFsConfigure().getType().equals("Local")) {
                                            entity.setPath(getAccess(entity));
                                        }
                                        value.setAvatar(entity);
                                        dashboard.save(value);
                                    });
                        }
                        catch (IOException e) {
                            log.error("Failed to upload file [ {} ]", configure.getCode(), e);
                            return CommonResponse.failure(e.getMessage());
                        }
                        finally {
                            try {
                                configure.getFile().getInputStream().close();
                            }
                            catch (IOException e) {
                                log.warn("Failed to close input stream", e);
                            }
                            return CommonResponse.success(value);
                        }
                    })
                    .orElseGet(() -> CommonResponse.failure(String.format("Dashboard [ %s ] not found", configure.getCode())));
        }
        return CommonResponse.failure(String.format("Mode [ %s ] not supported", configure.getMode()));
    }

    /**
     * Generates a FsRequest object based on the given MultipartFile and UploadBody.
     *
     * @param file the MultipartFile object containing the file data
     * @param configure the UploadBody object containing the configuration
     * @return the generated FsRequest object
     * @throws IOException if there is an error reading the file input stream
     */
    private FsRequest getFsRequest(MultipartFile file, UploadBody configure)
            throws IOException
    {
        return FsRequest.builder()
                .access(initializer.getFsConfigure().getAccess())
                .secret(initializer.getFsConfigure().getSecret())
                .endpoint(getHome(configure))
                .bucket(initializer.getFsConfigure().getBucket())
                .stream(file.getInputStream())
                .fileName("avatar.png")
                .build();
    }

    /**
     * Returns the home directory path based on the given configuration.
     *
     * @param configure the upload configuration containing the mode and code
     * @return the home directory path
     */
    private String getHome(UploadBody configure)
    {
        if (!initializer.getFsConfigure().getType().equals("Local")) {
            return initializer.getFsConfigure().getEndpoint();
        }
        return String.join("/", initializer.getDataHome(),
                UserDetailsService.getUser().getUsername(),
                configure.getMode().toString().toLowerCase(),
                configure.getCode());
    }

    /**
     * Returns the access URL for the given AvatarEntity.
     *
     * @param configure the AvatarEntity containing the path
     * @return the access URL
     */
    private String getAccess(AvatarEntity configure)
    {
        String protocol = request.getScheme();
        String host = request.getServerName();
        int port = request.getServerPort();
        return protocol + "://" + host + ":" + port + UrlUtils.fixUrl("/upload" + configure.getPath().replaceFirst(initializer.getDataHome(), ""));
    }
}
