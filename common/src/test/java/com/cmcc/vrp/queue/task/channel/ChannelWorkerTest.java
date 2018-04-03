package com.cmcc.vrp.queue.task.channel;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import com.cmcc.vrp.wx.impl.GdZcBossServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.beijing.BjBossServiceImpl;
import com.cmcc.vrp.boss.core.FdnBossServiceImpl;
import com.cmcc.vrp.boss.ecinvoker.impl.CqEcBossServiceImpl;
import com.cmcc.vrp.boss.ecinvoker.impl.SdEcBossServiceImpl;
import com.cmcc.vrp.boss.fujian.FjBossServiceImpl;
import com.cmcc.vrp.boss.gansu.GSBossServiceImpl;
import com.cmcc.vrp.boss.guangdong.GdBossServiceImpl;
import com.cmcc.vrp.boss.guangxi.GxBossServiceImpl;
import com.cmcc.vrp.boss.hainan.HaiNanBOSSServiceImpl;
import com.cmcc.vrp.boss.hebei.HbBossServiceImpl;
import com.cmcc.vrp.boss.heilongjiang.HLJBossServiceImpl;
import com.cmcc.vrp.boss.heilongjiang.fee.HljFeeBossServiceImpl;
import com.cmcc.vrp.boss.henan.HaChannelBossServiceImpl;
import com.cmcc.vrp.boss.henan.HaPlatformBossServiceImpl;
import com.cmcc.vrp.boss.hunan.HNBossServcieImpl;
import com.cmcc.vrp.boss.hunan.HNSpecialBossServiceImpl;
import com.cmcc.vrp.boss.jiangxi.JxBossServiceImpl;
import com.cmcc.vrp.boss.neimenggu.NMBossServiceImpl;
import com.cmcc.vrp.boss.shangdong.boss.service.SdCloudBossServiceImpl;
import com.cmcc.vrp.boss.shanghai.ShNationalBossServiceImpl;
import com.cmcc.vrp.boss.shanxi.SxBossServiceImpl;
import com.cmcc.vrp.boss.sichuan.SCBossServiceImpl;
import com.cmcc.vrp.boss.xinjiang.XjBossServiceImpl;
import com.cmcc.vrp.boss.zhejiang.ZjBossServiceImpl;
import com.cmcc.vrp.queue.task.Worker;

/**
 * Created by leelyn on 2016/11/20.
 */
@RunWith(PowerMockRunner.class)
public class ChannelWorkerTest {

