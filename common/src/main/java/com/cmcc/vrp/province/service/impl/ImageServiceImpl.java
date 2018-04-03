package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.model.RandomCodeInfo;
import com.cmcc.vrp.province.service.ImageService;
import com.cmcc.vrp.util.CalcRandomCodeUtils;
import com.cmcc.vrp.util.VerifycodeUtil;
import com.github.cage.Cage;
import com.github.cage.IGenerator;
import com.github.cage.image.Painter;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 验证码图片生成服务
 * <p>
 * Created by sunyiwei on 2016/11/16.
 */
@Service
public class ImageServiceImpl implements ImageService {
    @Override
    public RandomCodeInfo createSimpleRandomCode(int height, int width) {
        String randomStr = VerifycodeUtil.getRandomString();
        BufferedImage bufferedImage = create(randomStr, height, width);

        return build(bufferedImage, randomStr);
    }

    @Override
    public RandomCodeInfo createCalcRandomCode(int height, int width) {
        CalcRandomCodeUtils calcRandomCodeUtils = CalcRandomCodeUtils.randomModel();
        String content = calcRandomCodeUtils.getExpression();
        String expectedValue = String.valueOf(calcRandomCodeUtils.getValue());

        BufferedImage bufferedImage = create(content, height, width);
        return build(bufferedImage, expectedValue);
    }

    //创建随机验证码对象
    private RandomCodeInfo build(BufferedImage bufferedImage, String expectedValue) {
        RandomCodeInfo randomCodeInfo = new RandomCodeInfo();
        randomCodeInfo.setBufferedImage(bufferedImage);
        randomCodeInfo.setExpectedValue(expectedValue);

        return randomCodeInfo;
    }

    private BufferedImage create(final String content, int height, int width) {
        Cage cage = new CustomCage(width, height, randomColor());
        return cage.drawImage(content);
    }

    private Color randomColor() {
        Random rnd = new Random();
        return new Color(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    private void addNoise(BufferedImage bi, Color color, int count) {
        final int WIDTH = bi.getWidth();
        final int HEIGHT = bi.getHeight();
        Random rnd = new Random();

        Graphics g = bi.getGraphics();
        for (int i = 0; i < count; i++) {
            g.setColor(color);
            g.drawLine(rnd.nextInt(WIDTH), rnd.nextInt(HEIGHT), rnd.nextInt(WIDTH), rnd.nextInt(HEIGHT));
        }
    }

    private class CustomPainter extends Painter {
        public CustomPainter(int width, int height) {
            super(width, height, null, Quality.MAX, null, null);
        }

        @Override
        public BufferedImage draw(Font font, Color fGround, String text) {
            BufferedImage bi = super.draw(font, fGround, text);
            addNoise(bi, fGround, 16);

            return bi;
        }
    }

    private class CustomCage extends Cage {
        public CustomCage(int width, int height, final Color color) {
            super(new CustomPainter(width, height), null, new IGenerator<Color>() {
                @Override
                public Color next() {
                    return color;
                }
            }, null, null, null, null);
        }
    }
}
