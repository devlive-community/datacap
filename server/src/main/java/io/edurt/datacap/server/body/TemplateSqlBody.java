package io.edurt.datacap.server.body;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

import java.util.Map;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TemplateSqlBody
{
    @NotBlank
    private String templateName;

    @NotBlank
    private Long sourceId;

    private Map<String, Object> configure;
}
