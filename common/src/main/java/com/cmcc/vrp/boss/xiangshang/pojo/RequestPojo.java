package com.cmcc.vrp.boss.xiangshang.pojo;

/**
 * 向上公司的充值对象
 *
 * Created by sunyiwei on 2016/12/9.
 */
public class RequestPojo {
    //时间
    private String time;

    //代理商标识
    private String macid;

    //签名
    private String sign;

    //充值号码
    private String phone;

    //充值面额，整数
    private int deno;

    //订单流水号
    private String orderId;

    //运营商编号
    private String arsid;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getDeno() {
        return deno;
    }

    public void setDeno(int deno) {
        this.deno = deno;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getArsid() {
        return arsid;
    }

    public void setArsid(String arsid) {
        this.arsid = arsid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMacid() {
        return macid;
    }

    public void setMacid(String macid) {
        this.macid = macid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
