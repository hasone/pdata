package com.cmcc.vrp.boss.xiangshang.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 向上公司BOSS接口的响应对象
 * <p>
 * Created by sunyiwei on 2016/12/9.
 */
@XStreamAlias("result")
public class ResponsePojo {
    //订单号
    @XStreamAlias("id")
    private String id;

    //平台侧流水号
    @XStreamAlias("OrderId")
    private String orderid;

    //充值面额，整数
    @XStreamAlias("Deno")
    private int deno;

    //充值成功面额， 整数
    @XStreamAlias("SuccessDeno")
    private int successdeno;

    //错误代码， 状态代码
    @XStreamAlias("ErrCode")
    private String errcode;

    //错误信息
    @XStreamAlias("ErrInfo")
    private String errinfo;

    //消息签名
    @XStreamAlias("Sign")
    private String sign;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public int getDeno() {
        return deno;
    }

    public void setDeno(int deno) {
        this.deno = deno;
    }

    public int getSuccessdeno() {
        return successdeno;
    }

    public void setSuccessdeno(int successdeno) {
        this.successdeno = successdeno;
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrinfo() {
        return errinfo;
    }

    public void setErrinfo(String errinfo) {
        this.errinfo = errinfo;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
