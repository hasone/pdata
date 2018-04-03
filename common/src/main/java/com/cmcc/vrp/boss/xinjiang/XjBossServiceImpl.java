package com.cmcc.vrp.boss.xinjiang;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.xinjiang.enums.XjOldFlowPool;
import com.cmcc.vrp.boss.xinjiang.response.NewResourcePoolResp;
import com.cmcc.vrp.boss.xinjiang.response.SendResp;
import com.cmcc.vrp.boss.xinjiang.service.XinjiangBossService;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.webservice.sichuan.pojo.ReturnCode;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

/**
 * 新疆boss实现 XjBossServiceImpl.java
 *
 * @author wujiamin, qihang
 * @date 2016年11月18日, 2016年12月6日
 */
@Service
public class XjBossServiceImpl implements BossService {

    /**
     * 保存7月底之前上线的企业的流量池userId的Map，因为数据只有十几条，且以后不再有新的数据，所以在类初始化时直接加载完成 key为enterCode + "_" + prdCode
     * ,value 为 userId
     */
    public final static Map<String, String> OLD_FLOWPOOL_MAP;
    private static final Logger LOGGER = LoggerFactory
            .getLogger(XjBossServiceImpl.class);
    // 新疆7月底前开户企业可使用产品的前缀
    private static final String OLDPOOL_PREFIX = "oldProduct";
    // 新疆7月底后开户企业可使用产品的前缀
    private static final String NEWPOOL_PREFIX = "flowProduct";

    static {
        Map<String, String> map = XjOldFlowPool.toMap();
        OLD_FLOWPOOL_MAP = Collections.unmodifiableMap(map);
    }

    @Autowired
    private XinjiangBossService xinjiangBossService;
    @Autowired
    private EnterprisesService enterprisesService;
    @Autowired
    private ProductService productService;
    @Autowired
    private SupplierProductService supplierProductService;

    /**
     * 此处的splPid为product表的Id，非suppiler表的Id
     */
    @Override
    public BossOperationResult charge(Long entId, Long splPid, String mobile,
                                      String serialNum, Long prdSize) {
        // 第一版，只有新疆最新的boss,之后添加兼容多个流量池的
        Enterprise enterprise = enterprisesService.selectByPrimaryKey(entId);
        SupplierProduct sPrd = supplierProductService.selectByPrimaryKey(splPid);

        if (enterprise == null || sPrd == null) {
            LOGGER.error("企业id{}或者产品id{}在数据库不存在", entId, splPid);
            return new XjBossOperationResultImpl(
                    ReturnCode.parameter_error.getCode(),
                    ReturnCode.parameter_error.getMsg());

        }

        // 得到充值的mb数量
        Long mbNums = prdSize / 1024;

        // 根据产品编码为新流量池或老流量池，选择boss的方法
        if (sPrd.getCode().startsWith(OLDPOOL_PREFIX)) {// 选择的是旧的流量池

            String userId = OLD_FLOWPOOL_MAP.get(enterprise.getCode() + "_"
                    + sPrd.getCode());
            if (StringUtils.isBlank(userId)) {
                LOGGER.error("新疆旧流量编码{}在平台中不存在", enterprise.getCode() + "_"
                        + sPrd.getCode());
                return new XjBossOperationResultImpl(
                        ReturnCode.parameter_error.getCode(),
                        ReturnCode.parameter_error.getMsg());
            }

            SendResp resp = xinjiangBossService.getSendResp(
                    enterprise.getCode(), userId, mobile,
                    String.valueOf(mbNums), serialNum);
            return new XjBossOperationResultImpl(resp);

        } else if (sPrd.getCode().equals(NEWPOOL_PREFIX)) {// 选择的是新的流量池
            SendResp resp = xinjiangBossService.getNewSendResp(
                    enterprise.getCode(), mobile, String.valueOf(mbNums),
                    serialNum);
            return new XjBossOperationResultImpl(resp);

        } else {// 产品错误
            LOGGER.error("产品编码{}错误", sPrd.getCode());
            return new XjBossOperationResultImpl(
                    ReturnCode.parameter_error.getCode(),
                    ReturnCode.parameter_error.getMsg());
        }

    }

    /**
     * getFingerPrint
     */
    @Override
    public String getFingerPrint() {
        return "xinjiang";
    }

    /**
     * mdrcCharge
     */
    @Override
    public BossOperationResult mdrcCharge(String cardNumber, String mobile) {
        return null;
    }

    /**
     * syncFromBoss
     */
    @Override
    public boolean syncFromBoss(Long entId, Long prdId) {
        return false;
    }

    /**
     * queryAccountByEntId
     */
    public String queryAccountByEntId(Long entId) {
        String defaultResult = "网络繁忙，请稍后再试";
        Enterprise enterprise = enterprisesService.selectByPrimaryKey(entId);
        if (enterprise == null) {
            LOGGER.error("数据库中没有相关企业, 企业ID = {}", entId);//表示企业不存在
            return defaultResult;
        }

        NewResourcePoolResp resp = xinjiangBossService.getResourcePoolRespNew(enterprise.getCode());
        if (!resp.getResultCode().equals("0")) {
            LOGGER.error("boss查询余额失败，企业编码为{}, 错误信息:", enterprise.getCode(), resp.getResultInfo());
            return defaultResult;
        } else {
            return resp.getAddValue() + "MB";
        }
    }
}
