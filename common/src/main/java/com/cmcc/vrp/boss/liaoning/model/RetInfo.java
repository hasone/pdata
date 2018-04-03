package com.cmcc.vrp.boss.liaoning.model;

import java.util.List;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年2月22日 下午5:30:17
*/
public class RetInfo {
    private ErrorInfo ErrorInfo;
    
    private String BatchNO;
    
    private String CustName;
    
    private String Date;
    
    private String FailureFee;
    
    private String FailureNO;
    
    private String SuccessFee;
    
    private String SuccessNO;
    
    private List<Detail> Detail;
    
    private List<Prod> ProdList;
    
    public ErrorInfo getErrorInfo() {
        return ErrorInfo;
    }

    public void setErrorInfo(ErrorInfo errorInfo) {
        ErrorInfo = errorInfo;
    }

    public String getBatchNO() {
        return BatchNO;
    }

    public void setBatchNO(String batchNO) {
        BatchNO = batchNO;
    }

    public String getCustName() {
        return CustName;
    }

    public void setCustName(String custName) {
        CustName = custName;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getFailureFee() {
        return FailureFee;
    }

    public void setFailureFee(String failureFee) {
        FailureFee = failureFee;
    }

    public String getFailureNO() {
        return FailureNO;
    }

    public void setFailureNO(String failureNO) {
        FailureNO = failureNO;
    }

    public String getSuccessFee() {
        return SuccessFee;
    }

    public void setSuccessFee(String successFee) {
        SuccessFee = successFee;
    }

    public String getSuccessNO() {
        return SuccessNO;
    }

    public void setSuccessNO(String successNO) {
        SuccessNO = successNO;
    }

    public List<Detail> getDetail() {
        return Detail;
    }

    public void setDetail(List<Detail> detail) {
        Detail = detail;
    }

    public List<Prod> getProdList() {
        return ProdList;
    }

    public void setProdList(List<Prod> prodList) {
        ProdList = prodList;
    }

}
