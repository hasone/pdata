package com.cmcc.vrp.province.module;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/6/28.
 */
@XStreamAlias("ROOT")
public class PersonalFlowResp {

    @XStreamAlias("RETURN_CODE")
    String returnCode;

    @XStreamAlias("RETURN_MSG")
    String returnMsg;

    @XStreamAlias("DETAIL_MSG")
    String detailMsg;

    @XStreamAlias("OUT_DATA")
    String outDate;

}
