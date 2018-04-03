package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.GiveMoneyEnter;

public interface GiveMoneyEnterService {
    boolean insert(Long giveMoneyId, Long enterId);

    boolean updateByEnterId(Long giveMoneyId, Long enterId);

    GiveMoneyEnter selectByEnterId(Long enterId);

    boolean deleteGiveMoneyEnterByEnterId(Long enterId);
}
