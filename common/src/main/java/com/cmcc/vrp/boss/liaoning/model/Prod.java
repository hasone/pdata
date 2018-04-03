package com.cmcc.vrp.boss.liaoning.model;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年2月23日 上午8:43:08
*/
public class Prod {
    private String ProdFee;
    
    private String ProdId;
    
    private String ProdName;

    public String getProdFee() {
        return ProdFee;
    }

    public void setProdFee(String prodFee) {
        ProdFee = prodFee;
    }

    public String getProdId() {
        return ProdId;
    }

    public void setProdId(String prodId) {
        ProdId = prodId;
    }

    public String getProdName() {
        return ProdName;
    }

    public void setProdName(String prodName) {
        ProdName = prodName;
    }

    @Override
    public String toString() {
        return "Prod [ProdFee=" + ProdFee + ", ProdId=" + ProdId + ", ProdName=" + ProdName + "]";
    }
    
}
