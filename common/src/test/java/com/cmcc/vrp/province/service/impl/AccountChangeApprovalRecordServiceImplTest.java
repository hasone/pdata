/**
 *
 */
package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.AccountChangeApprovalRecordMapper;
import com.cmcc.vrp.province.model.AccountChangeApprovalRecord;
import com.cmcc.vrp.province.service.AccountChangeApprovalRecordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * <p>Description: 审批意见记录单元测试类</p>
 *
 * @author xujue
 * @date 2016年5月19日
 */
@RunWith(MockitoJUnitRunner.class)
public class AccountChangeApprovalRecordServiceImplTest {

    @InjectMocks
    AccountChangeApprovalRecordService acarService = new AccountChangeApprovalRecordServiceImpl();

    @Mock
    AccountChangeApprovalRecordMapper acarm;

    /**
     * 插入账户变更审核记录， 成功的情况
     */
    @Test
    public void testInsert() {

        assertFalse(acarService.insert(null));
        AccountChangeApprovalRecord acar = initObject();

        when(acarm.insert(acar)).thenReturn(1);

        assertTrue(acarService.insert(acar));
        verify(acarm).insert(acar);

    }

    /**
     * 插入账户变更审核记录， 失败的情况
     */
    @Test
    public void testInsert2() {
        AccountChangeApprovalRecord acar = initObject();

        when(acarm.insert(acar)).thenReturn(0);

        assertFalse(acarService.insert(acar));
        verify(acarm).insert(acar);

    }

    /**
     * 根据主键获取账户变更审核记录
     */
    @Test
    public void testGet() {

        Long id = 1L;

        AccountChangeApprovalRecord acar = new AccountChangeApprovalRecord();

        when(acarm.get(id)).thenReturn(acar);

        assertNull(acarService.get(null));
        assertNotNull(acarService.get(id));
        verify(acarm).get(id);

    }

    /**
     * 根据请求ID获取账户变更审核记录列表
     */
    @Test
    public void testGetByRequestId() {

        Long id = 1L;
        List<AccountChangeApprovalRecord> acarList = new ArrayList();

        when(acarm.getByRequestId(id)).thenReturn(acarList);

        assertNull(acarService.getByRequestId(null));
        assertNotNull(acarService.getByRequestId(id));
        verify(acarm).getByRequestId(id);

    }

    /**
     * 初始化记录
     *
     * @return 初始化后的账户变更审核记录对象
     */
    private AccountChangeApprovalRecord initObject() {

        AccountChangeApprovalRecord acar = new AccountChangeApprovalRecord();
        acar.setAccountChangeRequestId(1L);
        acar.setOperatorId(1L);
        acar.setSerialNum("aaa");
        acar.setOperatorResult(new Integer(1));

        return acar;

    }

}
