package com.cmcc.vrp.province.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.enums.MdrcBatchConfigStatus;
import com.cmcc.vrp.enums.NoticeMsgStatus;
import com.cmcc.vrp.province.dao.MdrcCardmakeDetailMapper;
import com.cmcc.vrp.province.mdrc.enums.MdrcCardStatus;
import com.cmcc.vrp.province.mdrc.service.MdrcBatchConfigService;
import com.cmcc.vrp.province.mdrc.service.MdrcBatchConfigStatusRecordService;
import com.cmcc.vrp.province.mdrc.service.MdrcCardInfoService;
import com.cmcc.vrp.province.mdrc.service.MdrcCardmakerService;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.MdrcBatchConfigInfo;
import com.cmcc.vrp.province.model.MdrcBatchConfig;
import com.cmcc.vrp.province.model.MdrcBatchConfigStatusRecord;
import com.cmcc.vrp.province.model.MdrcCardInfo;
import com.cmcc.vrp.province.model.MdrcCardmakeDetail;
import com.cmcc.vrp.province.model.MdrcCardmaker;
import com.cmcc.vrp.province.model.MdrcMakecardRequestConfig;
import com.cmcc.vrp.province.quartz.jobs.BatchConfigExpireJob;
import com.cmcc.vrp.province.quartz.service.ScheduleService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.CardNumAndPwdService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.MdrcBatchConfigInfoService;
import com.cmcc.vrp.province.service.MdrcCardmakeDetailService;
import com.cmcc.vrp.province.service.MdrcMakecardRequestConfigService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.SmsPojo;
import com.cmcc.vrp.queue.task.MdrcMakeCardWorker;
import com.cmcc.vrp.util.Constants.DELETE_FLAG;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

/**
 * Created by qinqinyan on 2016/11/30.
 */
@Service("mdrcCardmakeDetailService")
public class MdrcCardmakeDetailServiceImpl implements MdrcCardmakeDetailService {
    private static final Logger logger = LoggerFactory.getLogger(MdrcMakeCardWorker.class);

    @Autowired
    MdrcCardmakeDetailMapper mapper;
    @Autowired
    MdrcBatchConfigService mdrcBatchConfigService;
    @Autowired
    MdrcCardmakerService mdrcCardmakerService;
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    ManagerService managerService;
    @Autowired
    MdrcCardInfoService mdrcCardInfoService;
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    AdministerService administerService;
    @Autowired
    TaskProducer taskProducer;
    @Autowired
    MdrcMakecardRequestConfigService mdrcMakecardRequestConfigService;
    @Autowired
    CardNumAndPwdService mdrcCardNumAndPwdService;
    @Autowired
    MdrcBatchConfigInfoService mdrcBatchConfigInfoService;
    @Autowired
    MdrcBatchConfigStatusRecordService mdrcBatchConfigStatusRecordService;

    @Override
    public boolean deleteByPrimaryKey(Long id) {
        if(id!=null){
            return mapper.deleteByPrimaryKey(id)==1;
        }
        return false;
    }

    @Override
    public boolean insert(MdrcCardmakeDetail record) {
        if(record!=null){
            return mapper.insert(record)==1;
        }
        return false;
    }

    @Override
    public boolean insertSelective(MdrcCardmakeDetail record) {
        if(record!=null){
            return mapper.insertSelective(record)==1;
        }
        return false;
    }

    @Override
    public MdrcCardmakeDetail selectByPrimaryKey(Long id) {
        if(id!=null){
            return mapper.selectByPrimaryKey(id);
        }
        return null;
    }

    @Override
    public boolean updateByPrimaryKeySelective(MdrcCardmakeDetail record) {
        if(record!=null){
            return mapper.updateByPrimaryKeySelective(record)==1;
        }
        return false;
    }

    @Override
    public boolean updateByPrimaryKey(MdrcCardmakeDetail record) {
        if(record!=null){
            return mapper.updateByPrimaryKey(record)==1;
        }
        return false;
    }

    @Override
    public MdrcCardmakeDetail selectByRequestId(Long requestId) {
        if(requestId!=null){
            return mapper.selectByRequestId(requestId);
        }
        return null;
    }

