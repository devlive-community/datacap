package io.edurt.datacap.server.controller.user;

import io.edurt.datacap.server.body.FilterBody;
import io.edurt.datacap.server.body.PipelineBody;
import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.entity.PageEntity;
import io.edurt.datacap.server.entity.PipelineEntity;
import io.edurt.datacap.server.repository.PipelineRepository;
import io.edurt.datacap.server.service.PipelineService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/api/v1/pipeline")
public class PipelineController
{
    private final PipelineRepository pipelineRepository;
    private final PipelineService pipelineService;

    public PipelineController(PipelineRepository pipelineRepository, PipelineService pipelineService)
    {
        this.pipelineRepository = pipelineRepository;
        this.pipelineService = pipelineService;
    }

    @PostMapping(value = "/create")
    public Response<Object> create(@RequestBody PipelineBody configure)
    {
        return pipelineService.submit(configure);
    }

    @PostMapping(value = "list")
    public Response<PageEntity<PipelineEntity>> getAllByFilter(@RequestBody FilterBody filter)
    {
        return this.pipelineService.getAll(this.pipelineRepository, filter);
    }
}
