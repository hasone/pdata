<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-企业查看</title>
    <meta name="keywords" content="流量平台 企业编辑"/>
    <meta name="description" content="流量平台 企业编辑"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>

        .form input[type='text'],
        .form input[type='number'],
        .form input[type='password'],
        .form select {
            background: transparent;
            border: 1px solid #ccc;
            outline: none;
            border-radius: 3px;
            padding: 4px 5px;
            width: 350px;
        }

        .form label {
            width: 150px;
            text-align: right;
        }

        .form select {
            text-align: right;
            margin-left: 10px;
        }

        .dropdown-menu > li > a {
            padding: 6px 20px;
        }

        #previewWrap {
            background: #F3F7FA;
            padding: 20px;
            display: block;
            position: relative;
            max-width: 350px;
            margin-left: 150px;
        }

        #imgPreview {
            max-width: 100%;
        }
    </style>
</head>
<body>
<div class="main-container">
    <input type="hidden" name="id" value="${Enterprises.id!}"/>

    <div class="module-header mt-30 mb-20">
        <h3>企业信息详情
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back"
               onclick="history.go(-1)">返回</a>
        </h3>
    </div>

<#if changeTag?? && changeTag==1 && cqSync?? && cqSync==0>
    <div class="module-header mt-30 mb-20">
        <#if authFlag?? && authFlag == "true">
            <#if hasApprovalRecordFlag?? && hasApprovalRecordFlag == "true" && enterStatus?? && enterStatus == "true">
                <a href="${contextPath}/manage/historyEnterprise/index.html?entId=${Enterprises.id!}"
                   class="btn btn-sm btn-danger">
                    <i class="fa fa-plus mr-5"></i>历史记录
                </a>
                <br>
                <span>审批中，如需查看具体信息请点击上方历史记录查看</span>
            </#if>
            <#if hasApprovalRecordFlag?? && hasApprovalRecordFlag == "false" && enterStatus?? && enterStatus == "true">
                <a href="${contextPath}/manage/historyEnterprise/modifyEnterprise.html?entId=${Enterprises.id!}"
                   class="btn btn-sm btn-danger"><i class="fa fa-plus mr-5"></i>申请修改
                </a>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <a href="${contextPath}/manage/historyEnterprise/index.html?entId=${Enterprises.id!}"
                   class="btn btn-sm btn-danger">
                    <i class="fa fa-plus mr-5"></i>历史记录
                </a>
            </#if>
            <#if enterStatus?? && enterStatus=="false">
                <a href="${contextPath}/manage/historyEnterprise/index.html?entId=${Enterprises.id!}"
                   class="btn btn-sm btn-danger"><i class="fa fa-plus mr-5"></i>历史记录
                </a>
            </#if>
        </#if>
        <#if authFlag?? && authFlag == "false">
            <a href="${contextPath}/manage/historyEnterprise/index.html?entId=${Enterprises.id!}"
               class="btn btn-sm btn-danger"><i class="fa fa-plus mr-5"></i>历史记录
            </a>
        </#if>
    </div>
