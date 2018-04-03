package com.cmcc.vrp.sms.mandao;

import com.cmcc.vrp.queue.pojo.SmsPojo;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by sunyiwei on 2016/5/3.
 */
@Ignore
public class MDSendMessageServiceImplTest {

    @Autowired
    MDSendMessageServiceImpl messageService;

    @Ignore
    @Test
    public void testSend() throws Exception {
        final String FILENAME = "";

        List<String> mobiles = new LinkedList<String>();
        InputStream is = new BufferedInputStream(new FileInputStream(FILENAME));
        Scanner scanner = new Scanner(is);
        while (scanner.hasNext()) {
            mobiles.add(scanner.next());
        }

        final String CONTENT = "亲爱的杭州粉，感谢参与动漫节随手拍大赛，这70M流量请笑纳，欢迎继续关注浙里杭州，参与其它活动。来浙里，就对了！";

        for (String mobile : mobiles) {
            if (!messageService.send(build(mobile, CONTENT))) {
                System.out.format("发送短信给%s失败. %n", mobile);
            }
        }

        System.out.println("OK");
    }

    private SmsPojo build(String mobile, String content) {
        return new SmsPojo(mobile, content, null, null, null);
    }
}