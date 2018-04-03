package com.cmcc.vrp.wx.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月4日 上午8:45:43
*/
@XStreamAlias("SubmitOrderReq")
public class SubmitOrderReq {
    @XStreamAlias("ECCode")
    private String eCCode;

    @XStreamAlias("PrdOrdNum")
    private String prdOrdNum;

    @XStreamAlias("PoId")
    private String poId;

    @XStreamAlias("Member")
    private Member member;

    /**
     * @return
     */
    public String geteCCode() {
        return eCCode;
    }

    /**
     * @param eCCode
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

    public String getPoId() {
        return poId;
    }

    public void setPoId(String poId) {
        this.poId = poId;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
    

}
