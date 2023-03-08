package io.edurt.datacap.spi.executor;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.Properties;

@Data
@Builder
@ToString
public class PipelineField
{
    private String type;
    private Properties configure;
}
