package com.cmcc.vrp.queue.task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.province.mdrc.service.MdrcBatchConfigService;
import com.cmcc.vrp.province.model.MdrcCardmakeDetail;
import com.cmcc.vrp.province.model.MdrcMakecardRequestConfig;
import com.cmcc.vrp.province.service.MdrcCardmakeDetailService;
import com.cmcc.vrp.province.service.MdrcMakecardRequestConfigService;
import com.cmcc.vrp.queue.pojo.MdrcMakeCardPojo;
import com.cmcc.vrp.util.Constants;

/**
 * Created by qinqinyan on 2017/08/09.
 */
@RunWith(MockitoJUnitRunner.class)
public class MdrcMakeCardWorkerTest {
    @InjectMocks
    MdrcMakeCardWorker worker = new MdrcMakeCardWorker();
    
    @Mock
    MdrcCardmakeDetailService mdrcCardmakeDetailService;
    @Mock
    MdrcMakecardRequestConfigService mdrcMakecardRequestConfigService;
    @Mock
    MdrcBatchConfigService mdrcBatchConfigService;
    
    private MdrcMakeCardPojo createMdrcMakeCardPojo(){
        MdrcMakeCardPojo pojo = new MdrcMakeCardPojo();
        pojo.setAdminId(1L);
        pojo.setRequestId(1L);
        return pojo;
    }
    
    private MdrcCardmakeDetail createMdrcCardmakeDetail(){
        MdrcCardmakeDetail detail = new MdrcCardmakeDetail();
        detail.setCardmakeStatus(Integer.valueOf(Constants.MAKE_CARD_STATUS.NOT_MAKE_CARD.getResult()));
        return detail;
    }
    
    private MdrcMakecardRequestConfig createMdrcMakecardRequestConfig(){
        MdrcMakecardRequestConfig mdrcMakecardRequestConfig = new MdrcMakecardRequestConfig();
        mdrcMakecardRequestConfig.setRequestId(1L);
        return mdrcMakecardRequestConfig;
    }
    
    @Test
    public void exec(){
        worker.exec();
        
        worker.setTaskString(JSONObject.toJSONString(createMdrcMakeCardPojo()));
        
        MdrcCardmakeDetail newMdrcCardmakeDetail = createMdrcCardmakeDetail();
        newMdrcCardmakeDetail.setCardmakeStatus(Integer.valueOf(Constants.MAKE_CARD_STATUS.MAKE_CARD.getResult()));
        Mockito.when(mdrcCardmakeDetailService.selectByRequestId(Mockito.anyLong()))
        .thenReturn(null).thenReturn(createMdrcCardmakeDetail()).thenReturn(newMdrcCardmakeDetail);
        worker.exec();
        
        Mockito.when(mdrcCardmakeDetailService.makecard(Mockito.anyLong(), Mockito.anyLong()))
        .thenReturn(true).thenReturn(false);
        
        Mockito.when(mdrcMakecardRequestConfigService.selectByRequestId(Mockito.anyLong()))
        .thenReturn(null).thenReturn(createMdrcMakecardRequestConfig());
        
        Mockito.when(mdrcBatchConfigService.listFile(Mockito.anyLong(), Mockito.anyLong())).thenReturn(true);
        
        worker.exec();
        worker.exec();
    }

}
