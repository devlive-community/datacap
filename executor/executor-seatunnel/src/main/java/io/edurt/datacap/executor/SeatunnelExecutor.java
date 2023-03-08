package io.edurt.datacap.executor;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.google.common.base.Preconditions;
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

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

public class SeatunnelExecutor
        implements Executor
{
    @Override
    public void before(Pipeline configure)
    {
        JsonFactory jsonFactory = new JsonFactory();
        try (JsonGenerator jsonGenerator = jsonFactory.createGenerator(new File(configure.getFile()), JsonEncoding.UTF8)) {
            jsonGenerator.setPrettyPrinter(new DefaultPrettyPrinter());
            jsonGenerator.writeStartObject();
            this.writeChild("source", jsonGenerator, configure.getFrom());
            this.writeChild("sink", jsonGenerator, configure.getTo());
            jsonGenerator.writeEndObject();
        }
        catch (IOException exception) {
            Preconditions.checkArgument(false, exception);
        }
    }

    @Override
    public void start(Pipeline configure)
    {
        before(configure);
        SeaTunnelCommander commander = new SeaTunnelCommander(
                configure.getHome() + "/bin",
                String.join("/", configure.getWork(), configure.getFile()),
                configure.getUsername());
        LoggerExecutor loggerExecutor = new LogbackExecutor(configure.getWork(), configure.getUsername());
        ShellConfigure shellConfigure = new ShellConfigure();
        shellConfigure.setDirectory(configure.getWork());
        shellConfigure.setLoggerExecutor(loggerExecutor);
        shellConfigure.setCommand(Arrays.asList(commander.toCommand()));
        shellConfigure.setTimeout(configure.getTimeout());
        ShellCommander shellExecutor = new ProcessBuilderCommander(shellConfigure);
        ShellResponse response = shellExecutor.execute();
        if (!response.getSuccessful()) {
            Preconditions.checkArgument(false, "Could not execute command");
        }
    }

    private void writeChild(String type, JsonGenerator jsonGenerator, PipelineField configure)
            throws IOException
    {
        jsonGenerator.writeFieldName(type);
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
    }
}
