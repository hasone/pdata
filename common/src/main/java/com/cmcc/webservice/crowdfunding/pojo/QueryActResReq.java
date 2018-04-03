package com.cmcc.webservice.crowdfunding.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Request")
public class QueryActResReq {
    
    @XStreamAlias("Datetime")
    private String dateTime;
    
    @XStreamAlias("QueryActRes")
    private QueryActResPojo queryActResPojo;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public QueryActResPojo getQueryActResPojo() {
        return queryActResPojo;
    }

    public void setQueryActResPojo(QueryActResPojo queryActResPojo) {
        this.queryActResPojo = queryActResPojo;
    }

    
}
