/**
 * @Title: ResponseQueryData.java
 * @Package com.cmcc.vrp.neimenggu.boss.json.model.query
 * @author: qihang
 * @date: 2016年2月5日 上午11:07:25
 * @version V1.0
 */
package com.cmcc.vrp.boss.neimenggu.model.query;

import java.util.List;

/**
 * @ClassName: ResponseQueryData
 * @Description: 返回对象
 * @author: qihang
 * @date: 2016年2月5日 上午11:07:25
 *
 */
public class ResponseQueryData {
    String groupId = "";  //集团编号

    String groupName = ""; //集团名称

    String balance = ""; //集团账户可用余额,已正数为单位。例如1元返回100

    String effDate = ""; //集团注册生效时间

    int status = -1;//集团状态

    List<ProductObject> prodList;  //企业相关信息

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getRoupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getEffDate() {
        return effDate;
    }

    public void setEffDate(String effDate) {
        this.effDate = effDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ProductObject> getProdList() {
        return prodList;
    }

    public void setProdList(List<ProductObject> prodList) {
        this.prodList = prodList;
    }


}
