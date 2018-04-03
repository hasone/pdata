/**
 * @Title: 	ResourcePool.java 
 * @Package com.cmcc.xinjiang.boss.model.response 
 * @author:	qihang
 * @date:	2016年3月30日 下午5:59:13 
 * @version	V1.0   
 */
package com.cmcc.vrp.boss.xinjiang.response;

/**
 * @ClassName: ResourcePool
 * @Description: 5.6. 集团用户产品流量池信息查询 返回流量池的基本信息
 * @author: qihang
 * @date: 2016年3月30日 下午5:59:13
 * 
 */

public class ResourcePool {
    private String allInitValue;// 初始资源值

    private String startDate;// 生效时间

    private String userId;// 用户标识

    private String allValue;// 资源值

    private String endDate;// 失效时间

    private String resId;// 惟一标识

    public String getAllInitValue() {
        return allInitValue;
    }

    public void setAllInitValue(String allInitValue) {
        this.allInitValue = allInitValue;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAllValue() {
        return allValue;
    }

    public void setAllValue(String allValue) {
        this.allValue = allValue;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

}
