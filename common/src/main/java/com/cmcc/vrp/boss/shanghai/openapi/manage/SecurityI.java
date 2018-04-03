package com.cmcc.vrp.boss.shanghai.openapi.manage;

import com.cmcc.vrp.boss.shanghai.openapi.model.Aedk;
import com.cmcc.vrp.boss.shanghai.openapi.model.Ask;

/**
 * @author lgk8023
 *
 */
public interface SecurityI {
    public Ask getAsk() throws Exception;

    public Aedk getAedk() throws Exception;

    /**
     * @param text
     * @return
     * @throws Exception
     */
    public String encrypt(String text) throws Exception;

    /**
     * @param text
     * @return
     * @throws Exception
     */
    public String decrypt(String text) throws Exception;

    /**
     * @param text
     * @return
     * @throws Exception
     */
    public String sign(String text) throws Exception;

    /**
     * @param text
     * @param signValue
     * @param publicKey
     * @return
     * @throws Exception
     */
    public boolean verify(String text, String signValue, String publicKey) throws Exception;
}
