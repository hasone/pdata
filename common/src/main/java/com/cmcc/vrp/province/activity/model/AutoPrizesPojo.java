package com.cmcc.vrp.province.activity.model;

/**
 * -------------------
 * 奖品前缀
 * -------------------
 * 具体信息参见Active类
 *
 * @Author liuzengzeng
 * @Date 16/3/22 下午3:39
 *
 * 这个类有两种作用：
 * 1、用于请求生成活动对象类
 * 2、用于转化适应前端显示
 */
public class AutoPrizesPojo {
    private Integer idPrefix;

    private String rankName;

    private String cmccName;
    private String cmccEnterId;
    private String cmccId;
    private Integer cmccCount;
    private Integer cmccType;
    private Integer cmccResponse;

    private String cuccName;
    private String cuccEnterId;
    private String cuccId;
    private Integer cuccCount;
    private Integer cuccType;
    private Integer cuccResponse;

    private String ctccName;
    private String ctccEnterId;
    private String ctccId;
    private Integer ctccCount;
    private Integer ctccType;
    private Integer ctccResponse;

    private String otherName;
    private String otherEnterId;
    private String otherId;
    private Integer otherCount;
    private Integer otherType;
    private Integer otherResponse;

    private Integer count;

    private String probability;

    //extended 用于前端显示
    private String cmccProductCode;
    private Long cmccProductSize;
    private Long cmccPrice;
    private String cmccOwnershipRegion;
    private String cmccRoamingRegion;

    private String cuccProductCode;
    private Long cuccProductSize;
    private Long cuccPrice;
    private String cuccOwnershipRegion;
    private String cuccRoamingRegion;

    private String ctccProductCode;
    private Long ctccProductSize;
    private Long ctccPrice;
    private String ctccOwnershipRegion;
    private String ctccRoamingRegion;

    private String otherProductCode;
    private Long otherProductSize;
    private Long otherPrice;
    private String otherOwnershipRegion;
    private String otherRoamingRegion;





    public Integer getIdPrefix() {
        return idPrefix;
    }

