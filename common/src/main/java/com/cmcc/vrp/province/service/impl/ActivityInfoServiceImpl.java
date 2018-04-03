package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.ec.bean.Constants;
import com.cmcc.vrp.enums.BlackAndWhiteListType;
import com.cmcc.vrp.province.dao.ActivityInfoMapper;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityInfo;
import com.cmcc.vrp.province.model.EntProduct;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.ActivityInfoService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by qinqinyan on 2016/8/17.
 */
@Service("activityInfoService")
public class ActivityInfoServiceImpl implements ActivityInfoService {

    @Autowired
    ActivityInfoMapper activityInfoMapper;
    @Autowired
    ProductService productService;
    @Autowired
    EntProductService entProductService;
    @Autowired
    GlobalConfigService globalConfigService;

    @Override
    public boolean insert(ActivityInfo record) {
        if (record == null) {
            return false;
        }
        return activityInfoMapper.insert(record) == 1;
    }

    @Override
    public boolean updateByPrimaryKeySelective(ActivityInfo record) {
        if (record == null) {
            return false;
        }
        return activityInfoMapper.updateByPrimaryKeySelective(record) >= 0;
    }

    @Override
    public ActivityInfo selectByActivityId(String activityId) {
        if (StringUtils.isEmpty(activityId)) {
            return null;
        }
        return activityInfoMapper.selectByActivityId(activityId);
    }

    @Override
    public boolean insertActivityInfo(Activities activities, Long cmProductId,
                                      Long cuProductId, Long ctProductId, String cmMobileList,
                                      String cuMobileList, String ctMobileList, String cmUserSet,
                                      String cuUserSet, String ctUserSet) {
        ActivityInfo activityInfo = initActivityInfo(activities, cmProductId,
                cuProductId, ctProductId, cmMobileList, cuMobileList,
                ctMobileList, cmUserSet, cuUserSet, ctUserSet);

        return insert(activityInfo);
    }

    @Override
    public boolean insertActivityInfoForQrcode(Activities activities,
                                               ActivityInfo activityInfo, Long cmProductId, Long cuProductId,
                                               Long ctProductId) {
        initActivityInfoForQrcode(activities, activityInfo, cmProductId,
                cuProductId, ctProductId);
        return insert(activityInfo);
    }

    @Override
    public boolean updateActivityInfoForQrcode(Activities activities,
                                               ActivityInfo activityInfo, Long cmProductId, Long cuProductId,
                                               Long ctProductId) {
        ActivityInfo oldActivityInfo = selectByActivityId(activities
                .getActivityId());
        if (oldActivityInfo != null) {
            activityInfo.setId(oldActivityInfo.getId());
            initActivityInfoForQrcode(activities, activityInfo, cmProductId,
                    cuProductId, ctProductId);
            return updateByPrimaryKeySelective(activityInfo);
        }
        return false;
    }

