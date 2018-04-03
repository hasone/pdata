package com.cmcc.vrp.boss.shangdong.reconcile.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.math.NumberUtils;
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
import com.cmcc.vrp.province.model.PoolUsedStatistic;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.quartz.service.ScheduleService;
import com.cmcc.vrp.province.service.DiscountRecordService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.PoolUsedStatisticService;
import com.cmcc.vrp.province.service.SdDailystatisticService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

/**
 * 流量池对账类
 * 
 * @author panxin
 *
 */
@Service("sdFlowCellReconcile")
public class SdFlowCellReconcile extends AbstractSdFlowReconcile {
    private static final Logger logger = LoggerFactory
            .getLogger(SdFlowCellReconcile.class);

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

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private PoolUsedStatisticService poolUsedStatisticService;

    /**
     * 需要下载的boss账单文件的前缀名
     */
    @Override
    public String getSrcBillFileName() {
        return "DBOSS_5009_";
    }

    @Override
    public String getDestBillFileName() {
        return "FlowCell";
    }

    /**
     * 从数据库得到某一天的1099产品搜友充值成功记录
     * recordDateMinus的格式为 2016-11-14
     * 
     */
    @Override
    public List<ServiceModel> getDailyRecordFromDB(String recordDateMinus) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//小写的mm表示的是分钟  
        String startTime= DateUtil.getMonthOfBeforeDay(recordDateMinus, 2)+" 00:00:00"; 
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
        
