package io.edurt.datacap.service.body;

import io.edurt.datacap.service.configure.IConfigure;
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