    /**
     * 初始化二维码
     */
    private void initActivityInfoForQrcode(Activities activities,
                                           ActivityInfo activityInfo, Long cmProductId, Long cuProductId,
                                           Long ctProductId) {
        activityInfo.setActivityId(activities.getActivityId());
        activityInfo.setUserCount(activityInfo.getPrizeCount());

        Product cmProduct = productService.selectProductById(cmProductId);
        Product cuProduct = productService.selectProductById(cuProductId);
        Product ctProduct = productService.selectProductById(ctProductId);

        EntProduct cmEntProduct = entProductService
                .selectByProductIDAndEnterprizeID(cmProductId,
                        activities.getEntId());
        EntProduct cuEntProduct = entProductService
                .selectByProductIDAndEnterprizeID(cuProductId,
                        activities.getEntId());
        EntProduct ctEntProduct = entProductService
                .selectByProductIDAndEnterprizeID(ctProductId,
                        activities.getEntId());
        //流量池或者话费产品，cmEntProduct替换父产品
        if (cmProduct != null && cmProduct.getFlowAccountFlag() == 1) {
            Product flowAccountProduct = productService.get(cmProduct.getId());
            if(flowAccountProduct != null){
                cmEntProduct = entProductService.selectByProductIDAndEnterprizeID(flowAccountProduct.getFlowAccountProductId(), activities.getEntId());
            }else{
                cmEntProduct = null;
            }
        }
                
        if (cmProduct != null && (cmProduct.getFlowAccountFlag() == 3)) {
            Product flowAccountProduct = productService.selectProductById(cmProduct.getFlowAccountProductId());
            cmEntProduct = entProductService.selectByProductIDAndEnterprizeID(flowAccountProduct.getId(), activities.getEntId());
        }

        Integer totalPrice = 0;
        Long totalSize = 0L;

        if (cmProductId != null && cuProductId != null && ctProductId != null
                && activityInfo.getPrizeCount() >= 10L) {
            // 三种产品都存在，则按照移动:联通:电信=7:2:1
            Long cmCount = activityInfo.getPrizeCount().longValue() * 7 / 10;
            Long cuCount = activityInfo.getPrizeCount().longValue() * 2 / 10;
            Long ctCount = activityInfo.getPrizeCount().longValue() / 10;

            if (activityInfo.getPrizeCount().longValue() % 10 != 0) {
                cmCount += 1;
                cuCount += 1;
                ctCount += 1;
            }

            totalSize = cmCount.longValue()
                    * cmProduct.getProductSize().longValue()
                    + cuCount.longValue()
                    * cuProduct.getProductSize().longValue()
                    + ctCount.longValue()
                    * ctProduct.getProductSize().longValue();

            if (cmProduct.getPrice() != null && cmEntProduct != null
                    && cmEntProduct.getDiscount() != null) {
                totalPrice += cmCount.intValue()
                        * cmProduct.getPrice().intValue()
                        * cmEntProduct.getDiscount().intValue() / 100;
            }

            if (cuProduct.getPrice() != null && cuEntProduct != null
                    && cuEntProduct.getDiscount() != null) {
                totalPrice += cuCount.intValue()
                        * cuProduct.getPrice().intValue()
                        * cuEntProduct.getDiscount().intValue() / 100;
            }

            if (ctProduct.getPrice() != null && ctEntProduct != null
                    && ctEntProduct.getDiscount() != null) {
                totalPrice += ctCount.intValue()
                        * ctProduct.getPrice().intValue()
                        * ctEntProduct.getDiscount().intValue() / 100;
            }
            activityInfo.setPrice((long) totalPrice);
            activityInfo.setTotalProductSize(totalSize);
        } else if (cmProductId == null && cuProductId == null
                && ctProductId == null) {
            activityInfo.setPrice(0L);
            activityInfo.setTotalProductSize(0L);
        } else {
            Integer avePrice = 0;
            Long aveSize = 0L;
            Double aveDiscount = 0D;

            if (cmProduct != null && cmProduct.getProductSize() != null
                    && cmProduct.getPrice() != null) {
                avePrice += cmProduct.getPrice();
                aveSize += cmProduct.getProductSize();

                if (cmEntProduct != null && cmEntProduct.getDiscount() != null) {
                    aveDiscount += cmEntProduct.getDiscount();
                }
            }

            if (cuProduct != null && cuProduct.getProductSize() != null
                    && cuProduct.getPrice() != null) {
                avePrice += cuProduct.getPrice();
                aveSize += cuProduct.getProductSize();

                if (cuEntProduct != null && cuEntProduct.getDiscount() != null) {
                    aveDiscount += cuEntProduct.getDiscount();
                }
            }

            if (ctProduct != null && ctProduct.getProductSize() != null
                    && ctProduct.getPrice() != null) {
                avePrice += ctProduct.getPrice();
                aveSize += ctProduct.getProductSize();

                if (ctEntProduct != null && ctEntProduct.getDiscount() != null) {
                    aveDiscount += ctEntProduct.getDiscount();
                }
            }

            if (cmProduct != null && cuProduct != null && ctProduct != null) {
                avePrice = new BigDecimal(avePrice / 3).setScale(0,
                        BigDecimal.ROUND_HALF_UP).intValue();
                aveSize = new BigDecimal(aveSize / 3).setScale(0,
                        BigDecimal.ROUND_HALF_UP).longValue();
                aveDiscount = aveDiscount / 3;
            } else if (cmProduct != null && cuProduct != null
                    && ctProduct == null || cmProduct != null
                    && cuProduct == null && ctProduct != null
                    || cmProduct == null && cuProduct != null
                    && ctProduct != null) {
                avePrice = avePrice / 2;
                aveSize = aveSize / 2;
                aveDiscount = aveDiscount / 2;
            }

            totalPrice = new BigDecimal(avePrice.intValue()
                    * activityInfo.getPrizeCount().intValue()
                    * aveDiscount.doubleValue() / 100).setScale(0,
                    BigDecimal.ROUND_HALF_UP).intValue();
            totalSize = aveSize.longValue()
                    * activityInfo.getPrizeCount().longValue();

            activityInfo.setPrice((long) totalPrice);
            activityInfo.setTotalProductSize(totalSize);
        }

        activityInfo.setUpdateTime(new Date());
        if (activityInfo.getId() == null) {
            activityInfo.setCreateTime(new Date());
            activityInfo.setDeleteFlag(0);
        }
        return;
    }

