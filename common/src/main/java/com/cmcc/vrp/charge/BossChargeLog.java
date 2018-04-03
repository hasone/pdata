/**
 * @Title: BossChargeLog.java
 * @Package com.cmcc.vrp.province.boss.chongqing.web
 * @author: qihang
 * @date: 2015年9月17日 下午2:11:32
 * @version V1.0
 */
package com.cmcc.vrp.charge;

import org.apache.log4j.Logger;


/**
 * @ClassName: BossChargeLog
 * @Description: 用于对所有boss充值的记录进行日志记录，该类所有的输出都将输出到chargeRecord.log文件中
 * @author: qihang
 * @date: 2015年9月17日 上午10:31:32
 *
 */
public class BossChargeLog {
    private static Logger logger = Logger.getLogger(BossChargeLog.class);

    //打印request日志
    /**
     * @param pojo
     */
    public static void printRequestLog(ChargeLogPojo pojo) {

        logger.info(pojo.getLog());

    }

    /**
     * @param pojo
     * @param chargeResult
     */
    public static void printResponseLog(ChargeLogPojo pojo, ChargeResult chargeResult) {
        logger.info(pojo.setToResponse(chargeResult).getLog());
    }

}
