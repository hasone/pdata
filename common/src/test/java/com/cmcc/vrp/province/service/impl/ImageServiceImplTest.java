package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.service.ImageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * 图形验证码服务UT
 * <p>
 * Created by sunyiwei on 2016/11/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class ImageServiceImplTest {
    @InjectMocks
    ImageService imageService = new ImageServiceImpl();

    /**
     * 简单验证码
     *
     * @throws Exception
     */
    @Test
    public void testCreateSimpleRandomCode() throws Exception {
        imageService.createSimpleRandomCode(200, 400);
    }

    /**
     * 图形验证码
     *
     * @throws Exception
     */
    @Test
    public void testCreateCalcRandomCode() throws Exception {
        imageService.createCalcRandomCode(200, 400);
    }
}