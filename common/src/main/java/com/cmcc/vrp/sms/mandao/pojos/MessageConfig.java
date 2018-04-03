package com.cmcc.vrp.sms.mandao.pojos;

import java.util.Date;
import java.util.List;

/**
 * Created by leelyn on 2016/3/2.
 */
public class MessageConfig {

    private List<String> mobiles;
    private String content;
    private String ext;
    private Date stime;
    private String rrid;
    private MessageFormat msgfmt;

    public List<String> getMobiles() {
        return mobiles;
    }

    public void setMobiles(List<String> mobiles) {
        this.mobiles = mobiles;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public Date getStime() {
        return stime;
    }

    public void setStime(Date stime) {
        this.stime = stime;
    }

    public String getRrid() {
        return rrid;
    }

    public void setRrid(String rrid) {
        this.rrid = rrid;
    }

    public MessageFormat getMsgfmt() {
        return msgfmt;
    }

    public void setMsgfmt(MessageFormat msgfmt) {
        this.msgfmt = msgfmt;
    }
}
