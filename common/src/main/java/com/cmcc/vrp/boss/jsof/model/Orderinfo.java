package com.cmcc.vrp.boss.jsof.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("orderinfo")
public class Orderinfo {

    @XStreamAlias("err_msg")
    private String errMsg;
    
    @XStreamAlias("retcode")
    private String retcode;
    
    @XStreamAlias("orderid")
    private String orderid;
    
    @XStreamAlias("cardid")
    private String cardid;
    
    @XStreamAlias("cardnum")
    private String cardnum;
    
    @XStreamAlias("ordercash")
    private String ordercash;
    
    @XStreamAlias("cardname")
    private String cardname;
    
    @XStreamAlias("sporder_id")
    private String sporderId;
    
    @XStreamAlias("game_userid")
    private String gameUserid;
    
    @XStreamAlias("game_state")
    private String gameState;

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getRetcode() {
        return retcode;
    }

    public void setRetcode(String retcode) {
        this.retcode = retcode;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getCardid() {
        return cardid;
    }

    public void setCardid(String cardid) {
        this.cardid = cardid;
    }

    public String getCardnum() {
        return cardnum;
    }

    public void setCardnum(String cardnum) {
        this.cardnum = cardnum;
    }

    public String getOrdercash() {
        return ordercash;
    }

    public void setOrdercash(String ordercash) {
        this.ordercash = ordercash;
    }

    public String getCardname() {
        return cardname;
    }

    public void setCardname(String cardname) {
        this.cardname = cardname;
    }

    public String getSporderId() {
        return sporderId;
    }

    public void setSporderId(String sporderId) {
        this.sporderId = sporderId;
    }

    public String getGameUserid() {
        return gameUserid;
    }

    public void setGameUserid(String gameUserid) {
        this.gameUserid = gameUserid;
    }

    public String getGameState() {
        return gameState;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }
}
