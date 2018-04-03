package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.RoleMapper;
import com.cmcc.vrp.province.model.Role;
import com.cmcc.vrp.province.model.RoleAuthority;
import com.cmcc.vrp.province.service.AdminRoleService;
import com.cmcc.vrp.province.service.RoleAuthorityService;
import com.cmcc.vrp.province.service.RoleService;
import com.cmcc.vrp.util.QueryObject;
import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


/**
 * 
 * @author wujiamin
 * @date 2016年10月27日
 */
@RunWith(EasyMockRunner.class)
public class RoleServiceImplTest {
    @TestSubject
    private final RoleService roleService = new RoleServiceImpl();
    @Mock
    AdminRoleService adminRoleService;
    @Mock
    private RoleMapper roleMapper;
    @Mock
    private RoleAuthorityService roleAuthorityService;

    /**
     * @Title:testgetRoleById
     * @Description: 测试RoleService的getRoleById(roleId)函数
     * @author: qihang
     */
    @Test
    public void testGetRoleById() {
        assertNull(roleService.getRoleById(null));//参数为空

        Role role = getValidRole();

        //record
        //设定role，数据库存在该数据,
        EasyMock.expect(roleMapper.selectByPrimaryKey(role.getRoleId())).andReturn(new Role());

        //replay
        EasyMock.replay(roleMapper);
        assertNotNull(roleService.getRoleById(role.getRoleId()));//存在数据时返回非空

        //verify
        EasyMock.verify(roleMapper);
    }


    /**
     * @Title:testgetRoleById
     * @Description: 测试RoleService的getRoleByCode(roleId)函数
     * @author: qihang
     */
    @Test
    public void testGetRoleByCode() {
        assertNull(roleService.getRoleByCode(null));//参数为空
        assertNull(roleService.getRoleByCode(""));//参数为空

        Role role = getValidRole();

        //record
        //设定role，数据库存在该数据,
        EasyMock.expect(roleMapper.selectByCode(role.getCode())).andReturn(new Role());

        //replay
        EasyMock.replay(roleMapper);
        assertNotNull(roleService.getRoleByCode(role.getCode()));//存在数据时返回非空

        //verify
        EasyMock.verify(roleMapper);
    }

    /**
     * @Title:testGetAvailableRoles()
     * @Description: 测试RoleService的getAvailableRoles()函数
     * @author: qihang
     */
    @Test
    public void testGetAvailableRoles() {
        //record
        EasyMock.expect(roleMapper.getAvailableRoles()).andReturn(new LinkedList<Role>());

        //replay
        EasyMock.replay(roleMapper);
        assertNotNull(roleService.getAvailableRoles());

        //verify
        EasyMock.verify(roleMapper);
    }

    /**
     * @Title:testInsertNewRoleWithAuthIdsSuccess
     * @Description: 测试RoleService的insertNewRoleWithAuthIds(Role role, List<Long> authList) 成功的函数
     * @author: qihang
     */
    @Test
    public void testInsertWithAuth() {
        Role role = getValidRole();

        List<Long> authList = new LinkedList<Long>();
        authList.add(1L);
        authList.add(2L);

        //role为null时返回0
        assertFalse(roleService.insertNewRoleWithAuthIds(null, authList));

        //测试时默认插入数据均成功
        //数据库不存在code
        EasyMock.expect(roleMapper.selectByCode(role.getCode())).andReturn(null);
        //EasyMock.expect(roleMapper.insertSelective(role)).andReturn(1);
        EasyMock.expect(roleMapper.insert(role)).andReturn(1);
        EasyMock.expect(roleAuthorityService.insert(EasyMock.anyObject(RoleAuthority.class))).andReturn(true);
        EasyMock.expect(roleAuthorityService.insert(EasyMock.anyObject(RoleAuthority.class))).andReturn(true);

        //replay
        EasyMock.replay(roleMapper);
        EasyMock.replay(roleAuthorityService);


        //数据正确，验证成功
        assertTrue(roleService.insertNewRoleWithAuthIds(role, authList));

        //verify
        EasyMock.verify(roleMapper);
        EasyMock.verify(roleAuthorityService);
    }
    