    public String getProvinceCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.MDRC_PROVINCE_CODE.getKey());
    }


    private MdrcBatchConfig createMdrcBatchConfig(MdrcCardmakeDetail mdrcCardmakeDetail, Long adminId){
        MdrcBatchConfig mdrcBatchConfig = new MdrcBatchConfig();

        Manager manager = managerService.getManagerByAdminId(adminId);
        mdrcBatchConfig.setManagerId(manager.getId());

        mdrcBatchConfig.setConfigName(mdrcCardmakeDetail.getConfigName());
        mdrcBatchConfig.setCardmakerId(mdrcCardmakeDetail.getCardmakerId());
        mdrcBatchConfig.setTemplateId(mdrcCardmakeDetail.getTemplateId());// 设置模板
        mdrcBatchConfig.setAmount(mdrcCardmakeDetail.getAmount());
        mdrcBatchConfig.setProvinceCode(getProvinceCode());//省份编码

        mdrcBatchConfig.setNotiFlag(NoticeMsgStatus.NO.getCode());// 设置默认值，未下发通知短信

        mdrcBatchConfig.setCreateTime(new Date());// 设置创建时间
        mdrcBatchConfig.setCreatorId(adminId);// 设置创建者ID
        mdrcBatchConfig.setStatus(MdrcBatchConfigStatus.NOT_DOWNLOAD.getCode());// 设置规则状态：未下载

        mdrcBatchConfig.setEnterpriseId(mdrcCardmakeDetail.getEnterpriseId());
        mdrcBatchConfig.setProductId(mdrcCardmakeDetail.getProductId());
        mdrcBatchConfig.setConfigInfoId(mdrcCardmakeDetail.getConfigInfoId());
        
        mdrcBatchConfig.setEffectiveTime(mdrcCardmakeDetail.getStartTime());
        mdrcBatchConfig.setExpiryTime(mdrcCardmakeDetail.getEndTime());
        
        return mdrcBatchConfig;
    }

    private List<MdrcCardInfo> buildCardInfos(MdrcBatchConfig config, Date startTime, Date deadline, String cardMakerSerialNum) {
        long count = config.getAmount();

        List<String> cardNumberList =  mdrcCardNumAndPwdService.generatCardNums(config);

        List<MdrcCardInfo> cardInfos = new ArrayList<MdrcCardInfo>((int) count);
        for (int i = 0; i < count; i++) {
            MdrcCardInfo cardInfo = new MdrcCardInfo();

            String cardNum = cardNumberList.get(i);
            cardInfo.setCardNumber(cardNum);// 设置卡序列号
            cardInfo.setConfigId(config.getId());// 设置规则ID
            cardInfo.setStatus(MdrcCardStatus.NEW.getCode());// 设置卡状态，1：新制卡
            cardInfo.setCreateTime(new Date());// 创建时间
            cardInfo.setStartTime(startTime);//生效日期
            cardInfo.setDeadline(deadline);// 失效日期
            cardInfo.setOpStatus(MdrcCardStatus.NORMAL.getCode());

            cardInfos.add(cardInfo);
        }

        return cardInfos;
    }

    private String getQuartzId(MdrcBatchConfig mbc) {
        return mbc.getId() + mbc.getProvinceCode() + mbc.getThisYear() + "_" + System.currentTimeMillis();
    }

    @Override
    @Transactional
    public boolean makecard(Long requestId, Long adminId) {
        MdrcCardmakeDetail mdrcCardmakeDetail = selectByRequestId(requestId);

        // 查询制卡商
        MdrcCardmaker mdrcCardmaker = mdrcCardmakerService.selectByPrimaryKey(mdrcCardmakeDetail.getCardmakerId());
        
        MdrcBatchConfig m = createMdrcBatchConfig(mdrcCardmakeDetail, adminId);

        // 获得当前年份
        DateTime dateTime = new DateTime();
        String year = String.valueOf(dateTime.getYear());
        m.setThisYear(year);


        // 根据年份和省份编码查询配置规则
        List<MdrcBatchConfig> list = mdrcBatchConfigService.selectByYearAndProvinceCode(m.getThisYear(), m.getProvinceCode());
        MdrcBatchConfig mbc = null;
        if (list != null && list.size() != 0) {
            mbc = list.get(0);// 获取批次号最大的配置规则
        }

        // 如果年份和省份编码均相同，则批次号在原有基础上进行累加
        if (mbc != null && m.getThisYear().equals(mbc.getThisYear())
            && m.getProvinceCode().equals(mbc.getProvinceCode())) {
            m.setSerialNumber(mbc.getSerialNumber() + 1);
        } else {
            m.setSerialNumber(1);// 批次号，重新从1开始
        }

        // 创建记录
        if(mdrcBatchConfigService.insertSelective(m)<1){
            logger.info("插入营销卡卡规则记录失败，requestId = {}", requestId);
            return false;
        }
        //mdrcBatchConfigService.insertSelective(m)
        
        MdrcBatchConfigStatusRecord record = new MdrcBatchConfigStatusRecord();
        record.setConfigId(m.getId());
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        record.setPreStatus(m.getStatus());
        record.setNowStatus(m.getStatus());
        record.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());
        mdrcBatchConfigStatusRecordService.insertSelective(record);

        //批量插入卡
        if(mdrcCardInfoService.batchInsert(dateTime.getYearOfCentury(), buildCardInfos(m, mdrcCardmakeDetail.getStartTime(),
                mdrcCardmakeDetail.getEndTime(), mdrcCardmaker.getSerialNumber()))<1){
            logger.info("插入营销卡卡记录详情失败，requestId = {}", requestId);
            throw new RuntimeException();
        }
        //mdrcCardInfoService.batchInsert(dateTime.getYearOfCentury(), buildCardInfos(m, mdrcCardmakeDetail.getStartTime(),
        //    mdrcCardmakeDetail.getEndTime(), mdrcCardmaker.getSerialNumber()));

        //插入申请请求和卡数据关系表
        MdrcMakecardRequestConfig mdrcMakecardRequestConfig = createMdrcMakecardRequestConfig(m.getId(), requestId);
        if(!mdrcMakecardRequestConfigService.insertSelective(mdrcMakecardRequestConfig)){
            logger.info("插入申请请求和卡数据关系表失败，requestId = {}, configId = {}", requestId, mdrcMakecardRequestConfig.getConfigId());
            throw new RuntimeException();
        }
        //mdrcMakecardRequestConfigService.insertSelective(mdrcMakecardRequestConfig);

        MdrcCardmakeDetail updateRecord = new MdrcCardmakeDetail();
        updateRecord.setRequestId(requestId);
        updateRecord.setCardmakeStatus(1);
        if(!updateByPrimaryKeySelective(updateRecord)){
            logger.info("更新该请求为已制卡失败，requestId = {}", requestId);
            throw new RuntimeException();
        }
        //updateByPrimaryKeySelective(updateRecord);
        
        //创建过期时的定时任务
        logger.info("创建卡批次过期时的定时任务...");
        scheduleService.createScheduleJob(BatchConfigExpireJob.class, "BatchConfigExpireTask",
            String.valueOf(m.getId()), getQuartzId(m), mdrcCardmakeDetail.getEndTime());

        logger.info("发送短信通知采购人员");
        List<Administer> administers = administerService.queryAllUsersByAuthName("ROLE_MDRC_DATADL_CAIGOU");
        String content = m.getId() + "批次的流量卡数据已生成，请及时下载！";
        for (Administer admin:administers) {
            SmsPojo sms = new SmsPojo(admin.getMobilePhone(), content, null, null, null);
            logger.info("手机号:" + sms.getMobile() + "短信内容:" + sms.getContent());
            taskProducer.produceDeliverNoticeSmsMsg(sms);
        }
        return true;
    }

    private MdrcMakecardRequestConfig createMdrcMakecardRequestConfig(Long configId, Long requestId){
        MdrcMakecardRequestConfig record = new MdrcMakecardRequestConfig();
        record.setConfigId(configId);
        record.setRequestId(requestId);
        record.setCreateTime(new Date());
        record.setDeleteFlag(0);
        return record;
    }

    @Override
    @Transactional
    public boolean updateTrackingNumber(MdrcCardmakeDetail mdrcCardmakeDetail, MdrcBatchConfig mdrcBatchConfig) {
        // TODO Auto-generated method stub
        if(!updateByPrimaryKeySelective(mdrcCardmakeDetail)){
            logger.info("更新物流单号失败。mdrcCardmakeDetail = {}", JSON.toJSONString(mdrcCardmakeDetail));
            return false;
        }
        MdrcBatchConfig orgMdrcBatchConfig = mdrcBatchConfigService.select(mdrcBatchConfig.getId());
        if(orgMdrcBatchConfig.getStatus().toString().equals(MdrcBatchConfigStatus.DOWNLOADED.getCode().toString())){
            
            //插入变更记录
            MdrcBatchConfigStatusRecord record = new MdrcBatchConfigStatusRecord();
            record.setConfigId(mdrcBatchConfig.getId());
            record.setCreateTime(new Date());
            record.setUpdateTime(new Date());
            record.setPreStatus(mdrcBatchConfig.getStatus());
            
            mdrcBatchConfig.setStatus(MdrcBatchConfigStatus.POST.getCode());
            if(!mdrcBatchConfigService.update(mdrcBatchConfig)){
                logger.info("更新该批次卡信息失败. config = {}", mdrcBatchConfig.getId());
                throw new RuntimeException();
            }
            
            //填充物流单号
            MdrcBatchConfigInfo mdrcBatchConfigInfo =  mdrcBatchConfigInfoService.selectByPrimaryKey(orgMdrcBatchConfig.getConfigInfoId());
            if(mdrcBatchConfigInfo != null){
                mdrcBatchConfigInfo.setExpressNumber(mdrcCardmakeDetail.getTrackingNumber());
                if(!mdrcBatchConfigInfoService.updateByPrimaryKeySelective(mdrcBatchConfigInfo)){
                    logger.info("更新该批次物流单号失败. config = {}， MdrcBatchConfigInfo ={}", mdrcBatchConfig.getId(), mdrcBatchConfigInfo);
                }                
            }else{
                logger.info("更新该批次物流单号失败. config = {},扩展信息为空", mdrcBatchConfig.getId());
            }
            
            record.setNowStatus(mdrcBatchConfig.getStatus());
            record.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());
            mdrcBatchConfigStatusRecordService.insertSelective(record);
            
        }else{
            logger.info("该批次卡状态不是制卡中状态，因此填写物流信息失败. status = {}, config = {}", orgMdrcBatchConfig.getStatus(), mdrcBatchConfig.getId());
            throw new RuntimeException();
        }
        return true;
    }
    
    
}
