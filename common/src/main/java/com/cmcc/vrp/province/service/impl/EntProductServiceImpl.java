package com.cmcc.vrp.province.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmcc.vrp.province.dao.EntProductMapper;
import com.cmcc.vrp.province.model.Account;
import com.cmcc.vrp.province.model.EntProduct;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.ProductTemplateEnterpriseMap;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.util.Constants;
import com.google.gson.Gson;

/**
 * @ClassName: EntProductServiceImpl
 * @Description: TODO
 */
@Service("entProductService")
public class EntProductServiceImpl implements EntProductService {

    private static final Logger logger = LoggerFactory.getLogger(EntProductService.class);

    @Autowired
    EntProductMapper entProductMapper;

    @Autowired
    ProductService productService;

    @Autowired
    EnterprisesService enterprisesService;

    @Autowired
    AccountService accountService;

    @Override
    public List<EntProduct> selectByEnterprizeID(Long enterprizeId) {
        if (enterprizeId == null) {
            return null;
        }
        return entProductMapper.selectByEnterpriseID(enterprizeId);
    }

    /**
     * @Title: selectProIdsByEnterprizeID
     * @Description: TODO
     * @see com.cmcc.vrp.province.service.EntProductService#selectProIdsByEnterprizeID(java.lang.Long)
     */
    @Override
    public List<Long> selectProIdsByEnterprizeID(Long enterprizeId) {
        List<Long> listProIds = new ArrayList<Long>();
        if (enterprizeId == null) {
            return listProIds;
        }

        List<EntProduct> listEntPros = selectByEnterprizeID(enterprizeId);
        for (EntProduct entPro : listEntPros) {
            listProIds.add(entPro.getProductId());
        }

        return listProIds;
    }

    @Override
    public EntProduct selectByProductIDAndEnterprizeID(Long productId, Long enterpriseId) {
        if (productId != null && enterpriseId != null) {
            Map paramMap = new HashMap();
            paramMap.put("productId", productId);
            paramMap.put("enterpriseId", enterpriseId);
            try {
                return entProductMapper.selectByProductIDAndEnterprizeID(paramMap);
            } catch (Exception e) {
                System.out.println(e);
            }
            return null;
        } else {
            return null;
        }
    }

    @Override
    public boolean deleteByProductID(Long productID) {
        if (productID == null) {
            return false;
        }

        entProductMapper.deleteByProductID(productID);
        return true;
    }

    @Override
    public boolean insert(EntProduct record) {
        if (record == null) {
            return false;
        }
        return entProductMapper.insert(record) > 0;
    }

    @Override
    public boolean insertSelective(EntProduct record) {
        if (record == null) {
            return false;
        }
        return entProductMapper.insertSelective(record) > 0;
    }

    /**
     * @Title: updateEnterpriseProduct
     * @Description: 从boss处得到的产品信息更新到数据库 by qh
     * @see com.cmcc.vrp.province.service.EntProductService#updateEnterpriseProduct(java.util.List,
     * java.lang.Long)
     */
    @Override
    public boolean updateEnterpriseProduct(List<String> productCodes, Long enterpriseId) {

        if (enterpriseId == null || productCodes == null) {
            return false;
        }

        //从数据库得到的该企业已有产品的ID
        List<Long> dataProIds = selectProIdsByEnterprizeID(enterpriseId);

        //从Boss出得到的该企业已有产品的ID
        List<Long> bossProIds = productService.selectProIdsByProductCode(productCodes);

        List<Long> copyDataProIds = new ArrayList<Long>();
        copyDataProIds.addAll(dataProIds);

        //这里采用removeAll方法做差集
        dataProIds.removeAll(bossProIds);//得到的是数据库中存在，但Boss没有的记录
        bossProIds.removeAll(copyDataProIds);//得到的是Boss存在，但数据库没有的记录

        for (Long proId : dataProIds) {
            //删除
            EntProduct entProduct = new EntProduct();
            entProduct.setProductId(proId);
            entProduct.setEnterprizeId(enterpriseId);
            entProductMapper.deleteByProIdEnterId(entProduct);
        }

        for (Long proId : bossProIds) {
            //增加
            EntProduct entProduct = new EntProduct();
            entProduct.setProductId(proId);
            entProduct.setEnterprizeId(enterpriseId);
            entProduct.setDeleteFlag(0);
            entProduct.setCreateTime(new Date());
            entProduct.setUpdateTime(new Date());
            insertSelective(entProduct);
        }
        return true;
    }

    @Override
    public List<Product> selectProductByEnterId(Long enterId) {
        if (enterId == null) {
            return null;
        }
        return entProductMapper.selectProductByEnterId(enterId);
    }

    @Override
    public List<Product> selectAllProductByEnterId(Long enterId) {
        // TODO Auto-generated method stub
        if (enterId == null) {
            return null;
        }
        return entProductMapper.selectAllProductByEnterId(enterId);
    }

