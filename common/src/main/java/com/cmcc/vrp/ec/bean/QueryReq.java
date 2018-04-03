package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/5/24.
 */
@XStreamAlias("Request")
public class QueryReq {

    @XStreamAlias("SystemNum")
    String systemNum;
}
