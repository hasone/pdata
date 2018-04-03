package com.cmcc.vrp.province.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmcc.vrp.charge.ChargeResult;
import com.cmcc.vrp.ec.bean.CallBackReq;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ActivityWinRecordStatus;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.dao.ChargeRecordMapper;
import com.cmcc.vrp.province.dao.DistrictMapper;
import com.cmcc.vrp.province.mdrc.service.MdrcCardInfoService;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.InterfaceRecord;
import com.cmcc.vrp.province.model.PresentRecord;
import com.cmcc.vrp.province.model.statement.FingerprintStat;
import com.cmcc.vrp.province.module.ChargeStatisticLineModule;
import com.cmcc.vrp.province.module.ChargeStatisticListModule;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.InterfaceRecordService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.MonthlyPresentRecordService;
import com.cmcc.vrp.province.service.MonthlyPresentRuleService;
import com.cmcc.vrp.province.service.PresentRecordService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;
import com.google.gson.Gson;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @date 2016年12月2日 下午3:51:12
 */
@Service("chargeRecordService")
public class ChargeRecordServiceImpl implements ChargeRecordService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChargeRecordServiceImpl.class);

    @Autowired
    DistrictMapper districtMapper;

    @Autowired
    ChargeRecordMapper chargeRecordMapper;

    @Autowired
    EnterprisesService enterpriseService;

    @Autowired
    ManagerService managerService;

    @Autowired
    InterfaceRecordService interfaceRecordService;

    @Autowired
    PresentRecordService presentRecordService;

    @Autowired
    MonthlyPresentRecordService monthlyPresentRecordService;

    @Autowired
    ActivityWinRecordService activityWinRecordService;

    @Autowired
    MonthlyPresentRuleService monthlyPresentRuleService;

    @Autowired
    MdrcCardInfoService mdrcCardInfoService;
    
    @Autowired
    GlobalConfigService globalConfigService;

    @Override
    public List<ChargeStatisticListModule> statisticChargeList(QueryObject queryObject) {
        Map map = queryObject.toMap();
        if (map.get("needPage") != null) {
            map.put("pageNum", 0);
            map.put("pageSize", 0);
        }
        String endTime = (String) map.get("endTime");
        if (!StringUtils.isEmpty(endTime)) {
            endTime = endTime + " 23:59:59";
            map.put("endTime", endTime);
        }
        String managerId = (String) map.get("managerId");
        List<Long> enterIds = enterpriseService.getEnterIdByManagerId(Long.parseLong(managerId));
        if (enterIds != null && enterIds.size() > 0) {
            map.put("enterIds", enterIds);
        } else {
            return new ArrayList<ChargeStatisticListModule>();
        }

        String type = (String) map.get("type");
        if (!StringUtils.isEmpty(type)) {
            String[] typeArray = type.split(",");
            Integer[] typeList = new Integer[typeArray.length];
            for (int i = 0; i < typeArray.length; i++) {
                typeList[i] = Integer.parseInt(typeArray[i]);
            }
            map.put("typeList", typeList);
        }
        
        //增加山东产品筛选
        String sdPrdType = (String) map.get("sdPrdType");
        if("1099".equals(sdPrdType)){
            map.put("sdFlowName", "109901-");
        }else if("1087".equals(sdPrdType) ||
                 "1092".equals(sdPrdType) ||
                 "1105".equals(sdPrdType)){
            map.put("sdPackageName", sdPrdType);
        }

        if(isSdEnvironment()){
            return chargeRecordMapper.sdstatisticChargeList(map);
        }
        return chargeRecordMapper.statisticChargeList(map);
    }

    @Override
    public int statisticChargeListCount(QueryObject queryObject) {
        Map map = queryObject.toMap();
        String endTime = (String) map.get("endTime");
        if (!StringUtils.isEmpty(endTime)) {
            endTime = endTime + " 23:59:59";
            map.put("endTime", endTime);
        }
        String managerId = (String) map.get("managerId");
        List<Long> enterIds = enterpriseService.getEnterIdByManagerId(Long.parseLong(managerId));
        if (enterIds != null && enterIds.size() > 0) {
            map.put("enterIds", enterIds);
        } else {
            return 0;
        }

        String type = (String) map.get("type");
        if (!StringUtils.isEmpty(type)) {
            String[] typeArray = type.split(",");
            Integer[] typeList = new Integer[typeArray.length];
            for (int i = 0; i < typeArray.length; i++) {
                typeList[i] = Integer.parseInt(typeArray[i]);
            }
            map.put("typeList", typeList);
        }
        
        //增加山东产品筛选
        String sdPrdType = (String) map.get("sdPrdType");
        if("1099".equals(sdPrdType)){
            map.put("sdFlowName", "109901-");
        }else if("1087".equals(sdPrdType) ||
                 "1092".equals(sdPrdType) ||
                 "1105".equals(sdPrdType)){
            map.put("sdPackageName", sdPrdType);
        }
        
        return chargeRecordMapper.statisticChargeListCount(map);
    }

    @Override
    public List<ChargeStatisticLineModule> statistictByChargeDay(QueryObject queryObject) {
        Map map = queryObject.toMap();
        String endTime = (String) map.get("endTime");
        if (!StringUtils.isEmpty(endTime)) {
            endTime = endTime + " 23:59:59";
            map.put("endTime", endTime);
        }

        String managerId = (String) map.get("managerId");
        List<Long> managerIds = managerService.getSonTreeIdByManageId(Long.parseLong(managerId));
        map.put("managerIds", managerIds);

        return chargeRecordMapper.selectByChargeDay(map);
    }

    @Override
    public boolean create(ChargeRecord chargeRecord) {
        if (validate(chargeRecord)) {
            if (chargeRecord.getCount() == null) {
                chargeRecord.setCount(1);
            }
            return chargeRecordMapper.insertSelective(chargeRecord) == 1;
        }
        return false;

    }

    @Override
    public boolean batchInsert(List<ChargeRecord> records) {
        if (records == null || records.isEmpty()) {
            return false;
        }

        for (ChargeRecord record : records) {
            if (!validate(record)) {
                return false;
            } else if (record.getCount() == null) {
                record.setCount(1);
            }
        }

        return chargeRecordMapper.batchInsert(records) == records.size();
    }

    @Override
    public boolean updateStatus(Long id, ChargeRecordStatus status, String errorMsg) {
        if (id == null || status == null) {
            LOGGER.error("无效的记录ID或状态参数. Id = {}， Status = {}.", id, status == null ? null : status.getCode());
            return false;
        }

        ChargeRecord chargeRecord = chargeRecordMapper.get(id);
        if (chargeRecord == null) {
            LOGGER.error("无法找到相应的充值记录. Id = {}.", id);
            return false;
        }

        int statusCode = status.getCode();
        Long recordId = chargeRecord.getRecordId();
        ActivityType activityType = ActivityType.fromValue(chargeRecord.getTypeCode());
        activityType.setStatusCode(status.getStatusCode());
        return chargeRecordMapper.updateStatusAndStatusCode(id, status.getStatusCode(), statusCode, errorMsg,
                status.getFinanceStatus(), status.getUpdateChargeTime()) == 1 //更新总表状态
                && updateActivityRecord(activityType, recordId, statusCode, errorMsg, chargeRecord.getPhone()); //更新分表状态
    }

    @Override
    public boolean updateStatus(Long id, ChargeResult chargeResult) {
        if (id == null || chargeResult == null || chargeResult.getCode() == null) {
            LOGGER.error("无效的充值状态参数，Id = {}, ChargeResult = {}.", id,
                    chargeResult == null ? "空" : new Gson().toJson(chargeResult));
            return false;
        }

        ChargeRecordStatus chargeStatus = null;
        String errorMsg = null;

        //注意这里的成功有两种情况，一种是同步的成功，一种是异步的成功， 要分开处理！
        if (chargeResult.isSuccess()) {
            if (chargeResult.getCode().equals(ChargeResult.ChargeResultCode.SUCCESS)) {
                chargeStatus = ChargeRecordStatus.COMPLETE;
                errorMsg = ChargeRecordStatus.COMPLETE.getMessage();
            } else {
                chargeStatus = ChargeRecordStatus.PROCESSING;
                errorMsg = chargeResult.getFailureReason();
            }
        } else {
            chargeStatus = ChargeRecordStatus.FAILED;
            errorMsg = StringUtils.isBlank(chargeResult.getFailureReason()) ? ChargeRecordStatus.FAILED.getMessage()
                    : chargeResult.getFailureReason();
        }
        chargeStatus.setStatusCode(chargeResult.getStatusCode());
        chargeStatus.setFinanceStatus(chargeResult.getFinanceStatus());
        chargeStatus.setUpdateChargeTime(chargeResult.getUpdateChargeTime());
        return updateStatus(id, chargeStatus, errorMsg);
    }

    //更新活动记录
    private boolean updateActivityRecord(ActivityType activityType, Long recordId, int status, String errorMsg,
            String phone) {
        if (activityType == null) {
            LOGGER.error("活动类型不能为NULL.");
            return false;
        }

        ActivityWinRecordStatus activityWinRecordStatus = ActivityWinRecordStatus.fromValue(status);
        activityWinRecordStatus.setStatusCode(activityType.getStatusCode());
        boolean flag = false;
        switch (activityType) {
            case INTERFACE:
                flag = interfaceRecordService.updateActivityStatus(recordId, activityWinRecordStatus, errorMsg);
                break;
            case GIVE:
                flag = presentRecordService.updateActivityStatus(recordId, activityWinRecordStatus, errorMsg);
                break;
            case MONTHLY_PRESENT:
                flag = monthlyPresentRecordService.updateActivityStatus(recordId, activityWinRecordStatus, errorMsg);
                if (ChargeRecordStatus.COMPLETE.getCode().intValue() == status
                        || ChargeRecordStatus.PROCESSING.getCode().intValue() == status
                        || ChargeRecordStatus.FAILED.getCode().intValue() == status) {
                    //更新包月赠送为已完成状态
                    monthlyPresentRuleService.updateRuleStatusFini();
                }
                break;
            case REDPACKET:
            case LOTTERY:
            case GOLDENBALL:
            case FLOWCARD:
            case QRCODE:
            case CROWD_FUNDING:
            case LUCKY_REDPACKET:
                flag = activityWinRecordService.updateActivityStatus(recordId, activityWinRecordStatus, errorMsg, phone);
                break;
            default:
                LOGGER.error("无法匹配相应的活动类型，请检查是否配置了相应的活动代码！");
                flag = false;
                break;
        }

        return flag;
    }

    private boolean validate(CallBackReq cbq) {
        return cbq != null && cbq.getCallBackReqData() != null
                && StringUtils.isNotBlank(cbq.getCallBackReqData().getEcSerialNum())
                && StringUtils.isNotBlank(cbq.getCallBackReqData().getSystemNum())
                && StringUtils.isNotBlank(cbq.getCallBackReqData().getMobile())
                && cbq.getCallBackReqData().getStatus() != null;
    }

    @Override
    public boolean batchUpdateStatus(List<ChargeRecord> records) {
        if (records == null || records.isEmpty()) {
            return false;
        }

        for (ChargeRecord record : records) {
            if (!validateStatus(record)) {
                return false;
            }
        }

        return chargeRecordMapper.batchUpdateStatus(records, records.get(0).getUpdateChargeTime(), records.get(0)
                .getFinanceStatus()) == records.size();
    }

    private boolean validateStatus(ChargeRecord chargeRecord) {
        return chargeRecord != null && chargeRecord.getStatus() != null && chargeRecord.getId() != null;
    }

    private boolean validate(ChargeRecord chargeRecord) {
        return chargeRecord != null
                && chargeRecord.getPrdId() != null
                && chargeRecord.getEnterId() != null
                && chargeRecord.getRecordId() != null
                //                && chargeRecord.getChargeTime() != null
                && StringUtils.isNotBlank(chargeRecord.getaName()) && chargeRecord.getTypeCode() != null
                && chargeRecord.getStatus() != null
                && com.cmcc.vrp.util.StringUtils.isValidMobile(chargeRecord.getPhone())
                && StringUtils.isNotBlank(chargeRecord.getType());
        //&& chargeRecord.getEffectType() != null;
    }

    @Override
    public boolean updateByRecordId(Long recordId, Integer status, String errMsg) {
        if (recordId == null || status == null) {
            return false;
        }
        return chargeRecordMapper.updateByRecordId(recordId, status, errMsg) == 1;
    }

    @Override
    public boolean updateBySystemNum(String systemNum, Integer status, String errMsg, Integer financeStatus,
            Date updateChargeTime) {
        if (StringUtils.isBlank(systemNum) || status == null) {
            return false;
        }
        return chargeRecordMapper.updateBySystemNum(systemNum, status, errMsg, financeStatus, updateChargeTime) == 1;
    }

    @Override
    public ChargeRecord getRecordBySN(String systemNum) {
        return StringUtils.isBlank(systemNum) ? null : chargeRecordMapper.selectRecordBySN(systemNum);
    }

    @Override
    public List<ChargeRecord> getRecords(Long entId, Integer pageOff, Integer pageSize) {
        if (entId == null || pageOff == null || pageSize == null) {
            return null;
        }
        return chargeRecordMapper.selectRecords(entId, pageOff, pageSize);
    }

    @Override
    public boolean updateByPrimaryKeySelective(ChargeRecord record) {
        return chargeRecordMapper.updateByPrimaryKeySelective(record) == 1;
    }

    @Override
    public boolean updateByTypeCodeAndRecordId(Map map) {
        return chargeRecordMapper.updateByTypeCodeAndRecordId(map) == 1;
    }

    @Override
    public List<FingerprintStat> statement(DateTime start, DateTime end, Long entId) {
        if (start == null || end == null || start.isAfter(end)) {
            LOGGER.error("无效的查询参数. StartTime = {}， EndTime = {}， EntId = {}.", start, end, entId);
            return null;
        }

        return chargeRecordMapper.statement(start.toDate(), end.toDate(), entId);
    }

    @Override
    public List<ChargeRecord> selectRecordByEnterIdAndSerialNum(Long enterId, String serialNum) {
        if (enterId == null || StringUtils.isBlank(serialNum)) {
            return null;
        }
        return chargeRecordMapper.selectRecordByEnterIdAndSerialNum(enterId, serialNum);
    }

    @Override
    public boolean updateStatusCode(Long id, String statusCode) {
        if (id != null) {
            return chargeRecordMapper.updateStatusCode(id, statusCode) >= 0;
        }
        return false;
    }

    @Override
    public boolean updateStatusCodeBySystemNum(String systemNum, String statusCode) {
        if (systemNum != null && !StringUtils.isEmpty(systemNum)) {
            return chargeRecordMapper.updateStatusCodeBySystemNum(systemNum, statusCode) >= 0;
        }
        return false;
    }

    @Override
    public List<ChargeRecord> batchSelectBySystemNum(List<String> systemNums) {
        return systemNums == null ? null : chargeRecordMapper.batchSelectBySystemNum(systemNums);
    }

    @Override
    public boolean updateStatusAndStatusCode(Long recordId, String statusCode, Integer status, String errorMsg,
            Integer financeStatus, Date updateChargeTime) {
        if (recordId == null || StringUtils.isBlank(statusCode) || status == null || StringUtils.isBlank(errorMsg)) {
            return false;
        }
        return chargeRecordMapper.updateStatusAndStatusCode(recordId, statusCode, status, errorMsg, financeStatus,
                updateChargeTime) == 1;
    }

    @Override
    public boolean batchUpdateStatusCode(String statusCode, List<String> systemNums) {
        if (StringUtils.isBlank(statusCode) || systemNums == null) {
            return false;
        }
        return chargeRecordMapper.batchUpdateStatusCode(statusCode, systemNums) >= 0;
    }

    @Override
    @Transactional
    public boolean updateActivityRecords(List<ChargeRecord> records, ChargeResult chargeResult) {
        ActivityWinRecordStatus status = null;
        if (chargeResult.getCode().equals(ChargeResult.ChargeResultCode.PROCESSING)) {
            status = ActivityWinRecordStatus.PROCESSING;
        } else if (chargeResult.getCode().equals(ChargeResult.ChargeResultCode.FAILURE)) {
            status = ActivityWinRecordStatus.FALURE;
        }
        Map<Integer, Object> map = initList();
        for (ChargeRecord record : records) {
            int typeCode = record.getTypeCode();
            if (typeCode == ActivityType.INTERFACE.getCode()) {
                InterfaceRecord interfaceRecord = interfaceRecordService.get(record.getRecordId());
                interfaceRecord.setStatus(status.getCode());
                interfaceRecord.setErrMsg(status.getname());
                interfaceRecord.setStatusCode(chargeResult.getStatusCode());
                interfaceRecord.setPhoneNum(record.getPhone());
                List<InterfaceRecord> list = (List<InterfaceRecord>) map.get(typeCode);
                list.add(interfaceRecord);
                map.put(typeCode, list);
            } else if (typeCode == ActivityType.GIVE.getCode()) {
                PresentRecord presentRecord = presentRecordService.selectByRecordId(record.getRecordId());
                presentRecord.setStatus(Byte.parseByte(String.valueOf(status.getCode())));
                presentRecord.setErrorMessage(status.getname());
                presentRecord.setStatusCode(chargeResult.getStatusCode());
                presentRecord.setMobile(record.getPhone());
                List<PresentRecord> list = (List<PresentRecord>) map.get(typeCode);
                list.add(presentRecord);
                map.put(typeCode, list);
            } else if (typeCode == ActivityType.REDPACKET.getCode() || typeCode == ActivityType.LOTTERY.getCode()
                    || typeCode == ActivityType.GOLDENBALL.getCode() || typeCode == ActivityType.FLOWCARD.getCode()
                    || typeCode == ActivityType.QRCODE.getCode() || typeCode == ActivityType.LUCKY_REDPACKET.getCode()) {
                typeCode = ActivityType.QRCODE.getCode();
                ActivityWinRecord winRecord = activityWinRecordService.selectByPrimaryKey(record.getRecordId());
                winRecord.setStatus(status.getCode());
                winRecord.setReason(status.getname());
                winRecord.setStatusCode(chargeResult.getStatusCode());
                winRecord.setChargeMobile(record.getPhone());
                List<ActivityWinRecord> list = (List<ActivityWinRecord>) map.get(typeCode);
                list.add(winRecord);
                map.put(typeCode, list);
            }
        }
        return batchUpdateActivityStatus(map);
    }

    private Map<Integer, Object> initList() {
        Map<Integer, Object> map = new HashMap<Integer, Object>();
        map.put(ActivityType.INTERFACE.getCode(), new ArrayList<InterfaceRecord>());
        map.put(ActivityType.GIVE.getCode(), new ArrayList<PresentRecord>());
        map.put(ActivityType.QRCODE.getCode(), new ArrayList<ActivityWinRecord>());
        return map;
    }

    private boolean batchUpdateActivityStatus(Map<Integer, Object> map) {
        if (map == null || map.isEmpty()) {
            return false;
        }
        boolean interfac = true;
        boolean present = true;
        boolean activityWin = true;
        if (!((List<InterfaceRecord>) map.get(ActivityType.INTERFACE.getCode())).isEmpty()) {
            interfac = interfaceRecordService.batchUpdateStatus((List<InterfaceRecord>) map.get(ActivityType.INTERFACE
                    .getCode()));
        }
        if (!((List<PresentRecord>) map.get(ActivityType.GIVE.getCode())).isEmpty()) {
            present = presentRecordService.batchUpdateChargeResult((List<PresentRecord>) map.get(ActivityType.GIVE
                    .getCode()));
        }
        if (!((List<ActivityWinRecord>) map.get(ActivityType.QRCODE.getCode())).isEmpty()) {
            activityWin = activityWinRecordService.batchUpdate((List<ActivityWinRecord>) map.get(ActivityType.QRCODE
                    .getCode()));
        }
        return interfac && present && activityWin;
    }

    @Override
    public boolean updateBossChargeTimeBySystemNum(String systemNum, Date date) {
        return chargeRecordMapper.updateBossChargeTimeBySystemNum(systemNum, date) == 1;
    }

    @Override
    public boolean batchUpdateBossChargeTimeBySystemNum(List<String> buildSystemNums, Date date) {
        return chargeRecordMapper.batchUpdateBossChargeTimeBySystemNum(buildSystemNums, date) == buildSystemNums.size();
    }

    @Override
    public List<ChargeRecord> queryChargeRecord(QueryObject queryObject) {
        return chargeRecordMapper.queryChargeRecord(queryObject.toMap());
    }

    @Override
    public Long countChargeRecord(QueryObject queryObject) {
        return chargeRecordMapper.countChargeRecord(queryObject.toMap());
    }

    @Override
    public List<ChargeRecord> getJxChargeRecords(Date start, Date end, Long supplierId) {
        if (start == null || end == null || start.after(end)) {
            LOGGER.error("无效的查询参数. StartTime = {}， EndTime = {}， supplierId = {}.", start, end, supplierId);
            return null;
        }

        return chargeRecordMapper.getJxChargeRecords(start, end, supplierId);
    }

    @Override
    public List<ChargeRecord> getMdrcChargeRecords(String year, Map map) {
        if (StringUtils.isBlank(year)) {
            return null;
        }
        if (map == null) {
            map = new HashMap<String, Object>();
        }
        map.put("year", year.substring(year.length() - 2, year.length()));
        return chargeRecordMapper.getMdrcChargeRecords(map);
    }

    @Override
    public long countMdrcChargeRecords(String year, Map map) {
        if (StringUtils.isBlank(year)) {
            return 0;
        }
        if (map == null) {
            map = new HashMap<String, Object>();
        }
        map.put("year", year.substring(year.length() - 2, year.length()));
        return chargeRecordMapper.countMdrcChargeRecords(map);
    }
    
    @Override
    public List<ChargeRecord> getRecordsByMobileAndPrd(String mobile, Long splPid, Date date) {
        if (mobile == null || splPid == null || date == null) {
            LOGGER.error("无效的查询参数. mobile = {}， splPid = {}， date = {}.", mobile, splPid, date);
            return null;
        }

        return chargeRecordMapper.getRecordsByMobileAndPrd(mobile, splPid, date);
    }

    @Override
    public boolean updateQueryTime(String systemNum, Date date) {
        // TODO Auto-generated method stub
        return chargeRecordMapper.updateQueryTime(systemNum, date) == 1;
    }
    
    /**
     * 判断是否山东环境
     */
    private boolean isSdEnvironment(){
        return "sd".equalsIgnoreCase(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey()));
    }
}
