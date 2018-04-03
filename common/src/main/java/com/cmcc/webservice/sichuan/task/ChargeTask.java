/**
 * @Title: chargeTask.java
 * @Package com.cmcc.flow.webservice.task
 * @author: qihang
 * @date: 2015年11月4日 下午2:52:32
 * @version V1.0
 */
package com.cmcc.webservice.sichuan.task;

import com.cmcc.vrp.charge.ChargeResult;
import com.cmcc.vrp.charge.ChargeService;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.enums.AccountType;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.ProductService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Callable;

/**
 * @ClassName: chargeTask
 * @Description: TODO
 * @author: qihang
 * @date: 2015年11月4日 下午2:52:32
 */

public class ChargeTask implements Callable<ChargeResult> {

    private static Logger logger = Logger.getLogger(ChargeTask.class);
    @Autowired
    ProductService productService;
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    private ChargeService chargeService;
    private String enterCode = "";
    private String prdCode = "";
    private String chargeNum = "";
    private Long chargeRecordId = 0L;


    public ChargeTask() {

    }

    /**
     * 设置了连接boss使用的相关参数
     *
     * @param enterCode 企业编码
     * @param prdCode   产品编码
     * @param chargeNum 充值手机号
     */
    public void setConnectParams(String enterCode, String prdCode, String chargeNum, Long chargeRecordId) {
        this.enterCode = enterCode;
        this.prdCode = prdCode;
        this.chargeNum = chargeNum;
        this.chargeRecordId = chargeRecordId;
    }

    public String getEnterCode() {
        return enterCode;
    }

    public void setEnterCode(String enterCode) {
        this.enterCode = enterCode;
    }

    public String getPrdCode() {
        return prdCode;
    }

    public void setPrdCode(String prdCode) {
        this.prdCode = prdCode;
    }


    public String getChargeNum() {
        return chargeNum;
    }


    public void setChargeNum(String chargeNum) {
        this.chargeNum = chargeNum;
    }


    public Long getChargeRecordId() {
        return chargeRecordId;
    }


    public void setChargeRecordId(Long chargeRecordId) {
        this.chargeRecordId = chargeRecordId;
    }


    /**
     * @return
     * @throws Exception
     * @Title: call
     * @Description: 线程运行方法，包括连接boss充值和结果回填数据库
     * @see java.util.concurrent.Callable#call()
     */
    @Override
    public ChargeResult call() throws Exception {
        logger.info("线程运行：enterCode：" + enterCode + ",prdCode:" + prdCode);
        Enterprise enterprise = enterprisesService.selectByCode(enterCode);
        Product product = productService.selectByProductCode(prdCode);

        return chargeService.charge(chargeRecordId, enterprise.getId(), enterprise.getId(), product.getId(), AccountType.ENTERPRISE, chargeNum, SerialNumGenerator.buildSerialNum());
    }

}
