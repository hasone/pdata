/**
 * @Title: UserInfoServiceImpl.java
 * @Package com.cmcc.vrp.province.service.impl
 * @author: sunyiwei
 * @date: 2015年6月9日 下午4:42:19
 * @version V1.0
 */
package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.exception.SelfCheckException;
import com.cmcc.vrp.province.dao.UserInfoMapper;
import com.cmcc.vrp.province.model.UserInfo;
import com.cmcc.vrp.province.service.UserInfoService;
import com.cmcc.vrp.util.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: UserInfoServiceImpl
 * @Description: 用户服务实现类
 * @author: sunyiwei
 * @date: 2015年6月9日 下午4:42:19
 *
 */
@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {

    private static Logger logger = Logger.getLogger(UserInfoService.class);

    @Autowired
    private UserInfoMapper mapper;


    /**
     *
     * @Title: get
     * @Description: TODO
     * @param id
     * @return
     * @see com.cmcc.vrp.province.service.UserInfoService#get(java.lang.Long)
     */
    @Override
    public UserInfo get(Long id) {
        if (id == null) {
            return null;
        }

        return mapper.get(id);
    }

    /**
     * @Title: get
     * @Description: TODO
     * @param mobile
     * @return
     * @see com.cmcc.vrp.province.service.UserInfoService#get(java.lang.String)
     */
    @Override
    public UserInfo get(String mobile) {
        if (!StringUtils.isValidMobile(mobile)) {
            return null;
        }

        return mapper.getByMobile(mobile);
    }

    /**
     * @Title: insert
     * @Description: TODO
     * @param userInfo
     * @return
     * @see com.cmcc.vrp.province.service.UserInfoService#insert(com.cmcc.vrp.province.model.UserInfo)
     */
    @Override
    public boolean insert(UserInfo userInfo) {
        try {
            UserInfo.selfCheck(userInfo);
        } catch (SelfCheckException e) {
            return false;
        }

        //不能插入重复的记录
        if (mapper.getByMobile(userInfo.getMobile()) != null) {
            return false;
        }

        return mapper.insert(userInfo) == 1;
    }

    /**
     * @Title: delete
     * @Description: TODO
     * @param id
     * @return
     * @see com.cmcc.vrp.province.service.UserInfoService#delete(java.lang.Long)
     */
    @Override
    public boolean delete(Long id) {
        if (id == null) {
            return false;
        }

        return mapper.delete(id) == 1;
    }

    /**
     * @Title: delete
     * @Description: TODO
     * @param mobile
     * @return
     * @see com.cmcc.vrp.province.service.UserInfoService#delete(java.lang.String)
     */
    @Override
    public boolean delete(String mobile) {
        if (!StringUtils.isValidMobile(mobile)) {
            return false;
        }

        return mapper.deleteByMobile(mobile) == 1;
    }

}
