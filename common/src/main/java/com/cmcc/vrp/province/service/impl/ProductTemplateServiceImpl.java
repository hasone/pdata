package com.cmcc.vrp.province.service.impl;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.province.dao.ProductTemplateMapper;
import com.cmcc.vrp.province.model.EntProduct;
import com.cmcc.vrp.province.model.PlatformProductTemplateMap;
import com.cmcc.vrp.province.model.ProductTemplate;
import com.cmcc.vrp.province.model.ProductTemplateEnterpriseMap;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.PlatformProductTemplateMapService;
import com.cmcc.vrp.province.service.ProductTemplateEnterpriseMapService;
import com.cmcc.vrp.province.service.ProductTemplateService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("productTemplateService")
public class ProductTemplateServiceImpl implements ProductTemplateService{
    private static final Logger logger = LoggerFactory.getLogger(ProductTemplateServiceImpl.class);
    
    @Autowired
    ProductTemplateMapper mapper;
    @Autowired
    PlatformProductTemplateMapService platformProductTemplateMapService;
    @Autowired
    ProductTemplateEnterpriseMapService productTemplateEnterpriseMapService;
    @Autowired
    EntProductService entProductService;
    @Autowired
    SupplierProductService supplierProductService;



    @Override
    public boolean deleteByPrimaryKey(Long id) {
        // TODO Auto-generated method stub
        if(id!=null){
            return mapper.deleteByPrimaryKey(id)>=0;
        }
        return false;
    }

    @Override
    public boolean insert(ProductTemplate record) {
        // TODO Auto-generated method stub
        if(record!=null){
            return mapper.insert(record)==1;
        }
        return false;
    }

    @Override
    public boolean insertSelective(ProductTemplate record) {
        // TODO Auto-generated method stub
        if(record!=null){
            return mapper.insertSelective(record)==1;
        }
        return false;
    }

    @Override
    public ProductTemplate selectByPrimaryKey(Long id) {
        // TODO Auto-generated method stub
        if(id!=null){
            return mapper.selectByPrimaryKey(id);
        }
        return null;
    }

    @Override
    public boolean updateByPrimaryKeySelective(ProductTemplate record) {
        // TODO Auto-generated method stub
        if(record!=null){
            return mapper.updateByPrimaryKeySelective(record)>=0;
        }
        return false;
    }

    @Override
    public boolean updateByPrimaryKey(ProductTemplate record) {
        // TODO Auto-generated method stub
        if(record!=null){
            return mapper.updateByPrimaryKey(record)==1;
        }
        return false;
    }

    @Override
    public List<ProductTemplate> selectByMap(Map map) {
        // TODO Auto-generated method stub
        return mapper.selectByMap(map);
    }

    @Override
    public long countByMap(Map map) {
        // TODO Auto-generated method stub
        return mapper.countByMap(map);
    }

    @Override
    public List<ProductTemplate> selectAllProductTemplates() {
        // TODO Auto-generated method stub
        return mapper.selectAllProductTemplates();
    }

    @Override
    @Transactional
    public boolean createProductTemplate(ProductTemplate productTemplate, List<PlatformProductTemplateMap> maps) {
        // TODO Auto-generated method stub
        if(productTemplate==null){
            logger.info("产品模板参数为空，保存失败!");
            return false;
        }
        initProductTemplate(productTemplate);
        if(!insertSelective(productTemplate)){
            logger.info("插入产品模板失败!"+JSON.toJSONString(productTemplate));
            return false;
        }
        
        if(maps!=null && maps.size()>0){
            initPlatformProductTemplateMap(maps, productTemplate.getId());
            if(!platformProductTemplateMapService.batchInsert(maps)){
                logger.info("插入产品与模板关系失败!"+JSON.toJSONString(maps));
                throw new RuntimeException();
            }
        }
        return true;
    }
    
