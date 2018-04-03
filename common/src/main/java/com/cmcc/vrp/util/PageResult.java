/**
 * @Title: TestPageResult.java
 * @Package com.cmcc.vrp.util
 * @author: sunyiwei
 * @date: 2015年3月16日 下午4:57:55
 * @version V1.0
 */
package com.cmcc.vrp.util;

import java.util.List;

/**
 * @ClassName: TestPageResult
 * @Description: 通用的分页处理类
 * @author: sunyiwei
 * @date: 2015年3月16日 下午4:57:55
 *
 */
public class PageResult<T> {
    /**
     * 总页数
     */
    private int pages = 0;

    /**
     * 记录总数
     */
    private long records = 0;

    /**
     * 当前查询到的记录列表
     */
    private List<T> list = null;

    private String callbackURL = null;

    /**
     * 当前的查询对象，包含所有查询需要的信息
     */
    private QueryObject queryObject = new QueryObject();

    public PageResult() {
        super();
    }

    public PageResult(QueryObject queryObject, long records, List<T> list) {
        this.records = records;
        this.queryObject = queryObject;
        // compute total pages.
        this.pages = computePages(queryObject.getPageSize(), records);
        this.list = list;
    }


    /**
     * 重写默认配置函数，添加callbackURL属性
     * @author qihang
     */
    public PageResult(QueryObject queryObject, long records, List<T> list, String callbackURL) {
        //记录总个数
        this.records = records;
        //记录queryObject
        this.queryObject = queryObject;
        // compute total pages.
        this.pages = computePages(queryObject.getPageSize(), records);
        //设置callbackURL
        this.callbackURL = callbackURL;
        this.list = list;
    }

    private static int computePages(int pageSize, long records) {
        int nPageCount = (int) Math.floor((int) records / (1.0 * pageSize));
        if (records % pageSize != 0) {
            nPageCount++;
        }

        return nPageCount;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public long getRecords() {
        return records;
    }

    public void setRecords(long records) {
        this.records = records;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public QueryObject getQueryObject() {
        return queryObject;
    }

    public void setQueryObject(QueryObject queryObject) {
        this.queryObject = queryObject;
    }

    public String getCallbackURL() {
        return callbackURL;
    }

    public void setCallbackURL(String callbackURL) {
        this.callbackURL = callbackURL;
    }
}
