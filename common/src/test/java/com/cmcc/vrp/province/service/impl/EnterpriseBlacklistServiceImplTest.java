/**
 * 
 */
package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.EnterpriseBlacklistMapper;
import com.cmcc.vrp.province.model.EnterpriseBlacklist;
import com.cmcc.vrp.util.QueryObject;

/**
 * @desc:
 * @author: wuguoping
 * @data: 2017年7月21日
 */
@RunWith(MockitoJUnitRunner.class)
public class EnterpriseBlacklistServiceImplTest {
    @InjectMocks
    EnterpriseBlacklistServiceImpl enterpriseBlacklistServiceImpl = new EnterpriseBlacklistServiceImpl();
    @Mock
    EnterpriseBlacklistMapper enterpriseBlacklistMapper;

    /**
     * 
     * title: isContainKeywordTest desc: wuguoping 2017年7月21日
     */
    @Test
    public void isContainKeywordTest() {
        List<EnterpriseBlacklist> enterpriseBlacklists = new ArrayList<EnterpriseBlacklist>();
        EnterpriseBlacklist enterpriseBlacklist = new EnterpriseBlacklist();
        String entName = "hah";
        String keyword = "yidong";

        when(enterpriseBlacklistMapper.selectByEntNameKeyWord(any(String.class), any(String.class))).thenReturn(enterpriseBlacklists);
        assertFalse(enterpriseBlacklistServiceImpl.isContainKeyword(entName, keyword));

        enterpriseBlacklists.add(enterpriseBlacklist);
        when(enterpriseBlacklistMapper.selectByEntNameKeyWord(any(String.class), any(String.class))).thenReturn(enterpriseBlacklists);
        assertTrue(enterpriseBlacklistServiceImpl.isContainKeyword(entName, keyword));
    }

    /**
     * 
     * title: selectEntBlacklistByIdTest desc: wuguoping 2017年7月21日
     */
    @Test
    public void selectEntBlacklistByIdTest() {
        assertNull(enterpriseBlacklistServiceImpl.selectEntBlacklistById(null));
        assertNull(enterpriseBlacklistServiceImpl.selectEntBlacklistById(-1));
        when(enterpriseBlacklistMapper.selectById(any(Integer.class))).thenReturn(null);
        assertNull(enterpriseBlacklistServiceImpl.selectEntBlacklistById(1));
    }

    /**
     * 
     * title: insertTest desc: wuguoping 2017年7月21日
     */
    @Test
    public void insertTest() {
        assertFalse(enterpriseBlacklistServiceImpl.insert(null));

        EnterpriseBlacklist enterpriseBlacklist = new EnterpriseBlacklist();
        assertFalse(enterpriseBlacklistServiceImpl.insert(enterpriseBlacklist));

        enterpriseBlacklist.setEnterpriseName("xa");
        assertFalse(enterpriseBlacklistServiceImpl.insert(enterpriseBlacklist));

        List<EnterpriseBlacklist> enterpriseBlacklists = new ArrayList<EnterpriseBlacklist>();
        enterpriseBlacklists.add(enterpriseBlacklist);
        when(enterpriseBlacklistMapper.selectByEntName(any(String.class))).thenReturn(enterpriseBlacklists);
        assertFalse(enterpriseBlacklistServiceImpl.insert(enterpriseBlacklist));

        enterpriseBlacklist.setId(1l);
        when(enterpriseBlacklistMapper.insert(enterpriseBlacklist)).thenReturn(1);
        assertTrue(enterpriseBlacklistServiceImpl.insert(enterpriseBlacklist));

    }

    /**
     * 
     * title: getEntBlacklistForPageTest desc: wuguoping 2017年7月21日
     */
    @Test
    public void getEntBlacklistForPageTest() {
        assertNull(enterpriseBlacklistServiceImpl.getBlacklistEntNameList(null));

        when(enterpriseBlacklistMapper.getAllKeywordsList()).thenReturn(null);
        assertNull(enterpriseBlacklistServiceImpl.getBlacklistEntNameList("dhaha"));

        List<String> list = new ArrayList<String>();
        list.add("vv");
        Map<String, Object> map = new HashMap<String, Object>();
        QueryObject queryObject = new QueryObject();
        map.put("ss", queryObject);
        
        Set<String>  keywordsList = new HashSet<String>();
        keywordsList.add("test");
        when(enterpriseBlacklistMapper.getAllKeywordsList()).thenReturn(keywordsList);
        when(enterpriseBlacklistMapper.selectByKeywordList(map)).thenReturn(null);
        assertNull(enterpriseBlacklistServiceImpl.getBlacklistEntNameList("aa"));
        
        assertNotNull(enterpriseBlacklistServiceImpl.getBlacklistEntNameList("test11"));
    }

    /**
     * 
     * title: indistinctCheckByEntNameTest desc: wuguoping 2017年7月21日
     */
    @Test
    public void indistinctCheckByEntNameTest() {
        assertNull(enterpriseBlacklistServiceImpl.indistinctCheckByEntName(null));
        when(enterpriseBlacklistMapper.selectByEntName(any(String.class))).thenReturn(null);
        assertNull(enterpriseBlacklistServiceImpl.indistinctCheckByEntName("hahha"));
    }
    
    @Test
    public void testGetEntBlacklistForPage() {
        assertNull(enterpriseBlacklistServiceImpl.getEntBlacklistForPage(null));
        when(enterpriseBlacklistMapper.showEntBlacklistPagResult(Mockito.anyMap())).thenReturn(new ArrayList());
        assertNotNull(enterpriseBlacklistServiceImpl.getEntBlacklistForPage(new QueryObject()));
    }
    
    @Test
    public void testGetEntBlacklistCount() {
        assertNull(enterpriseBlacklistServiceImpl.getEntBlacklistCount(null));
        when(enterpriseBlacklistMapper.showEntBlacklistPagResult(Mockito.anyMap())).thenReturn(new ArrayList());
        assertNotNull(enterpriseBlacklistServiceImpl.getEntBlacklistCount(new QueryObject()));
    }
    
    @Test
    public void testUpdate() {   
        when(enterpriseBlacklistMapper.updateById(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenReturn(1);
        assertTrue(enterpriseBlacklistServiceImpl.update(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyLong()));
    }
    
    @Test
    public void testDelete() {   
        when(enterpriseBlacklistMapper.deleteById(Mockito.anyLong())).thenReturn(1);
        assertTrue(enterpriseBlacklistServiceImpl.delete(Mockito.anyLong()));
    }

}
