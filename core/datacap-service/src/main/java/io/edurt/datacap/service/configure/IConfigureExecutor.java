package io.edurt.datacap.service.configure;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.executor.common.RunProtocol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class IConfigureExecutor
{
    private String executor;
    private RunProtocol protocol = RunProtocol.NONE;
    private IConfigurePipelineType type;
    private List<IConfigureExecutorField> fields;
}
