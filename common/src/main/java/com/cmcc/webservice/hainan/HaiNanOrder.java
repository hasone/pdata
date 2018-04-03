package com.cmcc.webservice.hainan;

import java.util.Date;
import java.util.List;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午5:00:23
*/
public class HaiNanOrder {

    private Date requestTime;//请求时间

    private String requestSerialNumber;//请求序列号

    private String grpId;//集团编码

    private List<HaiNanBOSSProduct> item;//产品余额信息

    public String getRequestSerialNumber() {
        return requestSerialNumber;
    }

    public void setRequestSerialNumber(String requestSerialNumber) {
        this.requestSerialNumber = requestSerialNumber;
    }

    public String getGrpId() {
        return grpId;
    }

    public void setGrpId(String grpId) {
        this.grpId = grpId;
    }

    public List<HaiNanBOSSProduct> getItem() {
        return item;
    }

    public void setItem(List<HaiNanBOSSProduct> item) {
        this.item = item;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }


}
