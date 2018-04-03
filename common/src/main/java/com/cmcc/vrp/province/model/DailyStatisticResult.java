package com.cmcc.vrp.province.model;

/**
 * 
 * 日统计结果
 *
 */
public class DailyStatisticResult extends DailyStatistic implements Comparable<DailyStatisticResult> {
    private String name;

    private Long value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    /**
     * 从大到小排序
     */
    @Override
    public int compareTo(DailyStatisticResult o) {
        long result = this.getValue().longValue() - o.getValue().longValue();
        if (result > 0) {
            return -1;
        } else if (result == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int hashCode() {
        return String.valueOf(this.getValue()).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DailyStatisticResult) {
            DailyStatisticResult o = (DailyStatisticResult) obj;
            return String.valueOf(this.getValue()).equals(String.valueOf(o.getValue()));
        } else {
            return super.equals(obj);
        }
    }
}
