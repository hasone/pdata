package com.cmcc.vrp.province.model.json;

/**
 * 企业开户字段属性
 *
 * Created by sunyiwei on 2017/2/10.
 */
public class EntPropsInfo {
    private String name;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "EntPropsInfo{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
