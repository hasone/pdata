package com.cmcc.vrp.boss.shangdong.boss.enums;

/**
 * 山东有的产品类型,当前是1087,1092,1099
 *
 */
public enum ShandongPrdEnum {
    PRD1087("1087","1087流量包产品",false),
    PRD1092("1092","1092流量包产品",false),
    PRD1099("1099","1099流量池产品",true);
    
    private ShandongPrdEnum(String prdType, String message, boolean useFlowSize) {
        this.prdType = prdType;
        this.message = message;
        this.useFlowSize = useFlowSize;
    }

    private String prdType; //类型prdType
    
    private String message;
    
    private boolean useFlowSize;//充值LimitFlow是否使用流量
    

    public String getPrdType() {
        return prdType;
    }

    public void setPrdType(String prdType) {
        this.prdType = prdType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isUseFlowSize() {
        return useFlowSize;
    }

    public void setUseFlowSize(boolean useFlowSize) {
        this.useFlowSize = useFlowSize;
    }
    
    /**
     * 判断充值的产品编码,类似108701,109201,109901，充值时是否传递FlowSize
     */
    public static boolean needUseFlowSize(String prdCode){
        
        for(ShandongPrdEnum prdEnum : ShandongPrdEnum.values()){
            if(prdCode.startsWith(prdEnum.getPrdType())){
                return prdEnum.isUseFlowSize();
            }
        }
        
        return false;
    }
    
}
