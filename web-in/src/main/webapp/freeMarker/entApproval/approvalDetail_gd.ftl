<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-企业审批</title>
    <meta name="keywords" content="流量平台 企业审批"/>
    <meta name="description" content="流量平台 企业审批"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>
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
            width: 300px;
            text-align: right;
        }

        .form select {
            text-align: right;
            margin-left: 10px;
        }

        .lg-btn-input {
            width: 205px;
            text-align: left;
        }

        .dropdown-menu {
            left: 0;
            right: auto;
            width: 100%;
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
            margin-left: 300px;
        }

        #imgPreview {
            max-width: 100%;
        }

        .form-group span {
            word-break: break-all;
        }

    </style>
</head>
<body>
<div class="main-container">
    <!-- <input type="hidden" name="id" value="1" /> 暂时用1替代  -->

    <div class="module-header mt-30 mb-20">
        <h3>企业审批
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>

    <div class="tile mt-30">
        <div class="tile-header">审批信息查看</div>
        <div class="tile-content" style="padding: 0;">
            <div role="tabpanel" class="tab-pane active table-responsive" id="table_wrap1">
                <div class="table-responsive">
                    <table class="table table-indent text-center table-bordered-noheader mb-0">
                        <thead>
                        <tr>
                            <th>审批状态</th>
                            <th>审批用户</th>
                        <#if provinceFlag??&&provinceFlag=="sc">
                            <th>用户</th>
                        <#else>
                            <th>用户职位</th>
                        </#if>
                            <th>审批时间</th>
                            <th>审批意见</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#if opinions??>
                            <#list opinions as opinion>
                            <tr>
                                <td>${opinion.description!}</td>
                                <td>${opinion.userName!}</td>
                                <td>${opinion.managerName!}</td>
                                <td>${(opinion.updateTime?datetime)!}</td>
                                <td title="<#if opinion.comment??>${opinion.comment}</#if>">
                                    <#if opinion.comment??>
													${opinion.comment!}
												</#if>
                                </td>
                            </tr>
                            </#list>
                        </#if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <!--第一页-->
    <div class="tile mt-30" id="firstStep">
        <div class="tile-header">企业信息</div>
        <div class="tile-content">
            <div class="form">

                <div class="form-group">
                    <label>企业名称：</label>
                    <span>${enterprise.name!}</span>
                </div>

                <div class="form-group" hidden>
                    <label>企业简称：</label>
                    <span>${enterprise.entName!}</span>
                </div>

                <div class="form-group">
                    <label>企业联系邮箱：</label>
                    <span>${enterprise.email!}</span>
                </div>

				<#--
                <div class="form-group">
                    <label>企业联系电话：</label>
                    <span>${enterprise.phone!}</span>
                </div>
                -->

            <#list enterpriseManagers as enterpriseManager>
                <div class="form-group">
                    <label>企业管理员姓名：</label>
                    <span type="text" name="userName" id="userName"> ${(enterpriseManager.userName)!} </span>
                </div>
                <div class="form-group">
                    <label>企业管理员手机号码：</label>
                    <span type="text" name="mobilePhone" id="mobilePhone">${(enterpriseManager.mobilePhone)!}</span>
                </div>
            </#list>

                <div class="form-group">
                    <label>地区：</label>
                    <span>${enterprise.cmManagerName!}</span>
                </div>


                <div class="form-group">
                    <label>客户经理姓名：</label>
                    <span type="text" name="userName" id="userName"> ${(customerManager.userName)!} </span>
                </div>


                <div class="form-group">
                    <label>客户经理手机号码：</label>
                    <span type="text" name="mobilePhone" id="mobilePhone">${(customerManager.mobilePhone)!}</span>
                </div>

            <#if hasEmail??&&hasEmail=="true">
                <div class="form-group">
                    <label>客户经理邮箱：</label>
                    <span type="text" name="email" id="email"> ${(enterprise.cmEmail!)!} </span>
                </div>
            </#if>

                <div class="form-group" hidden>
                    <label>企业工商营业执照：</label>
                    <a name="businessLicence" id="businessLicence" onclick="downLoad(this)"
                       style="width: 242px;">${businessLicence!}</a>
                </div>

                <div class="form-group" hidden>
                    <label>营业执照有效期：</label>
                    <span>${(enterprise.licenceStartTime?datetime)!}</span>&nbsp;&nbsp;至&nbsp;&nbsp;<span>${(enterprise.licenceEndTime?datetime)!}</span>
                </div>

                <div class="form-group" hidden>
                    <label>企业管理员授权证明：</label>
                    <a name="authorization" id="authorization" onclick="downLoad(this)"
                       style="width: 242px;">${authorization!}</a>
                </div>

                <div class="form-group" hidden>
                    <label>企业管理员身份证扫描件（正面）：</label>
                    <a name="identification" id="identification" onclick="downLoad(this)"
                       style="width: 242px;">${identification!}</a>
                </div>

                <div class="form-group" hidden>
                    <label>企业管理员身份证扫描件（反面）：</label>
                    <a name="identificationBack" id="identificationBack" onclick="downLoad(this)"
                       style="width: 242px;">${identificationBack!}</a>
                </div>

            </div>
        </div>

        <div class="tile-content">
            <div class="btn-save mt-30">
                <a href="javascript:void(0)" class="btn btn-sm btn-warning dialog-btn" onclick="nextStep()"
                   id="next-btn">下一步</a>
            </div>
        </div>
    </div>

    <!--第二页-->
    <div class="tile mt-30" style="display: none;" id="secondStep">
        <div class="tile-header">企业信息</div>
        <div class="tile-content">
            <div class="form">
                <div class="form-group">
                    <label>集团编号：</label>
                    <span>${(enterprise.code)!}</span>
                </div>
               <#if provinceFlag??&&provinceFlag!="chongqing">
                <div class="form-group">
                    <label>集团产品号码：</label>
                    <span>${(extEntInfo.ecPrdCode)!}</span>
                </div>
               </#if>

                <#if crowdfundingFlag>
                    <div class="form-group">
                        <label>众筹方式：</label>
                        <span>
                            <#if extEntInfo.joinType?? && extEntInfo.joinType==1>
                                大企业模式
                            </#if>
                            <#if extEntInfo.joinType?? && extEntInfo.joinType==2>
                                中小企业模式
                            </#if>
                        </span>
                    </div>

                    <#if extEntInfo.joinType?? && extEntInfo.joinType==1>
                        <div class="form-group" style="word-break: break-all;">
                            <label>回调地址：</label>
                            <span>${(extEntInfo.callbackUrl)!}</span>
                        </div>
                    </#if>
                </#if>

            <#if isCooperate == "true">
                <div class="form-group" hidden>
                    <label>合作时间：</label>
                    <span>${(enterprise.startTime?datetime)!}</span>&nbsp;&nbsp;至&nbsp;&nbsp;<span>${(enterprise.endTime?datetime)!}</span>
                </div>
            </#if>

            <#if isCooperate == "true">
                <#if flag??&&flag==1>
                    <div class="form-group">
                        <label>企业信用额度：</label>
                        <span>${minCount!}</span>
                    </div>
                </#if>
            </#if>


            <#if flag??&&flag!=1>

                <div class="form-group" hidden>
                    <label>企业折扣：</label>
                    <span>${enterprise.discountName!}</span>
                </div>


                <div class="form-group" hidden>
                    <label>企业存送优惠：</label>
                    <span>${enterprise.giveMoneyName!}</span>
                </div>
            </#if>

                <div class="form-group" hidden>
                    <label>客户分类：</label>
                    <span>${enterprise.customerTypeName!}</span>
                </div>

                <div class="form-group" hidden>
                    <label>流量类型：</label>
                    <span>通用流量</span>
                </div>

                <div class="form-group" hidden>
                    <label>产品类型：</label>
                    <span>10M-11G所有通用产品包</span>
                </div>

                <div class="form-group" hidden>
                    <label>支付类型：</label>
                    <span>${enterprise.payTypeName!}</span>
                </div>

            <#if enterprise.interfaceFlag??&&enterprise.interfaceFlag==1>
                <div class="form-group" hidden>
                    <label>EC开通：</label>
                    <span>开通</span>
                </div>
            </#if>

            <#if enterprise.interfaceFlag??&&enterprise.interfaceFlag==0>
                <div class="form-group" hidden>
                    <label>EC开通：</label>
                    <span>关闭</span>
                </div>
            </#if>
            <#if enterprise.interfaceFlag??&&enterprise.interfaceFlag==2>
                <div class="form-group" hidden>
                    <label>EC开通：</label>
                    <span>未申请</span>
                </div>
            </#if>
            <#if enterprise.interfaceFlag??&&enterprise.interfaceFlag==3>
                <div class="form-group" hidden>
                    <label>EC开通：</label>
                    <span>申请中</span>
                </div>
            </#if>
            <#if enterprise.interfaceFlag??&&enterprise.interfaceFlag==4>
                <div class="form-group" hidden>
                    <label>EC开通：</label>
                    <span>已驳回</span>
                </div>
            </#if>

            <#if isCooperate == "true">

                <div class="form-group" hidden>
                    <label>客服说明附件：</label>
                    <a name="customerfile" id="customerfile" onclick="downLoad(this)"
                       style="width: 242px;">${customerfile!}</a>
                </div>

                <div class="form-group" hidden>
                    <label>合作协议文件：</label>
                    <a name="contract" id="contract" onclick="downLoad(this)" style="width: 242px;">${contract!}</a>
                </div>

                <#if provinceFlag??&&provinceFlag!="hainan"&&provinceFlag!="chongqing">
                    <div class="form-group">
                        <label>缴费截图：</label>
                        <a name="image" id="image" onclick="downLoad(this)" style="width: 242px;">${image!}</a>
                    </div>
                </#if>
				<#if provinceFlag??&&provinceFlag=="chongqing">
                    <div class="form-group">
                        <label>企业资质：</label>
                        <img id="businessLicence" style="width: 242px;"
                         src="${contextPath}/manage/enterprise/getImage.html?enterpriseId=${enterprise.id}&type=businessLicence"/>
                    </div>
                <div class="form-group">
                    <label>合作协议文件：</label>
                    <a name="contract" id="contract" onclick="downLoad(this)" style="width: 242px;">${contract!}</a>
                </div>
                </#if>
            </#if>
            </div>
        </div>

        <div class="tile-content">
            <form action="${contextPath}/manage/approval/saveApproval.html" method="post" name="enterprisesForm"
                  id="table_validate">
                <input type="hidden" name="enterId" id="enterId" value="${(enterprise.id)!}">
                <input type="hidden" name="approvalRecordId" id="approvalRecordId" value="${approvalRecordId!}">
                <input type="hidden" name="requestId" id="requestId" value="${requestId!}">
                <input type="hidden" name="processId" id="processId" value="${processId!}">

                <div class="tile-content">
                    <label>审批意见：</label>
                    <textarea rows="10" cols="100" maxlength="300" name="comment" id="comment"
                              class="hasPrompt"></textarea>
                </div>
                <div class="btn-save mt-30">
                    <input type="hidden" id="approvalStatus" name="approvalStatus" value="">
                    <a href="javascript:void(0)" class="btn btn-sm btn-warning dialog-btn" onclick="lastStep()"
                       id="last-btn">上一步</a>&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="javascript:void(0)" class="btn btn-sm btn-warning dialog-btn" id="save-btn1">通过</a>&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="javascript:void(0)" class="btn btn-sm btn-warning dialog-btn" id="save-btn2">驳回</a>&nbsp;&nbsp;&nbsp;&nbsp;<span
                        style='color:red'>${errorMsg!}</span>
                </div>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
        </div>
    </div>
    
    
    <!-- loading -->
