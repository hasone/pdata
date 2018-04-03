package com.cmcc.vrp.wx.beans;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2017/1/5.
 */
@XStreamAlias("CheckMobileResp")
public class CheckMobileResp {

    @XStreamAlias("Result")
    private Integer result;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }
}
