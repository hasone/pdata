package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.PresentSerialNumMapper;
import com.cmcc.vrp.province.model.PresentSerialNum;
import com.cmcc.vrp.province.service.PresentSerialNumService;
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
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 批量赠送块流水号对应关系UT
 * <p>
 * Created by sunyiwei on 2016/11/1.
 */
@RunWith(MockitoJUnitRunner.class)
public class PresentSerialNumSeriviceImplTest extends BaseTest {
    @InjectMocks
    PresentSerialNumService presentSerialNumSerivice = new PresentSerialNumServiceImpl();

    @Mock
    PresentSerialNumMapper presentSerialNumMapper;

    /**
     * 批量插入流水号对应关系UT
     *
     * @throws Exception
     */
    @Test
    public void testBatchInsert() throws Exception {
        assertFalse(presentSerialNumSerivice.batchInsert(null));
        assertFalse(presentSerialNumSerivice.batchInsert(invalidList()));

        List<PresentSerialNum> psns = validList();
        int size = psns.size();

        when(presentSerialNumMapper.batchInsert(anyListOf(PresentSerialNum.class)))
            .thenReturn(size - 1).thenReturn(size);

        assertFalse(presentSerialNumSerivice.batchInsert(psns));
        assertTrue(presentSerialNumSerivice.batchInsert(psns));

        verify(presentSerialNumMapper, times(2)).batchInsert(anyListOf(PresentSerialNum.class));
    }

    /**
     * 批量插入流水号对应关系UT
     *
     * @throws Exception
     */
    @Test
    public void testBatchInsert1() throws Exception {
        assertFalse(presentSerialNumSerivice.batchInsert(null, new LinkedList<String>()));
        assertFalse(presentSerialNumSerivice.batchInsert(randStr(), null));
        assertFalse(presentSerialNumSerivice.batchInsert(randStr(), new LinkedList<String>()));

        List<String> sns = randList();
        int size = sns.size();

        when(presentSerialNumMapper.batchInsert(anyListOf(PresentSerialNum.class)))
            .thenReturn(size - 1).thenReturn(size);

        assertFalse(presentSerialNumSerivice.batchInsert(randStr(), sns));
        assertTrue(presentSerialNumSerivice.batchInsert(randStr(), sns));

        verify(presentSerialNumMapper, times(2)).batchInsert(anyListOf(PresentSerialNum.class));
    }

    /**
     * 根据平台流水号获取相应的对应关系
     *
     * @throws Exception
     */
    @Test
    public void testSelectByPltSn() throws Exception {
        assertNull(presentSerialNumSerivice.selectByPltSn(null));
        assertNull(presentSerialNumSerivice.selectByPltSn(""));

        when(presentSerialNumMapper.getByPltSn(anyString()))
            .thenReturn(null).thenReturn(new PresentSerialNum());

        assertNull(presentSerialNumSerivice.selectByPltSn(randStr()));
        assertNotNull(presentSerialNumSerivice.selectByPltSn(randStr()));

        verify(presentSerialNumMapper, times(2)).getByPltSn(anyString());
    }

    private List<String> randList() {
        int size = new Random().nextInt(100) + 10;
        List<String> sns = new LinkedList<String>();
        for (int i = 0; i < size; i++) {
            sns.add(randStr());
        }

        return sns;
    }

    private List<PresentSerialNum> invalidList() {
        List<PresentSerialNum> psns = validList();
        psns.add(nullValue());

        return psns;
    }

    private List<PresentSerialNum> validList() {
        int size = new Random().nextInt(100) + 10;

        List<PresentSerialNum> psns = new LinkedList<PresentSerialNum>();
        for (int i = 0; i < size; i++) {
            psns.add(valid());
        }

        return psns;
    }

    private PresentSerialNum valid() {
        PresentSerialNum presentSerialNum = new PresentSerialNum();
        presentSerialNum.setBlockSerialNum(randStr());
        presentSerialNum.setPlatformSerialNum(randStr());

        return presentSerialNum;
    }

    private PresentSerialNum nullValue() {
        return null;
    }

    private PresentSerialNum nullBlockSn() {
        PresentSerialNum psn = valid();
        psn.setBlockSerialNum(null);

        return psn;
    }

    private PresentSerialNum nullPltSn() {
        PresentSerialNum psn = valid();
        psn.setPlatformSerialNum(null);

        return psn;
    }
}