package com.cmcc.vrp.boss.hainan;

import com.cmcc.vrp.boss.BaseBossServiceTest;
import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.hainan.model.HNBOSSCharge;
import com.cmcc.vrp.boss.hainan.model.HNBOSSChargeResponse;
import com.cmcc.vrp.boss.hainan.model.HNBOSSUserInfoCheckReponse;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.DateUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by lilin on 2016/10/13.
 */
@PrepareForTest({HaiNanBOSSServiceImpl.class, SerialNumGenerator.class, DateUtil.class})
public class HnBossServiceImplTest extends BaseBossServiceTest {
    @Mock
    SupplierProductService supplierProductService;
    @Mock
    EnterprisesService enterprisesService;
    @Mock
    SerialNumService serialNumService;
    @InjectMocks
    HaiNanBOSSServiceImpl haiNanBOSSService = new HaiNanBOSSServiceImpl();

    @Before
    public void initMocks() {
        when(supplierProductService.selectByPrimaryKey(anyLong())).thenReturn(this.buildSP());
        when(enterprisesService.selectByPrimaryKey(anyLong())).thenReturn(this.buildEnt());
    }

    @Ignore
    @Test
    public void testCharge() throws Exception {
        String systemNum = SerialNumGenerator.buildSerialNum();
        Mockito.when(serialNumService.getByPltSerialNum(systemNum)).thenReturn(new SerialNum());
        Mockito.when(serialNumService.updateSerial(any(SerialNum.class))).thenReturn(true);

        haiNanBOSSService = PowerMockito.spy(haiNanBOSSService);
        PowerMockito.doReturn(buildBOR()).when(haiNanBOSSService, "userInfoCheck", "18867101129");

        PowerMockito.mockStatic(SerialNumGenerator.class);
        PowerMockito.when(SerialNumGenerator.buildNormalBossReqNum("hainan", 25)).thenReturn("xxxxxx");

        Date date = PowerMockito.mock(Date.class);
        PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(date);

        PowerMockito.mockStatic(DateUtil.class);
        PowerMockito.mockStatic(Calendar.class);
        Calendar calendar = Calendar.getInstance();
        PowerMockito.when(Calendar.getInstance()).thenReturn(calendar);
        PowerMockito.when(calendar.get(Calendar.YEAR)).thenReturn(anyInt());
        PowerMockito.when(calendar.get(Calendar.MONTH)).thenReturn(anyInt());
        PowerMockito.when(DateUtil.getEndTimeOfMonth(anyInt(), anyInt())).thenReturn(date);

        SimpleDateFormat format = PowerMockito.mock(SimpleDateFormat.class);
        PowerMockito.whenNew(SimpleDateFormat.class).withArguments("yyyy-MM-dd").thenReturn(format);
        PowerMockito.whenNew(SimpleDateFormat.class).withArguments("yyyy-MM-dd HH:mm:ss").thenReturn(format);
        PowerMockito.when(format.format(date)).thenReturn("xxxxxx");

        PowerMockito.doReturn("xxxxxx").when(haiNanBOSSService, "charge", "xxxxxx", "xxxxxx", "xxxxxx", "18867101129", "1", "xxxxxx", "xxxxxx", "xxxxxx", "xxxxxx");
        PowerMockito.doReturn(buildCR()).when(haiNanBOSSService, "parseChargeResponse", "xxxxxx");

        BossOperationResult result = haiNanBOSSService.charge(1l, 1l, "18867101129", systemNum, null);
        Assert.assertTrue(result.isSuccess());
    }

    private HNBOSSChargeResponse buildCR() {
        HNBOSSChargeResponse response = new HNBOSSChargeResponse();
        response.setCode("0");
        HNBOSSCharge charge = new HNBOSSCharge();
        charge.setTradeId("xxxxxx");
        response.setCharge(charge);
        return response;
    }

    @Override
    public SupplierProduct buildSP() {
        SupplierProduct product = super.buildSP();
        String featrue = "{\"PAK_MONEY\":\"xxxxxx\",\"PAK_GPRS\":\"xxxxxx\"}";
        product.setFeature(featrue);
        return product;
    }

    @Override
    public Enterprise buildEnt() {
        Enterprise enterprise = super.buildEnt();
        enterprise.setCode("xxxxxx");
        return enterprise;
    }

    private HNBOSSUserInfoCheckReponse buildBOR() {
        HNBOSSUserInfoCheckReponse checkReponse = new HNBOSSUserInfoCheckReponse();
        checkReponse.setCode("0");
        return checkReponse;
    }
}
