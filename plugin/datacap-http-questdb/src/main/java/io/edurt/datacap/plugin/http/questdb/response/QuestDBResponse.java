package io.edurt.datacap.plugin.http.questdb.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class QuestDBResponse
{
    private String error;
    private String query;
    private List<Schema> columns;
    private List<List<Object>> dataset;
}
