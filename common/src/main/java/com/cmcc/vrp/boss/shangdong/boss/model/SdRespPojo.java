package com.cmcc.vrp.boss.shangdong.boss.model;

/**
 * 山东回调的接口,json
 * 
 * {
     "pkgSeq":"流量平台发起的流水号"
     "code":200  //成功的标志
     "msg":"ok" //成功时返回值，如果失败，记录boss给的失败信息
    }
 * 
 * @author qihang
 */
public class SdRespPojo {
    private String pkgSeq;
    
    private Integer code;
    
    private String msg;

    public String getPkgSeq() {
        return pkgSeq;
    }

    public void setPkgSeq(String pkgSeq) {
        this.pkgSeq = pkgSeq;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
