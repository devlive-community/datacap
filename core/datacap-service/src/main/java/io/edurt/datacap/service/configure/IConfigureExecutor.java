package io.edurt.datacap.service.configure;

import io.edurt.datacap.spi.executor.Protocol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class IConfigureExecutor
{
    private String executor;
    private Protocol protocol = Protocol.NONE;
    private IConfigurePipelineType type;
    private List<IConfigureExecutorField> fields;
}
