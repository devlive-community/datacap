package io.edurt.datacap.spi.executor;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.Properties;
import java.util.Set;

@Data
@Builder
@ToString
public class PipelineField
{
    private String type;
    private Properties configure;
    private Set<String> supportOptions;
}