</#if>

    <form hidden>
        <div class="tile mt-30">
            <div class="tile-content">
                <div class="row form">

                    <div class="col-md-6">
                        <div class="form-group">
                            <label id="name">企业名称：</label>
                            <span>${(Enterprises.name)!}</span>

                        </div>

                        <div class="form-group">
                            <label id="entName">企业简称：</label>
                            <span>${(Enterprises.entName)!}</span>
                        </div>

                        <div class="form-group">
                            <label id="code">企业编码：</label>
                            <span>${(Enterprises.code)!}</span>

                        </div>

                        <div class="form-group">
                            <label id="district">地区：</label>
                            <span>${(fullDistrictName)!}</span>
                        </div>
                    <#if cqFlag??&&cqFlag==0>
                        <div class="form-group">
                            <label>企业余额：</label>
                            <#if account?? && account.count??>
                                <span>${(account.count/100.0)?string("#.##")} 元</span>
                            </#if>
                        </div>
                    </#if>

                        <div class="form-group">
                            <label id="customerTypeId">客户分类：</label>
                            <span>${(Enterprises.customerTypeName)!}</span>
                        </div>

                        <div class="form-group">
                            <label id="businessTypeId">流量类型：</label>
                            <span>通用流量</span>
                        </div>

                        <div class="form-group">
                            <label id="prdType">产品类型：</label>
                            <span>10M-11G所有通用产品包</span>
                        </div>

                        <div class="form-group">
                            <label id="payTypeId">支付类型：</label>
                            <span>${(Enterprises.payTypeName)!}</span>
                        </div>

                        <div class="form-group">
                            <label id="discount">折扣：</label>
                            <span>${(Enterprises.discountName)!}</span>

                        </div>

                        <div class="form-group">
                            <label id="giveMoneyId">存送：</label>
                            <span>${(Enterprises.giveMoneyName)!}</span>
                        </div>

                        <div class="form-group">
                            <label id="entCredit">企业信用额度：</label>
                            <span>
                            <#if minCount??>
                            ${minCount?string("#.##")} 元
                            </#if>
                            </span>
                        </div>

                        <div class="form-group">
                            <label id="ec">EC开通：</label>
                        <#if (Enterprises.interfaceFlag)?? && Enterprises.interfaceFlag == 1> <span>开通</span></#if>
                        <#if (Enterprises.interfaceFlag)?? && Enterprises.interfaceFlag == 0> <span>关闭</span></#if>
                        <#if (Enterprises.interfaceFlag)?? && Enterprises.interfaceFlag == 2> <span>未申请</span></#if>
                        <#if (Enterprises.interfaceFlag)?? && Enterprises.interfaceFlag == 3> <span>申请中</span></#if>
                        <#if (Enterprises.interfaceFlag)?? && Enterprises.interfaceFlag == 4> <span>已驳回</span></#if>
                        </div>

                        <div class="form-group">
                            <label id="email">企业联系邮箱：</label>
                            <span>${(Enterprises.email)!}</span>
                        </div>
                        <div class="form-group">
                            <label id="phone">企业联系方式：</label>
                            <span>${(Enterprises.phone)!}</span>
                        </div>

                        <div class="form-group">
                            <label id="cooperationTime">合作开始时间：</label>
                            <span>${(Enterprises.startTime?datetime)!}
                                ~ ${(Enterprises.endTime?datetime)!}</span>
                        <#if contractExpire?? && contractExpire == "true">
                            <span style="color:red">已过期</span>
                        </#if>
                        </div>

                    <#list enterpriseManagers as enterpriseManager>
                        <div class="form-group">
                            <label>企业状态：</label>
                            <#if Enterprises.deleteFlag==0>
                                <span>正常</span>
                            </#if>
                            <#if Enterprises.deleteFlag==2>
                                <span>已暂停</span>
                            </#if>
                            <#if Enterprises.deleteFlag==3>
                                <span>已下线</span>
                            </#if>
                            <#if Enterprises.deleteFlag==4>
                                <span>等待市级管理员审批</span>
                            </#if>
                            <#if Enterprises.deleteFlag==5>
                                <span>等待省级管理员审批</span>
                            </#if>
                            <#if Enterprises.deleteFlag==6 && cqFlag==0>
                                <span>等待省级管理员审批</span>
                            </#if>
                            <#if Enterprises.deleteFlag==6 && cqFlag==1>
                                <span>等待分公司管理员审批</span>
                            </#if>
                            <#if Enterprises.deleteFlag==7>
                                <span>已保存</span>
                            </#if>
                            <#if Enterprises.deleteFlag==8>
                                <span>已驳回</span>
                            </#if>
                            <#if Enterprises.deleteFlag==10>
                                <span>认证审批中</span>
                            </#if>
                        </div>
                        <div class="form-group">
                            <label id="enterpriseManagerName">企业管理员姓名：</label>
                            <span type="text" name="userName"
                                  id="userName"> ${(enterpriseManager.userName)!} </span>
                        </div>
                        <div class="form-group">
                            <label id="enterpriseManagerPhone">企业管理员手机：</label>
                            <span type="text" name="mobilePhone"
                                  id="mobilePhone">${(enterpriseManager.mobilePhone)!}</span>
                        </div>
                    </#list>
                    </div>

                    <div class="col-md-6">

                        <div class="form-group">
                            <label id="customerManagerName">客户经理姓名：</label>
                            <span type="text" name="userName"
                                  id="userName"> ${(customerManager.userName)!} </span>
                        </div>
                        <div class="form-group">
                            <label id="customerManagerPhone">客户经理电话：</label>
                            <span type="text" name="mobilePhone"
                                  id="mobilePhone">${(customerManager.mobilePhone)!}</span>
                        </div>

                        <div class="form-group">
                            <label id="customerManagerEmail">客户经理邮箱：</label>
                            <span type="text" name="mobilePhone"
                                  id="mobilePhone">${(Enterprises.cmEmail)!}</span>
                        </div>

                        <div class="form-group">
                            <label>工商营业执照：</label>
                            <a name="businessLicence" id="businessLicence" onclick="downLoad(this)"
                            >${(enterpriseFile.businessLicence)!}</a>
                        </div>

                        <div class="form-group">
                            <label>企业资质：</label>
                            <a name="businessLicence" id="businessLicence"
                               onclick="downLoad(this)">${(enterpriseFile.businessLicence)!}</a>
                        </div>

                        <div class="form-group">
                            <label>合作协议文件：</label>
                            <a name="contract" id="contract"
                               onclick="downLoad(this)">${(enterpriseFile.contractName)!}</a>
                        </div>

                        <div class="form-group" id="search-time-range1">
                            <label>营业执照有效期：</label>&nbsp
                            <span>${Enterprises.licenceStartTime?datetime}</span>~
                            <span>${Enterprises.licenceEndTime?datetime}</span>
                        <#if licenceExpire?? && licenceExpire == "true">
                            <span style="color:red">已过期</span>
                        </#if>
                        </div>

                        <div class="form-group">
                            <label>企业管理授权证明：</label>
                            <a name="authorization" id="authorization" onclick="downLoad(this)"
                            >${(enterpriseFile.authorizationCertificate)!}</a>
                        </div>

                        <div class="form-group" id="identification">
                            <div class="form-group">
                                <label>企业管理员身&nbsp;&nbsp;<br>份证（正面）：</label>
                                <a name="identification" id="identification"
                                   onclick="downLoad(this)"
                                >${(enterpriseFile.identificationCard)!}</a>
                            </div>

                            <div class="form-group">
                                <label>企业管理员身&nbsp;&nbsp;<br>份证（反面）：</label>
                                <a name="identificationBack" id="identificationBack"
                                   onclick="downLoad(this)"
                                >${(enterpriseFile.identificationBack)!}</a>
                            </div>
                        </div>


                        <div class="form-group">
                            <label>客服说明附件：</label>
                            <a name="customerFile" id="customerFile"
                               onclick="downLoad(this)">${(enterpriseFile.customerfileName)!}</a>
                        </div>

                        <div class="form-group">
                            <label>合作协议文件：</label>
                            <a name="contract" id="contract" onclick="downLoad(this)"
                            >${(enterpriseFile.contractName)!}</a>
                        </div>

                        <div class="form-group">
                            <label>审批截图：</label>
                            <a name="image" id="image"
                               onclick="downLoad(this)">${(enterpriseFile.imageName)!}</a>
                        </div>

                        <div class="form-group" id="previewWrap">
                            <img src="${contextPath}/manage/enterprise/getImageFile.html?id=${Enterprises.id}"
                                 id="imgPreview">
                        </div>
                    </div>

                </div>
            </div>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    var convertTable = {
        "licenceTime": "search-time-range1",
        "licenceImage": "businessLicence"
    };

    var renderDataUrl = "${contextPath}/manage/entPropsInfo/query.html?${_csrf.parameterName}=${_csrf.token}";

    require(["moment", "common", "bootstrap", "daterangepicker"], function (mm, a, b, c) {
        //渲染页面
        render(renderDataUrl, convertTable);

        //初始化
        init();
        window["moment"] = mm;
        //初始化日期组件
        initDateRangePicker();

        checkFormValidate();
        goback();

    });
