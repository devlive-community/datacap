package io.edurt.datacap.server.controller;

import io.edurt.datacap.service.entity.SnippetEntity;
import io.edurt.datacap.service.repository.SnippetRepository;
import io.edurt.datacap.service.service.SnippetService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/api/v1/snippet")
public class SnippetController
        extends BaseController<SnippetEntity>
{
    private final SnippetRepository repository;
    private final SnippetService service;

    public SnippetController(SnippetRepository repository, SnippetService service)
    {
        super(repository, service);
        this.repository = repository;
        this.service = service;
    }
}
