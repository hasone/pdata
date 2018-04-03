package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.ec.bean.Constants.ProductType;
import com.cmcc.vrp.province.dao.ActivityInfoMapper;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityInfo;
import com.cmcc.vrp.province.model.EntProduct;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.ActivityInfoService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.util.AES;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by qinqinyan on 2016/10/24.
 *
 * @Description 活动详情服务测试类
 */
@RunWith(MockitoJUnitRunner.class)
public class ActivityInfoServiceImplTest {

    @InjectMocks
    ActivityInfoService activityInfoService = new ActivityInfoServiceImpl();
    @Mock
    ActivityInfoMapper activityInfoMapper;
    @Mock
    ProductService productService;
    @Mock
    EntProductService entProductService;
    @Mock
    GlobalConfigService globalConfigService;

    @Test
    public void testInsert() {
        //invalid
        assertFalse(activityInfoService.insert(null));
        //valid
        ActivityInfo record = new ActivityInfo();
        record.setId(1L);
        Mockito.when(activityInfoMapper.insert(Mockito.any(ActivityInfo.class))).thenReturn(0).thenReturn(1);
        assertFalse(activityInfoService.insert(record));
        assertTrue(activityInfoService.insert(record));
        Mockito.verify(activityInfoMapper, Mockito.times(2)).insert(Mockito.any(ActivityInfo.class));
    }

    @Test
    public void testUpdateByPrimaryKeySelective() {
        //invalid
        assertFalse(activityInfoService.updateByPrimaryKeySelective(null));
        //valid
        ActivityInfo record = new ActivityInfo();
        Mockito.when(activityInfoMapper.updateByPrimaryKeySelective(Mockito.any(ActivityInfo.class))).thenReturn(-1).thenReturn(1);
        assertFalse(activityInfoService.updateByPrimaryKeySelective(record));
        assertTrue(activityInfoService.updateByPrimaryKeySelective(record));
        Mockito.verify(activityInfoMapper, Mockito.times(2)).updateByPrimaryKeySelective(Mockito.any(ActivityInfo.class));
    }

    @Test
    public void testSelectByActivityId() throws Exception {
        //invalid
        assertNull(activityInfoService.selectByActivityId(null));
        //valid
        String activityId = "test";
        Mockito.when(activityInfoMapper.selectByActivityId(activityId)).thenReturn(new ActivityInfo());
        assertNotNull(activityInfoService.selectByActivityId(activityId));
        Mockito.verify(activityInfoMapper).selectByActivityId(activityId);
    }

    /**
     * 插入活动详情
     */
    @Test
    public void testInsertActivityInfo() {
        Activities activities = initActivities();
        Long cmProductId = 1L;
        Long cuProductId = 2L;
        Long ctProductId = 3L;
        String cmMobileList = "18867103710,18867103825";
        String cuMobileList = "15078519097";
        String ctMobileList = "18008489236";
        String cmUserSet = "18867103710,18867103825";
        String cuUserSet = "15078519097";
        String ctUserSet = "18008489236";

        //初始化Product
        Product cmProduct = initProduct(cmProductId);
        Product cuProduct = initProduct(cuProductId);
        Product ctProduct = initProduct(ctProductId);

        Mockito.when(productService.selectProductById(cmProductId)).thenReturn(cmProduct);
        Mockito.when(productService.selectProductById(cuProductId)).thenReturn(cuProduct);
        Mockito.when(productService.selectProductById(ctProductId)).thenReturn(ctProduct);

        //初始化EntProduct
        EntProduct cmEntProduct = initEntProduct(cmProductId, activities.getEntId());
        EntProduct cuEntProduct = initEntProduct(cuProductId, activities.getEntId());
        EntProduct ctEntProduct = initEntProduct(ctProductId, activities.getEntId());

        Mockito.when(entProductService.selectByProductIDAndEnterprizeID(cmProductId, activities.getEntId())).thenReturn(cmEntProduct);
        Mockito.when(entProductService.selectByProductIDAndEnterprizeID(cuProductId, activities.getEntId())).thenReturn(cuEntProduct);
        Mockito.when(entProductService.selectByProductIDAndEnterprizeID(ctProductId, activities.getEntId())).thenReturn(ctEntProduct);

        Mockito.when(activityInfoMapper.insert(Mockito.any(ActivityInfo.class))).thenReturn(1);

        Mockito.when(productService.getFlowProduct()).thenReturn(cmProduct);
        assertTrue(activityInfoService.insertActivityInfo(activities, cmProductId, cuProductId, ctProductId,
            cmMobileList, cuMobileList, ctMobileList, cmUserSet, cuUserSet, ctUserSet));

        Mockito.verify(activityInfoMapper).insert(Mockito.any(ActivityInfo.class));

    }

