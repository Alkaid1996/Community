package com.demoCommunity.Community.entity;

/**
 * 封装分页相关的信息
 */
public class Page {
    //当前页码
    private int current = 1;

    //显示上限
    private int limit = 10;

    //数据总数
    private int rows;

    //查询路径(用来分页连接）
    private String path;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (current >= 1) {
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit >= 1 && limit <= 100) {
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows >= 0) {
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 从当前页的页码来计算当前页的起始行
     *
     * @return
     */
    public int getOffset() {
        return (current - 1) * limit;
    }

    /**
     * 显示总页数
     *
     * @return
     */
    public int getTotal() {
        //能够整除的话就刚好，不能整除，就加1页
        if (rows % limit == 0) {
            return rows / limit;
        } else {
            return rows / limit + 1;
        }
    }

    /**
     * 获取要显示的起始页码
     */
    public int getFrom() {
        //显示之前的两页，如果之前的页已经在第一页前了，就显示第一页
        int from = current - 2;
        return from < 1 ? 1 : from;
    }

    /**
     * 获取要显示的结束页码
     */
    public int getTo() {
        int to = current + 2;
        int total = getTotal();
        return to > total ? total : to;
    }
}
