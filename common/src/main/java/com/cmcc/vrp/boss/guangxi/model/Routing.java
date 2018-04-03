package com.cmcc.vrp.boss.guangxi.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/9/13.
 */
@XStreamAlias("Routing")
public class Routing {
    @XStreamAlias("HomeDomain")
    private String homeDomain;
    @XStreamAlias("RouteValue")
    private String routeValue;

    public String getHomeDomain() {
        return homeDomain;
    }

    public void setHomeDomain(String homeDomain) {
        this.homeDomain = homeDomain;
    }

    public String getRouteValue() {
        return routeValue;
    }

    public void setRouteValue(String routeValue) {
        this.routeValue = routeValue;
    }
}