    @Test
    public void testInsertWithAuth1() {
        Role role = getValidRole();

        List<Long> authList = new LinkedList<Long>();
        authList.add(1L);
        authList.add(2L);

        //测试时默认插入数据均成功
        //数据库不存在code
        EasyMock.expect(roleMapper.selectByCode(role.getCode())).andReturn(null);
        //EasyMock.expect(roleMapper.insertSelective(role)).andReturn(1);
        EasyMock.expect(roleMapper.insert(role)).andReturn(0);

        //replay
        EasyMock.replay(roleMapper);
        try{
            roleService.insertNewRoleWithAuthIds(role, authList);
        }catch(Exception e){
            
        }
        //verify
        EasyMock.verify(roleMapper);
    }
    
    @Test
    public void testInsertWithAuth2() {
        Role role = getValidRole();

        List<Long> authList = new LinkedList<Long>();
        authList.add(1L);
        authList.add(2L);

        //测试时默认插入数据均成功
        //数据库不存在code
        EasyMock.expect(roleMapper.selectByCode(role.getCode())).andReturn(null);
        //EasyMock.expect(roleMapper.insertSelective(role)).andReturn(1);
        EasyMock.expect(roleMapper.insert(role)).andReturn(1);
        EasyMock.expect(roleAuthorityService.insert(EasyMock.anyObject(RoleAuthority.class))).andReturn(false);

        //replay
        EasyMock.replay(roleMapper);
        EasyMock.replay(roleAuthorityService);
        try{
            roleService.insertNewRoleWithAuthIds(role, authList);
        }catch(Exception e){
            
        }
        //verify
        EasyMock.verify(roleMapper);
    }

    /**
     * @Title:testInsertNewRoleWithAuthIdsSuccess
     * @Description: 测试RoleService的insertNewRoleWithAuthIds(Role role, List<Long> authList) 成功的函数
     * 不需要任何权限
     * @author: qihang
     */
    @Test
    public void testInsertWithoutAuth() {
        Role role = getValidRole();
        role.setRoleStatus(null);
        role.setCanBeDeleted(null);

        //测试时默认插入数据均成功
        //数据库不存在code
        EasyMock.expect(roleMapper.selectByCode(role.getCode())).andReturn(null);
        EasyMock.expect(roleMapper.insert(role)).andReturn(1);

        //replay
        EasyMock.replay(roleMapper);

        //数据正确，验证成功
        assertTrue(roleService.insertNewRoleWithAuthIds(role, null));

        //verify
        EasyMock.verify(roleMapper);
    }

    /**
     * @Title:testInsertNewRoleWithAuthIdsParamsErr
     * @Description: 测试RoleService的insertNewRoleWithAuthIds(Role role, List<Long> authList) 失败的函数
     * 验证参数错误，1.Role 为null 2.role.getCode为null或"" 3.role.getName为null或"" 4.role.getCreator为null
     * 5.role.getCode在数据库里已存在
     * @author: qihang
     */
    @Test
    public void testInsertParamsErr() {
        //1.Role 为null
        assertFalse(roleService.insertNewRoleWithAuthIds(null, null));

        //2.role.getCode为null
        Role role = getValidRole();
        role.setCode(null);
        assertFalse(roleService.insertNewRoleWithAuthIds(role, null));
        role.setCode("");
        assertFalse(roleService.insertNewRoleWithAuthIds(role, null));

        //3.role.getName为null或""
        role = getValidRole();
        role.setName(null);
        assertFalse(roleService.insertNewRoleWithAuthIds(role, null));
        role.setName("");
        assertFalse(roleService.insertNewRoleWithAuthIds(role, null));

        //4.role.getCreator为null
        role = getValidRole();
        role.setCreator(null);
        assertFalse(roleService.insertNewRoleWithAuthIds(role, null));

        //5.role.getCode在数据库里已存在
        role = getValidRole();
        EasyMock.expect(roleMapper.selectByCode(role.getCode())).andReturn(role);
        EasyMock.replay(roleMapper);
        assertFalse(roleService.insertNewRoleWithAuthIds(role, null));
        EasyMock.verify(roleMapper);
    }

