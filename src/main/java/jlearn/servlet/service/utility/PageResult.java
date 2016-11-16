package jlearn.servlet.service.utility;

import java.util.List;

public class PageResult<T>
{
    private List<T> slice;
    private PageRequest originRequest;
    private int totalRecords;
    private int totalPages;

    public PageResult(List<T> slice, int totalRecords, PageRequest originRequest)
    {
        this.slice = slice;
        this.totalRecords = totalRecords;
        this.originRequest = originRequest;
        this.totalPages = (int) Math.ceil((double)totalRecords / (double)originRequest.getPageSize());
    }

    public List<T> getSlice()
    {
        return slice;
    }

    public int getTotalRecords()
    {
        return totalRecords;
    }

    public int getTotalPages()
    {
        return totalPages;
    }

    public boolean hasNext()
    {
        return originRequest.getPageNum() < getTotalPages();
    }

    public int getPageNum()
    {
        return originRequest.getPageNum();
    }
}
