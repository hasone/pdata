package com.cmcc.vrp.boss.jiangsu.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("qrylist")
public class Qrylist {
    @XStreamAlias("item")
    private Item item;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
