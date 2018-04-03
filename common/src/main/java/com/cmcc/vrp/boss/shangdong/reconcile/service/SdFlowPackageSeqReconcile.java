package com.cmcc.vrp.boss.shangdong.reconcile.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.boss.shangdong.boss.model.BillDetail;
import com.cmcc.vrp.boss.shangdong.boss.model.RecordDetail;
import com.cmcc.vrp.boss.shangdong.boss.model.ServiceModel;
import com.cmcc.vrp.boss.shangdong.boss.model.UserOrder;
import com.cmcc.vrp.boss.shangdong.reconcile.BillMailService;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.dao.ChargeRecordMapper;
import com.cmcc.vrp.province.model.DiscountRecord;
import com.cmcc.vrp.province.model.Product;

/**
 * 流量包对账，使用流水号对账
 *
 */
@Service("sdFlowPackageSeqReconcile")
public class SdFlowPackageSeqReconcile extends SdFlowPackageReconcile {
    
    private static final Logger logger = LoggerFactory.
            getLogger(SdFlowPackageSeqReconcile.class);

    @Autowired
    private BillMailService billMailService;
    
    @Autowired
    private ChargeRecordMapper chargeRecordMapper;
    
    private final String errPattern = "流水号:{0}| 数据库记录：Id:{1},手机号:{2},充值产品:{3},userId:{4},状态:{5},详情:[{6}],账期:{7}| 错误信息:[{8}]";
    
    /**
     * 从数据库读取某时间段的记录，包含异步的充值流水号
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
        params.put("code", "1092");
        
        return chargeRecordMapper.sdGetReconcileSeqDatas(params);
    }
    
    /**
     * 复写getBillMapKey方法，得到的key值为pkqSeq
     */
    @Override
    public String getBillMapKey(BillDetail bill){
        return bill.getPkqSeq();
    }
    
    /**
     * 重写getChangeplusName方法
     */
    @Override
    protected String getChangeStatusFileName() {
        return "FlowPackageChangeError";
    }

    /**
     * 复写读取账单中每行。然后转化为账单对象的方法(只是增加了读流水号)
     */
    @Override
    protected BillDetail geneBillFromStr(String record){
        String[] params = record.split("\\|");
        if(params.length>=10){
            BillDetail billDetail =new BillDetail();
            billDetail.setMsisdn(params[0]);
            billDetail.setOprType(params[1]);
            billDetail.setOprTime(params[2]);
            billDetail.setEcid(params[3]);
            billDetail.setUserid(params[4]);
            billDetail.setBizid(params[5]);
            billDetail.setBeginTime(params[6]);
            billDetail.setEndTime(params[7]);
            billDetail.setPkqSeq(params[8]);
            if(StringUtils.isBlank(params[9])){
                billDetail.setCount("1");
            }else{
                billDetail.setCount(params[9]);
            }
            return billDetail;  
            
        }else{
            return null;
        }
    }
    
