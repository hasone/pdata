package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.enums.ActivityWinRecordStatus;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.dao.PresentRecordMapper;
import com.cmcc.vrp.province.model.PresentRecord;
import com.cmcc.vrp.province.model.PresentRecordResult;
import com.cmcc.vrp.province.model.PresentRule;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.json.post.PresentRecordJson;
import com.cmcc.vrp.province.service.PresentRecordService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.util.QueryObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>Title: </p> <p>Description: </p>
 *
 * @author lgk8023
 * @date 2016年12月2日 下午3:44:11
 */
@Service
public class PresentRecordServiceImpl implements PresentRecordService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PresentRecordServiceImpl.class);

    @Autowired
    PresentRecordMapper presentRecordMapper;

    @Autowired
    TaskProducer taskProducer;

    @Autowired
    private ProductService productService;

    @Override
    public boolean create(PresentRule presentRule, List<PresentRecordJson> presentRecordJson) {

        List<PresentRecord> list = new ArrayList<PresentRecord>();
        for (int i = 0; i < presentRecordJson.size(); i++) {

            //获取产品id
            Product p = productService.selectProductById(presentRecordJson.get(i).getPrdId());
            if (p == null) {
                return false;
            }

            String[] phoneArray = presentRecordJson.get(i).getPhones().split(",");
            List<String> phones = Arrays.asList(phoneArray);
            for (int j = 0; j < phones.size(); j++) {
                PresentRecord presentRecord = new PresentRecord();
                presentRecord.setCreateTime(new Date());

                //新疆流量池,需要进行产品转换
                //                if (p.getType().equals((int) ProductType.FLOW_ACCOUNT.getValue())) {
                //                    if (StringUtils.isEmpty(presentRecordJson.get(i).getPrdName())) {
                //                        return false;
                //                    }
                //                    String size = presentRecordJson.get(i).getPrdName();
                //                    size = size.substring(0, size.length() - 1);
                //                    Long kbSize = SizeUnits.MB.toKB(Long.parseLong(size));
                //                    Product platformProduct = productService.getProductForFlowAccount(kbSize.toString(), p.getId());
                //
                //                    if (platformProduct == null || platformProduct.getId() == null) {
                //                        return false;
                //                    }
                //                    presentRecord.setPrdId(platformProduct.getId());
                //                } else {
                //                    presentRecord.setPrdId(presentRecordJson.get(i).getPrdId());
                //                }
                presentRecord.setPrdId(presentRecordJson.get(i).getPrdId());

                presentRecord.setStatus(new Byte("1"));
                presentRecord.setMobile(phones.get(j));
                presentRecord.setRuleId(presentRule.getId());
                //增加流水号
                presentRecord.setSysSerialNum(SerialNumGenerator.buildSerialNum());
                presentRecord.setEffectType(presentRecordJson.get(i).getEffectType());

                list.add(presentRecord);
            }
        }
        return presentRecordMapper.batchInsert(list) > 0;
    }

    /**
     * @Title: queryCount
     * @Description: TODO
     * @return: int
     */
    public int queryCount(QueryObject queryObject) {

        return presentRecordMapper.queryCount(queryObject.toMap());
    }

    /**
     *
     */
    public List<PresentRecordResult> queryRecord(QueryObject queryObject) {
        return presentRecordMapper.queryRecord(queryObject.toMap());
    }

    /**
     * @Title: queryCountPlus
     * @Description: TODO
     * @see com.cmcc.vrp.province.service.PresentRecordService#queryCountPlus(java.util.Map)
     */
    @Override
    public int queryCountPlus(Map<String, Object> map) {
        return presentRecordMapper.queryCountPlus(map);
    }

    @Override
    public boolean updateStatus(Long id, ChargeRecordStatus status, String errorMsg) {
        if (id == null || status == null) {
            LOGGER.error("无效的记录ID或充值状态. Id = {}， Status = {}.", id, status == null ? null : status.getCode());
            return false;
        }

        return presentRecordMapper.updateStatusAndStatusCode(id, status.getCode(), status.getStatusCode(), errorMsg) == 1;
    }

    @Override
    public boolean updateActivityStatus(Long id, ActivityWinRecordStatus status, String errorMsg) {
        if (id == null || status == null) {
            LOGGER.error("无效的记录ID或充值状态. Id = {}， Status = {}.", id, status == null ? null : status.getCode());
            return false;
        }

        return presentRecordMapper.updateStatusAndStatusCode(id, status.getCode(), status.getStatusCode(), errorMsg) == 1;
    }

    @Override
    public boolean updatePresentStatus(Long id, ChargeRecordStatus status, String errorMsg) {
        if (id == null || status == null) {
            LOGGER.error("无效的记录ID或充值状态. Id = {}， Status = {}.", id, status == null ? null : status.getCode());
            return false;
        }

        return presentRecordMapper.updateStatus(id, status.getCode(), errorMsg) == 1;
    }

    /**
     * @Title: queryRecordPlus
     * @Description: TODO
     * @see com.cmcc.vrp.province.service.PresentRecordService#queryRecordPlus(java.util.Map)
     */
    @Override
    public List<PresentRecordResult> queryRecordPlus(Map<String, Object> map) {
        return presentRecordMapper.queryRecordPlus(map);
    }

    /**
     * 查询记录数
     */
    public int count(long id) {
        return presentRecordMapper.count(id);
    }

    /**
     * 查询记录
     */
    public List<PresentRecord> selectByRuleId(long ruleId) {
        return presentRecordMapper.selectByRuleId(ruleId);
    }

    /**
     * @Title: selectByRecordId
     * @Description: TODO
     * @see com.cmcc.vrp.province.service.PresentRecordService#selectByRecordId(long)
     */
    @Override
    public PresentRecord selectByRecordId(long id) {
        return presentRecordMapper.selectByPrimaryKey(id);
    }
    
    
    /**
     * @Title: selectBySysSerialNum
     */
    @Override
    public PresentRecord selectBySysSerialNum(String sysSerialNum) {
        // TODO Auto-generated method stub
        return presentRecordMapper.selectBySysSerialNum(sysSerialNum);
    }

    @Override
    public boolean batchUpdateChargeResult(List<PresentRecord> records) {
        return records != null && !records.isEmpty()
                && presentRecordMapper.batchUpdateChargeResult(records) == records.size();
    }

    @Override
    public boolean batchUpdateStatus(List<Long> ids, ChargeRecordStatus chargeRecordStatus) {
        return ids != null && !ids.isEmpty()
                && presentRecordMapper.batchUpdateStatus(ids, chargeRecordStatus.getCode()) == ids.size();
    }

    @Override
    public boolean batchUpdateStatusCode(List<Long> ids, String statusCode) {
        return ids != null && !ids.isEmpty()
                && presentRecordMapper.batchUpdateStatusCode(ids, statusCode) == ids.size();
    }

    @Override
    public boolean updateStatusCode(Long recordId, String statusCode) {
        return recordId != null && presentRecordMapper.updateStatusCode(recordId, statusCode) == 1;
    }
}