    /**
     * @Title:testInsertNewRoleWithAuthIdsParamsErr
     * @Description: 测试RoleService的insertNewRoleWithAuthIds(Role role, List<Long> authList) 失败的函数
     * 数据库insertSelective失败
     * @author: qihang
     */
    @Ignore
    @Test
    public void testInsertDatabaseErr() {
        Role role = getValidRole();

        //测试时默认插入数据均成功
        //数据库不存在code
        EasyMock.expect(roleMapper.selectByCode(role.getCode())).andReturn(null);
        EasyMock.expect(roleMapper.insert(role)).andReturn(0);

        //replay
        EasyMock.replay(roleMapper);

        //数据正确，验证成功
        assertFalse(roleService.insertNewRoleWithAuthIds(role, null));

        //verify
        EasyMock.verify(roleMapper);
    }

    /**
     * @Title:testupdateRoleWithAuthIds()
     * @Description: 测试RoleService的updateRoleWithAuthIds(Set<Long> oldAuthIds, Long[] enterAuthIds)函数
     * @author: qihang
     */
    @Test
    public void testUpdateWithAuthIds() {

        //测试用例，设定用户要求新加的权限项，用户需要新增权限项1,2,3
        List<Long> oldAuthIds = new LinkedList<Long>();
        List<Long> enterAuthIds = new LinkedList<Long>();

        //设定oldAuthIds包含元素（0,1,2），enterAuthIds包含元素（1,2,3）,即需要删除权限0，增加权限3

        for (int i = 0; i < 3; i++) {
            oldAuthIds.add((long) i);
            enterAuthIds.add((long) (i + 1));
        }

        //测试用例，需要新角色
        Role role = getValidRole();
        Long roleId = role.getRoleId();

        //expect
        EasyMock.expect(roleMapper.selectByCode(role.getCode())).andReturn(null);
        EasyMock.expect(roleMapper.updateByPrimaryKeySelective(role)).andReturn(1);
        EasyMock.expect(roleAuthorityService.selectAuthsByRoleId(roleId)).andReturn(oldAuthIds);
        EasyMock.expect(roleAuthorityService.deleteRoleAuthority(roleId, 0L)).andReturn(true);
        EasyMock.expect(roleAuthorityService.insert(roleId, 3L)).andReturn(true);


        //replay
        EasyMock.replay(roleMapper);
        EasyMock.replay(roleAuthorityService);

        assertTrue(roleService.updateRoleWithAuthIds(role, enterAuthIds));

        //verify
        EasyMock.verify(roleMapper);
        EasyMock.verify(roleAuthorityService);
    }
    
    @Test
    public void testUpdateWithAuthIds1() {

        //测试用例，设定用户要求新加的权限项，用户需要新增权限项1,2,3
        List<Long> oldAuthIds = new LinkedList<Long>();
        List<Long> enterAuthIds = new LinkedList<Long>();

        //设定oldAuthIds包含元素（0,1,2），enterAuthIds包含元素（1,2,3）,即需要删除权限0，增加权限3

        for (int i = 0; i < 3; i++) {
            oldAuthIds.add((long) i);
            enterAuthIds.add((long) (i + 1));
        }

        //测试用例，需要新角色
        Role role = getValidRole();

        Role role2 = getValidRole();;
        role2.setRoleId(2L);
        //expect
        EasyMock.expect(roleMapper.selectByCode(role.getCode())).andReturn(role2);

        //replay
        EasyMock.replay(roleMapper);
        
        assertFalse(roleService.updateRoleWithAuthIds(role, enterAuthIds));

        //verify
        EasyMock.verify(roleMapper);
    }
    @Test
    public void testUpdateWithAuthIds2() {

        //测试用例，设定用户要求新加的权限项，用户需要新增权限项1,2,3
        List<Long> oldAuthIds = new LinkedList<Long>();
        List<Long> enterAuthIds = new LinkedList<Long>();

        //设定oldAuthIds包含元素（0,1,2），enterAuthIds包含元素（1,2,3）,即需要删除权限0，增加权限3

        for (int i = 0; i < 3; i++) {
            oldAuthIds.add((long) i);
            enterAuthIds.add((long) (i + 1));
        }

        //测试用例，需要新角色
        Role role = getValidRole();
        Long roleId = role.getRoleId();

        //expect
        EasyMock.expect(roleMapper.selectByCode(role.getCode())).andReturn(null);
        EasyMock.expect(roleMapper.updateByPrimaryKeySelective(role)).andReturn(0);

        //replay
        EasyMock.replay(roleMapper);
        
        assertFalse(roleService.updateRoleWithAuthIds(role, enterAuthIds));

        //verify
        EasyMock.verify(roleMapper);
    }
    
