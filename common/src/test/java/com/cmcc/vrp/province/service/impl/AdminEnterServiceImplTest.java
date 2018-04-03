package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.cmcc.vrp.province.dao.AdminEnterMapper;
import com.cmcc.vrp.province.model.AdminEnter;
import com.cmcc.vrp.province.service.AdminEnterService;


/**
 * @ClassName: TestRoleServiceImpl
 * @Description: 角色管理单元测试类
 * @author: qihang
 * @date: 2015年3月11日 下午4:52:09
 */
@RunWith(EasyMockRunner.class)
public class AdminEnterServiceImplTest {
    @TestSubject
    private final AdminEnterService service = new AdminEnterServiceImpl();

    @Mock
    private AdminEnterMapper adminEnterMapper;


    @Test
    public void testInsert0() {
        assertFalse(service.insert(null));       
        assertFalse(service.insert(new AdminEnter()));
        
        AdminEnter adminEnter = new AdminEnter();
        adminEnter.setAdminId(1L);        
        assertFalse(service.insert(adminEnter));

        adminEnter.setEnterId(2L);

        //record
        //设定role
        EasyMock.expect(adminEnterMapper.selectCountByQuery(adminEnter)).andReturn(0);
        EasyMock.expect(adminEnterMapper.insert(adminEnter)).andReturn(1);

        //replay
        EasyMock.replay(adminEnterMapper);
        //
        assertTrue(service.insert(adminEnter));

        //verify
        EasyMock.verify(adminEnterMapper);
    }
    
    @Test
    public void testInsert1() {
        AdminEnter adminEnter = new AdminEnter();
        adminEnter.setAdminId(1L);
        adminEnter.setEnterId(2L);

        //record
        //设定role
        EasyMock.expect(adminEnterMapper.selectCountByQuery(adminEnter)).andReturn(0);
        EasyMock.expect(adminEnterMapper.insert(adminEnter)).andReturn(0);

        //replay
        EasyMock.replay(adminEnterMapper);
        //
        assertFalse(service.insert(adminEnter));

        //verify
        EasyMock.verify(adminEnterMapper);
    }
    
    @Test
    public void testInsert2() {
        AdminEnter adminEnter = new AdminEnter();
        adminEnter.setAdminId(1L);
        adminEnter.setEnterId(2L);

        //record
        //设定role
        EasyMock.expect(adminEnterMapper.selectCountByQuery(adminEnter)).andReturn(1);
        //replay
        EasyMock.replay(adminEnterMapper);
        //
        assertTrue(service.insert(adminEnter));

        //verify
        EasyMock.verify(adminEnterMapper);
    }
    
    @Test
    public void testInsert3() {
        AdminEnter adminEnter = new AdminEnter();
        adminEnter.setAdminId(1L);
        adminEnter.setEnterId(2L);

        //record
        //设定role
        EasyMock.expect(adminEnterMapper.selectCountByQuery(EasyMock.anyObject(AdminEnter.class))).andReturn(0);
        EasyMock.expect(adminEnterMapper.insert(EasyMock.anyObject(AdminEnter.class))).andReturn(1);
        
        //replay
        EasyMock.replay(adminEnterMapper);
        //
        assertTrue(service.insert(adminEnter.getAdminId(), adminEnter.getEnterId()));

        //verify
        EasyMock.verify(adminEnterMapper);
    }

    @Test
    public void testSelectCountByQuery() {
        assertEquals(service.selectCountByQuery(null), 0);
        
        AdminEnter adminEnter = new AdminEnter();
        adminEnter.setAdminId(1L);
        adminEnter.setEnterId(2L);

        EasyMock.expect(adminEnterMapper.selectCountByQuery(adminEnter)).andReturn(1);

        //replay
        EasyMock.replay(adminEnterMapper);

        assertEquals(service.selectCountByQuery(adminEnter), 1);

        //verify
        EasyMock.verify(adminEnterMapper);
    }


    @Test
    public void testDeleteByAdminId1() {
        assertFalse(service.deleteByAdminId(null));       
        
        Long adminId = 1L;
        //record
        //设定role，数据库存在该数据,
        EasyMock.expect(adminEnterMapper.deleteByAdminId(adminId)).andReturn(1);

        List<String> list = new ArrayList<String>();
        list.add("1");
        EasyMock.expect(adminEnterMapper.selectByAdminId(adminId)).andReturn(list);

        //replay
        EasyMock.replay(adminEnterMapper);

        //
        assertTrue(service.deleteByAdminId(adminId));

        //verify
        EasyMock.verify(adminEnterMapper);
    }
    
