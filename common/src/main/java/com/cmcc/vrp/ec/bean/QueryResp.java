package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.List;

/**
 * Created by leelyn on 2016/5/24.
 */
@XStreamAlias("Response")
public class QueryResp {

    @XStreamAlias("Datetime")
    String responseTime;

    @XStreamAlias("Records")
    List<QueryItem> items;

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public List<QueryItem> getItems() {
        return items;
    }

    public void setItems(List<QueryItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "QueryResp [responseTime=" + responseTime + ", items=" + items + "]";
    }
    
}
