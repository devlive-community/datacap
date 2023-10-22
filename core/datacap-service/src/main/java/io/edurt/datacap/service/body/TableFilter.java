package io.edurt.datacap.service.body;

import io.edurt.datacap.spi.model.Pagination;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TableFilter
{
    private Pagination pagination;
    private List<OrderFilter> orders;
}
