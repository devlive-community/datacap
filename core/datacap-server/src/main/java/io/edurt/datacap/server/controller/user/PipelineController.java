package io.edurt.datacap.server.controller.user;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.server.controller.BaseController;
import io.edurt.datacap.service.body.PipelineBody;
import io.edurt.datacap.service.entity.PipelineEntity;
import io.edurt.datacap.service.repository.PipelineRepository;
import io.edurt.datacap.service.service.PipelineService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/api/v1/pipeline")
public class PipelineController
        extends BaseController<PipelineEntity>
{
    private final PipelineRepository pipelineRepository;
    private final PipelineService pipelineService;

    public PipelineController(PipelineRepository pipelineRepository, PipelineService pipelineService)
    {
        super(pipelineRepository, pipelineService);
        this.pipelineRepository = pipelineRepository;
        this.pipelineService = pipelineService;
    }

    @PostMapping(value = "/create")
    public CommonResponse<Object> create(@RequestBody PipelineBody configure)
    {
        return pipelineService.submit(configure);
    }
}
