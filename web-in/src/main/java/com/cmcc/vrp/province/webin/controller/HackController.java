package com.cmcc.vrp.province.webin.controller;

import com.cmcc.vrp.boss.BossServiceProxyService;
import com.cmcc.vrp.boss.gansu.GSBossOperationResultImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 后门服务，用于测试各种只能在线上测试的功能, 这个类中的每个方法都应该有详细的说明，否则别人可能无法知道如何使用
 * <p>
 * Created by sunyiwei on 2016/5/26.
 */
@Controller
@RequestMapping("/manage/hack")
public class HackController {

    @Autowired
    private BossServiceProxyService bossServiceProxyService;

    @RequestMapping("gansu/boss/{mobile}/{serialNum}/{entId}/{pId}")
    @ResponseBody
    public GSBossOperationResultImpl testGanSuBoss(@PathVariable String mobile, @PathVariable String serialNum, @PathVariable Long entId, @PathVariable Long pId) {
        return (GSBossOperationResultImpl) bossServiceProxyService.charge(null, entId, pId, mobile, serialNum);
    }
}
