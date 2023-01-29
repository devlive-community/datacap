package io.edurt.datacap.plugin.natived.redis;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.spi.adapter.NativeAdapter;
import io.edurt.datacap.spi.model.Configure;
import io.edurt.datacap.spi.model.Response;
import io.edurt.datacap.spi.model.Time;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.MethodUtils;
import redis.clients.jedis.Client;
import redis.clients.jedis.Protocol;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@SuppressFBWarnings(value = {"RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE", "REC_CATCH_EXCEPTION"},
        justification = "I prefer to suppress these FindBugs warnings")
public class RedisAdapter
        extends NativeAdapter
{
    protected RedisConnection redisConnection;

    public RedisAdapter(RedisConnection redisConnection)
    {
        super(redisConnection);
        this.redisConnection = redisConnection;
    }

    @Override
    public Response handlerExecute(String content)
    {
        Time processorTime = new Time();
        processorTime.setStart(new Date().getTime());
        Response response = this.redisConnection.getResponse();
        Configure configure = this.redisConnection.getConfigure();
        if (response.getIsConnected()) {
            List<String> headers = new ArrayList<>();
            List<String> types = new ArrayList<>();
            List<Object> columns = new ArrayList<>();
            try {
                String[] commands = content.split(" ");
                Protocol.Command cmd = Protocol.Command.valueOf(commands[0].toUpperCase());
                Method method = MethodUtils.getMatchingMethod(Client.class, "sendCommand", Protocol.Command.class, String[].class);
                method.setAccessible(true);
                Client client = this.redisConnection.getJedis().getClient();
                List<String> cmdParam = new ArrayList<>();
                for (int i = 0; i < commands.length; i++) {
                    if (i == 0) {
                        continue;
                    }
                    cmdParam.add(commands[i]);
                }
                String[] cmdParamArr = cmdParam.toArray(new String[]{});
                method.invoke(client, cmd, cmdParamArr);
                Object body = client.getOne();
                headers.add(commands[1]);
                types.add("String");
                if (body instanceof List) {
                    List<Object> bodySplit = ((List) body);
                    for (Object obj : bodySplit) {
                        columns.add(handlerFormatter(configure.getFormat(), headers, Arrays.asList(new String((byte[]) obj, Charset.forName("UTF-8")))));
                    }
                }
                else {
                    columns.add(handlerFormatter(configure.getFormat(), headers, Arrays.asList(new String((byte[]) body, Charset.forName("UTF-8")))));
                }
                response.setIsSuccessful(Boolean.TRUE);
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
}
