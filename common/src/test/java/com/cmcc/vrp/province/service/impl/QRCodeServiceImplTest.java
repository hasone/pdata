package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.model.ActivityInfo;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.QRCodeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

/**
 * Created by qinqinyan on 2016/11/3.
 */
@RunWith(MockitoJUnitRunner.class)
public class QRCodeServiceImplTest {
    @InjectMocks
    QRCodeService qrCodeService = new QRCodeServiceImpl();
    @Mock
    ActivityWinRecordService activityWinRecordService;
    @Mock
    HttpServletRequest request  ;
    @Mock
    HttpServletResponse response;
    @Mock
    GlobalConfigService globalConfigService;

    @Test
    public void testQRCodeCommon1(){
        ActivityInfo activityInfo = new ActivityInfo();
        activityInfo.setActivityId("test");
        String fileName = "testName";

        Mockito.when(activityWinRecordService.batchInsertForQRcode(anyString(), any(ActivityInfo.class))).thenReturn(false);
        assertFalse(qrCodeService.qRCodeCommon(activityInfo, fileName, request, response));
        Mockito.verify(activityWinRecordService).batchInsertForQRcode(anyString(), any(ActivityInfo.class));
    }

    @Test
    public void testQRCodeCommon2(){
        ActivityInfo activityInfo = new ActivityInfo();
        activityInfo.setActivityId("test");
        activityInfo.setId(1L);
        activityInfo.setQrcodeSize(10);

        String fileName = "testName";

        List<ActivityWinRecord> activityWinRecords = new ArrayList<ActivityWinRecord>();
        ActivityWinRecord activityWinRecord = new ActivityWinRecord();
        activityWinRecord.setRecordId("1");
        activityWinRecords.add(activityWinRecord);

        Mockito.when(activityWinRecordService.batchInsertForQRcode(anyString(), any(ActivityInfo.class))).thenReturn(true);
        Mockito.when(activityWinRecordService.selectByActivityId(anyString())).thenReturn(activityWinRecords);
        Mockito.when(globalConfigService.get("QRCODE_DOWNLOAD_PATH")).thenReturn("/srv/appdata/data/qrcode");
        Mockito.when(globalConfigService.get("ACTIVITY_URL_KEY")).thenReturn("this is test");
        Mockito.when(globalConfigService.get("LOTTERY_QRCODE_URL"))
                .thenReturn("http://pdataqa.4ggogo.com/web-in/manage/qrcode/charge/index.html?token=");

        assertFalse(qrCodeService.qRCodeCommon(activityInfo, fileName, request, response));

        Mockito.verify(activityWinRecordService).batchInsertForQRcode(anyString(), any(ActivityInfo.class));
        Mockito.verify(activityWinRecordService).selectByActivityId(anyString());
        Mockito.verify(globalConfigService).get("QRCODE_DOWNLOAD_PATH");
        Mockito.verify(globalConfigService).get("ACTIVITY_URL_KEY");
        Mockito.verify(globalConfigService).get("LOTTERY_QRCODE_URL");
    }

    @Test
    public void testQRCodeCommon3(){
        ActivityInfo activityInfo = new ActivityInfo();
        activityInfo.setActivityId("test");
        activityInfo.setId(1L);
        activityInfo.setQrcodeSize(10);
        activityInfo.setPrizeCount(1L);

        String fileName = "testName";

        List<ActivityWinRecord> activityWinRecords = new ArrayList<ActivityWinRecord>();
        ActivityWinRecord activityWinRecord = new ActivityWinRecord();
        activityWinRecord.setRecordId("1");
        activityWinRecords.add(activityWinRecord);

        Mockito.when(activityWinRecordService.batchInsertForQRcode(anyString(), any(ActivityInfo.class))).thenReturn(true);
        Mockito.when(activityWinRecordService.selectByActivityId(anyString())).thenReturn(activityWinRecords);
        Mockito.when(globalConfigService.get("QRCODE_DOWNLOAD_PATH")).thenReturn("/srv/appdata/data/qrcode");
        Mockito.when(globalConfigService.get("ACTIVITY_URL_KEY")).thenReturn("this is test");
        Mockito.when(globalConfigService.get("LOTTERY_QRCODE_URL"))
                .thenReturn("http://pdataqa.4ggogo.com/web-in/manage/qrcode/charge/index.html?token=");

        assertFalse(qrCodeService.qRCodeCommon(activityInfo, fileName, request, response));

        Mockito.verify(activityWinRecordService, Mockito.atLeastOnce()).batchInsertForQRcode(anyString(), any(ActivityInfo.class));
        Mockito.verify(activityWinRecordService, Mockito.atLeastOnce()).selectByActivityId(anyString());
        Mockito.verify(globalConfigService, Mockito.atLeastOnce()).get("QRCODE_DOWNLOAD_PATH");
        Mockito.verify(globalConfigService, Mockito.atLeastOnce()).get("ACTIVITY_URL_KEY");
        Mockito.verify(globalConfigService, Mockito.atLeastOnce()).get("LOTTERY_QRCODE_URL");
    }


}
