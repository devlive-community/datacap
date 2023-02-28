package io.edurt.datacap.server.body;

import io.edurt.datacap.server.plugin.configure.IConfigure;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SourceBody
{
    private Long id;
    private String type;
    private String name;
    private IConfigure configure;
}
