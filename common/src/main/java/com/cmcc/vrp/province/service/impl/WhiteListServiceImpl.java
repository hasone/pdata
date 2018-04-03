/**
 * @Title: WhiteListServiceImpl.java
 * @Package com.cmcc.vrp.province.service.impl
 * @author: sunyiwei
 * @date: 2015年6月10日 上午11:10:05
 * @version V1.0
 */
package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.WhiteListMapper;
import com.cmcc.vrp.province.model.WhiteList;
import com.cmcc.vrp.province.service.WhiteListService;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: WhiteListServiceImpl
 * @Description: 白名单服务实现
 * @author: sunyiwei
 * @date: 2015年6月10日 上午11:10:05
 *
 */
@Service("whiteListService")
public class WhiteListServiceImpl implements WhiteListService {

    @Autowired
    private WhiteListMapper mapper;


    /*
     * 获取白名单对象
     */
    @Override
    public WhiteList get(String mobile) {
        if (!StringUtils.isValidMobile(mobile)) {
            return null;
        }

        return mapper.get(mobile);
    }

    /*
     * 删除白名单对象，逻辑删除
     */
    @Override
    public boolean delete(String mobile) {
        if (!StringUtils.isValidMobile(mobile)) {
            return false;
        }

        return mapper.delete(mobile) == 1;
    }

    /*
     * 插入白名单对象
     */
    @Override
    public boolean insert(String mobile) {
        if (!StringUtils.isValidMobile(mobile)) {
            return false;
        }

        WhiteList wl = getWhiteList(mobile);
        if (wl != null && mapper.get(mobile) == null) {
            return mapper.insert(wl) == 1;
        }

        return false;
    }

    /*
     * 由手机号生成白名单对象
     */
    private WhiteList getWhiteList(String mobile) {
        if (!StringUtils.isValidMobile(mobile)) {
            return null;
        }

        WhiteList wl = new WhiteList();
        wl.setCreateTime(new Date());
        wl.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
        wl.setMobile(mobile);
        wl.setUpdateTime(new Date());

        return wl;
    }

    @Override
    public Long count(QueryObject queryObject) {
        if (queryObject == null) {
            return 0L;
        }

        return mapper.count(queryObject.getQueryCriterias());
    }

    @Override
    public List<WhiteList> query(QueryObject queryObject) {
        if (queryObject == null) {
            return null;
        }

        return mapper.query(queryObject.toMap());
    }

    @Override
    public boolean delete(Long id) {
        if (id == null) {
            return false;
        }

        return mapper.deleteById(id) == 1;
    }

    @Override
    public boolean insertBatch(List<String> list) {
        if (list == null) {
            return false;
        }

        List<String> newList = new ArrayList<String>();
        for (String mobile : list) {
            if (!StringUtils.isValidMobile(mobile)
                || newList.contains(mobile)
                || mapper.get(mobile) != null) {
                continue;
            }

            newList.add(mobile);
        }

        if (newList == null || newList.size() == 0) {
            return true;
        }

        return mapper.insertBatch(newList) == newList.size();
    }


}
