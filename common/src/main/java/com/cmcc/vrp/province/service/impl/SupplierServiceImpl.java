package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.enums.IspType;
import com.cmcc.vrp.enums.SupplierLimitStatus;
import com.cmcc.vrp.enums.SupplierOperateLimitType;
import com.cmcc.vrp.enums.SupplierStatus;
import com.cmcc.vrp.province.dao.SupplierMapper;
import com.cmcc.vrp.province.dao.SupplierProductMapMapper;
import com.cmcc.vrp.province.dao.SupplierProductMapper;
import com.cmcc.vrp.province.model.Supplier;
import com.cmcc.vrp.province.model.SupplierFinanceRecord;
import com.cmcc.vrp.province.model.SupplierModifyLimitRecord;
import com.cmcc.vrp.province.model.SupplierReqUsePerDay;
import com.cmcc.vrp.province.service.SupplierFinanceRecordService;
import com.cmcc.vrp.province.service.SupplierModifyLimitRecordService;
import com.cmcc.vrp.province.service.SupplierPayRecordService;
import com.cmcc.vrp.province.service.SupplierProductMapService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.province.service.SupplierReqUsePerDayService;
import com.cmcc.vrp.province.service.SupplierService;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.QueryObject;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 供应商实现
 * <p>
 * Created by sunyiwei on 2016/6/13.
 */
