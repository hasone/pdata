/**
 * 
 */
package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @desc:
 * @author: wuguoping
 * @data: 2017年5月31日
 */
@XStreamAlias("Response")
public class TimeResp {
    @XStreamAlias("Datetime")
    String responseTime;

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

}
