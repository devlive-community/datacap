package io.edurt.datacap.server.body;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Properties;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PipelineFieldBody
{
    private Long id;
    private Properties configures;
}
