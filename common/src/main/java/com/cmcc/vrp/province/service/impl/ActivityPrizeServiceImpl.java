package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.enums.IspType;
import com.cmcc.vrp.enums.PrizeType;
import com.cmcc.vrp.province.dao.ActivityPrizeMapper;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityPrize;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityPrizeService;
import com.cmcc.vrp.province.service.ProductService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ActivityPrizeServiceImpl
 */
@Service("activityPrizeService")
public class ActivityPrizeServiceImpl implements ActivityPrizeService {

    @Autowired
    ProductService productService;
    @Autowired
    ActivitiesService activitiesService;
    @Autowired
    ActivityPrizeMapper activityPrizeMapper;

    @Override
    public boolean delete(Long id) {
        if (id == null) {
            return false;
        }
        return activityPrizeMapper.deleteByPrimaryKey(id) == 1;
    }

    @Override
    public boolean insert(ActivityPrize activityPrize) {
        if (activityPrize == null) {
            return false;
        }
        return activityPrizeMapper.insert(activityPrize) == 1;
    }

    @Override
    public ActivityPrize selectByPrimaryKey(Long id) {
        if (id == null) {
            return null;
        }
        return activityPrizeMapper.selectByPrimaryKey(id);
    }
    
    @Override
    public ActivityPrize selectPrizeDetailByPrimaryKey(Long id) {
        if (id == null) {
            return null;
        }
        return activityPrizeMapper.selectPrizeDetailByPrimaryKey(id);
    }

    @Override
    public boolean updateByPrimaryKey(ActivityPrize record) {
        if (record == null || record.getId() == null) {
            return false;
        }
        return activityPrizeMapper.updateByPrimaryKey(record) == 1;
    }

    @Override
    public List<ActivityPrize> selectByActivityId(String activityId) {
        if (StringUtils.isBlank(activityId)) {
            return null;
        }
        return activityPrizeMapper.getDetailByActivityId(activityId);
    }

    @Override
    public List<ActivityPrize> selectByActivityIdForIndividual(String activityId) {
        if (!StringUtils.isBlank(activityId)) {
            return activityPrizeMapper.selectByActivityIdForIndividual(activityId);
        }
        return null;
    }

