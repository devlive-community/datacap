package io.edurt.datacap.server.request.body;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SharedSourceBody
        extends BaseUserBody
{
    @JsonProperty(value = "public")
    private Boolean publish;
    private Long sourceId;
}
