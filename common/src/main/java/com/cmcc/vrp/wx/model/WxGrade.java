package com.cmcc.vrp.wx.model;

/**
 * WxGrade.java
 * @author wujiamin
 * @date 2017年3月2日
 */
public class WxGrade {
    private Long id;

    private String name;

    private Integer grade;

    private Long points;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }
}