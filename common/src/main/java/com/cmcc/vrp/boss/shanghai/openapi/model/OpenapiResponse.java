package com.cmcc.vrp.boss.shanghai.openapi.model;

import com.cmcc.vrp.boss.shanghai.openapi.util.JsonUtil;

/**
 * @author lgk8023
 *
 */
public class OpenapiResponse extends ServerResponse {
    private String signValue;
    private String publicKey;

    public OpenapiResponse() {

    }

    public OpenapiResponse(String result, String signValue, String publicKey) {
        super(result);
        this.signValue = signValue;
        this.publicKey = publicKey;
    }

    public OpenapiResponse(String result, String exceptionCode) {
        super(result, exceptionCode);
    }

    public String getSignValue() {
        return signValue;
    }

    public void setSignValue(String signValue) {
        this.signValue = signValue;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    /**
     *  (non-Javadoc)
     * @see com.cmcc.vrp.boss.shanghai.openapi.model.ServerResponse#toString()
     */
    public String toString() {
        return JsonUtil.toJsonString(this);
    }
}
