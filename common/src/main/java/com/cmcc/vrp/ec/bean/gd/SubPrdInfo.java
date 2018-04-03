package com.cmcc.vrp.ec.bean.gd;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("SubPrdInfo")
public class SubPrdInfo {

    @XStreamAlias("PrdCode")
    private String prdCode; //增值产品编号
    
    @XStreamAlias("OptCode")
    private String optCode; //0-订购产品，1-注销产品2-修改产品3-保持不变
    
    @XStreamAlias("StartEfft")
    private String startEfft;//生效时间
    
    @XStreamAlias("EndEfft")
    private String endEfft;//失效时间
    
    @XStreamAlias("pkgCode")
    private String pkgCode;//主体产品下的附加组合包编码
    
    @XStreamAlias("Service")
    private Service service;

    public String getPrdCode() {
        return prdCode;
    }

    public void setPrdCode(String prdCode) {
        this.prdCode = prdCode;
    }

    public String getOptCode() {
        return optCode;
    }

    public void setOptCode(String optCode) {
        this.optCode = optCode;
    }

    public String getStartEfft() {
        return startEfft;
    }

    public void setStartEfft(String startEfft) {
        this.startEfft = startEfft;
    }

    public String getEndEfft() {
        return endEfft;
    }

    public void setEndEfft(String endEfft) {
        this.endEfft = endEfft;
    }

    public String getPkgCode() {
        return pkgCode;
    }

    public void setPkgCode(String pkgCode) {
        this.pkgCode = pkgCode;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
