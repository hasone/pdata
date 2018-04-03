package com.cmcc.vrp.province.quartz.jobs;

import com.cmcc.vrp.enums.SupplierLimitStatus;
import com.cmcc.vrp.province.model.Supplier;
import com.cmcc.vrp.province.model.SupplierProdReqUsePerDay;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.model.SupplierReqUsePerDay;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SupplierProdReqUsePerDayService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.province.service.SupplierReqUsePerDayService;
import com.cmcc.vrp.province.service.SupplierService;
import com.cmcc.vrp.util.Constants;
import java.util.Date;
import java.util.List;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 插入供应商或者供应商产品限额统计记录
 * @author qinqinyan
 */
public class DailySupplierLimitStatisticJob extends QuartzJobBean {
    private static Logger logger = LoggerFactory.getLogger(DailySupplierLimitStatisticJob.class);

    @Autowired
    SupplierService supplierService;
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    SupplierReqUsePerDayService supplierReqUsePerDayService;
    @Autowired
    SupplierProductService supplierProductService;
    @Autowired
    SupplierProdReqUsePerDayService supplierProdReqUsePerDayService;

    @Override
    protected void executeInternal(JobExecutionContext context)
        throws JobExecutionException {
        
        //插入新的记录
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        for(Supplier supplier : suppliers){
            if(supplier.getLimitMoneyFlag()!=null && 
                    supplier.getLimitMoneyFlag().toString().equals(SupplierLimitStatus.ON.getCode().toString())){
                //供应商限额
                SupplierReqUsePerDay supplierReqUsePerDay = supplierReqUsePerDayService.getTodayRecord(supplier.getId());
                if(supplierReqUsePerDay == null){
                    //未插入
                    if(!supplierReqUsePerDayService.insertSelective(createSupplierReqUsePerDay(supplier.getId()))){
                        logger.info("插入供应商  = {} 限额统计记录失败。", supplier.getId());
                    }else{
                        logger.info("插入供应商  = {} 限额统计记录成功。", supplier.getId());
                    }
                }else{
                    logger.info("供应商  = {} 限额统计记录已经存在, 记录id = {}。", supplier.getId(), supplierReqUsePerDay.getId());
                }
            }
            //插入供应商产品的统计记录
            List<SupplierProduct> supplierProducts = supplierProductService.selectBySupplierId(supplier.getId());
            for(SupplierProduct supplierProduct : supplierProducts){
                if(supplierProduct.getLimitMoneyFlag()!=null && 
                        supplierProduct.getLimitMoneyFlag().toString()
                        .equals(SupplierLimitStatus.ON.getCode().toString())){
                    SupplierProdReqUsePerDay supplierProdReqUsePerDay = supplierProdReqUsePerDayService.getTodayRecord(supplierProduct.getId());
                    if(supplierProdReqUsePerDay == null){
                        if(!supplierProdReqUsePerDayService.insertSelective(createSupplierProdReqUsePerDay(supplierProduct.getId()))){
                            logger.info("插入供应商产品  = {} 限额统计记录失败。", supplierProduct.getId());
                        }else{
                            logger.info("插入供应商产品  = {} 限额统计记录成功。", supplierProduct.getId());
                        }
                    }else{
                        logger.info("供应商产品  = {} 限额统计记录已经存在, 记录id = {}。", supplierProduct.getId(), supplierProdReqUsePerDay.getId());
                    }
                }
            }
        }
    }
    
    private SupplierReqUsePerDay createSupplierReqUsePerDay(Long supplierId){
        SupplierReqUsePerDay record = new SupplierReqUsePerDay();
        record.setSupplierId(supplierId);
        record.setUsedMoney(0D);
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        record.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
        return record;
    }
    
    private SupplierProdReqUsePerDay createSupplierProdReqUsePerDay(Long supplierProductId){
        SupplierProdReqUsePerDay record = new SupplierProdReqUsePerDay();
        record.setSupplierProductId(supplierProductId);
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        record.setUseMoney(0D);
        record.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
        return record;
    }
}

