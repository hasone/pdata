package com.cmcc.vrp.queue.task;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.province.mdrc.service.MdrcCardInfoService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.MdrcInfoPojo;
import com.cmcc.vrp.queue.pojo.MdrcSmsChargePojo;
import com.cmcc.vrp.queue.pojo.SmsPojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * <p>Title:MdrcSmsChargeWorker </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年7月29日
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MdrcSmsChargeWorker extends Worker {

    private static final Logger LOGGER = LoggerFactory.getLogger(MdrcSmsChargeWorker.class);

    @Autowired
    MdrcCardInfoService mdrcCardInfoService;

    @Autowired
    TaskProducer taskProducer;

    @Override
    public void exec() {

        MdrcSmsChargePojo mdrcSmsChargePojo = null;
        String taskString = getTaskString();
        LOGGER.info("MdrcSmsChargeWorker taskString：" + taskString);

        try {
            mdrcSmsChargePojo = JSON.parseObject(taskString, MdrcSmsChargePojo.class);
//        	mdrcSmsChargePojo.setMobile(mdrcSmsChargePojo.getMobile().substring(0,11));
        } catch (Exception e) {
            LOGGER.error("解析任务参数时出错，任务参数为{}.", taskString);
            return;
        }

        MdrcInfoPojo mdrcInfoPojo = getMdrcInfo(mdrcSmsChargePojo);

        LOGGER.info("开始充值,手机号码：" + mdrcSmsChargePojo.getMobile());

        if (mdrcCardInfoService.use(mdrcInfoPojo.getCardNum(), mdrcInfoPojo.getPassword(),
            mdrcSmsChargePojo.getMobile(), mdrcSmsChargePojo.getMobile(), mdrcSmsChargePojo.getMobile())) {
            LOGGER.info("充值成功,手机号：{}, 卡号：{}", mdrcSmsChargePojo.getMobile(), mdrcInfoPojo.getCardNum());
        } else {
            LOGGER.info("充值失败,手机号：{}, 卡号：{}", mdrcSmsChargePojo.getMobile(), mdrcInfoPojo.getCardNum());
            String content = "您输入的卡号或密码不正确，请重新输入";
            LOGGER.info("发送充值失败提醒短息, 手机号={}, 短信内容={}", mdrcSmsChargePojo.getMobile(), content);
            taskProducer.produceDeliverNoticeSmsMsg(new SmsPojo(
                mdrcSmsChargePojo.getMobile(), content, null, null, null));
        }

    }

    private MdrcInfoPojo getMdrcInfo(MdrcSmsChargePojo mdrcSmsChargePojo) {
        MdrcInfoPojo mdrcInfoPojo = new MdrcInfoPojo();
        String content = mdrcSmsChargePojo.getContent();
        String[] strs = content.split("#");
        mdrcInfoPojo.setCardNum(strs[0]);
        mdrcInfoPojo.setPassword(strs[1]);
        return mdrcInfoPojo;
    }
}
