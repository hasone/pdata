package com.cmcc.vrp.boss.shangdong.boss;

import com.google.gson.Gson;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.shangdong.boss.model.ProductMemberFlowSystemPay;
import com.cmcc.vrp.boss.shangdong.boss.service.SdCloudWebserviceImpl;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyString;


/**
 * @author qihang
 */
@Ignore
@PrepareForTest({HttpUtils.class, SdCloudWebserviceImpl.class})
@RunWith(PowerMockRunner.class)
public class SdCloudWebserviceImplTest {

    @InjectMocks
    SdCloudWebserviceImpl sdBossService = new SdCloudWebserviceImpl();

    @Mock
    GlobalConfigService globalConfigService;

    /**
     * gettoken的url
     */
    String tokenurl;

    /**
     * 充值的url
     */
    String chargeUrl;


    /**
     * init
     */
    @Before
    public void init() {
        //云平台地址,mock使用
        String cloudUrl = "127.0.0.1";
        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.SD_CLOUD_URL.getKey())).
                thenReturn(cloudUrl);

        //云平台plat_formId,mock使用
        String platFormId = "sdllpt";
        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.SD_CLOUD_PLATFORMID.getKey())).
                thenReturn(platFormId);

        tokenurl = sdBossService.getPlatUrl() +
                sdBossService.PLAT_COMMONSERVICE_URL + sdBossService.DYNAMICTOKEN_URL;
        chargeUrl = sdBossService.getPlatUrl() + sdBossService.PLAT_LLPT_URL +
                sdBossService.UPDATE_PRODUCTMEMBER_URL;
        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.SD_CLOUD_CHARGE_URL.getKey())).thenReturn(
                chargeUrl);
    }

    /**
     * testGetAppToken
     */
    @Test
    @Ignore
    public void testGetAppToken() {
//        String tokenResult = "{\"code\":200,\"msg\":\"ok\","
//                + "\"token\":\"cc99cfc7-05a2-43b9-b1e9-dde00d59d591\"}";
        String tokenResult = buildResult("200", "ok", "cc99cfc7-05a2-43b9-b1e9-dde00d59d591");
        String token = "cc99cfc7-05a2-43b9-b1e9-dde00d59d591";


        PowerMockito.mockStatic(HttpUtils.class);
        PowerMockito.when(HttpUtils.post(Matchers.eq(tokenurl), Mockito.anyString(), Mockito.anyString(), Mockito.anyMap())).
                thenReturn(tokenResult);

        Assert.assertEquals(sdBossService.getAppToken("", ""), token);

        //测试错误返回
        PowerMockito.when(HttpUtils.post(Matchers.eq(tokenurl), Mockito.anyString(), Mockito.anyString(), Mockito.anyMap())).
                thenReturn("XXXXX");
        Assert.assertEquals(sdBossService.getAppToken("", ""), null);
    }

    /**
     * testCharge
     */
    @Test
    @Ignore
    public void testCharge() {
//        String tokenResult = "{\"code\":200,\"msg\":\"ok\","
//                + "\"token\":\"cc99cfc7-05a2-43b9-b1e9-dde00d59d591\"}";
        String tokenResult = buildResult("200", "ok", "cc99cfc7-05a2-43b9-b1e9-dde00d59d591");
//        String chargeResult = "{\"code\":200,\"msg\":\"ok\"}";
        String chargeResult = buildResult("200", "ok", null);
        PowerMockito.mockStatic(HttpUtils.class);
        PowerMockito.when(HttpUtils.post(Matchers.eq(tokenurl), anyString(), anyString(), Mockito.anyMap())).
                thenReturn(tokenResult);
        PowerMockito.when(HttpUtils.post(Matchers.eq(chargeUrl), anyString(), anyString(), Mockito.anyMap())).
                thenReturn(chargeResult);

        //测试充值正确
        BossOperationResult result = sdBossService.updateProductMemberFlowSystemPay(getInitPm(), false);
        Assert.assertTrue(result.isSuccess());

        //测试充值失败
        PowerMockito.when(HttpUtils.post(Matchers.eq(chargeUrl), anyString(), anyString(), Mockito.anyMap())).
                thenReturn(buildResult("500", "invoke error", null));
        result = sdBossService.updateProductMemberFlowSystemPay(getInitPm(), false);
        Assert.assertFalse(result.isSuccess());

        //测试返回异常数据,缺少某字段
        PowerMockito.when(HttpUtils.post(Matchers.eq(chargeUrl), anyString(), anyString(), Mockito.anyMap())).
                thenReturn(buildResult("500", null, null));
        result = sdBossService.updateProductMemberFlowSystemPay(getInitPm(), false);
        Assert.assertFalse(result.isSuccess());


        //测试返回异常数据,json无法解析
        PowerMockito.when(HttpUtils.post(Matchers.eq(chargeUrl), anyString(), anyString(), Mockito.anyMap())).
                thenReturn("XXXXXX");
        result = sdBossService.updateProductMemberFlowSystemPay(getInitPm(), false);
        Assert.assertFalse(result.isSuccess());

        //测试apptoken获取失败
        PowerMockito.when(HttpUtils.post(Matchers.eq(tokenurl), anyString(), anyString(), Mockito.anyMap())).
                thenReturn("XXXXX");
        result = sdBossService.updateProductMemberFlowSystemPay(getInitPm(), false);
        Assert.assertFalse(result.isSuccess());
    }

    /**
     * testGetCurrentMgrInfo
     */
    @Test
    public void testGetCurrentMgrInfo() {
//        String result = "{\"code\":200,\"msg\":\"ok\"}";
        String result = buildResult("200", "ok", null);
        PowerMockito.mockStatic(HttpUtils.class);
        PowerMockito.when(HttpUtils.post(anyString(), anyString(), anyString(), Mockito.anyMap())).
                thenReturn(result);

        Assert.assertEquals(sdBossService.getCurrentMgrInfo(""), result);
    }

    /**
     * testGetCurrentUserInfo
     */
    @Test
    public void testGetCurrentUserInfo() {
        String result = "{\"code\":200,\"msg\":\"ok\"}";

        PowerMockito.mockStatic(HttpUtils.class);
        PowerMockito.when(HttpUtils.post(anyString(), anyString(), anyString(), Mockito.anyMap())).
                thenReturn(result);

        Assert.assertEquals(sdBossService.getCurrentUserInfo(""), result);
    }

    /**
     * testGetEnterprisesInfoList
     */
    @Test
    @Ignore
    public void testGetEnterprisesInfoList() {
//        String result = getRecvEnters();
        String result = buildInfo("200", "15888888888");
        PowerMockito.mockStatic(HttpUtils.class);
        PowerMockito.when(HttpUtils.post(anyString(), anyString(), anyString(), Mockito.anyMap())).
                thenReturn(result);


        List<String> listEnters = sdBossService.getEnterprisesInfoList("");
        Assert.assertTrue(listEnters.contains("2624"));
        Assert.assertEquals(listEnters.size(), 1);
    }

    /**
     * testGetEnterpriseInfoList
     */
    @Test
    @Ignore
    public void testGetEnterpriseInfoList() {
//        String result = getRecvEnters();
        String result = buildInfo("200", "15888888888");
        PowerMockito.mockStatic(HttpUtils.class);
        PowerMockito.when(HttpUtils.post(anyString(), anyString(), anyString(), Mockito.anyMap())).
                thenReturn(result);


        List<String> listEnters = sdBossService.getEnterpriseInfoList("");
        Assert.assertTrue(listEnters.contains("2624"));
        Assert.assertEquals(listEnters.size(), 1);
    }

    /**
     * 初始化充值参数
     */
    private ProductMemberFlowSystemPay getInitPm() {
        //云平台充值参数
        ProductMemberFlowSystemPay pm = new ProductMemberFlowSystemPay();

        //企业订购产品后产生的一个用户
        pm.setUserID("11111111");

        pm.setPhone("18867102087");

        //产品标识
        pm.setProductID("1092");

        //企业id
        pm.setEnterpriseID("11111");

        //增值产品id（规格id）
        pm.setBizId("109201");

        pm.setFeeType("1");

        pm.setFactFee("1");

        pm.setLimitFlow("1");

        pm.setItemId("1");

        pm.setStatus("1");

        pm.setCycle("1");//账期

        //1031添加流水号
        pm.setPkgSeq("123456");

        return pm;
    }

    //    private String getRecvEnters(){
