package com.cmcc.vrp.boss.beijing.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/8/11.
 */
@XStreamAlias("WebBody")
public class QueryReqBody {

    @XStreamAlias("Params")
    private QueryReqPara para;

    public QueryReqPara getPara() {
        return para;
    }

    public void setPara(QueryReqPara para) {
        this.para = para;
    }
}
