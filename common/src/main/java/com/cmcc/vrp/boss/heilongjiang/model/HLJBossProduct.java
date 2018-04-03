package com.cmcc.vrp.boss.heilongjiang.model;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
/**
 * 
 * @ClassName: HLJBossProduct 
 * @Description: 黑龙江BOSS接口查询到的企业产品信息
 * @author: Rowe
 * @date: 2017年8月28日 下午12:00:09
 */
public class HLJBossProduct {
    
    @JSONField(name="GOODS_UNIT")
    private String goodsUnit;//产品大小单位，单位M
    
    @JSONField(name="COUNT_NUM")
    private Long countNum;//可赠送数据量
    
    @JSONField(name="EFF_TIME", format="yyyy-MM-dd HH:mm:ss")
    private Date effTime;//使用生效时间 yyyy-MM-dd HH:mm:ss
    
    @JSONField(name="EXP_TIME", format="yyyy-MM-dd HH:mm:ss")
    private Date expTime;//使用失效时间 yyyy-MM-dd HH:mm:ss
    
    @JSONField(name="TOTAL_QUANTITY")
    private Long totalQuantity;//总库存剩余量,赠送规则设置时取这个值 
    
    @JSONField(name="GOODS_NAME")
    private String goodsName;//产品名称
    
    @JSONField(name="RETAIL_PRICE")
    private Long retailPrice;//零售单价，单位里
    
    @JSONField(name="GOODS_ID")
    private String goodsId;//商品标识
    
    @JSONField(name="GOODS_SIZE")
    private Long goodsSize;//产品大小
    
    @JSONField(name="EXP_TIME_RULE")
    private String expTimeRule;//失效时间规则（2000 日包 2001 月包 2002 半年包 2003 年包 1000自定义）
    
    @JSONField(name="COST_PRICE")
    private Long costPrice;//成本单价 单位 里

    public String getGoodsUnit() {
        return goodsUnit;
    }

    public void setGoodsUnit(String goodsUnit) {
        this.goodsUnit = goodsUnit;
    }

    public Long getCountNum() {
        return countNum;
    }

    public void setCountNum(Long countNum) {
        this.countNum = countNum;
    }

    public Date getEffTime() {
        return effTime;
    }

    public void setEffTime(Date effTime) {
        this.effTime = effTime;
    }

    public Date getExpTime() {
        return expTime;
    }

    public void setExpTime(Date expTime) {
        this.expTime = expTime;
    }

    public Long getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Long totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Long getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(Long retailPrice) {
        this.retailPrice = retailPrice;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public Long getGoodsSize() {
        return goodsSize;
    }

    public void setGoodsSize(Long goodsSize) {
        this.goodsSize = goodsSize;
    }

    public String getExpTimeRule() {
        return expTimeRule;
    }

    public void setExpTimeRule(String expTimeRule) {
        this.expTimeRule = expTimeRule;
    }

    public Long getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Long costPrice) {
        this.costPrice = costPrice;
    }

   
}
