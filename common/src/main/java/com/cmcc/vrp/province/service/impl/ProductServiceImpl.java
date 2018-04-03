package com.cmcc.vrp.province.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.cmcc.vrp.boss.chongqing.CQBossServiceImpl;
import com.cmcc.vrp.enums.AccountType;
import com.cmcc.vrp.enums.ProductStatus;
import com.cmcc.vrp.exception.TransactionException;
import com.cmcc.vrp.province.dao.ProductMapper;
import com.cmcc.vrp.province.model.Account;
import com.cmcc.vrp.province.model.EntProduct;
import com.cmcc.vrp.province.model.EntSyncList;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.PrizeInfo;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.model.SupplierProductMap;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EntSyncListService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SupplierProductAccountService;
import com.cmcc.vrp.province.service.SupplierProductMapService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.SizeUnits;

/**
 * 平台产品服务
 */
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private EntProductService entProductService;

    @Autowired
    private SupplierProductMapService supplierProductMapService;

    @Autowired
    private EnterprisesService enterprisesService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private SupplierProductService supplierProductService;

    @Autowired
    private SupplierProductAccountService supplierProductAccountService;

    @Autowired
    EntSyncListService entSyncListService;
    
    @Autowired
    private GlobalConfigService globalConfigService;

    /**
     * @param queryObject
     * @return
     * @Title: list
     * @Description: 产品列表
     * @see com.cmcc.vrp.province.service.ProductService#list(com.cmcc.vrp.util.QueryObject)
     */
    @Override
    public List<Product> list(QueryObject queryObject) {
        if (queryObject == null) {
            return null;
        }

        Map<String, Object> map = queryObject.toMap();
        String sizeStr = (String) map.get("productSize");
        String unit = (String) map.get("unit");
        if (StringUtils.isNotBlank(sizeStr)) {
            Long size = Long.parseLong(sizeStr);
            if ("MB".equals(unit)) {
                size = SizeUnits.MB.toKB(size);
            } else if ("GB".equals(unit)) {
                size = SizeUnits.GB.toKB(size);
            }
            map.put("productSize", size.intValue());
        }

        return productMapper.getProductList(map);
    }

    /**
     * @param queryObject
     * @return
     * @Title: queryCount
     * @Description: 获取符合查询的产品数
     */
    @Override
    public int getProductCount(QueryObject queryObject) {
        if (queryObject == null) {
            return 0;
        }
        Map map = queryObject.toMap();
        String sizeStr = (String) map.get("productSize");
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
        return productMapper.getProductCount(map);
    }

    /**
     * @param id 产品ID
     * @return
     * @Title: delete
     * @Description: 删除产品
     */
    @Override
    public boolean delete(Long id) {
        if (id == null) {
            return false;
        }
        Product product = productMapper.selectByPrimaryKey(id);
        if (product != null) {
            //product.setDeleteFlag(Constants.DELETE_FLAG.DELETED.getValue());
            product.setDeleteFlag(1);
            return productMapper.updateByPrimaryKeySelective(product) == 1;
        }

        return false;
    }

    /**
     * @param id
     * @return
     * @Title: selectProductById
     * @Description: 根据id查找产品
     * @see com.cmcc.vrp.province.service.ProductService#selectProductById(java.lang.Long)
     */
    @Override
    public Product selectProductById(Long id) {
        if (id == null) {
            return null;
        }
        return productMapper.selectByPrimaryKey(id);
    }

    /**
     * @param code
     * @return
     * @Title: selectByCode
     * @Description: TODO
     * @see com.cmcc.vrp.province.service.ProductService#selectByCode(java.lang.String)
     */
    @Override
    public Product selectByCode(String code) {
        if (code == null) {
            return null;
        }
        List<Product> listProducts = productMapper.selectByCode(code);
        return (listProducts != null && listProducts.size() > 0) ? listProducts.get(0) : null;
    }

    /**
     * @param product
     * @return
     * @Title: insertProduct
     * @Description: 插入产品
     * @see com.cmcc.vrp.province.service.ProductService#insertProduct(com.cmcc.vrp.province.model.Product)
     */
    @Override
    public boolean insertProduct(Product product) {

        if (product == null) {
            return false;
        }

        // 2.检查重复问题
        // 表中已有该产品编码的平台产品
        Product p = selectByCode(product.getProductCode());
        if (p != null && p.getProductCode().equals(product.getProductCode())) {
            return false;
        }
        /*Product p = selectByProductName(product.getName());
        if (p != null && p.getName().equals(product.getName())) {// 表中已有同名用户
            return false;
        }*/

        product.setCreateTime(new Date());

        product.setUpdateTime(new Date());

        // 3.插入用户
        if (!insertSelective(product)) {
            return false;
        }

        // 全部完成
        return true;
    }

    /**
     * @param product
     * @return
     * @Title: updateProduct
     * @Description: 更新产品
     * @see com.cmcc.vrp.province.service.ProductService#updateProduct(com.cmcc.vrp.province.model.Product)
     */
    @Override
    public boolean updateProduct(Product product) {
        if (product == null) {
            return false;
        }

        //必须保证产品编码未被使用
        if (!StringUtils.isBlank(product.getProductCode())) {
            product.setProductCode(product.getProductCode().replaceAll(" ", ""));
            Product queryProduct = selectByCode(product.getProductCode());
            if (queryProduct != null && !queryProduct.getId().equals(product.getId())) {
                return false;
            }
        }
        /*if (product.getProductCode() != null) {
            product.setProductCode(product.getProductCode().replaceAll(" ", ""));
            Product queryProduct = selectByCode(product.getName());*/
        //1.如果要更改产品名，保证未被使用
        /*if (product.getName() != null) {
            product.setName(product.getName().replaceAll(" ", ""));
            Product queryProduct = selectByProductName(product.getName());
            if (queryProduct != null && !queryProduct.getId().equals(product.getId())) {
                return false;
            }
        }*/

        if (!updateSelective(product)) {
            return false;
        }

        return true;
    }

    /**
     * @param product
     * @return
     * @throws
     * @Title:checkUnique
     * @Description: 唯一性检查
     * @author: xuwanlin
     */
    @Override
    public boolean checkUnique(Product product) {
        if (!checkNameUnique(product) || !checkProductCodeUnique(product)) {
            return false;
        }
        return true;
    }

    /**
     * @param product
     * @return
     * @Title: checkNameUnique
     * @Description: TODO
     * @see com.cmcc.vrp.province.service.ProductService#checkNameUnique(com.cmcc.vrp.province.model.Product)
     */
    @Override
    public boolean checkNameUnique(Product product) {
        if (product == null) {
            return true;
        }

        Long currentId = product.getId();
        String currentName = product.getName();
        List<Product> products = selectAllProducts();
        for (Product queryProduct : products) {
            // 编辑状态时跳过当查询出来的产品Id是自己时的检验
            if (currentId != null && (queryProduct.getId().longValue() == currentId.longValue())) {
                continue;
            }
            if (queryProduct.getName().equals(currentName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param product
     * @return
     * @Title: checkProductCodeUnique
     * @Description: 检验产品编码唯一性
     * @see com.cmcc.vrp.province.service.ProductService#checkProductCodeUnique(com.cmcc.vrp.province.model.Product)
     */
    @Override
    public boolean checkProductCodeUnique(Product product) {
        if (product == null) {
            return true;
        }

        Long currentId = product.getId();
        String currentProductCode = product.getProductCode();
        List<Product> products = selectAllProducts();
        for (Product queryProduct : products) {
            // 编辑状态时跳过当查询出来的产品Id是自己时的检验
            if (currentId != null && (queryProduct.getId().longValue() == currentId.longValue())) {
                continue;
            }
            if (queryProduct.getProductCode().equals(currentProductCode)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return
     * @Title: selectAllProducts
     * @Description: 获取所有产品
     * @see com.cmcc.vrp.province.service.ProductService#selectAllProducts()
     */
    @Override
    public List<Product> selectAllProducts() {
        return productMapper.selectAllProducts();
    }

    public List<Product> getProListByProSizeEnterId(String productSize, Long entId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entId", entId);
        map.put("productSize", productSize);
        return productMapper.getProListByProSizeEnterId(map);
    }

    /**
     * @return
     * @Title: selectDistinctFlowSize
     * @Description: 获取所有流量包的大小并排序
     */
    public List<Product> selectDistinctProSize() {
        //List<Product> productsBeforeSort = productMapper.selectDistinctProSize();
        return productSortBySize(productMapper.selectDistinctProSize());
    }

    @Override
    public List<Product> selectAllProductsByEnterCode(String enterpriseCode) {
        return productMapper.selectAllProductsByEnterCode(enterpriseCode);
    }

    @Override
    public Product getCurrencyProduct() {
        return productMapper.getCurrencyProduct();
    }

    @Override
    public Product getFlowProduct() {
        return productMapper.getFlowProduct();
    }

    /**
     * 根据enterpriseId查找相关联产品，这个方法查找出来的都是上架的平台产品
     * @param enterpriseId
     * @return
     * @Title: selectAllProductsByEnterId
     * @Description: TODO
     * @return: List<Product>
     */
    @Override
    public List<Product> selectAllProductsByEnterId(Long enterpriseId) {
        if (enterpriseId == null) {
            return null;
        }
        return productMapper.selectAllProductsByEnterId(enterpriseId);
    }

    /**
     * 根据enterpriseId查找相关联产品，这个方法查找出来的所有产品（包括上架和下架）
     * @param enterpriseId
     * @return
     * @Title: selectProductsByEnterId
     * @Description: TODO
     * @return: List<Product>
     */
    @Override
    public List<Product> selectProductsByEnterId(Long enterpriseId) {
        if (enterpriseId == null) {
            return null;
        }
        return productMapper.selectProductsByEnterId(enterpriseId);
    }

    @Override
    public List<Product> selectByProductTemplateId(Long productTemplateId) {
        if (productTemplateId != null) {
            return productMapper.selectByProductTemplateId(productTemplateId);
        }
        return null;
    }

    /**
     * @param productName
     * @return
     * @Title: selectByProductName
     * @Description: 根据name查询产品
     * @see com.cmcc.vrp.province.service.ProductService#selectByProductName(java.lang.String)
     */
    @Override
    public Product selectByProductName(String productName) {
        if (productName == null) {
            return null;
        }
        return productMapper.selectByProductName(productName);
    }

    /**
     * @param productCode
     * @return
     * @Title: selectByProductCode
     * @Description: 根据code查询产品
     * @see com.cmcc.vrp.province.service.ProductService#selectByProductCode(java.lang.String)
     */
    @Override
    public Product selectByProductCode(String productCode) {
        if (productCode == null) {
            return null;
        }
        return productMapper.selectByProductCode(productCode);
    }

    /**
     * @param productCodes
     * @return
     * @Title: selectProIdsByProductCode
     * @Description: 根据给定的code数组，得到数据库中存在的id
     * @see com.cmcc.vrp.province.service.ProductService#selectProIdsByProductCode(java.util.List)
     */
    @Override
    public List<Long> selectProIdsByProductCode(List<String> productCodes) {
        List<Long> list = new ArrayList<Long>();
        for (String proCode : productCodes) {
            Product product = selectByProductCode(proCode);
            if (product != null) {
                list.add(product.getId());
            }
        }
        return list;
    }

    /**
     * @param productId
     * @return
     * @Title: get
     * @Description: TODO
     * @see com.cmcc.vrp.province.service.ProductService#get(java.lang.Long)
     */
    @Override
    public Product get(Long productId) {
        if (productId == null) {
            return null;
        }

        return productMapper.selectByPrimaryKey(productId);
    }

    @Override
    public boolean changeProductStatus(Product product) {
        if (product == null) {
            return false;
        }
        return productMapper.updateByPrimaryKeySelective(product) == 1;
    }

    @Override
    public List<Product> selectAllProductsON() {
        return productMapper.selectAllProductsON();
    }

    @Override
    public List<Product> batchSelectByCodes(Set<String> prdCodes) {
        if (prdCodes == null || prdCodes.isEmpty()) {
            return null;
        }

        return productMapper.batchSelectProductsByProductCodes(prdCodes);
    }

    /**
     * 用于订购信息查询
     */
    @Override
    public List<Product> getProductListForEnter(QueryObject queryObject) {
        if (queryObject == null) {
            return null;
        }
        Map map = queryObject.toMap();
        String sizeStr = (String) map.get("size");
        if (StringUtils.isNotBlank(sizeStr)) {
            Long size = Long.parseLong(sizeStr);
            size = SizeUnits.MB.toKB(size);
            map.put("size", size.intValue());
        }
        return productMapper.getProductListForEnter(map);
    }

    @Override
    public int getProductCountForEnter(QueryObject queryObject) {
        if (queryObject == null) {
            return 0;
        }
        Map map = queryObject.toMap();
        String sizeStr = (String) map.get("size");
        if (StringUtils.isNotBlank(sizeStr)) {
            Long size = Long.parseLong(sizeStr);
            size = SizeUnits.MB.toKB(size);
            map.put("size", size.intValue());
        }

        return productMapper.getProductCountForEnter(map);
    }

    @Override
    public boolean createNewProduct(Map<String, Object> map) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<Product> selectDefaultProductByCustomerType(Long productCustomerType) {
        return productMapper.selectDefaultProductByCustomerType(productCustomerType);
    }

    @Override
    @Transactional
    public boolean deletePlatformProduct(Long id) {
        //更新平台产品delete_flag状态
        if (!delete(id)) {
            throw new RuntimeException("更新平台产品删除状态失败");
        }
        //删除平台产品与企业的关联
        if (!entProductService.deleteByProductID(id)) {
            throw new RuntimeException("删除平台产品与企业的关联失败");
        }

        if (!supplierProductMapService.deleteByPlftPid(id)) {
            throw new RuntimeException("删除BOSS产品与平台产品的关联失败");
        }
        //删除平台产品与BOSS的关联
        return true;
    }

    /**
     * 企业订购信息查询
     */
    @Override
    public List<Product> getProductListFromEnterAccount(QueryObject queryObject) {
        if (queryObject == null) {
            return null;
        }
        Map map = queryObject.toMap();
        String sizeStr = (String) map.get("size");
        if (StringUtils.isNotBlank(sizeStr)) {
            Long size = Long.parseLong(sizeStr);
            size = SizeUnits.MB.toKB(size);
            map.put("size", size.intValue());
        }
        return productMapper.getProductListFromEnterAccount(map);
    }

    @Override
    public int getProductCountFromEnterAccount(QueryObject queryObject) {
        if (queryObject == null) {
            return 0;
        }
        Map map = queryObject.toMap();
        String sizeStr = (String) map.get("size");
        if (StringUtils.isNotBlank(sizeStr)) {
            Long size = Long.parseLong(sizeStr);
            size = SizeUnits.MB.toKB(size);
            map.put("size", size.intValue());
        }

        return productMapper.getProductCountFromEnterAccount(map);
    }

    @Override
    public List<Product> getProductsByPrizes(List<PrizeInfo> prizes) {
        return prizes == null ? null : productMapper.selectProductsByPrizes(prizes);
    }

    @Override
    public List<Product> getProductByEntIdAndIsp(QueryObject queryObject) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        Map map = queryObject.toMap();
        List<Product> listPrdsList;

        if (map.get("types") != null) {
            map.put("productTypes", map.get("types").toString().split(","));
            map.remove("types");
        }
        if (map.get("enterId") != null && map.get("isp") != null) {
            map.put("status", "on");
            //List<Product> products = productMapper.getProductListForEnter(map);

            if (!accountService.isEmptyAccount()) {
                //如果不是空账户,则需要进行删选出绑定了账户的产品
                /*if (products != null && products.size() > 0) {
                    Map newMap = new HashMap();
                    newMap.put("products", products);
                    newMap.put("enterId", map.get("enterId").toString());
                    return productMapper.getProducts(newMap);
                }*/
                List<Product> listPrds = productMapper.getProductsWhenAccountIsNotEmpty(map);
                sdPrdsSort(listPrds);
                return listPrds;
            } else {
                List<Product> listPrds = productMapper.getProductListForEnter(map);
                sdPrdsSort(listPrds);
                return listPrds;
            }
        }
        return null;
    }

    @Override
    public Integer countProductByEntIdAndIsp(QueryObject queryObject) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        Map map = queryObject.toMap();
        if (map.get("enterId") != null && map.get("isp") != null) {
            map.put("status", "on");
            if (!accountService.isEmptyAccount()) {
                return productMapper.countProductsWhenAccountIsNotEmpty(map);
            } else {
                return productMapper.getProductCountForEnter(map);
            }
        }
        return 0;
    }

    @Override
    public boolean batchInsertProduct(List<Product> records) {
        if (records == null) {
            return false;
        }
        return productMapper.batchInsertProduct(records) == records.size();
    }

    @Override
    public List<Product> selectProductByCreateTime(Date createTime) {
        if (createTime == null) {
            return null;
        }
        return productMapper.selectProductByCreateTime(createTime);
    }

    /**
     * @param productsBeforeSort
     * @return
     * @Title: productSortBySize
     * @Description: TODO
     * @return: List<Product>
     */
    public List<Product> productSortBySize(List<Product> productsBeforeSort) {

        Collections.sort(productsBeforeSort, new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                return (int) (o1.getProductSize() - o2.getProductSize());
            }
        });

        return productsBeforeSort;
    }

    /**
     * @param product 产品信息
     * @return boolean
     * @throws
     * @Title:insertSelective
     * @Description:数据库的插入操作
     */
    private boolean insertSelective(Product product) {
        if (product == null) {
            return false;
        }

        return productMapper.insertSelective(product) > 0;
    }

    /**
     * @param product
     * @return
     * @throws
     * @Title:updateSelective
     * @Description: 具体调用mapper方法
     * @author: xuwanlin
     */
    private boolean updateSelective(Product product) {
        if (product == null) {
            return false;
        }
        product.setUpdateTime(new Date());

        return productMapper.updateByPrimaryKeySelective(product) > 0;
    }

    @Override
    public boolean batchUpdate(List<Product> records) {
        if (records == null || records.size() <= 0) {
            return true;
        }
        return productMapper.batchUpdate(records) > 0;
    }

    @Override
    public List<Product> getProductsByPidWithDeleteFlag(Long productId) {
        if (productId == null) {
            return null;
        }
        return productMapper.getProductsByPidWithDeleteFlag(productId);
    }

    /**
     * 将流量池转化为平台的具体产品
     *
     * @param size
     * @return
     * @Title: getProductForFlowAccount
     * @Author: wujiamin
     * @date 2016年11月17日
     */
    @Override
    public Product getProductForFlowAccount(String size, Long productId) {
        return productMapper.selectFlowAccountProductByProductSize(Long.parseLong(size), productId);
    }

    /**
     * 根据产品大小查询
     *
     * @param size
     * @return
     */
    @Override
    public List<Product> selectByProSize(Long size) {
        if (size == null) {
            return null;
        }
        return productMapper.selectProductByProductSize(size.toString());
    }

    @Override
    public Product getPrdBySizeAndId(Long productSize, Long productId) {
        if (productSize == null || productId == null) {
            return null;
        }
        return productMapper.selectPrdBySizeAndId(productSize, productId);
    }

    /**
     * 同步运营商产品（重庆boss）
     *
     * @param enterCode
     * @param enterProCode
     * @return
     */
    @Transactional
    @Override
    public boolean synPrdsWithSupplierPro(String enterCode, String enterProCode) {
        if (StringUtils.isBlank(enterCode) || StringUtils.isBlank(enterProCode)) {
            return false;
        }

        Enterprise enterprise = enterprisesService.selectByCode(enterCode);
        if (enterprise == null) {
            return false;
        }

        CQBossServiceImpl bossService = applicationContext.getBean("cqBossService", CQBossServiceImpl.class);
        if (bossService == null) {
            return false;
        }

        List<String> prdsList = bossService.getProductsOrderByEnterCode(enterProCode);

        if (prdsList == null || prdsList.isEmpty()) {
            return false;
        }

        for (String prd : prdsList) {
            Double currentBossNumber = NumberUtils.toDouble(bossService.getEnterPrdSolde(enterProCode, prd));
            insertSupplierProduct(prd, currentBossNumber, enterprise, enterProCode);
        }
        return true;
    }

    /**
     * 更新数据库并建立产品与账户之间的关系
     *
     * @param productCode
     * @param currentBossNumber
     * @param enterprise
     * @param enterProCode
     */
    @Transactional
    @Override
    public void insertSupplierProduct(String productCode, Double currentBossNumber, Enterprise enterprise,
            String enterProCode) {

        Product platProduct = selectByProductCode(productCode);
        if (platProduct == null) {
            throw new TransactionException("平台产品不存在，同步失败。");
        }
        //1、向supplier_product表中插入记录，供应商产品
        JSONObject feature = new JSONObject(); //构建产品属性
        feature.put("productCode", productCode);
        feature.put("enterProCode", enterProCode);
        feature.put("enterCode", enterprise.getCode());
        SupplierProduct supplierProduct = null;
        supplierProduct = new SupplierProduct();
        supplierProduct.setSupplierId(1L);
        supplierProduct.setName(productCode);
        supplierProduct.setIsp("M");//默认为移动
        supplierProduct.setCode(productCode);
        supplierProduct.setSize(platProduct.getProductSize());
        supplierProduct.setOwnershipRegion("重庆");
        supplierProduct.setRoamingRegion("全国");
        supplierProduct.setPrice(platProduct.getPrice());//单位为分
        supplierProduct.setFeature(feature.toString());
        supplierProduct.setStatus(ProductStatus.NORMAL.getCode());//默认上架
        supplierProduct.setCreateTime(new Date());
        supplierProduct.setUpdateTime(new Date());
        supplierProduct.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());

        if (supplierProductService.selectByFeature(feature.toString()) == null) {
            if (!supplierProductService.insert(supplierProduct)) {
                throw new TransactionException("生成供应商产品时发生异常。");
            }
        }

        //2、向supplier_product_map表插入记录，将平台产品与供应商产品关联
        SupplierProductMap supplierProductMap = new SupplierProductMap();
        supplierProductMap.setCreateTime(new Date());
        supplierProductMap.setPlatformProductId(platProduct.getId());
        supplierProductMap.setSupplierProductId(supplierProduct.getId());
        supplierProductMap.setUpdateTime(new Date());
        supplierProductMap.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
        if (!supplierProductMapService.create(supplierProductMap)) {
            throw new TransactionException("生成平台产品与供应商产品关联关系时发生异常。");
        }

        //3、向ent_product表插入记录，将企业跟产品关联
        if (entProductService.selectByProductIDAndEnterprizeID(platProduct.getId(), enterprise.getId()) == null) {
            EntProduct entProduct = new EntProduct();
            entProduct.setUpdateTime(new Date());
            entProduct.setCreateTime(new Date());
            entProduct.setDiscount(100);
            entProduct.setEnterprizeId(enterprise.getId());
            entProduct.setProductId(platProduct.getId());
            entProduct.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
            if (!entProductService.insert(entProduct)) {
                throw new TransactionException("生成平台产品与供应商产品关联关系时发生异常。");
            }
        }

        //4、向account表中插入记录，将产品跟账户关联
        Account account = null;
        if ((account = accountService.get(enterprise.getId(), platProduct.getId(), AccountType.ENTERPRISE.getValue())) == null) {
            Map<Long, Double> infos = new HashMap<Long, Double>();
            infos.put(platProduct.getId(), currentBossNumber);
            accountService.createEnterAccount(enterprise.getId(), infos,
                    DigestUtils.md5Hex(UUID.randomUUID().toString()));
        } else {
            if (!accountService.updateCount(account.getId(), currentBossNumber)) {
                throw new TransactionException("更新企业账户失败");
            }
        }
        //5、向supplier_product_count表中插入记录
        EntSyncList entSyncList = entSyncListService.getByEntIdAndEntProCode(enterprise.getId(), enterProCode);
        SupplierProduct supplierPro = null;
        if (!((supplierPro = supplierProductService.selectByFeature(feature.toString())) == null)) {
            if (!supplierProductAccountService.createSupplierProductAccount(supplierPro.getId(), currentBossNumber,
                    entSyncList.getId())) {
                throw new TransactionException("创建产品账户失败");
            }
        }
    }

    @Override
    public List<Product> getPrdsByType(Long diffId, List<Integer> types, Long entId) {
        return productMapper.getPrdsByType(diffId, types, entId);
    }

    @Override
    public List<Product> getPrdsByTypePageList(QueryObject queryObject, List<Integer> types) {
        queryObject.getQueryCriterias().put("types", types);
        return productMapper.getPrdsByTypePageList(queryObject.toMap());
    }

    @Override
    public int getPrdsByTypePageCount(QueryObject queryObject, List<Integer> types) {
        queryObject.getQueryCriterias().put("types", types);
        return productMapper.getPrdsByTypePageCount(queryObject.toMap());
    }

    @Override
    public List<Product> getPlatFormBySupplierId(Long supplierId) {
        // TODO Auto-generated method stub
        if (supplierId != null) {
            return productMapper.getPlatFormBySupplierId(supplierId);
        }
        return null;
    }

    @Override
    public List<Product> listProductsByTemplateId(Map map) {
        // TODO Auto-generated method stub
        return productMapper.listProductsByTemplateId(map);
    }

    @Override
    public int countProductsByTemplate(Map map) {
        // TODO Auto-generated method stub
        return productMapper.countProductsByTemplate(map);
    }

    @Override
    public List<Product> getProductListAvailable(QueryObject queryObject) {
        // TODO Auto-generated method stub
        if (queryObject == null) {
            return null;
        }
        Map<String, Object> map = queryObject.toMap();
        String sizeStr = (String) map.get("size");
        String unit = (String) map.get("unit");
        if (StringUtils.isNotBlank(sizeStr) && StringUtils.isNotBlank(unit)) {
            Long size = Long.parseLong(sizeStr);
            if ("MB".equals(unit)) {
                size = SizeUnits.MB.toKB(size);
            } else if ("GB".equals(unit)) {
                size = SizeUnits.GB.toKB(size);
            }
            map.put("productSize", size.intValue());
        } else {
            map.remove("size");
            map.remove("unit");
        }

        if (map.get("productIds") != null && map.get("productIds").toString().split(",").length > 0) {
            map.put("productIdList", map.get("productIds").toString().split(","));
        }

        return productMapper.getProductListAvailable(map);
    }

    @Override
    public int countProductListAvailable(QueryObject queryObject) {
        // TODO Auto-generated method stub
        if (queryObject == null) {
            return 0;
        }
        Map<String, Object> map = queryObject.toMap();
        String sizeStr = (String) map.get("size");
        String unit = (String) map.get("unit");
        if (StringUtils.isNotBlank(sizeStr) && StringUtils.isNotBlank(unit)) {
            Long size = Long.parseLong(sizeStr);
            if ("MB".equals(unit)) {
                size = SizeUnits.MB.toKB(size);
            } else if ("GB".equals(unit)) {
                size = SizeUnits.GB.toKB(size);
            }
            map.put("productSize", size.intValue());
        } else {
            map.remove("size");
            map.remove("unit");
        }

        if (map.get("productIds") != null && map.get("productIds").toString().split(",").length > 0) {
            map.put("productIdList", map.get("productIds").toString().split(","));
        }
        return productMapper.countProductListAvailable(map);
    }

    @Override
    public List<Product> getBySplIdWithoutDel(Long spid) {
        if (spid == null) {
            return null;
        }
        return productMapper.getBySplIdWithoutDel(spid);
    }

    @Override
    public List<Product> getProductsByOrderListId(Long oriderListId) {
        return productMapper.getProductsByOrderListId(oriderListId);
    }

    @Override
    public int showForPageResultCount(QueryObject queryObject) {
        if (queryObject == null) {
            return 0;
        }
        return productMapper.showForPageResultCount(queryObject.toMap());
    }

    @Override
    public List<Product> showForPageResultList(QueryObject queryObject) {
        if (queryObject == null) {
            return new ArrayList<Product>();
        }
        return productMapper.showForPageResultList(queryObject.toMap());
    }

    @Override
    public List<Product> getOrderProductByMap(Map map) {
        return productMapper.getOrderProductByMap(map);
    }

    @Override
    public List<Product> selectByType(Integer type) {
        if (type == null) {
            return null;
        }
        return productMapper.selectByType(type);
    }

    @Override
    public boolean sdPrdsSort(List<Product> listPrds) {
        if(!isSdEnvironment()){//非山东环境不需要排序
            return false;
        }
        
        Collections.sort(listPrds, new Comparator<Product>() {

            public int compare(Product p1, Product p2) {
                String sdPrdCode1;
                String sdPrdCode2;
                if(StringUtils.isBlank(sdPrdCode1 = p1.getProductCode()) || 
                        StringUtils.isBlank(sdPrdCode2 = p2.getProductCode()) ||
                        sdPrdCode1.length()<=6 || 
                        sdPrdCode2.length()<=6){//防止数据错误抛异常
                    return 0;
                }
                
                //取最后六位，取完后的结果109201
                sdPrdCode1 = sdPrdCode1.substring(sdPrdCode1.length()-6); 
                sdPrdCode2 = sdPrdCode2.substring(sdPrdCode2.length()-6); 
                
                //1.先按前4位从大到小排序,一致则再按照后两位从小到大排序
                if(!sdPrdCode1.substring(0,4).equals(sdPrdCode2.substring(0, 4))){
                    return NumberUtils.toInt(sdPrdCode1.substring(0,4)) > 
                        NumberUtils.toInt(sdPrdCode2.substring(0, 4)) ? -1:1;
                }else{
                    return NumberUtils.toInt(sdPrdCode1.substring(4)) <= 
                        NumberUtils.toInt(sdPrdCode2.substring(4)) ? -1:1;
                }
                  
            }
        });
        
        return true;
    }
    
    
    /**
     * 判断是否山东环境
     */
    private boolean isSdEnvironment(){
        return "sd".equalsIgnoreCase(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey()));
    }
}
