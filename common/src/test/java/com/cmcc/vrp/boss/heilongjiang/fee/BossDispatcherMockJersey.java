package com.cmcc.vrp.boss.heilongjiang.fee;

import java.net.URISyntaxException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.http.client.utils.URIBuilder;

import com.cmcc.vrp.boss.sichuan.model.Sign;

/**
 * Jersey handler for HLJ boss mock.
 *
 * @author Lenovo
 */
@Path("hlj")
public class BossDispatcherMockJersey {

    private final String privateKey = Thread.currentThread().getContextClassLoader().getResource("hlj_private_key_pkcs8.pem").getPath();

    private class SignRequestParam {

        private String stringToSign;
        private String sign;

        public String getStringToSign() {
            return stringToSign;
        }

        public void setStringToSign(String stringToSign) {
            this.stringToSign = stringToSign;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }

    /**
     * Throws exception when an server side error happens in calculating sign.
     * 
     * @param ui
     * @return
     * @throws Exception
     */
    @GET
    @Path("/rest/1.0/sPhoneDefMsgVW_NWS_ml")
    @Produces(MediaType.APPLICATION_JSON)
    public PrepayQueryResponse balanceQuery(@Context UriInfo ui) throws Exception {
        this.validateParam(ui);

        PrepayQueryResponse bqr = new PrepayQueryResponse();
        bqr.setDeposit("deposit");
        bqr.setResCode("00000000");
        bqr.setResMsg("success or what");
        return bqr;
    }

    /**
     * Separate and parse request parameters. And calculate the sign. Called by every HLJ boss controller.
     * 
     * @param ui
     * @throws Exception
     */
    private void validateParam(UriInfo ui) throws Exception {
        MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
        SignRequestParam signParam = getStringToSign(queryParams);
        if (signParam != null && signParam.getStringToSign() != null) {
            String sign = Sign.sign(signParam.getStringToSign(), privateKey);
            if (!sign.equals(signParam.getSign())) {
                throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity("sign is wrong").type("text/plain").build());
            }
        } else {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity("request param is wrong").type("text/plain").build());
        }
    }

    /**
     * Throws exception when an server side error happens in calculating sign.
     * 
     * @param ui
     * @return
     * @throws Exception
     */
    @GET
    @Path("/rest/1.0/bs_zgamCfm_ml")
    @Produces(MediaType.APPLICATION_JSON)
    public ECouponResponse ECoupon(@Context UriInfo ui) throws Exception {
        this.validateParam(ui);
        ECouponResponse ecr = new ECouponResponse();
        ecr.setBossCorrectDate(new Date().toString());
        ecr.setBossCorrectNo("boss correct no");
        ecr.setResCode("rescode");
        ecr.setResMsg("resMsg");
        return ecr;
    }

    /**
     * Throws exception when an server side error happens in calculating sign.
     * 
     * @param ui
     * @return
     * @throws Exception
     */
    @GET
    @Path("/rest/1.0/s_jtzz_ml")
    @Produces(MediaType.APPLICATION_JSON)
    public GroupPersonResponse groupPerson(@Context UriInfo ui) throws Exception {
        this.validateParam(ui);
        GroupPersonResponse gpr = new GroupPersonResponse();
        gpr.setBoss_order_no("boss_order_no");
        gpr.setResCode("resCode");
        gpr.setResMsg("resMsg");
        return gpr;
    }

    /**
     * Throws exception when an server side error happens in calculating sign.
     * 
     * @param ui
     * @return
     * @throws Exception
     */
    @GET
    @Path("/rest/1.0/bs_jtlimit_ml")
    @Produces(MediaType.APPLICATION_JSON)
    public BalanceQueryResponse prepayQuery(@Context UriInfo ui) throws Exception {
        this.validateParam(ui);

        BalanceQueryResponse pqr = new BalanceQueryResponse();
        pqr.setLowestFee("lowest_fee");
        pqr.setAvailableBill("available_bill");
        pqr.setBankCust("bank_cust");
        pqr.setBeginFlag("begin_flag");
        pqr.setChSmCode("chSmCode");
        pqr.setChSmName("chSmName");
        pqr.setEnctName("enct_name");
        pqr.setExpireTime("expire_time");
        pqr.setiLoginAccept("iLoginAccept");
        pqr.setLimitowe("limitowe");
        pqr.setLowestFee("lowest_fee");
        pqr.setLowestFlag("lowest_flag");
        pqr.setMowpay("mowpay");
        pqr.setPayCode("pay_code");
        pqr.setPayName("pay_name");
        pqr.setPreBill("pre_bill");
        pqr.setResCode("resCode");
        pqr.setResMsg("resMsg");
        pqr.setShowPrepay("show_prepay");
        pqr.setSpecPrepay("spec_prepay");
        pqr.setUnbillOwe("unbill_owe");

        return pqr;
    }

    public SignRequestParam getStringToSign(MultivaluedMap<String, String> queryParams) {
        if (queryParams == null || queryParams.size() == 0) {
            return null;
        }
        SignRequestParam signParam = new SignRequestParam();
        String paramToSign = null;
        Map<String, java.util.List<String>> mapSorted = new TreeMap<String, java.util.List<String>>();
        mapSorted.putAll(queryParams);
        URIBuilder builder = new URIBuilder();
        String param = null;
        String paramSign = null;
        for (Iterator<Entry<String, List<String>>> iter = mapSorted.entrySet().iterator(); iter.hasNext();) {
            Entry<String, List<String>> entry = (Entry<String, List<String>>) iter.next();
            if (entry.getKey().toString().equals("sign")) {
                paramSign = entry.getValue().get(0).toString();
                signParam.setSign(paramSign);
                continue;
            }
            builder.setParameter(entry.getKey().toString(), entry.getValue().get(0).toString());
        }
        try {
            param = builder.build().toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        paramToSign = param.charAt(0) == '?' ? param.substring(1) : param;
        signParam.setStringToSign(paramToSign);
        return signParam;
    }

}