    @Override
    @Transactional
    public boolean updateEnterProductByRecords(Long enterId, List<EntProduct> entProductAdds,
            List<EntProduct> entProductDeletes, List<EntProduct> entProductUpdates) {
        if (enterId == null) {
            return false;
        }

        List<EntProduct> entProductsWithoutAccount = new ArrayList<EntProduct>();
        //如果有新增加的，先判断有没有账户
        if (entProductAdds != null) {
            for (EntProduct record : entProductAdds) {
                if (!accountService.checkAccountByEntIdAndProductId(record.getEnterprizeId(), record.getProductId())) {
                    entProductsWithoutAccount.add(record);
                }
            }
        }

        //构造account
        List<Account> accounts = createAccounts(entProductsWithoutAccount);

        if (entProductAdds != null && entProductAdds.size() > 0) {
            if (entProductMapper.batchInsert(entProductAdds) != entProductAdds.size()) {
                logger.info("企业id：" + enterId + "插入产品失败.");
                return false;
            }
        }

        if (entProductUpdates != null && entProductUpdates.size() > 0) {
            for (EntProduct entProduct : entProductUpdates) {
                if (!(entProductMapper.updateByProIdEnterId(entProduct) == 1)) {
                    logger.info("企业id：" + enterId + "更新产品折扣失败.");
                    throw new RuntimeException();
                }
            }
        }

        if (entProductDeletes != null && entProductDeletes.size() > 0) {
            for (EntProduct entProduct : entProductDeletes) {
                if (!(entProductMapper.deleteByProIdEnterId(entProduct) == 1)) {
                    logger.info("企业id：" + enterId + "删除产品失败.");
                    throw new RuntimeException();
                }
            }
        }

        //创建账户
        if (accounts != null && accounts.size() > 0) {
            if (!accountService.createProductAccount(accounts)) {
                logger.info("企业id：" + enterId + "创建产品账户失败.");
                throw new RuntimeException();
            }
        }
        return true;
    }

    private List<Account> createAccounts(List<EntProduct> entProductsWithoutAccount) {
        List<Account> accounts = new ArrayList<Account>();
        for (EntProduct record : entProductsWithoutAccount) {
            Account account = new Account();
            account.setEnterId(record.getEnterprizeId());
            account.setProductId(record.getProductId());
            account.setCount(0.0);
            account.setDeleteFlag((byte) Constants.DELETE_FLAG.UNDELETED.getValue());
            account.setMinCount(0.0d);
            account.setOwnerId(record.getEnterprizeId());
            account.setType(-1);
            account.setVersion(0);

            accounts.add(account);
        }
        return accounts;
    }

    @Override
    public boolean updateDiscountByEnterId(Long enterId, Integer discount) {
        return entProductMapper.updateDiscountByEnterId(enterId, discount) >= 0;
    }

    @Override
    public List<Product> productAvailableByEnterId(Long enterId) {
        List<Product> p = entProductMapper.productAvailableByEnterId(enterId);
        return p;
    }

    @Override
    public boolean batchInsertEntProduct(List<EntProduct> records) {
        if (records == null) {
            return false;
        }
        return entProductMapper.batchInsert(records) == records.size();
    }

    @Override
    public boolean batchUpdate(List<EntProduct> list) {
        if (list == null || list.size() <= 0) {
            return true;
        }
        return entProductMapper.batchUpdate(list) > 0;
    }

    @Override
    public List<EntProduct> selectByProductIdWithoutDeleleFlag(Long productId) {
        if (productId == null) {
            return null;
        }
        return entProductMapper.selectByProductIDWithoutDeleteFlag(productId);
    }

    @Override
    public boolean batchDeleteByEnterIdAndProductId(List<EntProduct> entProducts) {
        // TODO Auto-generated method stub
        return entProductMapper.batchDeleteByEnterIdAndProductId(entProducts) >= 0;
    }

    @Override
    public boolean batchDeleteByEnterId(List<ProductTemplateEnterpriseMap> productTemplateEnterpriseMaps) {
        // TODO Auto-generated method stub
        return entProductMapper.batchDeleteByEnterId(productTemplateEnterpriseMaps) >= 0;
    }

    @Override
    public String validateEntAndPrd(Long entId, Long productId) {
        //企业信息校验
        Enterprise enterprise = null;
        if (entId == null || (enterprise = enterprisesService.selectById(entId)) == null) {
            logger.info("企业不存在：entId为{}, productId为{}。", entId, productId);
            return "企业不存在";
        } else if (enterprise.getDeleteFlag().intValue() != Constants.DELETE_FLAG.UNDELETED.getValue()
                || (enterprise.getStartTime() != null && enterprise.getStartTime().after(new Date()))
                || (enterprise.getEndTime() != null && (new Date()).after(enterprise.getEndTime()))) {
            logger.info("企业状态异常:entId为{}, productId为{}, 企业信息为{}.", entId, productId, new Gson().toJson(enterprise));
            return "企业状态异常";
        }

        //产品信息校验
        Product product = null;
        if (productId == null || (product = productService.get(productId)) == null) {
            logger.info("产品不存在:entId为{}, productId为{}.", entId, productId);
            return "产品不存在";
        } else if (Constants.PRODUCT_STATUS.ON.getStatus() != product.getStatus().intValue()
                || product.getDeleteFlag().intValue() != Constants.DELETE_FLAG.UNDELETED.getValue()) {
            logger.info("产品状态异常：:entId为{}, productId为{}, 产品信息为{}.", entId, productId, new Gson().toJson(product));
            return "产品状态异常";
        } else if (selectByProductIDAndEnterprizeID(productId, entId) == null) {
            logger.info("企业未订购该产品：entId为{}, productId为{}。", entId, productId);
            return "企业未订购该产品";
        }

        //校验通过返回NULL,没有错误信息
        return null;
    }

}