    @Override
    public boolean batchInsert(List<ActivityPrize> records) {
        if (records == null || records.size() <= 0) {
            return false;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", records);
        return activityPrizeMapper.batchInsert(map) == records.size();
    }

    @Override
    public boolean batchInsertForCrowdFunding(List<ActivityPrize> records) {
        // TODO Auto-generated method stub
        if (records == null || records.size() <= 0) {
            return false;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", records);
        return activityPrizeMapper.batchInsertForCrowdFunding(map) == records.size();
    }

    @Override
    public boolean deleteByActivityId(String activityId) {
        if (StringUtils.isBlank(activityId)) {
            return false;
        }
        return activityPrizeMapper.deleteByActivityId(activityId) > 0;
    }

    @Override
    public List<ActivityPrize> getDetailByActivityId(String activityId) {
        if (StringUtils.isBlank(activityId)) {
            return null;
        }
        return activityPrizeMapper.getDetailByActivityId(activityId);
    }

    @Override
    public boolean batchInsertForFlowCard(Activities activities, Long cmProductId, Long cuProductId, Long ctProductId,
            String cmMobileList, String cuMobileList, String ctMobileList) {
        if (activities == null) {
            return false;
        }

        List<ActivityPrize> activityPrizes = new ArrayList<ActivityPrize>();

        initActivityPrize(activityPrizes, activities, cmProductId, cmMobileList);
        initActivityPrize(activityPrizes, activities, cuProductId, cuMobileList);
        initActivityPrize(activityPrizes, activities, ctProductId, ctMobileList);

        return batchInsert(activityPrizes);
    }

    @Override
    public boolean batchInsertForQRcord(Activities activities, Long cmProductId, Long cuProductId, Long ctProductId) {
        if (activities == null) {
            return false;
        }
        List<ActivityPrize> activityPrizes = new ArrayList<ActivityPrize>();
        initActivityPrize(activityPrizes, activities, cmProductId, null);
        initActivityPrize(activityPrizes, activities, cuProductId, null);
        initActivityPrize(activityPrizes, activities, ctProductId, null);
        return batchInsert(activityPrizes);
    }

    private void initActivityPrize(List<ActivityPrize> activityPrizes, Activities activities, Long productId,
            String mobiles) {
        if (activities != null && productId != null) {
            Product product = productService.selectProductById(productId);
            if (product != null) {
                ActivityPrize record = new ActivityPrize();
                record.setIdPrefix("-1");
                record.setRankName("");
                record.setEnterpriseId(activities.getEntId());
                record.setProductId(productId);
                if (!StringUtils.isBlank(mobiles)) {
                    record.setCount((long) mobiles.split(",").length);
                } else {
                    record.setCount(0L);
                }
                record.setProbability("1");
                record.setActivityId(activities.getActivityId());
                record.setCreateTime(new Date());
                record.setUpdateTime(new Date());
                record.setDeleteFlag(0);
                record.setPrizeName(product.getName());
                if (product.getType().intValue() == PrizeType.FLOWPACKAGE.getType()
                        && (!StringUtils.isBlank(mobiles))) {
                    record.setSize(((long) mobiles.split(",").length) * product.getProductSize());
                } else {
                    // 流量池或无手机号
                    record.setSize(0L);
                }
                activityPrizes.add(record);
            }
        }
    }

    @Override
    public boolean deleteActivityPrize(List<Long> delProdIds, String activityId) {
        if (delProdIds == null || delProdIds.size() < 1 || StringUtils.isBlank(activityId)) {
            return true;
        }
        return activityPrizeMapper.deleteActivityPrize(delProdIds, activityId) == delProdIds.size();
    }

    @Override
    public boolean addActivityPrize(List<Long> addProdIds, String activityId, Long cmMobileCnt, Long cuMobileCnt,
            Long ctMobileCnt) {
        if (addProdIds == null || addProdIds.size() < 1 || StringUtils.isBlank(activityId)) {
            return true;
        }
        List<ActivityPrize> activityPrizes = new ArrayList<ActivityPrize>();
        initActivityPrize(activityPrizes, addProdIds, activityId, cmMobileCnt, cuMobileCnt, ctMobileCnt);
        return batchInsert(activityPrizes);
    }

    @Override
    public boolean updateActivityPrize(List<Long> updateProdIds, String activityId, Long cmMobileCnt, Long cuMobileCnt,
            Long ctMobileCnt) {

        if (updateProdIds == null || updateProdIds.size() < 1) {
            return true;
        }

        List<ActivityPrize> activityPrizeList = selectByActivityId(activityId);
        int i = 0;
        while (i < updateProdIds.size()) {
            for (ActivityPrize item : activityPrizeList) {
                if (item.getProductId().toString().equals(updateProdIds.get(i).toString())) {
                    if (item.getIsp().equals(IspType.CMCC.getValue())) {
                        item.setCount(cmMobileCnt);
                    } else if (item.getIsp().equals(IspType.UNICOM.getValue())) {
                        item.setCount(cuMobileCnt);
                    } else {
                        item.setCount(ctMobileCnt);
                    }
                    activityPrizeMapper.updateCountByPrimaryKey(item);
                }
            }
            i++;
        }
        return true;
    }

    private void initActivityPrize(List<ActivityPrize> activityPrizes, List<Long> addProdIds, String activityId,
            Long cmMobileCnt, Long cuMobileCnt, Long ctMobileCnt) {
        Activities activities = activitiesService.selectByActivityId(activityId);

        for (Long prodId : addProdIds) {
            Product product = productService.selectProductById(prodId);
            if (product != null) {
                ActivityPrize record = new ActivityPrize();

                record.setIdPrefix("-1");
                record.setRankName("");
                record.setEnterpriseId(activities.getEntId());
                record.setProductId(prodId);
                // 移动
                if (product.getIsp().equals(IspType.CMCC.getValue())) {
                    if (cmMobileCnt != null) {
                        record.setCount(cmMobileCnt);
                    } else {
                        record.setCount(0L);
                    }
                }
                // 联通
                if (product.getIsp().equals(IspType.UNICOM.getValue())) {
                    if (cuMobileCnt != null) {
                        record.setCount(cuMobileCnt);
                    } else {
                        record.setCount(0L);
                    }
                }
                // 电信
                if (product.getIsp().equals(IspType.TELECOM.getValue())) {
                    if (ctMobileCnt != null) {
                        record.setCount(ctMobileCnt);
                    } else {
                        record.setCount(0L);
                    }
                }

                record.setProbability("1");
                record.setActivityId(activities.getActivityId());
                record.setCreateTime(new Date());
                record.setUpdateTime(new Date());
                record.setDeleteFlag(0);
                record.setPrizeName(product.getName());
                if (product.getType().intValue() == PrizeType.FLOWPACKAGE.getType()) {
                    record.setSize((record.getCount().longValue() * product.getProductSize()));
                } else {
                    // 流量池
                    record.setSize(0L);
                }
                activityPrizes.add(record);
            }
        }
    }

    @Override
    public boolean insertForRedpacket(ActivityPrize activityPrize) {
        if (activityPrize == null) {
            return false;
        }
        return activityPrizeMapper.insertForRedpacket(activityPrize) == 1;
    }

    @Override
    public boolean batchUpdateSelective(List<ActivityPrize> activityPrizes) {
        if (activityPrizes != null && activityPrizes.size() > 0) {
            Map map = new HashedMap();
            map.put("activityPrizes", activityPrizes);
            return activityPrizeMapper.batchUpdateSelective(map) == activityPrizes.size();
        }
        return false;
    }

    @Override
    public boolean batchUpdateDiscount(List<ActivityPrize> activityPrizes) {
        // TODO Auto-generated method stub
        if (activityPrizes != null && activityPrizes.size() > 0) {
            Map map = new HashedMap();
            map.put("activityPrizes", activityPrizes);
            return activityPrizeMapper.batchUpdateDiscount(map) == activityPrizes.size();
        }
        return false;
    }

}
