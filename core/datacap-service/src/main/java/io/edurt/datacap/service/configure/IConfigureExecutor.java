package io.edurt.datacap.service.configure;

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
    private IConfigurePipelineType type;
    private List<IConfigureExecutorField> fields;
}
