package io.edurt.datacap.executor;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.executor.connector.Connector;
import io.edurt.datacap.executor.connector.ConnectorFactory;
import io.edurt.datacap.executor.connector.ConnectorType;
import io.edurt.datacap.lib.logger.LoggerExecutor;
import io.edurt.datacap.lib.logger.logback.LogbackExecutor;
import io.edurt.datacap.lib.shell.ShellCommander;
import io.edurt.datacap.lib.shell.ShellConfigure;
import io.edurt.datacap.lib.shell.ShellResponse;
import io.edurt.datacap.lib.shell.process.ProcessBuilderCommander;
import io.edurt.datacap.spi.executor.Executor;
import io.edurt.datacap.spi.executor.Pipeline;
import io.edurt.datacap.spi.executor.PipelineField;
import io.edurt.datacap.spi.executor.PipelineResponse;
import io.edurt.datacap.spi.executor.PipelineState;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

@SuppressFBWarnings(value = {"RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE"})
public class SeatunnelExecutor
        implements Executor
{
    @Override
    public String before(Pipeline configure, Logger logger)
    {
        JsonFactory jsonFactory = new JsonFactory();
        String workFile = String.join(File.separator, configure.getWork(), configure.getPipelineName() + ".configure");
        try (JsonGenerator jsonGenerator = jsonFactory.createGenerator(new File(workFile), JsonEncoding.UTF8)) {
            jsonGenerator.setPrettyPrinter(new DefaultPrettyPrinter());
            jsonGenerator.writeStartObject();
            this.writeChild("env", jsonGenerator, null);
            this.writeChild("source", jsonGenerator, configure.getFrom());
            this.writeChild("sink", jsonGenerator, configure.getTo());
            jsonGenerator.writeEndObject();
            return null;
        }
        catch (Exception exception) {
            logger.error("Build configure file failed", exception);
            return exception.getMessage();
        }
    }

    @Override
    public PipelineResponse start(Pipeline configure)
    {
        SeaTunnelCommander commander = new SeaTunnelCommander(
                configure.getHome() + "/bin",
                String.join(File.separator, configure.getWork(), configure.getPipelineName() + ".configure"),
                configure.getPipelineName());
        LoggerExecutor loggerExecutor = new LogbackExecutor(configure.getWork(), configure.getPipelineName() + ".log");
        String result = before(configure, loggerExecutor.getLogger());
        if (StringUtils.isNotEmpty(result)) {
            return PipelineResponse.builder()
                    .state(PipelineState.FAILURE)
                    .timeout(false)
                    .successful(false)
                    .message(result)
                    .build();
        }

        ShellConfigure shellConfigure = ShellConfigure.builder()
                .directory(configure.getWork())
                .loggerExecutor(loggerExecutor)
                .command(Arrays.asList(commander.toCommand()))
                .timeout(configure.getTimeout())
                .username(configure.getUsername())
                .build();
        ShellCommander shellExecutor = new ProcessBuilderCommander(shellConfigure);
        ShellResponse response = shellExecutor.execute();
        PipelineState state = response.getSuccessful() ? PipelineState.SUCCESS : PipelineState.FAILURE;
        return PipelineResponse.builder()
                .state(state)
                .timeout(response.isTimeout())
                .successful(response.getSuccessful())
                .message(String.join("\n", response.getErrors()))
                .build();
    }

    private void writeChild(String type, JsonGenerator jsonGenerator, PipelineField configure)
            throws Exception
    {
        jsonGenerator.writeFieldName(type);
        if (ObjectUtils.isNotEmpty(configure)) {
            Connector factory = ConnectorFactory.createFormatter(ConnectorType.valueOf(configure.getType()), configure);
            for (Map.Entry<String, Object> entry : factory.formatToMap().entrySet()) {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeObjectFieldStart(entry.getKey());
                if (entry.getValue() instanceof Properties) {
                    for (Map.Entry<Object, Object> property : ((Properties) entry.getValue()).entrySet()) {
                        jsonGenerator.writeStringField(property.getKey().toString(), (String) property.getValue());
                    }
                }
                jsonGenerator.writeEndObject();
            }
            jsonGenerator.writeEndObject();
        }
        else {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeEndObject();
        }
    }
}
