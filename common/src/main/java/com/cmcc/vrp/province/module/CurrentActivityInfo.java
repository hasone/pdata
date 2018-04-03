/**
 * 活动当前情况
 */
package com.cmcc.vrp.province.module;

/**
 * @author wujiamin
 * @date 2016年8月18日下午1:33:46
 */
public class CurrentActivityInfo {
    private Long money;
    private Long flow;
    private Integer count;
    private Integer userCount;//中奖用户
    private Integer joinCount;//参与人数（一般情况下中奖用户与参与人数一致，二维码有黑白名单，参与人数不一致）

    public CurrentActivityInfo() {

    }

    public CurrentActivityInfo(Long money, Long flow, Integer count, Integer userCount) {
        this.money = money;
        this.flow = flow;
        this.count = count;
        this.userCount = count;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public Long getFlow() {
        return flow;
    }

    public void setFlow(Long flow) {
        this.flow = flow;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public Integer getJoinCount() {
        return joinCount;
    }

    public void setJoinCount(Integer joinCount) {
        this.joinCount = joinCount;
    }
}
