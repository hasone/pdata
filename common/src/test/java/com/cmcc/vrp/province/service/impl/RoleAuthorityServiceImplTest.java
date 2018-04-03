/**
 * @Title: RoleAuthorityServiceImplTest.java
 * @Package com.cmcc.vrp.province.service.impl
 * @author: qihang
 * @date: 2015年4月21日 下午3:58:59
 * @version V1.0
 */
package com.cmcc.vrp.province.service.impl;


import com.cmcc.vrp.province.dao.RoleAuthorityMapper;
import com.cmcc.vrp.province.model.RoleAuthority;
import com.cmcc.vrp.province.service.RoleAuthorityService;

import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @ClassName: RoleAuthorityServiceImplTest
 * @Description: 测试角色权限管理实现类
 * @author: qihang
 * @date: 2015年4月21日 下午3:58:59
 *
 */
@RunWith(EasyMockRunner.class)
public class RoleAuthorityServiceImplTest {
    @TestSubject
    private final RoleAuthorityService roleAuthorityService = new RoleAuthorityServiceImpl();

    @Mock
    RoleAuthorityMapper roleAuthorityMapper;

    /**
     * @Title:testInsert()
     * @Description: 测试Insert(roleAuthority)
     * @author: qihang
     */
    @Test
    public void testInsert() {
        //测试roleAuthority的RoleId为空
        RoleAuthority roleAuthority = generateRoleAuthority();
        EasyMock.expect(roleAuthorityMapper.select(EasyMock.anyObject(Map.class))).andReturn(null);
        EasyMock.expect(roleAuthorityMapper.insert(roleAuthority)).andReturn(1);
        EasyMock.replay(roleAuthorityMapper);
        assertTrue(roleAuthorityService.insert(roleAuthority));
        EasyMock.verify(roleAuthorityMapper);

    }

    /**
     * @Title:testInsert_err1
     * @Description: 测试Insert(roleAuthority) 错误，
     * @author: qihang
     */
    @Test
    public void testInsert_err1() {
        //测试roleAuthority为空
        assertFalse(roleAuthorityService.insert(null));

        //测试roleAuthority的RoleId为空
        RoleAuthority roleAuthority = generateRoleAuthority();
        roleAuthority.setRoleId(null);
        assertFalse(roleAuthorityService.insert(roleAuthority));

        //测试roleAuthority的AuthorityId为空
        roleAuthority = generateRoleAuthority();
        roleAuthority.setAuthorityId(null);
        assertFalse(roleAuthorityService.insert(roleAuthority));

        //测试有重复记录不能够插入成功
        roleAuthority = generateRoleAuthority();
        EasyMock.expect(roleAuthorityMapper.select(EasyMock.anyObject(Map.class))).andReturn(roleAuthority);
        EasyMock.replay(roleAuthorityMapper);
        assertFalse(roleAuthorityService.insert(roleAuthority));
        EasyMock.verify(roleAuthorityMapper);
    }


    /**
     * @Title:testInsert_err2
     * @Description: 测试Insert(roleAuthority) 错误，
     * @author: qihang
     */
    @Test
    public void testInsert_err2() {
        //测试数据库的insert插入失败
        RoleAuthority roleAuthority = generateRoleAuthority();
        EasyMock.expect(roleAuthorityMapper.select(EasyMock.anyObject(Map.class))).andReturn(null);
        EasyMock.expect(roleAuthorityMapper.insert(roleAuthority)).andReturn(0);
        EasyMock.replay(roleAuthorityMapper);
        assertFalse(roleAuthorityService.insert(roleAuthority));
        EasyMock.verify(roleAuthorityMapper);
    }

    @Test
    public void testInsert2() {
        EasyMock.expect(roleAuthorityMapper.select(EasyMock.anyObject(Map.class))).andReturn(null);
        EasyMock.expect(roleAuthorityMapper.insert(EasyMock.anyObject(RoleAuthority.class))).andReturn(1);
        EasyMock.replay(roleAuthorityMapper);
        assertTrue(roleAuthorityService.insert(1L,1L));
        EasyMock.verify(roleAuthorityMapper);
    }   
    
    @Test
    public void testSelect_error_null() {
        assertNull(roleAuthorityService.select(null,1L));
        assertNull(roleAuthorityService.select(1L,null));
    }
    
    @Test
    public void testSelectByRoleId(){
        assertNull(roleAuthorityService.selectByRoleId(null));
        
        EasyMock.expect(roleAuthorityMapper.selectByRoleId(EasyMock.anyLong())).andReturn(generateList());
        EasyMock.replay(roleAuthorityMapper);
        assertNotNull(roleAuthorityService.selectByRoleId(1L));
        EasyMock.verify(roleAuthorityMapper);
    }
    
