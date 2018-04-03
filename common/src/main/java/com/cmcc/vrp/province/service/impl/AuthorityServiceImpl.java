package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.AuthorityMapper;
import com.cmcc.vrp.province.model.Authority;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.RoleAuthority;
import com.cmcc.vrp.province.service.AuthorityService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.RoleAuthorityService;
import com.cmcc.vrp.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 权限操作实现类
 *
 * @author zhoujianbin
 */
@Service("authorityService")
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    @Qualifier("authorityMapper")
    private AuthorityMapper authorityMapper;

    @Autowired
    private RoleAuthorityService roleAuthorityService;

    @Autowired
    private ManagerService managerService;


    /**
     * selectAllAuthority
     *
     */
    public List<Authority> selectAllAuthority() {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("deleteFlag", Constants.DELETE_FLAG.UNDELETED.getValue());
        return authorityMapper.selectByQuery(param);
    }


    /**
     * 获取用户的权限Code
     *
     * @param userName
     * @return
     */
    public Set<String> queryAuthCodesByUserName(String userName) {
        List<Authority> auths = authorityMapper.queryUserAuthsByUserName(userName);
        Set<String> codes = new HashSet<String>();
        if (auths == null) {
            return codes;
        }
        Iterator<Authority> it = auths.iterator();
        while (it.hasNext()) {
            codes.add(it.next().getCode());
        }
        return codes;
    }

    /**
     * 获取用户的权限URL
     *
     * @param userName
     * @return
     */
    public Set<String> queryAuthUrlsByUserName(String userName) {
        List<Authority> auths = authorityMapper.queryUserAuthsByUserName(userName);
        Set<String> urls = new HashSet<String>();
        if (auths == null) {
            return urls;
        }
        Iterator<Authority> it = auths.iterator();
        while (it.hasNext()) {
            urls.add(it.next().getAuthorityUrl());
        }
        return urls;
    }


    /**
     * 判断某个管理员是否有某权限
     *
     * @param managerId
     * @param authorityName
     * @return
     * @date 2016年7月22日
     * @author wujiamin
     */
    @Override
    public boolean ifHaveAuthority(Long managerId, String authorityName) {
        List<RoleAuthority> ars = roleAuthorityService.selectExistingRoleAuthorityByAuthorityName(authorityName);
        if (ars != null && ars.size() > 0) {
            for (RoleAuthority ar : ars) {
                Manager manager = managerService.selectByPrimaryKey(managerId);
                if (manager.getRoleId().equals(ar.getRoleId())) {
                    return true;
                }
            }

        }
        return false;
    }

    @Override
    public boolean insert(Authority authority) {
        if (authority != null) {
            return authorityMapper.insert(authority) == 1;
        }
        return false;
    }

    @Override
    public Authority get(Long id) {
        if (id != null) {
            return authorityMapper.selectByPrimaryKey(id);
        }
        return null;
    }

    @Override
    public boolean updateByPrimaryKeySelective(Authority authority) {
        if (authority != null) {
            return authorityMapper.updateByPrimaryKeySelective(authority) == 1;
        }
        return false;
    }

    @Override
    public int countAuthority(Map<String, Object> map) {
        return map == null ? 0 : authorityMapper.countAuthority(map);
    }

    @Override
    public List<Authority> selectAuthorityPage(Map<String, Object> map) {
        return map == null ? null : authorityMapper.selectAuthorityPage(map);
    }

    @Override
    public boolean uniqueAuthority(Authority authority) {

        Long currentId = authority.getAuthorityId();
        String currentCode = authority.getCode();
        String currentAuthorityName = authority.getAuthorityName();

        Boolean bFlag = true;
        List<Authority> authorityList = selectAllAuthority();
        for (Authority auth : authorityList) {
            if (auth.getCode().equalsIgnoreCase(currentCode)
                || auth.getAuthorityName().equalsIgnoreCase(currentAuthorityName)) {
                if (!auth.getAuthorityId().equals(currentId)) {
                    bFlag = false;
                }
                break;
            }
        }
        return bFlag;
    }

    @Override
    public boolean deleteAuthorityById(Long authorityId) {
        if (authorityId != null) {
            Authority authority = new Authority();
            authority.setAuthorityId(authorityId);
            authority.setDeleteFlag(Constants.DELETE_FLAG.DELETED.getValue());
            return authorityMapper.updateByPrimaryKeySelective(authority) == 1;
        }
        return false;
    }


}