@Service
public class SupplierServiceImpl implements SupplierService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SupplierServiceImpl.class);

    @Autowired
    SupplierMapper supplierMapper;

    @Autowired
    SupplierProductMapper supplierProductMapper;

    @Autowired
    SupplierProductMapMapper supplierProductMapMapper;

    @Autowired
    SupplierProductMapService supplierProductMapService;

    @Autowired
    SupplierProductService supplierProductService;
    @Autowired
    SupplierFinanceRecordService supplierFinanceRecordService;
    @Autowired
    SupplierPayRecordService supplierPayRecordService;
    @Autowired
    SupplierModifyLimitRecordService supplierModifyLimitRecordService;
    @Autowired
    SupplierReqUsePerDayService supplierReqUsePerDayService;

    /**
     * 插入供应商信息 1、插入供应商信息 2、插入供应商财务记录
     */
    @Override
    @Transactional
    public boolean create(Supplier supplier) {
        if (!validate(supplier)) {
            LOGGER.error("无效的供应商参数， 新增供应商失败. Supplier = {}.", supplier == null ? "" : new Gson().toJson(supplier));
            return false;
        }

        initSupplier(supplier);
        if (supplierMapper.create(supplier) != 1) {
            LOGGER.info("插入供应商信息失败! supplier = {}", new Gson().toJson(supplier));
            return false;
        }

        SupplierFinanceRecord record = createSupplierFinanceRecord(supplier);
        if (!supplierFinanceRecordService.insertSelective(record)) {
            LOGGER.info("插入供应商财务记录失败! supplier = {}", new Gson().toJson(supplier));
            throw new RuntimeException();
        }
        return true;
    }

    private Supplier initSupplier(Supplier supplier) {
        supplier.setLimitMoney(-1D);
        supplier.setLimitMoneyFlag(SupplierLimitStatus.OFF.getCode());
        return supplier;
    }

    private SupplierFinanceRecord createSupplierFinanceRecord(Supplier supplier) {
        SupplierFinanceRecord record = new SupplierFinanceRecord();
        record.setSupplierId(supplier.getId());
        record.setTotalMoney(0D);
        record.setUsedMoney(0D);
        record.setBalance(0D);
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        record.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
        return record;
    }

    // 1. 删除供应商
    // 3. 删除供应商提供的供应商产品
    // 2. 删除该供应商提的供应商产品与平台产品的关系
    // 4. 删除供应商财务记录
    // 5. 删除供应商付款记录
    @Override
    @Transactional
    public boolean delete(Long supplierId) {
        if (supplierId != null) {
            return supplierMapper.delete(supplierId) == 1 && supplierProductMapService.deleteBySupplierId(supplierId)
                    && supplierProductService.deleteBysupplierId(supplierId)
                    && supplierFinanceRecordService.deleteBysupplierId(supplierId)
                    && supplierPayRecordService.deleteBysupplierId(supplierId);
        }
        LOGGER.error("删除供应商失败. 供应商ID = {}.", "");
        throw new RuntimeException("删除供应商失败.");
    }

    @Override
    public boolean update(Long supplierId, String newName, IspType newType) {
        return supplierId != null && (StringUtils.isNotBlank(newName) || newType != null) // 更新时名称或者ISP类型至少一个不为空，同时都为空就不需要更新了
                && supplierMapper.update(supplierId, newName, newType == null ? null : newType.getValue()) == 1;
    }

    @Override
    public Supplier get(Long supplierId) {
        if (supplierId != null) {
            return supplierMapper.get(supplierId);
        }

        LOGGER.error("无效的供应商ID.");
        return null;
    }

    @Override
    public List<Supplier> getSupplierBySync(int sync) {
        return supplierMapper.getSupplierBySync(sync);
    }

    @Override
    public List<Supplier> selectByMap(Map map) {
        // TODO Auto-generated method stub
        return supplierMapper.selectByMap(map);
    }

    @Override
    public List<Supplier> queryPaginationSupplier(QueryObject queryObject) {
        Map map = queryObject.toMap();
        return supplierMapper.queryPaginationSupplier(map);
    }

    @Override
    public int queryPaginationSupplierCount(QueryObject queryObject) {
        Map map = queryObject.toMap();

        return supplierMapper.queryPaginationSupplierCount(map);
    }

    @Override
    @Transactional
    public boolean suspendSupplier(Long supplierId) {
        int supplierProductSize = supplierProductMapper.selectBySupplierId(supplierId).size();
        int supplierProductMapSize = supplierProductMapMapper.getBySpplierId(supplierId).size();

        if (supplierMapper.delete(supplierId) == 1
                && supplierProductMapper.deleteBySupplierId(supplierId) == supplierProductSize
                && supplierProductMapMapper.deleteBySupplierId(supplierId) == supplierProductMapSize) {
            return true;
        }

        throw new RuntimeException();
    }

    @Override
    public boolean recoverSupplier(Long supplierId) {
        if (supplierMapper.recoverSupplier(supplierId) == 1) {
            return true;
        }
        throw new RuntimeException();

    }

    @Override
    public Supplier getByFingerPrint(String fingerPrint) {
        return supplierMapper.getByFingerPrint(fingerPrint);
    }

    @Override
    public List<Supplier> getAllSuppliers() {
        return supplierMapper.getAllSuppliers();
    }

    private boolean validate(Supplier supplier) {
        return supplier != null && StringUtils.isNotBlank(supplier.getName())
                && StringUtils.isNotBlank(supplier.getFingerprint());
    }

    @Override
    public Boolean updateByPrimaryKeySelective(Supplier supplier) {
        if (supplier != null) {
            if (supplier.getStatus() != null
                    && supplier.getStatus().intValue()==SupplierStatus.OFF.getCode()) {
                supplierProductMapService.clearPriorSupplierBySupplierId(supplier.getId());
            }
            return supplierMapper.updateByPrimaryKeySelective(supplier) == 1;
        }
        return false;
    }

    @Override
    public List<Supplier> selectByName(String name) {
        return supplierMapper.selectByName(name);
    }

    @Override
    @Transactional
    public boolean setSupplierLimit(Supplier supplier, Long adminId) {
        // TODO Auto-generated method stub
        Supplier historySupplier = get(supplier.getId());
        Integer operateType = validateChange(supplier, historySupplier);
        if (!operateType.toString().equals(SupplierOperateLimitType.NO_CHANGE.getValue().toString())) {
            // 1、插入操作变更记录
            SupplierModifyLimitRecord record = createSupplierModifyLimitRecord(supplier, operateType, adminId);
            if (!supplierModifyLimitRecordService.insertSelective(record)) {
                LOGGER.info("插入修改限额变更记录失败, 供应商id = {}, 设置限额标志 = {}, 设置额度  = {}", supplier.getId(),
                        supplier.getLimitMoneyFlag(), supplier.getLimitMoney());
                return false;
            }

            // 2、
            if (operateType.toString().equals(SupplierOperateLimitType.OPEN.getValue().toString())) {
                // 2.1 插入统计记录表
                SupplierReqUsePerDay supplierReqUsePerDay = createSupplierReqUsePerDay(supplier.getId());
                if (!supplierReqUsePerDayService.insertSelective(supplierReqUsePerDay)) {
                    LOGGER.info("插入统计记录表记录失败, 供应商id = {}, 设置限额标志 = {}, 设置额度  = {}", supplier.getId(),
                            supplier.getLimitMoneyFlag(), supplier.getLimitMoney());
                    throw new RuntimeException();
                }
            } else if (operateType.toString().equals(SupplierOperateLimitType.CLOSE.getValue().toString())) {
                // 2.2 删除统计记录
                SupplierReqUsePerDay supplierReqUsePerDay = supplierReqUsePerDayService
                        .getTodayRecord(supplier.getId());
                if (supplierReqUsePerDay != null) {
                    if (!supplierReqUsePerDayService.deleteByPrimaryKey(supplierReqUsePerDay.getId())) {
                        LOGGER.info("删除统计记录表记录失败, 供应商id = {}, 设置限额标志 = {}, 设置额度  = {}", supplier.getId(),
                                supplier.getLimitMoneyFlag(), supplier.getLimitMoney());
                        throw new RuntimeException();
                    }
                }
            }

            // 2、更新供应商
            supplier.setLimitUpdateTime(new Date());
            if (!updateByPrimaryKeySelective(supplier)) {
                LOGGER.info("更新供应商限额失败, 供应商id = {}, 设置限额标志 = {}, 设置额度  = {}", supplier.getId(),
                        supplier.getLimitMoneyFlag(), supplier.getLimitMoney());
                throw new RuntimeException();
            }
        }
        return true;
    }

    // 判断是否更改了限额
    private Integer validateChange(Supplier supplier, Supplier historySupplier) {
        if ((historySupplier.getLimitMoneyFlag() == null
                || historySupplier.getLimitMoneyFlag().toString().equals(SupplierLimitStatus.ON.getCode().toString()))
                && supplier.getLimitMoneyFlag().toString().equals(SupplierLimitStatus.OFF.getCode().toString())) {
            return SupplierOperateLimitType.CLOSE.getValue();
        } else if ((historySupplier.getLimitMoneyFlag() == null
                || historySupplier.getLimitMoneyFlag().toString().equals(SupplierLimitStatus.OFF.getCode().toString()))
                && supplier.getLimitMoneyFlag().toString().equals(SupplierLimitStatus.ON.getCode().toString())) {
            return SupplierOperateLimitType.OPEN.getValue();
        } else if (historySupplier.getLimitMoneyFlag() != null
                && historySupplier.getLimitMoneyFlag().toString().equals(SupplierLimitStatus.ON.getCode().toString())
                && historySupplier.getLimitMoneyFlag().toString().equals(supplier.getLimitMoneyFlag().toString())
                && historySupplier.getLimitMoney().longValue() != supplier.getLimitMoney().longValue()) {
            return SupplierOperateLimitType.MODIFY_LIMIT_MONEY.getValue();
        } else {
            return SupplierOperateLimitType.NO_CHANGE.getValue();
        }
    }

    private SupplierModifyLimitRecord createSupplierModifyLimitRecord(Supplier supplier, Integer operateType,
            Long adminId) {
        SupplierModifyLimitRecord record = new SupplierModifyLimitRecord();
        record.setSupplierId(supplier.getId());
        record.setOperateId(adminId);
        if (supplier.getLimitMoneyFlag().toString().equals(SupplierLimitStatus.OFF.getCode().toString())) {
            record.setLimitMoney(-1D);
        } else {
            record.setLimitMoney(supplier.getLimitMoney());
        }
        record.setOperateType(operateType);
        record.setCreateTime(new Date());
        record.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
        return record;
    }

    private SupplierReqUsePerDay createSupplierReqUsePerDay(Long supplierId) {
        SupplierReqUsePerDay record = new SupplierReqUsePerDay();
        record.setSupplierId(supplierId);
        record.setUsedMoney(0D);
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        record.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
        return record;
    }

    @Override
    public List<Supplier> getSupplierByPrdId(Long prdId) {
        if (prdId == null) {
            return null;
        }
        return supplierMapper.getSupplierByPrdId(prdId);
    }

    @Override
    public Supplier getPriorSupplierByPrdid(Long productId) {
        if (productId == null) {
            return  null;
        }
        List<Supplier> suppliers = supplierMapper.getPriorSupplierByPrdid(productId);
        if (suppliers == null
                || suppliers.size() == 0) {
            return null;
        } else {
            return suppliers.get(0);
        }
    }
}
