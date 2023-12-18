package io.edurt.datacap.server.controller;

import io.edurt.datacap.service.entity.ReportEntity;
import io.edurt.datacap.service.repository.ReportRepository;
import io.edurt.datacap.service.service.ReportService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/api/v1/report")
public class ReportController
        extends BaseController<ReportEntity>
{
    private final ReportRepository repository;
    private final ReportService service;

    protected ReportController(ReportRepository repository, ReportService service)
    {
        super(repository, service);
        this.repository = repository;
        this.service = service;
    }
}
