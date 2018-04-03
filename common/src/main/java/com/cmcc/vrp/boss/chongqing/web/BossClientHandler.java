/**
 * @Title: BossClientHandler.java
 * @Package com.cmcc.vrp.chongqing.boss.web
 * @author: qihang
 * @date: 2015年4月28日 上午11:45:45
 * @version V1.0
 */
package com.cmcc.vrp.boss.chongqing.web;

import com.cmcc.vrp.boss.chongqing.enums.BossConnectionType;
import com.cmcc.vrp.boss.chongqing.service.request.impl.BasicRequest;
import com.cmcc.vrp.boss.chongqing.service.response.impl.BasicResponse;
import com.cmcc.vrp.util.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

/**
 * @ClassName: BossClientHandler
 * @Description: 连接Boss的处理类
 * @author: qihang
 * @date: 2015年4月28日 上午11:45:45
 *
 */

public class BossClientHandler extends ChannelInboundHandlerAdapter {

    private static final Log logger = LogFactory
        .getLog(BossClientHandler.class);

    public BasicRequest serviceRequest;// boss请求报文相关类

    public BasicResponse serviceResponse;// boss返回报文相关类

    public byte[] recvDatas;// 已收到的数据缓存

    int currentTimes = 0;// 当前已收报文的个数
    int totalTimes = 0;// 总计需要收报文的个数

    /**
     * @Title: setConnectionParams
     * @Description: 设置连接属性，包括连接的类型，连接用的数据
     * @param ctx
     * @param msg
     * @return
     */
    public void setConnectionParams(BossConnectionType type,
                                    Map<String, Object> params, BasicRequest serviceRequest, BasicResponse serviceResponse) {
        this.serviceRequest = serviceRequest;
        this.serviceResponse = serviceResponse;

        serviceRequest.setParams(params);
        serviceResponse.setParams(params);
    }


    /**
     * @Title: channelRead
     * @Description: 当收到从服务器端的消息时的处理
     * @param ctx
     *        ,msg
     * @return
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
        throws Exception {
        ByteBuf recvBuf = (ByteBuf) msg;
        byte[] recvBytes = new byte[recvBuf.readableBytes()];
        recvBuf.readBytes(recvBytes);

        recvBuf.release();

        if (totalTimes == 0) {// 是收到的第一份报文，需要得到总报文的长度，决定需要收几次报文
            byte[] packetLengthBytes = new byte[4];// 该数组储存报文的长度
            for (int i = 0; i < 4; i++) {// 截取前4位，组装成一个新的byte[]
                packetLengthBytes[i] = recvBytes[i];
            }
            int packetLength = ByteUtil.convertBytesToInt(packetLengthBytes);// 将报文长度转化为int类型
            totalTimes = packetLength / 1024 + 1;// 需要接收的次数，总报文长度0-1023个为1次，1024-2047为2次，以此类推
        }

        currentTimes++;
        if (currentTimes == 1) {// 第一次报文不需要连接之前的报文
            recvDatas = recvBytes;
        } else {// 非第一次报文需要连接之前的报文，与之前报文合并
            recvDatas = ByteUtil.concatByteArrays(recvDatas, recvBytes);
        }

        // 检测是否已收到全部报文
        if (currentTimes == totalTimes) {
            serviceResponse.recvData(recvDatas);

            logger.info("从服务器端收到数据:" + new String(recvDatas));

            //serviceResponse.printAll();
            serviceResponse.responseHandler();
            ctx.channel().close();
        }

    }

    /**
     * @Title: channelActive
     * @Description: 连接成功后的处理
     * @param ctx
     * @return
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("已经与Boss成功建立连接:");
        byte[] sendBytes = serviceRequest.getRequestDatas();
        ByteBuf encoded = ctx.alloc().buffer(sendBytes.length);
        encoded.writeBytes(sendBytes);

        logger.info("向Boss发送数据:");
        logger.info(new String(sendBytes));

        ctx.write(encoded);
        ctx.flush();

    }

}
