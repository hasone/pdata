package com.cmcc.vrp.province.model.json;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午3:41:03
*/
public class RedPacketPrizeJson {
    private String rankName;
    private Long productId;
    private Long count;
    private Double probability;
    private Integer idPrefix;

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public Double getProbability() {
        return probability;
    }

    public void setProbability(Double probability) {
        this.probability = probability;
    }

    public Integer getIdPrefix() {
        return idPrefix;
    }

    public void setIdPrefix(Integer idPrefix) {
        this.idPrefix = idPrefix;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

}
