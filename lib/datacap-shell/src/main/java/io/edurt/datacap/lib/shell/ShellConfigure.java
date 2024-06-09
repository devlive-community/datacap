package io.edurt.datacap.lib.shell;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ShellConfigure
{
    private List<String> command;
    private String directory;
    private String username;
    private long timeout = 60;
    private LoggerExecutor loggerExecutor;
}
