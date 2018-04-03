package com.cmcc.vrp.queue;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.AddressResolver;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * 自定义的RMQ地址解析器
 *
 * Created by sunyiwei on 2017/2/6.
 */
@Component
public class MultipleAddressResolver implements AddressResolver {
    /**
     * 虚拟的地址,可以指定多个主机和端口, 主机间用逗号分隔,主机和端口间用冒号分隔,如
     *
     * host1:port1, host2:port2, host3:port3
     */
    @Value("#{settings['queue.ip']}")
    private String virtualAddress;

    private boolean isResolved = false;
    private List<Address> addressList;

    @Override
    public List<Address> getAddresses() throws IOException {
        //如果地址没有解析过,则解析,否则直接返回解析结果
        if (!isResolved) {
            parse();
        }

        return addressList;
    }

    //解析地址
    private void parse() {
        isResolved = true;

        if (StringUtils.isBlank(virtualAddress)) {
            return;
        }

        addressList = new LinkedList<Address>();
        String[] comps = virtualAddress.split(",");
        for (String comp : comps) {
            addressList.add(build(comp.trim()));
        }
    }

    private Address build(String hostAndPort) {
        //指定了端口
        if (hostAndPort.contains(":")) {
            String[] comp = hostAndPort.split(":");
            String host = comp[0];
            String port = comp[1];

            return new Address(host, NumberUtils.toInt(port, 5672));
        }

        //没有指定端口,直接当作主机名处理
        return new Address(hostAndPort);
    }
}
