package com.cmcc.vrp.ec.bean.gd;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("EntSrvRegReq")
public class EntSrvRegReq {

    @XStreamAlias("OptCode")
    private String optCode;  //操作类型,0-新增，1-修改
    
    @XStreamAlias("EntCode")
    private String entCode;  //集团客户编码,对应集团开户时BOSS同步到ADC的客户编码。
    
    @XStreamAlias("EntName")
    private String entName; //EC企业名称
    
    @XStreamAlias("AreaCode")
    private String areaCode; //地市编码
    
    @XStreamAlias("SICode")
    private String siCode; //SI企业代码,对应SI基本信息同步对应的SICode
    
    @XStreamAlias("SIName")
    private String siName;  //SI名称,对应SI基本信息同步对应的SIName
    
    @XStreamAlias("PrdCode")
    private String prdCode; //产品编号,BOSS分配的主体产品编号,如果是产品包则是产品包编号
    
    @XStreamAlias("PrdOrdCode")
    private String prdOrdCode;  //集团产品号码,11位，标识集团订购关系，由 ‘2’字母开头＋2位地市代码＋2位产品代码＋6位自由编码 组成的。
    
    @XStreamAlias("PrdPkgInfo")
    private PrdPkgInfo prdPkgInfo; //产品包订购信息,主体产品的开通和变更时，如果该产品挂在产品包订购下则需要提供产品包订购信息?
    
    @XStreamAlias("AccessNo")
    private String accessNo; //业务基本接入号,短号,非短彩类产品可以无此节点?
    
    @XStreamAlias("ECAccessPort")
    private String ecAccessPort; //接入端口号,长号,非短彩类产品可以无此节点?
    
    @XStreamAlias("OrdRela")
    private String ordRela; //订购属性,0 - 定购关1 - 白名单2 - 黑名单修改的时候可不传?
    
    @XStreamAlias("Service")
    private Service service; //产品下的服务信息,主体产品时必须,产品包时可以无此节点?
    
    @XStreamAlias("StartEfft")
    private String startEfft; //生效时间,该订购的生效日期。订购生效日期到达之前，暂时不向企业提供业务YYYY-MM-DD HH:MM:SS
    
    @XStreamAlias("EndEfft")
    private String endEfft; //失效时间
    
    @XStreamAlias("PrdAdminUser")
    private String prdAdminUser; //产品管理员用户名
    
    @XStreamAlias("PrdAdminName")
    private String prdAdminName; //产品管理员姓名
    
    @XStreamAlias("PrdAdminMobile")
    private String prdAdminMobile;//产品管理员手机号码
    
    @XStreamAlias("PrdAdminMail")
    private String prdAdminMail; //产品管理员邮箱
    
    @XStreamAlias("ISTextSign")
    private String isTextSign; //是否支持短信/彩信正文签名,0－不支持1－支持（产品包开通时为空）修改时可不传?
    
    @XStreamAlias("DefaultSignLang")
    private String defaultSignLang; //缺省签名语言,1-中文2-英文修改时可不传?
    
    @XStreamAlias("TextSignEn")
    private String textSignEn; //英文短信/彩信正文签名,英文短信正文签名，修改时可不传?
    
    @XStreamAlias("TextSignZh")
    private String textSignZh;// 中文短信/彩信正文签名,中文短信正文签名，修改时可不传?
    
    @XStreamAlias("PrdChannel")
    private String prdChannel; //集团产品发展渠道
    
    @XStreamAlias("PrdDevUserId")
    private String prdDevUserId; //集团产品发展人工号
    
    @XStreamImplicit(itemFieldName="SubPrdInfo")
    private List<SubPrdInfo> subPrdInfo;

    public String getOptCode() {
        return optCode;
    }

    public void setOptCode(String optCode) {
        this.optCode = optCode;
    }

    public String getEntCode() {
        return entCode;
    }

    public void setEntCode(String entCode) {
        this.entCode = entCode;
    }

    public String getEntName() {
        return entName;
    }

    public void setEntName(String entName) {
        this.entName = entName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getSiCode() {
        return siCode;
    }

    public void setSiCode(String siCode) {
        this.siCode = siCode;
    }

    public String getSiName() {
        return siName;
    }

    public void setSiName(String siName) {
        this.siName = siName;
    }

    public String getPrdCode() {
        return prdCode;
    }

    public void setPrdCode(String prdCode) {
        this.prdCode = prdCode;
    }

    public String getPrdOrdCode() {
        return prdOrdCode;
    }

    public void setPrdOrdCode(String prdOrdCode) {
        this.prdOrdCode = prdOrdCode;
    }

    public PrdPkgInfo getPrdPkgInfo() {
        return prdPkgInfo;
    }

    public void setPrdPkgInfo(PrdPkgInfo prdPkgInfo) {
        this.prdPkgInfo = prdPkgInfo;
    }

    public String getAccessNo() {
        return accessNo;
    }

    public void setAccessNo(String accessNo) {
        this.accessNo = accessNo;
    }

    public String getEcAccessPort() {
        return ecAccessPort;
    }

    public void setEcAccessPort(String ecAccessPort) {
        this.ecAccessPort = ecAccessPort;
    }

    public String getOrdRela() {
        return ordRela;
    }

    public void setOrdRela(String ordRela) {
        this.ordRela = ordRela;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public String getStartEfft() {
        return startEfft;
    }

    public void setStartEfft(String startEfft) {
        this.startEfft = startEfft;
    }

    public String getEndEfft() {
        return endEfft;
    }

    public void setEndEfft(String endEfft) {
        this.endEfft = endEfft;
    }

    public String getPrdAdminUser() {
        return prdAdminUser;
    }

    public void setPrdAdminUser(String prdAdminUser) {
        this.prdAdminUser = prdAdminUser;
    }

    public String getPrdAdminName() {
        return prdAdminName;
    }

    public void setPrdAdminName(String prdAdminName) {
        this.prdAdminName = prdAdminName;
    }

    public String getPrdAdminMobile() {
        return prdAdminMobile;
    }

    public void setPrdAdminMobile(String prdAdminMobile) {
        this.prdAdminMobile = prdAdminMobile;
    }

    public String getPrdAdminMail() {
        return prdAdminMail;
    }

    public void setPrdAdminMail(String prdAdminMail) {
        this.prdAdminMail = prdAdminMail;
    }

    public String getIsTextSign() {
        return isTextSign;
    }

    public void setIsTextSign(String isTextSign) {
        this.isTextSign = isTextSign;
    }

    public String getDefaultSignLang() {
        return defaultSignLang;
    }

    public void setDefaultSignLang(String defaultSignLang) {
        this.defaultSignLang = defaultSignLang;
    }

    public String getTextSignEn() {
        return textSignEn;
    }

    public void setTextSignEn(String textSignEn) {
        this.textSignEn = textSignEn;
    }

    public String getTextSignZh() {
        return textSignZh;
    }

    public void setTextSignZh(String textSignZh) {
        this.textSignZh = textSignZh;
    }

    public String getPrdChannel() {
        return prdChannel;
    }

    public void setPrdChannel(String prdChannel) {
        this.prdChannel = prdChannel;
    }

    public String getPrdDevUserId() {
        return prdDevUserId;
    }

    public void setPrdDevUserId(String prdDevUserId) {
        this.prdDevUserId = prdDevUserId;
    }

    public List<SubPrdInfo> getSubPrdInfo() {
        return subPrdInfo;
    }

    public void setSubPrdInfo(List<SubPrdInfo> subPrdInfo) {
        this.subPrdInfo = subPrdInfo;
    }
   
}
