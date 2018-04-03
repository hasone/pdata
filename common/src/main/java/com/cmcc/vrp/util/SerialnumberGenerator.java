package com.cmcc.vrp.util;

import com.cmcc.vrp.enums.ProductCode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SerialnumberGenerator {

    /**
     * 批量生成卡序列号默认27位
     * <p>
     *
     * @param busiCode     业务编码,占5位
     * @param year         年份,占2位
     * @param provinceCode 省份编码,占2位
     * @param cityCode     地市编码,占2位
     * @param providerCode 制造商编码,占2位
     * @param batchNumber  批次号,占5位
     * @param count        生成数量，占9位
     * @return
     */
    public static List<String> generator(String busiCode, String year, String provinceCode, String cityCode,
                                         String providerCode, Integer batchNumber, Long count) {
        DecimalFormat df1 = new DecimalFormat("00000");//格式化生产卡的批号
        DecimalFormat df2 = new DecimalFormat("000000000");//格式化序列号
        List<String> resultData = new ArrayList<String>();
        if (busiCode == null || year == null || cityCode == null || providerCode == null || batchNumber == null || count == null) {
            return null;
        }
        if (busiCode.length() < 5) {
            busiCode = "10000";//设置默认值
        } else {
            busiCode = busiCode.substring(busiCode.length() - 5, busiCode.length());//业务编码占5位，截取最后5位
        }

        if (year.length() < 2) {
            year = "16";//设置为默认值
        } else {
            year = year.substring(year.length() - 2, year.length());//年份占2位，获取最后2位
        }

        if (provinceCode.length() < 2) {
            provinceCode = "50";//设置为默认值
        } else {
            provinceCode = provinceCode.substring(provinceCode.length() - 2, provinceCode.length());//省份编码占2位，获取最后2位
        }

        if (cityCode.length() < 2) {
            cityCode = "00";//设置为默认值
        } else {
            cityCode = cityCode.substring(cityCode.length() - 2, cityCode.length());//地市占2位，获取最后2位
        }

        if (providerCode.length() < 2) {
            providerCode = "00";//设置为默认值
        } else {
            providerCode = providerCode.substring(providerCode.length() - 2, providerCode.length());//制造商编码占2位，获取最后2位
        }

        for (int i = 1; i <= count; i++) {
            String temp = busiCode + year + provinceCode + cityCode + providerCode + df1.format(batchNumber) + df2.format(i);
            resultData.add(temp);
        }
        return resultData;
    }

    /**
     * @param type        卡类型，占1位：0代表非定制卡、1代表定制卡
     * @param level       套餐档次，占2位：00代表10M、01代表30M、02代表70M、03代表150M、04代表500M、05代表1G、06代表2G、07代表3G、08代表4G、09代表6G、10代表11G
     * @param year        年份，占2位：取后两位
     * @param batchNumber 批次号，占2位
     * @param count       生成数量
     * @return
     */
    public static List<String> generatorSerialNumber(int type, int level, String year, Integer batchNumber, Long count) {
        DecimalFormat df1 = new DecimalFormat("00");//格式套餐档次和生产卡的批号
        DecimalFormat df2 = new DecimalFormat("00000");//格式化序列号

        List<String> resultData = new ArrayList<String>();
        if (year == null || batchNumber == null || count == null) {
            return null;
        }
        if (type < 0 || type > 1) {
            type = 0;//设置为默认值
        }
        if (level < 0 || level > 10) {
            level = ProductCode.LEVEL_DEFAULT.getCode();//超出范围设置为默认值
        }
        if (year.length() < 2) {
            year = "15";//设置为默认值
        } else {
            year = year.substring(year.length() - 2, year.length());//年份占2位，获取最后2位
        }

        for (int i = 1; i <= count; i++) {
            String temp = type + df1.format(level) + year + df1.format(batchNumber) + df2.format(i);
            resultData.add(temp);
        }

        return resultData;
    }

    public static void main(String[] args) {
//		List<String> numbers = generator("1000000","2015", "50","11", "22", new Integer(3), new Long(100L));
        List<String> numbers = generatorSerialNumber(0, 123, "15", new Integer(3), 100L);
        for (String temp : numbers) {
            System.out.println(temp + ",length=" + temp.length());
        }
    }


}
