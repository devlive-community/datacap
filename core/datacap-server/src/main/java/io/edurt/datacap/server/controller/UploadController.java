package io.edurt.datacap.server.controller;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.body.UploadBody;
import io.edurt.datacap.service.service.UploadService;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "uploadController")
@RequestMapping(value = "/api/v1/upload")
public class UploadController
{
    private final UploadService service;

    public UploadController(UploadService service)
    {
        this.service = service;
    }

    @PostMapping
    public CommonResponse upload(@ModelAttribute UploadBody configure)
    {
        return service.upload(configure);
    }
}
