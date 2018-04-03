/**
 * @Title: BossClient.java
 * @Package com.cmcc.vrp.chongqing.boss.web
 * @author: qihang
 * @date: 2015年4月28日 上午9:52:14
 * @version V1.0
 */
package com.cmcc.vrp.boss.chongqing.web;


import com.cmcc.vrp.boss.chongqing.enums.BossConnectionType;
import com.cmcc.vrp.boss.chongqing.service.request.impl.BasicRequest;
import com.cmcc.vrp.boss.chongqing.service.response.impl.BasicResponse;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * @ClassName: BossClient
 * @Description: TODO
 * @author: qihang
 * @date: 2015年4月28日 上午9:52:14
 */
@Component("cqBossClient")
@Scope("prototype")
public class BossClient {
    private static final Log logger = LogFactory.getLog(BossClient.class);

    @Autowired
    private GlobalConfigService globalConfigService;
   

    public static void main(String[] args) throws Exception {
//        Map<String, Object> params = new HashMap<String, Object>();
//        BossClient client = new BossClient();
        //100251部分
            /*params.put("telnum", "2308091054885");
            params.put("recnum", "13996015612");
		    params.put("prodid", "gl_mwsq_11G");
		    client.connect(BossConnectionType.SINGLE_SEND, params);*/

        //100246部分
           /* params.put("telnum", "2308091054885");
            params.put("prodId", "gl_mwsq_11G");
		    String[] sendTelNums=new String[1];
		    sendTelNums[0]="13996015612";
		    params.put("sendTelNums", sendTelNums);
		    client.connect(BossConnectionType.PATCH_SEND, params);*/

        //100248部分
        //正确流水号
        //params.put("bkjobid", "23146531601208");
        //错误流水号
           /* params.put("bkjobid", "13146531609999");
            client.connect(BossConnectionType.PATCHSEND_RESULTQUERY, params);*/

        //100247部分
        //正确字段
		    /*params.put("telnum", "2308091054885");
		    params.put("startDate", "20150101");
		    params.put("endDate", "20160101");
		    client.connect(BossConnectionType.PATCHSEND_TELQUERY, params);*/

        //100249部分
        //正确字段
		    /*params.put("telnum", "2308091054885");
		    params.put("prodid", "gl_mwsq_11G");
		    client.connect(BossConnectionType.ENTERPRO_QUERY, params);*/
        //错误企业号
		    /*params.put("telnum", "0000000000000");
		    params.put("prodid", "gl_mwsq_11G");
		    client.connect(BossConnectionType.ENTERPRO_QUERY, params);*/
        //错误产品号
		    /*params.put("telnum", "2308091054885");
		    params.put("prodid", "gl_mwsq_0G");
		    client.connect(BossConnectionType.ENTERPRO_QUERY, params);*/


        //100250部分
        //正确字段
		    /*params.put("telnum", "2308091054885");
		    params.put("prodid", "gl_mwsq_11G");
		    params.put("startdate", "20140101");
		    params.put("enddate", "20151231");
		    client.connect(BossConnectionType.ENTERSEND_QUERY, params);*/
        //错误字段telnum和prodid
		    /*params.put("telnum", "0000000000000");
		    params.put("prodid", "gl_mwsq_0G");
		    params.put("startdate", "20140101");
		    params.put("enddate", "20151231");
		    client.connect(BossConnectionType.ENTERSEND_QUERY, params);*/
		    /*params.put("telnum", "2308091054885");
		    params.put("prodid", "gl_mwsq_11G");
		    params.put("startdate", "20160101");
		    params.put("enddate", "20141231");
		    client.connect(BossConnectionType.ENTERSEND_QUERY, params);*/

        //200004部分
        //正确字段
		    /*params.put("sgroupid", "230352180783");
		    client.connect(BossConnectionType.ENTERPRO_TELQUERY, params);*/

        //200272部分
        //错误字段
		    /*params.put("subsid", "230352180783");
		    client.connect(BossConnectionType.ENTERORDER_QUERY, params);*/
        //正确字段
		   /* params.put("subsid", "2308091054885");
		    client.connect(BossConnectionType.ENTERORDER_QUERY, params);*/

        //200001部分
        //正确字段
		    /*params.put("grplinkphone", "18802310210");
		    client.connect(BossConnectionType.CONTACTENTER_QUERY, params);*/


		   /* params.put("bkjobid", "23146531601208");
		    client.connect(BossConnectionType.PATCHSEND_RESULTQUERY, params);*/
    }

    /**
     * 用于连接Boss
     * @param type  boss类型
     * @param params  参数
     * @param request  请求类型
     * @param response  响应类型
     * @throws InterruptedException  这段异常
     */
    public void connect(final BossConnectionType type, final Map<String, Object> params, final BasicRequest request,
            final BasicResponse response,EventLoopGroup workerGroup) throws InterruptedException {
        
        Bootstrap b = new Bootstrap();
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.handler(new ChannelInitializer() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                BossClientHandler socketHandler = new BossClientHandler();

                //设置Handler的属性
                socketHandler.setConnectionParams(type, params, request, response);
                ch.pipeline().addLast(socketHandler);

            }
        });

        // Start the client.
        ChannelFuture f = b.connect(getHost(), getPort()).sync();
        // Wait until the connection is closed.
        f.channel().closeFuture().sync();

        

    }

    public String getHost() {
    	return globalConfigService.get(GlobalConfigKeyEnum.BOSS_CQ_HOST.getKey());
    }

    public int getPort() {
    	return NumberUtils.toInt(globalConfigService.get(GlobalConfigKeyEnum.BOSS_CQ_PORT.getKey()));
    }
}