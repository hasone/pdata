package com.cmcc.vrp.boss.chongqing.enums;

/**
 * 重庆平台产品列表
 * 
 * @author lgk8023
 *
 */
public enum ProductList {
    gl_mwsq_10M("gl_mwsq_10M", "买万送千团购流量3元10M", "10", "300"),
    gl_mwsq_30M("gl_mwsq_30M", "买万送千团购流量5元30M", "30", "500"),
    gl_mwsq_70M("gl_mwsq_70M", "买万送千团购流量10元70M", "70", "1000"),
    gl_mwsq_150M("gl_mwsq_150M", "买万送千团购流量20元150M","150", "2000"),   
    gl_mwsq_500M("gl_mwsq_500M", "买万送千团购流量30元500M","500", "3000"), 
    gl_mwsq_1G("gl_mwsq_1G", "买万送千团购流量50元1G","1024", "5000"),
    gl_mwsq_11G("gl_mwsq_11G", "万送千团购流量11G", "11264", "28000"),
    gl_tgdz_500MS("gl_tgdz_500MS", "团购打折30元500M", "500", "3000"),
    gl_tgdz_1GS("gl_tgdz_1GS", "团购打折50元1G", "1024", "5000"),    
    gl_tgdz_2GS("gl_tgdz_2GS", "团购打折70元2G", "2048", "7000"),
    gl_tgdz_3GS("gl_tgdz_3GS", "团购打折100元3G", "3072", "10000"),
    gl_tgdz_4GS("gl_tgdz_4GS", "团购打折130元4G", "4096", "13000"),
    gl_mwsq_3G("gl_mwsq_3G", "买万送千团购流量3G", "3072", "10000"),   
    gl_tgdz_70MS("gl_tgdz_70MS", "团购打折10元70M", "70", "1000"), 
    gl_tgdz_30MS("gl_tgdz_30MS", "团购打折5元30M", "30", "500"),
    gl_tgdz_150MS("gl_tgdz_150MS", "团购打折20元150M", "150", "2000"),
    gl_tgdz_6GS("gl_tgdz_6GS", "团购打折180元6G", "6144", "18000"),
    gl_mwsq_2G("gl_mwsq_2G", "买完送千团购流量2G", "2048", "7000"),
    gl_mwsq_1g("gl_mwsq_1g", "买万送千团购流量1g", "1024", "5000"),
    gl_mwsq_70m("gl_mwsq_70m", "买万送千团购流量10元70mb", "70", "1000"),
    gl_tgdz_11GS("gl_tgdz_11GS", "团购打折280元11G", "11264", "28000"), 
    gl_mwsq_10m("gl_mwsq_10m", "买万送千团购流量3元10mb", "10", "300"),
    gl_mwsq_30m("gl_mwsq_30m", "买万送千团购流量5元30mb", "30", "500"),
    gl_mwsq_4G("gl_mwsq_4G", "买万送千团购流量130元4G", "4096", "13000"),    
    gl_mwsq_6G("gl_mwsq_6G", "买万送千团购流量180元6G", "6144", "18000"),
    gl_tgdz_10MS("gl_tgdz_10MS", "团购打折团购流量3元10M", "10", "300"), 
    gl_mwsq_400m("gl_mwsq_400m", "买万送千400M", "400", "3000"),
    gl_mwsq_600m("gl_mwsq_600m", "买万送千600M", "600", "5000"),
    gl_tgdz_700m("gl_tgdz_700m", "团购打折团购流量40元700M", "700", "4000"), 
    gl_mwsq_700m("gl_mwsq_700m", "买万送千团购流量40元700M", "700", "4000"),
    gl_gprstg_700m("gl_gprstg_700m", "团购国内流量700M", "700", "4000"),
    gl_tfxszs_500m("gl_tfxszs_500m", "团购流量5元500M", "500", "500"),
    gl_tfxszs95_500m("gl_tfxszs95_500m", "团购流量5元500M95折", "500", "500"),
    gl_tfxszs_1G("gl_tfxszs_1G", "团购流量10元1G", "1024", "1000"),
    gl_tfxszs95_1G("gl_tfxszs95_1G", "团购流量10元1G95折", "1024", "1000");
    
    
    private String productCode;
    
    private String name;

    private String size;
    
    private String price;

    ProductList(String productCode, String name, String size, String price) {
        this.productCode = productCode;
        this.name = name;
        this.size = size;
        this.price = price;
        
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
