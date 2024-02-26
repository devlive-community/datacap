package io.edurt.datacap.service.body.adhoc;

import io.edurt.datacap.service.enums.ColumnMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AdhocColumn
{
    private Long id;
    private String expression;
    private String function;
    private String alias;
    private String order;
    private ColumnMode mode;
    private String value;
}
