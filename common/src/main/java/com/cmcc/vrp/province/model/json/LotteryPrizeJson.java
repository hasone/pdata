/**
 * @Title: ActivityPrize.java
 * @Package com.cmcc.vrp.chongqing.model.json
 * @author: qihang
 * @date: 2015年11月19日 下午2:33:05
 * @version V1.0
 */
package com.cmcc.vrp.province.model.json;

/**
 * @ClassName: ActivityPrize
 * @Description: TODO
 * @author: qihang
 * @date: 2015年11月19日 下午2:33:05
 *
 */
public class LotteryPrizeJson {
    private String idPrefix;

    private String rankName;

    private Long productId;

    private Double probability;

    private Long count;

    private Long enterpriseId;

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Double getProbability() {
        return probability;
    }

    public void setProbability(Double probability) {
        this.probability = probability;
    }


    public String getIdPrefix() {
        return idPrefix;
    }

    public void setIdPrefix(String idPrefix) {
        this.idPrefix = idPrefix;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }


    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }


}
