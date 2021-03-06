<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-信息修改审批详情</title>
    <meta name="keywords" content="流量平台 信息修改审批详情"/>
    <meta name="description" content="流量平台 信息修改审批详情"/>

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
        .form {
            width: 1200px;
        }

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

        .form .form-group label {
            width: 186px;
            text-align: right;
        }

        .form .form-group .prompt {
            margin-left: 305px;
            color: #bbb;
            padding-bottom: 0;
            margin-bottom: 0;
        }

        .form .dateRange-picker {
            width: 225px;
        }

        .main-container {
        }

        .file-box {
            display: inline-block;
        }

        .btn-save {
            text-align: center;
        }

        @media (min-width: 768px) {
            .modal-dialog {
                width: 800px;
            }
        }
    </style>
</head>
<body>
<div class="main-container">
    <input type="hidden" name="id" id="id" value="${historyEnterprises.id!}"/>

    <div class="module-header mt-30 mb-20">
        <h3>历史审批详情
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>

<#if hasApproval??&&hasApproval=="true">
    <div class="tile mt-30" id="approvalHistory">
        <div class="tile-header">审批详情</div>
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
                                    <td>${opinion.managerName!}${opinion.roleName!}</td>
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
</#if>

    <div class="tile mt-30" id="modifyDetail">
        <div class="tile-header">修改备注</div>
        <div class="tile-content">
            <span style="word-break: break-all; display: inline-block;">${historyEnterprises.comment!}</span>
        </div>
    </div>

    <!--第一页-->
    <div class="tile mt-30" id="firstStep" hidden>
        <div class="tile-header">企业详情</div>
        <div class="tile-content">
            <div class="row form">

                <div class="form-group">
                    <label id="name">企业名称：</label>
                    <span>${historyEnterprises.name!}</span>
                </div>

                <div class="form-group">
                    <label id="entName">企业简称：</label>
                        <span>${(historyEnterprises.entName)!}<span>
                </div>

                <div class="form-group">
                    <label id="code">企业编码：</label>
                    <span>${historyEnterprises.code!}</span>
                </div>

                <div class="form-group">
                    <label id="district">地区：</label>
                    <span>${(fullDistrictName)!}</span>
                </div>

                <div class="form-group">
                    <label id="customerTypeId">客户分类：</label>
                    <span>${customerName!}</span>
                </div>

                <div class="form-group">
                    <label id="businessTypeId">流量类型：</label>
                    <span>通用流量</span>
                </div>

                <div class="form-group">
                    <label id="prdType">产品类型：</label>
                    <span>10M-11G所有通用产品包</span>
                </div>

                <#if flag??&&flag==0>
                    <div class="form-group">
                        <label id="discount">企业折扣：</label>
                        <span>${discountName!}</span>
                    </div>

                    <div class="form-group">
                        <label id="giveMoneyId">企业存送优惠：</label>
                        <span>${giveMoneyName!}</span>
                    </div>
                </#if>

                <#if flag??&&flag==1>
                    <div class="form-group">
                        <label id="entCredit">企业信用额度：</label>
                        <span>${minCount!}</span>
                    </div>
                </#if>

                <div class="form-group">
                    <label id="email">企业联系邮箱：</label>
                        <span>${(historyEnterprises.email)!}<span>
                </div>

                <div class="form-group">
                    <label id="phone">企业联系方式：</label>
                    <span>${historyEnterprises.phone!}</span>
                </div>

                <div class="form-group">
                    <label id="enterpriseManagerName">企业管理员姓名：</label>
                    <span>${(enterpriseManager.userName)!}</span>
                </div>

                <div class="form-group">
                    <label id="enterpriseManagerPhone">企业管理员手机：</label>
                    <span>${(enterpriseManager.mobilePhone)!}</span>
                </div>

                <div class="form-group">
                    <label id="customerManagerName">客户经理姓名：</label>
                    <span>${(customerManager.userName)!}</span>
                </div>

                <div class="form-group">
                    <label id="customerManagerPhone">客户经理手机：</label>
                    <span>${(customerManager.mobilePhone)!}</span>
                </div>

                <div class="form-group">
                    <label style="vertical-align: top;">工商营业执照：</label>
                    <img id="businessLicence" style="max-width: 242px;"
                         src="${contextPath}/manage/historyEnterprise/getImage.html?requestId=${historyEnterprises.requestId!}&type=businessLicence"/>
                </div>


                <div class="form-group" id="search-time-range1">
                    <label id="licenceTime">营业执照有效期：</label>&nbsp
                <#if historyEnterprises.licenceStartTime??&&historyEnterprises.licenceEndTime??>
                    <span>${historyEnterprises.licenceStartTime?datetime}</span>&nbsp;&nbsp;~&nbsp;&nbsp;
                    <span>${historyEnterprises.licenceEndTime?datetime}</span>
                </#if>
                </div>

                <div class="form-group">
                    <label>企业管理员&nbsp;&nbsp;<br>授权证明：</label>
                    <div class="file-box">
                        <a name="authorization" id="authorization" onclick="downLoad(this)"
                           style="width: 242px;">${authorization!}</a>
                    </div>
                </div>

                <div class="form-group">
                    <label style="vertical-align: top;">企业管理员身&nbsp;&nbsp;<br>份证（正面）：</label>
                    <img id="identification" style="max-width: 242px;"
                         src="${contextPath}/manage/historyEnterprise/getImage.html?requestId=${historyEnterprises.requestId!}&type=identification"/>
                </div>

                <div class="form-group">
                    <label style="vertical-align: top;">企业管理员身&nbsp;&nbsp;<br>份证（反面）：</label>
                    <img id="identificationBack" style="max-width: 242px;"
                         src="${contextPath}/manage/historyEnterprise/getImage.html?requestId=${historyEnterprises.requestId!}&type=identificationBack"/>
                </div>
            </div>
        </div>
    </div>

    <div class="btn-save mt-30 " id="first-btn">
        <#if nextPage == "true">
            <a href="javascript:void(0)" class="btn btn-sm btn-warning dialog-btn" onclick="nextStep()"
               id="next-btn">下一页</a>
        </#if>
    </div>

    <!--第二页-->
    <div class="tile mt-30" style="display: none;" id="secondStep">
        <div class="tile-header">企业详情</div>
        <div class="tile-content">
            <div class="row form">

                <div class="form-group">
                    <label id="customerManagerEmail">客户经理邮箱：</label>
                    <span>${(historyEnterprises.cmEmail)!}</span>
                </div>

                <div class="form-group">
                    <label>客服说明附件：</label>
                    <a name="customerfile" id="customerfile" onclick="downLoad(this)"
                       style="width: 242px;">${customerfile!}</a>
                </div>

                <div class="form-group">
                    <label id="cooperationTime">合作时间：</label>
                    <span>${(historyEnterprises.startTime?datetime)!}</span>&nbsp;&nbsp;~&nbsp;&nbsp;<span>${(historyEnterprises.endTime?datetime)!}</span>
                </div>

                <div class="form-group">
                    <label>合作协议文件：</label>
                    <a name="contract" id="contract" onclick="downLoad(this)" style="width: 242px;">${contract!}</a>
                </div>

                <div class="form-group">
                    <label style="vertical-align: top;">审批截图：</label>
                    <img id="image" style="max-width: 242px;"
                         src="${contextPath}/manage/historyEnterprise/getImage.html?requestId=${historyEnterprises.requestId!}&type=image"/>
                </div>

            </div>
        </div>
    </div>

    <div class="btn-save mt-30" id="second-btn" style="display: none;">
        <a href="javascript:void(0)" class="btn btn-sm btn-warning dialog-btn" onclick="lastStep()"
           id="last-btn">上一页</a>
    </div>

    <div class="modal fade dialog-lg" id="img-dialog">
        <div class="modal-dialog">
            <div class="modal-content bd-muted">
                <div class="modal-header bg-muted bd-muted">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <h5 class="modal-title">图片预览！</h5>
                </div>
                <div class="modal-body">
                    <img id="flexBox-img" style="max-width: 100%; display: block; margin: 0 auto;"/>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-warning btn-sm" data-dismiss="modal">确 定</button>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal-dialog -->
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
            <p class="weui_toast_content">数据加载中</p>
        </div>
    </div>
    <!-- loading end -->
    <script type="text/javascript" src="${contextPath}/manage/Js/jquery.js"></script>
    <script type="text/javascript" src="${contextPath}/manage2/Js/jquery.validate.js"></script>

    <script src="${contextPath}/assets/lib/require.js"></script>
    <script src="${contextPath}/assets/js/config.js"></script>

    <script>
        function nextStep() {
            $('#modifyDetail').hide();
            $('#firstStep').hide();
            $('#first-btn').hide();
            $('#secondStep').show();
            $('#second-btn').show();
        }

        function lastStep() {
            $('#modifyDetail').show();
            $('#firstStep').show();
            $('#first-btn').show();
            $('#secondStep').hide();
            $('#second-btn').hide();
        }

    </script>


    <script>
        var convertTable={
            "licenceImage": "businessLicence"
        };

        var renderDataUrl = "${contextPath}/manage/entPropsInfo/query.html?${_csrf.parameterName}=${_csrf.token}";

        var type = "${type!}";
        require(["moment", "common", "bootstrap", "daterangepicker"], function (mm, a, b, c) {
            //渲染页面
            render(renderDataUrl, convertTable, "firstStep");

            //初始化
            init();

            window["moment"] = mm;

            // checkFormValidate();

            goback();

            $(".form img").on("click", function () {
                var src = $(this).attr("src");
                $("#img-dialog img").attr("src", src);
                $("#img-dialog").modal("show");
            });

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
                    if (checkFileType($(this))) {
                        preview(this);
                    }
                }
            });
        }

        /**
         * 检查文件类型
         * @param ele
         * @param suffix
         */
        function checkFileType(ele, suffix) {
            if (ele.val()) {
                var parts = ele.val().split(/\./);
                var ext = parts[parts.length - 1];
                if (ext) {
                    ext = ext.toLowerCase();
                    var reg = suffix || /(jpg|jpeg|png)$/;
                    if (!reg.test(ext)) {
                        showTipDialog("图片格式不正确，只支持jpg，jpeg，png");
                        return false;
                    }

                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
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
            if (ele.files && ele.files[0]) {// FileReader,IE10+、FF22/23、Chrome28/29支持
                if (typeof FileReader != 'undefined') {
                    var reader = new FileReader;
                    reader.onload = function (event) {
                        event = event || window.event;
                        var img = new Image();
                        img.onload = function () {
                            $(ele).parent().prev("img").attr("src", event.target.result);
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

                var target = $(ele).parent().prev("img");
                var parent = target.parent();
                var div;
                if (parent.children(".prev_img").length) {
                    div = parent.children(".prev_img");
                } else {
                    div = $("<div class='prev_img'></div>");
                    parent.prepend(div);
                }
                div.css({
                    "filter": "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)",
                    position: "absolute",
                    left: target.position().left,
                    top: target.position().top
                });
                div[0].filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = data;
                var w = target.width();
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
        };

        function downLoad(obj) {
            var type = obj.id;
            var fileName = obj.innerHTML;
            location.href = "${contextPath}/manage/historyEnterprise/downloadFile.html?requestId=${historyEnterprises.requestId!}"+ "&&type=" + type;
        }

    </script>
</body>
</html>