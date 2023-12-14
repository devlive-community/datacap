package io.edurt.datacap.service.body;

import io.edurt.datacap.sql.model.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TableBody
{
    private String name;
    private String comment;
    private String engine;
    private List<Column> columns;
}
