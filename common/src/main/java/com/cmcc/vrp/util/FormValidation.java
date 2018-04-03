/**
 * /**
 *
 * @Title: FormValidation.java
 * @Package com.cmcc.flow.util
 * @author: sunyiwei
 * @date: 2015-3-4 下午3:35:29
 * @version V1.0
 */
package com.cmcc.vrp.util;

import com.cmcc.vrp.exception.SelfCheckException;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * @ClassName: FormValidation
 * @Description: 表单验证类，用于后端验证
 * @author: sunyiwei, qihang
 * @date: 2015-3-4 下午3:35:29
 *
 */
public class FormValidation {


    public final static FormValidation formValidation = new FormValidation();
    private static Map<String, String> patternMap = FormValidate.toPatternMap();
    private static Map<String, String> resultMap = FormValidate.toMessageMap();
    private static Map<String, Method> reflectMethodMap = FormValidation.toReflectMethodMap();

    static {
        resultMap.put(FormValidationType.IS_BLANK_REGEX, "不能为空");
        resultMap.put(FormValidationType.MIN_LENGTH_REGEX, "最小长度为");
        resultMap.put(FormValidationType.MAX_LENGTH_REGEX, "最大长度为");
        resultMap.put(FormValidationType.LENGTH_RANGE_REGEX, "长度范围是");
    }

    /**
     *
     * @Title:regex_validate
     * @Description: 将检验内容和规则列表中的每一项规则进行检验，判断是否符合规则，如果不符合抛出异常
     * @param ruleMap 规则列表，key是规则类型，value是检查的相关参数，可以为null
     * @param szContent 待校验的字符串
     * @return
     * @throws
     */
    public static boolean regex_validate(Map<String, List> ruleMap, String szContent) throws SelfCheckException {

        if (ruleMap.containsKey(FormValidationType.IS_BLANK_REGEX)) {//如果有非空规则检验则优先进行
            Method blankMethod = FormValidation.reflectMethodMap.get(FormValidationType.IS_BLANK_REGEX);
            if (blankMethod == null) {
                throw new SelfCheckException("no such method define");
            }

            String isBlankResult = runReflectMethod(blankMethod, szContent, null);
            if (isBlankResult != null) {
                throw new SelfCheckException(resultMap.get(FormValidationType.IS_BLANK_REGEX));
            }

            //从规则列表中删除判定空的检验(因为已经检查完成，)
            ruleMap.remove(FormValidationType.IS_BLANK_REGEX);
        }

        //开始遍历其余非空检验的规则
        Iterator iter = ruleMap.entrySet().iterator();
        while (iter.hasNext()) {//遍历ruleMap,找出所有规则进行遍历
            Map.Entry entry = (Map.Entry) iter.next();

            String ruleType = (String) entry.getKey();//得到检验类型
            List ruleParams = (List) entry.getValue();//得到检验参数

            if (patternMap.containsKey(ruleType)) { //类型是正则表达式的类型，则进行正则判断
                if (!regex_validate(patternMap.get(ruleType), szContent)) {
                    throw new SelfCheckException(resultMap.get(ruleType));
                }
            } else {//不是正则表达式，则调用相应方法
                Method m = FormValidation.reflectMethodMap.get(ruleType);
                if (m == null) {
                    throw new SelfCheckException("no such method define");
                }
                String lengthTestResult = runReflectMethod(m, szContent, ruleParams);
                if (lengthTestResult != null) {
                    throw new SelfCheckException(lengthTestResult);
                }
            }
        }
        return true;
    }

    /**
     *
     * @Title:toReflectMethodMap
     * @Description: 得到所有的长度类型检验的Method, 存入map中
     * @return Map<String,Method> key是方法名，value是指定方法
     * @throws
     */
    public static Map<String, Method> toReflectMethodMap() {

        Map<String, Method> lengthValidateMap = new HashMap<String, Method>();
        Class<FormValidation> formValidClass = FormValidation.class;
        try {
            lengthValidateMap.put("hasMinLength", formValidClass.getMethod("hasMinLength", new Class[]{String.class, List.class}));
            lengthValidateMap.put("hasMaxLength", formValidClass.getMethod("hasMaxLength", new Class[]{String.class, List.class}));
            lengthValidateMap.put("hasLengthRange", formValidClass.getMethod("hasLengthRange", new Class[]{String.class, List.class}));
            lengthValidateMap.put("isBlank", formValidClass.getMethod("isBlank", new Class[]{String.class, List.class}));
        } catch (SecurityException e) {
        } catch (NoSuchMethodException e) {
        }
        return lengthValidateMap;

    }

