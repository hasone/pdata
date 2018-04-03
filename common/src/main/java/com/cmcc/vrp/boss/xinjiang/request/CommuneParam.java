/**
 * @Title: 	CommuneParam.java 
 * @Package com.cmcc.xinjiang.boss.model 
 * @author:	qihang
 * @date:	2016年3月29日 下午10:33:59 
 * @version	V1.0   
 */
package com.cmcc.vrp.boss.xinjiang.request;

/** 
 * @ClassName:	CommuneParam 
 * @Description:  公用参数seqId + transCode
 * @author:	qihang
 * @date:	2016年3月29日 下午10:33:59 
 *  
 */
public class CommuneParam {

    public String seqId;// 访问的随机字符串，boss不做检验

    public String transCode;// 交易编码，指定是4种接口的哪一种类型

    public CommuneParam(String seqId, String transCode) {
        this.seqId = seqId;
        this.transCode = transCode;
    }

    public String getSeqId() {
        return seqId;
    }

    public void setSeqId(String seqId) {
        this.seqId = seqId;
    }

    public String getTransCode() {
        return transCode;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }

}
