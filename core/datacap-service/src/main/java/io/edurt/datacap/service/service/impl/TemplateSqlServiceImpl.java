package io.edurt.datacap.service.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.edurt.datacap.common.enums.ServiceState;
import io.edurt.datacap.common.enums.Type;
import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.common.utils.JsonUtils;
import io.edurt.datacap.service.adapter.PageRequestAdapter;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.body.TemplateSqlBody;
import io.edurt.datacap.service.entity.ExecuteEntity;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.entity.SourceEntity;
import io.edurt.datacap.service.entity.TemplateEntity;
import io.edurt.datacap.service.itransient.SqlConfigure;
import io.edurt.datacap.service.repository.SourceRepository;
import io.edurt.datacap.service.repository.TemplateSqlRepository;
import io.edurt.datacap.service.service.ExecuteService;
import io.edurt.datacap.service.service.TemplateSqlService;
import io.edurt.datacap.spi.FormatType;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TemplateSqlServiceImpl
        implements TemplateSqlService
{
    private static String regex = "\\$\\{\\s*\\S*\\}";

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
    public CommonResponse<TemplateEntity> saveOrUpdate(TemplateEntity configure)
    {
        if (ObjectUtils.isEmpty(configure.getId())) {
            List<TemplateEntity> templateEntities = this.templateSqlRepository.findByName(configure.getName());
            boolean skip = false;
            if (!templateEntities.isEmpty()) {
                for (TemplateEntity templateEntity : templateEntities) {
                    for (String plugin : templateEntity.getPlugin().split(",")) {
                        if (configure.getPlugin().contains(plugin)) {
                            skip = true;
                            break;
                        }
                    }
                }
            }
            if (skip) {
                return CommonResponse.failure(ServiceState.PLUGIN_ONLY_ONE_TEMPLATE);
            }
        }
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
                    if (matcherValue.contains(":")) {
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
                                    return CommonResponse.failure(ServiceState.REQUEST_VALID_TYPE, buffer.toString());
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
            json = JsonUtils.toJSON(sqlConfigureList);
        }
        else {
            json = JsonUtils.toJSON(configure.getConfigure());
        }
        configure.setConfigure(json);
        return CommonResponse.success(this.templateSqlRepository.save(configure));
    }

    @Override
    public CommonResponse<PageEntity<TemplateEntity>> getAllByFilter(FilterBody filter)
    {
        Pageable pageable = PageRequestAdapter.of(filter);
        return CommonResponse.success(PageEntity.build(this.templateSqlRepository.findAll(pageable)));
    }

    @Override
    public CommonResponse<TemplateEntity> getById(Long id)
    {
        return CommonResponse.success(this.templateSqlRepository.findById(id));
    }

    @Override
    public CommonResponse<Object> execute(TemplateSqlBody configure)
    {
        Optional<SourceEntity> sourceEntity = this.sourceRepository.findById(configure.getSourceId());
        if (!sourceEntity.isPresent()) {
            return CommonResponse.failure(ServiceState.SOURCE_NOT_FOUND);
        }

        TemplateEntity sqlEntity = this.templateSqlRepository.findByNameAndPluginContaining(configure.getTemplateName(), sourceEntity.get().getType());
        if (ObjectUtils.isEmpty(sqlEntity)) {
            return CommonResponse.failure(ServiceState.SOURCE_NOT_SUPPORTED);
        }
        if (sqlEntity.getPlugin().indexOf(sourceEntity.get().getType()) == -1) {
            return CommonResponse.failure(ServiceState.SOURCE_NOT_SUPPORTED);
        }

        ExecuteEntity executeConfigure = new ExecuteEntity();
        final String[] content = {sqlEntity.getContent()};
        try {
            if (ObjectUtils.isNotEmpty(configure.getConfigure())) {
                List<LinkedHashMap> configures = JsonUtils.objectmapper.readValue(sqlEntity.getConfigure(), List.class);
                configure.getConfigure().entrySet().forEach(value -> {
                    Optional<SqlConfigure> sqlConfigure = configures.stream()
                            .filter(v -> String.valueOf(v.get("column")).equalsIgnoreCase(value.getKey()))
                            .map(v -> {
                                SqlConfigure configure1 = new SqlConfigure();
                                configure1.setColumn(v.get("column").toString());
                                configure1.setType(Type.valueOf(String.valueOf(v.get("type"))));
                                configure1.setExpression(String.valueOf(v.get("expression")));
                                return configure1;
                            })
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
