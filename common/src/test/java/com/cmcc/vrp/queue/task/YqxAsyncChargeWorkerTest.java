/**
 *
 */
package com.cmcc.vrp.queue.task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;

import com.cmcc.vrp.province.service.YqxChargeService;
import com.cmcc.vrp.queue.pojo.YqxChargePojo;
import com.google.gson.Gson;

/**
 * <p>Title:AsyncChargeWorkerTest </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年11月16日
 */
@RunWith(PowerMockRunner.class)
public class YqxAsyncChargeWorkerTest {
    @InjectMocks
    YqxAsyncChargeWorker asyncChargeWorker = new YqxAsyncChargeWorker();
    @Mock
    private YqxChargeService yqxChargeService;

    /**
     * testExec1
     */
    @Test
    public void testExec1() {

        asyncChargeWorker.setTaskString("  ");
        asyncChargeWorker.exec();
        
        asyncChargeWorker.setTaskString(new Gson().toJson(new YqxChargePojo("1","2")));
        Mockito.when(yqxChargeService.charge(Mockito.anyString())).thenReturn(true);
        asyncChargeWorker.exec();
        
    }

   
}