    @Test
    public void testDeleteByAdminId2() {
        Long adminId = 1L;
        //record
        //设定role，数据库存在该数据,
        EasyMock.expect(adminEnterMapper.deleteByAdminId(adminId)).andReturn(0);

        List<String> list = new ArrayList<String>();
        list.add("1");
        EasyMock.expect(adminEnterMapper.selectByAdminId(adminId)).andReturn(list);

        //replay
        EasyMock.replay(adminEnterMapper);

        //
        assertFalse(service.deleteByAdminId(adminId));

        //verify
        EasyMock.verify(adminEnterMapper);
    }
    
    @Test
    public void testDeleteByAdminId3() {
        Long adminId = 1L;
        EasyMock.expect(adminEnterMapper.selectByAdminId(adminId)).andReturn(null);
        EasyMock.replay(adminEnterMapper);
        assertTrue(service.deleteByAdminId(adminId));
        EasyMock.verify(adminEnterMapper);
    }

    @Test
    public void testDeleteByEnterId0() {
        assertFalse(service.deleteByEnterId(null));
        
        Long enterId = 1L;
        //record
        //设定role，数据库存在该数据,
        EasyMock.expect(adminEnterMapper.deleteByEnterId(enterId)).andReturn(1);

        //replay
        EasyMock.replay(adminEnterMapper);

        assertTrue(service.deleteByEnterId(enterId));

        //verify
        EasyMock.verify(adminEnterMapper);
    }
    
    @Test
    public void testDeleteByEnterId1() {
        Long enterId = 1L;
        //record
        //设定role，数据库存在该数据,
        EasyMock.expect(adminEnterMapper.deleteByEnterId(enterId)).andReturn(0);

        //replay
        EasyMock.replay(adminEnterMapper);

        assertFalse(service.deleteByEnterId(enterId));

        //verify
        EasyMock.verify(adminEnterMapper);
    }
    

    @Test
    public void testDeleteAdminRole0() {
        assertFalse(service.deleteAdminRole(null));
        AdminEnter adminEnter = new AdminEnter();
        assertFalse(service.deleteAdminRole(adminEnter));
        adminEnter.setAdminId(1L);
        assertFalse(service.deleteAdminRole(adminEnter));
        adminEnter.setEnterId(2L);
        //record
        //设定role，数据库存在该数据,
        EasyMock.expect(adminEnterMapper.deleteAdminEnter(adminEnter)).andReturn(1);

        //replay
        EasyMock.replay(adminEnterMapper);

        assertTrue(service.deleteAdminRole(adminEnter));

        //verify
        EasyMock.verify(adminEnterMapper);
    }
    
    @Test
    public void testDeleteAdminRole1() {
        AdminEnter adminEnter = new AdminEnter();
        adminEnter.setAdminId(1L);
        adminEnter.setEnterId(2L);
        //record
        //设定role，数据库存在该数据,
        EasyMock.expect(adminEnterMapper.deleteAdminEnter(adminEnter)).andReturn(0);

        EasyMock.replay(adminEnterMapper);

        assertFalse(service.deleteAdminRole(adminEnter));

        EasyMock.verify(adminEnterMapper);
    }
    
    @Test
    public void testSelectCountByAdminIdEntId() {
        assertEquals(service.selectCountByAdminIdEntId(null,1L), 0);        
        assertEquals(service.selectCountByAdminIdEntId(1L,null), 0);

        //设定role，数据库存在该数据,
        EasyMock.expect(adminEnterMapper.selectCountByQuery(EasyMock.anyObject(AdminEnter.class))).andReturn(1);

        //replay
        EasyMock.replay(adminEnterMapper);

        assertEquals(service.selectCountByAdminIdEntId(1L,1L), 1);

        //verify
        EasyMock.verify(adminEnterMapper);
    }
    
    @Test
    public void testSelecteByAdminId() {
        //设定role，数据库存在该数据,
        EasyMock.expect(adminEnterMapper.selectByAdminId(1L)).andReturn(new ArrayList());

        //replay
        EasyMock.replay(adminEnterMapper);

        assertNotNull(service.selectByAdminId(1L));

        //verify
        EasyMock.verify(adminEnterMapper);
    }    
    
    @Test
    public void testSelecteEntIdByAdminId() {
        assertNull(service.selecteEntIdByAdminId(null));        
      
        //设定role，数据库存在该数据,
        EasyMock.expect(adminEnterMapper.selecteEntIdByAdminId(1L)).andReturn(new ArrayList());

        //replay
        EasyMock.replay(adminEnterMapper);

        assertNotNull(service.selecteEntIdByAdminId(1L));

        //verify
        EasyMock.verify(adminEnterMapper);
    }
    
    @Test
    public void testSelecteEntByAdminId() {
        assertNull(service.selecteEntByAdminId(null));        
      
        //设定role，数据库存在该数据,
        EasyMock.expect(adminEnterMapper.selecteEntByAdminId(1L)).andReturn(new ArrayList());

        //replay
        EasyMock.replay(adminEnterMapper);

        assertNotNull(service.selecteEntByAdminId(1L));

        //verify
        EasyMock.verify(adminEnterMapper);
    }
}
