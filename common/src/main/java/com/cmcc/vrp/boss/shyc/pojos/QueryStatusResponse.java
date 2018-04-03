package com.cmcc.vrp.boss.shyc.pojos;

import java.util.List;

/**
 * 上海月呈定义的查询订单状态的响应对象
 *
 * Created by sunyiwei on 2017/3/14.
 */
public class QueryStatusResponse {
    private String code;
    private String message;
    private List<Report> reports;

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

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }
}
