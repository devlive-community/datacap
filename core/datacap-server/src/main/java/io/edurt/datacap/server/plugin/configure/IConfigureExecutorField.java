package io.edurt.datacap.server.plugin.configure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class IConfigureExecutorField
{
    private String field;
    private boolean required;
}
