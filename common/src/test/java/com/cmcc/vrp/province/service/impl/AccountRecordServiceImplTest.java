package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.enums.AccountRecordType;
import com.cmcc.vrp.province.dao.AccountRecordMapper;
import com.cmcc.vrp.province.model.AccountRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 账户记录UT
 * <p>
 * Created by sunyiwei on 2016/10/31.
 */
@RunWith(MockitoJUnitRunner.class)
public class AccountRecordServiceImplTest extends BaseTest {
    @InjectMocks
    AccountRecordServiceImpl accountRecordService = new AccountRecordServiceImpl();

    @Mock
    AccountRecordMapper accountRecordMapper;

    /**
     * 测试批量插入
     *
     * @throws Exception
     */
    @Test
    public void testBatchInsert() throws Exception {
        assertFalse(accountRecordService.batchInsert(null));
        assertTrue(accountRecordService.batchInsert(new LinkedList<AccountRecord>()));
        assertFalse(accountRecordService.batchInsert(buildInvalidArs()));

        List<AccountRecord> ars = buildValidArs();
        when(accountRecordMapper.batchInsert(anyListOf(AccountRecord.class)))
            .thenReturn(ars.size() - 1).thenReturn(ars.size());

        assertFalse(accountRecordService.batchInsert(ars));
        assertTrue(accountRecordService.batchInsert(ars));

        verify(accountRecordMapper, times(2)).batchInsert(anyListOf(AccountRecord.class));
    }

    /**
     * 测试创建账户记录
     *
     * @throws Exception
     */
    @Test
    public void testCreate() throws Exception {
        assertFalse(accountRecordService.create(buildNullAccountRecord()));
        assertFalse(accountRecordService.create(buildNullEnterId()));
        assertFalse(accountRecordService.create(buildNullOwnerId()));
        assertFalse(accountRecordService.create(buildNullAccountId()));
        assertFalse(accountRecordService.create(buildNullType()));
        assertFalse(accountRecordService.create(buildNullSerialNum()));
        assertFalse(accountRecordService.create(buildNegativeCount()));

        when(accountRecordMapper.insert(any(AccountRecord.class)))
            .thenReturn(0).thenReturn(1);

        assertFalse(accountRecordService.create(buildValid()));
        assertTrue(accountRecordService.create(buildValid()));

        verify(accountRecordMapper, times(2)).insert(any(AccountRecord.class));
    }

    /**
     * 测试根据系统流水号和企业ID获取相应的记录
     *
     * @throws Exception
     */
    @Test
    public void testSelectBySerialNumAndEnterId() throws Exception {
        assertNull(accountRecordService.selectBySerialNumAndEnterId(null, 3433L));
        assertNull(accountRecordService.selectBySerialNumAndEnterId("", 3433L));
        assertNull(accountRecordService.selectBySerialNumAndEnterId("fdsaf", null));

        when(accountRecordMapper.selectBySerialNumAndEnterId(anyString(), anyLong()))
            .thenReturn(null).thenReturn(new LinkedList<AccountRecord>());

        assertNull(accountRecordService.selectBySerialNumAndEnterId("fdsa", 433L));
        assertNotNull(accountRecordService.selectBySerialNumAndEnterId("fdsa", 433L));

        verify(accountRecordMapper, times(2)).selectBySerialNumAndEnterId(anyString(), anyLong());
    }

    /**
     * 根据平台流水号获取相应的出账记录
     *
     * @throws Exception
     */
    @Test
    public void testGetOutgoingRecordByPltSn() throws Exception {
        assertNull(accountRecordService.getOutgoingRecordByPltSn(null));
        assertNull(accountRecordService.getOutgoingRecordByPltSn(""));

        when(accountRecordMapper.getOutgoingRecordByPltSn(anyString()))
            .thenReturn(null).thenReturn(new AccountRecord());

        assertNull(accountRecordService.getOutgoingRecordByPltSn(randStr()));
        assertNotNull(accountRecordService.getOutgoingRecordByPltSn(randStr()));

        verify(accountRecordMapper, times(2)).getOutgoingRecordByPltSn(anyString());
    }

    private List<AccountRecord> buildValidArs() {
        int size = new Random().nextInt(100) + 10;
        List<AccountRecord> ars = new LinkedList<AccountRecord>();
        for (int i = 0; i < size; i++) {
            ars.add(buildValid());
        }

        return ars;
    }

    private List<AccountRecord> buildInvalidArs() {
        int size = new Random().nextInt(100) + 10;
        List<AccountRecord> ars = new LinkedList<AccountRecord>();
        for (int i = 0; i < size; i++) {
            ars.add(buildValid());
        }

        //增加一个验证无法通过的账户记录
        ars.add(buildNegativeCount());
        return ars;
    }

    private AccountRecord buildValid() {
        Random random = new Random();

        AccountRecord accountRecord = new AccountRecord();
        accountRecord.setEnterId(random.nextLong());
        accountRecord.setOwnerId(random.nextLong());
        accountRecord.setAccountId(random.nextLong());
        accountRecord.setType(AccountRecordType.OUTGO.getValue());
        accountRecord.setSerialNum(randStr(15));
        accountRecord.setCount(random.nextDouble());

        return accountRecord;
    }

    private AccountRecord buildNullAccountRecord() {
        return null;
    }

    private AccountRecord buildNullEnterId() {
        AccountRecord accountRecord = new AccountRecord();
        accountRecord.setEnterId(null);

        return accountRecord;
    }

    private AccountRecord buildNullOwnerId() {
        AccountRecord accountRecord = new AccountRecord();
        accountRecord.setOwnerId(null);

        return accountRecord;
    }

    private AccountRecord buildNullAccountId() {
        AccountRecord accountRecord = new AccountRecord();
        accountRecord.setAccountId(null);

        return accountRecord;
    }

    private AccountRecord buildNullType() {
        AccountRecord accountRecord = new AccountRecord();
        accountRecord.setType(null);

        return accountRecord;
    }

    private AccountRecord buildNullSerialNum() {
        AccountRecord accountRecord = new AccountRecord();
        accountRecord.setSerialNum(null);

        return accountRecord;
    }

    private AccountRecord buildNegativeCount() {
        AccountRecord accountRecord = new AccountRecord();
        accountRecord.setCount(-1d);

        return accountRecord;
    }
}
