package com.cmcc.vrp.boss.guangdongcard;

import com.cmcc.vrp.boss.guangdongcard.wsdl.ADCABServiceForOpen;
import com.cmcc.vrp.boss.guangdongcard.wsdl.AdcSiRequest;
import com.cmcc.vrp.boss.guangdongcard.wsdl.AdcSiResponse;
import com.cmcc.vrp.boss.guangdongcard.wsdl.NGADCABServiceForOpen;
import com.cmcc.vrp.province.model.GdPrdInfo;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.bind.JAXBElement;

import java.lang.reflect.Field;
import java.util.UUID;

/**
 * 广东流量卡接口的测试
 * <p>
 * Created by sunyiwei on 2016/11/21.
 */
@Ignore
public class AdcSiRequestFactoryTest {
    @Autowired
    AdcSiRequestFactory adcSiRequestFactory;

    /**
     * 创建广东流量卡接口的请求，并发送，然后接受响应
     *
     * @throws Exception
     */
    @Test
    public void testBuild() throws Exception {
        String custId = "75590032314300";
        String ecCode = "2000702804";
        String ecPrdCode = "30730000731";
        String syAppCode = UUID.randomUUID().toString();
        String mobile = "18867102100";
        String userProductCode = "8585";
        String subProductCode = "prod.10086000001992";
        String serviceCode = "8588.memserv10";
        boolean testFlag = true;
        GdPrdInfo gdPrdInfo = new GdPrdInfo();
        gdPrdInfo.setIsNeedServicesNode("1");
        gdPrdInfo.setServiceCode("8588.memserv10");
        gdPrdInfo.setSubProductCode("prod.10086000001992");
        gdPrdInfo.setUserProductCode("8585");
        gdPrdInfo.setUseCycle("1");
        
        
        AdcSiRequest adcSiRequest = adcSiRequestFactory.build(custId, ecCode, ecPrdCode, syAppCode, mobile,gdPrdInfo, testFlag);
        print(adcSiRequest);

        ADCABServiceForOpen serviceFactory = new ADCABServiceForOpen();
        NGADCABServiceForOpen service = serviceFactory.getNGADCABServicesForOpen();
        AdcSiResponse response = service.adcSiInterface(adcSiRequest);
        print(response);
    }

    private void print(Object obj) {
        System.out.println("================");

        Class clazz = obj.getClass();
        Field[] fs = clazz.getDeclaredFields();
        for (Field f : fs) {
            try {
                f.setAccessible(true);

                Object value = f.get(obj);
                if (value.getClass() == JAXBElement.class) {
                    System.out.println(f.getName() + ":" + ((JAXBElement) value).getValue());
                } else {
                    System.out.println(f.getName() + ":" + value.toString());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}