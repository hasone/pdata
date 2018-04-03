package com.cmcc.vrp.boss.hebei.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/10/12.
 */
@XStreamAlias("Response")
public class QueryResponse {

    @XStreamAlias("RspCode")
    private String rspCode;

    @XStreamAlias("RspDesc")
    private String rspDesc;
}
