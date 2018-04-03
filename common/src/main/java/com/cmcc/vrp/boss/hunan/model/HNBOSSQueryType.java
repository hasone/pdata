package com.cmcc.vrp.boss.hunan.model;

/**
 * @ClassName: HNBOSSQueryType
 * @Description: 湖南省BOSS接口查询类型
 * @author: Rowe
 * @date: 2016年4月7日 下午3:45:02
 */
public enum HNBOSSQueryType {

    QUERY_GROUP_PRODUCT(0, "查询集团产品列表"),
    QUERY_GROUP_ACCOUNT(1, "查询集团产品余额");

    private Integer code;

    private String name;

    private HNBOSSQueryType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
