package com.cmcc.vrp.province.webin.controller;

import com.cmcc.vrp.province.model.Authority;
import com.cmcc.vrp.province.model.Role;
import com.cmcc.vrp.province.service.AuthorityService;
import com.cmcc.vrp.province.service.RoleAuthorityService;
import com.cmcc.vrp.province.service.RoleService;
import com.cmcc.vrp.util.QueryObject;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

@RunWith(EasyMockRunner.class)
public class RoleControllerTest {

    @TestSubject
    private final RoleController controller = new RoleController();
    ModelMap map = new ModelMap();
    QueryObject queryObject = new QueryObject();
    @Mock
    private RoleService roleService;
    @Mock
    private AuthorityService authorityService;
    @Mock
    private RoleAuthorityService roleAuthorityService;

    /** 
     * @Title: testAddOrEditShow 
     * @Author: wujiamin
     * @date 2016年10月17日下午4:37:02
    */
    @Test
    public void testAddOrEditShow() {

        List<Authority> authorities = new ArrayList<Authority>();

        Role role = new Role();
        role.setRoleId(1l);

        expect(authorityService.selectAllAuthority()).andReturn(authorities);

        expect(roleService.getRoleById(1l)).andReturn(new Role());

        expect(roleAuthorityService.selectAuthsByRoleId(1l)).andReturn(
            new ArrayList<Long>());

        replay(authorityService, roleService, roleAuthorityService);

        Assert.assertEquals(controller.addOrEditShow(map, role),
            "role/addOrEdit.ftl");

        verify(authorityService, roleService, roleAuthorityService);
    }

    @Ignore
    @Test
    public void testaddSubmit() {

//		Role role = new Role();
//		role.setName("kok");
//		role.setCode("290");
//		// role.setRoleId(1l);
//
//		Long[] authorities = new Long[] { 1l };
//
//		expect(roleService.getAvailableRoles())
//				.andReturn(new ArrayList<Role>());
//
//		expect(
//				roleService.insertNewRoleWithAuthIds(role,
//						Arrays.asList(authorities))).andReturn(true);
//
//		replay(roleService);
//
//		Assert.assertEquals(controller.addOrEditSubmit(map, role, authorities),
//				"redirect:index.html");
//
//		verify(roleService);
    }

    @Ignore
    @Test
    public void testEditSubmit() {

//		Role role = new Role();
//		role.setName("kok");
//		role.setCode("290");
//		role.setRoleId(1l);
//
//		Long[] authorities = new Long[] { 1l };
//
//		expect(roleService.getAvailableRoles())
//				.andReturn(new ArrayList<Role>());
//		expect(
//				roleService.updateRoleWithAuthIds(role,
//						Arrays.asList(authorities))).andReturn(true);
//
//		replay(roleService);
//
//		Assert.assertEquals(controller.addOrEditSubmit(map, role, authorities),
//				"redirect:index.html");
//
//		verify(roleService);
    }

    @Ignore
    @Test
    public void testIndex() {

//		expect(roleService.queryPaginationRoleCount(queryObject)).andReturn(10);
//		expect(roleService.queryPaginationRoleList(queryObject)).andReturn(
//				new ArrayList<Role>());
//
//		replay(roleService);
//
//		Assert.assertEquals(controller.index(map, queryObject),
//				"role/index.ftl");
//
//		verify(roleService);
    }

    /** 
     * @Title: testDelete 
     * @Author: wujiamin
     * @date 2016年10月17日下午4:39:30
    */
    @Test
    public void testDelete() {

        expect(roleService.deleteRoleById(1l)).andReturn(true);
    }
}