    private Activities initActivities() {
        Activities activities = new Activities();
        activities.setActivityId("test");
        activities.setEntId(1L);
        return activities;
    }

    private Activities initInvalidActivities() {
        Activities activities = new Activities();
        activities.setActivityId("invalidtest");
        activities.setEntId(1L);
        return activities;
    }

    /**
     * 插入活动详情（二维码）
     */
    @Test
    public void testInsertActivityInfoForQrcode() {
        //初始化活动类
        Activities activities = initActivities();

        //初始化活动详情类
        ActivityInfo activityInfo = initActivityInfo();

        //初始化活动类
        Long cmProductId = 1L;
        Long cuProductId = 2L;
        Long ctProductId = 3L;

        Product cmProduct = initProduct(cmProductId);
        Product cuProduct = initProduct(cuProductId);
        Product ctProduct = initProduct(ctProductId);

        Mockito.when(productService.selectProductById(cmProductId)).thenReturn(cmProduct);
        Mockito.when(productService.selectProductById(cuProductId)).thenReturn(cuProduct);
        Mockito.when(productService.selectProductById(ctProductId)).thenReturn(ctProduct);

        //初始化企业产品关系类
        EntProduct cmEntProduct = initEntProduct(cmProductId, activities.getEntId());
        EntProduct cuEntProduct = initEntProduct(cuProductId, activities.getEntId());
        EntProduct ctEntProduct = initEntProduct(ctProductId, activities.getEntId());

        Mockito.when(entProductService.selectByProductIDAndEnterprizeID(cmProductId, activities.getEntId())).thenReturn(cmEntProduct);
        Mockito.when(entProductService.selectByProductIDAndEnterprizeID(cuProductId, activities.getEntId())).thenReturn(cuEntProduct);
        Mockito.when(entProductService.selectByProductIDAndEnterprizeID(ctProductId, activities.getEntId())).thenReturn(ctEntProduct);

        Mockito.when(activityInfoMapper.insert(Mockito.any(ActivityInfo.class))).thenReturn(1);
    
        Mockito.when(productService.getFlowProduct()).thenReturn(cmProduct);
        
        assertTrue(activityInfoService.insertActivityInfoForQrcode(activities, activityInfo,
            cmProductId, cuProductId, ctProductId));

        Mockito.verify(activityInfoMapper).insert(Mockito.any(ActivityInfo.class));

    }
    
    /**
     * 插入活动详情（二维码）
     */
    @Test
    public void testInsertActivityInfoForQrcode2() {
        //初始化活动类
        Activities activities = initActivities();

        //初始化活动详情类
        ActivityInfo activityInfo = initActivityInfo2();

        //初始化活动类
        Long cmProductId = 1L;
        Long cuProductId = 2L;
        Long ctProductId = 3L;

        Product cmProduct = initProduct(cmProductId);
        Product cuProduct = initProduct(cuProductId);
        Product ctProduct = initProduct(ctProductId);

        Mockito.when(productService.selectProductById(cmProductId)).thenReturn(cmProduct);
        Mockito.when(productService.selectProductById(cuProductId)).thenReturn(cuProduct);
        Mockito.when(productService.selectProductById(ctProductId)).thenReturn(ctProduct);

        //初始化企业产品关系类
        EntProduct cmEntProduct = initEntProduct(cmProductId, activities.getEntId());
        EntProduct cuEntProduct = initEntProduct(cuProductId, activities.getEntId());
        EntProduct ctEntProduct = initEntProduct(ctProductId, activities.getEntId());

        Mockito.when(entProductService.selectByProductIDAndEnterprizeID(cmProductId, activities.getEntId())).thenReturn(cmEntProduct);
        Mockito.when(entProductService.selectByProductIDAndEnterprizeID(cuProductId, activities.getEntId())).thenReturn(cuEntProduct);
        Mockito.when(entProductService.selectByProductIDAndEnterprizeID(ctProductId, activities.getEntId())).thenReturn(ctEntProduct);

        Mockito.when(activityInfoMapper.insert(Mockito.any(ActivityInfo.class))).thenReturn(1);

        Mockito.when(productService.getFlowProduct()).thenReturn(cmProduct);
        
        assertTrue(activityInfoService.insertActivityInfoForQrcode(activities, activityInfo,
            cmProductId, cuProductId, ctProductId));

        Mockito.verify(activityInfoMapper).insert(Mockito.any(ActivityInfo.class));

    }

