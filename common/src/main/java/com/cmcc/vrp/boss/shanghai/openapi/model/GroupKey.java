package com.cmcc.vrp.boss.shanghai.openapi.model;


/**
 * @author yan.jy@primeton.com
 * createTime锛�2014骞�7鏈�22鏃�
 *
 * 瀵嗛挜缁勶紝鎻愪緵缁欏紑鍙戣�呬娇鐢�
 */
public class GroupKey {
    private Ask ask;
    private Aedk aedk;

    public GroupKey() {

    }

    public GroupKey(Ask ask, Aedk aedk) {
        this.ask = ask;
        this.aedk = aedk;
    }

    public Ask getAsk() {
        return ask;
    }

    public void setAsk(Ask ask) {
        this.ask = ask;
    }

    public Aedk getAedk() {
        return aedk;
    }

    public void setAedk(Aedk aedk) {
        this.aedk = aedk;
    }
}
