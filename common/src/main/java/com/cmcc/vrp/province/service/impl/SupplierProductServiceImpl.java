/**
 *
 */
package com.cmcc.vrp.province.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cmcc.vrp.enums.AccountType;
import com.cmcc.vrp.enums.OrderOperationStatus;
import com.cmcc.vrp.enums.ProductStatus;
import com.cmcc.vrp.enums.SupplierLimitStatus;
import com.cmcc.vrp.enums.SupplierOperateLimitType;
import com.cmcc.vrp.enums.SupplierStatus;
import com.cmcc.vrp.exception.TransactionException;
import com.cmcc.vrp.province.dao.ShBossProductMapper;
import com.cmcc.vrp.province.dao.SupplierProductMapper;
import com.cmcc.vrp.province.model.Account;
import com.cmcc.vrp.province.model.DiscountRecord;
import com.cmcc.vrp.province.model.EntProduct;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.ProductConverter;
import com.cmcc.vrp.province.model.SdBossProduct;
import com.cmcc.vrp.province.model.ShBossProduct;
import com.cmcc.vrp.province.model.Supplier;
import com.cmcc.vrp.province.model.SupplierProdModifyLimitRecord;
import com.cmcc.vrp.province.model.SupplierProdReqUsePerDay;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.model.SupplierProductMap;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.DiscountRecordService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.ProductConverterService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SdBossProductService;
import com.cmcc.vrp.province.service.SupplierProdModifyLimitRecordService;
import com.cmcc.vrp.province.service.SupplierProdReqUsePerDayService;
import com.cmcc.vrp.province.service.SupplierProductMapService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.province.service.SupplierService;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.Constants.DELETE_FLAG;
import com.cmcc.vrp.util.Constants.FLOW_ACCOUNT_FLAG;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.SizeUnits;

/**
 * @author wujiamin
 */
