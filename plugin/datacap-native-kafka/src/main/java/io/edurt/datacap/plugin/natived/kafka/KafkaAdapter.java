package io.edurt.datacap.plugin.natived.kafka;

import com.google.common.base.Preconditions;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.spi.adapter.NativeAdapter;
import io.edurt.datacap.spi.model.Configure;
import io.edurt.datacap.spi.model.Response;
import io.edurt.datacap.spi.model.Time;
import io.edurt.datacap.sql.SqlBase;
import io.edurt.datacap.sql.SqlBaseToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ConsumerGroupDescription;
import org.apache.kafka.clients.admin.DescribeConsumerGroupsResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
@SuppressFBWarnings(value = {"RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE", "REC_CATCH_EXCEPTION"},
        justification = "I prefer to suppress these FindBugs warnings")
public class KafkaAdapter
        extends NativeAdapter
{
    protected KafkaConnection kafkaConnection;
    private final KafkaParser parser;

    public KafkaAdapter(KafkaConnection kafkaConnection, KafkaParser parser)
    {
        super(kafkaConnection, parser);
        this.kafkaConnection = kafkaConnection;
        this.parser = parser;
    }

    @Override
    public Response handlerExecute(String content)
    {
        Time processorTime = new Time();
        processorTime.setStart(new Date().getTime());
        Response response = this.kafkaConnection.getResponse();
        Configure configure = this.kafkaConnection.getConfigure();
        if (response.getIsConnected()) {
            List<String> headers = new ArrayList<>();
            List<String> types = new ArrayList<>();
            List<Object> columns = new ArrayList<>();
            try {
                SqlBase sqlBase = this.parser.getSqlBase();
                if (sqlBase.isSuccessful()) {
                    AdminClient client = this.kafkaConnection.getClient();
                    if (ObjectUtils.isNotEmpty(this.parser.getSqlBase().getColumns())) {
                        headers.addAll(this.parser.getSqlBase().getColumns());
                    }
                    else {
                        headers.add("*");
                    }
                    types.add("String");
                    this.adapter(client, sqlBase)
                            .forEach(column -> columns.add(handlerFormatter(configure.getFormat(), headers, Collections.singletonList(column))));
                    response.setIsSuccessful(Boolean.TRUE);
                }
                else {
                    response.setIsSuccessful(Boolean.FALSE);
                    response.setMessage(sqlBase.getMessage());
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
                response.setColumns(columns);
            }
        }
        processorTime.setEnd(new Date().getTime());
        response.setProcessor(processorTime);
        return response;
    }

    private List<String> adapter(AdminClient client, SqlBase info)
    {
        List<String> array = new ArrayList<>();
        if (info.getToken() == SqlBaseToken.SHOW) {
            if (info.getChildToken() == SqlBaseToken.TOPICS) {
                this.adapterShowTopics(client, array);
            }
            else if (info.getChildToken() == SqlBaseToken.CONSUMERS) {
                this.adapterShowConsumers(client, info, array);
            }
        }
        return array;
    }

    private void adapterShowTopics(AdminClient client, List<String> array)
    {
        try {
            client.listTopics()
                    .listings()
                    .get()
                    .forEach(v -> array.add(v.name()));
        }
        catch (Exception e) {
            Preconditions.checkArgument(false, ExceptionUtils.getMessage(e));
        }
    }

    private void adapterShowConsumers(AdminClient client, SqlBase info, List<String> array)
    {
        try {
            if (StringUtils.isNotEmpty(info.getTable())) {
                client.listConsumerGroups()
                        .all()
                        .get()
                        .parallelStream()
                        .forEach(v -> {
                            try {
                                DescribeConsumerGroupsResult describeConsumerGroupsResult = client.describeConsumerGroups(Collections.singleton(v.groupId()));
                                ConsumerGroupDescription consumerGroupDescription = describeConsumerGroupsResult.all().get().get(v.groupId());
                                if (consumerGroupDescription.members().stream().anyMatch(member ->
                                        member.assignment().topicPartitions().stream().anyMatch(tp ->
                                                tp.topic().equals(info.getTable().replace("`", ""))))) {
                                    array.add(v.groupId());
                                }
                            }
                            catch (Exception e) {
                                Preconditions.checkArgument(false, ExceptionUtils.getMessage(e));
                            }
                        });
            }
            else {
                client.listConsumerGroups()
                        .all()
                        .get()
                        .forEach(v -> array.add(v.groupId()));
            }
        }
        catch (Exception e) {
            Preconditions.checkArgument(false, ExceptionUtils.getMessage(e));
        }
    }
}