<div id="loadingToast" class="weui_loading_toast">
    <div class="weui_mask_transparent"></div>
    <div class="weui_toast">
        <div class="weui_loading">
            <div class="weui_loading_leaf weui_loading_leaf_0"></div>
            <div class="weui_loading_leaf weui_loading_leaf_1"></div>
            <div class="weui_loading_leaf weui_loading_leaf_2"></div>
            <div class="weui_loading_leaf weui_loading_leaf_3"></div>
            <div class="weui_loading_leaf weui_loading_leaf_4"></div>
            <div class="weui_loading_leaf weui_loading_leaf_5"></div>
            <div class="weui_loading_leaf weui_loading_leaf_6"></div>
            <div class="weui_loading_leaf weui_loading_leaf_7"></div>
            <div class="weui_loading_leaf weui_loading_leaf_8"></div>
            <div class="weui_loading_leaf weui_loading_leaf_9"></div>
            <div class="weui_loading_leaf weui_loading_leaf_10"></div>
            <div class="weui_loading_leaf weui_loading_leaf_11"></div>
        </div>
        <p class="weui_toast_content">更新中</p>
    </div>
</div>
<!-- loading end -->

    <script src="${contextPath}/assets/lib/require.js"></script>
    <script src="${contextPath}/assets/js/config.js"></script>

    <script>
        function nextStep() {
            $("#firstStep").hide();
            $("#secondStep").show();
        }

        function lastStep() {
            $("#firstStep").show();
            $("#secondStep").hide();
        }
    </script>

    <script>

        require(["moment", "common", "bootstrap", "daterangepicker"], function (mm, a, b, c) {
            //初始化
            init();
            window["moment"] = mm;
            goback();
            checkFormValidate();

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

            //保存审批
            $("#save-btn1").on("click", function () {
                if ($("#table_validate").validate().form()) {
                	showToast();
                    $("#approvalStatus").val(1);
                    $("#table_validate").submit();
                }			
                return false;
            });
            $("#save-btn2").on("click", function () {
                if ($("#table_validate").validate().form()) {
                    $("#approvalStatus").val(0);
                    $("#table_validate").submit();
                }

                return false;
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

                preload.style.cssText = "width:100px;height:100px;visibility: visible;position: absolute;left: 0px;top: 0px";
                // 设置sizingMethod='image'
                preload.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='image')";
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


        function goback() {
            if ($('#isModifyChanged').val() == 'true') {
                return confirm("是否确定不保存记录退出？");
            }
            else {
                return true;
            }
        }
        ;


        function checkFormValidate() {
            $("#table_validate").validate({
                errorPlacement: function (error, element) {
                    error.addClass("error-tip");
                    if ($(element).siblings('div').hasClass('prompt')) {
                        $(element).siblings('div.prompt').before(error);
                    } else {
                        $(element).parent().append(error);
                    }
                },
                errorElement: "span",
                rules: {
                    comment: {
                        required: true,
                        maxlength: 300
                    }
                },
                messages: {
                    comment: {
                        required: "请填写审批意见",
                        maxlength: "审批意见不超过300字符"
                    }
                }
            });
        }


        function downLoad(obj) {
            var type = obj.id;
            var fileName = obj.innerHTML;
            location.href = "${contextPath}/manage/enterprise/downloadFile.html?enterpriseId=${enterprise.id}" + "&&type=" + type;
        }
    </script>
</body>
</html>