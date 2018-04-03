package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("Request")
public class ShProductReq {
    
    @XStreamAlias("OrderProduct")
    private ShOrderProduct orderProduct;

    public ShOrderProduct getOrderProduct() {
        return orderProduct;
    }

    public void setOrderProduct(ShOrderProduct orderProduct) {
        this.orderProduct = orderProduct;
    }
}
