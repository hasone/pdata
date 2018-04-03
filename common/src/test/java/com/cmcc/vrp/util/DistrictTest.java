package com.cmcc.vrp.util;

import com.cmcc.vrp.province.model.District;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Formatter;
import java.util.Scanner;

/**
 * 用于自动生成地区信息
 * <p>
 * Created by sunyiwei on 2016/6/24.
 */
public class DistrictTest {
    private int id = 0;

    @Test
    @Ignore
    public void testDistrict() throws Exception {
        final String districtFilename = "C:\\Users\\Lenovo\\Desktop\\district.txt";
        final String outputFilename = "C:\\Users\\Lenovo\\Desktop\\district.sql";

        //append root
        StringBuilder stringBuilder = new StringBuilder();
        appendRoot(stringBuilder, "黑龙江");

        String content = StreamUtils.copyToString(new FileInputStream(new File(districtFilename)), Charsets.UTF_8);
        Scanner scanner = new Scanner(content);
        while (scanner.hasNext()) {
            String lineContent = scanner.nextLine();

            //append city first
            Scanner cityScanner = new Scanner(lineContent);
            int cityId = appendCity(stringBuilder, cityScanner.next());
            while (cityScanner.hasNext()) {
                appendRegion(stringBuilder, cityScanner.next(), cityId);
            }
        }

        FileUtils.write(new File(outputFilename), stringBuilder.toString(), Charsets.UTF_8);
    }

    @Ignore
    @Test
    public void testGenericDistrict() throws Exception {
        final String districtFilename = "C:\\Users\\Lenovo\\Desktop\\district.txt";
        final String outputFilename = "C:\\Users\\Lenovo\\Desktop\\district.sql";

        String content = StreamUtils.copyToString(new FileInputStream(new File(districtFilename)), Charsets.UTF_8);
        Scanner scanner = new Scanner(content);
        StringBuilder stringBuilder = new StringBuilder();

        while (scanner.hasNext()) {
            String lineContent = scanner.nextLine();
            stringBuilder.append(toSqlString(build(lineContent.split("\\s"))));
            stringBuilder.append(System.lineSeparator());
        }

        FileUtils.write(new File(outputFilename), stringBuilder.toString(), Charsets.UTF_8);
    }

    private String toSqlString(District district) {
        StringBuilder stringBuilder = new StringBuilder();
        Formatter formatter = new Formatter(stringBuilder);

        formatter.format("insert into district values(%d, '%s', %d, %d, NULL);", district.getId(), district.getName(), district.getLevel(), district.getParentId());
        return stringBuilder.toString();
    }

    private District build(String[] comps) {
        District district = new District();

        district.setId(NumberUtils.toLong(comps[0]));
        district.setName(comps[1]);
        district.setLevel(NumberUtils.toInt(comps[2]));
        district.setParentId(NumberUtils.toLong(comps[3]));

        return district;
    }

    private int appendRoot(StringBuilder stringBuilder, String rootName) {
        return append(stringBuilder, rootName, "1", 0);
    }

    private int appendCity(StringBuilder stringBuilder, String cityName) {
        return append(stringBuilder, cityName, "2", 1);
    }

    private int appendRegion(StringBuilder stringBuilder, String regionName, int parentId) {
        return append(stringBuilder, regionName, "3", parentId);
    }

    private int append(StringBuilder stringBuilder, String name, String level, int parentId) {
        stringBuilder.append(String.format("INSERT INTO `district` VALUES (%d, '%s', '%s', '%d', null);", ++id, name, level, parentId));
        stringBuilder.append(System.lineSeparator());

        return id;
    }
}
