package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.RandomCodeInfo;

/**
 * 验证码图片生成服务
 * <p>
 * Created by sunyiwei on 2016/11/16.
 */
public interface ImageService {
    /**
     * 生成指定大小的简单图形验证码
     *
     * @param height 图片高度
     * @param width  图片宽度
     * @return RandomCodeInfo 图形验证码信息对象
     */
    RandomCodeInfo createSimpleRandomCode(int height, int width);

    /**
     * 生成指定大小的算术图形验证码
     *
     * @param height 图片高度
     * @param width 图片宽度
     * @return 图形验证码信息对象
     */
    RandomCodeInfo createCalcRandomCode(int height, int width);
}
