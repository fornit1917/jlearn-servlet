package jlearn.servlet.service.utility;

public class PageRequest
{
    private int pageSize;
    private int pageNum;

    public PageRequest(int pageNum, int pageSize)
    {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public int getPageSize()
    {
        return pageSize;
    }

    public int getPageNum()
    {
        return pageNum;
    }

    public int getOffset()
    {
        return (pageNum - 1) * pageSize;
    }
}
