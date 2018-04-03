package com.cmcc.vrp.boss.jiangsu.model;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
/**
 * @author lgk8023
 *
 */
@XStreamAlias("content")
public class QryOrderResponseContent {
    
    @XStreamAlias("ret_code")
    private String retCode;

    @XStreamImplicit(itemFieldName="qrylist")
    private List<Qrylist> qrylist;
    public String getRetCode() {
        return retCode;
    }
    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }
    public List<Qrylist> getQrylist() {
        return qrylist;
    }
    public void setQrylist(List<Qrylist> qrylist) {
        this.qrylist = qrylist;
    }
   
}
