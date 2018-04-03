/**
 * @Title: LotteryPage.java
 * @Package com.cmcc.vrp.chongqing.model.json
 * @author: qihang
 * @date: 2015年11月19日 下午2:07:25
 * @version V1.0
 */
package com.cmcc.vrp.province.model.json.post;

import java.util.List;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午3:41:42
*/
public class LotteryBasic {

    private String plateName;//平台名称（新增）

    private String enterpriseId;//企业编码

    private String enterpriseName;//企业名称（新增）

    private String activeName;//活动名称

    private String activeId;//活动编码

    private LotteryTime time;//活动时间类

    private Long givedNumber;//每用户可中最大数量（新增），不填：无限

    private String daily;//活动类型(变更)：true:每天    false:活动期间

    private Long maxPlayNumber;//用户最大可玩数量（变更），不填：无限(现在主要面向不显示次数的活动，比如红包)

    private Integer checkType;//鉴权接口类型 0：不做鉴权 | 1：白名单 | 2：黑名单

    private String checkUrl;//鉴权接口地址

    private Integer status;//活动状态  0:未上架 | 1：上架

    private Integer deleteFlag;//软删除标志

    private String chargeUrl; //奖品记录接口,主要面向省公司平台 

    private Integer fixedProbability;//中奖概率：0：不固定概率 | 1：固定概率

    private Integer userType;//用户类型，默认1微信用户

    private Integer gameType;//游戏类型：默认1大转盘

    private String rules;//活动规则

    private List<ActivityPrizes> prizes;//奖项设置

    public String getPlateName() {
        return plateName;
    }

    public void setPlateName(String plateName) {
        this.plateName = plateName;
    }

    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getActiveName() {
        return activeName;
    }

    public void setActiveName(String activeName) {
        this.activeName = activeName;
    }

    public String getActiveId() {
        return activeId;
    }

    public void setActiveId(String activeId) {
        this.activeId = activeId;
    }

    public LotteryTime getTime() {
        return time;
    }

    public void setTime(LotteryTime time) {
        this.time = time;
    }

    public Long getGivedNumber() {
        return givedNumber;
    }

    public void setGivedNumber(Long givedNumber) {
        this.givedNumber = givedNumber;
    }

    public String getDaily() {
        return daily;
    }

    public void setDaily(String daily) {
        this.daily = daily;
    }

    public Long getMaxPlayNumber() {
        return maxPlayNumber;
    }

    public void setMaxPlayNumber(Long maxPlayNumber) {
        this.maxPlayNumber = maxPlayNumber;
    }

    public Integer getCheckType() {
        return checkType;
    }

    public void setCheckType(Integer checkType) {
        this.checkType = checkType;
    }

    public String getCheckUrl() {
        return checkUrl;
    }

    public void setCheckUrl(String checkUrl) {
        this.checkUrl = checkUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getChargeUrl() {
        return chargeUrl;
    }

    public void setChargeUrl(String chargeUrl) {
        this.chargeUrl = chargeUrl;
    }

    public Integer getFixedProbability() {
        return fixedProbability;
    }

    public void setFixedProbability(Integer fixedProbability) {
        this.fixedProbability = fixedProbability;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public List<ActivityPrizes> getPrizes() {
        return prizes;
    }

    public void setPrizes(List<ActivityPrizes> prizes) {
        this.prizes = prizes;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getGameType() {
        return gameType;
    }

    public void setGameType(Integer gameType) {
        this.gameType = gameType;
    }


}
