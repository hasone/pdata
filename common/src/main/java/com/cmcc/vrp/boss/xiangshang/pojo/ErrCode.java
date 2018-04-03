package com.cmcc.vrp.boss.xiangshang.pojo;

/**
 * 向上公司定义的错误码
 * <p>
 * Created by sunyiwei on 2016/12/9.
 */
public enum ErrCode {
    InvalidParam("InvalidParam", "无效的充值参数", Status.FAIL), //平台自定义
    ValidateFail("ValidateFail", "签名校验失败", Status.FAIL),  //平台自定义，只适用于响应
    OrderSuccess("OrderSuccess", "订单充值成功", Status.SUCCESS),
    OrderExists("OrderExists", "代理商订单号重复", Status.FAIL),
    OrderFail("OrderFail", "订单处理失败", Status.FAIL),
    OrderSended("OrderSended", "系统已经正确接收订单，并正在进行充值", Status.PROCESSING),
    ConfigError("ConfigError", "配置性错误，具体错误查看错误信息", Status.FAIL),
    NumberError("NumberError", "号码错误", Status.FAIL),
    AccountClosed("AccountClosed", "充值账号已经关闭，请联系商务", Status.FAIL),
    AccountBalance("AccountBalance", "账号余额不足", Status.FAIL),
    ProductClosed("ProductClosed", "充值产品暂不可用，稍后再试", Status.FAIL),
    ProductError("ProductError", "产品错误，请联系商务", Status.FAIL),
    OrderNotExists("OrderNotExists", "订单查询失败，请延时5分钟再查，若还是查询不到，务必重新下单请求", Status.PROCESSING);

    private String errcode;
    private String message;
    private Status status;

    ErrCode(String errcode, String message, Status status) {
        this.errcode = errcode;
        this.message = message;
        this.status = status;
    }

    /**
     * 解析errcode， 返回相应的枚举对象
     *
     * @param errcode 错误码
     * @return
     */
    public static ErrCode fromCode(String errcode) {
        for (ErrCode ecode : ErrCode.values()) {
            if (ecode.getErrcode().equals(errcode)) {
                return ecode;
            }
        }

        return null;
    }

    public String getErrcode() {
        return errcode;
    }

    public String getMessage() {
        return message;
    }

    public Status getStatus() {
        return status;
    }
}
