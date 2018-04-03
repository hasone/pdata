package com.cmcc.vrp.ec.bean.gd;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("Service")
public class Service {

    @XStreamAlias("ServiceId")
    private String serviceId; //服务编码
    
    @XStreamAlias("ServiceCode")
    private String serviceCode; //业务编码
    
    @XStreamImplicit(itemFieldName="USERINFOMAP")
    private List<USERINFOMAP> userinfomap; //业务配置参数

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public List<USERINFOMAP> getUserinfomap() {
        return userinfomap;
    }

    public void setUserinfomap(List<USERINFOMAP> userinfomap) {
        this.userinfomap = userinfomap;
    }

}
