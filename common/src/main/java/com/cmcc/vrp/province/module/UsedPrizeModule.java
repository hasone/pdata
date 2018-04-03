package com.cmcc.vrp.province.module;

/**
 * @author JamieWu
 *         活动过程中已用流量包统计的模型
 */
public class UsedPrizeModule {
    private Long prdId;
    private String prdName;
    private String prdCode;
    private String rankName;
    private Long totalNum;//某奖品总数量
    private Long usedNum;//某奖品已用数量
    private Long remainedNum;//某奖品剩余数量

    public Long getPrdId() {
        return prdId;
    }

    public void setPrdId(Long prdId) {
        this.prdId = prdId;
    }

    public String getPrdName() {
        return prdName;
    }

    public void setPrdName(String prdName) {
        this.prdName = prdName;
    }

    public String getPrdCode() {
        return prdCode;
    }

    public void setPrdCode(String prdCode) {
        this.prdCode = prdCode;
    }

    public Long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Long totalNum) {
        this.totalNum = totalNum;
    }

    public Long getUsedNum() {
        return usedNum;
    }

    public void setUsedNum(Long usedNum) {
        this.usedNum = usedNum;
    }

    public Long getRemainedNum() {
        return remainedNum;
    }

    public void setRemainedNum(Long remainedNum) {
        this.remainedNum = remainedNum;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

}
