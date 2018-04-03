package com.cmcc.vrp.boss.henan.model;

/**
 * Created by leelyn on 2016/8/29.
 */
public class HaCallbackReq {

    private String method;
    private String jd_json_paras;
    private String province_code;
    private String sign;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getJd_json_paras() {
        return jd_json_paras;
    }

    public void setJd_json_paras(String jd_json_paras) {
        this.jd_json_paras = jd_json_paras;
    }

    public String getProvince_code() {
        return province_code;
    }

    public void setProvince_code(String province_code) {
        this.province_code = province_code;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
