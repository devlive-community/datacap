package io.edurt.datacap.plugin.http.greptime.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.edurt.datacap.plugin.http.greptime.response.record.Output;
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
public class GreptimeDBResponse
{
    private int code;
    private String error;
    private List<Output> output;
}
