package com.cmcc.vrp.wx.flowcoin.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/7/11.
 */
@XStreamAlias("BODY")
public class GdBody {

    @XStreamAlias("ECCode")
    private String eCCode;

    @XStreamAlias("PrdOrdNum")
    private String prdOrdNum;

    @XStreamAlias("Member")
    private GdMember member;

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

    public GdMember getMember() {
        return member;
    }

    public void setMember(GdMember member) {
        this.member = member;
    }
}
