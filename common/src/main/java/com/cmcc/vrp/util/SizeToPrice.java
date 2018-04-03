package com.cmcc.vrp.util;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2016年11月29日 上午11:04:27
*/
public enum SizeToPrice {	
	SIZE_10M("10m", "10", "300"),
	SIZE_30M("30m", "30", "500"),
	SIZE_70M("70m", "70", "1000"),
	SIZE_150M("150m", "150", "2000"), 
	SIZE_400M("400m", "400", "3000"),
	SIZE_500M("500m", "500", "3000"),
	SIZE_600M("600m", "600", "5000"),
	SIZE_700M("700m", "700", "4000"),
	SIZE_1G("1g", "1024", "5000"),	
	SIZE_2G("2g", "2048", "7000"),
	SIZE_3G("3g", "3072", "10000"),
	SIZE_4G("4g", "4096", "13000"),
	SIZE_6G("6g", "6144", "18000"),
	SIZE_11G("11g", "11264", "28000"),
	SIZE_10MS("10ms", "10", "300"),
	SIZE_30MS("30ms", "30", "500"),
	SIZE_70MS("70ms", "70", "1000"),
	SIZE_150MS("150ms", "150", "2000"), 
	SIZE_400MS("400ms", "400", "3000"),
	SIZE_500MS("500ms", "500", "3000"),
	SIZE_600MS("600ms", "600", "5000"),
	SIZE_700MS("700ms", "700", "4000"),
	SIZE_1GS("1gs", "1024", "5000"),	
	SIZE_2GS("2gs", "2048", "7000"),
	SIZE_3GS("3gs", "3072", "10000"),
	SIZE_4GS("4gs", "4096", "13000"), 
	SIZE_6GS("6gs", "6144", "18000"), 
	SIZE_11GS("11gs", "11264", "28000"),
	gl_tfxszs_500m("gl_tfxszs_500m", "500", "500"),
	gl_tfxszs95_500m("gl_tfxszs95_500m", "500", "500"),
	gl_tfxszs_1G("gl_tfxszs_1g", "1024", "1000"),
	gl_tfxszs95_1G("gl_tfxszs95_1g", "1024", "1000");	
    private String name;
    private String size;
    private String price;

    SizeToPrice(String name, String size, String price) {
    	this.name = name;
        this.size = size;
        this.price = price;
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
