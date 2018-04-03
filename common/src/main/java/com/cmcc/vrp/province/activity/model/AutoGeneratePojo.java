package com.cmcc.vrp.province.activity.model;

import java.util.List;

/**
 * -------------------
 * 模板生成的参数
 * -------------------
 * plateName    平台名称
 * enterpriseId 企业编码
 * enterpriseName   企业名称
 * activeName   活动名称
 * activeId     活动编码
 * time         活动时间类
 * givedNumber  用户可中最大数量
 * 不填：无限
 * maxPlayNumber    用户最大可玩数量
 * 不填：无限
 * checkType    鉴权接口类型
 * 0：不做鉴权 | 1：白名单 | 2：黑名单
 * checkUrl     鉴权接口地址
 * prizes       奖品记录列表
 * fixedProbability 是否固定概率
 * 0：不固定概率 | 1：固定概率
 * <p>
 * picBuketName 图片bucket名称
 * picLocation  图片存放路径
 * <p>
 * description  活动描述
 * object       活动对象
 * rules        活动规则
 * status       活动状态
 * 0:未上架 | 1：上架
 * deleteFlag   软删除标志
 * <p>
 * chargeUrl    充值接口,主要面向省公司平台
 * <p>
 * ------面向自营平台核心模块------
 * appId        核心模块账户id
 * appSecret    核心模块账户秘钥
 * accountId    核心模块企业账户
 * 
 * --------------------
 * add by qiniqnyan on 2017/07/14
 * getUserFromWxFlag  从微信公众号获取用户信息标志位。设置为true，则直接从运营公众号获取用户mobile，不设置或者设置为其他，则对于游戏新用户，均输入手机号。使用该标志位必要条件：（1）微信相关活动；（2）相应公众号具备获取用户手机号接口。
 * getUserFromWxUrl  从微信获取用户手机号url。getUserFromWxFlag为true时，getUserFromWxUrl不能为空。
 * --------------------
 *
 * @Author liuzengzeng
 * @Date 16/3/22 下午3:08
 */
public class AutoGeneratePojo {

    private String plateName;
    private String enterpriseId;
    private String enterpriseName;
    private String activeName;
    private String activeId;
    private AutoTimePojo time;
    private Integer userType;
    private Integer gameType;
    private Integer givedNumber;
    private Integer maxPlayNumber;
    private Integer checkType;
    private String checkUrl;
    private List<AutoPrizesPojo> prizes;
    private int fixedProbability;
    private String picBuketName;
    private String picLocation;
    private String description;
    private String object;
    private String rules;
    private String appId;
    private String appSecret;
    private String accountId;
    private Integer status;
    private Integer deleteFlag;
    private String chargeUrl;
    private String daily;
    
    private String getUserFromWxFlag;
    
    private String getUserFromWxUrl;

    public String getDaily() {
        return daily;
    }

    public void setDaily(String daily) {
        this.daily = daily;
    }

    public String getChargeUrl() {
        return chargeUrl;
    }

    public void setChargeUrl(String chargeUrl) {
        this.chargeUrl = chargeUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
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

    public Integer getMaxPlayNumber() {
        return maxPlayNumber;
    }

    public void setMaxPlayNumber(Integer maxPlayNumber) {
        this.maxPlayNumber = maxPlayNumber;
    }

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

    public AutoTimePojo getTime() {
        return time;
    }

    public void setTime(AutoTimePojo time) {
        this.time = time;
    }

    public Integer getGivedNumber() {
        return givedNumber;
    }

    public void setGivedNumber(Integer givedNumber) {
        this.givedNumber = givedNumber;
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

    public List<AutoPrizesPojo> getPrizes() {
        return prizes;
    }

    public void setPrizes(List<AutoPrizesPojo> prizes) {
        this.prizes = prizes;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public int getFixedProbability() {
        return fixedProbability;
    }

    public void setFixedProbability(int fixedProbability) {
        this.fixedProbability = fixedProbability;
    }

    public String getPicBuketName() {
        return picBuketName;
    }

    public void setPicBuketName(String picBuketName) {
        this.picBuketName = picBuketName;
    }

    public String getPicLocation() {
        return picLocation;
    }

    public void setPicLocation(String picLocation) {
        this.picLocation = picLocation;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getGetUserFromWxFlag() {
        return getUserFromWxFlag;
    }

    public void setGetUserFromWxFlag(String getUserFromWxFlag) {
        this.getUserFromWxFlag = getUserFromWxFlag;
    }

    public String getGetUserFromWxUrl() {
        return getUserFromWxUrl;
    }

    public void setGetUserFromWxUrl(String getUserFromWxUrl) {
        this.getUserFromWxUrl = getUserFromWxUrl;
    }
    
}
