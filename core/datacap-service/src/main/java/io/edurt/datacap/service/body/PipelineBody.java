package io.edurt.datacap.service.body;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PipelineBody
{
    private Long id;
    private PipelineFieldBody from;
    private PipelineFieldBody to;
    private String content;
    private String executor;
}
