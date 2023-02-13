package io.edurt.datacap.server.adapter;

import io.edurt.datacap.common.CollectorUtils;
import io.edurt.datacap.server.body.FilterBody;
import io.edurt.datacap.server.body.OrderBody;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class PageRequestAdapter
{
    private PageRequestAdapter()
    {
    }

    public static PageRequest of(int start, int end)
    {
        if (start > 0) {
            start = start - 1;
        }
        return PageRequest.of(start, end);
    }

    public static PageRequest of(int start, int end, Sort sort)
    {
        if (start > 0) {
            start = start - 1;
        }
        return PageRequest.of(start, end, sort);
    }

    public static PageRequest of(FilterBody filter)
    {
        PageRequest pageable = PageRequestAdapter.of(filter.getPage(), filter.getSize());
        if (ObjectUtils.isNotEmpty(filter.getOrders())) {
            // Remove duplicate data passed
            List<OrderBody> remoted = new ArrayList<>();
            filter.getOrders().stream().filter(CollectorUtils.distinctByKey(p -> p.getColumn()))  //filter保留true的值
                    .forEach(remoted::add);

            List<Sort.Order> orders = new ArrayList<>();
            remoted.stream()
                    .filter(value -> value.getOrder().equalsIgnoreCase("asc") || value.getOrder().equalsIgnoreCase("desc")) // Filter other
                    .forEach(value -> {
                        if (value.getOrder().equalsIgnoreCase("desc")) {
                            orders.add(Sort.Order.desc(value.getColumn()));
                        }
                        else {
                            orders.add(Sort.Order.asc(value.getColumn()));
                        }
                    });
            Sort sort = Sort.by(orders);
            pageable = PageRequestAdapter.of(filter.getPage(), filter.getSize(), sort);
        }
        return pageable;
    }
}
