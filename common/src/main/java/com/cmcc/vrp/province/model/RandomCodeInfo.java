package com.cmcc.vrp.province.model;

import java.awt.image.BufferedImage;

/**
 * 图形验证码信息
 *
 * Created by sunyiwei on 2016/11/16.
 */
public class RandomCodeInfo {
    //图形验证码对象
    private BufferedImage bufferedImage;

    //与图形验证码对应的期望答案
    private String expectedValue;

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public String getExpectedValue() {
        return expectedValue;
    }

    public void setExpectedValue(String expectedValue) {
        this.expectedValue = expectedValue;
    }
}
