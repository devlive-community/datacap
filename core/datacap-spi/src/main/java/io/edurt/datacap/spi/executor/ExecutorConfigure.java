package io.edurt.datacap.spi.executor;

import io.edurt.datacap.lib.logger.LoggerExecutor;
import io.edurt.datacap.lib.shell.ShellCommander;
import io.edurt.datacap.spi.model.Configure;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ExecutorConfigure
{
    private String username;
    private long timeout = 600;
    private LoggerExecutor logger;
    private ShellCommander commander;
    private Configure from;
    private Configure to;
}