    public void setIdPrefix(Integer idPrefix) {
        this.idPrefix = idPrefix;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public String getCmccName() {
        return cmccName;
    }

    public void setCmccName(String cmccName) {
        this.cmccName = cmccName;
    }

    public String getCmccEnterId() {
        return cmccEnterId;
    }

    public void setCmccEnterId(String cmccEnterId) {
        this.cmccEnterId = cmccEnterId;
    }

    public String getCmccId() {
        return cmccId;
    }

    public void setCmccId(String cmccId) {
        this.cmccId = cmccId;
    }

    public Integer getCmccCount() {
        return cmccCount;
    }

    public void setCmccCount(Integer cmccCount) {
        this.cmccCount = cmccCount;
    }

    public Integer getCmccType() {
        return cmccType;
    }

    public void setCmccType(Integer cmccType) {
        this.cmccType = cmccType;
    }

    public String getCuccName() {
        return cuccName;
    }

    public void setCuccName(String cuccName) {
        this.cuccName = cuccName;
    }

    public String getCuccEnterId() {
        return cuccEnterId;
    }

    public void setCuccEnterId(String cuccEnterId) {
        this.cuccEnterId = cuccEnterId;
    }

    public String getCuccId() {
        return cuccId;
    }

    public void setCuccId(String cuccId) {
        this.cuccId = cuccId;
    }

    public Integer getCuccCount() {
        return cuccCount;
    }

    public void setCuccCount(Integer cuccCount) {
        this.cuccCount = cuccCount;
    }

    public Integer getCuccType() {
        return cuccType;
    }

    public void setCuccType(Integer cuccType) {
        this.cuccType = cuccType;
    }

    public String getCtccName() {
        return ctccName;
    }

    public void setCtccName(String ctccName) {
        this.ctccName = ctccName;
    }

    public String getCtccEnterId() {
        return ctccEnterId;
    }

    public void setCtccEnterId(String ctccEnterId) {
        this.ctccEnterId = ctccEnterId;
    }

    public String getCtccId() {
        return ctccId;
    }

    public void setCtccId(String ctccId) {
        this.ctccId = ctccId;
    }

    public Integer getCtccCount() {
        return ctccCount;
    }

    public void setCtccCount(Integer ctccCount) {
        this.ctccCount = ctccCount;
    }

    public Integer getCtccType() {
        return ctccType;
    }

    public void setCtccType(Integer ctccType) {
        this.ctccType = ctccType;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public String getOtherEnterId() {
        return otherEnterId;
    }

    public void setOtherEnterId(String otherEnterId) {
        this.otherEnterId = otherEnterId;
    }

    public String getOtherId() {
        return otherId;
    }

    public void setOtherId(String otherId) {
        this.otherId = otherId;
    }

    public Integer getOtherCount() {
        return otherCount;
    }

    public void setOtherCount(Integer otherCount) {
        this.otherCount = otherCount;
    }

    public Integer getOtherType() {
        return otherType;
    }

    public void setOtherType(Integer otherType) {
        this.otherType = otherType;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getProbability() {
        return probability;
    }

    public void setProbability(String probability) {
        this.probability = probability;
    }

    public Integer getCmccResponse() {
        return cmccResponse;
    }

    public void setCmccResponse(Integer cmccResponse) {
        this.cmccResponse = cmccResponse;
    }

    public Integer getCuccResponse() {
        return cuccResponse;
    }

    public void setCuccResponse(Integer cuccResponse) {
        this.cuccResponse = cuccResponse;
    }

    public Integer getCtccResponse() {
        return ctccResponse;
    }

    public void setCtccResponse(Integer ctccResponse) {
        this.ctccResponse = ctccResponse;
    }

    public Integer getOtherResponse() {
        return otherResponse;
    }

    public void setOtherResponse(Integer otherResponse) {
        this.otherResponse = otherResponse;
    }

    public String getCmccProductCode() {
        return cmccProductCode;
    }

    public void setCmccProductCode(String cmccProductCode) {
        this.cmccProductCode = cmccProductCode;
    }

    public Long getCmccProductSize() {
        return cmccProductSize;
    }

    public void setCmccProductSize(Long cmccProductSize) {
        this.cmccProductSize = cmccProductSize;
    }

    public Long getCmccPrice() {
        return cmccPrice;
    }

    public void setCmccPrice(Long cmccPrice) {
        this.cmccPrice = cmccPrice;
    }

    public String getCmccOwnershipRegion() {
        return cmccOwnershipRegion;
    }

    public void setCmccOwnershipRegion(String cmccOwnershipRegion) {
        this.cmccOwnershipRegion = cmccOwnershipRegion;
    }

    public String getCmccRoamingRegion() {
        return cmccRoamingRegion;
    }

    public void setCmccRoamingRegion(String cmccRoamingRegion) {
        this.cmccRoamingRegion = cmccRoamingRegion;
    }

    public String getCuccProductCode() {
        return cuccProductCode;
    }

    public void setCuccProductCode(String cuccProductCode) {
        this.cuccProductCode = cuccProductCode;
    }

    public Long getCuccProductSize() {
        return cuccProductSize;
    }

    public void setCuccProductSize(Long cuccProductSize) {
        this.cuccProductSize = cuccProductSize;
    }

    public Long getCuccPrice() {
        return cuccPrice;
    }

    public void setCuccPrice(Long cuccPrice) {
        this.cuccPrice = cuccPrice;
    }

    public String getCuccOwnershipRegion() {
        return cuccOwnershipRegion;
    }

    public void setCuccOwnershipRegion(String cuccOwnershipRegion) {
        this.cuccOwnershipRegion = cuccOwnershipRegion;
    }

    public String getCuccRoamingRegion() {
        return cuccRoamingRegion;
    }

    public void setCuccRoamingRegion(String cuccRoamingRegion) {
        this.cuccRoamingRegion = cuccRoamingRegion;
    }

    public String getCtccProductCode() {
        return ctccProductCode;
    }

    public void setCtccProductCode(String ctccProductCode) {
        this.ctccProductCode = ctccProductCode;
    }

    public Long getCtccProductSize() {
        return ctccProductSize;
    }

    public void setCtccProductSize(Long ctccProductSize) {
        this.ctccProductSize = ctccProductSize;
    }

    public Long getCtccPrice() {
        return ctccPrice;
    }

    public void setCtccPrice(Long ctccPrice) {
        this.ctccPrice = ctccPrice;
    }

    public String getCtccOwnershipRegion() {
        return ctccOwnershipRegion;
    }

    public void setCtccOwnershipRegion(String ctccOwnershipRegion) {
        this.ctccOwnershipRegion = ctccOwnershipRegion;
    }

    public String getCtccRoamingRegion() {
        return ctccRoamingRegion;
    }

    public void setCtccRoamingRegion(String ctccRoamingRegion) {
        this.ctccRoamingRegion = ctccRoamingRegion;
    }

    public String getOtherProductCode() {
        return otherProductCode;
    }

    public void setOtherProductCode(String otherProductCode) {
        this.otherProductCode = otherProductCode;
    }

    public Long getOtherProductSize() {
        return otherProductSize;
    }

    public void setOtherProductSize(Long otherProductSize) {
        this.otherProductSize = otherProductSize;
    }

    public Long getOtherPrice() {
        return otherPrice;
    }

    public void setOtherPrice(Long otherPrice) {
        this.otherPrice = otherPrice;
    }

    public String getOtherOwnershipRegion() {
        return otherOwnershipRegion;
    }

    public void setOtherOwnershipRegion(String otherOwnershipRegion) {
        this.otherOwnershipRegion = otherOwnershipRegion;
    }

    public String getOtherRoamingRegion() {
        return otherRoamingRegion;
    }

    public void setOtherRoamingRegion(String otherRoamingRegion) {
        this.otherRoamingRegion = otherRoamingRegion;
    }

}
