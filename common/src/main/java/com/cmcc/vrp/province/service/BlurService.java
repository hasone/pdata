package com.cmcc.vrp.province.service;

/**
 * 
 * @ClassName: BlurService 
 * @Description: TODO
 * @author: Rowe
 * @date: 2017年9月29日 下午12:11:32
 */
public interface BlurService {

    /**
     * 
     * @Title: blurEntName 
     * @Description: 企业名称模糊显示
     * @param entName
     * @return
     * @return: String
     */
    public String blurEntName(String entName);

    /**
     * 
     * @Title: blurEntCode 
     * @Description: 企业编码模糊显示
     * @param entCode
     * @return
     * @return: String
     */
    public String blurEntCode(String entCode);

    /**
     * 
     * @Title: blurUserName 
     * @Description: 用户名称模糊显示
     * @param userName
     * @return
     * @return: String
     */
    public String blurUserName(String userName);

    /**
     * 
     * @Title: blurMobile 
     * @Description: 手机号码模糊显示
     * @param mobile
     * @return
     * @return: String
     */
    public String blurMobile(String mobile);

    /**
     * 
     * @Title: blurEmail 
     * @Description: 邮箱模糊显示
     * @param email
     * @return
     * @return: String
     */
    public String blurEmail(String email);

}
