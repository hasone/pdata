package com.cmcc.vrp.province.activity.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * -------------------
 * 奖品设置信息
 * -------------------
 * id           主键，自增
 * idPrefix     奖品前缀，从0开始计数，特等奖为0
 * rankName     奖品级别，特等奖、一等奖等
 * <p>
 * plateName    平台名称
 * enterpriseId 企业编码
 * enterpriseName   企业名称
 * activeName   活动名称
 * activeId     活动编码
 * <p>
 * cmccName     移动奖品名称
 * cmccEnterId  移动奖品提供者编码
 * cmccId       移动奖品编码
 * cmccCount    移动奖品数量，默认填写1，主要面向流量池奖品
 * cmccType     奖品类型
 * 0:流量池 | 1：流量包
 * cmccResponse 奖品兑换方式
 * 0:稍后兑换 | 1：直接兑换
 * <p>
 * cuccName     联通奖品名称
 * cuccEnterId  联通奖品提供者编码
 * cuccId       联通奖品编码
 * cuccCount    联通奖品数量，默认填写1，主要面向流量池奖品
 * cuccType     奖品类型
 * 0:流量池 | 1：流量包
 * cuccResponse 奖品兑换方式
 * 0:稍后兑换 | 1：直接兑换
 * <p>
 * ctccName     电信奖品名称
 * ctccEnterId  电信奖品提供者编码
 * ctccId       电信奖品编码
 * ctccCount    电信奖品数量，默认填写1，主要面向流量池奖品
 * ctccType     奖品类型
 * 0:流量池 | 1：流量包
 * ctccResponse 奖品兑换方式
 * 0:稍后兑换 | 1：直接兑换
 * <p>
 * otherName     其他奖品名称
 * otherEnterId  其他奖品提供者编码
 * otherId       其他奖品编码
 * otherCount    其他奖品数量，默认填写1，主要面向流量池奖品
 * otherType     奖品类型
 * 2:其他奖品，非流量
 * otherResponse 奖品兑换方式
 * 0:稍后兑换 | 1：直接兑换
 * <p>
 * count        奖品总数
 * probability  概率
 * deleteFlag   软删除标志
 *
 * @Author liuzengzeng
 * @Date 16/3/22 下午3:39
 */
public class Active implements Serializable {
    private static final long serialVersionUID = 2665420585067911420L;

    private Integer id;

    private String plateName;
    private String enterpriseId;
    private String enterpriseName;
    private String activeName;
    private String activeId;

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

    private Integer deleteFlag;

    /**
     * @param pojo
     * @return
     */
    public static List<Active> converToActives(AutoGeneratePojo pojo) {
        List<Active> activeList = new ArrayList<Active>();
        List<AutoPrizesPojo> autoPrizesPojoList = pojo.getPrizes();
        for (AutoPrizesPojo autoPrizesPojo : autoPrizesPojoList) {
            Active active = new Active();

            active.setPlateName(pojo.getPlateName());
            active.setEnterpriseId(pojo.getEnterpriseId());
            active.setEnterpriseName(pojo.getEnterpriseName());
            active.setActiveName(pojo.getActiveName());
            active.setActiveId(pojo.getActiveId());

            active.setIdPrefix(autoPrizesPojo.getIdPrefix());
            active.setRankName(autoPrizesPojo.getRankName());

            active.setCmccName(autoPrizesPojo.getCmccName());
            active.setCmccId(autoPrizesPojo.getCmccId());
            active.setCmccCount(autoPrizesPojo.getCmccCount());
            active.setCmccType(autoPrizesPojo.getCmccType());
            active.setCmccEnterId(autoPrizesPojo.getCmccEnterId());
            active.setCmccResponse(autoPrizesPojo.getCmccResponse());

            active.setCuccName(autoPrizesPojo.getCuccName());
            active.setCuccId(autoPrizesPojo.getCuccId());
            active.setCuccCount(autoPrizesPojo.getCuccCount());
            active.setCuccType(autoPrizesPojo.getCuccType());
            active.setCuccEnterId(autoPrizesPojo.getCuccEnterId());
            active.setCuccResponse(autoPrizesPojo.getCuccResponse());


            active.setCtccName(autoPrizesPojo.getCtccName());
            active.setCtccId(autoPrizesPojo.getCtccId());
            active.setCtccCount(autoPrizesPojo.getCtccCount());
            active.setCtccType(autoPrizesPojo.getCtccType());
            active.setCtccEnterId(autoPrizesPojo.getCtccEnterId());
            active.setCtccResponse(autoPrizesPojo.getCtccResponse());


            active.setOtherName(autoPrizesPojo.getOtherName());
            active.setOtherId(autoPrizesPojo.getOtherId());
            active.setOtherCount(autoPrizesPojo.getOtherCount());
            active.setOtherType(autoPrizesPojo.getOtherType());
            active.setOtherEnterId(autoPrizesPojo.getOtherEnterId());
            active.setOtherResponse(autoPrizesPojo.getOtherResponse());


            active.setDeleteFlag(0);
            active.setCount(autoPrizesPojo.getCount());
            active.setProbability(autoPrizesPojo.getProbability());
            activeList.add(active);
        }
        return activeList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlateName() {
        return plateName;
    }

    public void setPlateName(String plateName) {
        this.plateName = plateName;
    }

    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getActiveName() {
        return activeName;
    }

    public void setActiveName(String activeName) {
        this.activeName = activeName;
    }

    public String getActiveId() {
        return activeId;
    }

    public void setActiveId(String activeId) {
        this.activeId = activeId;
    }

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

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Integer getCtccResponse() {
        return ctccResponse;
    }

    public void setCtccResponse(Integer ctccResponse) {
        this.ctccResponse = ctccResponse;
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

    public Integer getOtherResponse() {
        return otherResponse;
    }

    public void setOtherResponse(Integer otherResponse) {
        this.otherResponse = otherResponse;
    }
}