    /**
     * 初始化流量券
     */
    private ActivityInfo initActivityInfo(Activities activities,
                                          Long cmProductId, Long cuProductId, Long ctProductId,
                                          String cmMobileList, String cuMobileList, String ctMobileList,
                                          String cmUserSet, String cuUserSet, String ctUserSet) {
        ActivityInfo activityInfo = new ActivityInfo();

        int cmMobileCnt = 0;
        int cuMobileCnt = 0;
        int ctMobileCnt = 0;

        int cmUserCnt = 0;
        int cuUserCnt = 0;
        int ctUserCnt = 0;

        long cmSize = 0;
        long cuSize = 0;
        long ctSize = 0;

        long cmPrice = 0;
        long cuPrice = 0;
        long ctPrice = 0;

        if (cmProductId != null && !StringUtils.isEmpty(cmUserSet)) {
            cmMobileCnt = cmMobileList.split(",").length;
            cmUserCnt = cmUserSet.split(",").length;
            Product product = productService.selectProductById(cmProductId);
            EntProduct entProduct = entProductService.selectByProductIDAndEnterprizeID(cmProductId, activities.getEntId());
            Integer type = product.getType();
            // 如果是话费、流量池产品时,先转化为父产品
                                   
            if (type.equals((int)Constants.ProductType.MOBILE_FEE.getValue())) {
                product = productService.selectProductById(product.getFlowAccountProductId());
                entProduct = entProductService.selectByProductIDAndEnterprizeID(product.getId(), activities.getEntId());
                cmSize = cmMobileCnt * product.getProductSize();
                cmPrice = cmMobileCnt * product.getPrice()
                        * entProduct.getDiscount() / 100;
            } else if(type.equals((int)Constants.ProductType.FLOW_ACCOUNT.getValue())){
                cmSize = cmMobileCnt * product.getProductSize();
                cmPrice = cmMobileCnt * product.getPrice();
            } else {
                cmSize = cmMobileCnt * product.getProductSize();
                cmPrice = cmMobileCnt * product.getPrice()
                        * entProduct.getDiscount() / 100;
            }                              
        }
        if (cuProductId != null && !StringUtils.isEmpty(cuUserSet)) {
            cuMobileCnt = cuMobileList.split(",").length;
            cuUserCnt = cuUserSet.split(",").length;
            Product product = productService.selectProductById(cuProductId);
            cuSize = cuMobileCnt * product.getProductSize();

            EntProduct entProduct = entProductService
                    .selectByProductIDAndEnterprizeID(cuProductId,
                            activities.getEntId());

            cuPrice = cuMobileCnt * product.getPrice()
                    * entProduct.getDiscount() / 100;
        }
        if (ctProductId != null && !StringUtils.isEmpty(ctUserSet)) {
            ctMobileCnt = ctMobileList.split(",").length;
            ctUserCnt = ctUserSet.split(",").length;
            Product product = productService.selectProductById(ctProductId);
            ctSize = ctMobileCnt * product.getProductSize();

            EntProduct entProduct = entProductService
                    .selectByProductIDAndEnterprizeID(ctProductId,
                            activities.getEntId());

            ctPrice = ctMobileCnt * product.getPrice()
                    * entProduct.getDiscount() / 100;
        }

        activityInfo
                .setPrizeCount((long) (cmMobileCnt + cuMobileCnt + ctMobileCnt));
        activityInfo.setUserCount((long) (cmUserCnt + cuUserCnt + ctUserCnt));
        activityInfo.setTotalProductSize(cmSize + cuSize + ctSize);
        activityInfo.setPrice(cmPrice + cuPrice + ctPrice);

        activityInfo.setActivityId(activities.getActivityId());
        activityInfo.setCreateTime(new Date());
        activityInfo.setUpdateTime(new Date());
        activityInfo.setDeleteFlag(0);

        activityInfo.setHasWhiteOrBlack(BlackAndWhiteListType.NOLIST.getCode());
        activityInfo.setQrcodeSize(0);

        return activityInfo;
    }

