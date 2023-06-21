package io.edurt.datacap.service.service.impl;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.adapter.PageRequestAdapter;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.entity.SnippetEntity;
import io.edurt.datacap.service.entity.UserEntity;
import io.edurt.datacap.service.repository.SnippetRepository;
import io.edurt.datacap.service.security.UserDetailsService;
import io.edurt.datacap.service.service.SnippetService;
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
    public CommonResponse<SnippetEntity> saveOrUpdate(SnippetEntity configure)
    {
        configure.setUser(UserDetailsService.getUser());
        return CommonResponse.success(this.snippetRepository.save(configure));
    }

    @Override
    public CommonResponse<PageEntity<SnippetEntity>> getAll(int offset, int limit)
    {
        Pageable pageable = PageRequestAdapter.of(offset, limit);
        UserEntity user = UserDetailsService.getUser();
        return CommonResponse.success(PageEntity.build(this.snippetRepository.findAllByUser(user, pageable)));
    }

    @Override
    public CommonResponse<Long> delete(Long id)
    {
        this.snippetRepository.deleteById(id);
        return CommonResponse.success(id);
    }

    @Override
    public CommonResponse<SnippetEntity> getById(Long id)
    {
        return CommonResponse.success(this.snippetRepository.findById(id));
    }
}
