/**
 * @FileName: MatrixToImageWriter.java
 * @Package:com.cmcc.sunyiwei
 * @Description: QRCode生成时由矩阵数据生成图片
 * @author: sunyiwei
 * @date:2015��3��10�� ����10:02:56
 * @version V1.0
 */
package com.cmcc.vrp.util;

import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @ClassName: MatrixToImageWriter
 * @Description: RCode生成时由矩阵数据生成图片
 * @author: sunyiwei
 * @date: 2015年4月23日 下午5:11:35
 *
 */
public class MatrixToImageWriter {
    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

    private MatrixToImageWriter() {
    }

    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height,
            BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

    public static void writeToFile(BitMatrix matrix, String format, File file)
        throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, file)) {
            throw new IOException("Could not write an image of format "
                + format + " to " + file);
        }
    }

    public static void writeToStream(BitMatrix matrix, String format,
                                     OutputStream stream) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, stream)) {
            throw new IOException("Could not write an image of format "
                + format);
        }
    }
}
