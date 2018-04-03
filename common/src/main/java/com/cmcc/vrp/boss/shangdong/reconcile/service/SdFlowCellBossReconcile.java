package com.cmcc.vrp.boss.shangdong.reconcile.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.boss.shangdong.boss.model.BillDetail;
import com.cmcc.vrp.boss.shangdong.boss.model.RecordDetail;
import com.cmcc.vrp.boss.shangdong.boss.model.ServiceModel;
import com.cmcc.vrp.boss.shangdong.boss.model.UserOrder;
import com.cmcc.vrp.boss.shangdong.email.model.EmailType;
import com.cmcc.vrp.boss.shangdong.reconcile.BillMailService;
import com.cmcc.vrp.province.dao.ChargeRecordMapper;
import com.cmcc.vrp.province.dao.SupplierProductMapper;
import com.cmcc.vrp.province.model.DiscountRecord;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.DiscountRecordService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SdDailystatisticService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

/**
 * 流量池BOSS账单对账类,和boss对账，不出话单
 * 
 * @author qihang
 *
 */
@Service("sdFlowCellBossReconcile")
public class SdFlowCellBossReconcile extends AbstractSdFlowReconcile {
    
    private static final Logger logger = LoggerFactory.
            getLogger(SdFlowCellBossReconcile.class);
    
    @Autowired
    private ChargeRecordMapper chargeRecordMapper;
    
    @Autowired
    private GlobalConfigService globalConfigService;
    
    @Autowired
    private SupplierProductMapper supplierProductMapper;
    
    @Autowired
    private BillMailService billMailService;
    
    @Autowired
    private SdDailystatisticService sdDailystatisticService;
    
    @Autowired
    private DiscountRecordService discountService;

    @Override
    protected boolean downloadFile(String date) {
        return downloadFile(date, getSrcBillFileName(), getDestBillFileName());
    }

    @Override
    public String getSrcBillFileName() {
        return "DBOSS_4009_";
    }

    @Override
    public String getDestBillFileName() {
        return "FlowCellBoss";
    }

    /**
     * 从数据库得到某一天的1099产品搜友充值成功记录
     * recordDateMinus的格式为 2016-11-14
     * 
     */
    @Override
    public List<ServiceModel> getDailyRecordFromDB(String recordDateMinus) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//小写的mm表示的是分钟  
        String startTime= recordDateMinus+" 00:00:00"; 
        String endTime= recordDateMinus+" 23:59:59";
        
        Date startTimeDate= null;
        Date endTimeDate= null;
        try {
            startTimeDate=sdf.parse(startTime);
            endTimeDate=sdf.parse(endTime);
        } catch (ParseException e) {
            logger.error(e.getMessage());
            return new ArrayList<ServiceModel>();
        }
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("billStartTime", startTimeDate);
        params.put("billEndTime", endTimeDate);
        params.put("code", "1099");
        
