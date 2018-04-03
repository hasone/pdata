package com.cmcc.vrp.util;

import java.util.Map;

/**
 * 拼接前端统计图JS
 *
 * @ClassName: StatisticalChartUtils
 * @Description: TODO
 * @author: hexinxu
 * @date: 2015年6月18日 上午9:52:15
 */
public class StatisticalChartUtils {

    public static String pie(Map<String, String> map) {

        String pieStr = "option = {" + "title : {" + "text: '" + map.get("title") + "',"
            + "x:'center'" + "}," + "tooltip : {" + "trigger: 'item',"
            + "formatter: '{a} <br/>{b} : {c} ({d}%)'" + "}," + "legend: {"
            + "orient : 'vertical'," + "x : 'left'," + map.get("nameData") + "},"
            + "toolbox: {" + "show : true," + "feature : {"
            + "restore : {show: true}," + "saveAsImage : {show: true}"
            + "}" + "}," + "calculable : false," + "series : [" + "{"
            + "name:'统计数据'," + "type:'pie'," + "radius : '55%',"
            + "center: ['50%', '60%']," + map.get("valueData") + "} ] }";

        return pieStr;
    }

    public static String line(Map<String, String> map) {

        String lineStr = "option = { title : {"
            + "text: '" + map.get("title") + "'\r\n"
            + "}, tooltip : { \r\n"
            + "trigger: 'axis'\r\n"
            + "},\r\n"
            + "legend: {\r\n"
            + "data:[" + map.get("nameData")
            + " \r\n]},\r\n"
            + "toolbox: {\r\n"
            + "show : true,\r\n"
            + "feature : {\r\n"
            + "restore : {show: true},\r\n"
            + "saveAsImage : {show: true}\r\n"
            + "}\r\n"
            + "},\r\n"
            + "calculable : true,\r\n"
            + "xAxis : [\r\n"
            + "{\r\n"
            + "type : 'category',\r\n"
            + "boundaryGap : false,\r\n"
            + " data : " + map.get("xAxis")
            + "}\r\n"
            + "],\r\n"
            + "yAxis : [\r\n"
            + "{\r\n"
            + "type : 'value'\r\n"
            + "}\r\n"
            + "],\r\n"
            + "series : [ " + seriesStr(map)
            + "] }";

        return lineStr;
    }

    public static String seriesStr(Map<String, String> map) {
        StringBuffer buffer = new StringBuffer();

        String[] names = map.get("nameData").toString().split(",");

        for (String s : names) {

            buffer.append("{\r\n"
                + "name:" + s + ",\r\n"
                + "type:'line',\r\n"
                + "data:" + map.get(s) + ",\r\n"
                + "markLine : {\r\n"
                + "data : [\r\n"
                + "{type : 'average', name: '平均值'}\r\n"
                + "]\r\n"
                + "}\r\n"
                + "},\r\n");

        }

        return buffer.toString().substring(0, buffer.length() - 1);
    }
}
