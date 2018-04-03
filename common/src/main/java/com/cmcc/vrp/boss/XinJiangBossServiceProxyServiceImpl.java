package com.cmcc.vrp.boss;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmcc.vrp.boss.sichuan.SCBossOperationResultImpl;
import com.cmcc.vrp.boss.xinjiang.XjBossOperationResultImpl;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.webservice.sichuan.pojo.ReturnCode;

/**
 * 新疆平台的boss代理
 * XinJiangBossServiceProxyServiceImpl.java
 *
 * @author wujiamin
 * @date 2016年11月18日
 */
public class XinJiangBossServiceProxyServiceImpl extends AbstractBossServiceProxyServiceImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(XinJiangBossServiceProxyServiceImpl.class);
    private final String prefix = "xinjiang.boss.proxy.";

    @Override
    public BossOperationResult charge(Long chargeRecordId, Long entId, Long prdId, String mobile, String serialNum) {
        //检查产品是否存在
        Product product = productService.get(prdId);
        if (product == null || product.getDeleteFlag() != 0 || product.getStatus() != 1) {
            LOGGER.error("平台产品id:{},没有该产品", prdId);
            return new XjBossOperationResultImpl(ReturnCode.no_product.getCode(), ReturnCode.no_product.getMsg());
        }

        //调用新疆boss对象进行充值操作
        for (BossService bossService : bossServices) {
            if (bossService.getFingerPrint().equals("xinjiang")) {
                //营销卡不传递chargeRecordId，不插入chargeRecord记录
                if (chargeRecordId != null) {
                    //将splPid及price插入数据库
                    ChargeRecord record = new ChargeRecord();
                    record.setId(chargeRecordId);
                    record.setPrice(product.getPrice().longValue());
                    record.setSupplierProductId(prdId);

                    if (!chargeRecordService.updateByPrimaryKeySelective(record)) {
                        LOGGER.error("无法更新chargeRecord的price:{}和supplierProductId:{}", product.getPrice().longValue(), prdId);
                        return new SCBossOperationResultImpl(ReturnCode.parameter_error);
                    }
                }
                return bossService.charge(entId, prdId, mobile, serialNum, null);
            }
        }

        //没有找到新疆boss，返回处理方内部错误
        return new XjBossOperationResultImpl(ReturnCode.inner_error.getCode(), ReturnCode.inner_error.getMsg());

    }

    @Override
    public boolean needSyncFromBoss() {
        //新疆平台不需要从boss侧同步余额
        return false;
    }

    @Override
    public BossMatchResult chooseBossService(String mobile, Long entId, Long prdId) {
        return null;
    }

    @Override
    protected String getPrefix() {
        return prefix;
    }

}
