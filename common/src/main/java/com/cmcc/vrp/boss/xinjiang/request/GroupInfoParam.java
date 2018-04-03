/**
 * @Title: 	GroupInfoParam.java 
 * @Package com.cmcc.xinjiang.boss.model 
 * @author:	qihang
 * @date:	2016年3月29日 下午10:54:05 
 * @version	V1.0   
 */
package com.cmcc.vrp.boss.xinjiang.request;

/** 
 * @ClassName:	GroupInfoParam 
 * @Description:  查询企业参数
 * @author:	qihang
 * @date:	2016年3月29日 下午10:54:05 
 *  
 */
public class GroupInfoParam implements ServiceBasicParam {

    private String groupId;// 集团Id

    public GroupInfoParam(String groupId) {
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
