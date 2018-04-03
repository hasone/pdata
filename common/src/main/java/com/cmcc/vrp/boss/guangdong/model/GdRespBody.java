package com.cmcc.vrp.boss.guangdong.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/10/12.
 */
@XStreamAlias("BODY")
public class GdRespBody {

    @XStreamAlias("ECCode")
    private String eCCode;

    @XStreamAlias("PrdOrdNum")
    private String prdOrdNum;

    @XStreamAlias("Member")
    private GdRespMember gdRespMember;

    /**
     * 获取code
     * @return
     */
    public String geteCCode() {
        return eCCode;
    }

    /**
     * 设置code
     * @param eCCode  code值
     */
    public void seteCCode(String eCCode) {
        this.eCCode = eCCode;
    }

    public String getPrdOrdNum() {
        return prdOrdNum;
    }

    public void setPrdOrdNum(String prdOrdNum) {
        this.prdOrdNum = prdOrdNum;
    }

    public GdRespMember getGdRespMember() {
        return gdRespMember;
    }

    public void setGdRespMember(GdRespMember gdRespMember) {
        this.gdRespMember = gdRespMember;
    }
}
