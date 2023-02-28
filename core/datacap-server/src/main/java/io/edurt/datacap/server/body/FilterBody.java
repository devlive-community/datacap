package io.edurt.datacap.server.body;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FilterBody
{
    private Integer page = 1;
    private Integer size = 10;
    private List<OrderBody> orders;
}
