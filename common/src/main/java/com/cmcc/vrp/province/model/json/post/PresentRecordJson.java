package com.cmcc.vrp.province.model.json.post;

/**
 * 普通赠送前端传递json格式
 *
 * @author JamieWu
 */
public class PresentRecordJson {
    private String prdName;
    private Integer giveNum;
    private String phones;
    private Long prdId;
    private String size;
    private Integer effectType;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPrdName() {
        return prdName;
    }

    public void setPrdName(String prdName) {
        this.prdName = prdName;
    }

    public Integer getGiveNum() {
        return giveNum;
    }

    public void setGiveNum(Integer giveNum) {
        this.giveNum = giveNum;
    }

    public String getPhones() {
        return phones;
    }

    public void setPhones(String phones) {
        this.phones = phones;
    }

    public Long getPrdId() {
        return prdId;
    }

    public void setPrdId(Long prdId) {
        this.prdId = prdId;
    }

    public Integer getEffectType() {
        return effectType;
    }

    public void setEffectType(Integer effectType) {
        this.effectType = effectType;
    }
}