    @Test
    public void testChannelWorker() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<Info> list = buildList();
        for (Info info : list) {
            testDeal(info);
        }
    }

    private void testDeal(Info info) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<? extends Worker> workerClass = info.getWorkerClass();
        Class<? extends BossService> bossServiceClass = info.getBossServiceClass();

        Worker subWorker = workerClass.getConstructor().newInstance();
        BossService bossService = null;
        if (bossServiceClass != null) {
            bossService = bossServiceClass.getConstructor().newInstance();
            ReflectionTestUtils.setField(subWorker, "bossService", bossService);
        }

        if (subWorker instanceof ZwChannelWorker) {
            Assert.assertTrue(((ZwChannelWorker) subWorker).isContinueDistribute());
            Assert.assertNull(((ZwChannelWorker) subWorker).getBossService());
        }

        if (subWorker instanceof BjChannelWorker) {
            Assert.assertFalse(((BjChannelWorker) subWorker).isContinueDistribute());
            Assert.assertEquals(bossService, ((BjChannelWorker) subWorker).getBossService());
        }

        if (subWorker instanceof CqChannelWorker) {
            Assert.assertFalse(((CqChannelWorker) subWorker).isContinueDistribute());
            Assert.assertEquals(bossService, ((CqChannelWorker) subWorker).getBossService());
        }

        if (subWorker instanceof CqPlatformWorker) {
            Assert.assertFalse(((CqPlatformWorker) subWorker).isContinueDistribute());
            Assert.assertEquals(bossService, ((CqPlatformWorker) subWorker).getBossService());
        }

        if (subWorker instanceof FdnChannelWorker) {
            Assert.assertFalse(((FdnChannelWorker) subWorker).isContinueDistribute());
            Assert.assertEquals(bossService, ((FdnChannelWorker) subWorker).getBossService());
        }

        if (subWorker instanceof FjChannelWorker) {
            Assert.assertFalse(((FjChannelWorker) subWorker).isContinueDistribute());
            Assert.assertEquals(bossService, ((FjChannelWorker) subWorker).getBossService());
        }

        if (subWorker instanceof GdChannelWorker) {
            Assert.assertFalse(((GdChannelWorker) subWorker).isContinueDistribute());
            Assert.assertEquals(bossService, ((GdChannelWorker) subWorker).getBossService());
        }

        if (subWorker instanceof GsChannelWorker) {
            Assert.assertFalse(((GsChannelWorker) subWorker).isContinueDistribute());
            Assert.assertEquals(bossService, ((GsChannelWorker) subWorker).getBossService());
        }

        if (subWorker instanceof GxChannelWorker) {
            Assert.assertFalse(((GxChannelWorker) subWorker).isContinueDistribute());
            Assert.assertEquals(bossService, ((GxChannelWorker) subWorker).getBossService());
        }

        if (subWorker instanceof HaChannelWorker) {
            Assert.assertFalse(((HaChannelWorker) subWorker).isContinueDistribute());
            Assert.assertEquals(bossService, ((HaChannelWorker) subWorker).getBossService());
        }

        if (subWorker instanceof HaPlatformWorker) {
            Assert.assertFalse(((HaPlatformWorker) subWorker).isContinueDistribute());
            Assert.assertEquals(bossService, ((HaPlatformWorker) subWorker).getBossService());
        }

        if (subWorker instanceof HeChannelWorker) {
            Assert.assertFalse(((HeChannelWorker) subWorker).isContinueDistribute());
            Assert.assertEquals(bossService, ((HeChannelWorker) subWorker).getBossService());
        }

        if (subWorker instanceof HiChannelWorker) {
            Assert.assertFalse(((HiChannelWorker) subWorker).isContinueDistribute());
            Assert.assertEquals(bossService, ((HiChannelWorker) subWorker).getBossService());
        }

        if (subWorker instanceof HlChannelWorker) {
            Assert.assertFalse(((HlChannelWorker) subWorker).isContinueDistribute());
            Assert.assertEquals(bossService, ((HlChannelWorker) subWorker).getBossService());
        }

        if (subWorker instanceof HnChannelWorker) {
            Assert.assertFalse(((HnChannelWorker) subWorker).isContinueDistribute());
            Assert.assertEquals(bossService, ((HnChannelWorker) subWorker).getBossService());
        }

        if (subWorker instanceof HnChannelFreeWorker) {
            Assert.assertFalse(((HnChannelFreeWorker) subWorker).isContinueDistribute());
            Assert.assertEquals(bossService, ((HnChannelFreeWorker) subWorker).getBossService());
        }

        if (subWorker instanceof JxChannelWorker) {
            Assert.assertFalse(((JxChannelWorker) subWorker).isContinueDistribute());
            Assert.assertEquals(bossService, ((JxChannelWorker) subWorker).getBossService());
        }
        if (subWorker instanceof NmChannelWorker) {
            Assert.assertFalse(((NmChannelWorker) subWorker).isContinueDistribute());
            Assert.assertEquals(bossService, ((NmChannelWorker) subWorker).getBossService());
        }
        if (subWorker instanceof ScChannelWorker) {
            Assert.assertFalse(((ScChannelWorker) subWorker).isContinueDistribute());
            Assert.assertEquals(bossService, ((ScChannelWorker) subWorker).getBossService());
        }
        if (subWorker instanceof SdChannelWorker) {
            Assert.assertFalse(((SdChannelWorker) subWorker).isContinueDistribute());
            Assert.assertEquals(bossService, ((SdChannelWorker) subWorker).getBossService());
        }

        if (subWorker instanceof SdPlatformWorker) {
            Assert.assertFalse(((SdPlatformWorker) subWorker).isContinueDistribute());
            Assert.assertEquals(bossService, ((SdPlatformWorker) subWorker).getBossService());
        }
        if (subWorker instanceof ShChannelWorker) {
            Assert.assertFalse(((ShChannelWorker) subWorker).isContinueDistribute());
            Assert.assertEquals(bossService, ((ShChannelWorker) subWorker).getBossService());
        }
        if (subWorker instanceof SxChannelWorker) {
            Assert.assertFalse(((SxChannelWorker) subWorker).isContinueDistribute());
            Assert.assertEquals(bossService, ((SxChannelWorker) subWorker).getBossService());
        }

        if (subWorker instanceof XjChannelWorker) {
            Assert.assertFalse(((XjChannelWorker) subWorker).isContinueDistribute());
            Assert.assertEquals(bossService, ((XjChannelWorker) subWorker).getBossService());
        }
        if (subWorker instanceof ZjChannelWorker) {
            Assert.assertFalse(((ZjChannelWorker) subWorker).isContinueDistribute());
            Assert.assertEquals(bossService, ((ZjChannelWorker) subWorker).getBossService());
        }

        if (subWorker instanceof HlFeeChannelWorker) {
            Assert.assertFalse(((HlFeeChannelWorker) subWorker).isContinueDistribute());
            Assert.assertEquals(bossService, ((HlFeeChannelWorker) subWorker).getBossService());
        }

        if (subWorker instanceof CrowdfundingChannelWorker) {
            Assert.assertFalse(((CrowdfundingChannelWorker) subWorker).isContinueDistribute());
            Assert.assertEquals(bossService, ((CrowdfundingChannelWorker) subWorker).getBossService());
        }
    }

    private List<Info> buildList() {
        List<Info> list = new LinkedList<Info>();
        list.add(new Info(BjChannelWorker.class, BjBossServiceImpl.class));
        list.add(new Info(CqChannelWorker.class, CqEcBossServiceImpl.class));
        list.add(new Info(CqPlatformWorker.class, null));
        list.add(new Info(FdnChannelWorker.class, FdnBossServiceImpl.class));
        list.add(new Info(FjChannelWorker.class, FjBossServiceImpl.class));
        list.add(new Info(GdChannelWorker.class, GdBossServiceImpl.class));
        list.add(new Info(GsChannelWorker.class, GSBossServiceImpl.class));
        list.add(new Info(GxChannelWorker.class, GxBossServiceImpl.class));
        list.add(new Info(HaChannelWorker.class, HaChannelBossServiceImpl.class));
        list.add(new Info(HaPlatformWorker.class, HaPlatformBossServiceImpl.class));
        list.add(new Info(HeChannelWorker.class, HbBossServiceImpl.class));
        list.add(new Info(HiChannelWorker.class, HaiNanBOSSServiceImpl.class));
        list.add(new Info(HlChannelWorker.class, HLJBossServiceImpl.class));
        list.add(new Info(HnChannelWorker.class, HNBossServcieImpl.class));
        list.add(new Info(HnChannelFreeWorker.class, HNSpecialBossServiceImpl.class));
        list.add(new Info(JxChannelWorker.class, JxBossServiceImpl.class));
        list.add(new Info(NmChannelWorker.class, NMBossServiceImpl.class));
        list.add(new Info(ScChannelWorker.class, SCBossServiceImpl.class));
        list.add(new Info(SdChannelWorker.class, SdEcBossServiceImpl.class));
        list.add(new Info(SdPlatformWorker.class, SdCloudBossServiceImpl.class));
        list.add(new Info(ShChannelWorker.class, ShNationalBossServiceImpl.class));
        list.add(new Info(SxChannelWorker.class, SxBossServiceImpl.class));
        list.add(new Info(XjChannelWorker.class, XjBossServiceImpl.class));
        list.add(new Info(ZjChannelWorker.class, ZjBossServiceImpl.class));
        list.add(new Info(ZwChannelWorker.class, null));
        list.add(new Info(HlFeeChannelWorker.class, HljFeeBossServiceImpl.class));
        list.add(new Info(CrowdfundingChannelWorker.class, GdZcBossServiceImpl.class));
        return list;
    }


    private class Info {
        private Class<? extends Worker> workerClass;
        private Class<? extends BossService> bossServiceClass;

        public Info(Class<? extends Worker> workerClass, Class<? extends BossService> bossServiceClass) {
            this.workerClass = workerClass;
            this.bossServiceClass = bossServiceClass;
        }

        public Class<? extends BossService> getBossServiceClass() {
            return bossServiceClass;
        }

        public void setBossServiceClass(Class<? extends BossService> bossServiceClass) {
            this.bossServiceClass = bossServiceClass;
        }

        public Class<? extends Worker> getWorkerClass() {
            return workerClass;
        }

        public void setWorkerClass(Class<? extends Worker> workerClass) {
            this.workerClass = workerClass;
        }
    }
}
