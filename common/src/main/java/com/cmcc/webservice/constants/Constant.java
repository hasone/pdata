package com.cmcc.webservice.constants;


/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午5:05:37
*/
public class Constant {

    /**
     * @param originalConstant
     * @return
     */
    public static String transferConstant(String originalConstant) {
        return originalConstant;
    }

    public enum RegionInfo {
        Default("571", "杭州"),

        HangZhou("571", "杭州"),
        HuZhou("572", "湖州"),
        JiaXing("573", "嘉兴"),
        NingBo("574", "宁波"),
        ShaoXing("575", "绍兴"),
        TaiZhou("576", "台州"),
        WenZhou("577", "温州"),
        LiShui("578", "丽水"),
        JinHua("579", "金华"),
        QuZhou("570", "衢州"),
        ZhouShan("580", "舟山"),;

        private String id;
        private String name;

        RegionInfo(String id, String name) {
            this.id = id;
            this.name = name;
        }

        static RegionInfo getRegionInfoById(String id) {
            RegionInfo[] infos = RegionInfo.values();
            for (RegionInfo info : infos) {
                if (info.getId().equals(id)) {
                    return info;
                }
            }
            return Default;
        }

        static RegionInfo getRegionInfoByName(String name) {
            RegionInfo[] infos = RegionInfo.values();
            for (RegionInfo info : infos) {
                if (info.getName().equals(name)) {
                    return info;
                }
            }
            return Default;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    public enum Bank {

        GH_ZJ(10011, "000030", "30"),    //工商银行浙江分行
        NH_ZJ(10012, "000040", "40"),    //农业银行浙江分行
        ZG_ZJ(10013, "000050", "50"),    //中国银行浙江分行
        JS_ZJ(10014, "000060", "60"),    //建设银行浙江分行
        JT_ZJ(10015, "11JW001", "JW"),    //交通银行浙江分行
        ZS_ZJ(10016, "00000A", "0A"),    //招商银行浙江分行
        YC_ZJ(10017, "0000Z0", "Z0"),    //邮政储蓄浙江分行
        HX_ZJ(10018, "0000A3", "A3"),    //华夏银行浙江分行
        ZFB(10019, "0000AP", "AP"),    //支付宝
        YLSW(10010, "000010", "10"),    //银联商务
        JL(10020, "0000JL", "JL"),    //捷蓝
        YCZ_O(20002, "11DX001", "DX"),    //原易充值
        YCZ_N(20003, "0000YC", "YC"),    //新易充值
        NZ_ZJ(10029, "0000NZ", "NZ"),    //宁波中行（生产）
        ZFJD(10066, "11MO001", "MO"),   //手机支付基地（和包平台）
        CFT(10067, "0000TP", "TP"),    //财付通
        OTHER(10999, "QT", "QT"), //其它银行
        ;

        private int id;
        private String code;
        private String type;

        Bank(int id, String code, String type) {
            this.id = id;
            this.code = code;
            this.type = type;
        }

        public int getId() {
            return id;
        }

/*		public String getCode() {
            return code;
		}*/

        public String getType() {
            return type;
        }

    }


    public enum PlatForm {

        YH(10001, "01"),    //银行
        YYT(10002, "02"),   //营业厅
        KF(10003, "03"),    //客户
        IVR(10004, "04"),   //IVR
        WY(10005, "05"),    //网上营业厅
        DT(10006, "06"),    //短信营业厅
        WAP(10007, "07"),   //WAP营业厅
        ZD(10008, "08"),    //终端管理平台
        QD(10009, "09"),    //渠道管理平台
        YLSW(10010, "10"),    //银联商务
        GH_ZJ(10011, "30"),    //工商银行浙江分行
        NH_ZJ(10012, "40"),    //农业银行浙江分行
        ZG_ZJ(10013, "50"),    //中国银行浙江分行
        JS_ZJ(10014, "60"),    //建设银行浙江分行
        JT_ZJ(10015, "JW"),    //交通银行浙江分行
        ZS_ZJ(10016, "0A"),    //招商银行浙江分行
        YC_ZJ(10017, "Z0"),    //邮政储蓄浙江分行
        HX_ZJ(10018, "A3"),    //华夏银行浙江分行
        ZFB(10019, "AP"),    //支付宝
        JL(10020, "JL"),    //捷蓝
        APP_CX(10021, "M0"), //手机客户端(彩讯)
        /*		YCZ_O(20002, "DX"),    //原易充值
                YCZ_N(20003, "YC"),    //新易充值
        */
        ZFJD(10066, "MO"),  //手机支付基地（和包平台）

        DHJL(10022, "11"), //电话经理

        CRM(10091, "91"),   //CRM
        BOSS(10092, "92"),   //BOSS
        DXPT(10093, "93"),   //短信平台
        OTHER(10099, "00"),  //其它

        ESOP(10200, "12"),   //ESOP
        MESOP(10201, "13"),   //MESOP
        JCGL(10202, "14"),   //基础管理平台

        UPG(99999, null),//支付网关，现未定义
        ;

        private int id;
        private String code;

        PlatForm(int id, String code) {
            this.id = id;
            this.code = code;
        }

        public int getId() {
            return id;
        }

        public String getCode() {
            return code;
        }
    }


    //银行通用模板
    public enum GeneTemplate {

        ZH_CJ("1379929974849", "ZHOWE"),    //招商银行易充值催缴短信
        ZH_TY("1379930004589", "ZHMSG"),   //招行银行易充值通用短信
        ;

        private String id;
        private String key;

        GeneTemplate(String id, String key) {
            this.id = id;
            this.key = key;
        }

        static GeneTemplate getGeneTemplateById(String id) {
            GeneTemplate[] templates = GeneTemplate.values();
            for (GeneTemplate template : templates) {
                if (template.getId().equals(id)) {
                    return template;
                }
            }
            return null;
        }

        public String getId() {
            return id;
        }

        public String getKey() {
            return key;
        }
    }


    public enum PubErrorCodes {

        SUCCESS("0000", "成功"), PROTOCALERR("0001", "通讯报文错"), PROTOCALVERSIONERR(
            "0002", "协议版本错"), SYSTEMERR("0003", "系统错"), IOERR("0004",
            "读取文件错"), CREDITILLEGAL("0010", "信用卡无效"), AUTHENTICATEERR(
            "0011", "持卡人身份证信息不匹配"), CREDENTIALNUMFORMATERRERR("0012",
            "证件号码输入错误"), CREDENTIALTYPEERR("0013", "证件类型不符"), MOBILENUMERR(
            "0014", "手机号码不符"), ACCOUNTERR("0015", "核对户名不相符"), CREDENTIALNUMERRERR(
            "0016", "证件号不正确"), MESSAGEDIFFERERR("0017", "信息不一致"), CHECKOUTOFLIMITERR(
            "0018", "手机号核对次数超过上限"), REQUESTILLEGALERR("0019", "申请的数据无效"), CONTRACTNOTEXISTS(
            "0020", "签约关系不存在"), AMOUNTNOTMATCHERR("0050", "金额不符"), ACCOUNTAMOUNTNOTENOUPHERR(
            "0051", "借方帐户余额不足"), BANKAMOUNTNOTENOUPHERR("0052", "帐户余额不足"), CREDITOUTOFLIMITERR(
            "0053", "卡片信用额度超限"), DAYOUTOFLIMITERR("0054", "日累计支付金额超限"), MOUNTHOUTOFLIMITERR(
            "0055", "月累计支付金额超限"), YEAROUTOFLIMITERR("0056", "年累计支付金额超限"), TOTALOUTOFLIMITERR(
            "0057", "支付金额超过最高累计限额"), NOTENTRUSTERR("0100", "尚未办理委托"), UNSUPPORTGUESTERR(
            "0101", "本业务不支持非签约客户"), ACCOUNTALREADYCLOSEERR("0102",
            "该用户委托已销户"), ACCOUNTNOTMATCHERR("0103", "扣款帐号与委托账号不一致"), SAMEFLOWNOPROCESSINGERR(
            "0104", "相同公司流水号交易正在处理"), ANOTHERORDEREXISTSERR("0105",
            "已存在不同金额的同一订单号"), ORDERALREADYPAYERR("0106", "该订单已支付成功"), OLDSUCCESSERR(
            "0107", "原交易已成功"), OLDINCORRECTERR("0108", "原交易不正确"), OLDNOTEXISTERR(
            "0109", "原交易不存在"), VERIFYCODEEXPIRE("0110", "验证码已失效 "), VERIFYCODEINVALID(
            "0111", "验证码核对不通过"), DONECODEDUPLICATE("0112", "重复的流水号"), DONECODENOTEXISTS(
            "0113", "没有公司方流水号"), PROVIDERCODENOTEXISTS("0114", "平台商编号未设置"), UNKNOWNERR(
            "9999", "未知异常");

        private String code;
        private String message;

        PubErrorCodes(String code, String message) {
            this.code = code;
            this.message = message;
        }

        static PubErrorCodes getPubErrorCodeByCode(String code) {
            PubErrorCodes[] codes = PubErrorCodes.values();
            for (PubErrorCodes icode : codes) {
                if (icode.getCode().equals(code)) {
                    return icode;
                }
            }
            return SYSTEMERR;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }


    /**
    * <p>Title: </p>
    * <p>Description: </p>
    * @author lgk8023
    * @date 2017年1月22日 下午5:05:58
    */
    public interface ParamType {
        public static final int PARAM_TYPE_IN = 0;
        public static final int PARAM_TYPE_OUT = 1;
    }


    /**
    * <p>Title: </p>
    * <p>Description: </p>
    * @author lgk8023
    * @date 2017年1月22日 下午5:06:05
    */
    public interface Common {
        public static final String EMPTY = "";
        public static final String POINT = ".";
        public static final String GREAT = ">";
        public static final String GREAT_CODE = "&gt;";
        public static final String LOW = "<";
        public static final String LOW_CODE = "&lt;";
        public static final String LINE = "/";

        public static final int MAX_LISTID = 50;
        public static final int MAX_REMARKS_LENGTH = 255;

        public static final String V1 = "1.00";
    }

    //报文的公共信息
    /**
    * <p>Title: </p>
    * <p>Description: </p>
    * @author lgk8023
    * @date 2017年1月22日 下午5:06:10
    */
    public interface PublicInfo {
        public static final String ADVPAY = "AdvPay";//业务平台根节点
        public static final String PUB_INFO = "PubInfo";
        public static final String BUSI_DATA = "BusiData";

        public static final String VERSION = "Version";//协议版本
        public static final String TRANSACTION_ID = "TransactionId";//交易流水
        public static final String TRANSACTION_DATE = "TransactionDate";//交易时间
        public static final String BUSI_CODE = "BusiCode";//业务类型
        public static final String ORIGIN_ID = "OriginId";//平台编号
        public static final String REGION_ID = "RegionId";//地区编号
        public static final String COUNTY_ID = "CountyId";//县市编号
        public static final String RETURN_CODE = "ReturnCode";//返回编码
        public static final String RETURN_MSG = "ReturnMsg";//返回信息
        public static final String VERIFY_CODE = "VerifyCode";//验证码
        public static final String ORG_ID = "OrgId";//操作员组织编号
        public static final String OPER_ID = "OperId";//操作员编号
        public static final String CHARGE_DETAIL = "ChargeDetail";//订单细节

        public static final String CREATE_TIME = "CreateTime";//
        public static final String ENTERPRISE_CODE = "EnterpriseCode";//企业编码
        public static final String PRODUCT_CODE = "ProductCode";//产品编码
        public static final String CHARGE_PHONE_NUM = "ChargePhoneNum";//充值电话
        public static final String CHARGE_TIME = "ChargeTime"; //充值时间
        public static final String ORDER_STATUS = "OrderStatus"; //订单状态
        public static final String FAILURE_REASSON = "FailureSeason"; //订单相关信息


        public static final String PRODUCTS_ELEMENT = "Products";   //<Products>标签
        public static final String PRODUCT_ELEMENT = "Product";   //<Product>标签
        public static final String PRODUCT_STATUS = "ProductStatus";   //产品状态
        public static final String RESTE_NUM = "ResteNum";  //流量包剩余数量


        public static final String Package = "Package";//银行报文根节点
        public static final String BANK_ID = "BankId";//银行编号
        public static final String AGENT_BANK_ID = "AgentBankId";//代理银行编号
    }


    //加入到pubinfo中的字段，组装报文前要删掉
    /**
    * <p>Title: </p>
    * <p>Description: </p>
    * @author lgk8023
    * @date 2017年1月22日 下午5:06:14
    */
    public interface PUBINFOTMP {
        public static final String BUSI_ID = "Tmp_BusiId";//业务编号
        public static final String BUSI_CODE = "Tmp_BusiCode";//业务编码
        public static final String PLATFORM_ID = "Tmp_PlatformId";//平台编号
        public static final String PLATFORM_CODE = "Tmp_PlatformCode";//平台编号
        public static final String ORDER_ID = "Upg_OrderId";//订单编号

        public static final String RET_CODE = "Tmp_CODE";//返回编码
        public static final String RET_MESSAGE = "Tmp_MESSAGE";//返回信息
    }


    //业务平台报文的业务参数
    /**
    * <p>Title: </p>
    * <p>Description: </p>
    * @author lgk8023
    * @date 2017年1月22日 下午5:06:18
    */
    public interface PARAMETER {
        //关键字段
        public static final String ACCOUNT_TYPE = "AccountType";//业务帐号类型
        public static final String ACCOUNT_CODE = "AccountCode";//业务帐号
        public static final String ACCOUNT_NAME = "AccountName";//业务帐号名称
        public static final String AGREEMENT_TYPE = "BindType";//签约类型
        public static final String AGREEMENT_ID = "ContractNo";//协议编号
        public static final String PAYITEMTYPE = "PayItemType";//业务编号


        //卡面信息
        public static final String BANKCODEID = "BankCodeId";//银行编码
        public static final String BANK_CODE = "BankCode";//银行编码
        public static final String BANK_ID = "BankId";//银行编码
        public static final String BANK_CARD_CUST_NAME = "BankCustName";//银行卡户名
        public static final String BANK_CERT_CARD_TYPE = "CardType";//银行证件类型
        public static final String BANK_CERT_CARD_CODE = "CardId";//银行证件号码
        public static final String BANK_CARD_TYPE = "BankCardType";//银行卡类型
        public static final String BANK_CARD_NO = "BankCardNo";//银行卡号
        public static final String EXPIRE_DATE = "ExpireDate";//失效时间
        public static final String CVV2 = "SafeCode";//CVV2
        public static final String BANK_BIND_BILLID = "ExtMobile";//银行预留手机号
        public static final String INSTALLMENT_NUM = "InstallmentNum";//银行预留手机号(分期期数)
        public static final String INSTALLMENT_RATE_TYPE = "InstallmentRateType";//银行预留手机号

        //协议要素
        public static final String BIND_BILLID = "BindBillId";//绑定手机号
        public static final String IS_AUTO_PAY = "IsAutoPay";//是否自动充值
        public static final String TRIGGER_VALUE = "TriggerValue";//自动充值阀值
        public static final String AUTO_AMOUNT = "AutoAmount";//自动充值金额
        public static final String EXT_BILLID = "ExtBillId";//副号码

        //具体业务涉及参数
        public static final String Order_Fee = "PayAmount";//支付金额
        public static final String CHECK_CODE = "CheckCode";//验证码
        public static final String PAY_PASSWORD = "PayPassword";//支付密码
        public static final String OPER_TYPE = "OperType";//操作类型(变更签约协议要素)
        public static final String Pay_Mod = "PayMod";//支付模式，主动支付/自动支付
        public static final String PAY_INFO = "PayInfo";//支付订单明细
        public static final String GOODS_NAME = "GoodsName";//商品名称
        public static final String IS_CHECK = "IsCheck";//是否校验验证码
        public static final String CHECKCODE = "CheckCode";//验证码
        public static final String TRANSACTION_ID_SMS = "TransactionIdSMS";//获取验证码交易流水号
        public static final String IS_INSTALLMENT = "IsInstallment";//是否分期付款
        public static final String INSTALLMENT_BANKID = "InstallmentBankId";//分期银行编号
        public static final String USER_RATE = "UserRate";//用户承担费率
        public static final String USER_AMOUNT = "UserAmount";//用户承担费用
        public static final String MERCHANT_RATE = "MerchantRate";//商户承担费率
        public static final String MERCHANT_AMOUNT = "MerchantAmount";//商户承担费用
        public static final String PRODUCT_NAME = "ProductName";//商品名称
        public static final String PRODUCT_ID = "ProductId";//商品编号
        public static final String PRODUCT_CODE = "ProductCode";//商品编码
        public static final String PRODUCT_DESC = "ProductDesc";//商品说明
        public static final String PRODUCT_NUM = "ProductNum";//商品数量
        public static final String PAY_PERIOD = "PayPeriod";//支付有效期

        //对账业务参数
        public static final String START_DATE = "StartDate";//开始时间
        public static final String END_DATE = "EndDate";//结束时间
        public static final String CHECK_DATE = "CheckDate";//清算日期
        public static final String COUNT = "Count";//总笔数
        public static final String AMOUNT = "Amount";//总金额
        public static final String FILE_NAME = "FileName";//文件名
        public static final String Mobile = "Mobile";//手机号
        public static final String BIND_MODE = "BindMode";//手机号CheckDate
        //修改支付密码业务参数
        public static final String OLD_PASSWORD = "OldPassword";//旧的支付密码
        public static final String NEW_PASSWORD = "NewPassword";//旧的支付密码

    }

    /**
    * <p>Title: </p>
    * <p>Description: </p>
    * @author lgk8023
    * @date 2017年1月22日 下午5:06:23
    */
    public interface BUSI_ID {//业务编号
        public static final int AGREEMENT_ADD = 100001;//实时签约（业务平台发起）
        public static final int AGREEMENT_UPDATE = 100002;//签约变更（业务平台发起）
        public static final int AGREEMENT_CANCEL = 100003;//签约取消（业务平台发起）
        public static final int AGREEMENT_QUERY = 100007;//签约查询（包括快捷）（业务平台发起）
        public static final int AGREEMENT_PAY = 100008;//签约支付（业务平台发起）
        public static final int PAY_QUERY = 100010;//支付查询（业务平台发起）
        public static final int PAY_DETAIL = 100011;//支付明细（业务平台发起）
        public static final int CHECK_ACCOUNT = 100012;//对账（业务平台发起）
        public static final int CHECK_ACCOUNT_QUERY = 100013;//对账结果查询（业务平台发起）
        public static final int PASSWORD_CHANGE = 100014;//变更支付密码（业务平台发起）

        public static final int DT_YCZ = 100015;//短信易充值短信处理（短厅发起）
        public static final int BOSS_PayNotify = 100016;//催缴通知（BOSS发起）

        public static final int BANK_CUST_STATE_QUERY = 200001;//用户状态查询（银行发起）
        public static final int BANK_AGREEMENT_ADD = 200002;//实时签约（银行发起）
        public static final int BANK_AGREEMENT_CANCEL = 200003;//实时解约（银行发起）
        public static final int BANK_AGREEMENT_QUERY = 200004;//异步签约（银行发起）--其实是查询签约数据
        public static final int BANK_AGREEMENT_BATCH_ADD = 200005;//批量签约（银行发起）
        public static final int BANK_AGREEMENT_BATCH_CANCEL = 200006;//批量解约（银行发起）
        public static final int BANK_PAY_NOTICE = 200007;//支付通知（银行发起）
        public static final int BANK_RECHARGE = 200008;//话费充值（银行发起）
        public static final int BANK_CHECK_ACCOUNT = 200009;//对帐（银行发起）
        public static final int BANK_SEND_MSG = 200010;//下发短信（银行发起）


    }

    /**
    * <p>Title: </p>
    * <p>Description: </p>
    * @author lgk8023
    * @date 2017年1月22日 下午5:06:27
    */
    public interface BUSI_CODE {

        //页面
        public static final String UnionBindWeb = "5001";//统一签约(页面)
        public static final String BindCenterWeb = "5002";//签约中心(页面)
        public static final String UnionPayWeb = "5003";//统一支付(页面)
        public static final String PayResultQueryWeb = "5004";//支付查询（页面）
        public static final String BankSendMsg = "2010";//下发短信（银行发起）
        public static final String PayResultNotifyWeb = "8021";//支付通知（页面）

        //业务平台、CRM/BOSS等发起
        public static final String BindOnline = "3001";//签约
        public static final String BindChange = "3003";//签约变更
        public static final String BindCancel = "3002";//签约取消
        public static final String BindQuery = "3004";//签约查询
        public static final String PayOnline = "3005";//签约支付
        public static final String PayResultQuery = "3006";//支付查询
        public static final String PayListQuery = "3007";//扣款明细查询
        public static final String PayPasswordModify = "3008";//修改支付密码
        public static final String Check = "3009";//对帐
        public static final String CheckQuery = "3010";//对帐结果查询

        public static final String ShortMsgProcess = "3013";//上行短信处理
        public static final String UserChangeNotify = "3011";//销户、过户数据同步
        public static final String PayNotify = "3012";//催缴通知
        public static final String OrderReFund = "3018";//退款

        public static final String TwoDimensionalCode = "3019";//生成二维码(业务平台发起)
        public static final String CreatTwoDimensionalCode = "1026";//生成二维码（向二维码平台）
        public static final String AyalyTwoDimensionalCode = "1027";//解析二维码（向二维码平台）


        //银行发起
        public static final String BankCustStateQuery = "2001";//用户状态查询（银行发起）
        public static final String BankBindOnline = "2002";//实时签约（银行发起）
        public static final String BankBindCancel = "2003";//实时解约（银行发起）
        public static final String BankBindQuery = "2004";//异步签约（银行发起）--其实是查询签约数据
        public static final String BankBindBatchAdd = "2005";//批量签约（银行发起）
        public static final String BankBindBatchCancel = "2006";//批量解约（银行发起）
        public static final String BankPayNotice = "2007";//支付通知（银行发起）
        public static final String BankRecharge = "2008";//充值缴费（银行发起）
        public static final String BankCheck = "2009";//对帐（银行发起）


        //临时定义
        public static final String QuickPayBind = "QuickPayBind";//快捷支付绑定
        public static final String QuickPayBindCancel = "QuickPayBindCancel";//快捷支付解绑
        public static final String QuickPay = "QuickPay";//快捷支付
        public static final String GetCheckCode = "GetCheckCode";//获取校验码

    }

    /**
    * <p>Title: </p>
    * <p>Description: </p>
    * @author lgk8023
    * @date 2017年1月22日 下午5:07:00
    */
    public interface BusiCode2Crm {
        /**
         * 
         */
        public static final String QueryCustInfo = "000001";//客户信息查询
        public static final String Charge = "CZ";//充值
        public static final String OrderProduct = "104002";//产品订购，退订
        public static final String SyncBindInfo = "QYTB";//同步签约数据
        public static final String CheckStatement = "SQDZ";//对账申请
        public static final String PayResultNotify = "ZFTZ";//支付通知
        public static final String CHARGE_FP = "DYFP";//充值打印发票
    }

    /**
    * <p>Title: </p>
    * <p>Description: </p>
    * @author lgk8023
    * @date 2017年1月22日 下午5:07:04
    */
    public interface BusiCode2BusiPlat {
        public static final String ShortMsgSend = "4001";//短信下发
        public static final String PayResultNotify = "4002";//支付通知
        public static final String BindResultNotify = "4003";//签约通知
        public static final String PayResultNotifyWeb = "6001";//页面通知
    }

    /**
    * <p>Title: </p>
    * <p>Description: </p>
    * @date 2017年1月22日 下午5:07:07
    */
    public interface BusiCode2Bank {
        public static final String CheckBankCard = "1001";//银行卡资料校验
        public static final String getVerify = "1002";//获取验证码
        public static final String CheckVerify = "1003";//校验验证码
        public static final String RandomPay = "1004";//随机扣款
        public static final String RandomPayCheck = "1005";//随机扣款验证
        public static final String BindAdd = "1006";//实时签约
        public static final String BindCancel = "1007";//签约取消
        public static final String BindChange = "1008";//签约变更
        public static final String BindQuery = "1009";//签约查询
        public static final String PaySync = "1010";//在线支付
        //		public static final String PayAsyn = "1011";//异步支付
        public static final String QuickPay = "1012";//快捷支付
        public static final String OweNotice = "1013";//催缴通知
        public static final String PayQuery = "1014";//支付查询
        public static final String PayDetail = "1015";//扣款明细
        public static final String ShortMessageUp = "1020";//上行短信

        public static final String NetPay = "8001";//网银支付
        public static final String NetPayQuery = "8002";//网银支付查询

    }

    public interface StaticDataCodeType {
        public static final String CALLED = "CALLED";//上行短信接口接入号
        public static final String NoDisturbTime = "NoDisturbTime";//免打扰时间
        public static final String HttpCodeReturn = "HttpCodeReturn";//反向接口机
        public static final String HttpCode4PayNotice = "HttpCode4PayNotice";//支付通知的业务平台的http编码
        public static final String HttpCode4BindNotice = "HttpCode4BindNotice";//签约通知的业务平台的http编码
        public static final String ChargeBankCode = "ChargeBankCode";//充值的银行编号
        //		public static final String OweRemindSwitch = "OweRemindSwitch";//催缴通知的开关
        public static final String TestNumSwicth = "TestNumSwicth";//测试号码的开关(如果开，只有配置的测试号码可以办理业务)
        public static final String BusiCodeMapping = "BusiCodeMapping";//业务编码映射
        public static final String IsCheckSign = "IsCheckSign";//(平台)是否校验签名
        public static final String IsCheckCustName = "IsCheckCustName";//(平台)是否进行实名校验  wuxuan add
        public static final String AgreementType = "AgreementType";//协议类型
        public static final String OperType = "OperType";//操作类型
        public static final String PlatformRoutePath = "PLATFORM_FILE_PATH";//平台路径资源
        public static final String TEMPLATE = "TEMPLATE";//短信模板
        public static final String EXPIRE_TIME = "EXPIRE_TIME";//失效时间
        public static final String SMS_CONTENT_LIMIT_ = "SMS_CONTENT_LIMIT_";//短信内容限制
        public static final String SWITCH = "SWITCH";//开关
        public static final String BIND_PAYITEM_MAPPING = "BIND_PAYITEM_MAPPING";//签约类型和(业务类型+平台)的映射关系
        public static final String PAY_ITEM_TYPE = "PAY_ITEM_TYPE";//业务类型
        public static final String BANK_CODE_MAPPING = "BANK_CODE_MAPPING";//银行返回的银行编码与支付网关的映射关系
        public static final String CHARGE_RATE_RANGE = "CHARGE_RATE_RANGE";//充值优惠比例
        public static final String FILE_TYPE_MAPPING = "FILE_TYPE_MAPPING";//文件类型映射
        public static final String CHECK_TYPE = "CHECK_TYPE";//(银联商务支付时指定)验证类型
        public static final String HINT_AUTO_PAY_SMS = "HINT_AUTO_PAY_SMS";//(催缴时)提示开通自动充值短信的配置信息
        public static final String CHARGE_LIMIT = "CHARGE_LIMIT";//充值限制(金额等)
        public static final String SMS_ERROR_MSG_HINT = "SMS_ERROR_MSG_HINT";//短信内容中的错误信息映射
        public static final String NOTICE_ENCODE = "NOTICE_ENCODE";//后台通知业务平台的
        public static final String IS_CHECK_ACCOUNT_CODE = "IS_CHECK_ACCOUNT_CODE";//是否校验业务帐号
        public static final String BANK_CARD_LIMIT_BIND_COUNT = "BANK_CARD_LIMIT_BIND_COUNT";//银行卡能绑定次数
        public static final String OPENDIALOG = "OPENDIALOG";//是否弹出易充值签约支付对话框
        public static final String BIND_PAYITEM_PAGE = "BIND_PAYITEM_PAGE";//统一支付页面tab页和(业务类型+平台)的映射关系
        public static final String CRM_OP_ID = "CRM_OP_ID";//统一支付网关与Esb平台的映射关系
        public static final String HTTP_TD_URL = "HTTP_TD_URL";//提供给二维码平台调用的服务
        public static final String LOG_IMAGE = "LOG_IMAGE";//二维码图片流
        public static final String BIND_PAYITEM_BANK = "BIND_PAYITEM_BANK";//统一支付页面可支付银行和（payitemtype+平台)的映射关系
        public static final String HTTP_WAP_PLATFORM = "HTTP_WAP_PLATFORM_ACCESS";//业务平台接入支付界面展示
        public static final String SILVER_SUPPLIERS_LIST = "SILVER_SUPPLIERS_LIST";//页面网银列表
        public static final String BIND_PAYITEM_NETBANK = "BIND_PAYITEM_NETBANK";//页面网银列表
    }


    public interface StaticDataCodeValue {
        //短信模板相关
        public static final String BIND_SUCCESS = "BIND_SUCCESS_";
        public static final String YCZ_GENERAL = "YCZ_GENERAL_";//银行反向易充值下行短信
        public static final String SMS_YCZ_HINT_CHARGE = "SMS_YCZ_HINT_CHARGE";//(催缴时)短信提示开通自动充值

        //失效时间相关
        public static final String PAY_BANK_J1 = "PAY_BANK_J1";
        public static final String PAY_DEFAULT = "PAY_DEFAULT";
        public static final String PAY_ = "PAY_";
        public static final String PRE_BIND = "PRE_BIND";
        public static final String SMS_YCZ_BIND = "SMS_YCZ_BIND";
        public static final String SMS_YCZ_PAY = "SMS_YCZ_PAY";
        public static final String ZD_CHECK_PAY = "ZD_CHECK_PAY";
        public static final String ZD_WAIT_CHECK_PAY = "ZD_WAIT_CHECK_PAY";

        //开关
        public static final String SMS_CONTENT_LIMIT_ = "SMS_CONTENT_LIMIT_";//短信内容限制开关
        public static final String PAY_NOTICE_2_PLAT = "PAY_NOTICE_2_PLAT_";//向业务平台发送支付通知

        //(催缴时)提示开通自动充值短信的配置信息相关
        public static final String PERIOD = "PERIOD";//签约时间段
        public static final String COUNTS = "COUNTS";//次数


    }

    public interface Switch {//开关
        public static final String open = "1";//开
        public static final String close = "0";//关
    }

    public interface PayNoticeResult {
        public static final String SUCCESS = "SUCCESS";//成功
        public static final String FAIL = "FAIL";//失败
    }

    public interface CheckSignFlag {
        public static final String YES = "1";//校验
        public static final String NO = "0";//不校验
    }

    //wuxuan add
    public interface CheckCustNameFlag {
        public static final String YES = "1";//校验
        public static final String NO = "0";//不校验
    }

    public interface PayMenthod {
        public static final int ContractPay = 1;//签约支付
        public static final int NetPay = 2;//网银支付
        public static final int QuickPay = 3;//快捷支付
    }

    /**
     * 签约类型
     *
     * @author Administrator
     */
    public interface BINDMODE {
        public static final int ZQY = 0;//正式签约
        public static final int QY = 1; //预签约
    }

    public interface Bind_Mode {

        public static final int YWQY = 1;//业务平台正向正式签约
        public static final int YWYQY = 2; //业务平台正向预签约

        public static final int YHQY = 0;//银行反向正式签约
        public static final int YHYQY = 1; //银行反向预签约
    }

    public interface TmpBusiDataType {
        public static final int TEST_PAY = 1;//验证扣款记录
        public static final int SAVE_DATA = 2;//页面保存数据
        public static final int PRE_CONTRACT = 3;//预签约保存数据(和页面保存数据类似)
        public static final int SMS_YCZ_CARDNO = 4;//短厅易充值签约时待用户回复银行卡号
        public static final int SMS_YCZ_CCINFO_BIND = 5;//短厅易充值签约时待用户回复信用卡信息（有效期和CVV2）
        public static final int SMS_YCZ_CCINFO_HK = 6;//短厅易充值换卡时待用户回复信用卡信息（有效期和CVV2）
        public static final int SMS_YCZ_HINT_AUTO_COUNTS = 7;//记录(催缴时)下发推荐开通自动充值短信的次数
        public static final int SMS_DHJL_WAIT_REPLY = 8;//电话经理预缴下发短信，等待客户回复
        public static final int SMS_DHJL_WAIT_CARDNO = 9;//电话经理预缴客户回复的卡号为信用卡，信用卡信息(有效期和CVV2)
        public static final int SMS_DHJL_CHANGE_BJ = 10;//电话经理预缴客户修改为扣本金支付
    }

    public interface AGREEMENTTYPE {
        public static final int ZD = 5; //终端支付
        public static final int QDZJGJ = 6;//渠道资金归集
        public static final int YCZ = 7;//短信易充值
        public static final int ZDZ = 8;//总对总
        public static final int KJZF = 9;//快捷支付
        public static final int QDHJK = 10;//渠道回缴款
    }


    public interface ACCOUNTTYPE {
        public static final int BILLID = 1;//手机号
        public static final int CHANNEL = 3;//渠道商编号
        public static final int BROADBAND = 2;//宽带帐号(备用)
        public static final int EMAIL = 4;//E-mail(备用)
    }

    public interface ELEMENT_DEF_ID {
        public static final int BANK_CARD = 1000;//绑定银行卡
        public static final int ACCOUNT_ID = 1001;//业务账号
        public static final int AUTO_STATUS = 1002;//是否自动充值
        public static final int LIMITE_AMOUNT = 1003;//自动充值阀值
        public static final int AUTO_AMOUNT = 1004;//自动充值金额
        public static final int DAILY_LIMIT = 1005;//日支付限额
        public static final int MONTHLY_LIMIT = 1006;//月支付限额
        public static final int SINGLE_PAYMENT_MAX = 1007;//单笔支付限额
        public static final int SINGLE_PAYMENT_MIN = 1008;//单笔支付限额
        public static final int BIND_BILLID = 1009;//绑定手机号码
        public static final int EXT_BILLID = 1010;//副号码
        public static final int BIND_BANK_TYPE = 1011;//绑定银行类型（对公，对私）
        public static final int YL_BILLID = 1012;//银行预留手机号码
    }

    public interface STATE {//状态
        public static final String U = "U";//成功
        public static final String E = "E";//失败

        public static final String M = "M";

        public static final String I = "I";//只对帐 未调账数据
    }

    public interface RECSTATE {//订单状态
        public static final String I = "1";//处理中
        public static final String U = "2";//成功
        public static final String E = "3";//失败
        public static final String S_5 = "5";//超时订单
        public static final String W = "6";//支付等待[异步支付]

        public static final String X = "20";//支付成功，充值失败

        public static final String V1 = "7";//银行校验卡面信息成功
        public static final String V2 = "8";//银行签约成功
        public static final String V3 = "9";//捷蓝校验验证码成功
        public static final String V4 = "10";//CRM产品订购成功
        public static final String V5 = "11";//网关保存签约关系成功
        public static final String V6 = "12";//网关设置初始支付密码成功
        public static final String V7 = "13";//网关下发短信成功
        public static final String V8 = "14";//银行变更签约关系成功
        public static final String V9 = "15";//银行解约成功
        public static final String VA = "16";//CRM产品退订成功
        public static final String VB = "17";//银行扣款成功
        public static final String VC = "18";//查询签约关系成功
        public static final String VD = "19";//19:发送联动优势催缴工单
        public static final String VE = "20";//20:支付成功，充值失败
        public static final String VF = "21";//首次收到银行支付通知
    }

    public interface LogState {//业务日志状态
        public static final String U = "1";//成功
        public static final String E = "2";//失败
        public static final String W = "3";//支付等待[异步支付]
        public static final String X = "4";//支付成功，充值失败
    }

    public interface AGREEMENT_MODE {//签约模式
        public static final String U = "U";//正常
        public static final String P = "P";//预签约
        public static final String O = "O";//反向签约
        public static final String V = "V";//反向预签约
    }

    public interface CONTRACT_STATE {//签约关系状态
        public static final String U = "U";//正常
        public static final String E = "E";//删除
        public static final String S = "S";//暂停
    }


    public interface TmpBusiDataState {
        public static final String U = "U";//成功
        public static final String E = "E";//失败
        public static final String S_1 = "1";//预留手机号不正确，需扣款验证
        public static final String S_2 = "2";//已测试扣款，待验证
    }


    public interface CHECKRESULT {
        public static final String CONTRACT_1 = "1";//校验能否签约成功
        public static final String CONTRACT_2 = "2";//校验能否签约失败，已签约不能再签约
        public static final String CONTRACT_3 = "3";//校验能否签约失败，与其他已签约关系互斥
        public static final String CONTRACT_4 = "4";//校验能否签约失败，该卡号已签约
    }


    public interface BUSICONFIG {
        public static final int Y = 1;//支持
        public static final int N = 0;//不支持
    }

    public interface SyncDealType {
        public static final String add = "1";//新增
        public static final String change = "2";//变更
        public static final String cancel = "3";//取消
    }


    public interface SmsParamType {
        public static final String str = "1";//字符串
        public static final String num = "2";//数字
        public static final String date = "3";//时间
    }

    public interface SmsCataLog {
        public static final String OweNotice = "1";//催缴类
        public static final String BusiDeal = "2";//业务受理类
        public static final String BusiReply = "3";//业务受理回复类
    }

    public interface PlatformType {
        public static final int bank = 1;
        public static final int busi_plat = 2;
    }

    //限制次数
    public interface LimitTimes {
        public static final int ZD_CHECK_PAY_DAY = 3;//终端捷蓝支付验证扣款每天验证次数
    }

    public interface IsNull {
        public static final int Y = 1;//可以为空
        public static final int N = 0;//不可以为空
    }

    public interface PAY_MOD {
        public static final int SELF = 1;//主动支付
        public static final int AUTO = 2;//自动支付
    }

    public interface AUTO_STATUS {
        public static final String open = "1";//开通自动支付
        public static final String close = "0";//未开通自动支付
    }

    public interface CrmProductOperType {
        public static final int open = 1;//产品开通
        public static final int close = 0;//产品取消
    }


    public interface PAY_TYPE {
        public static final int SYNC = 0;//在线支付
        public static final int ASYN = 1;//异步支付
    }

    public interface BANK_CARD_TYPE {//银行卡类型
        public static final int JJK = 1;//借记卡
        public static final int XYK = 2;//信用卡
    }

    public interface CRM_OFFER_OPER {//CRM产品操作类型
        public static final String open = "1";//开通
        public static final String close = "0";//关闭
    }

    /**
     * WS客户端配置参数
     *
     * @author Administrator
     */
    public interface WS_CLIENT_METHOD {
        public static final String ESOP_PAYNOTICE = "ESOP_PAYNOTICE";//ESOP通知方法
        public static final String MESOP_CREATE_TWODIMENSIONALCODE = "MESOP_CREATE_TWODIMENSIONALCODE";//MESOP二维码创建
        public static final String MESOP_ANALY_TWODIMENSIONALCODE = "MESOP_ANALY_TWODIMENSIONALCODE";//MESOP解析二维码
    }

    public interface PayItemType {
        public static final int TYPE_1 = 1;//话费缴费
        public static final int TYPE_2 = 2;//话费充值
        public static final int TYPE_3 = 3;//代理额度充值
        public static final int TYPE_4 = 4;//回缴款归集
        public static final int TYPE_5 = 5;//终端款支付
        public static final int TYPE_6 = 6;//业务费支付
        public static final int TYPE_7 = 7;//分期付款
    }


    public interface PayStatus {
        public static final String SUCCESS = "0";//支付成功
        public static final String FAIL = "1";//支付失败
        public static final String WAIT = "2";//等待支付（异步支付待确认）
        public static final String CHARGE_WAIT = "3";//支付成功、充值等待(充值失败，待对账解决)
        public static final String FAIL_STOP = "4";//支付失败，客户自改为本金支付（电话经理预缴使用）
    }

    public interface BusiStates {
        public static final String SUCCESS = "1";//成功
        public static final String FAIL = "2";//失败
        public static final String WAIT = "3";//等待
    }

    //平台的割接状态
    public interface PlatOnlineState {
        public static final int U = 1;//已上线
        public static final int E = 0;//未上线
        public static final int I = 2;//割接中
    }

    public interface ImplMode {
        public static String NEW = "0";        //非服务调用
        public static String LOCAL = "1";    //本地服务调用
        public static String EJB = "2";        //EJB服务调用
    }

    /**
     * 支付类型判断
     *
     * @author Administrator
     */
    public interface IsContractPay {
        public static final String AGREEMENT_PAYMENT = "0";//0：非签约充值

        public static final String NOT_AGREEMENT_PAYMENT = "1"; // 1：签约充值
    }

    /**
     * 是否打印发票
     *
     * @author Administrator
     */
    public interface IsPrintInvoice {

        public static final String YES = "1";//1：打印发票

        public static final String NO = "0"; //0：不打印发票
    }

    /**
     * 实体编码
     *
     * @author Administrator
     */
    public interface EntituCode {
        public static final String SIGN_DETAI_REPORT = "SIGN_DETAI_REPORT"; //签约解约查询实体对象

        public static final String PAY_CHECK_REPORT = "PAY_CHECK_REPORT"; //支付核对报表实体对象

        public static final String PAY_DETAIL = "PAY_DETAIL"; //支付明细【渠道专用】

        public static final String CONTRACT_PAY_DETAIL = "CONTRACT_PAY_DETAIL"; //支付明细

        public static final String CHECK_ENTITY = "CHECK_ENTITY"; //对账实体

        public static final String BANK_CARD_INFO = "BANK_CARD_INFO"; //银行卡表单个字段加密

        public static final String QBO_UPG_CUST_AGREEMENT = "QBO_UPG_CUST_AGREEMENT"; //查询BO对象

        public static final String QBO_UPG_AGREEMENT_ELEMENT = "QBO_UPG_AGREEMENT_ELEMENT"; //查询BO对象

        public static final String UPG_AGREEMENT_ELEMENT_INS = "UPG_AGREEMENT_ELEMENT_INS"; //签约实例要素

        public static final String UPG_AGREEMENT_ELEMENT_INS_HIS = "UPG_AGREEMENT_ELEMENT_INS_HIS"; //签约实例要素历史表

        public static final String EntituCode = "LOG_ENTITY";
    }

    /**
     * 操作类型
     */
    public interface OPEN_TYPE {

        public static final String AGREEMENT_TYPE = "1";//签约类型

        public static final String TERMINATION_TYPE = "2";//解约类型

        public static final String TRANSFER_TYPE = "3";//过户销户

        public static final String CHARGE_TYPE = "4";//充值类型

        public static final String CHANNEL_PAY_TYPE = "5";//支付类型

        public static final String MESSAGE_DAY_REGISTRATION = "6";//6  短信易充值用户注册日报表查询

        public static final String MESSAGE_MONTH_REGISTRATION = "7";//7  短信易充值用户注册月报表查询

        public static final String MESSAGE_DAY_CHARGE = "8";//8  短信易充值用户充值日报表

        public static final String MESSAGE_MONTH_CHARGE = "9";//9  短信易充值用户充值月报表

        public static final String MESSAGE_DAY_REGISTRATION_CHARGE = "10";//10 短信易充值用户注册报表

        public static final String delBusiDateTmp = "11";//11清除临时表数据

        public static final String dealCallOrder = "12";//12催缴进程处理

        public static final String delBusiNessStat = "13";//13清除临时表数据
    }

    //BS_PARA_DETAIL 表中的PARA_TYPE值
    public interface BUSINESS_STAT {
        public static final String BUSINESS_DAY = "BUSINESS_DAY";//日沉淀
        public static final String BUSINESS_MONTH = "BUSINESS_MONTH";//月沉淀表
        public static final String ACCOUNT_STAT = "IsBankAccountStat";//到账沉淀表
        public static final String BUSINESS_OPENTYPE = "BUSINESS_OPENTYPE";//操作类型
        public static final String REPORT_BUSI_CODE = "UPG_REPORT_BUSI_CODE";//报表对象
        public static final String CHECK_THRESHOLD = "CHECK_THRESHOLD";//对账阀值
        public static final String CHECK_SMS_STAFF = "CHECK_SMS_STAFF";//对账下发短信
        public static final String BANK_CHECK_SMS_STAFF = "BANK_CHECK_SMS_STAFF";//金融机构对账下发流程
        public static final String SYSNCHRONUSUPGORDER = "IsSynchronousUpgOrder";//工单处理数据是否同步工单业务表
        public static final String REAl_PRECIPITATION = "REAl_PRECIPITATION";//是否实时沉淀
        public static final String REL_BUSI_NESS = "REL_BUSI_NESS";//业务转发操作
    }

    //业务类型
    public interface BUSI_TYPE {

        public static final int BUSI_TYPE_1 = 1;//业务类

        public static final int BUSI_TYPE_2 = 2;//沉淀类业务

    }

    //充值类型
    public interface CHARGE_TYPE {
        public static final int AUTOMATIC = 1;//主动充值

        public static final int INITIATIVE = 0;//自动充值
    }

    //查询类型
    public interface QRY_TYPE {
        public static final int COUNT = 1;//查询总记录数

        public static final int MONEY = 2;//查询总金额

        public static final int AGREEMTN_USER = 3;//查询签约用户的充值情况

        public static final int AGREEMTN_USER_COUNT = 4;//查询签约用户的充值总行数
    }

    // 工行相关
    public interface ICBC_INFO {
        //工行对公的相关参数-----------------------------------------
        String ICBC_PUBLIC_URL = "https://corporbank3.dccnet.com.cn/servlet/ICBCINBSEBusinessServlet";
        String ICBC_PUBLIC_QUERY_URL = "corporbank3.dccnet.com.cn";
        String ICBC_PUBLIC_APINAME = "B2B";
        String ICBC_PUBLIC_APIVERSION = "001.001.001.001";
        String ICBC_PUBLIC_ACCOUNTCUR = "001";
        String ICBC_PUBLIC_JOINFLAG = "2";
        String ICBC_PUBLIC_MERCHANTURL = "http://211.140.15.104/tss/front/sh/icbcPay!icbcNotifyUrl";
        String ICBC_PUBLIC_SEND_TYPE = "0";
        String ICBC_PUBLIC_JKS_PASSWORD = "12345678";
        String ICBC_PUBLIC_GS_PUBLIC = "2";
        String ICBC_PUBLIC_NOFITY_CRTNAME = "testb2ccpublic.crt";
        String ICBC_PUBLIC_FINISH_CODE = "3";
        int ICBC_PUBLIC__PERIOD = 10;//对公有效期
    }

    //支付汇总上传文件相关
    public interface UPLODE_INFO {
        String PAY_SUM = "pay_sum";
        String BOU_SYMB = "_";
        String FTP_CODE_PATH = "UPG_FILE_CHANNEL_PAY_SUM_09";
        String CON_SYMB = "|";
        String FILE_TYPE = ".rpt";
    }

}
