package com.cmcc.vrp.boss.chongqing;

import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.boss.chongqing.service.ProductQueryService;
import com.cmcc.vrp.province.model.EntSyncList;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.service.EntSyncListService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.StringUtils;

/**
* <p>Title: </p>
* <p>Description: 重庆产品个数查询</p>
* @author lgk8023
* @date 2017年5月16日 下午4:53:57
*/
@Service("productQueryService")
public class ProductQueryServiceImpl implements ProductQueryService{
    private static final Logger logger = LoggerFactory.getLogger(ProductQueryServiceImpl.class);
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    EntSyncListService entSyncListService;
    @Autowired
    private ApplicationContext applicationContext;
    
    @Override
    public Double getProductNum(String productCode) {
        String entCode = getEnterCode();
        Enterprise enterprise = null;
        List<EntSyncList> entSyncLists = null;
        if (StringUtils.isEmpty(productCode)
                || StringUtils.isEmpty(entCode)
                || (enterprise = enterprisesService.selectByCode(entCode)) == null) {
            logger.error("请求参数错误，productCode{},entCode", productCode,entCode);
            return null;
        }
        CQBossServiceImpl bossService = applicationContext.getBean("cqBossService", CQBossServiceImpl.class);
        if (bossService == null) {
            return null;
        }
        Double num = 0.0;
        entSyncLists = entSyncListService.getByEntId(enterprise.getId());
        for(EntSyncList entSyncList:entSyncLists) {
            String entProCode = entSyncList.getEntProductCode();
            List<String> prdsList = bossService.getProductsOrderByEnterCode(entProCode);
            if (prdsList != null
                    && !prdsList.isEmpty()) {
                for(String prd:prdsList) {
                    if (productCode.equals(prd)) {
                        Double currentBossNumber = NumberUtils.toDouble(bossService.getEnterPrdSolde(entProCode, prd));
                        num = num + currentBossNumber;
                    }
                }
            }
        }
        return num;
    }

    public String getEnterCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.YQX_ORDERID_ENTER_CODE.getKey());
    }
}
