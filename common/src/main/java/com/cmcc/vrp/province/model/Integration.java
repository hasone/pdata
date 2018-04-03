package com.cmcc.vrp.province.model;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午2:58:02
*/
public class Integration {

    private Long id;

    private String entName;

    private String entCode;  //企业编码

    private String initName; //积分名称

    private String rate; //积分：流量兑换比例

    private String createTime;

    private String updateTime;

    private Long phoneNum; //兑换号码个数

    private String phone;

    private String proName;

    private String exchangeTime;  //兑换时间

    private String result; //兑换结果

    private Long status; //1成功；2失败

    private Long integrate; //积分

    private Long flow;//流量

    public Long getIntegrate() {
        return integrate;
    }

    public void setIntegrate(Long integrate) {
        this.integrate = integrate;
    }

    public Long getFlow() {
        return flow;
    }

    public void setFlow(Long flow) {
        this.flow = flow;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntName() {
        return entName;
    }

    public void setEntName(String entName) {
        this.entName = entName;
    }

    public String getEntCode() {
        return entCode;
    }

    public void setEntCode(String entCode) {
        this.entCode = entCode;
    }

    public String getInitName() {
        return initName;
    }

    public void setInitName(String initName) {
        this.initName = initName;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Long getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(Long phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getExchangeTime() {
        return exchangeTime;
    }

    public void setExchangeTime(String exchangeTime) {
        this.exchangeTime = exchangeTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }


}