    @Test
    public void testUpdateWithAuthIds3() {

        //测试用例，设定用户要求新加的权限项，用户需要新增权限项1,2,3
        List<Long> oldAuthIds = new LinkedList<Long>();
        List<Long> enterAuthIds = new LinkedList<Long>();

        //设定oldAuthIds包含元素（0,1,2），enterAuthIds包含元素（1,2,3）,即需要删除权限0，增加权限3

        for (int i = 0; i < 3; i++) {
            oldAuthIds.add((long) i);
            enterAuthIds.add((long) (i + 1));
        }

        //测试用例，需要新角色
        Role role = getValidRole();
        Long roleId = role.getRoleId();

      //expect
        EasyMock.expect(roleMapper.selectByCode(role.getCode())).andReturn(null);
        EasyMock.expect(roleMapper.updateByPrimaryKeySelective(role)).andReturn(1);
        EasyMock.expect(roleAuthorityService.selectAuthsByRoleId(roleId)).andReturn(oldAuthIds);
        EasyMock.expect(roleAuthorityService.deleteRoleAuthority(roleId, 0L)).andReturn(false);

        //replay
        EasyMock.replay(roleMapper);
        EasyMock.replay(roleAuthorityService);

        try{
            roleService.updateRoleWithAuthIds(role, enterAuthIds);
        }catch(Exception e){
            System.out.println(e.getMessage());           
        }

        //verify
        EasyMock.verify(roleMapper);
        EasyMock.verify(roleAuthorityService);
    }

    @Test
    public void testUpdateWithAuthIds4() {

        //测试用例，设定用户要求新加的权限项，用户需要新增权限项1,2,3
        List<Long> oldAuthIds = new LinkedList<Long>();
        List<Long> enterAuthIds = new LinkedList<Long>();

        //设定oldAuthIds包含元素（0,1,2），enterAuthIds包含元素（1,2,3）,即需要删除权限0，增加权限3

        for (int i = 0; i < 3; i++) {
            oldAuthIds.add((long) i);
            enterAuthIds.add((long) (i + 1));
        }

        //测试用例，需要新角色
        Role role = getValidRole();
        Long roleId = role.getRoleId();

        //expect
        EasyMock.expect(roleMapper.selectByCode(role.getCode())).andReturn(null);
        EasyMock.expect(roleMapper.updateByPrimaryKeySelective(role)).andReturn(1);
        EasyMock.expect(roleAuthorityService.selectAuthsByRoleId(roleId)).andReturn(oldAuthIds);
        EasyMock.expect(roleAuthorityService.deleteRoleAuthority(roleId, 0L)).andReturn(true);
        EasyMock.expect(roleAuthorityService.insert(roleId, 3L)).andReturn(false);


        //replay
        EasyMock.replay(roleMapper);
        EasyMock.replay(roleAuthorityService);

        try{
            roleService.updateRoleWithAuthIds(role, enterAuthIds);
        }catch(Exception e){
            System.out.println(e.getMessage());           
        }

        //verify
        EasyMock.verify(roleMapper);
        EasyMock.verify(roleAuthorityService);
    }
    
