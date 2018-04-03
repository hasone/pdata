package com.cmcc.vrp.util;

import java.util.List;

/**
 * 短信模板类，用于判断模板是否正确以及替换模板内容
 *
 * @author qihang@chinamobile.com
 * @date 2015年1月30日
 * @tag
 */
public class SmsTemplateEl {


    /**
     * 检测所给表达式是否符合规则，符合时返回true，不符合时返回错误信息
     *
     * @param String
     * @return String
     * @author qihang@chinamobile.com
     * @date 2015年1月30日
     */
    public static String virifySmsTemplate(String content) {

        String result = "true";//正确时返回true
        int preValue = -1;//上一${与}中间的值
        int currentValue = -1;//当前${与}中间的值
        int left = 0;
        int right = 0;

        while (content != null) {
            left = content.indexOf("${");
            if (left >= 0) {//存在${
                right = content.indexOf("}"); //定位}
                if (right <= 0) {//${与}不配对
                    result = "'<span>$<span>{'和 '}' 数量不匹配";
                    return result;
                }
                //${与}配对，获取占位符中间的内容,${与}中间的数字必须从0开始依次递增
                String num = content.substring(left + 2, right);
                if (!virifyString(num)) {//中间数据不合法
                    result = "存在一组占位符 '<span>$<span>{' 和 '}' 中间内容不合法";
                    return result;
                } else {
                    currentValue = Integer.parseInt(num);
                }

                if (preValue == -1 || (currentValue > preValue && currentValue < 12)) {//正确的标签
                    preValue = currentValue;
                    content = content.substring(right + 1);
                } else {
                    result = "占位符'<span>$<span>{" + currentValue + "}'错误";
                    return result;
                }
            } else {
                break;
            }
        }

        return result;
    }

    public static boolean virifyString(String string) {
        return (string != null && !StringUtils.isEmpty(string) && StringUtils.isNumeric(string));
    }

    /**
     * 根据模板和给定内容返回套用模板的结果，成功返回短信内容，失败返回fail
     *
     * @param String
     * @param List<String>
     * @return String
     * @author qihang@chinamobile.com
     * @date 2015年1月30日
     */
    public static String fillingElExpression(String elExpression, List<String> list) {
        if (list == null || list.size() == 0) {
            return "fail";
        }
        int indexNum = getTemplateLength(elExpression);
        if (indexNum != list.size()) {//模板标签个数和list的长度不符合
            return "fail";
        }

        String result = elExpression;
//    	System.out.println("El:"+result);
        for (int i = 0; i < list.size(); i++) {
            String index = "${" + Integer.toString(i) + "}";//例如${0}
            if (result.indexOf(index) == -1) {//没有找到标签${i}
                return "fail";
            } else {
                result = result.replace(index, list.get(i));
            }
        }
        return result;
    }

    /**
     * 判断给定模板中标签的个数
     *
     * @param String
     * @return int
     * @author qihang@chinamobile.com
     * @date 2015年1月30日
     */
    public static int getTemplateLength(String elExpression) {
        if (elExpression == null || elExpression.trim().length() == 0) {
            return 0;
        }
        int total = 0;//标签总数
        int curIndex = 0;//现在的标签数
        while (true) {
            String index = "${" + Integer.toString(curIndex) + "}";//例如${0}
            if (elExpression.indexOf(index) == -1) {//没有找到标签
                break;
            } else {
                curIndex++;
            }
        }
        System.out.println(curIndex);
        return curIndex;
    }

    public static void main(String[] args) {
        String content = "${3}a ${3}a ";
        System.out.println(virifySmsTemplate(content));
    }
}
