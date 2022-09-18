package io.edurt.datacap.server.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@ToString
public class PageEntity<T>
{
    private int page;
    private int size;
    private long total;
    private long totalPage;
    private List<T> content;

    private PageEntity()
    {
    }

    public static PageEntity build(Page page)
    {
        PageEntity pageEntity = new PageEntity<>();
        pageEntity.setPage(page.getNumber());
        pageEntity.setSize(page.getSize());
        pageEntity.setTotal(page.getTotalElements());
        pageEntity.setTotalPage(page.getTotalPages());
        pageEntity.setContent(page.getContent());
        return pageEntity;
    }
}
