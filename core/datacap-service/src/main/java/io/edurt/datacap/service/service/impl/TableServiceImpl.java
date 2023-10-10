package io.edurt.datacap.service.service.impl;

import com.google.common.collect.Lists;
import com.google.inject.Injector;
import io.edurt.datacap.common.enums.ServiceState;
import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.common.sql.SqlBuilder;
import io.edurt.datacap.common.sql.configure.SqlBody;
import io.edurt.datacap.common.sql.configure.SqlColumn;
import io.edurt.datacap.common.sql.configure.SqlType;
import io.edurt.datacap.service.body.TableFilter;
import io.edurt.datacap.service.common.PluginUtils;
import io.edurt.datacap.service.entity.SourceEntity;
import io.edurt.datacap.service.entity.metadata.DatabaseEntity;
import io.edurt.datacap.service.entity.metadata.TableEntity;
import io.edurt.datacap.service.repository.metadata.TableRepository;
import io.edurt.datacap.service.service.TableService;
import io.edurt.datacap.spi.FormatType;
import io.edurt.datacap.spi.Plugin;
import io.edurt.datacap.spi.model.Configure;
import io.edurt.datacap.spi.model.Pagination;
import io.edurt.datacap.spi.model.Response;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class TableServiceImpl
        implements TableService
{
    private final Injector injector;
    private final TableRepository repository;

    public TableServiceImpl(Injector injector, TableRepository repository)
    {
        this.injector = injector;
        this.repository = repository;
    }

    @Override
    public CommonResponse<List<TableEntity>> getAllByDatabase(Long id)
    {
        DatabaseEntity database = DatabaseEntity.builder()
                .id(id)
                .build();
        return CommonResponse.success(this.repository.findAllByDatabase(database));
    }

    @Override
    public CommonResponse<Object> getDataById(Long id, TableFilter configure)
    {
        TableEntity table = this.repository.findById(id)
                .orElse(null);
        if (table == null) {
            return CommonResponse.failure(String.format("Table [ %s ] not found", id));
        }

        SourceEntity entity = table.getDatabase().getSource();
        Optional<Plugin> pluginOptional = PluginUtils.getPluginByNameAndType(this.injector, entity.getType(), entity.getProtocol());
        if (!pluginOptional.isPresent()) {
            return CommonResponse.failure(ServiceState.PLUGIN_NOT_FOUND);
        }

        List<SqlColumn> columns = Lists.newArrayList();
        int totalRows = Integer.parseInt(table.getRows());
        Plugin plugin = pluginOptional.get();

        Configure countConfigure = entity.toConfigure();
        countConfigure.setFormat(FormatType.NONE);
        plugin.connect(countConfigure);
        SqlBody countBody = SqlBody.builder()
                .type(SqlType.SELECT)
                .database(table.getDatabase().getName())
                .table(table.getName())
                .columns(Arrays.asList(SqlColumn.builder()
                        .column("COUNT(1)")
                        .build()))
                .build();
        SqlBuilder countBuilder = new SqlBuilder(countBody);
        String countSql = countBuilder.getSql();
        Response countResponse = plugin.execute(countSql);
        plugin.destroy();
        if (countResponse.getIsSuccessful() && !countResponse.getColumns().isEmpty()) {
            List<Object> applyResponse = (ArrayList) countResponse.getColumns().get(0);
            int applyTotal = Integer.parseInt(String.valueOf(applyResponse.get(0)));
            if (totalRows != applyTotal) {
                totalRows = applyTotal;
                table.setRows(String.valueOf(totalRows));
                this.repository.save(table);
            }
        }

        table.getColumns()
                .stream()
                .sorted(Comparator.comparing(item -> Integer.parseInt(item.getPosition())))
                .forEach(column -> columns.add(SqlColumn.builder()
                        .column(String.format("`%s`", column.getName()))
                        .build()));
        int offset = configure.getPageSize() * (configure.getCurrentPage() - 1);
        SqlBody body = SqlBody.builder()
                .type(SqlType.SELECT)
                .database(table.getDatabase().getName())
                .table(table.getName())
                .columns(columns)
                .limit(configure.getPageSize())
                .offset(offset)
                .build();
        SqlBuilder builder = new SqlBuilder(body);
        String sql = builder.getSql();
        plugin.connect(entity.toConfigure());
        Response response = plugin.execute(sql);
        response.setContent(sql);
        plugin.destroy();
        Pagination pagination = Pagination.newInstance(configure.getPageSize(), configure.getCurrentPage(), totalRows);
        response.setPagination(pagination);
        return CommonResponse.success(response);
    }
}
