/**
 * @Title: ProductObject.java
 * @Package com.cmcc.vrp.neimenggu.boss.json.model.query
 * @author: qihang
 * @date: 2016年2月5日 上午11:10:23
 * @version V1.0
 */
package com.cmcc.vrp.boss.neimenggu.model.query;


/**
 * @ClassName: ProductObject
 * @Description: 查询企业接口返回信息中产品相关类
 * @author: qihang
 * @date: 2016年2月5日 上午11:10:23
 *
 */
public class ProductObject {
    //{"prodId":"40006118","prodAttrType":0,"groupId":0,"prodName":"通用流量统付-个人30元包500M","prodType":2}
    String prodId; //产品编号

    int prodAttrType;//产品名称

    int groupId; //集团Id

    String prodName; //产品名称

    int prodType;  //产品类型 1.定向2：通用

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public int getProdAttrType() {
        return prodAttrType;
    }

    public void setProdAttrType(int prodAttrType) {
        this.prodAttrType = prodAttrType;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public int getProdType() {
        return prodType;
    }

    public void setProdType(int prodType) {
        this.prodType = prodType;
    }


}
