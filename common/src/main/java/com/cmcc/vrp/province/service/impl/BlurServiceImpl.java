package com.cmcc.vrp.province.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.service.BlurService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

/**
 * 
 * @ClassName: BlurServiceImpl 
 * @Description: TODO
 * @author: Rowe
 * @date: 2017年9月27日 上午9:46:24
 */
@Service("blurService")
public class BlurServiceImpl implements BlurService {

    @Autowired
    GlobalConfigService globalConfigService;

    //模糊显示手机号码：全部显示*
    private final static String RE_MOBILE = "(\\S)";

    //模糊显示企业名称：集团客户名称，保留第1位和最后2位，中间模糊化
    private final static String RE_ENT_NAME = "(?<=\\S{1})(\\S)(?=\\S{2})";

    //模糊显示企业编码：全部用8个*代替
    private final static String RE_ENT_CODE = "(\\S)";

    //模糊显示用户名称：两个字或三个字的至少1个字用*代替，大于三个字的至少2个字用*代替，保留姓 
    private final static String RE_USER_NAME = "(?<=\\S{1})(\\S)";

    /**
     * 
     * @Title: blurEntName 
     * @Description: 集团客户名称，保留第1位和最后2位，中间模糊化
     * @param entName
     * @return
     * @return: String
     */
    @Override
    public String blurEntName(String entName) {
        if (isBlurEntName() && StringUtils.isNotBlank(entName)) {
            return replaceAction(entName, RE_ENT_NAME);
        } else {
            return entName;
        }
    }

    /**
     * 
     * @Title: blurEntCode 
     * @Description: 全部用8个*代替
     * @param entCode
     * @return
     * @return: String
     */
    @Override
    public String blurEntCode(String entCode) {
        if (isBlurEntCode() && StringUtils.isNotBlank(entCode)) {
            return "********";
        } else {
            return entCode;
        }
    }

    /**
     * 
     * @Title: blurUserName 
     * @Description: 两个字或三个字的至少1个字用*代替，大于三个字的至少2个字用*代替，保留姓
     * @param userName
     * @return
     * @return: String
     */
    @Override
    public String blurUserName(String userName) {
        if (isBlurUserName() && StringUtils.isNotBlank(userName)) {
            return replaceAction(userName, RE_USER_NAME);
        } else {
            return userName;
        }
    }

    /**
     * 
     * @Title: blurMobile 
     * @Description: 全部用8个*代替
     * @param mobile
     * @return
     * @return: String
     */
    @Override
    public String blurMobile(String mobile) {
        if (isUserMobile() && StringUtils.isNotBlank(mobile)) {
            return "********";
        } else {
            return mobile;
        }
    }

    /**
     * 
     * @Title: blurEmail 
     * @Description: 邮箱：@前面的用3个*代替
     * @param email
     * @return
     * @return: String
     */
    @Override
    public String blurEmail(String email) {
        if (!isBlurUserEmail() || StringUtils.isBlank(email) || email.indexOf('@') <= 0) {
            return email;
        } else {
            return "***" + email.substring(email.indexOf('@'));
        }
    }

    private String replaceAction(String str, String regular) {
        return str.replaceAll(regular, "*");
    }

    private boolean isBlurEntName() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BLUR_ENT_NAME.getKey());
        if (StringUtils.isNotBlank(value) && "YES".equalsIgnoreCase(value)) {
            return true;
        }
        return false;//默认完整显示
    }

    private boolean isBlurEntCode() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BLUR_ENT_CODE.getKey());
        if (StringUtils.isNotBlank(value) && "YES".equalsIgnoreCase(value)) {
            return true;
        }
        return false;//默认完整显示
    }

    private boolean isBlurUserName() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BLUR_USER_NAME.getKey());
        if (StringUtils.isNotBlank(value) && "YES".equalsIgnoreCase(value)) {
            return true;
        }
        return false;//默认完整显示
    }

    private boolean isUserMobile() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BLUR_USER_MOBILE.getKey());
        if (StringUtils.isNotBlank(value) && "YES".equalsIgnoreCase(value)) {
            return true;
        }
        return false;//默认完整显示
    }

    private boolean isBlurUserEmail() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BLUR_USER_EMAIL.getKey());
        if (StringUtils.isNotBlank(value) && "YES".equalsIgnoreCase(value)) {
            return true;
        }
        return false;//默认完整显示
    }

}
