package com.cmcc.vrp.util;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.Formatter;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sunyiwei on 2016/8/8.
 */
public class PhoneRegionUtilTest {
    @Ignore
    @Test
    public void testInsert() throws Exception {
        final String output = "C:\\Users\\Lenovo\\Desktop\\178.txt";
        final String url = "http://shouji.fkcha.com/list_";
        final String suffix = ".html";

        final int begin = 1780000;
        int end = 1790000;

        final Map<String, String> map = new ConcurrentHashMap<String, String>();

        final int count = 10;
        final int subCount = (end - begin) / count;
        final CountDownLatch countDownLatch = new CountDownLatch(count);

        ExecutorService executorService = Executors.newFixedThreadPool(count);
        for (int m = 0; m < count; m++) {
            final int M = m;

            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    for (int i = begin + M * subCount; i < begin + (M + 1) * subCount; i++) {
                        String URL = url + i + suffix;
                        String response = HttpUtils.get(URL);

                        final String search = "<li><span class=\"green\">归属地：</span>([\\u4e00-\\u9fa5]{0,})</li>";
                        Pattern pattern = Pattern.compile(search);
                        Matcher matcher = pattern.matcher(response);
                        if (matcher.find()) {
                            map.put(String.valueOf(i), matcher.group(1));
                        }
                    }

                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        executorService.shutdownNow();
        FileUtils.write(new File(output), toString(map), Charsets.UTF_8);
    }

    private String toString(Map<String, String> map) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : map.keySet()) {
            stringBuilder.append(key).append(" ").append(map.get(key)).append(System.lineSeparator());
        }

        return stringBuilder.toString();
    }

    @Ignore
    @Test
    public void testParse() throws Exception {
        final String inputFile = "C:\\Users\\Lenovo\\Desktop\\178_zy.txt";
        final String outputFile = "C:\\Users\\Lenovo\\Desktop\\178_zy.sql";

        String content = FileUtils.readFileToString(new File(inputFile));
        Scanner scanner = new Scanner(content);

        int lineNum = 0;
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            lineNum++;

            String lineContent = scanner.nextLine();

            String[] lineComponent = lineContent.split("\\s");
            if (lineComponent.length < 2) {
                System.err.println("无效的行内容. 行号为" + lineNum);
                continue;
            }

            String prefix = lineComponent[0];
            String province = lineComponent[1];
            String city = lineComponent.length == 3 ? lineComponent[2] : lineComponent[1];

            Formatter formatter = new Formatter();
            formatter.format("INSERT INTO `phone_region` (`number_prefix`, `province`, `city`, `type`, `create_time`, `update_time`, `delete_flag`) VALUES ('%s', '%s', '%s', '中国移动', NOW(), NOW(), '0');%n", prefix, province, city);
            stringBuilder.append(formatter.toString());
        }

        FileUtils.write(new File(outputFile), stringBuilder.toString(), Charsets.UTF_8);
    }
}
