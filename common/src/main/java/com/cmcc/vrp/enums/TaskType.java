package com.cmcc.vrp.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 任务类型枚举
 *
 * @author zhoujianbin
 */
public enum TaskType {

    RED_PACKET("R", "红包流量赠送任务类型", "redPacketTask"),
    BATCH_GIVE("B", "流量批量赠送任务类型", "batchTask"),
    MONTH_GIVE("M", "月流量赠送任务类型", "monthlyTask"),
    FLOWCARD("F", "流量卡充值业务类型", "flowCardTask"),
    GAME("G", "转盘游戏充值业务类型", "gameTask"),
    LOTTERY("L", "转盘游戏充值业务类型", "lotteryTask"),
    INDIVIDUAL_REDPACKET("IR", "个人红包", "individual_redpacket"),
    INDIVIDUAL_PRESENT("IP", "个人 赠送", "individual_present");

    private String code;

    private String message;

    /**
     * 任务名称，必须与Spring-bean.xml文件中<bean id="xxx">一致
     * 否则程序任务处理类无法获取正确的任务对象
     */
    private String taskName;

    TaskType(String code, String message, String taskName) {
        this.code = code;
        this.message = message;
        this.taskName = taskName;
    }

    /**
     * @return
     */
    public static Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        for (TaskType item : TaskType.values()) {
            map.put(item.getCode(), item.getMessage());
        }
        return map;
    }

    /**
     * @return
     */
    public static Map<String, String> taskNameMap() {
        Map<String, String> map = new HashMap<String, String>();
        for (TaskType item : TaskType.values()) {
            map.put(item.getCode(), item.getTaskName());
        }
        return map;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

}