package io.edurt.datacap.server.service.impl;

import io.edurt.datacap.server.adapter.PageRequestAdapter;
import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.entity.PageEntity;
import io.edurt.datacap.server.entity.SnippetEntity;
import io.edurt.datacap.server.entity.UserEntity;
import io.edurt.datacap.server.repository.SnippetRepository;
import io.edurt.datacap.server.security.UserDetailsService;
import io.edurt.datacap.server.service.SnippetService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SnippetServiceImpl
        implements SnippetService
{
    private final SnippetRepository snippetRepository;

    public SnippetServiceImpl(SnippetRepository snippetRepository)
    {
        this.snippetRepository = snippetRepository;
    }

    @Override
    public Response<SnippetEntity> saveOrUpdate(SnippetEntity configure)
    {
        configure.setUser(UserDetailsService.getUser());
        return Response.success(this.snippetRepository.save(configure));
    }

    @Override
    public Response<PageEntity<SnippetEntity>> getAll(int offset, int limit)
    {
        Pageable pageable = PageRequestAdapter.of(offset, limit);
        UserEntity user = UserDetailsService.getUser();
        return Response.success(PageEntity.build(this.snippetRepository.findAllByUser(user, pageable)));
    }

    @Override
    public Response<Long> delete(Long id)
    {
        this.snippetRepository.deleteById(id);
        return Response.success(id);
    }

    @Override
    public Response<SnippetEntity> getById(Long id)
    {
        return Response.success(this.snippetRepository.findById(id));
    }
}
