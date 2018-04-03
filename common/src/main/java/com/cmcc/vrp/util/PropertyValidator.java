package com.cmcc.vrp.util;

import com.cmcc.vrp.exception.SelfCheckException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertyValidator {
    public static final String MOBILE_PHONE = "^1[3-8][0-9]{9}$";

    public static final String EMAIL_ADDRESS_REGEX = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";

    public static final String CHINESE_LOWER_UPPER_UNDERLINE_NUMBER_REGEX = "^[\u4E00-\u9FA5A-Za-z0-9_()（）]+$";

    public static final String LOWER_UPPER_UNDERLINE_NUMBER_REGEX = "^[A-Za-z0-9_]+$";

    public static final String NUMBER_REGEX = "^\\d+$";

    public static final String NON_NEGATIVE_REGEX = "^[1-9]\\d*|0$";

    public static final String POSITIVE_REGEX = "^[1-9]+\\d*$";

    public static final String STRICT_PWD = "^(?![a-zA-Z0-9]+$)(?![^a-zA-Z/D]+$)(?![^0-9/D]+$).{10,20}$";

    public static final String CHINESE_ONLY_REGEX = "[\u4e00-\u9fa5]";

    /**
     * @param szContent     检测属性内容
     * @param szContentName 检测属性对应页面上的名称
     *                      如果是空抛出异常SelfCheckException，包含提示信息
     * @return boolean
     * @throws SelfCheckException
     * @Title:isBlank
     * @Description: 检测字符串是否为空
     * @author: qihang
     */
    public static boolean isBlank(String szContent, String szContentName) throws SelfCheckException {
        if (StringUtils.isEmpty(szContent)) {
            throw new SelfCheckException(szContentName + "不能为空!");
        }
        return true;
    }

    /**
     * @param phoneNum      检验电话号码
     * @param szContentName 检测属性对应页面上的名称
     *                      已包含非空检测，不符合手机号码格式抛出异常SelfCheckException，包含提示信息
     * @return boolean
     * @throws SelfCheckException
     * @Title:isPhoneNum
     * @Description: 检测手机号码是否合法
     * @author: qihang
     */
    public static boolean isPhoneNum(String phoneNum, String szContentName) throws SelfCheckException {
        isBlank(phoneNum, szContentName);
        if (!Pattern.compile(PropertyValidator.MOBILE_PHONE).matcher(phoneNum).matches()) {
            throw new SelfCheckException(szContentName + "不是有效的手机号码!");
        }
        return true;
    }


    public static boolean isPhoneNumNoException(String phoneNum) {
        if (org.apache.commons.lang.StringUtils.isEmpty(phoneNum)) {
            return false;
        }
        if (!Pattern.compile(PropertyValidator.MOBILE_PHONE).matcher(phoneNum).matches()) {
            return false;
        }
        return true;
    }

    /**
     * @param email         检验邮箱
     * @param szContentName 检测属性对应页面上的名称
     *                      已包含非空检测，不符合邮箱格式抛出异常SelfCheckException，包含提示信息
     * @return boolean
     * @throws SelfCheckException
     * @Title:isEmail
     * @Description: 检测邮箱是否合法
     * @author: qihang
     */
    public static boolean isEmail(String email, String szContentName) throws SelfCheckException {
        isBlank(email, szContentName);
        if (!Pattern.compile(PropertyValidator.EMAIL_ADDRESS_REGEX).matcher(email).matches()) {
            throw new SelfCheckException(szContentName + "不是有效的email地址!");
        }
        return true;
    }

    /**
     * @param szContent     检测属性内容
     * @param szContentName 检测属性对应页面上的名称
     *                      已包含非空检测，不符合格式抛出异常SelfCheckException，包含提示信息
     * @return boolean
     * @throws SelfCheckException
     * @Title:isChineseLowerNumberUnderline
     * @Description: 检测字符串由汉字、英文字符、下划线或数字组成
     * @author: qihang
     */
    public static boolean isChineseLowerNumberUnderline(String szContent, String szContentName)
        throws SelfCheckException {
        isBlank(szContent, szContentName);
        if (!Pattern.compile(PropertyValidator.CHINESE_LOWER_UPPER_UNDERLINE_NUMBER_REGEX)
            .matcher(szContent).matches()) {
            throw new SelfCheckException(szContentName + "只能由中英文字符、数字、下划线或中英文括号组成!");
        }
        return true;
    }

    /**
     * @param szContent     检测属性内容
     * @param szContentName 检测属性对应页面上的名称
     *                      已包含非空检测，不符合格式抛出异常SelfCheckException，包含提示信息
     * @return boolean
     * @throws SelfCheckException
     * @Title:isLowerNumberUnderline
     * @Description: 检测字符串由字母、数字或下划线组成
     * @author: qihang
     */
    public static boolean isLowerNumberUnderline(String szContent, String szContentName)
        throws SelfCheckException {
        isBlank(szContent, szContentName);
        if (!Pattern.compile(PropertyValidator.LOWER_UPPER_UNDERLINE_NUMBER_REGEX)
            .matcher(szContent).matches()) {
            throw new SelfCheckException(szContentName + "只能字母、数字或下划线组成!");
        }
        return true;
    }

    /**
     * @param szContent     检测属性内容
     * @param szContentName 检测属性对应页面上的名称
     *                      已包含非空检测，不符合格式抛出异常SelfCheckException，包含提示信息
     * @return boolean
     * @throws SelfCheckException
     * @Title:isNumber
     * @Description: 检测字符串由数字组成
     * @author: qihang
     */
    public static boolean isNumber(String szContent, String szContentName)
        throws SelfCheckException {
        isBlank(szContent, szContentName);
        if (!Pattern.compile(PropertyValidator.NUMBER_REGEX)
            .matcher(szContent).matches()) {
            throw new SelfCheckException(szContentName + "只能由数字组成!");
        }
        return true;
    }

    /**
     * @param szContent     检测属性内容
     * @param szContentName 检测属性对应页面上的名称
     *                      已包含非空检测，不符合格式抛出异常SelfCheckException，包含提示信息
     * @return boolean
     * @throws SelfCheckException
     * @Title:isNumberNonNegative
     * @Description: 检测字符串由非负数字组成
     * @author: qihang
     */
    public static boolean isNumberNonNegative(String szContent, String szContentName)
        throws SelfCheckException {
        isBlank(szContent, szContentName);
        if (!Pattern.compile(PropertyValidator.NON_NEGATIVE_REGEX)
            .matcher(szContent).matches()) {
            throw new SelfCheckException(szContentName + "只能是非负正数！");
        }
        return true;
    }

    public static boolean isNumberPositive(String szContent, String szContentName)
        throws SelfCheckException {
        isBlank(szContent, szContentName);
        if (!Pattern.compile(PropertyValidator.POSITIVE_REGEX)
            .matcher(szContent).matches()) {
            throw new SelfCheckException(szContentName + "只能是正数！");
        }
        return true;
    }

    public static boolean isStrictPwd(String szContent, String szContentName)
        throws SelfCheckException {
        isBlank(szContent, szContentName);
        if (!Pattern.compile(PropertyValidator.STRICT_PWD)
            .matcher(szContent).matches()) {
            throw new SelfCheckException(szContentName + "必须包含字母、数字、特殊符号且长度为10到20位!");
        }
        return true;
    }

    /**
     * @param szContent     检测属性内容
     * @param szContentName 检测属性对应页面上的名称
     * @param nMinLength    已包含非空检测，不符合格式抛出异常SelfCheckException，包含提示信息
     * @return boolean
     * @throws SelfCheckException
     * @Title:minLengthCheck
     * @Description: 查看字符串的最小个数限制
     * @author: qihang
     */
    public static boolean minLengthCheck(String szContent, int nMinLength, String szContentName)
        throws SelfCheckException {
        isBlank(szContent, szContentName);
        if (szContent.length() < nMinLength) {
            throw new SelfCheckException(szContentName + "最小长度为" + nMinLength);
        }
        return true;
    }

    /**
     * @param szContent     检测属性内容
     * @param szContentName 检测属性对应页面上的名称
     * @return boolean
     * @throws SelfCheckException
     * @Title:maxLengthCheck
     * @Description: 查看字符串的最大个数限制
     * @author: qihang
     */
    public static boolean maxLengthCheck(String szContent, int nMaxLength, String szContentName)
        throws SelfCheckException {
        isBlank(szContent, szContentName);
        if (szContent.length() > nMaxLength) {
            throw new SelfCheckException(szContentName + "最大长度为" + nMaxLength);
        }
        return true;
    }

    /**
     * @param szContent     检测属性内容
     * @param szContentName 检测属性对应页面上的名称
     * @param nMinLength    已包含非空检测，不符合格式抛出异常SelfCheckException，包含提示信息
     * @return boolean
     * @throws SelfCheckException
     * @Title:maxLengthCheck
     * @Description: 查看字符串的字数范围限制
     * @author: qihang
     */
    public static boolean rangeLengthCheck(String szContent, int nMinLength, int nMaxLength, String szContentName)
        throws SelfCheckException {
        isBlank(szContent, szContentName);
        if (szContent.length() > nMaxLength || szContent.length() < nMinLength) {
            throw new SelfCheckException(szContentName + "长度范围为" + nMinLength + "-" + nMaxLength);
        }
        return true;
    }

    /**
     * 检查字符串中不能包含有中文
     *
     * @param szContent
     * @param szContentName
     * @return
     * @throws SelfCheckException
     */
    public static boolean isNoContainChinese(String szContent, String szContentName) throws SelfCheckException {
        isBlank(szContent, szContentName);
        Pattern p = Pattern.compile(PropertyValidator.CHINESE_ONLY_REGEX);
        Matcher m = p.matcher(szContent);
        if (m.find()) {
            throw new SelfCheckException(szContentName + "含有中文");
        }
        return true;
    }

    public static void main(String[] args) {
        try {
            boolean flag = PropertyValidator.isChineseLowerNumberUnderline("！", "产品编码");
            System.out.println(flag);
        } catch (SelfCheckException e) {
            e.printStackTrace();
        }
    }

}
