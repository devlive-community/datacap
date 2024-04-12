package io.edurt.datacap.service.service.impl;

import io.edurt.datacap.common.enums.FunctionImportMode;
import io.edurt.datacap.common.enums.ServiceState;
import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.adapter.PageRequestAdapter;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.body.FunctionsImportBody;
import io.edurt.datacap.service.entity.FunctionEntity;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.repository.FunctionsRepository;
import io.edurt.datacap.service.service.FunctionService;
import org.apache.commons.io.IOUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FunctionServiceImpl
        implements FunctionService
{
    private final FunctionsRepository functionsRepository;

    public FunctionServiceImpl(FunctionsRepository functionsRepository)
    {
        this.functionsRepository = functionsRepository;
    }

    @Override
    public CommonResponse<PageEntity<FunctionEntity>> getAllByFilter(FilterBody filter)
    {
        Pageable pageable = PageRequestAdapter.of(filter);
        return CommonResponse.success(PageEntity.build(this.functionsRepository.findAll(pageable)));
    }

    @Override
    public CommonResponse<FunctionEntity> getById(Long id)
    {
        return CommonResponse.success(this.functionsRepository.findById(id));
    }

    @Override
    public CommonResponse<Object> batchImport(FunctionsImportBody configure)
    {
        List<FunctionEntity> functions = new ArrayList<>();
        if (configure.getMode().equals(FunctionImportMode.txt)) {
            Arrays.stream(configure.getContent().split("\n")).forEach(value -> {
                FunctionEntity function = new FunctionEntity();
                function.setContent(value);
                function.setName(value);
                function.setExample(value);
                function.setPlugin(configure.getPlugin());
                function.setDescription(value);
                function.setType(configure.getType());
                functions.add(function);
            });
        }
        else {
            try {
                URI uri = new URI(configure.getContent());
                Arrays.stream(IOUtils.toString(uri).split("\n")).forEach(value -> {
                    FunctionEntity function = new FunctionEntity();
                    function.setContent(value);
                    function.setName(value);
                    function.setExample(value);
                    function.setPlugin(configure.getPlugin());
                    function.setDescription(value);
                    function.setType(configure.getType());
                    functions.add(function);
                });
            }
            catch (URISyntaxException | IOException e) {
                return CommonResponse.failure(ServiceState.INVALID_REMOTE_ADDRESS, ServiceState.INVALID_REMOTE_ADDRESS.getValue() + ":" + e.getMessage());
            }
        }
        return CommonResponse.success(this.functionsRepository.saveAll(functions));
    }

    @Override
    public CommonResponse<PageEntity<FunctionEntity>> getAllByPlugin(String plugin)
    {
        FilterBody filter = new FilterBody();
        filter.setSize(Integer.MAX_VALUE);
        Pageable pageable = PageRequestAdapter.of(filter);
        return CommonResponse.success(PageEntity.build(this.functionsRepository.findAllByPluginContaining(pageable, plugin)));
    }
}
