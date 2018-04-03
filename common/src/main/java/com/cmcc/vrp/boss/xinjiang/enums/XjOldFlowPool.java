package com.cmcc.vrp.boss.xinjiang.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 保存新疆7月底上线前开户企业的流量池使用产品的信息
 *
 */
public enum XjOldFlowPool {
    ENTER_491_oldProductOne("9919122100","9116042210859236","oldProductOne"),
    ENTER_503_oldProductOne("9911102117","1116053112661278","oldProductOne"),
    ENTER_506_oldProductOne("9918151423","9116053012640692","oldProductOne"),
    ENTER_509_oldProductOne("9919922282","9916060212782910","oldProductOne"),
    ENTER_509_oldProductTwo("9919922282","9916071814980805","oldProductTwo"),
    ENTER_509_oldProductThree("9919922282","9916072515277668","oldProductThree"),
    ENTER_515_oldProductOne("9909002727","9016060713025937","oldProductOne"),
    ENTER_518_oldProductOne("9919109783","9116062713940501","oldProductOne"),
    ENTER_521_oldProductOne("9021224430","1216063014135195","oldProductOne"),
    ENTER_524_oldProductOne("9061629447","1616070114203854","oldProductOne"),
    ENTER_524_oldProductTwo("9061629447","1616071814976956","oldProductTwo"),
    ENTER_524_oldProductThree("9061629447","1616072015100893","oldProductThree"),
    ENTER_527_oldProductOne("9061629770","1216063014135195","oldProductOne"),
    ENTER_530_oldProductOne("9919101288","9116070714462785","oldProductOne"),
    ENTER_536_oldProductOne("9929270086","9216071414807381","oldProductOne"),
    ENTER_539_oldProductOne("9061629950","1616071814976171","oldProductOne"),
    ENTER_545_oldProductOne("9989829242","9816072215189466","oldProductOne");
    
    private String enterCode;
    
    private String userId;
    
    private String prdCode;

    private XjOldFlowPool(String enterCode, String userId, String prdCode) {
        this.enterCode = enterCode;
        this.userId = userId;
        this.prdCode = prdCode;
    }

    public String getEnterCode() {
        return enterCode;
    }

    public void setEnterCode(String enterCode) {
        this.enterCode = enterCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPrdCode() {
        return prdCode;
    }

    public void setPrdCode(String prdCode) {
        this.prdCode = prdCode;
    }
    
    /**
     * 生成map的方法，key为enterCode+"_"+prdCode ,value为userId
     */
    public static Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        for (XjOldFlowPool item : XjOldFlowPool.values()){
            map.put(item.getEnterCode() + "_" +item.getPrdCode(), item.getUserId());
        }
        return map;
    }
    
}
