/**
 *
 */
package com.cmcc.vrp.province.model;

/**
 * @author JamieWu
 *         存储折扣信息内容
 */
public class Discount {
    private Long id;

    private String name;

    private Double discount;
    
    private Long customerTypeId;
   
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Long getCustomerTypeId() {
        return customerTypeId;
    }

    public void setCustomerTypeId(Long customerTypeId) {
        this.customerTypeId = customerTypeId;
    }
}