    @Override
    public boolean updateActivityInfo(String activityId, Long cmProductId,
                                      Long cuProductId, Long ctProductId, Long cmMobileCnt,
                                      Long cuMobileCnt, Long ctMobileCnt, Long cmUserCnt, Long cuUserCnt,
                                      Long ctUserCnt) {
        ActivityInfo activityInfo = new ActivityInfo();
        activityInfo.setActivityId(activityId);
        activityInfo.setUpdateTime(new Date());
        activityInfo.setUserCount(cmUserCnt + cuUserCnt + ctUserCnt);
        activityInfo.setPrizeCount(cmMobileCnt + cuMobileCnt + ctMobileCnt);

        Long price = 0L;
        Long size = 0L;
        if (cmProductId != null) {
            Product product = productService.selectProductById(cmProductId);
            price += product.getPrice().intValue() * cmMobileCnt;
            size += product.getProductSize().longValue() * cmMobileCnt;
        }
        if (cuProductId != null) {
            Product product = productService.selectProductById(cuProductId);
            price += product.getPrice().intValue() * cuMobileCnt;
            size += product.getProductSize().longValue() * cuMobileCnt;
        }
        if (ctProductId != null) {
            Product product = productService.selectProductById(ctProductId);
            price += product.getPrice().intValue() * ctMobileCnt;
            size += product.getProductSize().longValue() * ctMobileCnt;
        }
        activityInfo.setPrice(price);
        activityInfo.setTotalProductSize(size);
        return updateByPrimaryKeySelective(activityInfo);
    }

    @Override
    public boolean insertForRedpacket(ActivityInfo activityInfo) {
        if (activityInfo == null) {
            return false;
        }
        return activityInfoMapper.insertForRedpacket(activityInfo) == 1;
    }

    @Override
    public boolean insertSelective(ActivityInfo activityInfo) {
        if(activityInfo == null){
            return false;
        }
        return activityInfoMapper.insertSelective(activityInfo) >= 0;
    }

    @Override
    public boolean updateForRendomPacket(ActivityInfo activityInfo) {
        return activityInfoMapper.updateForRendomPacket(activityInfo) >= 0;
    }
}
