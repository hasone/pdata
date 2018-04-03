package com.cmcc.vrp.province.model;

import com.alibaba.fastjson.JSON;

/**
 * 奖项
 */
public class AwardPojo {
    /*
     * 奖项名称
     */
    private String name;

    /*
     * 该奖项的中奖概率[0, 1)
     */
    private float possibility;

    /*
     * 该奖项对应的流量值大小，以M为单位
     */
    private int size;

    /*
     * 产品ID
     */
    private Long prdId;

    /*
     * 该奖项对应的角度
     */
    private int angle;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the possibility
     */
    public float getPossibility() {
        return possibility;
    }

    /**
     * @param possibility the possibility to set
     */
    public void setPossibility(float possibility) {
        this.possibility = possibility;
    }


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    /**
     * @Title: getPrdId
     * @return: Long
     */
    public Long getPrdId() {
        return prdId;
    }

    /**
     * @Title: setPrdId
     * @return: void
     */
    public void setPrdId(Long prdId) {
        this.prdId = prdId;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
