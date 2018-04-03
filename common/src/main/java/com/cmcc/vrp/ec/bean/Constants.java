package com.cmcc.vrp.ec.bean;

import java.util.LinkedList;
import java.util.List;

import com.cmcc.vrp.util.Constants.MINUS_ACCOUNT_TYPE;


/**
 * Created by leelyn on 2016/5/18.
 */
public class Constants {

    public static final String ISSUER = "flow-platform";
    public static final String APP_KEY_ATTR = "App-Key";
    public static final String SYSTEM_NUM_ATTR = "System-Num";
    public static final String BODY_XML_ATTR = "Body-Xml";
    public static final String TOKEN_HEADER = "4GGOGO-Auth-Token";
    public static final String SIGNATURE_HEADER = "HTTP-X-4GGOGO-Signature";
    public static final String FINGERPRINT = "fingerprint";

    //删除标识
    public static final byte UNDELETED_FLAG = 0;
    public static final byte DELETED_FLAG = 1;

    //双创接口标识
    public static final String SEC_INTERFACE = "SHUANGCHUANG";
    public static final String JIAKAI_INTERFACE = "JIAKAI";
    //产品类型
    public enum ProductType {
        //现金产品
        CURRENCY((byte) 0, "现金产品",MINUS_ACCOUNT_TYPE.CURRENCY.getCode()),

        //流量池产品
        FLOW_ACCOUNT((byte) 1, "流量池产品",MINUS_ACCOUNT_TYPE.FLOW.getCode()),

        //流量包产品
        FLOW_PACKAGE((byte) 2, "流量包产品",MINUS_ACCOUNT_TYPE.OTHER.getCode()),

        //话费产品
        MOBILE_FEE((byte) 3,"话费产品",MINUS_ACCOUNT_TYPE.CURRENCY.getCode()),
        
        //虚拟币产品
        VIRTUAL_COIN((byte) 4, "虚拟币",MINUS_ACCOUNT_TYPE.CURRENCY.getCode()),
        
        //预付费资金产品
        PRE_PAY_CURRENCY((byte) 5,  "预付费资金产品",MINUS_ACCOUNT_TYPE.CURRENCY.getCode()),
        
        //预付费流量包产品
        PRE_PAY_PRODUCT((byte) 6,  "预付费流量包产品",MINUS_ACCOUNT_TYPE.OTHER.getCode());

        private byte value;
        private String desc;
        private Integer minusType; //扣款类型 是tryMinusCurrencyAccount还是tryMinusFlowAccount
        
        ProductType(byte value, String desc,int minusType) {
            this.value = value;
            this.desc = desc;
            this.minusType = minusType;
        }

        public static boolean isValidProductType(int value) {
            for (ProductType pt : ProductType.values()) {
                if (pt.getValue() == value) {
                    return true;
                }
            }

            return false;
        }

        /**
         * @param value
         * @return
         */
        public static ProductType fromValue(byte value) {
            for (ProductType pt : ProductType.values()) {
                if (pt.getValue() == value) {
                    return pt;
                }
            }

            return null;
        }
        
        /**
         * accountService产品类型扣款顺序
         */
        public static List<Integer> getMinusAccSeq(){
            List<Integer> prdTypes = new LinkedList<Integer>();
            prdTypes.add((int) com.cmcc.vrp.ec.bean.Constants.ProductType.PRE_PAY_CURRENCY.getValue());
            prdTypes.add((int) com.cmcc.vrp.ec.bean.Constants.ProductType.FLOW_ACCOUNT.getValue());
            prdTypes.add((int) com.cmcc.vrp.ec.bean.Constants.ProductType.CURRENCY.getValue());
            return prdTypes;
        }

        public byte getValue() {
            return value;
        }

        public void setValue(byte value) {
            this.value = value;
        }
        
        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public Integer getMinusType() {
            return minusType;
        }

        public void setMinusType(Integer minusType) {
            this.minusType = minusType;
        }
        
        
    }

}
