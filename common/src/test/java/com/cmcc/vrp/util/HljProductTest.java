package com.cmcc.vrp.util;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

/**
 * 迁移黑龙江产品信息
 * <p>
 * Created by sunyiwei on 2016/6/24.
 */
public class HljProductTest {
    @Ignore
    @Test
    public void testHljProduct() throws Exception {
        final String fileName = "C:\\Users\\Lenovo\\Desktop\\heilongjiang_product.txt";
        final String outputFilename = "C:\\Users\\Lenovo\\Desktop\\product.sql";

        StringBuilder stringBuilder = new StringBuilder();

        String content = StreamUtils.copyToString(new FileInputStream(new File(fileName)), Charsets.UTF_8);
        Scanner scanner = new Scanner(content);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] comps = line.split("\\s+");

            stringBuilder.append(format(removeQuote(comps[2]), removeQuote(comps[4]), NumberUtils.toLong(removeQuote(comps[5]))));
            stringBuilder.append(System.lineSeparator());
        }

        FileUtils.write(new File(outputFilename), stringBuilder.toString(), Charsets.UTF_8);
    }

    private String format(String name, String code, long size) {
        return String.format("INSERT INTO `product` VALUES (null, '%s', '2', '%s', '1', NOW(), NOW(), '0', '%d', '0', 'M', '全国', '全国', '%d');",
            code, name, parsePrice(name) * 100, size * 1024);
    }

    private String removeQuote(String ori) {
        return ori.substring(1, ori.length() - 1);
    }

    private int parsePrice(String name) {
        int index = name.indexOf("元");
        return NumberUtils.toInt(name.substring(0, index));
    }
}
