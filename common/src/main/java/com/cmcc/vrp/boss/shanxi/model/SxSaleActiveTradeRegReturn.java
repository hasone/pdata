package com.cmcc.vrp.boss.shanxi.model;

public enum SxSaleActiveTradeRegReturn {
	
	SUCCESS("0", "成功");
	
    private String code;
	
    private String msg;
	
    SxSaleActiveTradeRegReturn(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
