/**
 * @author sunyiwei
 * @date 2015年8月12日 下午2:17:17
 */
package com.cmcc.vrp.province.quartz.jobs;

import com.cmcc.vrp.province.mdrc.service.MdrcBatchConfigService;
import com.cmcc.vrp.province.mdrc.service.MdrcCardInfoService;
import com.cmcc.vrp.province.model.MdrcBatchConfig;
import com.cmcc.vrp.province.model.MdrcCardInfo;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 卡批次过期后执行的定时任务
 */
public class BatchConfigExpireJob implements Job {
    private static Logger logger = Logger.getLogger(BatchConfigExpireJob.class);

    @Autowired
    private MdrcBatchConfigService mbcService;

    @Autowired
    private MdrcCardInfoService cardService;

    /**
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("进入卡批次过期定时任务...");

        Long batchId = NumberUtils.toLong((String) context.getJobDetail().getJobDataMap().get("param"));
        MdrcBatchConfig mbc = null;
        if (batchId == null || (mbc = mbcService.selectByPrimaryKey(batchId)) == null) {
            logger.error("无效的卡批次id...");
            return;
        }
        ;

        String year = mbc.getThisYear();
        logger.info("进入卡数据批量过期更新操作, 批次ID为" + mbc.getSerialNumber() + ", 年份为" + year
            + ",省份编码为" + mbc.getProvinceCode());

        List<MdrcCardInfo> cards = cardService.listByConfig(mbc);
        List<String> nums = new ArrayList<String>();
        for (MdrcCardInfo card : cards) {
            nums.add(card.getCardNumber());
        }

        int count = cardService.batchExpire(NumberUtils.toInt(year.substring(year.length() - 2, year.length())), nums);
        logger.info("批量卡数据过期操作已经完成, 共过期" + count + "张卡。");
    }

}
