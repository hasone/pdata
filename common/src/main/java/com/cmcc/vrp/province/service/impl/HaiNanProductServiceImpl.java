package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.enums.ProductStatus;
import com.cmcc.vrp.exception.TransactionException;
import com.cmcc.vrp.province.dao.ProductMapper;
import com.cmcc.vrp.province.model.EntProduct;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.model.SupplierProductMap;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SupplierProductMapService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.province.service.SupplierService;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.SizeUnits;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class HaiNanProductServiceImpl extends ProductServiceImpl {
    @Autowired
    @Qualifier("haiNanBossService")
    BossService bossService;
    @Autowired
    private EnterprisesService enterprisesService;
    @Autowired
    private EntProductService entProductService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private SupplierProductService supplierProductService;
    @Autowired
    private SupplierProductMapService supplierProductMapService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductMapper productMapper;

    /**
     * 海南BOSS端没有提供企业账户查询接口，企业账户信息由BOSS端主动调用平台提供的订购接口同步至平台账户
     */
    @Override
    public List<Product> selectAllProductsByEnterId(Long enterpriseId) {
        if (enterpriseId == null) {
            return null;
        }
        return entProductService.selectProductByEnterId(enterpriseId);
    }

    /**
     * 海南BOSS端产品信息{"GRP_ID":"XXXX","PAK_MONEY":"XXXX","PAK_GPRS":"XXXX"},附加：供应商ID
     */
    @Override
    @Transactional
    public boolean createNewProduct(Map<String, Object> map) {

        //0.获取参数
        String grpID = (String) map.get("GRP_ID");
        String pakMoney = (String) map.get("PAK_MONEY");
        String pakGPRS = (String) map.get("PAK_GPRS");

        //获取供应商ID
        Long ispId = (Long) map.get("ispId");

        String feature = "{\"GRP_ID\":\"" + grpID + "\",\"PAK_MONEY\":\"" + pakMoney + "\",\"PAK_GPRS\":\"" + pakGPRS + "\"}";

        //1.校验参数
        if (StringUtils.isBlank(grpID) || StringUtils.isBlank(pakMoney) || StringUtils.isBlank(pakGPRS)) {
            return false;
        }

        Enterprise enterprise = enterprisesService.selectByCode(grpID);
        if (enterprise == null) {
            return false;
        }

        //2.向suplier_product插入记录，供应商产品
        SupplierProduct supplierProduct = new SupplierProduct();
        supplierProduct.setSupplierId(ispId);//供应商
        supplierProduct.setName(String.valueOf(NumberUtils.toInt(pakMoney) * 1.0 / 100) + "元" + pakGPRS + "MB流量包");
        supplierProduct.setIsp("M");//默认为移动
        supplierProduct.setCode(SerialNumGenerator.buildSerialNum());
        supplierProduct.setSize(SizeUnits.MB.toKB(NumberUtils.toLong(pakGPRS)));//单位为MB，转换为KB
        supplierProduct.setOwnershipRegion("海南");
        supplierProduct.setRoamingRegion("海南");
        supplierProduct.setPrice(NumberUtils.toInt(pakMoney));//单位为分
        supplierProduct.setFeature(feature);
        supplierProduct.setStatus(ProductStatus.NORMAL.getCode());//默认上架
        supplierProduct.setCreateTime(new Date());
        supplierProduct.setUpdateTime(new Date());
        supplierProduct.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
        if (!supplierProductService.insert(supplierProduct)) {
            throw new TransactionException();
        }

        //3.向product表插入记录，平台产品
        Product product = productService.selectByProductName(supplierProduct.getName());
        if (product == null) {
            product = new Product();
            product.setProductCode(supplierProduct.getCode());//产品编码
            product.setType(new Integer(com.cmcc.vrp.ec.bean.Constants.ProductType.FLOW_PACKAGE.getValue()));//产品类型：流量包产品
            product.setName(supplierProduct.getName());//名称
            product.setStatus(ProductStatus.NORMAL.getCode());//默认上架
            product.setCreateTime(new Date());
            product.setUpdateTime(new Date());
            product.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
            product.setPrice(supplierProduct.getPrice());

            product.setIsp(supplierProduct.getIsp());
            product.setOwnershipRegion(supplierProduct.getOwnershipRegion());
            product.setRoamingRegion(supplierProduct.getRoamingRegion());
            product.setProductSize(supplierProduct.getSize());

            product.setIsp(supplierProduct.getIsp());

            if (productMapper.insertSelective(product) == 0) {
                throw new TransactionException();
            }
        }

        //4.向supplier_product_map表插入记录，将平台产品与供应商产品关联
        SupplierProductMap supplierProductMap = new SupplierProductMap();
        supplierProductMap.setCreateTime(new Date());
        supplierProductMap.setPlatformProductId(product.getId());
        supplierProductMap.setSupplierProductId(supplierProduct.getId());
        supplierProductMap.setUpdateTime(new Date());
        supplierProductMap.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
        if (!supplierProductMapService.create(supplierProductMap)) {
            throw new TransactionException();
        }

        //5.向ent_product表插入记录，将企业跟产品关联
        EntProduct entProduct = new EntProduct();
        entProduct.setUpdateTime(new Date());
        entProduct.setCreateTime(new Date());
        entProduct.setDiscount(100);
        entProduct.setEnterprizeId(enterprise.getId());
        entProduct.setProductId(product.getId());
        entProduct.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
        if (!entProductService.insert(entProduct)) {
            throw new TransactionException();
        }
        return true;
    }
}
