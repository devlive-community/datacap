package io.edurt.datacap.server.controller.user;

import io.edurt.datacap.server.body.PipelineBody;
import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.service.PipelineService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/api/v1/pipeline")
public class PipelineController
{
    private final PipelineService pipelineService;

    public PipelineController(PipelineService pipelineService)
    {
        this.pipelineService = pipelineService;
    }

    @PostMapping(value = "/create")
    public Response<Object> create(@RequestBody PipelineBody configure)
    {
        return pipelineService.submit(configure);
    }
}