//    	return "{\"enterprises\":[{\"city\":\"淄博市\",\"county\":\"临淄区\",\"industries\":[],\"admins\":[],"
//    			+ "\"companyType\":null,\"organizationCode\":\"123124-1\",\"industryCategory1\":null,\"industryCategory2\":null,"
//    			+ "\"industryCategory3\":null,\"industryCategory4\":null,\"province\":\"山东省\",\"town\":null,\"postcode\":null,"
//    			+ "\"businessLicense\":null,\"taxLicense\":null,\"createDate\":1461140419000,\"ecId\":\"5331935023450\",\"corporateSignature\":\"hte\","
//    			+ "\"discount\":\"10\",\"customerManager\":{\"phone\":\"15966977351\",\"areaCode\":null,\"name\":\"王静\","
//    			+ "\"id\":3945,\"number\":\"c7005007\"},"
//    			+ "\"address\":\"5331935023450\",\"name\":\"中国石油化工股份有限公司齐鲁分公司氯碱厂\",\"id\":2624}],\"code\":200,\"msg\":\"ok\"}";
//    }
    private String buildResult(String code, String msg, String token) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setToken(token);
        return new Gson().toJson(result);
    }

    private String buildInfo(String code, String phone) {
        Info info = new Info();
        info.setCode(code);

        List<Ent> entList = new ArrayList<Ent>();
        Ent ent = new Ent();
        ent.setId("2624");
        ent.setDiscount("10");

        CustomerManager customer = new CustomerManager();
        customer.setId("3945");
        ent.setCustomerManager(customer);

        entList.add(ent);
        info.setEnterprises(entList);
        return new Gson().toJson(info);
    }

    private class Result {
        private String code;
        private String msg;
        private String token;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    private class Info {
        private String code;
        private String msg;
        private List<Ent> enterprises;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public List<Ent> getEnterprises() {
            return enterprises;
        }

        public void setEnterprises(List<Ent> enterprises) {
            this.enterprises = enterprises;
        }
    }

    private class Ent {
        private String city;
        private String county;
        private String industries;
        private String admins;
        private String companyType;
        private String organizationCode;
        private String industryCategory1;
        private String industryCategory2;
        private String industryCategory3;
        private String industryCategory4;
        private String province;
        private String town;
        private String postcode;
        private String businessLicense;
        private String taxLicense;
        private String createDate;
        private String corporateSignature;
        private String discount;
        private String address;
        private String name;
        private String id;
        private CustomerManager customerManager;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCounty() {
            return county;
        }

        public void setCounty(String county) {
            this.county = county;
        }

        public String getIndustries() {
            return industries;
        }

        public void setIndustries(String industries) {
            this.industries = industries;
        }

        public String getAdmins() {
            return admins;
        }

        public void setAdmins(String admins) {
            this.admins = admins;
        }

        public String getCompanyType() {
            return companyType;
        }

        public void setCompanyType(String companyType) {
            this.companyType = companyType;
        }

        public String getOrganizationCode() {
            return organizationCode;
        }

        public void setOrganizationCode(String organizationCode) {
            this.organizationCode = organizationCode;
        }

        public String getIndustryCategory1() {
            return industryCategory1;
        }

        public void setIndustryCategory1(String industryCategory1) {
            this.industryCategory1 = industryCategory1;
        }

        public String getIndustryCategory2() {
            return industryCategory2;
        }

        public void setIndustryCategory2(String industryCategory2) {
            this.industryCategory2 = industryCategory2;
        }

        public String getIndustryCategory3() {
            return industryCategory3;
        }

        public void setIndustryCategory3(String industryCategory3) {
            this.industryCategory3 = industryCategory3;
        }

        public String getIndustryCategory4() {
            return industryCategory4;
        }

        public void setIndustryCategory4(String industryCategory4) {
            this.industryCategory4 = industryCategory4;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getTown() {
            return town;
        }

        public void setTown(String town) {
            this.town = town;
        }

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }

        public String getBusinessLicense() {
            return businessLicense;
        }

        public void setBusinessLicense(String businessLicense) {
            this.businessLicense = businessLicense;
        }

        public String getTaxLicense() {
            return taxLicense;
        }

        public void setTaxLicense(String taxLicense) {
            this.taxLicense = taxLicense;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getCorporateSignature() {
            return corporateSignature;
        }

        public void setCorporateSignature(String corporateSignature) {
            this.corporateSignature = corporateSignature;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public CustomerManager getCustomerManager() {
            return customerManager;
        }

        public void setCustomerManager(CustomerManager customerManager) {
            this.customerManager = customerManager;
        }
    }

    private class CustomerManager {
        private String phone;
        private String areaCode;
        private String name;
        private String id;
        private String number;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }
}