    /**
     * @Title:testupdateRoleWithAuthIds()
     * @Description: 测试RoleService的updateRoleWithAuthIds(Role role, List<Long> entryAuthIds)函数
     * 无权限需要删除和添加
     * @author: qihang
     */
    @Test
    public void testUpdateWithoutAuthIds() {
        //测试role为空时失败
        assertFalse(roleService.updateRoleWithAuthIds(null, null));
        //测试role的id为空时失败
        Role role = getValidRole();
        role.setRoleId(null);
        assertFalse(roleService.updateRoleWithAuthIds(role, null));


        //测试用例，角色没有权限
        role = getValidRole();
        Long roleId = role.getRoleId();
        List<Long> oldAuthIds = new LinkedList<Long>();

        //expect
        EasyMock.expect(roleMapper.selectByCode(role.getCode())).andReturn(null);
        EasyMock.expect(roleMapper.updateByPrimaryKeySelective(role)).andReturn(1);
        EasyMock.expect(roleAuthorityService.selectAuthsByRoleId(roleId)).andReturn(oldAuthIds);

        //replay
        EasyMock.replay(roleMapper);
        EasyMock.replay(roleAuthorityService);

        assertTrue(roleService.updateRoleWithAuthIds(role, null));

        //verify
        EasyMock.verify(roleMapper);
        EasyMock.verify(roleAuthorityService);
    }


    /**
     * @Title:testDeleteByRoleId()
     * @Description: 测试RoleService的deleteByPrimaryKey(roleId)函数
     * @author: qihang
     */
    @Test
    public void testDeleteByRoleId() {
        Role role = getValidRole();
        Long roleId = role.getRoleId();

        //record
        EasyMock.expect(roleMapper.selectByPrimaryKey(roleId)).andReturn(role);
        EasyMock.expect(roleMapper.updateByPrimaryKeySelective(role)).andReturn(1);
        EasyMock.expect(roleAuthorityService.deleteByRoleId(roleId)).andReturn(true);
        EasyMock.expect(adminRoleService.deleteByRoleId(roleId)).andReturn(true);


        //replay
        EasyMock.replay(roleMapper);
        EasyMock.replay(roleAuthorityService);
        EasyMock.replay(adminRoleService);
        assertTrue(roleService.deleteRoleById(roleId));

        //verify
        EasyMock.verify(roleMapper);
        EasyMock.verify(roleAuthorityService);
        EasyMock.verify(adminRoleService);
    }
    