        return chargeRecordMapper.sdGetFlowReconcileDatas(params);
    }

    /**
     * 得到本地账单的存放路径
     */
    @Override
    protected String getLocalBillFilePath() {
        // return "D:\\shandongFlow\\bill";
        return globalConfigService
                .get(GlobalConfigKeyEnum.SD_RECONCILE_FLOWBILL_PATH.getKey());
    }

    /**
     * 得到本地生成对账后文件的路径
     */
    @Override
    protected String getLocalChangeRecordsPath() {
        // return "D:\\shandongFlow\\changeRecords";
        return globalConfigService
                .get(GlobalConfigKeyEnum.SD_RECONCILE_FLOWCHANGERECORD_PATH
                        .getKey());
    }

    /**
     * 得到本地话单的存放路径
     */
    @Override
    protected String getLocalHuadanPath() {
        // return "D:\\shandongFlow\\huadan";
        return globalConfigService
                .get(GlobalConfigKeyEnum.SD_RECONCILE_FLOWHUADAN_PATH.getKey());
    }

    /**
     * record :账单的基本格式 格式：成员手机号|00|最后一次操作时间||集团用户ID（产品实例id）
     * |产品规格（增值产品标示）|开始时间|结束时间|PkgSeq
     * 样例：13605339630|00|20160328071529||5338028221371
     * |109206|20160328071805|20160401120000
     */
    @Override
    protected BillDetail geneBillFromStr(String record) {
        String[] params = record.split("\\|");
        if (params.length >= 8) {
            BillDetail billDetail = new BillDetail();
            billDetail.setMsisdn(params[1]);
            billDetail.setOprType("");
            billDetail.setOprTime(params[2]);
            billDetail.setEcid("");
            billDetail.setUserid(params[3]);
            billDetail.setBizid(params[4]);
            billDetail.setBeginTime(params[5]);
            billDetail.setEndTime(params[6]);
            billDetail.setFlow(params[7]);
            return billDetail;

        } else {
            return null;
        }
    }

    @Override
    protected String getLocalBillFileName(String date) {
        return "cloudB" + date + "023000_0005.req";
    }

    /**
     * 给出一个账单对象eachDetail，在所有的数据库对象List<ServiceModel>中找出一个充值时间匹配的的记录，并返回，若没有则返回空
     */
    @Override
    protected ServiceModel getSuitableRecord(BillDetail eachDetail,
            List<ServiceModel> recordList) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        // 遍历所有数据库recordList，找出某条匹配的记录，返回该对象，并在recordList中删除该对象，
        // findIdsList添加相应ServiceModel对象的Id
        for (ServiceModel model : recordList) {
            /**
             * if表示数据库中对象与账单对象1.bizid相同 2.userId相同 3.手机号相同
             */
            if (eachDetail.getBizid().equals(model.getProductCode())
                    && eachDetail.getUserid().equals(model.getUserId())
                    && eachDetail.getMsisdn().equals(model.getMobile())) {

                Date chargeTime = model.getOperateTime();
                Date billTime;
                try {
                    billTime = dateFormat.parse(eachDetail.getOprTime());
                } catch (ParseException e) {
                    logger.error(e.getMessage());
                    return null;
                }

                // 获取数据库里的充值时间，转化为秒为单位的Long型
                Long chargeTimeLong = chargeTime.getTime();
                // 获取数据库里的充值时间，转化为秒为单位的Long型
                Long billTimeLong = billTime.getTime();

                // 现在对账已经是24小时制的，判断账单和话单的时间是否在300秒以内
                if (Math.abs(chargeTimeLong - billTimeLong) / 1000 < 300) {
                    eachDetail.setId(model.getRecordId());
                    recordList.remove(model); // 05121420添加，删除已经完成对账的记录
                    return model;
                }
            }
        }
        return null;
    }

    /**
     * 从账单和数据库找出的记录中，合并生成话单的基本信息
     * 0826新增，使用在账单中的记录，在discountMap表中查询当天符合时间的折扣，若找到则使用该折扣
     * ，没有的话使用数据库userorder里的折扣 TODO 等待罗祖武的折扣功能
     */
    @Override
    protected RecordDetail geneFromBillModel(BillDetail bill,
            Map<String, UserOrder> mapUserOrders,
            Map<String, Product> mapPrdsMap, int serialNumber,
            Map<String, List<DiscountRecord>> discountMap) {
        UserOrder userOrder = mapUserOrders.get(bill.getUserid() + "_"
                + bill.getBizid());
        if (userOrder == null) {
            return null;
        }

        RecordDetail detail = initRecordDetail(bill, userOrder, serialNumber);
        detail.setPayType(0);
        return detail;
    }

    private RecordDetail initRecordDetail(BillDetail bill, UserOrder userOrder,
            int serialNumber) {
        RecordDetail detail = new RecordDetail();

        // 生成序列号yyyyMMddHHmmss+8位数字
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String transactionTime = dateFormat.format(new Date());

        // 20160228003436 30895648
        detail.setSerialNumber(transactionTime
                + String.format("%8d", serialNumber).replace(" ", "0"));
        detail.setCustomerType(1);
        detail.setCustomerID(userOrder.getCustomerID());
        detail.setUserID(bill.getUserid());
        detail.setMemberPhone(bill.getMsisdn());
        detail.setServiceID(bill.getBizid());
        detail.setMeasureMode("100104");
        detail.setChargingType("03");
        detail.setOrderDate(bill.getBeginTime());
        detail.setExpireDate(bill.getEndTime());
        detail.setBeginTime("");
        detail.setEndTime("");
        detail.setDuration("");
        detail.setUsageAmount(bill.getFlow());
        detail.setOrderQuantity("");

        return detail;
    }

    @Override
    protected boolean downloadFile(String date) {
        return downloadFile(date, getSrcBillFileName(), getDestBillFileName());
    }

    @Override
    protected RecordDetail calculTotalFromList(List<RecordDetail> list,
            int serialNum) {
        if (list == null || list.isEmpty()) {
            return null;
        }

        // 同一天的同一企业，同一产品，很多字段是固定的，因此只要取第一个记录填充
        RecordDetail newDetail = new RecordDetail();
        RecordDetail firstDetail = list.get(0);

        // 生成序列号yyyyMMddHHmmss+8位数字
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String transactionTime = dateFormat.format(new Date());
        // newDetail.setSerialNumber(transactionTime+VerifycodeUtil.getRandomNum(8));
        newDetail.setSerialNumber(transactionTime
                + String.format("%8d", serialNum).replace(" ", "0"));
        newDetail.setCustomerType(1);
        newDetail.setCustomerID(firstDetail.getCustomerID());
        newDetail.setUserID(firstDetail.getUserID());
        newDetail.setMemberPhone("");
        newDetail.setSiid("0");
        newDetail.setServiceID(firstDetail.getServiceID());
        newDetail.setChargingType("01");
        newDetail.setOrderDate(firstDetail.getOrderDate());
        newDetail.setExpireDate(firstDetail.getExpireDate());
        newDetail.setBeginTime("");
        newDetail.setEndTime("");
        newDetail.setDuration("");
        newDetail.setOrderQuantity(String.valueOf(list.size()));
        newDetail.setOriginalPrice("");
        newDetail.setPreferentialPrice("");
        newDetail.setPayType(0);

        // 计算充值总额和打折后的总额
        int usageAmount = 0;

        for (RecordDetail recordDetail : list) {
            usageAmount += NumberUtils.toInt(recordDetail.getUsageAmount());
        }

        newDetail.setUsageAmount(String.valueOf(usageAmount));
        return newDetail;
    }

    /**
     * @param recordList
     *            数据库中当天的所有记录
     * @param billMap
     *            账单中的记录，key为 手机+userid+bizid
     * @param date
     *            对账的日期
     */
    @Override
    public List<RecordDetail> reconcileByIntervalTime(
            List<ServiceModel> recordList,
            Map<String, List<BillDetail>> billMap, String date,
            Map<String, UserOrder> userOrderMap,
            Map<String, List<DiscountRecord>> discountMap,
            Map<String, BufferedWriter> writersMap) throws IOException {
        // 存放数据库比boss多的记录,池对账不需要使用
        // BufferedWriter changeStatusWriter =
        // writersMap.get(changeStatusFileName);

        // 存放boss比数据库多的记录
        BufferedWriter bossRecordsPlusWriter = writersMap
                .get(bossRecordsPlusName);

        // 存放boss无法生成话单的记录
        BufferedWriter bossRecordsErrorWriter = writersMap
                .get(bossrecordsErrorName);

        // 生成所有的话单记录，按userid+bizId为map的key值进行储存
        Map<String, List<RecordDetail>> accountMap = new HashMap<String, List<RecordDetail>>();

        // 步骤1.将所有的账单记录，生成话单记录，按分组储存在Map<String, List<RecordDetail>> accountMap中
        int countHuadan = 0;// 账单到话单的记录个数

        Map<String, Product> mapPrdsMap = getInitProductsShandong();

        for (Entry<String, List<BillDetail>> entry : billMap.entrySet()) {

            // 遍历
            List<BillDetail> bills = entry.getValue();
            for (BillDetail eachDetail : bills) {

                RecordDetail newRecord = geneFromBillModel(eachDetail,
                        userOrderMap, mapPrdsMap, countHuadan, discountMap);
                if (newRecord == null) {
                    writeContents(bossRecordsErrorWriter, eachDetail.toRecord());
                    continue;
                }

                // 判断accountMap是否有userId和bizId为key的对象
                // 1.有，则添加到list中
                // 2.没有，则init一个list，将list加到map中
                if (!accountMap.containsKey(eachDetail.getUserid() + "_"
                        + eachDetail.getBizid())) {
                    List<RecordDetail> list = new ArrayList<RecordDetail>();
                    list.add(newRecord);
                    accountMap.put(
                            eachDetail.getUserid() + "_"
                                    + eachDetail.getBizid(), list);
                } else {
                    List<RecordDetail> list = accountMap.get(eachDetail
                            .getUserid() + "_" + eachDetail.getBizid());
                    list.add(newRecord);
                }

                countHuadan++;
            }
        }

        // 步骤2.重新遍历原始账单记录，计算出总额，插入到数据库中
        analyseCellRecord(billMap, date);

        // 步骤3.从数据库得到总上一账期到对账数据时间的充值记录
        String startTime = DateUtil.getMonthOfBeforeDay(date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8), 2);
        Map<String, Long> usedFlowMap = poolUsedStatisticService
                .getUsedFlowByTime(startTime, date.replaceAll("-", ""));

        // 步骤4.将账单和数据库中的记录比较，进行总额对账
        totalFeeCompare(usedFlowMap, recordList, bossRecordsPlusWriter);

        // 步骤5.遍历Map<String,List<RecordDetail>>每一条，计算每个企业对应每个产品的总价，生成话单记录。
        logger.error("生成话单的充值记录条数为：" + countHuadan);
        return getTotalRecord(accountMap, countHuadan);
    }

    /**
     * 步骤2.重新遍历原始账单记录，计算出总额，插入到数据库中 (1) 删除数据库中当天的所有统计记录 (2)
     * 遍历所有的账单记录，计算出当天的总值，插入到数据库中
     */
    private boolean analyseCellRecord(Map<String, List<BillDetail>> billMap,
            String date) {
        String dateFormat = date.replaceAll("-", "");

        // (1) 删除数据库中当天的所有统计记录
        poolUsedStatisticService.deleteByDate(dateFormat);

        // (2) 遍历所有的账单记录，计算出当天的总值，插入到数据库中
        // 账单的key为 bill.getMsisdn()+"_"+ bill.getUserid()+ "_"+bill.getBizid()
        for (Entry<String, List<BillDetail>> entry : billMap.entrySet()) {

            PoolUsedStatistic poolUsedStatistic = new PoolUsedStatistic();
            poolUsedStatistic.setDate(dateFormat); // 设置日期

            // 记录当天已使用流量值
            long usedAmount = 0L;

            // 遍历list
            List<BillDetail> bills = entry.getValue();
            for (BillDetail eachDetail : bills) {
                usedAmount += NumberUtils.toLong(eachDetail.getFlow());
                poolUsedStatistic.setEnterCode(eachDetail.getUserid());
                poolUsedStatistic.setMobile(eachDetail.getMsisdn());
                poolUsedStatistic.setProductCode(eachDetail.getBizid());
            }

            // 设置总额
            poolUsedStatistic.setUsedAmount(usedAmount);
            // 插入数据库
            poolUsedStatisticService.insertSelective(poolUsedStatistic);
        }
        return true;
    }

    /**
     * 步骤4.将账单和数据库中的记录比较，进行总额对账
     */
    private boolean totalFeeCompare(Map<String, Long> usedFlowMap,
            List<ServiceModel> dbList, BufferedWriter bossRecordsPlusWriter) {
        for (ServiceModel serviceModel : dbList) {
            String key = serviceModel.getMobile() + "_" + serviceModel.getUserId() + "_" + serviceModel.getProductCode();
            if (usedFlowMap.containsKey(key)){
                long dbFlow = Long.parseLong(serviceModel.getSize());
                long usedFlow = usedFlowMap.get(key);
                if (usedFlow > dbFlow){
                    writeContents(bossRecordsPlusWriter, serviceModel.getMobile() + "|" + serviceModel.getUserId()
                            + "|" + serviceModel.getProductCode() + "|" + usedFlow + "|" + dbFlow);
                }
                usedFlowMap.remove(key);
            }
        }
        
        for (Entry<String, Long> entry : usedFlowMap.entrySet()) {
            writeContents(bossRecordsPlusWriter, entry.getKey().replace("_", "|") + "|" + entry.getValue() + "|" + "0");
        }
        return true;
    }

    @Override
    protected String getChangeStatusFileName() {
        return "FlowCellChangeStatus";
    }
    
    @Override
    protected String getChangeplusName() {
        return "FlowCellChangePlus";
    }

    @Override
    protected String getBossRecordsPlusName() {
        return "FlowCellPlus";
    }

    @Override
    protected String getBossrecordsErrorName() {
        return "FlowCellError";
    }

    @Override
    protected boolean generateRecordFile(List<RecordDetail> detailList,
            String recordDate) {
      //将文件名中去掉-
        String date = recordDate.replaceAll("-", "");

        //"023000_0003.req"
        String outFileName = getLocalBillFileName(date);

        BufferedWriter writer = null;
        try {
            File wfile = new File(getLocalHuadanPath() + File.separator + outFileName);
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(wfile), "utf-8"));

            for (RecordDetail detail : detailList) {
                writer.write(detail.toBillRecord() + "\r\n");
                writer.flush();
            }
            return true;
        } catch (IOException e) {
            logger.error("生成话单文件出现异常:" + e.getMessage());
            return false;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }
    }

    @Override
    protected void sendEmail(String fileNameContain,boolean isDaily) {
        List<String> folderList = new ArrayList<String>();
        folderList.add(getLocalBillFilePath());
        folderList.add(getLocalChangeRecordsPath());
        folderList.add(getLocalHuadanPath());
        
        if(isDaily){
            billMailService.sendEmail(fileNameContain, EmailType.DAY_EMAIL_CELL , folderList);
        }
        
    }

    
}
