package io.edurt.datacap.spi.executor;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuppressFBWarnings(value = {"URF_UNREAD_FIELD"},
        justification = "I prefer to suppress these FindBugs warnings")
public class Pipeline
{
    private String username;
    private String home;
    private String pipelineName;
    private String work;
    private long timeout = 600;
    private PipelineField from;
    private PipelineField to;
    private String way;
    private String mode;
}
