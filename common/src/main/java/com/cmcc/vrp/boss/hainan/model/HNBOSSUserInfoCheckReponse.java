package com.cmcc.vrp.boss.hainan.model;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 上午10:18:52
*/
public class HNBOSSUserInfoCheckReponse extends AbstractBossOperationResultImpl {
    private String code;//状态码

    private String desc;//状态描述

    private HNBOSSUserInfo userInfo;//用户资料校验包装类

    @Override
    public String getResultCode() {
        // TODO Auto-generated method stub
        return code;
    }

    @Override
    public boolean isSuccess() {
        if (StringUtils.isNotBlank(code) && UserStatus.NORMAL.getCode().equals(code)) {
            return true;
        }
        return false;
    }

    @Override
    public String getResultDesc() {
        // TODO Auto-generated method stub
        return desc;
    }

    @Override
    public HNBOSSUserInfo getOperationResult() {
        // TODO Auto-generated method stub
        return userInfo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public HNBOSSUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(HNBOSSUserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public boolean isNeedQuery() {
        return false;
    }

    public enum UserStatus {
        NORMAL("0", "正常"),
        NOT_HAINAN("1", "非海南移动号码"),
        NOT_EXSIT("2", "该号码用户不存在"),
        OUT_OF_SERVICE("3", "停机");

        private String code;
        private String desc;

        UserStatus() {
        }

        UserStatus(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * @return
         */
        public static Map<String, String> toMap() {
            Map<String, String> map = new HashMap<String, String>();
            for (UserStatus item : UserStatus.values()) {
                map.put(item.getCode(), item.getDesc());
            }
            return map;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

    }
}
