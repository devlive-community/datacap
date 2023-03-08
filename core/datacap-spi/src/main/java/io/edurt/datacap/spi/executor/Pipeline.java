package io.edurt.datacap.spi.executor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Pipeline
{
    private String username;
    private String home;
    private String work;
    private String file;
    private long timeout = 600;
    private PipelineField from;
    private PipelineField to;

    public String getFile()
    {
        return String.join("/", this.getWork(), String.format("%s.configure", UUID.randomUUID().toString()));
    }
}
