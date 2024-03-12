package io.edurt.datacap.executor.seatunnel;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.executor.Executor;
import io.edurt.datacap.executor.common.RunProtocol;
import io.edurt.datacap.executor.common.RunState;
import io.edurt.datacap.executor.configure.ExecutorConfigure;
import io.edurt.datacap.executor.configure.ExecutorRequest;
import io.edurt.datacap.executor.configure.ExecutorResponse;
import io.edurt.datacap.executor.seatunnel.connector.Connector;
import io.edurt.datacap.executor.seatunnel.connector.ConnectorFactory;
import io.edurt.datacap.executor.seatunnel.connector.ConnectorType;
import io.edurt.datacap.lib.logger.LoggerExecutor;
import io.edurt.datacap.lib.logger.logback.LogbackExecutor;
import io.edurt.datacap.lib.shell.ShellCommander;
import io.edurt.datacap.lib.shell.ShellConfigure;
import io.edurt.datacap.lib.shell.ShellResponse;
import io.edurt.datacap.lib.shell.process.ProcessBuilderCommander;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

@SuppressFBWarnings(value = {"RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE"})
@Slf4j
public class SeatunnelExecutor
        implements Executor
{
    @Override
    public String name()
    {
        return "Seatunnel";
    }

    @Override
    public ExecutorResponse start(ExecutorRequest request)
    {
        try {
            SeaTunnelCommander commander = new SeaTunnelCommander(
                    request.getExecutorHome() + "/bin",
                    request.getStartScript(),
                    request.getRunWay().name().toLowerCase(),
                    request.getRunMode().name().toLowerCase(),
                    String.join(File.separator, request.getWorkHome(), request.getTaskName() + ".configure"),
                    request.getTaskName(),
                    request.getRunEngine());

            LoggerExecutor loggerExecutor = new LogbackExecutor(request.getWorkHome(), request.getTaskName() + ".log");
            String result = before(request, loggerExecutor.getLogger());
            if (StringUtils.isNotEmpty(result)) {
                return new ExecutorResponse(false, false, RunState.FAILURE, result);
            }

            ShellConfigure shellConfigure = ShellConfigure.builder()
                    .directory(request.getWorkHome())
                    .loggerExecutor(loggerExecutor)
                    .command(Collections.singletonList(commander.toCommand()))
                    .timeout(request.getTimeout())
                    .username(request.getUserName())
                    .build();
            ShellCommander shellExecutor = new ProcessBuilderCommander(shellConfigure);
            ShellResponse response = shellExecutor.execute();
            log.info("Task [ {} ] executed code [ {} ]", request.getTaskName(), response.getCode());
            RunState state = response.getSuccessful() ? RunState.SUCCESS : RunState.FAILURE;
            log.info("Task [ {} ] executed state [ {} ]", request.getTaskName(), state);

            String message = null;
            if (response.getErrors() != null && !response.getSuccessful()) {
                message = String.join("\n", response.getErrors());
            }
            return new ExecutorResponse(response.isTimeout(), response.getSuccessful(), state, message);
        }
        catch (Exception exception) {
            return new ExecutorResponse(false, false, RunState.FAILURE, exception.getMessage());
        }
    }

    @Override
    public ExecutorResponse stop(ExecutorRequest request)
    {
        return null;
    }

    /**
     * Writes a child element to the JSON output using the specified type, JSON generator, and executor configure.
     *
     * @param type the type of the child element
     * @param jsonGenerator the JSON generator to write the child element to
     * @param configure the executor configure to use for formatting the child element
     * @throws Exception if there is an error writing the child element
     */
    private void writeChild(String type, JsonGenerator jsonGenerator, ExecutorConfigure configure)
            throws Exception
    {
        jsonGenerator.writeFieldName(type);
        if (ObjectUtils.isNotEmpty(configure)) {
            String protocol = configure.getType();
            if (configure.getProtocol().equals(RunProtocol.JDBC)) {
                protocol = "Jdbc";
            }
            Connector factory = ConnectorFactory.createFormatter(ConnectorType.valueOf(protocol), configure);
            for (Map.Entry<String, Object> entry : factory.formatToMap().entrySet()) {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeObjectFieldStart(entry.getKey());
                if (entry.getValue() instanceof Properties) {
                    for (Map.Entry<Object, Object> property : ((Properties) entry.getValue()).entrySet()) {
                        String[] split = property.getValue().toString().split("\n");
                        if (split.length > 1 && !String.valueOf(property.getKey()).equalsIgnoreCase("sql")) {
                            jsonGenerator.writeArrayFieldStart(property.getKey().toString());
                            for (String line : split) {
                                jsonGenerator.writeString(line);
                            }
                            jsonGenerator.writeEndArray();
                        }
                        else {
                            jsonGenerator.writeStringField(property.getKey().toString(), (String) property.getValue());
                        }
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

    /**
     * Generates a configure file in JSON format based on the given ExecutorRequest.
     *
     * @param request the ExecutorRequest object containing the necessary information for generating the configure file
     * @param logger the Logger object for logging any errors that occur during the process
     * @return null if the configure file was generated successfully, or an error message if an exception occurred
     */
    private String before(ExecutorRequest request, Logger logger)
    {
        JsonFactory jsonFactory = new JsonFactory();
        String workFile = String.join(File.separator, request.getWorkHome(), request.getTaskName() + ".configure");
        try (JsonGenerator jsonGenerator = jsonFactory.createGenerator(new File(workFile), JsonEncoding.UTF8)) {
            jsonGenerator.setPrettyPrinter(new DefaultPrettyPrinter());
            jsonGenerator.writeStartObject();
            this.writeChild("env", jsonGenerator, null);
            this.writeChild("source", jsonGenerator, request.getInput());
            this.writeChild("sink", jsonGenerator, request.getOutput());
            jsonGenerator.writeEndObject();
            return null;
        }
        catch (Exception exception) {
            logger.error("Build configure file failed", exception);
            return exception.getMessage();
        }
    }
}
