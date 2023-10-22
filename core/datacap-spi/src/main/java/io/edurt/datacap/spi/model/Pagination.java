package io.edurt.datacap.spi.model;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Pagination
{
    private int pageSize;
    private int currentPage;
    private int totalRecords;

    private Pagination(int pageSize, int currentPage, int totalRecords)
    {
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.totalRecords = totalRecords;
    }

    public static Pagination newInstance(int pageSize, int currentPage, int totalRecords)
    {
        return new Pagination(pageSize, currentPage, totalRecords);
    }

    public int getTotalPages()
    {
        return (int) Math.ceil((double) totalRecords / pageSize);
    }

    public int getStartIndex()
    {
        return (currentPage - 1) * pageSize;
    }

    public int getEndIndex()
    {
        int endIndex = currentPage * pageSize - 1;
        return endIndex < totalRecords ? endIndex : totalRecords - 1;
    }

    public int getPageSize()
    {
        return pageSize;
    }

    public int getCurrentPage()
    {
        return currentPage;
    }

    public int getTotalRecords()
    {
        return totalRecords;
    }

    public boolean getHasPreviousPage()
    {
        return currentPage > 1;
    }

    public boolean getHasNextPage()
    {
        return currentPage < getTotalPages();
    }
}