    /**
     * 按流水号对账的逻辑
     */
    @Override
    public List<RecordDetail> reconcileByIntervalTime(
            List<ServiceModel> recordList,
            Map<String, List<BillDetail>> billMap, String date,
            Map<String, UserOrder> userOrderMap,
            Map<String, List<DiscountRecord>> discountMap,
            Map<String, BufferedWriter> writersMap) throws IOException {
        //存放数据库比boss多的记录
        BufferedWriter changeStatusWriter = writersMap.get(changeStatusFileName);
        
        BufferedWriter changePlusWriter = writersMap.get(changePlusFileName);

        //存放boss比数据库多的记录
        BufferedWriter bossRecordsPlusWriter = writersMap.get(bossRecordsPlusName);

        //存放boss无法生成话单的记录       
        BufferedWriter bossRecordsErrorWriter = writersMap.get(bossrecordsErrorName);

        //生成所有的话单记录，按userid+bizId为map的key值进行储存
        Map<String, List<RecordDetail>> accountMap = new HashMap<String, List<RecordDetail>>();

        //1.将账单中的所有记录与更新状态后的记录状态文件做比较，如存在则记录在生成Map<String,List<RecordDetail>> ,key值为企业标识+产品标识
        int countHuadan = 0;//账单到话单的记录个数

        Map<String, Product> mapPrdsMap = getInitProductsShandong();

        for (Entry<String, List<BillDetail>> entry : billMap.entrySet()) {

            //遍历
            List<BillDetail> bills = entry.getValue();
            for (BillDetail eachDetail : bills) {
                
                //这里合适的记录是按照流水号直接匹配的，以前是按照企业+产品+时间进行匹配的
                ServiceModel serviceModel = getSuitableRecord(eachDetail, recordList);
                if (serviceModel == null) {
                    // 如果账单文件有， 数据库没有的记录，写PlusWriter
                    writeContents(bossRecordsPlusWriter, eachDetail.toRecord());
                }else{
                    //将boss账单记录与与数据库记录比较，若有错误信息则打印到错误文件中
                    String errMsg = compareBillAndDB(eachDetail, serviceModel);
                    if(StringUtils.isNotBlank(errMsg)){
                        writeContents(changeStatusWriter , getErrMsgByPattern(serviceModel,errMsg));
                    }
                }
                
                RecordDetail newRecord = geneFromBillModel(eachDetail, userOrderMap, mapPrdsMap, countHuadan,
                        discountMap);
                if (newRecord == null) {
                    //将无法生成话单的记录，写bossRecordsErrorWriter
                    writeContents(bossRecordsErrorWriter, eachDetail.toRecord());
                    continue;
                }

                //判断accountMap是否有userId和bizId为key的对象
                //1.有，则添加到list中
                //2.没有，则init一个list，将list加到map中
                if (!accountMap.containsKey(eachDetail.getUserid() + "_" + eachDetail.getBizid())) {
                    List<RecordDetail> list = new ArrayList<RecordDetail>();
                    list.add(newRecord);
                    accountMap.put(eachDetail.getUserid() + "_" + eachDetail.getBizid(), list);
                } else {
                    List<RecordDetail> list = accountMap.get(eachDetail.getUserid() + "_" + eachDetail.getBizid());
                    list.add(newRecord);
                }

                countHuadan++;
            }
        }

        //2.将剩余的recordList中的对象（没有找到账单中匹配记录），输出到文件中
        for (ServiceModel model : recordList) {
            
            String content = "";
            if (model == null || model.getProductCode() == null) {
                continue;
            }else if(model.getStatus().equals(ChargeRecordStatus.COMPLETE.getCode())){
                content = "数据库记录为充值成功,boss账单没有相关记录";            
            }else if(model.getStatus().equals(ChargeRecordStatus.PROCESSING.getCode())){
                content = "数据库记录为已发送充值请求,boss账单没有相关记录";
            }else{
                continue;
            }
            
            //将错误内容输出到文件中
            String errMsg = getErrMsgByPattern(model, content);
            if(StringUtils.isNotBlank(errMsg)){
                writeContents(changePlusWriter, errMsg);
            }
        }

        //3.遍历Map<String,List<RecordDetail>>每一条，计算每个企业对应每个产品的总价，生成话单记录。 
        logger.error("生成话单的充值记录条数为：" + countHuadan);
        return getTotalRecord(accountMap, countHuadan);
    }
    
    /**
     * 替换父类的模糊查询方法，改用流水号进行匹配
     */
    @Override
    protected ServiceModel getSuitableRecord(BillDetail eachDetail,List<ServiceModel> recordList){ 
     
        for(ServiceModel model : recordList){
            if(model.getBossReqSerialNum().equals(eachDetail.getPkqSeq())){
                eachDetail.setId(model.getRecordId());
                recordList.remove(model);  //05121420添加，删除已经完成对账的记录
                return model;
            }
        }
        
        return null;
    }
    
    /**
     * 输出错误内容到文件中，按照ERR_PATTERN的结构
     */
    private String getErrMsgByPattern(ServiceModel serviceModel,String errMsg){
        try{
            return MessageFormat.format(errPattern, 
                    serviceModel.getBossReqSerialNum(),
                    serviceModel.getRecordId().toString(),
                    serviceModel.getMobile(),
                    serviceModel.getProductCode(),
                    serviceModel.getUserId(),
                    serviceModel.getStatus(),
                    serviceModel.getErrorMessage(),
                    serviceModel.getCount(),
                    errMsg);
        }catch(IllegalArgumentException e){
            logger.error(errMsg.toString());
            return "";
        }
    }
    
    /**
     * 比较
     * (1)手机号是否一致
     * (2)企业userId
     * (3)产品
     * (4)dbModel状态是否为成功，注bill账单是重置成功的
     * (5)充值账期数目
     */
    private String compareBillAndDB(BillDetail bill,ServiceModel dbModel){
        if(!bill.getMsisdn().equals(dbModel.getMobile())){
            return "相同流水号记录，账单与数据库手机号不一致";
        }else if(!bill.getUserid().equals(dbModel.getUserId())){
            return "相同流水号记录，账单与数据库企业userId不一致";
        }else if(!bill.getBizid().equals(dbModel.getProductCode())){
            return "相同流水号记录，账单与数据库产品不一致";
        }else if(!ChargeRecordStatus.COMPLETE.getCode().equals(dbModel.getStatus())){
            return "相同流水号记录，数据库充值状态不是成功";
        }else if(dbModel.getCount()!=null && !String.valueOf(dbModel.getCount()).equals(bill.getCount())){
            return "相同流水号记录，账单与数据库充值账期数目不相同";
        }
        else{
            return "";
        }
    }
    
}
