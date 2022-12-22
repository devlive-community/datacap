package io.edurt.datacap.client.cli.service;

import com.google.gson.internal.LinkedTreeMap;
import io.edurt.datacap.client.cli.http.HttpCommon;
import io.edurt.datacap.client.cli.http.HttpConfigure;
import io.edurt.datacap.client.cli.model.DataSource;
import io.edurt.datacap.client.cli.response.ServerResponse;
import io.edurt.datacap.client.cli.utils.TableUtils;
import org.springframework.shell.table.BeanListTableModel;
import org.springframework.shell.table.Table;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class DataSourceServiceImpl
        implements DataSourceService
{
    private static final String url = "/api/v1/source";
    private final CacheService cacheService;

    public DataSourceServiceImpl(CacheService cacheService)
    {
        this.cacheService = cacheService;
    }

    @Override
    public Table showList()
    {
        HttpConfigure configure = this.cacheService.getConfigure();
        configure.setUrl(url);
        HttpCommon httpCommon = new HttpCommon(configure);
        ServerResponse serverResponse = httpCommon.withTokenForGet();
        LinkedTreeMap<String, Object> map = (LinkedTreeMap) serverResponse.getData();
        List<LinkedTreeMap<String, Object>> list = (List<LinkedTreeMap<String, Object>>) map.get("content");
        final List<DataSource> dataSources = new ArrayList<>();
        list.forEach(value -> {
            DataSource dataSource = new DataSource();
            // Double
            dataSource.setId(Integer.valueOf(String.valueOf(value.get("id")).split("\\.")[0]));
            dataSource.setName(String.valueOf(value.get("name")));
            dataSource.setType(String.valueOf(value.get("type")));
            dataSource.setDescription(String.valueOf(value.get("description")));
            dataSources.add(dataSource);
        });
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("id", "Id");
        headers.put("name", "Name");
        headers.put("type", "Type");
        headers.put("description", "Description");
        TableModel model = new BeanListTableModel<>(dataSources, headers);
        TableBuilder tableBuilder = new TableBuilder(model);
        TableUtils.applyStyle(tableBuilder);
        return tableBuilder.build();
    }
}
