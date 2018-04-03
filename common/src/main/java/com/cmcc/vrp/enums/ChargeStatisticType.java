package com.cmcc.vrp.enums;

import java.util.HashSet;
import java.util.Set;

/**
 * 报表类型
 *
 */
public enum ChargeStatisticType {
    PlatForm(1,"平台报表"),
    District(2,"地区报表"),
    Enterprise(3,"企业报表"), 
    Product(4,"产品报表");
    
    
    private int code;
    
    private String name;


    private ChargeStatisticType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * 根据角色Id获取相应权限
     * 
     */
    public static Set<Integer> getTypesByRoleId(boolean isCustomManager,boolean isCityManager,
            boolean isProvinceManager){
        Set<Integer> types = new HashSet<Integer>();
        
        if(isCustomManager){
            types.add(Enterprise.getCode());
            types.add(Product.getCode());
        }
        if(isProvinceManager){
            types.add(PlatForm.getCode());
            types.add(Enterprise.getCode());
            types.add(District.getCode());
            types.add(Product.getCode());
        }
        if(isCityManager){
            types.add(PlatForm.getCode());
            types.add(Enterprise.getCode());
            types.add(Product.getCode());
        }
        
        return types;
    }
}
