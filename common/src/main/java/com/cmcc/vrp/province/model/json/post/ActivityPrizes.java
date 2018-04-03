/**
 * @Title: ActivityPrize.java
 * @Package com.cmcc.vrp.chongqing.model.json
 * @author: qihang
 * @date: 2015年11月19日 下午2:33:05
 * @version V1.0
 */
package com.cmcc.vrp.province.model.json.post;

/**
 *
 * @ClassName: ActivityPrizes
 * @Description: TODO
 * @author: Rowe
 * @date: 2016年3月30日 上午12:55:05
 */
public class ActivityPrizes {
    private Integer idPrefix; //前缀
    private String rankName;//几等奖
    private String cmccName; //奖品名称
    private String cmccEnterId;//enterpriseCode
    private String cmccId;   //产品代码productCode


    private Integer cmccCount;//移动奖品数量，默认填写1，主要面向流量池奖品
    private Integer cmccType;//奖品类型 0:流量池 | 1：流量包
    private Integer cmccResponse;//奖品兑换方式  0:稍后兑换 | 1：直接兑换

    private Long count;//奖品数量
    private Double probability;//概率

    public Integer getIdPrefix() {
        return idPrefix;
    }

    public void setIdPrefix(Integer idPrefix) {
        this.idPrefix = idPrefix;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public String getCmccName() {
        return cmccName;
    }

    public void setCmccName(String cmccName) {
        this.cmccName = cmccName;
    }

    public String getCmccEnterId() {
        return cmccEnterId;
    }

    public void setCmccEnterId(String cmccEnterId) {
        this.cmccEnterId = cmccEnterId;
    }

    public String getCmccId() {
        return cmccId;
    }

    public void setCmccId(String cmccId) {
        this.cmccId = cmccId;
    }

    public Integer getCmccCount() {
        return cmccCount;
    }

    public void setCmccCount(Integer cmccCount) {
        this.cmccCount = cmccCount;
    }

    public Integer getCmccType() {
        return cmccType;
    }

    public void setCmccType(Integer cmccType) {
        this.cmccType = cmccType;
    }

    public Integer getCmccResponse() {
        return cmccResponse;
    }

    public void setCmccResponse(Integer cmccResponse) {
        this.cmccResponse = cmccResponse;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Double getProbability() {
        return probability;
    }

    public void setProbability(Double probability) {
        this.probability = probability;
    }


}
