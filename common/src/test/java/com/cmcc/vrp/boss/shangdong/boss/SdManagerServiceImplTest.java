package com.cmcc.vrp.boss.shangdong.boss;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.boss.shangdong.boss.service.SdCloudWebserviceImpl;
import com.cmcc.vrp.boss.shangdong.boss.service.impl.SdAdministerServiceImpl;
import com.cmcc.vrp.boss.shangdong.boss.service.impl.SdManagerServiceImpl;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.google.gson.Gson;

/**
 *
 * @author panxin
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class SdManagerServiceImplTest {
    @InjectMocks
    SdManagerServiceImpl sdManagerService = new SdManagerServiceImpl();

    @Mock
    SdAdministerServiceImpl sdAdministerService;

    @Mock
    private SdCloudWebserviceImpl platFormService;

    @Mock
    AdministerService administerService;

    @Mock
    private GlobalConfigService globalConfigService;

    /**
     * testCreateManager
     */
    @Test
    public void testCreateManager() {
        List<String> resultList = new ArrayList<String>();
        resultList.add("01");

        //正确的json数据
//        String jsonCurrentMgrInfo = "{\"code\":\"200\",\"manager\":{\"username\":\"panxin\",\"phone\":\"18867105827\","
//                + "\"registerIp\":\"1092\",\"enterpriseId\":\"01\",\"email\":\"panxin@163.com\",\"gender\":\"1\",\"newsLetter\":\"true\","
//                + "\"credentialNumber\":\"2614\",\"credentialType\":\"shenfenzheng\",\"lastUpdateDate\":\"20161112\",\"createDate\":\"20161112\","
//                + "\"lastLoginDate\":\"20161112\",\"isAdmin\":\"true\",\"customerGroupId\":\"\",\"customerGroupName\":\"\","
//                + "\"enterpriseName\": \"test\",\"name\": \"panxin\",\"id\": \"123\",\"status\": \"状态\"}, \"msg\": \"ok\"}";

        String jsonCurrentMgrInfo = buildInfo("200", "18867105827");
        when(platFormService.getCurrentMgrInfo(Mockito.anyString())).thenReturn(jsonCurrentMgrInfo);
        when(platFormService.getEnterprisesInfoList(Mockito.anyString())).thenReturn(resultList);
        when(sdAdministerService.insertManager(Matchers.any(Administer.class), Matchers.anyLong(), anyListOf(String.class),
                Matchers.anyLong())).thenReturn(true);
        when(globalConfigService.get("ACCOUNT_MANAGER_ROLE_ID")).thenReturn("2");
        when(globalConfigService.get("CITY_MANAGER_ROLE_ID")).thenReturn("7");
        Assert.assertEquals(sdManagerService.createManager("111"), "18867105827");

        //缺少了手机字段
//        String jsonError = "{\"code\":\"200\",\"manager\":{\"username\":\"panxin\",\"phone\":\"\","
//                + "\"registerIp\":\"1092\",\"enterpriseId\":\"01\",\"email\":\"panxin@163.com\",\"gender\":\"1\",\"newsLetter\":\"true\","
//                + "\"credentialNumber\":\"2614\",\"credentialType\":\"shenfenzheng\",\"lastUpdateDate\":\"20161112\",\"createDate\":\"20161112\","
//                + "\"lastLoginDate\":\"20161112\",\"isAdmin\":\"true\",\"customerGroupId\":\"\",\"customerGroupName\":\"\","
//                + "\"enterpriseName\": \"test\",\"name\": \"panxin\",\"id\": \"123\",\"status\": \"状态\"}, \"msg\": \"ok\"}";

        String jsonError = buildInfo("200", null);
        when(platFormService.getCurrentMgrInfo(Mockito.anyString())).thenReturn(jsonError);
        Assert.assertEquals(sdManagerService.createManager("111"), "-1");

        //无法解析的json数据，解析异常
        String jsonNotCompile = "{";
        when(platFormService.getCurrentMgrInfo(Mockito.anyString())).thenReturn(jsonNotCompile);
        Assert.assertNull(sdManagerService.createManager("111"));


    }

    /**
     * testUpdateManager
     */
    @Test
    public void testUpdateManager() {
        //正确的json数据
//        String jsonCurrentMgrInfo = "{\"code\":\"200\",\"manager\":{\"username\":\"panxin\",\"phone\":\"18867105827\","
//                + "\"registerIp\":\"1092\",\"enterpriseId\":\"01\",\"email\":\"panxin@163.com\",\"gender\":\"1\",\"newsLetter\":\"true\","
//                + "\"credentialNumber\":\"2614\",\"credentialType\":\"shenfenzheng\",\"lastUpdateDate\":\"20161112\",\"createDate\":\"20161112\","
//                + "\"lastLoginDate\":\"20161112\",\"isAdmin\":\"true\",\"customerGroupId\":\"\",\"customerGroupName\":\"\","
//                + "\"enterpriseName\": \"test\",\"name\": \"panxin\",\"id\": \"123\",\"status\": \"状态\"}, \"msg\": \"ok\"}";
        String jsonCurrentMgrInfo = buildInfo("200", "18867105827");

        Administer administer = new Administer();
        List<String> resultList = new ArrayList<String>();
        resultList.add("01");

        when(platFormService.getCurrentMgrInfo(Mockito.anyString())).thenReturn(jsonCurrentMgrInfo);
        when(administerService.selectByMobilePhone("18867105827")).thenReturn(administer);
        when(platFormService.getEnterprisesInfoList(Mockito.anyString())).thenReturn(resultList);
        when(sdAdministerService.updateManager(Matchers.any(Administer.class), anyListOf(String.class), Matchers.anyLong(),
                Matchers.anyLong())).thenReturn(true);
        when(globalConfigService.get("ACCOUNT_MANAGER_ROLE_ID")).thenReturn("2");
        when(globalConfigService.get("CITY_MANAGER_ROLE_ID")).thenReturn("7");
        Assert.assertTrue(sdManagerService.updateManager("111"));


        //无法解析的json数据，解析异常
        String jsonNotCompile = "{";
        when(platFormService.getCurrentMgrInfo(Mockito.anyString())).thenReturn(jsonNotCompile);
        Assert.assertFalse(sdManagerService.updateManager("111"));
    }
    private String buildInfo(String code, String phone) {
        Info info = new Info();
        Manager manager = new Manager();
        manager.setPhone(phone);
        info.setManager(manager);
        info.setCode(code);
        return new Gson().toJson(info);
    }

    private class Info {
        private String code;

        private String msg;

        private Manager manager;

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

        public Manager getManager() {
            return manager;
        }

        public void setManager(Manager manager) {
            this.manager = manager;
        }
    }

    private class Manager {
        String username;
        String phone;
        String registerIp;
        String enterpriseId;
        String email;
        String gender;
        String newsLetter;
        String credentialNumber;
        String credentialType;
        String lastUpdateDate;
        String createDate;
        String lastLoginDate;
        String isAdmin;
        String customerGroupId;
        String customerGroupName;
        String enterpriseName;
        String name;
        String id;
        String status;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getRegisterIp() {
            return registerIp;
        }

        public void setRegisterIp(String registerIp) {
            this.registerIp = registerIp;
        }

        public String getEnterpriseId() {
            return enterpriseId;
        }

        public void setEnterpriseId(String enterpriseId) {
            this.enterpriseId = enterpriseId;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getNewsLetter() {
            return newsLetter;
        }

        public void setNewsLetter(String newsLetter) {
            this.newsLetter = newsLetter;
        }

        public String getCredentialNumber() {
            return credentialNumber;
        }

        public void setCredentialNumber(String credentialNumber) {
            this.credentialNumber = credentialNumber;
        }

        public String getCredentialType() {
            return credentialType;
        }

        public void setCredentialType(String credentialType) {
            this.credentialType = credentialType;
        }

        public String getLastUpdateDate() {
            return lastUpdateDate;
        }

        public void setLastUpdateDate(String lastUpdateDate) {
            this.lastUpdateDate = lastUpdateDate;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getLastLoginDate() {
            return lastLoginDate;
        }

        public void setLastLoginDate(String lastLoginDate) {
            this.lastLoginDate = lastLoginDate;
        }

        public String getIsAdmin() {
            return isAdmin;
        }

        public void setIsAdmin(String isAdmin) {
            this.isAdmin = isAdmin;
        }

        public String getCustomerGroupId() {
            return customerGroupId;
        }

        public void setCustomerGroupId(String customerGroupId) {
            this.customerGroupId = customerGroupId;
        }

        public String getCustomerGroupName() {
            return customerGroupName;
        }

        public void setCustomerGroupName(String customerGroupName) {
            this.customerGroupName = customerGroupName;
        }

        public String getEnterpriseName() {
            return enterpriseName;
        }

        public void setEnterpriseName(String enterpriseName) {
            this.enterpriseName = enterpriseName;
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
