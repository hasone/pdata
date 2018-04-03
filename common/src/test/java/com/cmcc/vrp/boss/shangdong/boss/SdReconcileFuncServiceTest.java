//package com.cmcc.vrp.boss.shangdong.boss;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.io.FileUtils;
//import org.apache.commons.net.ftp.FTPClient;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Matchers;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.powermock.api.mockito.PowerMockito;
//import org.powermock.core.classloader.annotations.PrepareForTest;
//import org.powermock.modules.junit4.PowerMockRunner;
//
//import com.cmcc.vrp.boss.gansu.HttpUtil;
//import com.cmcc.vrp.boss.shangdong.boss.model.BillDetail;
//import com.cmcc.vrp.boss.shangdong.boss.model.BillStatisticModel;
//import com.cmcc.vrp.boss.shangdong.boss.model.HuadanStatisticModel;
//import com.cmcc.vrp.boss.shangdong.boss.model.RecordDetail;
//import com.cmcc.vrp.boss.shangdong.boss.model.ServiceModel;
//import com.cmcc.vrp.boss.shangdong.boss.model.UserOrder;
//import com.cmcc.vrp.boss.shangdong.reconcile.SdReconcileFuncService;
//import com.cmcc.vrp.boss.shangdong.reconcile.SdReconcileFuncServiceImpl;
//import com.cmcc.vrp.province.dao.ChargeRecordMapper;
//import com.cmcc.vrp.province.dao.SupplierProductMapper;
//import com.cmcc.vrp.province.model.DiscountRecord;
//import com.cmcc.vrp.province.service.DiscountRecordService;
//import com.cmcc.vrp.province.service.GlobalConfigService;
//import com.cmcc.vrp.util.FTPUtil;
//import com.cmcc.vrp.util.GlobalConfigKeyEnum;
//import com.cmcc.vrp.util.UncompressFileGZIP;
//
///**
// * 
// * @author qihang
// *
// */
//@PrepareForTest({FTPClient.class,FTPUtil.class,UncompressFileGZIP.class,
//    FileUtils.class,HttpUtil.class,File.class,SdReconcileFuncServiceImpl.class,
//    BufferedWriter.class,FileOutputStream.class,OutputStreamWriter.class,InputStreamReader.class,
//    BufferedReader.class,FileInputStream.class})
//@RunWith(PowerMockRunner.class)
//public class SdReconcileFuncServiceTest {
//    
//    @InjectMocks
//    SdReconcileFuncService sdReconcileFuncService = new SdReconcileFuncServiceImpl();
//    
//    @Mock
//    GlobalConfigService globalConfigService;
//    
//    @Mock
//    ChargeRecordMapper chargeRecordMapper;
//    
//    @Mock
//    SupplierProductMapper supplierProductMapper;
//    
//    @Mock
//    DiscountRecordService discountService;
//    
//    /**
//     * mock初始化数据库的参数
//     */
//    @Before
//    public void init(){
//
//        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.
//            SD_FTP_URL.getKey())).
//            thenReturn("127.0.0.1");
//        
//        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.
//            SD_FTP_LOGINNAME.getKey())).
//            thenReturn("qihang");
//        
//        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.
//            SD_FTP_LOGINPASS.getKey())).
//            thenReturn("xiaoqi160");
//        
//        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.
//            SD_FTP_FILEPATH.getKey())).
//            thenReturn("aaa");
//        
//        PowerMockito.when(globalConfigService.get(
//            GlobalConfigKeyEnum.
//            SD_RECONCILE_BILL_PATH.getKey())).
//            thenReturn("/shandongBill/bill");
//        
//        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.
//            SD_RECONCILE_HUADAN_PATH.getKey())).
//            thenReturn("/shandongBill/huadan");
//        
//        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.
//            SD_RECONCILE_CHANGERECORD_PATH.getKey())).
//            thenReturn("/shandongBill/changeRecords");
//        
//        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.
//            SD_RECONCILE_NOTICE_URL.getKey())).
//            thenReturn("127.0.0.1");
//        
//        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.
//            SD_RECONCILE_EMAIL.getKey())).
//            thenReturn("OK");
//        
//        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.
//                SD_CLOUD_CHARGE_URL.getKey())).
//                thenReturn("http://211.137.190.207:8089/sdkService/restWs/llpt/updateProductMemberFlowSystemPay");
//        
//        PowerMockito.when(supplierProductMapper.sdGetAllUserOrders())
//            .thenReturn(initUserOrders());
//        
//    }
//    
//    /**
//     * 初始化3个userOrder对象
//     * @return
//     */
//    private List<UserOrder> initUserOrders(){
//        List<UserOrder> list = new ArrayList<UserOrder>();
//        UserOrder userOrder1= new UserOrder();
//        userOrder1.setDiscount("10");
//        userOrder1.setBizId("109202");
//        userOrder1.setCustomerID("12345678");
//        userOrder1.setUserID("0001");
//        
//        UserOrder userOrder2= new UserOrder();
//        userOrder2.setDiscount("10");
//        userOrder2.setBizId("109203");
//        userOrder2.setCustomerID("12345678");
//        userOrder2.setUserID("0001");
//        
//        UserOrder userOrder3= new UserOrder();
//        userOrder3.setDiscount("10");
//        userOrder3.setBizId("109202");
//        userOrder3.setCustomerID("12345678");
//        userOrder3.setUserID("0002");
//        
//        list.add(userOrder1);
//        list.add(userOrder2);
//        list.add(userOrder3);
//        
//        return list;
//    }
//    
//    /**
//     * testDownloadFile
//     * @throws Exception 
//     */
//    @Test
//    public void testDownloadFile() throws Exception {
//        String date = "2016-11-11";
//        
//        PowerMockito.mockStatic(FTPUtil.class);
//        PowerMockito.mockStatic(UncompressFileGZIP.class);
//        PowerMockito.mockStatic(FileUtils.class);
//        
//        //1.全部成功
//        PowerMockito.when(FTPUtil.downFileFuzzyName(Matchers.anyString(), Matchers.anyInt(), 
//                Matchers.anyString(), Matchers.anyString(), Matchers.anyString(), 
//                Matchers.anyString(), Matchers.anyString())).
//            thenReturn("not null");
//        
//        PowerMockito.when(UncompressFileGZIP.doUncompressFile(Matchers.anyString())).
//            thenReturn(true);
//        
//        PowerMockito.doNothing().when(FileUtils.class, "moveFile", Mockito.any(File.class),Mockito.any(File.class));
//        Assert.assertTrue(sdReconcileFuncService.downloadFile(date));
//        
//        //2.下载失败
//        PowerMockito.when(FTPUtil.downFileFuzzyName(Matchers.anyString(), Matchers.anyInt(), 
//                Matchers.anyString(), Matchers.anyString(), Matchers.anyString(), 
//                Matchers.anyString(), Matchers.anyString())).
//            thenReturn("");
//        Assert.assertFalse(sdReconcileFuncService.downloadFile(date));
//        
//        
//        //3.解压失败
//        PowerMockito.when(FTPUtil.downFileFuzzyName(Matchers.anyString(), Matchers.anyInt(), 
//                Matchers.anyString(), Matchers.anyString(), Matchers.anyString(), 
//                Matchers.anyString(), Matchers.anyString())).
//            thenReturn("not null");
//        
//        PowerMockito.when(UncompressFileGZIP.doUncompressFile(Matchers.anyString())).
//            thenReturn(false);
//        Assert.assertFalse(sdReconcileFuncService.downloadFile(date));
//        
//        //4.重命名异常
//        PowerMockito.when(FTPUtil.downFileFuzzyName(Matchers.anyString(), Matchers.anyInt(), 
//                Matchers.anyString(), Matchers.anyString(), Matchers.anyString(), 
//                Matchers.anyString(), Matchers.anyString())).
//            thenReturn("not null");
//        
//        PowerMockito.when(UncompressFileGZIP.doUncompressFile(Matchers.anyString())).
//            thenReturn(true);
//        
//        PowerMockito.doThrow(new IOException()).
//            when(FileUtils.class, "moveFile", Mockito.any(File.class),Mockito.any(File.class));
//        Assert.assertFalse(sdReconcileFuncService.downloadFile(date));
//         
//    }
//
//    /**
//     * testUploadHuadan
//     * @throws Exception 
//     */
//    @Test
//    public void testUploadHuadanCorrect() throws Exception{
//        String date = "2016-11-11";
//        
//        PowerMockito.mockStatic(FTPUtil.class);
//        PowerMockito.mockStatic(UncompressFileGZIP.class);
//        PowerMockito.mockStatic(FileUtils.class);
//        PowerMockito.mockStatic(HttpUtil.class);
//
//        
//        FileInputStream inputStream =PowerMockito.mock(FileInputStream.class);  
//        File file =  PowerMockito.mock(File.class);
//        
//       
//        PowerMockito.whenNew(File.class).withArguments(Matchers.anyString()).thenReturn(file);
//        PowerMockito.whenNew(FileInputStream.class).withAnyArguments().thenReturn(inputStream);
//       
//        //测试正确上传
//        PowerMockito.when(FTPUtil.uploadFile(Matchers.anyString(), 
//                Matchers.anyInt(), Matchers.anyString(), Matchers.anyString(), Matchers.anyString(), 
//                Matchers.anyString(), Matchers.any(InputStream.class))).thenReturn(true);
//        
//        
//        PowerMockito.when(HttpUtil.class,"doGet", Mockito.any(String.class),Mockito.any(String.class),
//                Mockito.any(String.class),Mockito.any(Boolean.class)).thenReturn("any string");
//       
//        
//        Assert.assertTrue(sdReconcileFuncService.uploadHuadan(date));
//        
//        
//    }
//    
//    /**
//     * testUploadHuadanFileNotFound
//     */
//    @Test
//    public void testUploadHuadanFileNotFound() throws Exception{
//        String date = "2016-11-11";
//        
//        PowerMockito.mockStatic(FTPUtil.class);
//        PowerMockito.mockStatic(UncompressFileGZIP.class);
//        PowerMockito.mockStatic(FileUtils.class);
//        PowerMockito.mockStatic(HttpUtil.class);
//        
//        PowerMockito.when(FTPUtil.uploadFile(Matchers.anyString(), 
//                Matchers.anyInt(), Matchers.anyString(), Matchers.anyString(), Matchers.anyString(), 
//                Matchers.anyString(), Matchers.any(InputStream.class))).thenReturn(true);
//        
//        PowerMockito.when(HttpUtil.class,"doGet", Mockito.any(String.class),Mockito.any(String.class),
//                Mockito.any(String.class),Mockito.any(Boolean.class)).thenReturn("any string");
//        
//        Assert.assertFalse(sdReconcileFuncService.uploadHuadan(date));
//    }
//    
//    /**
//     * testAnalyseBill
//     * @throws Exception 
//     */
//    @Test
//    public void testAnalyseBill() throws Exception{
//        String[] records = new String[3];
//        records[0] = "17854057616|00|20161020143122||5338029470434|109206|20161020143117|20161110000000";
//        records[1] = "17854057616|00|20161020143123||5338029470434|109206|20161020143117|20161110000000";
//        records[2] = "18769922138|00|20161020143209||5338029470434|109202|20161020143200|20161023000000";
//        
//        File file =  PowerMockito.mock(File.class);
//        BufferedReader bufferedReader = PowerMockito.mock(BufferedReader.class);  
//        FileInputStream fileInputStream = PowerMockito.mock(FileInputStream.class);
//        InputStreamReader inputStreamReader = PowerMockito.mock(InputStreamReader.class);
//
//        
//        PowerMockito.whenNew(File.class).withAnyArguments().thenReturn(file);
//        PowerMockito.whenNew(FileInputStream.class).
//            withAnyArguments().thenReturn(fileInputStream);
//        PowerMockito.whenNew(InputStreamReader.class).
//            withAnyArguments().thenReturn(inputStreamReader);
//        PowerMockito.whenNew(BufferedReader.class).withAnyArguments().thenReturn(bufferedReader);
//
//        
//        //设置读取到账单的每行数据为record[0],record[1],record[2],null
//        PowerMockito.when(bufferedReader.readLine()).thenReturn(records[0], records[1],records[2],null);
//        
//        Map<String, List<BillDetail>> map =sdReconcileFuncService.
//                analyseBill("20161021","20161020",new HashMap<String, List<DiscountRecord>>());
//        Assert.assertTrue(map.containsKey("17854057616_5338029470434_109206"));
//        Assert.assertTrue(map.containsKey("18769922138_5338029470434_109202"));
//        
//        List<BillDetail> list1 = map.get("17854057616_5338029470434_109206");
//        List<BillDetail> list2 = map.get("18769922138_5338029470434_109202");
//        
//        Assert.assertEquals(list1.size(), 2);
//        Assert.assertEquals(list2.size(), 1);
//        
//        //以下是测试异常状态
//        //1.捕获IOException
//        PowerMockito.doThrow(new IOException()).
//            when(bufferedReader,"readLine");
//        map = sdReconcileFuncService.analyseBill("20161021","20161020",new HashMap<String, List<DiscountRecord>>());
//        Assert.assertTrue(map.isEmpty());
//        
//    }
//    
//    @Test
//    public void getDailyRecordFromDB(){
//        ServiceModel model = new ServiceModel();
//        model.setRecordId(1L);
//        
//        List<ServiceModel> list= new ArrayList<ServiceModel>();
//        list.add(model);
//        
//        PowerMockito.when(chargeRecordMapper.sdGetReconcileDatas(Matchers.anyMap())).thenReturn(list);
//        
//        //1.测试正确
//        List<ServiceModel> resultList =sdReconcileFuncService.getDailyRecordFromDB("2016-11-14");
//        
//        Assert.assertEquals(resultList.size(), list.size());
//        
//        //2.测试日期格式错误
//        resultList =sdReconcileFuncService.getDailyRecordFromDB("20161114");
//        Assert.assertTrue(resultList.isEmpty());
//    }
//    
//    /**
//     * testReconcileByIntervalTime
//     * @throws Exception
//     */
//    @Test
//    public void testReconcileByIntervalTime() throws Exception{
//        String date = "20161020";
//                
//        //存放数据库比boss多的记录
//        BufferedWriter changeStatusWriter = PowerMockito.mock(BufferedWriter.class);
//        //存放boss比数据库多的记录
//        BufferedWriter bossRecordsPlusWriter = PowerMockito.mock(BufferedWriter.class);
//        //存放boss无法生成话单的记录       
//        BufferedWriter bossRecordsErrorWriter = PowerMockito.mock(BufferedWriter.class);
//        
//        Map<String, BufferedWriter> writerMap = new HashMap<String, BufferedWriter>();
//        writerMap.put("changeRecord", changeStatusWriter);
//        writerMap.put("bossPlus", bossRecordsPlusWriter);
//        writerMap.put("bossError", bossRecordsErrorWriter);
//        
//        PowerMockito.doNothing().when(bossRecordsPlusWriter).write(Matchers.anyString());
//        PowerMockito.doNothing().when(bossRecordsPlusWriter).flush();
//        
//        PowerMockito.doNothing().when(bossRecordsErrorWriter).write(Matchers.anyString());
//        PowerMockito.doNothing().when(bossRecordsErrorWriter).flush();
//
//        
//        Map<String, UserOrder> userOrderMap = sdReconcileFuncService.getAllUserOrdersToMap();
//        
//        
//        Map<String, List<BillDetail>> billMap = initTestBills();
//        List<ServiceModel> serviceModels = initDBModels();
//        
//        List<RecordDetail> details=sdReconcileFuncService.
//            reconcileByIntervalTime(serviceModels, billMap, date,userOrderMap,
//                    new HashMap<String, List<DiscountRecord>>(),writerMap);
//        Assert.assertEquals(details.size(), 6);
//    }
//    
//    /**
//     * testUploadDailyHuadan
//     * @throws Exception 
//     */
//    @Test
//    public void testUploadDailyHuadan() throws Exception{
//        PowerMockito.mockStatic(FTPUtil.class);
//        PowerMockito.mockStatic(HttpUtil.class);
//        
//        PowerMockito.when(FTPUtil.uploadFile(Matchers.anyString(),Matchers.anyInt(),
//                Matchers.anyString(),Matchers.anyString(),
//                Matchers.anyString(),Matchers.anyString(),
//                Matchers.any(FileInputStream.class)))
//            .thenReturn(true);
//        PowerMockito.when(HttpUtil.doGet(Matchers.anyString(), 
//                Matchers.anyString(), Matchers.anyString(),
//                Matchers.anyBoolean())).thenReturn("");
//        
//        File anyFile =  PowerMockito.mock(File.class);
//        FileInputStream anyFis = PowerMockito.mock(FileInputStream.class);
//        
//        PowerMockito.whenNew(File.class)
//            .withAnyArguments()
//            .thenReturn(anyFile);
//        PowerMockito.whenNew(FileInputStream.class)
//            .withAnyArguments()
//            .thenReturn(anyFis);
//        PowerMockito.doNothing().when(anyFis).close();
//        
//        //测试上传正确
//        Assert.assertTrue(sdReconcileFuncService.uploadDailyHuadan("2016-10-24"));
//        
//        //测试上传错误
//        PowerMockito.when(FTPUtil.uploadFile(Matchers.anyString(),Matchers.anyInt(),
//                Matchers.anyString(),Matchers.anyString(),
//                Matchers.anyString(),Matchers.anyString(),
//                Matchers.any(FileInputStream.class)))
//            .thenReturn(false);
//        Assert.assertFalse(sdReconcileFuncService.uploadDailyHuadan("2016-10-24"));
//        
//    }
//    
//    /**
//     * testStatisticDB
//     */
//    @Test
//    public void testStatisticDB(){
//        List<ServiceModel> models = initForStatisticDB();
//        BillStatisticModel result =sdReconcileFuncService.statisticDB(models);
//        
//        Assert.assertEquals(result.getSuccessCount(), 3);
//        Assert.assertEquals(result.getDiscountPrize(), new Double(24.0d));
//        Assert.assertEquals(result.getOriginalPrize(), new Double(25.0d)); 
//    }
//    
//    /**
//     * testStatisticHuadanFile
//     * @throws Exception 
//     */
//    @Test
//    public void testStatisticHuadanFile() throws Exception{
//
//        FileInputStream fis = PowerMockito.mock(FileInputStream.class);
//        InputStreamReader isr = PowerMockito.mock(InputStreamReader.class);
//        BufferedReader br = PowerMockito.mock(BufferedReader.class);
//        
//        PowerMockito.whenNew(FileInputStream.class).withAnyArguments().thenReturn(fis);
//        PowerMockito.whenNew(InputStreamReader.class).withAnyArguments().thenReturn(isr);
//        PowerMockito.whenNew(BufferedReader.class).withAnyArguments().thenReturn(br);
//        
//        //PowerMockito.when(br.readLine()).thenReturn(, values)
//        
//        String[] huadan = new String[4];
//        huadan[0] = "2016071502305417122873|1|6328023841961|6328024651000||0|109201|0102001|01|20160714105300|20160801000000|||||1|3.00|2.10|0|";
//        huadan[1] = "2016071502302507667706|1|6328023841961|6328024651000|13589076444|0|109201|100104|03|20160714105300|20160801000000||||||3.00|2.10|0|";
//        huadan[2] = "2016061804300976917532|1|5358031499882|5358031601914||0|109210|0102001|01|20160616091455|20160705120000|||||1|10.00|10.00|0|";
//        huadan[3] = "2016061804300466675988|1|5358031499882|5358031601914|18365968350|0|109210|100104|03|20160616091455|20160705120000||||||10.00|10.00|0|";
//        
//        PowerMockito.when(br.readLine()).
//            thenReturn(huadan[0], huadan[1],huadan[2],huadan[3],null);
//        
//        HuadanStatisticModel model = sdReconcileFuncService.statisticHuadanFile("20160101");
//        
//        Assert.assertEquals(model.getDiscountPrize01(), model.getDiscountPrize03());
//        Assert.assertEquals(model.getOriginalPrize01(), model.getOriginalPrize03());
//    }
//    
//    /**
//     * testStatisticDataBill
//     * @throws Exception 
//     */
//    @Test
//    public void testStatisticDataBill() throws Exception{
//        FileInputStream fis = PowerMockito.mock(FileInputStream.class);
//        InputStreamReader isr = PowerMockito.mock(InputStreamReader.class);
//        BufferedReader br = PowerMockito.mock(BufferedReader.class);
//        
//        PowerMockito.whenNew(FileInputStream.class).withAnyArguments().thenReturn(fis);
//        PowerMockito.whenNew(InputStreamReader.class).withAnyArguments().thenReturn(isr);
//        PowerMockito.whenNew(BufferedReader.class).withAnyArguments().thenReturn(br);
//        
//        String[] bill = new String[4];
//        bill[0]="15053182801|00|20160823015317||0001|109203|20160823015259|20160914000000";
//        bill[1]="15263171913|00|20160823015350||0001|109202|20160823015332|20160912000000";
//        bill[2]="13573712171|00|20160823015410||0002|109202|20160823015350|20160901000000";
//        bill[3]="15966093385|00|20160823015443||0001|109202|20160823015424|20160829000000";
//        
//        PowerMockito.when(br.readLine()).
//            thenReturn(bill[0], bill[1],bill[2],bill[3],null);
//        
//        BillStatisticModel result = sdReconcileFuncService.statisticDataBill("20160101");
//        
//        System.out.println(result.getSuccessCount());
//        Assert.assertEquals(result.getSuccessCount(), 4);
//    }
//    
//    private Map<String, List<BillDetail>> initTestBills() throws Exception{
//        String[] records = new String[5];
//        records[0] = "18800001111|00|20161020143000||0001|109202|20161020143117|20161110000000";
//        records[1] = "18800001111|00|20161020143100||0001|109202|20161020143117|20161110000000";
//        records[2] = "18800002222|00|20161020143000||0001|109203|20161020143117|20161110000000";
//        records[3] = "18800003333|00|20161020143100||0001|109202|20161020143117|20161110000000";
//        records[4] = "18800003333|00|20161020143100||0005|109202|20161020143117|20161110000000";
//        
//        File file =  PowerMockito.mock(File.class);
//        BufferedReader bufferedReader = PowerMockito.mock(BufferedReader.class);  
//        FileInputStream fileInputStream = PowerMockito.mock(FileInputStream.class);
//        InputStreamReader inputStreamReader = PowerMockito.mock(InputStreamReader.class);
//
//        
//        PowerMockito.whenNew(File.class).withAnyArguments().thenReturn(file);
//        PowerMockito.whenNew(FileInputStream.class).
//            withAnyArguments().thenReturn(fileInputStream);
//        PowerMockito.whenNew(InputStreamReader.class).
//            withAnyArguments().thenReturn(inputStreamReader);
//        PowerMockito.whenNew(BufferedReader.class).withAnyArguments().thenReturn(bufferedReader);
//        
//        //设置读取到账单的每行数据为record[0],record[1],record[2],null
//        PowerMockito.when(bufferedReader.readLine()).thenReturn(
//                records[0], records[1],records[2],records[3],records[4],null);
//        
//        Map<String, List<BillDetail>> map =sdReconcileFuncService.analyseBill("20161021","20161020",new HashMap<String, List<DiscountRecord>>());
//        return map;
//    }
//    
//   
//    private List<ServiceModel> initDBModels(){
//        List<ServiceModel> list = new ArrayList<ServiceModel>();
//        try {
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
//            ServiceModel model1 = new ServiceModel();
//            model1.setProductCode("109202");
//            model1.setBossEnterpriseId("12345678");
//            model1.setUserId("0001");
//            model1.setRecordId(1L);
//            model1.setMobile("18800001111");
//            model1.setPrice(500);
//            model1.setOrderDiscount(10);
//            model1.setOperateTime(dateFormat.parse("20161020143000"));
//
//            ServiceModel model2 = new ServiceModel();
//            model2.setProductCode("109203");
//            model2.setBossEnterpriseId("12345678");
//            model2.setUserId("0001");
//            model2.setRecordId(1L);
//            model2.setMobile("18800002222");
//            model2.setOperateTime(dateFormat.parse("20161020143000"));
//            model2.setPrice(1000);
//            model2.setOrderDiscount(10);
//            
//            list.add(model1);
//            list.add(model2);
//            
//            
//        } catch (ParseException e) {
//            return list;
//        }
//        
//        return list;
//    }
//    
//    private List<ServiceModel> initForStatisticDB(){
//        List<ServiceModel> list = new ArrayList<ServiceModel>();
//        try {
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
//            ServiceModel model1 = new ServiceModel();
//            model1.setProductCode("109202");
//            model1.setBossEnterpriseId("12345678");
//            model1.setUserId("0001");
//            model1.setRecordId(1L);
//            model1.setMobile("18800001111");
//            model1.setPrice(500);
//            model1.setOrderDiscount(8);            
//            model1.setOperateTime(dateFormat.parse("20161020143000"));
//
//            
//            ServiceModel model2 = new ServiceModel();
//            model2.setProductCode("109203");
//            model2.setBossEnterpriseId("12345678");
//            model2.setUserId("0001");
//            model2.setRecordId(1L);
//            model2.setMobile("18800002222");
//            model2.setOperateTime(dateFormat.parse("20161020143000"));
//            model2.setPrice(1000);
//            model2.setOrderDiscount(10);
//            
//            ServiceModel model3 = new ServiceModel();
//            model3.setProductCode("109203");
//            model3.setBossEnterpriseId("12345678");
//            model3.setUserId("0001");
//            model3.setRecordId(1L);
//            model3.setMobile("18800003333");
//            model3.setOperateTime(dateFormat.parse("20161020143000"));
//            model3.setPrice(1000);
//            model3.setOrderDiscount(10);
//            
//            
//            list.add(model1);
//            list.add(model2);
//            list.add(model3);
//            
//            
//        } catch (ParseException e) {
//            return list;
//        }
//        
//        return list;
//    }
//}
