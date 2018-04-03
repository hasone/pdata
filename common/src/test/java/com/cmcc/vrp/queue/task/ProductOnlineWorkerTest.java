package com.cmcc.vrp.queue.task;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.context.ApplicationContext;

import com.cmcc.vrp.boss.shanghai.ShProductAutoServiceImpl;
import com.cmcc.vrp.province.service.ProductAutoService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.ProductOnlinePojo;
import com.google.gson.Gson;

/**
 * Created by leelyn on 2016/11/20.
 */
@RunWith(PowerMockRunner.class)
//@ContextConfiguration("classpath:conf/applicationContext.xml")
//@PrepareForTest(ProductOnlineWorker.class)
public class ProductOnlineWorkerTest {

    @InjectMocks
    ProductOnlineWorker productOnlineWorker = new ProductOnlineWorker();

    @Mock
    ApplicationContext applicationContext;

    @Mock
    ProductAutoService productAutoService;

    @Mock
    TaskProducer taskProducer;

    
    @Test
    public void testExec() throws Exception {
	ProductAutoService paService = new ShProductAutoServiceImpl();
	Map<String, ProductAutoService> map = new HashMap<String, ProductAutoService>();
	map.put("aa", paService);
	
	PowerMockito.when(applicationContext.getBeansOfType(ProductAutoService.class)).thenReturn(map);
	PowerMockito.when(taskProducer.productProductOnlineMsg(Mockito.any(ProductOnlinePojo.class))).thenReturn(true);
	
	productOnlineWorker.setTaskString(buildTaskStr());
	productOnlineWorker.exec();
	
	Mockito.verify(applicationContext, Mockito.times(1)).getBeansOfType(ProductAutoService.class);
	Mockito.verify(taskProducer, Mockito.times(1)).productProductOnlineMsg(Mockito.any(ProductOnlinePojo.class));
    }


    private String buildTaskStr() {
        ProductOnlinePojo pojo = new ProductOnlinePojo();
        pojo.setEntId(1l);
        pojo.setFingerPrint("shanghainational123456789");
        return new Gson().toJson(pojo);
    }

}
