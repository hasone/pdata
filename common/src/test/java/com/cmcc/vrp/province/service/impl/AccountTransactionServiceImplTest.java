
package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.AccountMapper;
import com.cmcc.vrp.province.model.AccountMinus;
import com.cmcc.vrp.province.model.AccountRecord;
import com.cmcc.vrp.province.service.AccountRecordService;
import com.cmcc.vrp.province.service.AccountTransactionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * <p>Title:AccountTransactionServiceImplTest </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年11月7日
 */
@RunWith(MockitoJUnitRunner.class)
public class AccountTransactionServiceImplTest {

    @InjectMocks
    AccountTransactionService atService = new AccountTransactionServiceImpl();

    @Mock
    AccountMapper accountMapper;

    @Mock
    AccountRecordService accountRecordService;

    @Test
    public void testMinus() {

        List<AccountMinus> accountMinusList = new ArrayList();
        AccountMinus am1 = new AccountMinus();
        am1.setId(1L);
        am1.setDelta(0.0);
        AccountMinus am2 = new AccountMinus();
        am2.setId(2L);
        am2.setDelta(1.0);
        accountMinusList.add(am1);
        accountMinusList.add(am2);

        when(accountMapper.updateCount(Mockito.anyLong(), Mockito.anyDouble())).thenReturn(0).thenReturn(1);
        when(accountRecordService.create(Mockito.any(AccountRecord.class))).thenReturn(false).thenReturn(true);

        try {
            atService.minus(accountMinusList);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            atService.minus(accountMinusList);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        atService.minus(accountMinusList);

    }

}