    @Override
    @Transactional
    public boolean editProductTemplate(ProductTemplate productTemplate, List<PlatformProductTemplateMap> maps) {
        // TODO Auto-generated method stub
        if(productTemplate==null){
            logger.info("产品模板参数为空，保存编辑失败!");
            return false;
        }
        
        //1、插入活动模板
        initProductTemplate(productTemplate);
        if(!updateByPrimaryKeySelective(productTemplate)){
            logger.info("更新产品模板失败！"+JSON.toJSONString(productTemplate));
            return false;
        }
        
        //2、更新产品与模板关联关系
        //3、跟新企业与产品关联关系
        List<PlatformProductTemplateMap> oldMaps = null;
        List<PlatformProductTemplateMap> delMaps = null;
        List<PlatformProductTemplateMap> addMaps = null;
        
        List<ProductTemplateEnterpriseMap> productTemplateEnterpriseMaps = null;
        if(maps!=null && maps.size()>0){
            //需要和原有做对比，找到要删除，要增加的记录
            oldMaps = platformProductTemplateMapService.selectByTemplateId(productTemplate.getId());
            delMaps = delMaps(maps, oldMaps);
            addMaps = addMaps(maps, oldMaps);
            
            productTemplateEnterpriseMaps = productTemplateEnterpriseMapService
                    .selectByProductTemplateId(productTemplate.getId());
        }
        
        if(delMaps!=null && delMaps.size()>0){
            //2.1 删除产品与模板关联关系
            if(!platformProductTemplateMapService.batchDelete(delMaps)){
                logger.info("删除产品与模板关联关系失败。"+JSON.toJSONString(delMaps));
                throw new RuntimeException();
            }
            
            //3.1删除企业与产品关联关系
            if(productTemplateEnterpriseMaps!=null && productTemplateEnterpriseMaps.size()>0){
                List<EntProduct> delEntProducts = createDelEntProducts(delMaps, productTemplateEnterpriseMaps);
                if(!entProductService.batchDeleteByEnterIdAndProductId(delEntProducts)){
                    logger.info("删除平台产品与企业关联关系失败."+JSON.toJSONString(delEntProducts));
                    throw new RuntimeException();
                }
            }
        }
        
        initPlatformProductTemplateMap(addMaps, productTemplate.getId());
        if(addMaps.size()>0){
            //2.2 插入产品与模板关联关系
            if(!platformProductTemplateMapService.batchInsert(addMaps)){
                logger.info("插入产品与模板关联关系失败。"+JSON.toJSONString(addMaps));
                throw new RuntimeException();
            }
            
            //3.2插入企业与产品关联关系
            if(productTemplateEnterpriseMaps!=null && productTemplateEnterpriseMaps.size()>0){
                List<EntProduct> addEntProducts = createAddEntProduct(addMaps, productTemplateEnterpriseMaps);
                if(!entProductService.batchInsertEntProduct(addEntProducts)){
                    logger.info("插入平台产品与企业关联关系失败。"+JSON.toJSONString(addEntProducts));
                    throw new RuntimeException();
                }
            }
        }
        return true;
    }
    
    private List<EntProduct> createDelEntProducts(List<PlatformProductTemplateMap> delMaps, List<ProductTemplateEnterpriseMap> productTemplateEnterpriseMaps){
        List<EntProduct> entProducts = new ArrayList<EntProduct>();
        for(PlatformProductTemplateMap map : delMaps){
            for(ProductTemplateEnterpriseMap item : productTemplateEnterpriseMaps){
                EntProduct delMap = new EntProduct();
                delMap.setProductId(map.getPlatformProductId());
                delMap.setEnterprizeId(item.getEnterpriseId());
                delMap.setDeleteFlag(1);
                delMap.setUpdateTime(new Date());
                entProducts.add(delMap);
            }
        }
        return entProducts;
    }
    
    private List<EntProduct> createAddEntProduct(List<PlatformProductTemplateMap> addMaps, List<ProductTemplateEnterpriseMap> productTemplateEnterpriseMaps){
        List<EntProduct> entProducts = new ArrayList<EntProduct>();
        for(PlatformProductTemplateMap map : addMaps){
            for(ProductTemplateEnterpriseMap item : productTemplateEnterpriseMaps){
                EntProduct addMap = new EntProduct();
                addMap.setProductId(map.getPlatformProductId());
                addMap.setEnterprizeId(item.getEnterpriseId());
                addMap.setCreateTime(new Date());
                addMap.setUpdateTime(new Date());
                addMap.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
                addMap.setDiscount(100); //默认无折扣
                entProducts.add(addMap);
            }
        }
        return entProducts;
    }
    
    /**
     * 获取要新增的记录
     * */
    private List<PlatformProductTemplateMap> addMaps(List<PlatformProductTemplateMap> newMaps, 
            List<PlatformProductTemplateMap> oldMaps){
        List<PlatformProductTemplateMap> addMaps = new ArrayList<PlatformProductTemplateMap>();
        if(oldMaps==null || oldMaps.size()<1){
            return newMaps;
        }else{
            for(int i=0; i<newMaps.size(); i++){
                PlatformProductTemplateMap newMap = newMaps.get(i);
                int j = 0;
                for(; j<oldMaps.size(); j++){
                    PlatformProductTemplateMap oldMap = oldMaps.get(j);

                    if(newMap.getPlatformProductId().toString()
                            .equals(oldMap.getPlatformProductId().toString())){
                        break;
                    }
                    if(j+1 == oldMaps.size()){
                        addMaps.add(newMap);
                    }
                }
            }
            return addMaps;
        }
    }
    
    /**
     * 获取要删除的记录
     * */
    private List<PlatformProductTemplateMap> delMaps(List<PlatformProductTemplateMap> newMaps, 
            List<PlatformProductTemplateMap> oldMaps){
        List<PlatformProductTemplateMap> delMaps = new ArrayList<PlatformProductTemplateMap>();
        if(oldMaps!=null && oldMaps.size()>0){
            for(int i=0; i<oldMaps.size(); i++){
                PlatformProductTemplateMap oldMap = oldMaps.get(i);
                int j = 0;
                for(; j<newMaps.size(); j++){
                    PlatformProductTemplateMap newMap = newMaps.get(j);
                    if(oldMap.getPlatformProductId().toString()
                            .equals(newMap.getPlatformProductId().toString())){
                        break;
                    }
                    if(j+1 == newMaps.size()){
                        PlatformProductTemplateMap item = new PlatformProductTemplateMap();
                        item.setId(oldMap.getId());
                        item.setPlatformProductId(oldMap.getPlatformProductId());
                        item.setUpdateTime(new Date());
                        item.setDeleteFlag(1);
                        delMaps.add(item);
                    }
                }
            } 
        }
        return delMaps;
    }