@Service("supplierProductService")
public class SupplierProductServiceImpl implements SupplierProductService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SupplierProductServiceImpl.class);
    @Autowired
    SupplierProductMapper supplierProductMapper;
    @Autowired
    SupplierProductMapService supplierProductMapService;
    @Autowired
    DiscountRecordService discountRecordService;
    @Autowired
    ProductService productService;
    @Autowired
    EntProductService entProductService;
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    AccountService accountService;
    @Autowired
    ProductConverterService productConverterService;
    @Autowired
    SupplierProdModifyLimitRecordService supplierProdModifyLimitRecordService;
    @Autowired
    SupplierProdReqUsePerDayService supplierProdReqUsePerDayService;
    @Autowired
    SupplierService supplierService;
    @Autowired
    ShBossProductMapper shBossProductMapper;
    @Autowired
    SdBossProductService sdBossProductService;

    @Override
    public boolean insert(SupplierProduct product) {
        product.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
        product.setCreateTime(new Date());
        product.setUpdateTime(new Date());
        product.setLimitMoney(-1D);
        product.setLimitMoneyFlag(SupplierLimitStatus.OFF.getCode());
        return supplierProductMapper.insert(product) == 1;
    }

    /*
     * private boolean validate(SupplierProduct product) { LOGGER.info(
     * "校验待插入的供应商产品参数值...SupplierProduct = {}.",
     * JSONObject.toJSONString(product));
     * 
     * boolean flag = product != null &&
     * StringUtils.isNotBlank(product.getName()) && product.getSupplierId() !=
     * null && StringUtils.isNotBlank(product.getCode()) && product.getSize() !=
     * null && StringUtils.isNotBlank(product.getOwnershipRegion()) &&
     * StringUtils.isNotBlank(product.getRoamingRegion()) && product.getPrice()
     * != null && product.getStatus() != null &&
     * StringUtils.isNotBlank(product.getIsp()) &&
     * selectByCode(product.getCode()) == null;
     * 
     * LOGGER.info("校验结果为{}.", flag); return flag; }
     */

    @Override
    @Transactional
    public boolean deleteSupplierProduct(Long productId) {
        // 逻辑删除供应商产品
        // 删除供应商产品、平台产品关系
        SupplierProduct product = new SupplierProduct();
        product.setId(productId);
        product.setDeleteFlag(Constants.DELETE_FLAG.DELETED.getValue());
        product.setUpdateTime(new Date());
        if (updateByPrimaryKey(product) && supplierProductMapService.deleteBySplPid(productId)) {
            return true;
        }

        throw new RuntimeException();
    }

    @Override
    public boolean updateByPrimaryKey(SupplierProduct product) {
        if (product.getStatus() != null
                && product.getStatus().intValue()==SupplierStatus.OFF.getCode()) {
            supplierProductMapService.clearPriorSupplierBySplId(product.getId());
        }
        product.setUpdateTime(new Date());
        return supplierProductMapper.updateByPrimaryKeySelective(product) > 0;
    }

    @Override
    public SupplierProduct selectByPrimaryKey(Long id) {

        return supplierProductMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SupplierProduct> selectBySupplierId(Long supplierId) {
        if (supplierId == null) {
            return null;
        }
        return supplierProductMapper.selectBySupplierId(supplierId);
    }

    @Override
    public List<SupplierProduct> selectByName(String name) {
        if (StringUtils.isEmpty(name)) {
            return new ArrayList<SupplierProduct>();
        }
        return supplierProductMapper.selectByName(name);
    }

    @Override
    public SupplierProduct selectByCode(String code) {
        if (code == null) {
            return null;
        }
        return supplierProductMapper.selectByCode(code);
    }

    @Override
    public List<SupplierProduct> queryPaginationSupplierProduct(QueryObject queryObject) {
        Map map = queryObject.toMap();
        String sizeStr = (String) map.get("size");
        String unit = (String) map.get("unit");
        if (StringUtils.isNotBlank(sizeStr)) {
            Long size = Long.parseLong(sizeStr);
            if ("MB".equals(unit)) {
                size = SizeUnits.MB.toKB(size);
            } else if ("GB".equals(unit)) {
                size = SizeUnits.GB.toKB(size);
            }
            map.put("size", size.intValue());
        }
        return supplierProductMapper.queryPaginationSupplierProduct(map);
    }

    @Override
    public int queryPaginationSupplierProductCount(QueryObject queryObject) {
        Map map = queryObject.toMap();
        String sizeStr = (String) map.get("size");
        String unit = (String) map.get("unit");
        if (StringUtils.isNotBlank(sizeStr)) {
            Long size = Long.parseLong(sizeStr);
            if ("MB".equals(unit)) {
                size = SizeUnits.MB.toKB(size);
            } else if ("GB".equals(unit)) {
                size = SizeUnits.GB.toKB(size);
            }
            map.put("size", size.intValue());
        }
        return supplierProductMapper.queryPaginationSupplierProductCount(map);
    }

    @Override
    public List<SupplierProduct> queryPaginationSupplierProduct2PltProduct(QueryObject queryObject) {
        return supplierProductMapper.queryPaginationSupplierProduct2PltProduct(queryObject.toMap());
    }

    @Override
    public int queryPaginationSupplierProduct2PltProductCount(QueryObject queryObject) {
        return supplierProductMapper.queryPaginationSupplierProduct2PltProductCount(queryObject.toMap());
    }

    @Override
    public int querySupplierProductAvailableCount(QueryObject queryObject) {
        Map map = queryObject.toMap();
        String sizeStr = (String) map.get("size");
        if (StringUtils.isNotBlank(sizeStr)) {
            Long size = Long.parseLong(sizeStr);
            map.put("size", size.intValue());
        }
        return supplierProductMapper.querySupplierProductAvailableCount(map);
    }

    @Override
    public List<SupplierProduct> querySupplierProductAvailable(QueryObject queryObject) {
        Map map = queryObject.toMap();
        String sizeStr = (String) map.get("size");
        if (StringUtils.isNotBlank(sizeStr)) {
            Long size = Long.parseLong(sizeStr);
            map.put("size", size.intValue());
        }
        return supplierProductMapper.querySupplierProductAvailable(map);
    }

    @Override
    public SupplierProduct selectByFeature(String feature) {
        return supplierProductMapper.selectByFeature(feature);
    }

    @Override
    public boolean batchInsertSupplierProduct(List<SupplierProduct> list) {
        if (list == null) {
            return false;
        }
        return supplierProductMapper.batchInsertSupplierProduct(list) == list.size();
    }

    @Override
    public List<SupplierProduct> selectSupplierByCreateTime(Date createTime) {
        if (createTime == null) {
            return null;
        }
        return supplierProductMapper.selectSupplierByCreateTime(createTime);
    }

    @Override
    public SupplierProduct selectByCodeAndSupplierId(String code, Long supplierId) {
        if (StringUtils.isBlank(code) || supplierId == null) {
            return null;
        }
        return supplierProductMapper.selectByCodeAndSupplierId(code, supplierId);
    }

    @Override
    @Transactional
    public boolean updateSupplierProduct(List<EntProduct> epList, List<SupplierProductMap> spmList,
            List<Product> pList, List<SupplierProduct> spList, List<DiscountRecord> records) {
        // 参数校验
        if (epList == null || spmList == null || pList == null || spList == null || records == null) {
            return false;
        }
        // 批量更新供应商产品
        if (!batchUpdate(spList)) {
            throw new TransactionException("批量更新供应商产品信息时发生异常，事务回滚！");
        }

        // 批量更新供应商产品与平台产品的关联关系
        if (!supplierProductMapService.batchUpdate(spmList)) {
            throw new TransactionException("批量更新供应商产品与平台产品关联关系时发生异常，事务回滚！");
        }

        // 批量更新平台产品
        if (!productService.batchUpdate(pList)) {
            throw new TransactionException("批量更新供平台产品信息时发生异常，事务回滚！");
        }

        // 批量更新平台产品与企业的关联关系
        if (!entProductService.batchUpdate(epList)) {
            throw new TransactionException("批量更新供平台产品与企业关联关系时发生异常，事务回滚！");
        }

        // 批量生成折扣记录信息
        if (!discountRecordService.batchInsert(records)) {
            throw new TransactionException("批量生成折扣记录时发生异常，事务回滚！");
        }

        return true;
    }

    @Override
    public boolean createOrUpdateSupplierProduct(String enterpriseCode, List<SupplierProduct> newProducts,
            List<SupplierProduct> updateProducts) {

        // 参数校验
        if (StringUtils.isBlank(enterpriseCode)) {
            LOGGER.error("新增或更新订单时失败：企业信息不存在， code = " + enterpriseCode);
            return false;
        }

        Enterprise enterprise = enterprisesService.selectByCode(enterpriseCode);
        if (enterprise == null) {
            LOGGER.error("新增或更新订单时失败：企业信息不存在， code = " + enterpriseCode);
            return false;
        }

        boolean createResult = false;// 新增操作结果
        boolean updateResult = false;// 更新操作结果

        // 新增操作
        if (newProducts != null && newProducts.size() > 0) {
            createResult = createNewSupplierProduct(enterprise.getId(), newProducts);
        }

        // 更新操作
        if (updateProducts != null && updateProducts.size() > 0) {
            updateResult = updateSulliperProduct(enterprise.getId(), updateProducts);
        }

        return createResult || updateResult;
    }

    @Override
    public boolean updateSulliperProduct(Long entId, List<SupplierProduct> updateProducts) {
        // 参数校验
        if (entId == null) {
            return false;
        }

        if (updateProducts == null || updateProducts.size() <= 0) {
            return true;
        }

        // 更新企业与平台产品关联关系
        List<EntProduct> entProducts = new ArrayList<EntProduct>();
        List<Product> proList = new LinkedList<Product>();
        for (SupplierProduct sp : updateProducts) {

            List<Product> tempList = productService.getBySplIdWithoutDel(sp.getId());
            if (tempList == null || tempList.size() <= 0) {
                continue;
            }

            for (Product p : tempList) {
                if (!OrderOperationStatus.DELETE.getOpStatus().equals(p.getSupplierOpStatus())) {//删除状态不更新平台产品
                    p.setDeleteFlag(sp.getDeleteFlag());
                }
            }

            proList.addAll(tempList);

            List<EntProduct> entProductList = entProductService.selectByProductIdWithoutDeleleFlag(tempList.get(0)
                    .getId());
            if (entProductList != null && entProductList.size() > 0) {
                for (EntProduct entProduct : entProductList) {
                    entProduct.setDeleteFlag(sp.getDeleteFlag());
                    entProduct.setDiscount(sp.getDiscount());
                    entProduct.setUpdateTime(new Date());
                    entProducts.add(entProduct);
                }
            }
        }
        if (!entProductService.batchUpdate(entProducts)) {
            LOGGER.error("批量更新企业与平台产品关联关系时发生异常，事务回滚！entId = " + entId);
            throw new TransactionException("批量更新企业与平台产品关联关系时发生异常，事务回滚！entId = " + entId);
        }

        // 批量更新产品信息
        if (!productService.batchUpdate(proList)) {
            LOGGER.error("批量更新平台产品信息时发生异常，事务回滚！entId = " + entId);
            throw new TransactionException("批量更新平台产品信息时发生异常，事务回滚，事务回滚！entId = " + entId);
        }

        // 批量更新供应商产品信息
        if (!batchUpdate(updateProducts)) {
            LOGGER.error("批量更新更新供应商产品信息时发生异常，事务回滚！entId = " + entId);
            throw new TransactionException("批量更新更新供应商产品信息时发生异常，事务回滚！entId = " + entId);
        }

        // 批量生成折扣记录信息
        List<DiscountRecord> discountRecordList = new ArrayList<DiscountRecord>();
        DiscountRecord discountRecord = null;
        for (SupplierProduct sp : updateProducts) {
            discountRecord = new DiscountRecord();
            discountRecord.setUserId(sp.getName());
            discountRecord.setPrdCode(sp.getCode());
            discountRecord.setDiscount(sp.getDiscount());
            discountRecord.setCreateTime(new Date());
            discountRecord.setUpdateTime(new Date());
            discountRecord.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());
            discountRecordList.add(discountRecord);
        }
        if (!discountRecordService.batchInsert(discountRecordList)) {
            LOGGER.error("批量生成折扣记录时发生异常，事务回滚！entId = " + entId);
            throw new TransactionException("批量生成折扣记录时发生异常，事务回滚！entId = " + entId);
        }
        return true;

    }

    @Override
    @Transactional
    public boolean createNewSupplierProduct(Long entId, List<SupplierProduct> newProducts) {
        // 参数校验
        if (entId == null || newProducts == null || newProducts.size() <= 0) {
            return false;
        }

        for (SupplierProduct sp : newProducts) {
            SdBossProduct sdBossProduct = sdBossProductService.selectByCode(sp.getCode());
            if (sdBossProduct == null) {
                continue;
            }
            Product product = new Product();
            product.setProductCode(sp.getName() + sdBossProduct.getCode());// 设置平台产品编码
            product.setName(sdBossProduct.getName());// 设置平台产品名称
            product.setStatus(ProductStatus.NORMAL.getCode());// 产品状态：默认上架
            product.setCreateTime(new Date());
            product.setUpdateTime(new Date());
            product.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());// 默认未删除
            product.setPrice(sdBossProduct.getPrice().intValue());
            product.setIsp(sp.getIsp());
            product.setRoamingRegion(sp.getRoamingRegion());
            product.setOwnershipRegion(sp.getOwnershipRegion());
            product.setProductSize(sdBossProduct.getSize());
            product.setFlowAccountFlag(FLOW_ACCOUNT_FLAG.REAL_PRODUCT.getCode());// 真实产品
            product.setType(sdBossProduct.getType());
            if (!createNewSupplierProduct(entId, sp, product)) {
                throw new TransactionException("订单信息处理处理失败，事务回滚！");
            }
        }
        return true;
    }

    @Override
    public boolean batchUpdate(List<SupplierProduct> list) {
        if (list == null || list.size() <= 0) {
            return true;
        }
        return supplierProductMapper.batchUpdate(list) > 0;
    }

    @Override
    public List<SupplierProduct> selectByNameWithOutDeleteFlag(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        return supplierProductMapper.selectByNameWithoutDeleteFlag(name);
    }

    @Override
    public List<SupplierProduct> selectByNameAndCodeOrStatusOrDeleteFlag(String name, String code, Integer status,
            Integer deleteFlag) {
        if (StringUtils.isBlank(name) || StringUtils.isBlank(code)) {
            return null;
        }
        return supplierProductMapper.selectByNameAndCodeOrStatusOrDeleteFlag(name, code, status, deleteFlag);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean createNewSupplierProduct(Long entId, SupplierProduct supplierProduct, Product product) {
        // 参数校验
        if (entId == null || supplierProduct == null || product == null) {
            return false;
        }

        // 生成供应商产品
        if (!insert(supplierProduct)) {
            throw new TransactionException("生成供应商产品时发生异常，事务回滚。");
        }

        // 生成平台产品
        product.setName(product.getName() + supplierProduct.getId());
        if (!productService.insertProduct(product)) {
            throw new TransactionException("生成平台产品时发生异常，事务回滚。");
        }

        // 建立账户转换关系：流量包产品与资金账户建立关系；预付费产品与预付费账户建立关系
        if (com.cmcc.vrp.ec.bean.Constants.ProductType.FLOW_PACKAGE.getValue() == product.getType().byteValue()
                || com.cmcc.vrp.ec.bean.Constants.ProductType.PRE_PAY_CURRENCY.getValue() == product.getType().byteValue()
                || com.cmcc.vrp.ec.bean.Constants.ProductType.PRE_PAY_PRODUCT.getValue() == product.getType().byteValue()) {

            //流量包产品，查找资金账户
            List<Product> list = null;
            if (com.cmcc.vrp.ec.bean.Constants.ProductType.FLOW_PACKAGE.getValue() == product.getType().byteValue()) {
                list = productService.selectByType(Integer.valueOf(com.cmcc.vrp.ec.bean.Constants.ProductType.CURRENCY
                        .getValue()));
            } else{
               //预付费产品，查找预付费账户
                list = productService.selectByType(Integer.valueOf(com.cmcc.vrp.ec.bean.Constants.ProductType.PRE_PAY_CURRENCY
                        .getValue()));
            }
            Product accountProduct = null;
            //查找指定账户类型的产品
            if (list != null && list.size() > 0) {
                accountProduct = list.get(0);
            }

            //指定类型的账户不存在，直接返回
            if (accountProduct == null) {
                throw new TransactionException("生成供应商产品时发生异常，指定类型（type = " + product.getType() + ")产品不存在");
            } else {
                Integer accountProductType = accountProduct.getType();
                //创建预付费产品账户
                if ((com.cmcc.vrp.ec.bean.Constants.ProductType.PRE_PAY_CURRENCY.getValue() == accountProductType.byteValue() ||
                        com.cmcc.vrp.ec.bean.Constants.ProductType.PRE_PAY_PRODUCT.getValue() == accountProductType.byteValue())
                        && accountService.get(entId, accountProduct.getId(), AccountType.ENTERPRISE.getValue()) == null) {//账户不存在则新建账户
                    Map<Long, Double> infos = new HashMap<Long, Double>();
                    infos.put(accountProduct.getId(), 0.0);//默认账户余额为0.0
                    if (accountService.createEnterAccount(entId, infos,
                            DigestUtils.md5Hex(UUID.randomUUID().toString()))) {
                        LOGGER.info("创建企业帐户成功.");
                    } else {
                        LOGGER.info("创建企业账户失败.");
                        throw new TransactionException("创建企业账户失败，事务回滚。");
                    }
                }
            }

            //建立账户关联关系
            ProductConverter productConverter = new ProductConverter();
            productConverter.setSourcePrdId(product.getId());
            productConverter.setDestPrdId(accountProduct.getId());
            productConverter.setCreateTime(new Date());
            productConverter.setUpdateTime(new Date());
            productConverter.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());
            productConverter.setSourcePrdName(product.getName());
            productConverter.setDestPrdName(accountProduct.getName());
            productConverter.setSourcePrdCode(product.getProductCode());
            productConverter.setSourcePrdSize(product.getProductSize().intValue());
            productConverter.setSourcePrdPrice(product.getPrice());
            productConverter.setSourceIsp(supplierProduct.getIsp());
            if (!productConverterService.insert(productConverter)) {
                throw new TransactionException("生成供应商产品时发生异常，建立指定类型产品（type = " + product.getType() + ")账户关系时失败");
            }
        }

        // 生成供应商产品与平台产品关联关系
        SupplierProductMap supplierProductMap = new SupplierProductMap();
        supplierProductMap.setCreateTime(new Date());
        supplierProductMap.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());
        supplierProductMap.setUpdateTime(new Date());
        supplierProductMap.setPlatformProductId(product.getId());
        supplierProductMap.setSupplierProductId(supplierProduct.getId());
        if (!supplierProductMapService.create(supplierProductMap)) {
            throw new TransactionException("生成平台产品与供应商产品关联关系时发生异常，事务回滚。");
        }

        // 生成企业与平台产品关联关系
        EntProduct entProduct = new EntProduct();
        entProduct.setCreateTime(new Date());
        entProduct.setProductId(product.getId());
        entProduct.setEnterprizeId(entId);
        entProduct.setDiscount(supplierProduct.getDiscount());
        entProduct.setCreateTime(new Date());
        entProduct.setUpdateTime(new Date());
        entProduct.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());
        if (!entProductService.insert(entProduct)) {
            throw new TransactionException("生成平台产品与供应商产品关联关系时发生异常，事务回滚。");
        }

        // 生成折扣记录信息
        Date date = new Date();
        DiscountRecord discountRecord = new DiscountRecord();
        discountRecord.setUserId(supplierProduct.getName());
        discountRecord.setPrdCode(supplierProduct.getCode());
        discountRecord.setDiscount(supplierProduct.getDiscount());
        discountRecord.setCreateTime(date);
        discountRecord.setUpdateTime(date);
        discountRecord.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());
        if (com.cmcc.vrp.ec.bean.Constants.ProductType.PRE_PAY_PRODUCT.getValue() == product.getType().byteValue()) {//预付费产品折扣下月生效
            try {
                //计算下月月初时间
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int nextMonth = calendar.get(Calendar.MONTH) + 2;
                String nextDateStr = year + "-" + nextMonth % 12 + "-01 00:00:00";
                discountRecord.setValidateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(nextDateStr));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            discountRecord.setValidateTime(date);
        }

        if (!discountRecordService.insert(discountRecord)) {
            throw new TransactionException("生成平台折扣记录信息时发生异常，事务回滚。");
        }

        // 生成产品账户信息
        Account account = new Account();
        account.setEnterId(entId);
        account.setProductId(product.getId());
        account.setCount(0.0);
        account.setMinCount(0.0);
        account.setOwnerId(entId);
        account.setCreateTime(new Date());
        account.setUpdateTime(new Date());
        account.setDeleteFlag((byte) Constants.DELETE_FLAG.UNDELETED.getValue());
        account.setType(AccountType.ENTERPRISE.getValue());
        account.setVersion(0);
        ArrayList<Account> list = new ArrayList<Account>();
        list.add(account);
        if (!accountService.createProductAccount(list)) {
            throw new TransactionException("生成产品账户时发生异常，事务回滚。");
        }

        return true;
    }

    @Override
    public SupplierProduct selectById(Long id) {
        return supplierProductMapper.selectById(id);
    }

    @Override
    public boolean deleteBysupplierId(Long supplierId) {
        // TODO Auto-generated method stub
        if (supplierId == null) {
            LOGGER.error("无效的供应商ID，删除供应商产品失败.");
            return false;
        }
        return supplierProductMapper.deleteBySupplierId(supplierId) >= 0;
    }

    @Override
    public List<SupplierProduct> selectAllSupplierProducts() {
        // TODO Auto-generated method stub
        return supplierProductMapper.selectAllSupplierProducts();
    }

    @Override
    public SupplierProduct selectOnshelfByPrimaryKey(Long id) {
        return supplierProductMapper.selectOnshelfByPrimaryKey(id);
    }

    @Override
    public List<SupplierProduct> selectByMap(Map map) {
        // TODO Auto-generated method stub
        return supplierProductMapper.selectByMap(map);
    }

    @Override
    @Transactional
    public boolean setProductSupplierLimit(SupplierProduct supplierProduct, Long adminId) {
        // TODO Auto-generated method stub
        SupplierProduct historySupplierProd = selectById(supplierProduct.getId());
        Integer operateType = validateChange(supplierProduct, historySupplierProd);
        if (!operateType.toString().equals(SupplierOperateLimitType.NO_CHANGE.getValue().toString())) {
            // 1、插入供应商产品变更记录
            SupplierProdModifyLimitRecord record = createSupplierModifyLimitRecord(supplierProduct, operateType,
                    adminId);
            if (!supplierProdModifyLimitRecordService.insertSelective(record)) {
                LOGGER.info("插入供应商产品限额变更记录失败，供应商产品id = {}, 限额标志位 = {}, 限额额度  = {}", supplierProduct.getId(),
                        supplierProduct.getLimitMoneyFlag(), supplierProduct.getLimitMoney());
                return false;
            }

            // 2
            if (operateType.toString().equals(SupplierOperateLimitType.OPEN.getValue().toString())) {
                // 2.1 插入供应商产品统计记录
                SupplierProdReqUsePerDay supplierProdReqUsePerDay = createSupplierProdReqUsePerDay(supplierProduct
                        .getId());
                if (!supplierProdReqUsePerDayService.insertSelective(supplierProdReqUsePerDay)) {
                    LOGGER.info("插入供应商产品统计记录失败，供应商产品id = {}, 限额标志位 = {}, 限额额度  = {}", supplierProduct.getId(),
                            supplierProduct.getLimitMoneyFlag(), supplierProduct.getLimitMoney());
                    throw new RuntimeException();
                }
            } else if (operateType.toString().equals(SupplierOperateLimitType.CLOSE.getValue().toString())) {
                // 2.2 删除供应商产品统计记录失败
                SupplierProdReqUsePerDay supplierProdReqUsePerDay = supplierProdReqUsePerDayService
                        .getTodayRecord(supplierProduct.getId());
                if (supplierProdReqUsePerDay != null) {
                    if (!supplierProdReqUsePerDayService.deleteByPrimaryKey(supplierProdReqUsePerDay.getId())) {
                        LOGGER.info("删除供应商产品统计记录失败，供应商产品id = {}, 限额标志位 = {}, 限额额度  = {}", supplierProduct.getId(),
                                supplierProduct.getLimitMoneyFlag(), supplierProduct.getLimitMoney());
                        throw new RuntimeException();
                    }
                }
            }

            if (!updateByPrimaryKey(supplierProduct)) {
                LOGGER.info("设置供应商产品限额失败，供应商产品id = {}, 限额标志位 = {}, 限额额度  = {}", supplierProduct.getId(),
                        supplierProduct.getLimitMoneyFlag(), supplierProduct.getLimitMoney());
                throw new RuntimeException();
            }

            Supplier supplier = new Supplier();
            supplier.setId(historySupplierProd.getSupplierId());
            supplier.setLimitUpdateTime(new Date());
            if (!supplierService.updateByPrimaryKeySelective(supplier)) {
                LOGGER.info("设置供应商产品限额失败，供应商产品id = {}, 限额标志位 = {}, 限额额度  = {}, 原因：更新操作时间异常", supplierProduct.getId(),
                        supplierProduct.getLimitMoneyFlag(), supplierProduct.getLimitMoney());
                throw new RuntimeException();
            }
        }
        return true;
    }

    // 判断是否更改了限额
    private Integer validateChange(SupplierProduct supplierProduct, SupplierProduct historySupplierProd) {
        if ((historySupplierProd.getLimitMoneyFlag() == null || historySupplierProd.getLimitMoneyFlag().toString()
                .equals(SupplierLimitStatus.ON.getCode().toString()))
                && supplierProduct.getLimitMoneyFlag().toString().equals(SupplierLimitStatus.OFF.getCode().toString())) {
            return SupplierOperateLimitType.CLOSE.getValue();
        } else if ((historySupplierProd.getLimitMoneyFlag() == null || historySupplierProd.getLimitMoneyFlag()
                .toString().equals(SupplierLimitStatus.OFF.getCode().toString()))
                && supplierProduct.getLimitMoneyFlag().toString().equals(SupplierLimitStatus.ON.getCode().toString())) {
            return SupplierOperateLimitType.OPEN.getValue();
        } else if (historySupplierProd.getLimitMoneyFlag() != null
                && historySupplierProd.getLimitMoneyFlag().toString()
                        .equals(SupplierLimitStatus.ON.getCode().toString())
                && historySupplierProd.getLimitMoneyFlag().toString()
                        .equals(supplierProduct.getLimitMoneyFlag().toString())
                && historySupplierProd.getLimitMoney().longValue() != supplierProduct.getLimitMoney().longValue()) {
            return SupplierOperateLimitType.MODIFY_LIMIT_MONEY.getValue();
        } else {
            return SupplierOperateLimitType.NO_CHANGE.getValue();
        }
    }

    private SupplierProdModifyLimitRecord createSupplierModifyLimitRecord(SupplierProduct supplierProduct,
            Integer operateType, Long adminId) {
        SupplierProdModifyLimitRecord record = new SupplierProdModifyLimitRecord();
        record.setSupplierProductId(supplierProduct.getId());
        record.setOperateId(adminId);
        if (supplierProduct.getLimitMoneyFlag().toString().equals(SupplierLimitStatus.OFF.getCode().toString())) {
            record.setLimitMoney(null);
        } else {
            record.setLimitMoney(supplierProduct.getLimitMoney());
        }
        record.setOperateType(operateType);
        record.setCreateTime(new Date());
        record.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
        return record;
    }

    private SupplierProdReqUsePerDay createSupplierProdReqUsePerDay(Long supplierProductId) {
        SupplierProdReqUsePerDay record = new SupplierProdReqUsePerDay();
        record.setSupplierProductId(supplierProductId);
        record.setUseMoney(0D);
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        record.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
        return record;
    }

    @Override
    public List<SupplierProduct> getByNameOrCodeOrOpStatus(String name, String code, Integer opStatus) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("code", code);
        map.put("opStatus", opStatus);
        return supplierProductMapper.getByNameOrCodeOrOpStatus(map);
    }

    @Override
    public List<ShBossProduct> getShBossProducts() {

        return shBossProductMapper.getShBossProducts();
    }

    @Override
    public List<ShBossProduct> getShBossProductsByOrderType(String orderType) {
        return shBossProductMapper.getShBossProductsByOrderType(orderType);
    }
}