    private EntProduct initEntProduct(Long productId, Long entId) {
        EntProduct entProduct = new EntProduct();
        if (productId.toString().equals("1")) {
            entProduct.setProductId(1L);
        } else if (productId.toString().equals("2")) {
            entProduct.setProductId(2L);
        } else if (productId.toString().equals("3")) {
            entProduct.setProductId(3L);
        }
        entProduct.setEnterprizeId(entId);
        entProduct.setDiscount(97);
        return entProduct;
    }

    private Product initProduct(Long productId) {
        Product product = new Product();
        if (productId.toString().equals("1")) {
            product.setId(1L);
            product.setFlowAccountFlag(1);
            product.setType((int)ProductType.FLOW_ACCOUNT.getValue());
        } else if (productId.toString().equals("2")) {
            product.setId(2L);
            product.setType((int)ProductType.FLOW_PACKAGE.getValue());
        } else if (productId.toString().equals("3")) {
            product.setId(3L);
            product.setType((int)ProductType.MOBILE_FEE.getValue());
        }
        product.setPrice(1000);
        product.setProductSize(1000L);
        return product;
    }

    private ActivityInfo initActivityInfo() {
        ActivityInfo activityInfo = new ActivityInfo();
        activityInfo.setPrizeCount(1L);
        return activityInfo;
    }
    
    private ActivityInfo initActivityInfo2() {
        ActivityInfo activityInfo = new ActivityInfo();
        activityInfo.setPrizeCount(10L);
        return activityInfo;
    }

    private ActivityInfo initInvalidActivityInfo() {
        ActivityInfo activityInfo = new ActivityInfo();
        activityInfo.setActivityId("invalidtest");
        activityInfo.setPrizeCount(1L);
        return activityInfo;
    }

    @Test
    public void testUpdateActivityInfoForQrcode() {

        Long cmProductId = 1L;
        Long cuProductId = 2L;
        Long ctProductId = 3L;

        Activities invalidActivities = initInvalidActivities();
        Activities activities = initActivities();

        ActivityInfo oldActivityInfo = initActivityInfo();
        ActivityInfo activityInfo = initActivityInfo();

        //invalid
        Mockito.when(activityInfoMapper.selectByActivityId(invalidActivities.getActivityId())).thenReturn(null);
        assertFalse(activityInfoService.updateActivityInfoForQrcode(invalidActivities, activityInfo,
            cmProductId, cuProductId, ctProductId));
        Mockito.verify(activityInfoMapper).selectByActivityId(invalidActivities.getActivityId());

        //valid
        //初始化活动类
        Product cmProduct = initProduct(cmProductId);
        Product cuProduct = initProduct(cuProductId);
        Product ctProduct = initProduct(ctProductId);

        Mockito.when(productService.selectProductById(cmProductId)).thenReturn(cmProduct);
        Mockito.when(productService.selectProductById(cuProductId)).thenReturn(cuProduct);
        Mockito.when(productService.selectProductById(ctProductId)).thenReturn(ctProduct);

        //初始化企业产品关系类
        EntProduct cmEntProduct = initEntProduct(cmProductId, activities.getEntId());
        EntProduct cuEntProduct = initEntProduct(cuProductId, activities.getEntId());
        EntProduct ctEntProduct = initEntProduct(ctProductId, activities.getEntId());

        Mockito.when(entProductService.selectByProductIDAndEnterprizeID(cmProductId, activities.getEntId())).thenReturn(cmEntProduct);
        Mockito.when(entProductService.selectByProductIDAndEnterprizeID(cuProductId, activities.getEntId())).thenReturn(cuEntProduct);
        Mockito.when(entProductService.selectByProductIDAndEnterprizeID(ctProductId, activities.getEntId())).thenReturn(ctEntProduct);

        Mockito.when(activityInfoMapper.updateByPrimaryKeySelective(Mockito.any(ActivityInfo.class))).thenReturn(1);
        Mockito.when(activityInfoMapper.selectByActivityId(activities.getActivityId())).thenReturn(oldActivityInfo);
        
        Mockito.when(productService.getFlowProduct()).thenReturn(cmProduct);
        
        assertTrue(activityInfoService.updateActivityInfoForQrcode(activities, activityInfo, cmProductId, cuProductId, ctProductId));

        Mockito.verify(activityInfoMapper).updateByPrimaryKeySelective(Mockito.any(ActivityInfo.class));
        Mockito.verify(activityInfoMapper).selectByActivityId(activities.getActivityId());

    }

