package io.edurt.datacap.plugin.natived.alioss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.ObjectListing;
import com.google.common.base.Preconditions;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.spi.adapter.NativeAdapter;
import io.edurt.datacap.spi.model.Configure;
import io.edurt.datacap.spi.model.Response;
import io.edurt.datacap.spi.model.Time;
import io.edurt.datacap.sql.SqlBase;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
@SuppressFBWarnings(value = {"RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE", "REC_CATCH_EXCEPTION"},
        justification = "I prefer to suppress these FindBugs warnings")
public class AliossAdapter
        extends NativeAdapter
{
    protected AliossConnection aliossConnection;
    private final AliossParser parser;

    public AliossAdapter(AliossConnection aliossConnection, AliossParser parser)
    {
        super(aliossConnection, parser);
        this.aliossConnection = aliossConnection;
        this.parser = parser;
    }

    @Override
    public Response handlerExecute(String content)
    {
        Time processorTime = new Time();
        processorTime.setStart(new Date().getTime());
        Response response = this.aliossConnection.getResponse();
        Configure configure = this.aliossConnection.getConfigure();
        if (response.getIsConnected()) {
            List<String> headers = new ArrayList<>();
            List<String> types = new ArrayList<>();
            List<Object> columns = new ArrayList<>();
            try {
                SqlBase sqlBase = this.parser.getSqlBase();
                if (sqlBase.isSuccessful()) {
                    OSS client = this.aliossConnection.getOssClient();
                    if (ObjectUtils.isNotEmpty(this.parser.getSqlBase().getColumns())) {
                        headers.addAll(this.parser.getSqlBase().getColumns());
                    }
                    else {
                        headers.add("*");
                    }
                    types.add("String");
                    ObjectListing objectListing = client.listObjects(configure.getDatabase().get(), sqlBase.getTable());
                    objectListing.getObjectSummaries()
                            .forEach(column -> columns.add(Collections.singletonList(column.getKey())));
                    response.setIsSuccessful(Boolean.TRUE);
                }
                else {
                    Preconditions.checkArgument(true, sqlBase.getMessage());
                }
            }
            catch (Exception ex) {
                log.error("Execute content failed content {} exception ", content, ex);
                response.setIsSuccessful(Boolean.FALSE);
                response.setMessage(ex.getMessage());
            }
            finally {
                response.setHeaders(headers);
                response.setTypes(types);
                response.setColumns(handlerFormatter(configure.getInjector(), configure.getFormat(), headers, columns));
            }
        }
        processorTime.setEnd(new Date().getTime());
        response.setProcessor(processorTime);
        return response;
    }
}
