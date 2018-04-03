package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.UserInfoMapper;
import com.cmcc.vrp.province.model.UserInfo;
import com.cmcc.vrp.province.service.UserInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @ClassName: UserInfoServiceImplTest
 * @Description: 转盘用户对象的服务
 * @author: qinqinyan
 * @date: 2016年5月26日
 */
@RunWith(MockitoJUnitRunner.class)
public class UserInfoServiceImplTest {

    @InjectMocks
    UserInfoService userInfoService = new UserInfoServiceImpl();

    @Mock
    UserInfoMapper userInfoMapper;

    /*
     * 由用户ID获取对象
     */
    @Test
    public void testGetById() {
        //invalid
        UserInfo invalidRecord = invalidUserInfo();
        assertNull(userInfoService.get(invalidRecord.getId()));

        //valid
        UserInfo record = validUserInfo();
        Mockito.when(userInfoMapper.get(record.getId())).thenReturn(record);

        assertNotNull(userInfoService.get(record.getId()));
        Mockito.verify(userInfoMapper).get(record.getId());
    }

    /*
     * 由用户手机号获取对象
     */
    @Test
    public void testGetByMobile() {
        //invalid
        assertNull(userInfoService.get(invalidUserInfo().getMobile()));
        assertNull(userInfoService.get(nullMobile().getMobile()));

        //valid
        UserInfo record = validUserInfo();
        Mockito.when(userInfoMapper.getByMobile(record.getMobile())).thenReturn(record);

        assertNotNull(userInfoService.get(record.getMobile()));
        Mockito.verify(userInfoMapper).getByMobile(record.getMobile());
    }

    /*
     * 插入用户
     */
    @Test
    public void testInsert1() {
        //invalid
        assertFalse(userInfoService.insert(invalidUserInfo()));
        assertFalse(userInfoService.insert(nullMobile()));
        assertFalse(userInfoService.insert(nullCreateTime()));
        assertFalse(userInfoService.insert(nullDeleteFlag()));

        //valid
        UserInfo record = validUserInfo();
        Mockito.when(userInfoMapper.getByMobile(record.getMobile())).thenReturn(null);
        Mockito.when(userInfoMapper.insert(record)).thenReturn(1);

        assertTrue(userInfoService.insert(record));
        Mockito.verify(userInfoMapper).getByMobile(record.getMobile());
        Mockito.verify(userInfoMapper).insert(record);
    }
    
    @Test
    public void testInsert2() {
        UserInfo record = validUserInfo();
        Mockito.when(userInfoMapper.getByMobile(record.getMobile())).thenReturn(new UserInfo());
   
        assertFalse(userInfoService.insert(record));
        Mockito.verify(userInfoMapper).getByMobile(record.getMobile());
    }
    
    @Test
    public void testInsert3() {
        UserInfo record = validUserInfo();
        Mockito.when(userInfoMapper.getByMobile(record.getMobile())).thenReturn(null);
        Mockito.when(userInfoMapper.insert(record)).thenReturn(0);

        assertFalse(userInfoService.insert(record));
        Mockito.verify(userInfoMapper).getByMobile(record.getMobile());
        Mockito.verify(userInfoMapper).insert(record);
    }



    /*
     * 根据ID删除用户
     */
    @Test
    public void testDeleteById() {
        //invalid
        assertFalse(userInfoService.delete(invalidUserInfo().getId()));
        //valid
        UserInfo record = validUserInfo();
        Mockito.when(userInfoMapper.delete(record.getId())).thenReturn(1);

        assertTrue(userInfoService.delete(record.getId()));
        
        Mockito.when(userInfoMapper.delete(record.getId())).thenReturn(0);
        assertFalse(userInfoService.delete(record.getId()));
        
        Mockito.verify(userInfoMapper,Mockito.times(2)).delete(record.getId());
    }

    /*
     * 由用户手机号删除用户
     */
    @Test
    public void testDeleteByMobile() {
        //invalid
        assertFalse(userInfoService.delete(invalidUserInfo().getMobile()));
        assertFalse(userInfoService.delete(nullMobile().getMobile()));
        //valid
        UserInfo record = validUserInfo();
        Mockito.when(userInfoMapper.deleteByMobile(record.getMobile())).thenReturn(1);
        assertTrue(userInfoService.delete(record.getMobile()));
        
        Mockito.when(userInfoMapper.deleteByMobile(record.getMobile())).thenReturn(0);
        assertFalse(userInfoService.delete(record.getMobile()));
     
        Mockito.verify(userInfoMapper,Mockito.times(2)).deleteByMobile(record.getMobile());
    }

    private UserInfo invalidUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(null);
        userInfo.setMobile("1886564");
        return userInfo;
    }

    private UserInfo nullMobile() {
        UserInfo userInfo = new UserInfo();
        userInfo.setMobile(null);
        return userInfo;
    }

    private UserInfo nullCreateTime() {
        UserInfo userInfo = new UserInfo();
        userInfo.setMobile("18867102222");
        userInfo.setCreateTime(null);
        return userInfo;
    }

    private UserInfo nullDeleteFlag() {
        UserInfo userInfo = new UserInfo();
        userInfo.setMobile("18867102222");
        userInfo.setCreateTime(new Date());
        userInfo.setDeleteFlag(null);
        return userInfo;
    }

    private UserInfo validUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        userInfo.setMobile("18867102222");
        userInfo.setCreateTime(new Date());
        userInfo.setUpdateTime(new Date());
        userInfo.setDeleteFlag(0);
        return userInfo;
    }


}
