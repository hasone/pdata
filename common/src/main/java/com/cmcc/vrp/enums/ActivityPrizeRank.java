package com.cmcc.vrp.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by qinqinyan on 2016/11/23.
 */
public enum ActivityPrizeRank {

    FirstRankPrize("0","一等奖"),
    SecondRankPrize("1","二等奖"),
    ThirdRankPrize("2","三等奖"),
    ForthRankPrize("3","四等奖"),
    FifthRankPrize("4","五等奖"),
    SixthRankPrize("5","六等奖");

    private String idPrefix;

    private String rankName;

    ActivityPrizeRank(String idPrefix, String rankName){
        this.idPrefix = idPrefix;
        this.rankName = rankName;
    }

    /**
     * @return
     */
    public static Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        for (ActivityPrizeRank item : ActivityPrizeRank.values()) {
            map.put(item.getIdPrefix(), item.getRankName());
        }
        return map;
    }

    public String getIdPrefix() {
        return idPrefix;
    }

    public void setIdPrefix(String idPrefix) {
        this.idPrefix = idPrefix;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }




}
