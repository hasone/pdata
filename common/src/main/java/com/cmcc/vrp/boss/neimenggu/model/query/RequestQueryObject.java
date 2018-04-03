/**
 * @Title: RequestQueryObject.java
 * @Package com.cmcc.vrp.neimenggu.boss.json.model
 * @author: qihang
 * @date: 2016年2月2日 下午3:46:30
 * @version V1.0
 */
package com.cmcc.vrp.boss.neimenggu.model.query;

/**
 * @ClassName: RequestQueryObject
 * @Description: 发送对象的报文类
 * @author: qihang
 * @date: 2016年2月2日 下午3:46:30
 *
 */
public class RequestQueryObject {
    QueryBusiParams busiParams;//需要的参数

    String busiCode;//接口编码：OP_GetGroupInfo

    public QueryBusiParams getBusiParams() {
        return busiParams;
    }

    public void setBusiParams(QueryBusiParams busiParams) {
        this.busiParams = busiParams;
    }

    public String getBusiCode() {
        return busiCode;
    }

    public void setBusiCode(String busiCode) {
        this.busiCode = busiCode;
    }


}
