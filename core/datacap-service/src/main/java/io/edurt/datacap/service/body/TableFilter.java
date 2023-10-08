package io.edurt.datacap.service.body;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TableFilter
{
    private long limit = 500;
    private long offset = 1;
}
