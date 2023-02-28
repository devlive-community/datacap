package io.edurt.datacap.plugin.http.cratedb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CrateDBResponse
{
    @JsonProperty(value = "cols")
    private List<String> headers;

    @JsonProperty(value = "col_types")
    private List<String> types;

    @JsonProperty(value = "rows")
    private List<Object> columns;

    @JsonProperty(value = "error")
    private CrateDBResponseError error;
}
