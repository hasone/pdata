package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.AdministerMapper;
import com.cmcc.vrp.province.dao.MdrcCardmakerMapper;
import com.cmcc.vrp.province.mdrc.service.impl.MdrcCardmakerServiceImpl;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.MdrcCardmaker;
import com.cmcc.vrp.util.QueryObject;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.LinkedList;
import java.util.List;

import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertSame;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class MdrcCardmakerServiceImplTest {

    @InjectMocks
    MdrcCardmakerServiceImpl mdrcCardmakerService = new MdrcCardmakerServiceImpl();
    @Mock
    MdrcCardmakerMapper mdrcCardmakerMapper;
    @Mock
    AdministerMapper administerMapper;

    @Test
    public void testSelectAllCardmaker() {
        Mockito.when(mdrcCardmakerMapper.selectAllCardmaker()).thenReturn(initMdrcCardmakerList());
        assertSame(initMdrcCardmakerList().size(), mdrcCardmakerService.selectAllCardmaker().size());
        Mockito.verify(mdrcCardmakerMapper).selectAllCardmaker();
    }

    @Test
    public void testSelectByPrimaryKey() {
        Mockito.when(mdrcCardmakerMapper.selectByPrimaryKey(Mockito.anyLong())).thenReturn(initMdrcCardmaker());
        assertSame(initMdrcCardmaker().getId(), mdrcCardmakerService.selectByPrimaryKey(initMdrcCardmaker().getId()).getId());
        Mockito.verify(mdrcCardmakerMapper).selectByPrimaryKey(Mockito.anyLong());
    }

    /**
     * 新建制卡商信息
     */
    @Test
    public void testInsert() {
        Mockito.when(mdrcCardmakerMapper.insert(Mockito.any(MdrcCardmaker.class))).thenReturn(1);
        assertTrue(mdrcCardmakerService.insert(initMdrcCardmaker()));
        Mockito.verify(mdrcCardmakerMapper).insert(Mockito.any(MdrcCardmaker.class));
    }

    /**
     * 查询符合条件的记录数
     */
    @Test
    public void testCount() {
        assertSame(0, mdrcCardmakerService.count(null));

        Mockito.when(mdrcCardmakerMapper.count(Mockito.anyMap())).thenReturn(1);
        assertSame(1, mdrcCardmakerService.count(new QueryObject()));
        Mockito.verify(mdrcCardmakerMapper).count(Mockito.anyMap());
    }

    /**
     * 查询符合条件的记录
     */
    @Test
    public void testList() {
        assertNull(mdrcCardmakerService.list(null));
        Mockito.when(mdrcCardmakerMapper.select(Mockito.anyMap())).thenReturn(initMdrcCardmakerList());
        assertSame(initMdrcCardmakerList().size(), mdrcCardmakerService.list(new QueryObject()).size());
        Mockito.verify(mdrcCardmakerMapper).select(Mockito.anyMap());
    }

    /**
     * 更新制卡商信息
     * <p>
     *
     * @return
     */
    @Test
    public void testUpdate() {
        Mockito.when(mdrcCardmakerMapper.updateByPrimaryKeySelective(Mockito.any(MdrcCardmaker.class))).thenReturn(1);
        assertTrue(mdrcCardmakerService.update(initMdrcCardmaker()));
        Mockito.verify(mdrcCardmakerMapper).updateByPrimaryKeySelective(Mockito.any(MdrcCardmaker.class));
    }

    /**
     * 获取新的序列号
     * <p>
     *
     * @return
     */
    @Test
    public void testGetNewSerialNumber() {
        Mockito.when(mdrcCardmakerMapper.selectMaxSerialNumber()).thenReturn(1);
        assertSame(2, mdrcCardmakerService.getNewSerialNumber());
        Mockito.verify(mdrcCardmakerMapper).selectMaxSerialNumber();
    }

    @Test
    public void testGetNewSerialNumber2() {
        Mockito.when(mdrcCardmakerMapper.selectMaxSerialNumber()).thenReturn(null);
        assertSame(1, mdrcCardmakerService.getNewSerialNumber());
        Mockito.verify(mdrcCardmakerMapper).selectMaxSerialNumber();
    }

    /**
     * 按名称插叙已存在记录数
     */
    @Test
    public void testCountByName() {
        String name = "测试";
        Mockito.when(mdrcCardmakerMapper.countByName(Mockito.anyString())).thenReturn(2);
        assertSame(2, mdrcCardmakerService.countByName(name));
        Mockito.verify(mdrcCardmakerMapper).countByName(Mockito.anyString());
    }

    /**
     * 根据记录标识和创建者标识查询
     */
    @Test
    public void testSelectWithKeys() {
        Mockito.when(mdrcCardmakerMapper.selectWithKeys(Mockito.anyMap())).thenReturn(initMdrcCardmaker());
        assertSame(initMdrcCardmaker().getId(), mdrcCardmakerService.selectWithKeys(initMdrcCardmaker().getId(), initMdrcCardmaker().getCreatorId()).getId());
        Mockito.verify(mdrcCardmakerMapper).selectWithKeys(Mockito.anyMap());
    }

    /**
     * @Title: selectUnboundCardmaker
     */
    @Test
    public void testSelectUnboundCardmaker() {
        Mockito.when(administerMapper.selectUnboundCardmaker(Mockito.anyString())).thenReturn(initAdministerList());
        assertSame(initAdministerList().size(), mdrcCardmakerService.selectUnboundCardmaker("MDRC_DOWNLOAD_AUTH_CODE").size());
        Mockito.verify(administerMapper).selectUnboundCardmaker(Mockito.anyString());
    }

    /**
     * 删除制卡商
     */
    @Test
    public void testDeleteById() {
        Mockito.when(mdrcCardmakerMapper.deleteByPrimaryKey(Mockito.anyLong())).thenReturn(1);
        assertTrue(mdrcCardmakerService.deleteById(initMdrcCardmaker().getId()));
        Mockito.verify(mdrcCardmakerMapper).deleteByPrimaryKey(Mockito.anyLong());
    }

    /**
     * 唯一性校验
     */
    @Test
    @Ignore
    public void testCheckUnique() {
        assertFalse(mdrcCardmakerService.checkUnique(null));
        Mockito.when(mdrcCardmakerMapper.checkUnique(Mockito.any(MdrcCardmaker.class))).thenReturn(initMdrcCardmakerList());
        assertFalse(mdrcCardmakerService.checkUnique(initMdrcCardmaker()));
        Mockito.verify(mdrcCardmakerMapper).checkUnique(Mockito.any(MdrcCardmaker.class));
    }

    @Test
    public void testCheckUniqueReturnTrue() {
        Mockito.when(mdrcCardmakerMapper.checkUnique(Mockito.any(MdrcCardmaker.class))).thenReturn(new LinkedList<MdrcCardmaker>());
        assertTrue(mdrcCardmakerService.checkUnique(initMdrcCardmaker()));
        Mockito.verify(mdrcCardmakerMapper).checkUnique(Mockito.any(MdrcCardmaker.class));
    }

    private List<MdrcCardmaker> initMdrcCardmakerList() {
        List<MdrcCardmaker> list = new LinkedList<MdrcCardmaker>();
        MdrcCardmaker record = new MdrcCardmaker();
        record.setId(1L);
        list.add(record);
        return list;
    }

    private MdrcCardmaker initMdrcCardmaker() {
        MdrcCardmaker record = new MdrcCardmaker();
        record.setId(1L);
        record.setCreatorId(1L);
        return record;
    }

    private List<Administer> initAdministerList() {
        List<Administer> list = new LinkedList<Administer>();
        Administer record = new Administer();
        record.setId(1L);
        list.add(record);
        return list;
    }
}