    @Test
    public void testDeleteByRoleId2() {
        Role role = getValidRole();
        Long roleId = role.getRoleId();

        //record
        EasyMock.expect(roleMapper.selectByPrimaryKey(roleId)).andReturn(role);
        EasyMock.expect(roleMapper.updateByPrimaryKeySelective(role)).andReturn(1);
        EasyMock.expect(roleAuthorityService.deleteByRoleId(roleId)).andReturn(false);

        //replay
        EasyMock.replay(roleMapper);
        EasyMock.replay(roleAuthorityService);
        try{
            roleService.deleteRoleById(roleId);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        //verify
        EasyMock.verify(roleMapper);
        EasyMock.verify(roleAuthorityService);
    }

    @Test
    public void testDeleteByRoleId3() {
        Role role = getValidRole();
        Long roleId = role.getRoleId();

        //record
        EasyMock.expect(roleMapper.selectByPrimaryKey(roleId)).andReturn(role);
        EasyMock.expect(roleMapper.updateByPrimaryKeySelective(role)).andReturn(1);
        EasyMock.expect(roleAuthorityService.deleteByRoleId(roleId)).andReturn(true);
        EasyMock.expect(adminRoleService.deleteByRoleId(roleId)).andReturn(false);


        //replay
        EasyMock.replay(roleMapper);
        EasyMock.replay(roleAuthorityService);
        EasyMock.replay(adminRoleService);

        try{
            roleService.deleteRoleById(roleId);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        //verify
        EasyMock.verify(roleMapper);
        EasyMock.verify(roleAuthorityService);
        EasyMock.verify(adminRoleService);
    }
    /**
     * @Title:testDeleteByRoleId()
     * @Description: 测试RoleService的deleteByPrimaryKey(roleId)函数
     * @author: qihang
     */
    @Test
    public void testDeleteByRoleIdErr_NotExist() {
        assertFalse(roleService.deleteRoleById(null));

        Role role = getValidRole();
        Long roleId = role.getRoleId();

        //record
        EasyMock.expect(roleMapper.selectByPrimaryKey(roleId)).andReturn(null);


        //replay
        EasyMock.replay(roleMapper);

        assertFalse(roleService.deleteRoleById(roleId));

        //verify
        EasyMock.verify(roleMapper);

    }

    /**
     * @Title:testDeleteByRoleId()
     * @Description: 测试RoleService的deleteByPrimaryKey(roleId)函数
     * @author: qihang
     */
    @Test
    public void testDeleteByRoleIdErr_DeleteFailed() {

        Role role = getValidRole();
        Long roleId = role.getRoleId();

        //record
        EasyMock.expect(roleMapper.selectByPrimaryKey(roleId)).andReturn(role);
        EasyMock.expect(roleMapper.updateByPrimaryKeySelective(role)).andReturn(0);

        //replay
        EasyMock.replay(roleMapper);

        assertFalse(roleService.deleteRoleById(roleId));

        //verify
        EasyMock.verify(roleMapper);

    }

    /**
     * @Title:testQueryPaginationRoleCount()
     * @Description: 测试RoleService的queryPaginationRoleCount(QueryObject query)函数
     * @author: qihang
     */
    @Test
    public void testQueryPaginationRoleCount() {
        int count = 1;
        QueryObject query = new QueryObject();
        //record
        EasyMock.expect(roleMapper.queryPaginationRoleCount(EasyMock.anyObject(Map.class))).andReturn(count);

        //replay
        EasyMock.replay(roleMapper);

        assertEquals(roleService.queryPaginationRoleCount(query), 1);

        //verify
        EasyMock.verify(roleMapper);
    }

    /**
     * @Title:testQueryPaginationRoleList()
     * @Description: 测试RoleService的queryPaginationRoleList(QueryObject queryObject)函数
     * @author: qihang
     */
    @Test
    public void testQueryPaginationRoleList() {
        List<Role> list = new ArrayList<Role>();
        QueryObject query = new QueryObject();
        //record
        EasyMock.expect(roleMapper.queryPaginationRoleList(EasyMock.anyObject(Map.class))).andReturn(list);

        //replay
        EasyMock.replay(roleMapper);

        assertEquals(roleService.queryPaginationRoleList(query), list);

        //verify
        EasyMock.verify(roleMapper);
    }
    
    /**
     * @Title: testQueryPaginationRoleList 
     * @Author: wujiamin
     * @date 2016年10月27日
     */
    @Test
    public void testGetCreateRoleByRoleId() {
        List<Role> list = new ArrayList<Role>();
        //record
        EasyMock.expect(roleMapper.getCreateRoleByRoleId(1L)).andReturn(list);

        //replay
        EasyMock.replay(roleMapper);

        assertEquals(roleService.getCreateRoleByRoleId(1L), list);

        //verify
        EasyMock.verify(roleMapper);
    }
    
    @Test
    public void testGetAllRoles() {
        List<Role> list = new ArrayList<Role>();
        //record
        EasyMock.expect(roleMapper.getAllRoles()).andReturn(list);

        //replay
        EasyMock.replay(roleMapper);

        assertEquals(roleService.getAllRoles(), list);

        //verify
        EasyMock.verify(roleMapper);
    }

    /**
     * @Title:getValidRole
     * @Description: 生成一个可用于测试的角色
     * @author: qihang
     */
    public Role getValidRole() {
        Role role = new Role();
        role.setRoleId(1L);
        role.setName("超级管理员");
        role.setCode("1");
        role.setCreateTime(new Date());
        role.setCreator(1L);
        role.setUpdateTime(new Date());
        role.setDeleteFlag(0);
        role.setRoleStatus(0);
        role.setCanBeDeleted(0);
        return role;

    }
}
