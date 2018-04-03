package com.cmcc.vrp.boss.shyc.pojos;

import java.util.List;

/**
 * 上海月呈定义的查询流量包定义的响应体
 *
 * Created by sunyiwei on 2017/3/14.
 */
public class FlowPackageQueryResponse {
    private String code;
    private String message;
    private List<FlowPacakge> packages;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FlowPacakge> getPackages() {
        return packages;
    }

    public void setPackages(List<FlowPacakge> packages) {
        this.packages = packages;
    }
}