    /**
     *
     * @Title:regex_validate
     * @Description: 校验指定的表达式是否满足指定的正则表达式
     * @param szRegex 指定的正则表达式
     * @param szContent 待校验的字符串
     * @return
     * @throws
     */
    private static boolean regex_validate(String szRegex, String szContent) {
        return Pattern.compile(szRegex).matcher(szContent).matches();
    }

    /**
     *
     * @Title:runReflectMethod
     * @Description: 运行反射的方法,
     * @param method  得到的指定方法
     * @param szContent,listNum   都为方法的参数
     * @return
     * @throws
     */
    private static String runReflectMethod(Method method, String szContent, List listNum) {
        try {
            String result = (String) method.invoke(formValidation, new Object[]{szContent, listNum});
            return result;
        } catch (IllegalArgumentException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        } catch (InvocationTargetException e) {
            return null;
        }
    }

    public static void main(String[] args) throws SecurityException, NoSuchMethodException {
        //	Method m=FormValidation.class.getMethod("hasMinLength", new Class[]{String.class,List.class});

        //增加规则相关的参数，这里是最小长度和最大长度
        List listNum = new ArrayList();
        listNum.add(6);
        listNum.add(20);

        Map ruleMap = new HashMap();
        ruleMap.put(FormValidationType.IS_BLANK_REGEX, null);
        ruleMap.put(FormValidationType.NUMBER_REGEX, null);//增加检查是否是数字的规则
        ruleMap.put(FormValidationType.LENGTH_RANGE_REGEX, listNum);//增加长度验证的功能，需要有参数

        try {
            FormValidation.regex_validate(ruleMap, null);
            System.out.println("true");
        } catch (SelfCheckException e) {
            System.out.println(e.toString());
        }

    }

    /**
     *
     * @Title:isBlank
     * @Description: 校验字符串的最小长度是否为空，不是是空返回null,是空返回错误信息
     * @param szContent
     * @param nMinLength
     * @return
     * @throws
     */
    public String isBlank(String szContent, List listNum) {
        if (StringUtils.isBlank(szContent)) {
            return "不能为空";
        }
        return null;
    }

    /**
     *
     * @Title:hasMinLength
     * @Description: 校验字符串的最小长度是否为指定长度
     * @param szContent
     * @param nMinLength
     * @return
     * @throws
     */
    public String hasMinLength(String szContent, List listNum) {
        if (isBlank(szContent, listNum) != null) {//非空检测
            return "不能为空";
        }

        if (listNum == null || listNum.size() == 0) {
            return "最小长度检验至少需要一个参数";
        }

        int nMinLength = (Integer) listNum.get(0);
        if (szContent.length() >= nMinLength) {
            return null;
        } else {
            return "最小长度为" + nMinLength;
        }
    }

    /**
     *
     * @Title:hasMaxLength
     * @Description: 校验字符串的最大长度是否为指定长度
     * @param szContent
     * @param nMaxLength
     * @return
     * @throws
     */
    public String hasMaxLength(String szContent, List listNum) {
        if (isBlank(szContent, listNum) != null) {//非空检测
            return "不能为空";
        }

        if (listNum == null || listNum.size() == 0) {
            return "最大长度检验至少需要一个参数";
        }

        int nMaxLength = (Integer) listNum.get(0);
        if (szContent.length() <= nMaxLength) {
            return null;
        } else {
            return "最大长度为" + nMaxLength;
        }
    }

    /**
     *
     * @Title:hasMinLength
     * @Description: 校验字符串的最小长度是否为指定长度
     * @param szContent
     * @param nMinLength
     * @return
     * @throws
     */
    public String hasLengthRange(String szContent, List listNum) {
        if (isBlank(szContent, listNum) != null) {//非空检测
            return "不能为空";
        }

        if (listNum == null || listNum.size() < 2) {
            return "长度范围检验至少需要两个参数";
        }

        int nMinLength = (Integer) listNum.get(0);
        int nMaxLength = (Integer) listNum.get(1);

        String minLenValideResult = hasMinLength(szContent, listNum);
        listNum.remove(0);
        String maxLenValideResult = hasMaxLength(szContent, listNum);


        if (minLenValideResult == null && maxLenValideResult == null) {
            return null;
        } else {
            return "长度范围是" + nMinLength + "-" + nMaxLength;
        }
    }
}
