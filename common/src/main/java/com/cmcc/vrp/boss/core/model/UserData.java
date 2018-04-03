package com.cmcc.vrp.boss.core.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/8/17.
 */
@XStreamAlias("UserData")
public class UserData {
    @XStreamAlias("MobNum")
    private String mobNum;

    @XStreamAlias("UserPackage")
    private String aPackage;

    public String getMobNum() {
        return mobNum;
    }

    public void setMobNum(String mobNum) {
        this.mobNum = mobNum;
    }

    /**
     * 获取包
     * @return  包内容
     */
    public String getaPackage() {
        return aPackage;
    }

    /**
     * 设置包内容
     * @param aPackage 包内容
     */
    public void setaPackage(String aPackage) {
        this.aPackage = aPackage;
    }
}
