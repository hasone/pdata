package com.cmcc.vrp.boss.guangxi.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/9/13.
 */
@XStreamAlias("RoutingInfo")
public class RoutingInfo {
    @XStreamAlias("OrigDomain")
    private String origDomain;
    @XStreamAlias("RouteType")
    private String routeType;
    @XStreamAlias("Routing")
    private Routing routing;

    public String getOrigDomain() {
        return origDomain;
    }

    public void setOrigDomain(String origDomain) {
        this.origDomain = origDomain;
    }

    public String getRouteType() {
        return routeType;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }

    public Routing getRouting() {
        return routing;
    }

    public void setRouting(Routing routing) {
        this.routing = routing;
    }
}