</script>

<script>
    /**
     * 初始化
     */
    function init() {
        $("input[type='file']").change(function () {
            readFileName(this);

            //审批截图
            if ($(this).hasClass("approveImage")) {
                preview(this);
            }
        });
    }

    /**
     * 截取文件名称
     * @param ele
     */
    function readFileName(ele) {
        var path = ele.value;
        var lastsep = path.lastIndexOf("\\");
        var filename = path.substring(lastsep + 1);
        $(ele).parent().children("input.file-text").val(filename);
    }

    /**
     * 预览上传的图片
     */
    function preview(ele) {
        $("#previewWrap").show();
        if (ele.files && ele.files[0]) {// FileReader,IE10+、FF22/23、Chrome28/29支持
            if (typeof FileReader != 'undefined') {
                var reader = new FileReader;
                reader.onload = function (event) {
                    event = event || window.event;
                    var img = new Image();
                    img.onload = function () {
                        $("#imgPreview").attr("src", event.target.result);
                    };
                    img.src = event.target.result;
                };
                reader.readAsDataURL(ele.files[0]);
            }
        } else {
            var preload = document.createElement("div"),
                    body = document.body,
                    data, oriWidth, oriHeight, ratio;

            preload.style.cssText =
                    "width:100px;height:100px;visibility: visible;position: absolute;left: 0px;top: 0px";
            // 设置sizingMethod='image'
            preload.style.filter =
                    "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='image')";
            body.insertBefore(preload, body.childNodes[0]);
            ele.select();
            try {
                data = document.selection.createRange().text;
            } finally {
                document.selection.empty();
            }

            if (!!data) {
                data = data.replace(/[)'"%]/g, function (s) {
                    return escape(escape(s));
                });
                //预载图片
                try {
                    preload.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = data;
                } catch (e) {
                    //console.log(e.description);
                    return;
                }
            }

            oriWidth = preload.offsetWidth;
            oriHeight = preload.offsetHeight;

            document.body.removeChild(preload);
            preload = null;

            var target = $("#imgPreview");
            var parent = target.parent();
            var div = $("<div>");
            parent.prepend(div);
            div.css({
                        "filter": "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)",
                        position: "absolute",
                        left: '20px',
                        top: '20px'
                    });
            div[0].filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = data;
            var w = parent.width();
            var h = w / oriWidth * oriHeight;
            div[0].style.width = w + "px";
            div[0].style.height = h + "px";
            target.css({height: h + "px"});
        }
    }

    function checkFormValidate() {
        $("#table_validate").packValidate({
                                              errorElement: "span",
                                              rules: {
                                                  name: {
                                                      required: true,
                                                      maxlength: 64,
                                                      remote: {
                                                          url: "check.html",
                                                          data: {
                                                          <#if Enterprises.id??>
                                                              id: ${Enterprises.id! },
                                                          </#if>
                                                              name: function () {
                                                                  return $('#name').val()
                                                              }
                                                          }
                                                      },
                                                      searchBox: true
                                                  },
                                                  entName: {
                                                      required: true,
                                                      maxlength: 64
                                                  },
                                                  code: {
                                                      required: true,
                                                      maxlength: 20,
                                                      format1: true,
                                                      remote: {
                                                          url: "check.html",
                                                          data: {
                                                          <#if Enterprises.id??>
                                                              id: ${Enterprises.id! },
                                                          </#if>
                                                              code: function () {
                                                                  return $('#code').val()
                                                              }
                                                          }
                                                      },
                                                  },

                                                  phone: {
                                                      required: true,
                                                      mobile: true
                                                  }
                                              },
                                              messages: {
                                                  name: {
                                                      remote: "企业名称已经存在!"
                                                  },
                                                  code: {
                                                      remote: "企业编码已经存在!"
                                                  }
                                              }
                                          });
    }

    function goback() {
        if ($('#isModifyChanged').val() == 'true') {
            return confirm("是否确定不保存记录退出？");
        }
        else {
            return true;
        }
    }
    ;

    function downLoad(obj) {
        var type = obj.id;
        var fileName = obj.innerHTML;
        location.href =
                "${contextPath}/manage/enterprise/downloadFile.html?enterpriseId=${enterprise.id}"
                + "&&type=" + type;
    }

</script>
</body>
</html>