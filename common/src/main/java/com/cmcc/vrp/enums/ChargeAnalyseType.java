package com.cmcc.vrp.enums;

import java.util.HashSet;
import java.util.Set;

/**
 * 充值分析类型
 *
 */
public enum ChargeAnalyseType {
    
    PlatForm(1,"平台分析"),
    Enterprise(2,"企业分析"),
    District(3,"地区分析"),
    Product(4,"产品分析");
    
    
    private int code;
    
    private String name;


    private ChargeAnalyseType(int code, String name) {
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
