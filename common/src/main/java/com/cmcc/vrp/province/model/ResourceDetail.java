package com.cmcc.vrp.province.model;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午3:06:42
*/
public class ResourceDetail {
    private String name;

    private String modified;

    private String type;

    private String path;

    private String isImg;

    private String icon;
    private long space;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIsImg() {
        return isImg;
    }

    public void setIsImg(String isImg) {
        this.isImg = isImg;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSpace() {
        return space;
    }

    public void setSpace(long space) {
        this.space = space;
    }

}
