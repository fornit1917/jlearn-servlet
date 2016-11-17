package jlearn.servlet.service.utility;

public class PageRequest
{
    private int pageSize;
    private int pageNum;

    public PageRequest(int pageNum, int pageSize)
    {
        this.pageNum = pageNum < 1 ? 1 : pageNum;
        this.pageSize = pageSize < 1 ? 1 : pageSize;
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
