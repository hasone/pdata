package com.cmcc.vrp.util;

/**
 * @ClassName: FormValidationType
 * @Description: 表单验证的所有类型
 * @author: qihang
 * @date: 2015-3-5
 */
public class FormValidationType {
    /**
     * 手机号码正则表达式
     */
    public static final String MOBILE_PHONE_REGEX = FormValidate.MOBILE_PHONE.getValidate_type();

    /**
     * 邮箱地址正则表达式
     */
    public static final String EMAIL_ADDRESS_REGEX = FormValidate.EMAIL_ADDRESS_REGEX.getValidate_type();

    /**
     * 汉字、英文字符、下划线及数字
     */
    public static final String CHINESE_LOWER_UPPER_UNDERLINE_NUMBER_REGEX = FormValidate.CHINESE_LOWER_UPPER_UNDERLINE_NUMBER_REGEX.getValidate_type();

    /**
     * 英文字符、下划线及数字
     */
    public static final String LOWER_UPPER_UNDERLINE_NUMBER_REGEX = FormValidate.LOWER_UPPER_UNDERLINE_NUMBER_REGEX.getValidate_type();

    /**
     * 数字
     */
    public static final String NUMBER_REGEX = FormValidate.NUMBER_REGEX.getValidate_type();

    /**
     * 非负整数
     */
    public static final String NON_NEGATIVE_REGEX = FormValidate.NON_NEGATIVE_REGEX.getValidate_type();

    /**
     * 最短长度验证
     */
    public static final String MIN_LENGTH_REGEX = "hasMinLength";
    /**
     * 最长长度验证
     */
    public static final String MAX_LENGTH_REGEX = "hasMaxLength";
    /**
     * 长度范围验证
     */
    public static final String LENGTH_RANGE_REGEX = "hasLengthRange";
    /**
     * 字符空验证
     */
    public static final String IS_BLANK_REGEX = "isBlank";
}
