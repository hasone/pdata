package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.SupplierProductMapMapper;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.model.SupplierProductMap;
import com.cmcc.vrp.province.service.SupplierProductMapService;
import com.cmcc.vrp.util.Constants;
import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sunyiwei on 2016/6/13.
 */
@Service("supplierProductMapService")
public class SupplierProductMapServiceImpl implements SupplierProductMapService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SupplierProductMapServiceImpl.class);

    @Autowired
    SupplierProductMapMapper supplierProductMapMapper;

    @Override
    public boolean create(SupplierProductMap supplierProductMap) {
        if (!validate(supplierProductMap)) {
            LOGGER.error("无效的供应商产品关联关系, 新增关联关系失败. SupplierProductMap = {}.", supplierProductMap == null ? "" : new Gson().toJson(supplierProductMap));
            return false;
        }

        return supplierProductMapMapper.create(supplierProductMap) == 1;
    }

    @Override
    public boolean create(Long pltfPid, Long splPid) {
        return pltfPid != null
            && splPid != null
            && create(build(pltfPid, splPid));
    }

    //先删除原来的关联关系，再新增新的关系
    @Override
    @Transactional
    public boolean updateByPltfPid(Long pltfPid, List<Long> splPids) {
        if (deleteByPlftPid(pltfPid)) {
            supplierProductMapMapper.batchInsert(batchBuildByPlftPid(pltfPid, splPids));

            LOGGER.debug("根据平台产品ID更新关联关系成功. PltfPid = {}, SplPids = {}.", pltfPid, new Gson().toJson(splPids));
            return true;
        }

        LOGGER.error("根据平台产品ID更新关联关系失败. PltfPid = {}, SplPids = {}.", pltfPid, new Gson().toJson(splPids));
        throw new RuntimeException();
    }

    @Override
    @Transactional
    public boolean updateBySplPid(Long splPid, List<Long> pltPids) {
        if (deleteBySplPid(splPid)) {
            supplierProductMapMapper.batchInsert(batchBuildBySplPid(splPid, pltPids));

            LOGGER.debug("根据供应商产品ID更新关联关系成功. SplPid= {}, PltfPids = {}.", splPid, new Gson().toJson(pltPids));
            return true;
        }

        LOGGER.debug("根据供应商产品ID更新关联关系失败. SplPid= {}, PltfPids = {}.", splPid, new Gson().toJson(pltPids));
        throw new RuntimeException();
    }

    @Override
    public boolean deleteByPlftPid(Long plftPid) {
        if (plftPid == null) {
            LOGGER.error("无效的平台产品ID，删除关联关系失败.");
            return false;
        }

        return supplierProductMapMapper.deleteByPlftPid(plftPid) >= 0;
    }

    @Override
    public boolean delete(Long plftPid, Long splPid) {
        if (plftPid == null || splPid == null) {
            LOGGER.error("无效的平台产品ID或供应商产品ID，删除关联关系失败.");
            return false;
        }

        return supplierProductMapMapper.deleteByPlftPidAndSplPid(plftPid, splPid) >= 0;
    }

    @Override
    public boolean deleteBySplPid(Long splPid) {
        if (splPid == null) {
            LOGGER.error("无效的供应商产品ID，删除关联关系失败.");
            return false;
        }

        return supplierProductMapMapper.deleteBySplPid(splPid) >= 0;
    }

    @Override
    public boolean deleteBySupplierId(Long supplierId) {
        if (supplierId == null) {
            LOGGER.error("无效的供应商ID，删除关联关系失败.");
            return false;
        }
        return supplierProductMapMapper.deleteBySupplierId(supplierId) >= 0;
    }

    @Override
    public List<SupplierProduct> getByPltfPid(Long pltfPid) {
        if (pltfPid == null) {
            LOGGER.error("无效的平台产品ID，获取供应商产品信息列表失败.");
            return null;
        }

        return supplierProductMapMapper.getByPltfPid(pltfPid);
    }

    @Override
    public List<SupplierProduct> getOnshelfByPltfPid(Long pltfPid) {
        // TODO Auto-generated method stub
        if (pltfPid == null) {
            LOGGER.error("无效的平台产品ID，获取上架的供应商产品信息列表失败.");
            return null;
        }

        return supplierProductMapMapper.getOnshelfByPltfPid(pltfPid);
    }

    @Override
    public List<Product> getBySplPid(Long splPid) {
        if (splPid == null) {
            LOGGER.error("无效的供应商产品ID，获取平台产品信息列表失败.");
            return null;
        }

        return supplierProductMapMapper.getBySplPid(splPid);
    }

    private boolean validate(SupplierProductMap spm) {
        return spm != null
            && spm.getPlatformProductId() != null
            && spm.getSupplierProductId() != null;
    }

    private List<SupplierProductMap> batchBuildByPlftPid(Long pltfPid, List<Long> splPids) {
        List<SupplierProductMap> maps = new LinkedList<SupplierProductMap>();
        for (Long splPid : splPids) {
            maps.add(build(pltfPid, splPid));
        }

        return maps;
    }

    private List<SupplierProductMap> batchBuildBySplPid(Long splPid, List<Long> pltfPids) {
        List<SupplierProductMap> maps = new LinkedList<SupplierProductMap>();
        for (Long pltfPid : pltfPids) {
            maps.add(build(pltfPid, splPid));
        }

        return maps;
    }

    private SupplierProductMap build(Long plftPid, Long splPid) {
        SupplierProductMap spm = new SupplierProductMap();

        spm.setPlatformProductId(plftPid);
        spm.setSupplierProductId(splPid);
        spm.setCreateTime(new Date());
        spm.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
        spm.setUpdateTime(new Date());

        return spm;
    }

    @Override
    public boolean batchInsertMap(List<SupplierProductMap> records) {
        if (records == null) {
            return false;
        }
        return supplierProductMapMapper.batchInsert(records) == records.size();
    }

    @Override
    public boolean batchUpdate(List<SupplierProductMap> list) {
	if(list == null || list.size() <= 0){
	    return true;
	}
	return supplierProductMapMapper.batchUpdate(list) > 0;
    }

    @Override
    public List<SupplierProductMap> selectBySplPidWithOutDeleteFlag(Long splPid) {
	if(splPid == null){
	    return null;
	}
	return supplierProductMapMapper.selectBySplPidWithOutDeleteFlag(splPid);
    }

    @Override
    public List<Product> getBySplPidWithoutDel(Long splPid) {
        if(splPid == null || splPid.longValue() < 0.0){
            return null;
        }
        return supplierProductMapMapper.getBySplPidWithoutDel(splPid);
    }

    @Override
    public boolean updatePriorSupplier(Long prdId, Long supplierId) {
        if (prdId == null
                || supplierId == null) {
            return false;
        }
        if (!clearPriorSupplier(prdId)) {
            LOGGER.error("清除现有关联关系失败");
            return false;
        }
        if (supplierId != 0
                && !(supplierProductMapMapper.updatePriorSupplier(prdId, supplierId) > 0)) {
            LOGGER.error("更新优先供应商失败");
            return false;
        }
        return true;
    }

    @Override
    public boolean clearPriorSupplier(Long prdId) {
        if (prdId == null) {
            return false;
        }
        return supplierProductMapMapper.clearPriorSupplier(prdId) > 0;
    }

    @Override
    public SupplierProductMap getBypltfpidAndSplpid(Long prdId, Long splId) {
        if (prdId == null
                || splId == null) {
            return null;
        }
        return supplierProductMapMapper.getBypltfpidAndSplpid(prdId, splId);
    }

    @Override
    public int clearPriorSupplierBySplId(Long splId) {
        return supplierProductMapMapper.clearPriorSupplierBySplId(splId);
    }

    @Override
    public int clearPriorSupplierBySupplierId(Long supplierId) {
        return supplierProductMapMapper.clearPriorSupplierBySupplierId(supplierId);
    }
}
