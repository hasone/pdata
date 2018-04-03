/**
 *
 */
package com.cmcc.vrp.queue.pojo;

/**
 * <p>Title:MdrcSmsChargePojo </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年7月29日
 */
public class MdrcSmsChargePojo {

    private String mobile;

    private String content;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content.replaceAll(" ", "").replaceAll("\n", "");
    }


}
