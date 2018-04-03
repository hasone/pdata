package com.cmcc.vrp.province.model;

/**
 * 从营销模板获取运营数据
 * @author qinqinyan
 */
public class GetDataFromTemplateResp {

    private Long visitCount;

    private Long playCount;

    private Long winCount;

    private String msg;

    public Long getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(Long visitCount) {
        this.visitCount = visitCount;
    }

    public Long getPlayCount() {
        return playCount;
    }
    /**
     * 
     * */
    public void setPlayCount(Long playCount) {
        this.playCount = playCount;
    }
    /**
     * 
     * */
    public Long getWinCount() {
        return winCount;
    }
    /**
     * 
     * */
    public void setWinCount(Long winCount) {
        this.winCount = winCount;
    }
    /**
     * 
     * */
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
