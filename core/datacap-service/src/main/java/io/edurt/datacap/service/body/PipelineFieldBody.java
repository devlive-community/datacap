package io.edurt.datacap.service.body;

import io.edurt.datacap.spi.executor.Protocol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Properties;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PipelineFieldBody
{
    private Long id;
    private Properties configures;
    private Protocol protocol;
}
