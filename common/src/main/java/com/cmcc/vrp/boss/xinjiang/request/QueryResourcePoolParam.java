/**
 * @Title: 	QueryResourcePoolParam.java 
 * @Package com.cmcc.xinjiang.boss.model 
 * @author:	qihang
 * @date:	2016年3月29日 下午10:57:28 
 * @version	V1.0   
 */
package com.cmcc.vrp.boss.xinjiang.request;

/** 
 * @ClassName:	QueryResourcePoolParam 
 * @Description:  集团用户产品流量池信息查询，参数
 * @author:	qihang
 * @date:	2016年3月29日 下午10:57:28 
 *  
 */
public class QueryResourcePoolParam implements ServiceBasicParam {
    private String groupId;

    public QueryResourcePoolParam(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * 转化为packet
     */
    public String toPacket() {
        return "{GROUP_ID=[\"" + groupId + "\"]}";
    }

}
