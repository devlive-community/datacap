package io.edurt.datacap.service.body;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.executor.common.RunProtocol;
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
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class PipelineFieldBody
{
    private Long id;
    private Properties configures;
    private RunProtocol protocol;
}
