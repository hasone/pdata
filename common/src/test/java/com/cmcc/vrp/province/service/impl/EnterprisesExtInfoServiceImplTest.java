package com.cmcc.vrp.province.service.impl;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.enums.CrowdFundingJoinType;
import com.cmcc.vrp.province.dao.EnterprisesExtInfoMapper;
import com.cmcc.vrp.province.model.EnterprisesExtInfo;
import com.cmcc.vrp.province.service.EnterprisesExtInfoService;
import com.cmcc.vrp.util.Constants;

/**
 * 企业扩展信息的UT， 使用mockito方式
 * <p>
 * Created by sunyiwei on 2016/11/23.
 */
@RunWith(MockitoJUnitRunner.class)
public class EnterprisesExtInfoServiceImplTest {
    @InjectMocks
    EnterprisesExtInfoService enterprisesExtInfoService = new EnterprisesExtInfoServiceImpl();

    @Mock
    EnterprisesExtInfoMapper enterprisesExtInfoMapper;

    /**
     * 根据企业ID获取企业扩展信息
     *
     * @throws Exception
     */
    @Test
    public void testGet() throws Exception {
        assertNull(enterprisesExtInfoService.get(null));

        when(enterprisesExtInfoMapper.get(anyLong()))
            .thenReturn(null).thenReturn(new EnterprisesExtInfo());

        assertNull(enterprisesExtInfoService.get(343L));
        assertNotNull(enterprisesExtInfoService.get(343L));

        verify(enterprisesExtInfoMapper, times(2)).get(anyLong());
    }

    /**
     * 根据企业ID删除企业扩展信息
     *
     * @throws Exception
     */
    @Test
    public void testDelete() throws Exception {
        assertFalse(enterprisesExtInfoService.delete(null));

        when(enterprisesExtInfoMapper.delete(anyLong()))
            .thenReturn(0).thenReturn(1);

        assertFalse(enterprisesExtInfoService.delete(343L));
        assertTrue(enterprisesExtInfoService.delete(343L));

        verify(enterprisesExtInfoMapper, times(2)).delete(anyLong());
    }

    /**
     * 测试插入新的企业扩展信息
     *
     * @throws Exception
     */
    @Test
    public void testInsert() throws Exception {
        assertFalse(enterprisesExtInfoService.insert(null));
        assertFalse(enterprisesExtInfoService.insert(nullEnterId()));
        assertFalse(enterprisesExtInfoService.update(nullDeleteFlag()));

        when(enterprisesExtInfoMapper.insertSelective(any(EnterprisesExtInfo.class)))
            .thenReturn(0).thenReturn(1);

        assertFalse(enterprisesExtInfoService.insert(validEei()));
        assertTrue(enterprisesExtInfoService.insert(validEei()));

        verify(enterprisesExtInfoMapper, times(2))
            .insertSelective(any(EnterprisesExtInfo.class));
    }

    /**
     * 更新企业扩展信息UT
     *
     * @throws Exception
     */
    @Test
    public void testUpdate() throws Exception {
        assertFalse(enterprisesExtInfoService.update(null));
        assertFalse(enterprisesExtInfoService.update(nullEnterId()));
        assertFalse(enterprisesExtInfoService.update(nullDeleteFlag()));
        
        Mockito.when(enterprisesExtInfoMapper.updateByEntId(Mockito.any(EnterprisesExtInfo.class))).thenReturn(-1);
        assertFalse(enterprisesExtInfoService.update(validEei()));
        
        Mockito.when(enterprisesExtInfoMapper.updateByEntId(Mockito.any(EnterprisesExtInfo.class))).thenReturn(1);
        Mockito.when(enterprisesExtInfoMapper.get(Mockito.anyLong())).thenReturn(validEei());
        Mockito.when(enterprisesExtInfoMapper.setCallbackUrlNullByEntId(Mockito.anyLong())).thenReturn(-1);
        try{
            enterprisesExtInfoService.update(validEei());
        }catch(Exception e){
            
        }
        
        Mockito.when(enterprisesExtInfoMapper.setCallbackUrlNullByEntId(Mockito.anyLong())).thenReturn(1);
        assertTrue(enterprisesExtInfoService.update(validEei()));        
    }
    
    @Test
    public void testSelectByEcCodeAndEcPrdCode(){
        Mockito.when(enterprisesExtInfoMapper.selectByEcCodeAndEcPrdCode(Mockito.anyString(), Mockito.anyString())).thenReturn(new ArrayList());
        assertNotNull(enterprisesExtInfoService.selectByEcCodeAndEcPrdCode("test", "test"));
    }

    private EnterprisesExtInfo validEei() {
        EnterprisesExtInfo eei = new EnterprisesExtInfo();
        eei.setEnterId(433L);
        eei.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
        eei.setJoinType(CrowdFundingJoinType.Small_Enterprise.getCode());
        eei.setCallbackUrl("callbackUrl");
        return eei;
    }

    private EnterprisesExtInfo nullEnterId() {
        EnterprisesExtInfo eei = validEei();
        eei.setEnterId(null);

        return eei;
    }

    private EnterprisesExtInfo nullDeleteFlag() {
        EnterprisesExtInfo eei = validEei();
        eei.setDeleteFlag(null);

        return eei;
    }
}