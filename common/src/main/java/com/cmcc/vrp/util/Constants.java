
package com.cmcc.vrp.util;

/**
 * @ClassName: Constants
 * @Description: 数据库中的状态及标识常量
 * @author: sunyiwei
 * @date: 2015年3月17日 下午1:40:19
 */
public class Constants {
    /**
     * 即将失效的天数
     */
    public static final int expireDays = 7;

    /**
     * 产品是否可以配置数量
     */
    public static enum CONFIGURABLE_NUM {
        YES(0),
        NO(1);
        private int value;

        CONFIGURABLE_NUM(int value) {
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
    
    /**
     * 是否使用产品模板：1:使用产品模板，0：不使用产品模板
     * */
    public static enum USE_PRODUCT_TEMPLATE {
        NOT_USED(0),
        USED(1);
        private int value;

        private USE_PRODUCT_TEMPLATE(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    
    /**
     * 产品模板与企业是否默认关联
     * 0：不默认关联；1默认关联
     * 主要是在企业开户时用到
     * @author qinqinyan
     * */
    public static enum PRODUCT_TEMPLATE_ENTERPRISE_MAP {
        NOT_DEFAULT(0), DEFAULT(1);
        private int value;

        private PRODUCT_TEMPLATE_ENTERPRISE_MAP(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 删除标记： 0:未删除；1：已删除
     */
    public static enum DELETE_FLAG {
        UNDELETED(0), DELETED(1);

        private int value;

        private DELETE_FLAG(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 企业红包分发模式： 0代表随机，1代表平均
     */
    public static enum ENTREDPACKET_MODE {
        RANDOM(0), AVERAGE(1);

        private int value;

        private ENTREDPACKET_MODE(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 上下架状态， 0为下架, 1为上架, 2为全部
     */
    public static enum ENTREDPACKET_STATUS {
        OFF(0), ON(1), PROCESSING(2), END(3);

        private int value;

        private ENTREDPACKET_STATUS(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 红包记录类型 0: redpacket_id指向企业流量红包表; 1: redpacket_id指向用户流量红包表
     */
    public static enum REDPACKET_RECORD_TYPE {
        USER(0), ENT(1);

        private int value;

        private REDPACKET_RECORD_TYPE(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 角色状态，0:启用；1：禁用
     */
    public static enum ROLE_STATUS {
        ON(0), OFF(1);

        private int value;

        private ROLE_STATUS(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 角色是否可删除标识： 0不可删除 1可删除
     */
    public static enum ROLE_CAN_BE_DELETED {
        NO(0), YES(1);

        private int value;

        private ROLE_CAN_BE_DELETED(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 用户帐户记录表操作类型： 0代表收入， 1代表支出
     */
    public static enum USER_BALANCE_RECORD {
        GAIN(0), GIVE(1);

        private int value;

        private USER_BALANCE_RECORD(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * @ClassName: SOURCE_TYPE_ID
     * @Description: 红包流量来源ID， 0代表抢红包， 1代表发红包， 2代表流量充值， 3代表流量兑换， 4代表受赠送，
     * 5代表赚流量，6代表其它
     * @author: sunyiwei
     * @date: 2015年3月19日 上午9:39:11
     */
    public static enum SOURCE_TYPE_ID {
        GRAB("抢红包"), GIVE("赠流量"), CHARGE("充值"), EXCHANGE("兑流量"), PRESENT("受赠送"), GAIN(
                "赚流量"), OTHER("其它");

        private String name;

        private SOURCE_TYPE_ID(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }


    /**
     * @ClassName: BALANCE_OUTGO_SOURCETYPE
     * @Description: 流量支出类型
     * @author: qihang
     * @date: 2015年3月27日 上午11:12:11
     */
    public static enum BALANCE_OUTGO_SOURCETYPE {
        FLOW_EXPIRED("过期失效"), EXCHANGE("流量兑换");

        private String name;

        private BALANCE_OUTGO_SOURCETYPE(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }

    /**
     * @ClassName: PRODUCT_STATUS
     * @Description: 产品上下架状态
     * @author: sunyiwei
     * @date: 2015年4月27日 上午9:35:13
     */
    public static enum PRODUCT_STATUS {
        ON(1),
        OFF(0);

        private int status;

        private PRODUCT_STATUS(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }
    }

    public static enum COUPON_TYPE {
        transfer(1),
        charge(0);

        private int type;

        private COUPON_TYPE(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    /**
     * 对账类型：0按天对账；1按月对账；2自定义时间范围
     */
    public static enum CHECK_ACCOUNT_TYPE {
        CHECK_By_DAY(0),
        CHECK_By_MONTH(1),
        CHECK_By_DEFINITION_TIME_RANGE(2);

        private int type;

        private CHECK_ACCOUNT_TYPE(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    /**
     * 对账结果：成功；失败
     */
    public static enum CHECK_ACCOUNT_RESULT {
        CHECK_SUCCESS(0),
        CHECK_FAIL(1);

        private int result;

        private CHECK_ACCOUNT_RESULT(int result) {
            this.result = result;
        }

        public int getResult() {
            return result;
        }
    }

    /**
     * 生成大转盘结果
     */
    public static enum CREATE_LOTTERY_RESULTE {
        CREATE_SUCCESS("200");

        private String result;

        private CREATE_LOTTERY_RESULTE(String result) {
            this.result = result;
        }

        public String getResult() {
            return result;
        }
    }
    
    /**
     * 激活状态
     * 0，未激活；1，已激活（即已经处理该请求）
     * */
    public static enum MDRC_ACTIVE_STATUS {
        NOT_ACTIVE("0"),
        ACTIVE("1");
        
        private String result;

        private MDRC_ACTIVE_STATUS(String result) {
            this.result = result;
        }

        public String getResult() {
            return result;
        }
    }
    
    /**
     * 制卡状态
     * 1、已制卡；0、未制卡,2:准备卡数据以提供下载
     * */
    public static enum MAKE_CARD_STATUS {
        NOT_MAKE_CARD("0"),
        MAKE_CARD("1"),
        GENERATE_FILES("2");
        
        private String result;

        private MAKE_CARD_STATUS(String result) {
            this.result = result;
        }

        public String getResult() {
            return result;
        }
    }
    
    /**
     * 审核记录是否被处理
     * 0:已经处理；1未被处理
     * */
    public static enum APPROVAL_RECORD_IS_NEW {

        IS_NEW("1"),
        IS_NOT_NEW("0");

        private String result;

        private APPROVAL_RECORD_IS_NEW(String result) {
            this.result = result;
        }

        public String getResult() {
            return result;
        }
    }
    

    /**
     * 审核结果
     * 1:通过；0:驳回
     */
    public static enum APPROVAL_RESULT {

        PASS("1"),
        REJECT("0");

        private String result;

        private APPROVAL_RESULT(String result) {
            this.result = result;
        }

        public String getResult() {
            return result;
        }
    }

    /**
     * 营销活动配置缓存key值
     * @author qinqinyan
     * @date 2017/08/31
     * */
    public static enum ACTIVITY_CONFIG_KEY{
        PROVINCE("province"),
        ISP("isp");

        private String result;

        private ACTIVITY_CONFIG_KEY(String result) {
            this.result = result;
        }

        public String getResult() {
            return result;
        }
    }

    /**
     * 1:虚拟产品；2：真实产品 
     */
    public static enum FLOW_ACCOUNT_FLAG {
        VIRTUAL_PRODUCT(1, "虚拟产品"),
        REAL_PRODUCT(2, "真实产品");
        private Integer code;
        private String msg;

        private FLOW_ACCOUNT_FLAG(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
    
    /**
     * minusCount类型  0,资金  1,流量
     */
    public static enum MINUS_ACCOUNT_TYPE {
        CURRENCY(0, "资金"),
        FLOW(1, "流量"),
        OTHER(9, "其它"),;
        private Integer code;
        private String msg;

        private MINUS_ACCOUNT_TYPE(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
