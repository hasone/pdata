/**
 * @Title: QueryObject.java
 * @Package com.cmcc.vrp.util
 * @author: sunyiwei
 * @date: 2015年3月16日 下午5:14:55
 * @version V1.0
 */
package com.cmcc.vrp.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName: QueryObject
 * @Description: 通用的查询对象
 * @author: sunyiwei
 * @date: 2015年3月16日 下午5:14:55
 *
 */
public class QueryObject {

    private int back = 0;//是否是返回操作，1为是，0为否
    
    /**
     * 当前查询页
     */
    private int pageNum = 1;

    /**
     * 每页最大显示记录数
     */
    private int pageSize = 10;

    /**
     * 查询条件，支持的key由具体的service说明
     */
    private Map<String, Object> queryCriterias = new LinkedHashMap<String, Object>();

    public QueryObject() {

    }

    public QueryObject(int pageNum, int pageSize,
                       Map<String, Object> queryCriterias) {
        this.queryCriterias = queryCriterias;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public QueryObject(int pageNum, Map<String, Object> queryCriterias) {
        this.queryCriterias = queryCriterias;
        this.pageNum = pageNum;
    }

    public QueryObject(Map<String, Object> queryCriterias) {
        this.queryCriterias = queryCriterias;
    }

    /**
     * 过滤掉查询中"\\"
     * @param q
     * @return
     */
    public static QueryObject filterQueryObject(QueryObject q) {
        for (Map.Entry<String, Object> entry : q.getQueryCriterias().entrySet()) {
            Object o = entry.getValue();
            if (o instanceof String) {
                String temp = (String) o;
                temp = temp.replaceAll("\\\\_", "_");
                temp = temp.replaceAll("\\\\%", "%");
                entry.setValue(temp);
            }
        }
        return q;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Map<String, Object> getQueryCriterias() {
        return queryCriterias;
    }

    public void setQueryCriterias(Map<String, Object> queryCriterias) {
        this.queryCriterias = queryCriterias;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> queryMap = new HashMap<String, Object>();

        /**
         * 防止用户输入无效的数字
         */
        pageNum = pageNum < 1 ? 1 : pageNum;
        pageSize = pageSize < 10 ? 10 : pageSize;
        
        queryMap.put("pageNum", (pageNum - 1) * pageSize);
        queryMap.put("pageSize", pageSize);

        Iterator iterator = queryCriterias.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String szKey = (String) entry.getKey();
            queryMap.put(szKey, queryCriterias.get(szKey));
        }

        return queryMap;
    }

    public int getBack() {
        return back;
    }

    public void setBack(int back) {
        this.back = back;
    }
    
    public boolean isBack(){
        return (this.back > 0);
    }
}
