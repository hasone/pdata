package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by sunyiwei on 2016/6/27.
 */
@XStreamAlias("Response")
public class PhoneRegionResp {
    @XStreamAlias("User")
    private User user;

    public PhoneRegionResp(String mobile, String hlr) {
        User user = new User();

        user.setHlr(hlr);
        user.setMobile(mobile);

        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private static class User {
        @XStreamAlias("Mobile")
        private String mobile;

        @XStreamAlias("HLR")
        private String hlr;

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getHlr() {
            return hlr;
        }

        public void setHlr(String hlr) {
            this.hlr = hlr;
        }
    }
}


