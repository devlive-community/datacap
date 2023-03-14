package io.edurt.datacap.lib.shell;

import io.edurt.datacap.lib.logger.LoggerExecutor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShellConfigure
{
    private List<String> command;
    private String directory;
    private String username;
    private long timeout = 60;
    private LoggerExecutor loggerExecutor;
}
