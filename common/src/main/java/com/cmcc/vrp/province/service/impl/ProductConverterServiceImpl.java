package com.cmcc.vrp.province.service.impl;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmcc.vrp.enums.ProductConverterType;
import com.cmcc.vrp.exception.TransactionException;
import com.cmcc.vrp.province.cache.AbstractCacheSupport;
import com.cmcc.vrp.province.dao.ProductConverterMapper;
import com.cmcc.vrp.province.model.ProductConverter;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ProductConverterService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;
import com.google.gson.Gson;

/**
 * 产品转换服务类
 * @author qihang
 *
 */
@Service
public class ProductConverterServiceImpl extends AbstractCacheSupport
        implements ProductConverterService {

    private static Logger LOGGER = LoggerFactory
            .getLogger(ProductConverterServiceImpl.class);

    @Autowired
    ProductConverterMapper converterMapper;
    
    @Autowired
    GlobalConfigService globalConfigService;

    @Override
    public boolean insert(ProductConverter converter) {
        validate(converter);
        return converterMapper.insert(converter) == 1
                && cacheService.add(getKey(converter), new Gson().toJson(converter));
        
        /*return validate(converter)
                && converterMapper.insert(converter) == 1
                && cacheService.add(getKey(converter), new Gson().toJson(converter));*/
    }

    @Override
    public boolean insertBatch(List<ProductConverter> list) {
        batchValidate(list);
        return list.isEmpty() ||
               (converterMapper.batchInsert(list) > 0 
                       && batchAddToCache(list));
    }

    @Override
    public ProductConverter get(final Long id) {
        if (id == null) {
            return null;
        }

        return process(new Operator() {
            @Override
            public String getValueFromCache() {
                /*缓存非Id存储，无法直接获取,需要通过数据库.
                                                     推荐使用get(final Long srcPrdId,final Long destPrdId)
                */
                return null;
            }

            @Override
            public ProductConverter getFromDB() {
                return converterMapper.selectByPrimaryKey(id);
            }

        });
    }

    @Override
    public boolean delete(Long id) {
        ProductConverter converter = null;
        return id != null && (converter = get(id)) != null
                && cacheService.delete(getKey(converter))
                && converterMapper.delete(id) == 1;
    }
    
    @Override
    public boolean deleteBatch(List<ProductConverter> list) {
        return  list.isEmpty() ||
                (cacheService.delete(getKeys(list))
                && converterMapper.batchDelete(list) > 0); 
    }

    @Override
    public List<ProductConverter> get() {
        return converterMapper.getAll();
    }

    @Override
    public ProductConverter get(final Long srcPrdId,
            final Long destPrdId) {
        if (srcPrdId == null || destPrdId == null) {
            return null;
        }

        return process(new Operator() {
            @Override
            public String getValueFromCache() {
                return cacheService.get(getKey(srcPrdId, destPrdId));
            }

            @Override
            public ProductConverter getFromDB() {
                List<ProductConverter> list = converterMapper
                        .getBySrcDestId(srcPrdId, destPrdId);

                return list.isEmpty() ? null : list.get(0);
            }
        });
    }
    
    @Override
    public boolean isInterdictConvert(Long srcPrdId, Long destPrdId) {
        if(srcPrdId.equals(destPrdId)){
            return false;
        }
        
        switch (getConverterType()) {
            case BLACKLIST:
                return get(srcPrdId,destPrdId) != null;
            case WHITELIST:
                return get(srcPrdId,destPrdId) == null;
            default:
                return false;
        }
    }

    @Override
    public boolean validate(ProductConverter converter)
            throws InvalidParameterException {
        if (converter == null) {
            throw new InvalidParameterException("无效的产品转换对象.");
        }

        if (converter.getSourcePrdId() == null) {
            throw new InvalidParameterException("产品转换对象原产品Id为空.");
        }

        if (converter.getDestPrdId() == null) {
            throw new InvalidParameterException("产品转换对象目标产品Id为空.");
        }

        return true;
    }
    
    @Override
    public boolean batchValidate(List<ProductConverter> converters)
            throws InvalidParameterException {
        for(ProductConverter converter : converters){
            validate(converter);
        }
        return true;
    }
    
    @Override
    @Transactional
    public boolean insertDeleteBatch(List<ProductConverter> addList,
            List<ProductConverter> deleteList) {
        if(!insertBatch(addList)){ //批量插入不成功
            return false;
        }
        
        if(!deleteBatch(deleteList)){ //批量删除不成功，回滚
            throw new TransactionException("ProductInterdictConverter批量插入和删除失败");
        }
        
        return true;
    }
    
    @Override
    public boolean save(List<ProductConverter> pageList,
            List<ProductConverter> dbList) {
        
        //需要新增到数据库的项目
        List<ProductConverter> addList = new ArrayList<ProductConverter>();
        //需要从数据库删除的项目
        List<ProductConverter> deleteList = new ArrayList<ProductConverter>();
        
        //保存数据库记录中已在页面项中存在的项目Id
        Set<Long> findIdDBList = new HashSet<Long>();
        
        /**
         * 遍历pageList,从所有的dbList中找到匹配（srcId和destId相同的项）
         * 1.没有找到，说明数据库不存在，添加到addList中
         * 2.找到，说明存在，将数据库项Id保存到findIdDBList中
         */
        for(ProductConverter pageConverter : pageList){
            boolean findFlag = false;
            for(ProductConverter dbConverter : dbList){
                if(pageConverter.getSourcePrdId().equals(dbConverter.getSourcePrdId()) &&
                        pageConverter.getDestPrdId().equals(dbConverter.getDestPrdId())){
                    
                    findIdDBList.add(dbConverter.getId());
                    findFlag = true;
                    break;
                }
            }
            
            if(!findFlag){ //没有找到，说明数据库中不存在该记录，需要添加到数据库中
                pageConverter.setCreateTime(new Date());
                pageConverter.setUpdateTime(new Date());
                pageConverter.setDeleteFlag(com.cmcc.vrp.util.Constants.DELETE_FLAG.UNDELETED.getValue());
                
                addList.add(pageConverter);
            }
        }
        

        /**
         * 过滤掉dataBaseList在页面存在的项目(保存在findIdDBList中)，留下数据库待删除项目
         */
        for(ProductConverter dbConverter:dbList){
            if(!findIdDBList.contains(dbConverter.getId())){ //findIdDBList中不存在存在，需要删除
                deleteList.add(dbConverter);
            }
        }
        
        //批量插入和删除，事务
        return insertDeleteBatch(addList,deleteList);
    }
    
    @Override
    public int count(QueryObject queryObject) {
        return converterMapper.count(queryObject.toMap());
    }

    @Override
    public List<ProductConverter> page(QueryObject queryObject) {
        return converterMapper.page(queryObject.toMap());
    }
    
    @Override
    protected String getPrefix() {
        return "prd.interdit.";
    }
    
    private String getKey(ProductConverter converter) {
        return getKey(converter.getSourcePrdId(), converter.getDestPrdId());
    }
    
    private List<String> getKeys(List<ProductConverter> list) {
        List<String> listKeys = new ArrayList<String>();
        
        for(ProductConverter converter : list){
            listKeys.add(getKey(converter.getSourcePrdId(), converter.getDestPrdId()));
        }
        
        return listKeys;
    }
 

    private String getKey(Long sourcePrdId, Long destPrdId) {
        return "src."+sourcePrdId + ".dest." + destPrdId;
    }

    private ProductConverter process(Operator operator) {
        ProductConverter converter = null;

        String value = operator.getValueFromCache();

        try {
            converter = new Gson().fromJson(value, ProductConverter.class);
        } catch (Exception e) {
            LOGGER.error("解析缓存数据时出错, 缓存数据为{}, 错误信息为{}, 错误堆栈为{}.", value,
                    e.getMessage(), e.getStackTrace());
        }

        // 缓存没有命中
        if (converter == null) {
            converter = operator.getFromDB();
            if (converter != null
                    && !cacheService.add(getKey(converter), new Gson().toJson(converter))) {
                LOGGER.error("设置缓存时出错，Key = {}， Value = {}.",
                        getKey(converter), new Gson().toJson(converter));
            }
        }

        return converter;
    }
    
    private boolean batchAddToCache(List<ProductConverter> list){
        for(ProductConverter converter : list){
            if(!cacheService.add(getKey(converter), new Gson().toJson(converter))){
                return false;
            }
        }
        
        return true;
    }

    private interface Operator {
        String getValueFromCache();

        ProductConverter getFromDB();
    }
    
    /**
     * 从globalconfig中得到类型，共三种
     */
    private ProductConverterType getConverterType(){
        String value = globalConfigService.get(GlobalConfigKeyEnum.PRODUCT_CONVERTER_TYPE.getKey());
      
        for(ProductConverterType type :ProductConverterType.values()){
            if(value.equalsIgnoreCase(type.getType())){
                return type;
            }
        }
        
        return ProductConverterType.NOUSE;
    }

    
}
