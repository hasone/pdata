package com.cmcc.vrp.sms.shanghai;

import com.cmcc.vrp.queue.ShMsgTaskProducer;
import com.cmcc.vrp.queue.pojo.SmsPojo;
import com.cmcc.vrp.sms.SendMessageService;
import com.cmcc.vrp.sms.shanghai.pojos.ShMsgPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by leelyn on 2016/11/16.
 */
@Service
public class ShSendMessageImpl implements SendMessageService {

    @Autowired
    ShMsgTaskProducer taskProducer;

    @Override
    public boolean send(SmsPojo smsPojo) {
        if (smsPojo == null) {
            return false;
        }
        ShMsgPojo pojo = new ShMsgPojo();
        pojo.setMobile(smsPojo.getMobile());
        pojo.setContent(smsPojo.getContent());
        return taskProducer.produceShMsg(pojo);
    }
}
