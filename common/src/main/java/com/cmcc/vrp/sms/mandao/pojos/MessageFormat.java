package com.cmcc.vrp.sms.mandao.pojos;

/**
 * Created by leelyn on 2016/3/2.
 */
public enum MessageFormat {
    ASCII("0", "ASCII串"),
    WRITE_CARD("3", "短信写卡操作"),
    BINARY("4", "二进制信息"),
    GBK("15", "含GBK汉字");

    private String value;
    private String description;

    MessageFormat(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
