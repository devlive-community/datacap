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
public class PipelineResponse
{
    private PipelineState state;
    private boolean timeout;
    private boolean successful;
    private String message;
}