        return chargeRecordMapper.sdGetReconcileDatas(params);
    }

    
    /**
     * 不计算总值，不用做
     */
    @Override
    protected RecordDetail calculTotalFromList(List<RecordDetail> list,
            int serialNum) {

        return null;
    }

    @Override
    protected String getLocalBillFilePath() {
      //return "D:\\shandongBill\\bill";
        return globalConfigService.get(GlobalConfigKeyEnum.SD_RECONCILE_FLOWBOSSBILL_PATH.getKey());
    }

    @Override
    protected String getLocalBillFileName(String date) {
        return "cloudB"+date+ "023000_0004.req";
    }

    @Override
    protected String getLocalHuadanPath() {
      //return "D:\\shandongBill\\huadan";
        return globalConfigService.get(GlobalConfigKeyEnum.SD_RECONCILE_FLOWBOSSHUADAN_PATH.getKey());
    }

    @Override
    protected String getLocalChangeRecordsPath() {
      //return "D:\\shandongBill\\changeRecords"; 
        return globalConfigService.get(
                GlobalConfigKeyEnum.SD_RECONCILE_FLOWBOSSCHANGERECORD_PATH.getKey());
    }

    @Override
    protected BillDetail geneBillFromStr(String record) {
        String[] params = record.split("\\|");
        if(params.length>=9){
            BillDetail billDetail =new BillDetail();
            billDetail.setMsisdn(params[0]);
            billDetail.setOprType(params[1]);
            billDetail.setOprTime(params[2]);
            billDetail.setEcid(params[3]);
            billDetail.setUserid(params[4]);
            billDetail.setBizid(params[5]);
            billDetail.setBeginTime(params[6]);
            billDetail.setEndTime(params[7]);
            if(params.length == 9){
                billDetail.setFlow(params[8]);
            }else{
                billDetail.setFlow(params[9]);
            }
            return billDetail;  
            
        }else{
            return null;
        }
    }

    @Override
    protected ServiceModel getSuitableRecord(BillDetail eachDetail,
            List<ServiceModel> recordList) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        
        //遍历所有数据库recordList，找出某条匹配的记录，返回该对象，并在recordList中删除该对象，
        //findIdsList添加相应ServiceModel对象的Id
        for(ServiceModel model : recordList){
            /**
             * if表示数据库中对象与账单对象1.bizid相同 2.userId相同  3.手机号相同 4.流量大小相同
             */
            if(eachDetail.getBizid().equals(model.getProductCode()) &&
                eachDetail.getUserid().equals(model.getUserId()) &&
                eachDetail.getMsisdn().equals(model.getMobile()) &&
                eachDetail.getFlow().equals(model.getSize())){
                
                Date chargeTime =model.getOperateTime();
                Date billTime;
                try {
                    billTime = dateFormat.parse(eachDetail.getOprTime());
                } catch (ParseException e) {
                    logger.error(e.getMessage());
                    return null;
                }
                
                //获取数据库里的充值时间，转化为秒为单位的Long型
                Long chargeTimeLong = chargeTime.getTime(); 
                //获取数据库里的充值时间，转化为秒为单位的Long型
                Long billTimeLong = billTime.getTime();

                //现在对账已经是24小时制的，判断账单和话单的时间是否在300秒以内
                if(Math.abs(chargeTimeLong-billTimeLong)/1000< 300 ){
                    eachDetail.setId(model.getRecordId());
                    recordList.remove(model);  //05121420添加，删除已经完成对账的记录
                    return model;
                }            
            }
        }
        return null;
    }

    /**
     * 不计算总值不用做
     */
    @Override
    protected RecordDetail geneFromBillModel(BillDetail bill,
            Map<String, UserOrder> mapUserOrders,
            Map<String, Product> mapPrdsMap, int serialNumber,
            Map<String, List<DiscountRecord>> discountMap) {

        return null;
    }
    
    @Override
    public List<RecordDetail> reconcileByIntervalTime(
            List<ServiceModel> recordList,
            Map<String, List<BillDetail>> billMap, String date,
            Map<String, UserOrder> userOrderMap,
            Map<String, List<DiscountRecord>> discountMap,
            Map<String, BufferedWriter> writersMap) throws IOException {

        //存放数据库比boss多的记录
        BufferedWriter changeStatusWriter = writersMap.get(changeStatusFileName);

        //存放boss比数据库多的记录
        BufferedWriter bossRecordsPlusWriter = writersMap.get(bossRecordsPlusName);

        
        for (Entry<String, List<BillDetail>> entry : billMap.entrySet()) {

            //遍历
            List<BillDetail> bills = entry.getValue();
            for (BillDetail eachDetail : bills) {
                ServiceModel serviceModel = getSuitableRecord(eachDetail, recordList);
                if (serviceModel == null) {
                    // 如果账单文件有， 数据库没有的记录，写PlusWriter
                    writeContents(bossRecordsPlusWriter, eachDetail.toRecord());
                }

            }
        }

        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //2.将剩余的recordList中的对象（没有找到账单中匹配记录），输出到文件中
        for (ServiceModel model : recordList) {
            if (model == null || model.getProductCode() == null) {
                continue;
            }

            writeContents(
                    changeStatusWriter,
                    "类型:" + model.getType() + "," + ",记录Id:" + model.getRecordId() + ",手机号:" + model.getMobile()
                            + ",原信息:" + model.getErrorMessage() + ",充值时间："
                            + dateTimeFormat.format(model.getOperateTime()) + ",详情:充值成功但是boss对账时没有相关记录");
        }

        return new ArrayList<RecordDetail>();
    }

    @Override
    protected String getChangeStatusFileName() {
        return "FlowCellBossChangeStatus";
    }

    @Override
    protected String getChangeplusName() {
        return "FlowCellBossChangePlus";
    }
    
    @Override
    protected String getBossRecordsPlusName() {
        return "FlowCellBossPlus";
    }

    @Override
    protected String getBossrecordsErrorName() {
        return "FlowCellBossError";
    }

    @Override
    protected boolean generateRecordFile(List<RecordDetail> detailList,
            String recordDate) {
        //不生成话单
        return true;
    }

    @Override
    protected void sendEmail(String fileNameContain,
            boolean isDaily) {
        List<String> folderList = new ArrayList<String>();
        folderList.add(getLocalBillFilePath());
        folderList.add(getLocalHuadanPath());
        folderList.add(getLocalChangeRecordsPath());
        
        if(isDaily){
            billMailService.sendEmail(fileNameContain, EmailType.DAY_EMAIL_CELL_BOSS , folderList);
        }
        
    }

    


}
