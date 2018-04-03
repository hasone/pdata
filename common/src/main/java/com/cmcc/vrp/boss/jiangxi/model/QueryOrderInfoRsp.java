package com.cmcc.vrp.boss.jiangxi.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;


/**
 * @author lgk8023
 *
 */
@XStreamAlias("QueryOrderInfoRsp")
public class QueryOrderInfoRsp {

    @XStreamAlias("HEAD")
    private JxQueryHead head;

    @XStreamAlias("BODY")
    private String body;

    public JxQueryHead getHead() {
        return head;
    }

    public void setHead(JxQueryHead head) {
        this.head = head;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
