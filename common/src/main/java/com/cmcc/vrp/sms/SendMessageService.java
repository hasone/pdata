package com.cmcc.vrp.sms;

import com.cmcc.vrp.queue.pojo.SmsPojo;

/**
 * 短信下发服务类
 * <p>
 * 1. 建议所有的短信下发操作都必须有记录，最好能记录到表中
 * 2. 可以考虑通过队列来实现
 * <p>
 * Created by sunyiwei on 2016/4/6.
 */
public interface SendMessageService {
    /**
     * 发送单条短信
     *
     * @param smsPojo 短信对象
     *
     * @return 发送成功返回true， 否则false
     */
    boolean send(SmsPojo smsPojo);
}
