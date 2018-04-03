package com.cmcc.vrp.boss;

/**
 * BOSS匹配的结果
 *
 * Created by sunyiwei on 2016/8/10.
 */
public class BossMatchResult {
    //选定的BOSS服务
    private BossService bossService;

    //选定的供应商产品，必须与bossService匹配，因为最终是调用boss服务进行充值
    private Long splPid;

    public BossMatchResult(BossService bossService, Long splPid) {
        this.bossService = bossService;
        this.splPid = splPid;
    }

    public BossService getBossService() {
        return bossService;
    }

    public void setBossService(BossService bossService) {
        this.bossService = bossService;
    }

    public Long getSplPid() {
        return splPid;
    }

    public void setSplPid(Long splPid) {
        this.splPid = splPid;
    }
}