    @Test
    public void testUpdateActivityInfo() {
        String activityId = "test";

        Long cmProductId = 1L;
        Long cuProductId = 2L;
        Long ctProductId = 3L;

        Long cmMobileCnt = 1L;
        Long cuMobileCnt = 1L;
        Long ctMobileCnt = 1L;

        Long cmUserCnt = 1L;
        Long cuUserCnt = 1L;
        Long ctUserCnt = 1L;

        //初始化活动类
        Product cmProduct = initProduct(cmProductId);
        Product cuProduct = initProduct(cuProductId);
        Product ctProduct = initProduct(ctProductId);

        Mockito.when(productService.selectProductById(cmProductId)).thenReturn(cmProduct);
        Mockito.when(productService.selectProductById(cuProductId)).thenReturn(cuProduct);
        Mockito.when(productService.selectProductById(ctProductId)).thenReturn(ctProduct);

        Mockito.when(activityInfoMapper.updateByPrimaryKeySelective(Mockito.any(ActivityInfo.class))).thenReturn(1);

        assertTrue(activityInfoService.updateActivityInfo(activityId, cmProductId, cuProductId, ctProductId,
            cmMobileCnt, cuMobileCnt, ctMobileCnt, cmUserCnt, cuUserCnt, ctUserCnt));

        Mockito.verify(activityInfoMapper).updateByPrimaryKeySelective(Mockito.any(ActivityInfo.class));

    }

    @Test
    public void testInsertForRedpacket() {
        //invalid
        assertFalse(activityInfoService.insertForRedpacket(null));

        //valid
        ActivityInfo record = initActivityInfo();
        Mockito.when(activityInfoMapper.insertForRedpacket(Mockito.any(ActivityInfo.class))).thenReturn(0).thenReturn(1);
        assertFalse(activityInfoService.insertForRedpacket(record));
        assertTrue(activityInfoService.insertForRedpacket(record));
        Mockito.verify(activityInfoMapper, Mockito.times(2)).insertForRedpacket(Mockito.any(ActivityInfo.class));
    }

    @Test
    public void testAES(){
        //String content = "";
        //String content = "test,18867103717";
        String content = "crowdfunding,18867103717";
        String password = "315eb115d98fcbad39ffc5edebd669c9";
        String appKey="crowdfunding";

        byte[] encryptResult = AES.encrypt(content.getBytes(), password.getBytes());
        System.out.println(Arrays.toString(content.toCharArray()));
        String byteArrayToString = DatatypeConverter.printHexBinary(encryptResult);
        System.out.println("加密后的字符串:" + byteArrayToString);

        //这个是接口接到信息后开始解密
        byte[] encryptArray = DatatypeConverter.parseHexBinary(byteArrayToString);
        System.out.println("加密后的字符串:" + Arrays.toString(encryptArray));

        byte[] decryptResult = null;
        // 解密
        try{
            decryptResult = AES.decrypt(encryptResult, password.getBytes());
        }catch(Exception e){
            System.out.println("解密出现异常，异常原因："+e.getMessage());
            return;
        }

        if(decryptResult == null){
            System.out.println("解密出来的信息为空。");
            return;
        }

        String result = new String(decryptResult);
        System.out.println("解密后：" + result);

        String[] resultArray = result.split(",");
        if(resultArray==null || resultArray.length!=2
            || !(resultArray[0].equals(appKey))){
            System.out.println("身份校验不通过,解密后信息result:"+ result);
            return;
        }
        String mobile = resultArray[1];
        System.out.println("获取到的手机号：" + mobile);
    }

    @Test
    public void testInsertSelective(){
        assertFalse(activityInfoService.insertSelective(null));

        Mockito.when(activityInfoMapper.insertSelective(Mockito.any(ActivityInfo.class))).thenReturn(1);
        assertTrue(activityInfoService.insertSelective(new ActivityInfo()));
    }

}
