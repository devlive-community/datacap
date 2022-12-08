package io.edurt.datacap.server.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.edurt.datacap.server.adapter.PageRequestAdapter;
import io.edurt.datacap.server.body.FilterBody;
import io.edurt.datacap.server.body.TemplateSqlBody;
import io.edurt.datacap.server.common.JSON;
import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.common.ServiceState;
import io.edurt.datacap.server.common.Type;
import io.edurt.datacap.server.entity.ExecuteEntity;
import io.edurt.datacap.server.entity.PageEntity;
import io.edurt.datacap.server.entity.SourceEntity;
import io.edurt.datacap.server.entity.TemplateSqlEntity;
import io.edurt.datacap.server.itransient.SqlConfigure;
import io.edurt.datacap.server.repository.SourceRepository;
import io.edurt.datacap.server.repository.TemplateSqlRepository;
import io.edurt.datacap.server.service.ExecuteService;
import io.edurt.datacap.server.service.TemplateSqlService;
import io.edurt.datacap.spi.FormatType;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TemplateSqlServiceImpl
        implements TemplateSqlService
{
    private final String regex = "\\$\\{\\s*\\S*\\}";

    private final TemplateSqlRepository templateSqlRepository;
    private final SourceRepository sourceRepository;
    private final ExecuteService executeService;

    public TemplateSqlServiceImpl(TemplateSqlRepository templateSqlRepository, SourceRepository sourceRepository, ExecuteService executeService)
    {
        this.templateSqlRepository = templateSqlRepository;
        this.sourceRepository = sourceRepository;
        this.executeService = executeService;
    }

    @Override
    public Response<TemplateSqlEntity> saveOrUpdate(TemplateSqlEntity configure)
    {
        // Building configure
        String json;
        if (StringUtils.isEmpty(configure.getConfigure()) || ObjectUtils.isEmpty(configure.getConfigure())) {
            List<SqlConfigure> sqlConfigureList = new ArrayList<>();
            String[] fieldAndTypes = configure.getContent().split("\n");
            for (int i = 0; i < fieldAndTypes.length; i++) {
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(fieldAndTypes[i]);
                if (matcher.find()) {
                    String matcherValue = matcher.group(0).replace("${", "").replace("}", "");
                    if (matcherValue.indexOf(":") > -1) {
                        String[] fieldAndType = matcherValue.split(":");
                        if (fieldAndType.length == 2) {
                            Optional<SqlConfigure> sqlConfigure = sqlConfigureList.stream().filter(v -> v.getColumn().equalsIgnoreCase(fieldAndType[0])).findFirst();
                            if (!sqlConfigure.isPresent()) {
                                if (ObjectUtils.isEmpty(EnumUtils.getEnum(Type.class, fieldAndType[1]))) {
                                    StringBuffer buffer = new StringBuffer();
                                    buffer.append(ServiceState.REQUEST_VALID_TYPE.getValue());
                                    buffer.append("<br/>");
                                    buffer.append(String.format("Field : %s", fieldAndType[0]));
                                    buffer.append("<br/>");
                                    buffer.append(String.format("Type : %s", fieldAndType[1]));
                                    buffer.append("<br/>");
                                    buffer.append(String.format("Line : %s", (i + 1)));
                                    return Response.failure(ServiceState.REQUEST_VALID_TYPE, buffer.toString());
                                }
                                SqlConfigure s = new SqlConfigure();
                                s.setColumn(fieldAndType[0]);
                                s.setType(Type.valueOf(fieldAndType[1]));
                                s.setExpression(matcher.group(0));
                                sqlConfigureList.add(s);
                            }
                        }
                    }
                }
            }
            json = JSON.toJSON(sqlConfigureList);
        }
        else {
            json = JSON.toJSON(configure.getConfigure());
        }
        configure.setConfigure(json);
        return Response.success(this.templateSqlRepository.save(configure));
    }

    @Override
    public Response<PageEntity<TemplateSqlEntity>> getAllByFilter(FilterBody filter)
    {
        Pageable pageable = PageRequestAdapter.of(filter);
        return Response.success(PageEntity.build(this.templateSqlRepository.findAll(pageable)));
    }

    @Override
    public Response<TemplateSqlEntity> getById(Long id)
    {
        return Response.success(this.templateSqlRepository.findById(id));
    }

    @Override
    public Response<Object> execute(TemplateSqlBody configure)
    {
        Optional<SourceEntity> sourceEntity = this.sourceRepository.findById(configure.getSourceId());
        if (!sourceEntity.isPresent()) {
            return Response.failure(ServiceState.SOURCE_NOT_FOUND);
        }

        TemplateSqlEntity sqlEntity = this.templateSqlRepository.findByNameAndPluginContaining(configure.getTemplateName(), sourceEntity.get().getType());
        if (ObjectUtils.isEmpty(sqlEntity)) {
            return Response.failure(ServiceState.SOURCE_NOT_SUPPORTED);
        }
        if (sqlEntity.getPlugin().indexOf(sourceEntity.get().getType()) == -1) {
            return Response.failure(ServiceState.SOURCE_NOT_SUPPORTED);
        }

        ExecuteEntity executeConfigure = new ExecuteEntity();
        final String[] content = {sqlEntity.getContent()};
        try {
            if (ObjectUtils.isNotEmpty(configure.getConfigure())) {
                List<SqlConfigure> configures = JSON.objectmapper.readValue(sqlEntity.getConfigure(), List.class);
                configure.getConfigure().entrySet().forEach(value -> {
                    Optional<SqlConfigure> sqlConfigure = configures.stream()
                            .filter(v -> v.getColumn().equalsIgnoreCase(value.getKey()))
                            .findFirst();
                    if (sqlConfigure.isPresent()) {
                        content[0] = content[0].replace(sqlConfigure.get().getExpression(), String.valueOf(value.getValue()));
                    }
                });
            }
            executeConfigure.setContent(content[0]);
            executeConfigure.setFormat(FormatType.JSON);
            executeConfigure.setName(String.valueOf(configure.getSourceId()));
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return this.executeService.execute(executeConfigure);
    }
}