    /**
     * 初始化产品模板
     * */
    private void initProductTemplate(ProductTemplate productTemplate){
        if(productTemplate.getId()==null){
            productTemplate.setCreateTime(new Date());
            productTemplate.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
            productTemplate.setDefaultFlag(0);
        }
        productTemplate.setUpdateTime(new Date());
    }
    
    /**
     * 初始化关系
     * */
    private void initPlatformProductTemplateMap(List<PlatformProductTemplateMap> maps, 
            Long templateId){
        for(PlatformProductTemplateMap item : maps){
            if(item.getProductTemplateId()==null){
                item.setProductTemplateId(templateId);
            }
            item.setCreateTime(new Date());
            item.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
            item.setUpdateTime(new Date());
        }
    }

    @Override
    public boolean whetherUseProdTemplate(Long id) {
        List<ProductTemplateEnterpriseMap> maps = productTemplateEnterpriseMapService.selectByProductTemplateId(id);
        if(maps!=null && maps.size()>0){
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean deleteProductTemplate(Long id) {
        //企业与产品关联关系列表
        List<ProductTemplateEnterpriseMap> productTemplateEnterpriseMaps = productTemplateEnterpriseMapService
                .selectByProductTemplateId(id);
        
        if(!productTemplateEnterpriseMapService.deleteByProductTemplateId(id)){
            logger.info("删除产品模板与企业关联关系失败。模板id："+id);
            return false;
        }

        if(!deleteByPrimaryKey(id)){
            logger.info("删除产品模板失败。模板id："+id);
            throw new RuntimeException();
        }
       
        //删除企业与产品关联关系失败
        if(productTemplateEnterpriseMaps!=null && productTemplateEnterpriseMaps.size()>0){
            if(!entProductService.batchDeleteByEnterId(productTemplateEnterpriseMaps)){
                logger.info("删除产品与企业关联关系失败。企业列表："+JSON.toJSONString(productTemplateEnterpriseMaps));
                throw new RuntimeException();
            }
        }
        
        return true;
    }

    @Override
    public List<ProductTemplate> selectByName(String name) {
        return mapper.selectByName(name);
    }

    @Override
    public ProductTemplate getDefaultProductTemplate(){
        List<ProductTemplate> defaultProductTemplates = mapper.getDefaultProductTemplate();
        return defaultProductTemplates!=null&&defaultProductTemplates.size()>0?defaultProductTemplates.get(0):null;
    }

    @Override
    @Transactional
    public boolean relatedDefaultProductTemplate(Long entId){
        ProductTemplate productTemplate = getDefaultProductTemplate();
        if(productTemplate!=null){
            //1、企业关联产品组
            ProductTemplateEnterpriseMap productTemplateEnterpriseMap = createProductTemplateEnterpriseMap(entId, productTemplate.getId());
            if(!productTemplateEnterpriseMapService.insertSelective(productTemplateEnterpriseMap)){
                logger.info("企业 id="+entId+"关联产品组 templateId="+productTemplate.getId()+"失败");
                return false;
            }

            //2、企业关联产品
            List<EntProduct> entProducts = createEntProductList(entId, productTemplate.getId());
            if(entProducts!=null && entProducts.size()>0){
                if(!entProductService.batchInsertEntProduct(entProducts)){
                    logger.info("企业 id="+entId+"关联产品组 templateId="+productTemplate.getId()+"的产品失败");
                    return false;
                }
            }
            return true;
        }else{
            logger.info("未获取到默认产品组");
        }
        return false;
    }

    private ProductTemplateEnterpriseMap createProductTemplateEnterpriseMap(Long entId, Long templateId){
        ProductTemplateEnterpriseMap map = new ProductTemplateEnterpriseMap();
        map.setEnterpriseId(entId);
        map.setProductTemplateId(templateId);
        map.setCreateTime(new Date());
        map.setUpdateTime(new Date());
        map.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
        return map;
    }

    private List<EntProduct> createEntProductList(Long entId, Long templateId){
        List<EntProduct> list = new ArrayList<EntProduct>();
        List<PlatformProductTemplateMap> products = platformProductTemplateMapService.selectByTemplateId(templateId);
        for(PlatformProductTemplateMap item : products){
            EntProduct entProduct = new EntProduct();
            entProduct.setProductId(item.getPlatformProductId());
            entProduct.setEnterprizeId(entId);
            entProduct.setDeleteFlag(0);
            entProduct.setCreateTime(new Date());
            entProduct.setUpdateTime(new Date());
            entProduct.setDiscount(100);
            list.add(entProduct);
        }
        return list;
    }
}
