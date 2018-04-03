package com.cmcc.vrp.boss.jsof;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.jsof.model.Feature;
import com.cmcc.vrp.boss.jsof.model.Orderinfo;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;
import com.cmcc.vrp.util.MD5;
import com.cmcc.vrp.util.StringUtils;
import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * @author lgk8023
 *
 */
@Service
public class JsofBossServiceImpl implements BossService{

    private static final Logger LOGGER = LoggerFactory.getLogger(JsofBossServiceImpl.class);
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    SupplierProductService supplierProductService;
    @Autowired
    EnterprisesService enterprisesService;
    private static XStream xStream = null;

    static {
        xStream = new XStream(new DomDriver("GB2312"));
        xStream.alias("orderinfo", Orderinfo.class);
        xStream.autodetectAnnotations(true);
    }
    
    public static void main(String[] args) {
        String systemNum = SerialNumGenerator.buildSerialNum();
        JsofBossServiceImpl bossService = new JsofBossServiceImpl();
        bossService.charge(1l, 1l, "15996271050", systemNum, null);
    }
    @Override
    public BossOperationResult charge(Long entId, Long splPid, String mobile, String serialNum, Long prdSize) {
        LOGGER.info("江苏欧飞充值请求start-{}", serialNum);
        SupplierProduct supplierProduct = null;
        if (entId == null
                || splPid == null
                || (enterprisesService.selectByPrimaryKey(entId)) ==null
                || (supplierProduct = supplierProductService.selectByPrimaryKey(splPid)) == null) {
            LOGGER.error("江苏欧飞充值失败，参数错误. EntId = {}, SplPid = {}, mobile = {}, serialNum = {}", entId, splPid, mobile, serialNum);
            return null;
        }
/*        supplierProduct = new SupplierProduct();
        supplierProduct.setCode("30M");
        supplierProduct.setFeature("{flowValue:30M,perValue:5}");*/
        Gson gson = new Gson();
        Feature feature = gson.fromJson(supplierProduct.getFeature(), Feature.class);
        String flowValue = feature.getFlowValue();
        String perValue = feature.getPerValue();
        try {
            String url = buildBody(mobile, flowValue, perValue, serialNum);
            LOGGER.info("构造请求地址url={}", url);
     
            String response = HttpUtils.post(url, null);
            System.out.println(response);
            LOGGER.info("充值结果返回报文:{}", response);
            
            if (StringUtils.isEmpty(response)) {
                return new JsofBossOperationResultImpl("-100", "boss响应为空");
            }
            Orderinfo orderinfo = (Orderinfo) xStream.fromXML(response);
            if (orderinfo == null) {
                return new JsofBossOperationResultImpl("-103", "解析报文为空");
            }
            if (!"1".equals(orderinfo.getRetcode())) {
                return new JsofBossOperationResultImpl("-104", orderinfo.getErrMsg());
            }
            if ("9".equals(orderinfo.getGameState())) {
                return new JsofBossOperationResultImpl("9", "充值失败");
            }
            if ("1".equals(orderinfo.getGameState())) {
                return new JsofBossOperationResultImpl("1", "充值成功");
            }
            return new JsofBossOperationResultImpl("0", "同步请求成功"); 
        } catch (Exception e) {
            LOGGER.error("充值异常：" + e.getMessage());
            return new JsofBossOperationResultImpl("-102", "充值失败");
        }        
    }

    private String buildBody(String mobile, String flowValue, String perValue, String serialNum) {

        String md5Userpws = MD5.encodeLowerCase(getUserpws());
        String body = getUserid() + md5Userpws + mobile + perValue + flowValue + "2" + "1" + "1" + serialNum + getKeyString();
        LOGGER.info("待加密包体-{}", body);
        String md5Str = MD5.encodeUpCase(body);
        LOGGER.info("加密后包体md5Str={}", md5Str);
        String url = getUrl() + "?userid=" + getUserid() + "&userpws=" + md5Userpws + "&phoneno=" + mobile
                + "&perValue=" + perValue + "&flowValue=" + flowValue + "&range=2&effectStartTime=1&effectTime=1"
                + "&sporderId=" + serialNum + "&md5Str=" + md5Str + "&version=6.0&retUrl=" + getRetUrl();
        return url;
    }

    @Override
    public String getFingerPrint() {
        // TODO Auto-generated method stub
        return "jiangsuoufei";
    }

    @Override
    public BossOperationResult mdrcCharge(String cardNumber, String mobile) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean syncFromBoss(Long entId, Long prdId) {
        // TODO Auto-generated method stub
        return false;
    }

    private String getUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_JSOF_CHARGE_URL.getKey());
        //return "http://apitest.ofpay.com/flowOrder.do";
    }
    private String getUserid() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_JSOF_USERID.getKey());
        //return "A08566";
    }
    private String getUserpws() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_JSOF_USERPWS.getKey());
        //return "of111111";
    }
    private String getRetUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_JSOF_RETURL.getKey());
        //return "http://test";    
    }
    
    private String getKeyString() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_JSOF_KEYSTRING.getKey());
        //return "OFCARD";
    }
}
