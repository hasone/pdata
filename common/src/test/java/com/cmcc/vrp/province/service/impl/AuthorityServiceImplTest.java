/**
 *
 */
package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.AuthorityMapper;
import com.cmcc.vrp.province.model.Authority;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.RoleAuthority;
import com.cmcc.vrp.province.service.AuthorityService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.RoleAuthorityService;
import com.cmcc.vrp.util.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * <p>Description: 权限操作单元测试类</p>
 *
 * @author xj
 * @date 2016年5月18日
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthorityServiceImplTest {

    @Mock
    AuthorityMapper authorityMapper;

    @InjectMocks
    AuthorityService authorityService = new AuthorityServiceImpl();
    @Mock
    RoleAuthorityService roleAuthorityService;
    @Mock
    ManagerService managerService;
    @Test
    public void testSelectAllAuthority() {

        List<Authority> authorityList = new ArrayList<Authority>();

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("deleteFlag", Constants.DELETE_FLAG.UNDELETED.getValue());

        when(authorityMapper.selectByQuery(param)).thenReturn(authorityList);
        assertNotNull(authorityService.selectAllAuthority());
        verify(authorityMapper).selectByQuery(param);

    }

    @Test
    public void testqueryAuthCodesByUserName() {

        List<Authority> auths = new ArrayList<Authority>();
        String userName = "aaa";

        when(authorityMapper.queryUserAuthsByUserName(userName)).thenReturn(auths);

        assertNotNull(authorityService.queryAuthCodesByUserName(userName));
        verify(authorityMapper).queryUserAuthsByUserName(userName);

    }

    @Test
    public void testqueryAuthCodesByUserName2() {

        String userName = "aaa";

        when(authorityMapper.queryUserAuthsByUserName(userName)).thenReturn(null);

        assertNotNull(authorityService.queryAuthCodesByUserName(userName));
        verify(authorityMapper).queryUserAuthsByUserName(userName);

    }

    @Test
    public void testqueryAuthCodesByUserName3() {

        String userName = "aaa";

        List<Authority> auths = new ArrayList<Authority>();
        Authority authority = new Authority();
        authority.setCode("1");
        auths.add(authority);

        when(authorityMapper.queryUserAuthsByUserName(userName)).thenReturn(auths);

        assertNotNull(authorityService.queryAuthCodesByUserName(userName));
        verify(authorityMapper).queryUserAuthsByUserName(userName);
    }

    @Test
    public void testQueryAuthUrlsByUserName() {

        List<Authority> auths = new ArrayList<Authority>();
        String userName = "aaa";

        when(authorityMapper.queryUserAuthsByUserName(userName)).thenReturn(auths);

        assertNotNull(authorityService.queryAuthUrlsByUserName(userName));
        verify(authorityMapper).queryUserAuthsByUserName(userName);

    }

    @Test
    public void testQueryAuthUrlsByUserName2() {

        String userName = "aaa";

        when(authorityMapper.queryUserAuthsByUserName(userName)).thenReturn(null);

        assertNotNull(authorityService.queryAuthUrlsByUserName(userName));
        verify(authorityMapper).queryUserAuthsByUserName(userName);

    }

    @Test
    public void testQueryAuthUrlsByUserName3() {

        String userName = "aaa";

        List<Authority> auths = new ArrayList<Authority>();
        Authority authority = new Authority();
        authority.setAuthorityUrl("test");
        auths.add(authority);

        when(authorityMapper.queryUserAuthsByUserName(userName)).thenReturn(auths);

        assertNotNull(authorityService.queryAuthUrlsByUserName(userName));
        verify(authorityMapper).queryUserAuthsByUserName(userName);

    }

    @Test
    public void testIfHaveAuthority(){
        Long managerId = 1L;
        String authorityName = "test";
        Mockito.when(roleAuthorityService.selectExistingRoleAuthorityByAuthorityName(anyString()))
                .thenReturn(new ArrayList<RoleAuthority>());
        assertFalse(authorityService.ifHaveAuthority(managerId, authorityName));
        Mockito.verify(roleAuthorityService).selectExistingRoleAuthorityByAuthorityName(anyString());
    }

    @Test
    public void testIfHaveAuthority1(){
        List<RoleAuthority> ars = new ArrayList<RoleAuthority>();
        RoleAuthority roleAuthority = new RoleAuthority();
        roleAuthority.setRoleId(1L);
        ars.add(roleAuthority);

        Manager manager = new Manager();
        manager.setRoleId(1L);

        Long managerId = 1L;
        String authorityName = "test";

        Mockito.when(roleAuthorityService.selectExistingRoleAuthorityByAuthorityName(anyString()))
        .thenReturn(ars);
        Mockito.when(managerService.selectByPrimaryKey(anyLong())).thenReturn(manager);
        assertTrue(authorityService.ifHaveAuthority(managerId, authorityName));
        Mockito.verify(roleAuthorityService).selectExistingRoleAuthorityByAuthorityName(anyString());
        Mockito.verify(managerService).selectByPrimaryKey(anyLong());
    }

    @Test
    public void testInsert(){
        assertFalse(authorityService.insert(null));

        Mockito.when(authorityMapper.insert(any(Authority.class))).thenReturn(1);
        assertTrue(authorityService.insert(new Authority()));
        Mockito.verify(authorityMapper).insert(any(Authority.class));
    }

    @Test
    public void testGet(){
        assertNull(authorityService.get(null));

        Long id = 1L;
        Mockito.when(authorityMapper.selectByPrimaryKey(anyLong())).thenReturn(new Authority());
        assertNotNull(authorityService.get(id));
        Mockito.verify(authorityMapper).selectByPrimaryKey(anyLong());
    }

    @Test
    public void testUpdateByPrimaryKeySelective(){
        assertFalse(authorityService.updateByPrimaryKeySelective(null));

        Mockito.when(authorityMapper.updateByPrimaryKeySelective(any(Authority.class))).thenReturn(1);
        assertTrue(authorityService.updateByPrimaryKeySelective(new Authority()));
        Mockito.verify(authorityMapper).updateByPrimaryKeySelective(any(Authority.class));
    }

    @Test
    public void testCountAuthority(){
        assertSame(0, authorityService.countAuthority(null));
    }

    @Test
    public void testCountAuthority1(){
        Mockito.when(authorityMapper.countAuthority(anyMap())).thenReturn(1);
        assertSame(1, authorityService.countAuthority(anyMap()));
        Mockito.verify(authorityMapper).countAuthority(anyMap());
    }

    @Test
    public void testSelectAuthorityPage(){
        assertNull(authorityService.selectAuthorityPage(null));

        Mockito.when(authorityMapper.selectAuthorityPage(anyMap())).thenReturn(new ArrayList<Authority>());
        assertNotNull(authorityService.selectAuthorityPage(anyMap()));
        Mockito.verify(authorityMapper).selectAuthorityPage(anyMap());
    }

    @Test
    public void testUniqueAuthority(){
        List<Authority> authorityList = new ArrayList<Authority>();
        Authority authority = new Authority();
        authority.setAuthorityId(1L);
        authority.setCode("1");
        authority.setAuthorityName("test");
        authorityList.add(authority);

        Mockito.when(authorityMapper.selectByQuery(anyMap())).thenReturn(authorityList);
        assertTrue(authorityService.uniqueAuthority(authority));
        Mockito.verify(authorityMapper).selectByQuery(anyMap());
    }

    @Test
    public void testUniqueAuthority1(){
        Authority authority1 = new Authority();
        authority1.setAuthorityId(2L);
        authority1.setCode("2");
        authority1.setAuthorityName("test");

        List<Authority> authorityList = new ArrayList<Authority>();
        Authority authority = new Authority();
        authority.setAuthorityId(1L);
        authority.setCode("1");
        authority.setAuthorityName("test");
        authorityList.add(authority);

        Mockito.when(authorityMapper.selectByQuery(anyMap())).thenReturn(authorityList);
        assertFalse(authorityService.uniqueAuthority(authority1));
        Mockito.verify(authorityMapper).selectByQuery(anyMap());
    }

    @Test
    public void testDeleteAuthorityById(){
        assertFalse(authorityService.deleteAuthorityById(null));

        Long authorityId = 1L;
        Mockito.when(authorityMapper.updateByPrimaryKeySelective(any(Authority.class))).thenReturn(1);
        assertTrue(authorityService.deleteAuthorityById(authorityId));
        Mockito.verify(authorityMapper).updateByPrimaryKeySelective(any(Authority.class));
    }




}
