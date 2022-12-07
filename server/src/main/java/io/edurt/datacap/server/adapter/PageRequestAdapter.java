package io.edurt.datacap.server.adapter;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

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
}
