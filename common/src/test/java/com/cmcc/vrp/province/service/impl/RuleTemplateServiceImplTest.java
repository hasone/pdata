package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.RuleTemplateMapper;
import com.cmcc.vrp.province.model.RuleTemplate;
import com.cmcc.vrp.province.model.json.RedPacketPage;
import com.cmcc.vrp.province.service.AdminRoleService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.RuleTemplateService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;


import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.rpc.ServiceException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * @author qinqinyan
 * @Description 规则模板服务类
 * @date 2016年5月26日
 */

/**
 * @author wujiamin
 * @date 2016年10月17日下午4:42:06
 */
/**
 * @author wujiamin
 * @date 2016年10月17日下午4:42:19
 */

@RunWith(MockitoJUnitRunner.class)
public class RuleTemplateServiceImplTest {

    @InjectMocks
    RuleTemplateService ruleTemplateService = new RuleTemplateServiceImpl();
    @Mock
    RuleTemplateMapper ruleTemplateMapper;
    @Mock
    AdminRoleService adminRoleService;
    @Mock
    GlobalConfigService globalConfigService;
    
    @Before
    public void initMocks() throws ServiceException, RemoteException {
        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.ACTIVITY_RULE_TEMPLATE_FILE_PATH.getKey())).thenReturn("xxxxxx");
    }
    
    @Test
    public void testDeleteByPrimaryKey() {
        //invalid
        assertFalse(ruleTemplateService.deleteByPrimaryKey(nullId().getId()));
        //valid
        RuleTemplate ruleTemplate = validRuleTemplate();

        Mockito.when(ruleTemplateService.selectByPrimaryKey(ruleTemplate.getId())).thenReturn(ruleTemplate);
        Mockito.when(ruleTemplateMapper.updateByPrimaryKeySelective(Mockito.any(RuleTemplate.class))).thenReturn(0).thenReturn(1);

        assertFalse(ruleTemplateService.deleteByPrimaryKey(ruleTemplate.getId()));
        assertTrue(ruleTemplateService.deleteByPrimaryKey(ruleTemplate.getId()));
        Mockito.verify(ruleTemplateMapper, Mockito.times(2)).updateByPrimaryKeySelective(Mockito.any(RuleTemplate.class));
    }

    @Test
    public void testInsert() {
        //invalid
        assertFalse(ruleTemplateService.insert(null));

        //valid
        RuleTemplate ruleTemplate = validRuleTemplate();
        Mockito.when(ruleTemplateMapper.insert(Mockito.any(RuleTemplate.class))).thenReturn(0).thenReturn(1);

        assertFalse(ruleTemplateService.insert(ruleTemplate));
        assertTrue(ruleTemplateService.insert(ruleTemplate));
        Mockito.verify(ruleTemplateMapper, Mockito.times(2)).insert(Mockito.any(RuleTemplate.class));
    }

    @Test
    public void testInsertSelective() {
        Mockito.when(ruleTemplateMapper.insertSelective(Mockito.any(RuleTemplate.class))).thenReturn(0).thenReturn(1);
        assertFalse(ruleTemplateService.insertSelective(new RuleTemplate()));
        assertTrue(ruleTemplateService.insertSelective(new RuleTemplate()));
        Mockito.verify(ruleTemplateMapper, Mockito.times(2)).insertSelective(Mockito.any(RuleTemplate.class));
    }

    @Test
    public void testSelectByPrimaryKey() {
        //invalid
        assertNull(ruleTemplateService.selectByPrimaryKey(nullId().getId()));
        //valid
        RuleTemplate record = validRuleTemplate();
        Mockito.when(ruleTemplateMapper.selectByPrimaryKey(record.getId())).thenReturn(record);
        assertNotNull(ruleTemplateService.selectByPrimaryKey(record.getId()));
        Mockito.verify(ruleTemplateMapper).selectByPrimaryKey(record.getId());
    }

    @Test
    public void testUpdateByPrimaryKeySelective() {
        //invalid
        assertFalse(ruleTemplateService.updateByPrimaryKeySelective(null));

        //valid
        Mockito.when(ruleTemplateMapper.updateByPrimaryKeySelective(Mockito.any(RuleTemplate.class))).thenReturn(0).thenReturn(1);
        assertFalse(ruleTemplateService.updateByPrimaryKeySelective(validRuleTemplate()));
        assertTrue(ruleTemplateService.updateByPrimaryKeySelective(validRuleTemplate()));

        Mockito.verify(ruleTemplateMapper, Mockito.times(2)).updateByPrimaryKeySelective(Mockito.any(RuleTemplate.class));
    }

    @Test
    public void testUpdateByPrimaryKey() {
        //invalid
        assertFalse(ruleTemplateService.updateByPrimaryKey(null));

        //valid
        Mockito.when(ruleTemplateMapper.updateByPrimaryKey(Mockito.any(RuleTemplate.class))).thenReturn(0).thenReturn(1);
        assertFalse(ruleTemplateService.updateByPrimaryKey(validRuleTemplate()));
        assertTrue(ruleTemplateService.updateByPrimaryKey(validRuleTemplate()));

        Mockito.verify(ruleTemplateMapper, Mockito.times(2)).updateByPrimaryKey(Mockito.any(RuleTemplate.class));
    }

    /**
     * 查询记录条数
     * <p>
     *
     * @param queryObject
     * @return
     */
    @Test
    public void testCount() {

        QueryObject queryObject = new QueryObject();
        Long result = getLong();
        Mockito.when(ruleTemplateMapper.count(queryObject.toMap())).thenReturn(null).thenReturn(result);
        assertSame(0L, ruleTemplateService.count(null));
        assertSame(0L, ruleTemplateService.count(queryObject));
        assertSame(result, ruleTemplateService.count(queryObject));
        Mockito.verify(ruleTemplateMapper, Mockito.times(2)).count(queryObject.toMap());
    }

    /**
     * 查询记录列表
     * <p>
     *
     * @param queryObject
     * @return
     */
    @Test
    public void testListRuleTemplate() {
        QueryObject queryObject = new QueryObject();
        List<RuleTemplate> list = new ArrayList();
        Mockito.when(ruleTemplateMapper.listRuleTemplate(queryObject.toMap())).thenReturn(list);
        assertNull(ruleTemplateService.listRuleTemplate(null));
        assertSame(list, ruleTemplateService.listRuleTemplate(queryObject));
        Mockito.verify(ruleTemplateMapper).listRuleTemplate(queryObject.toMap());
    }

    /**
     * 校验模板名称是否在该活动中重名
     */
    @Test
    public void testAddValidate() {

        assertEquals("传入参数不完整", ruleTemplateService.addValidate(null, new QueryObject()));
        assertEquals("传入参数不完整", ruleTemplateService.addValidate(nullName(), new QueryObject()));

        QueryObject queryObject = new QueryObject();
        List<RuleTemplate> recordList = new LinkedList<RuleTemplate>();
        RuleTemplate rt = new RuleTemplate();
        recordList.add(rt);
        RuleTemplate rt1= new RuleTemplate();
        rt1.setName("圣诞活动");
        recordList.add(rt1);
        
        RuleTemplate ruleTemplate = new RuleTemplate();
        ruleTemplate.setName("圣诞活动");;
        
        Mockito.when(ruleTemplateService.listRuleTemplate(queryObject)).thenReturn(recordList);
        assertEquals(ruleTemplate.getName() + "规则已经存在该名称的短信模板！",
            ruleTemplateService.addValidate(ruleTemplate, queryObject));
        
        ruleTemplate.setId(1L);
        assertEquals(ruleTemplate.getName() + "规则已经存在该名称的短信模板！",
                ruleTemplateService.addValidate(ruleTemplate, queryObject));

        //valid
        assertNull(ruleTemplateService.addValidate(thirdValidRuleTemplate(), queryObject));
    }

    /**
     * 获取资源文件列表
     * <p>
     *
     * @param id
     * @return
     */
    @Ignore
    @Test
    public void testListFiles() {
        long id = 1L;
        assertNull(ruleTemplateService.listFiles(id));
    }

    /**
     * 保存文件
     * <p>
     *
     * @param id
     * @param filename
     * @param data
     * @throws IOException
     */
    @Test
    public void testWriteFile() throws Exception {
    }

    /** 
     * @Title: testUpdateResourcesCount 
     * @Author: wujiamin
     * @date 2016年10月17日下午4:42:34
    */
    @Test
    public void testUpdateResourcesCount() {
        Integer imageCnt = 4;
        Long id = 2L;
        Mockito.when(ruleTemplateMapper.updateByPrimaryKeySelective(Mockito.any(RuleTemplate.class))).thenReturn(1);
        assertSame(1, ruleTemplateService.updateResourcesCount(id, imageCnt));
        Mockito.verify(ruleTemplateMapper).updateByPrimaryKeySelective(Mockito.any(RuleTemplate.class));
    }

    @Test
    public void testGetFile() throws IOException {
    }

    /** 
     * @Title: testUpdateFrontAndRearImage 
     * @Author: wujiamin
     * @date 2016年10月17日下午4:42:29
    */
    @Test
    public void testUpdateFrontAndRearImage() {
        String image = "测试";

        //invalid
        long invalidId = 2L;
        Mockito.when(ruleTemplateService.selectByPrimaryKey(invalidId)).thenReturn(null);
        assertEquals(0, ruleTemplateService.updateFrontAndRearImage(invalidId, image));

        //valid
        RuleTemplate ruleTemplate = validRuleTemplate();
        Mockito.when(ruleTemplateService.selectByPrimaryKey(ruleTemplate.getId())).thenReturn(ruleTemplate);
        Mockito.when(ruleTemplateMapper.updateByPrimaryKeySelective(Mockito.any(RuleTemplate.class))).thenReturn(1);
        assertEquals(1, ruleTemplateService.updateFrontAndRearImage(ruleTemplate.getId(), image));
        Mockito.verify(ruleTemplateMapper).updateByPrimaryKeySelective(Mockito.any(RuleTemplate.class));
    }

    @Test
    public void testDeleteFile() throws Exception {
        File folder = PowerMockito.mock(File.class);
        PowerMockito.whenNew(File.class).withAnyArguments().thenReturn(folder);
        PowerMockito.when(folder.exists()).thenReturn(true);
        PowerMockito.when(folder.isDirectory()).thenReturn(true);
        
        ruleTemplateService.deleteFile(1L, "test");
    }

    /** 
     * @Title: testGetRuleTemplateByCreator 
     * @Author: wujiamin
     * @date 2016年10月17日下午4:42:24
    */
    @Test
    public void testGetRuleTemplateByCreator() {
        //invalid
        assertNull(ruleTemplateService.getRuleTemplateByCreator(null));

        //valid
        Long creatorId = 1L;
        Long roleId = 1L;
        List<RuleTemplate> list = new ArrayList();
        Mockito.when(adminRoleService.getRoleIdByAdminId(creatorId)).thenReturn(roleId);
        Mockito.when(ruleTemplateMapper.getRuleTemplateByCreatorId(Mockito.anyMap())).thenReturn(list);
        assertSame(list, ruleTemplateService.getRuleTemplateByCreator(creatorId));

        roleId = 2L;
        Mockito.when(adminRoleService.getRoleIdByAdminId(creatorId)).thenReturn(roleId);
        assertSame(list, ruleTemplateService.getRuleTemplateByCreator(creatorId));
        Mockito.verify(adminRoleService, Mockito.times(2)).getRoleIdByAdminId(Mockito.anyLong());
        Mockito.verify(ruleTemplateMapper, Mockito.times(2)).getRuleTemplateByCreatorId(Mockito.anyMap());
    }

    @Test
    public void testGetTemplateToCreateRedpacket() {
        //invalid
        assertNull(ruleTemplateService.getTemplateToCreateRedpacket(null));

        //valid
        Long creatorId = 1L;
        Long roleId = 1L;
        List<RuleTemplate> list = new ArrayList();
        Mockito.when(adminRoleService.getRoleIdByAdminId(creatorId)).thenReturn(roleId);
        Mockito.when(ruleTemplateMapper.getRuleTemplateByCreatorId(Mockito.anyMap())).thenReturn(list);
        assertSame(list, ruleTemplateService.getTemplateToCreateRedpacket(creatorId));

        roleId = 2L;
        Mockito.when(adminRoleService.getRoleIdByAdminId(creatorId)).thenReturn(roleId);
        assertSame(list, ruleTemplateService.getTemplateToCreateRedpacket(creatorId));
        Mockito.verify(adminRoleService, Mockito.times(2)).getRoleIdByAdminId(Mockito.anyLong());
        Mockito.verify(ruleTemplateMapper, Mockito.times(2)).getRuleTemplateByCreatorId(Mockito.anyMap());
    }

    /**
     * 从红包生成页面中获取规则模板对象
     *
     * @param pageParams
     * @return
     */
    @Test
    public void testGetRedPacketRuleTempalteFromPage() {
	RedPacketPage pageParams = new RedPacketPage();
	pageParams.setActivityName("123");
	pageParams.setActivityDes("1234567");
	pageParams.setDescription("987654");
	pageParams.setPeople("people");
	RuleTemplate ruleTemplate = new RuleTemplate();
	ruleTemplate.setName(pageParams.getActivityName());
	ruleTemplate.setTitle(pageParams.getActivityName());
	ruleTemplate.setActivityDes(pageParams.getActivityDes());
	ruleTemplate.setDescription(pageParams.getDescription());
	ruleTemplate.setPeople(pageParams.getPeople());
	assertNotNull(ruleTemplateService.getRedPacketRuleTempalteFromPage(pageParams));
    }

    private RuleTemplate nullId() {
        RuleTemplate record = new RuleTemplate();
        record.setId(null);
        return record;
    }

    private RuleTemplate validRuleTemplate() {
        RuleTemplate record = new RuleTemplate();
        record.setId(1L);
        record.setName("圣诞活动");
        record.setDeleteFlag(0);
        return record;
    }

    private RuleTemplate thirdValidRuleTemplate() {
        RuleTemplate record = new RuleTemplate();
        record.setId(1L);
        record.setName("元旦活动");
        return record;
    }

    private Long getLong() {
        Long count = (long) Math.round(11);
        return count;
    }

    private RuleTemplate nullName() {
        RuleTemplate record = new RuleTemplate();
        record.setId(1L);
        record.setName(null);
        return record;
    }

}