    @Test
    public void testDeleteByRoleId(){
        assertFalse(roleAuthorityService.deleteByRoleId(null));
        
        EasyMock.expect(roleAuthorityMapper.selectByRoleId(EasyMock.anyLong())).andReturn(new ArrayList());
        EasyMock.replay(roleAuthorityMapper);
        assertTrue(roleAuthorityService.deleteByRoleId(1L));
    }

    @Test
    public void testDeleteByRoleId2(){       
        EasyMock.expect(roleAuthorityMapper.selectByRoleId(EasyMock.anyLong())).andReturn(generateList());
        EasyMock.expect(roleAuthorityMapper.deleteByRoleId(EasyMock.anyLong())).andReturn(0);
        EasyMock.replay(roleAuthorityMapper);
        assertFalse(roleAuthorityService.deleteByRoleId(1L));
        EasyMock.verify(roleAuthorityMapper);
    }
    
    @Test
    public void testDeleteByRoleId3(){       
        EasyMock.expect(roleAuthorityMapper.selectByRoleId(EasyMock.anyLong())).andReturn(generateList());
        EasyMock.expect(roleAuthorityMapper.deleteByRoleId(EasyMock.anyLong())).andReturn(1);
        EasyMock.replay(roleAuthorityMapper);
        assertTrue(roleAuthorityService.deleteByRoleId(1L));
        EasyMock.verify(roleAuthorityMapper);
    }
    
    @Test
    public void testDeleteRoleAuthority1(){
        assertFalse(roleAuthorityService.deleteRoleAuthority(null,1L));
        
        assertFalse(roleAuthorityService.deleteRoleAuthority(1L,null));
        
        EasyMock.expect(roleAuthorityMapper.select(EasyMock.anyObject(Map.class))).andReturn(null);
        EasyMock.replay(roleAuthorityMapper);

        assertTrue(roleAuthorityService.deleteRoleAuthority(1L,1L));
        EasyMock.verify(roleAuthorityMapper);
    }
    
    @Test
    public void testDeleteRoleAuthority2(){
        EasyMock.expect(roleAuthorityMapper.select(EasyMock.anyObject(Map.class))).andReturn(new RoleAuthority());
        EasyMock.expect(roleAuthorityMapper.deleteRoleAuthority(EasyMock.anyObject(RoleAuthority.class))).andReturn(0);     
        EasyMock.replay(roleAuthorityMapper);
        assertFalse(roleAuthorityService.deleteRoleAuthority(1L,1L));
        EasyMock.verify(roleAuthorityMapper);
    }
    
    @Test
    public void testDeleteRoleAuthority3(){
        EasyMock.expect(roleAuthorityMapper.select(EasyMock.anyObject(Map.class))).andReturn(new RoleAuthority());
        EasyMock.expect(roleAuthorityMapper.deleteRoleAuthority(EasyMock.anyObject(RoleAuthority.class))).andReturn(1);     
        EasyMock.replay(roleAuthorityMapper);
        assertTrue(roleAuthorityService.deleteRoleAuthority(1L,1L));
        EasyMock.verify(roleAuthorityMapper);
    }
    
    @Test
    public void testSelectAuthsByRoleId(){
        assertNull(roleAuthorityService.selectAuthsByRoleId(null));
        
        EasyMock.expect(roleAuthorityMapper.selectAuthsByRoleIds(1L)).andReturn(new ArrayList<Long>());    
        EasyMock.replay(roleAuthorityMapper);
        assertNotNull(roleAuthorityService.selectAuthsByRoleId(1L));
        EasyMock.verify(roleAuthorityMapper);
    }
    
    @Test
    public void testSelectExistingRoleAuthorityByAuthorityName(){
        assertNull(roleAuthorityService.selectAuthsByRoleId(null));
        
        EasyMock.expect(roleAuthorityMapper.selectExistingRoleAuthorityByAuthorityName(EasyMock.anyString())).andReturn(new ArrayList<RoleAuthority>());    
        EasyMock.replay(roleAuthorityMapper);
        assertNotNull(roleAuthorityService.selectExistingRoleAuthorityByAuthorityName("11"));
        EasyMock.verify(roleAuthorityMapper);
    }

    /**
     * @Title:generateRoleAuthority
     * @Description: 生成一个可用于角色权限
     * @author: qihang
     */
    public RoleAuthority generateRoleAuthority() {
        RoleAuthority roleAuthority = new RoleAuthority();
        roleAuthority.setAuthorityId(1L);
        roleAuthority.setRoleId(1L);
        return roleAuthority;
    }
    
    

    /**
     * @Title:generateList
     * @Description: 生成一个可用于角色权限列表
     * @author: qihang
     */
    public List<RoleAuthority> generateList() {
        RoleAuthority roleAuthority = generateRoleAuthority();
        List<RoleAuthority> list = new ArrayList<RoleAuthority>();
        list.add(roleAuthority);
        return list;
    }

}
