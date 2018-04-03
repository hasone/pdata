<!DOCTYPE html>
<#global contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-新增流量券</title>
    <meta name="keywords" content="流量平台 新增流量券"/>
    <meta name="description" content="流量平台 新增流量券"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .modal-dialog {
            width: 750px !important;
        }
    </style>

</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>新增流量券
        </h3>
    </div>

    <form id="dataForm" class="form"
          action="${contextPath}/manage/flowcard/save.html?${_csrf.parameterName}=${_csrf.token}" value="${_csrf.token}"
          method="post" enctype="multipart/form-data">
        <div class="tile mt-30">
            <div class="tile-header">
                活动信息
            </div>
            <div class="tile-content">
                <div class="row form">

                    <div class="col-md-6">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                        <input type="hidden" class="form-control" name="cmProductId" id="cmProductId"
                               value="${cmProductId!}">
                        <input type="hidden" class="form-control" name="cuProductId" id="cuProductId"
                               value="${cuProductId!}">
                        <input type="hidden" class="form-control" name="ctProductId" id="ctProductId"
                               value="${ctProductId!}">

                        <input type="hidden" class="form-control" name="correctMobiles" id="correctMobiles"
                               value="${correctMobiles!}">
                        <input type="hidden" class="form-control" name="invalidMobiles" id="invalidMobiles"
                               value="${invalidMobiles!}">

                        <input type="hidden" class="form-control" name="cmMobileList" id="cmMobileList"
                               value="${cmMobileList!}">
                        <input type="hidden" class="form-control" name="cuMobileList" id="cuMobileList"
                               value="${cuMobileList!}">
                        <input type="hidden" class="form-control" name="ctMobileList" id="ctMobileList"
                               value="${ctMobileList!}">

                        <input type="hidden" class="form-control" name="cmUserSet" id="cmUserSet" value="${cmUserSet!}">
                        <input type="hidden" class="form-control" name="cuUserSet" id="cuUserSet" value="${cuUserSet!}">
                        <input type="hidden" class="form-control" name="ctUserSet" id="ctUserSet" value="${ctUserSet!}">

                        <input type="hidden" class="form-control" name="activityId" id="activityId"
                               value="${activities.activityId!}">

                        <input type="hidden" class="form-control" name="isSaved" id="isSaved">

                        <input type="hidden" class="form-control" name="type" id="type" value="5">

                        <div class="form-group">
                            <label style="font-weight:700 ">企业名称：</label>
                            <input type="hidden" name="entId" id="entId" value="${enterprises.id!}" required>
                            <span>${enterprises.name!}</span>
                        </div>

                        <div class="form-group">
                            <label>活动名称：</label>
                            <input type="hidden" name="name" id="name" value="${activities.name!}" required>
                            <span>${activities.name!}</span>
                        </div>

                    <#if hasInvalidMobiles == "true">
                        <div class="form-group">
                            <div>
                                <label>错误手机号文件下载：</label>
                                <a class='btn-icon icon-detail mr-5' onclick="downloadErrorPhone()">错误手机号文件</a>
                            </div>
                        </div>
                    </#if>
                    </div>

                    <div class="col-md-6">
                        <div class="form-group">
                            <label>企业余额：</label>
                            <span id="balance">${balance!}</span>
                        </div>

                        <div class="form-group">
                            <label>活动时间：</label>
                            <span>${(activities.startTime?date)!}</span>&nbsp;&nbsp;至&nbsp;&nbsp;<span>${(activities.endTime?date)!}</span>
                            <input type="hidden" name="startTime" id="startTime" readonly="readonly"
                                   value="${(activities.startTime?date)!}" required>
                            <input type="hidden" name="endTime" id="endTime" readonly="readonly"
                                   value="${(activities.endTime?date)!}">
                            <label class="error error-tip" id="time_tip"></label>
                        </div>
                    </div>

                </div>
            </div>
        </div>

        <div class="tile mt-30">
            <div class="tile-header">活动规模</div>
            <div class="tile-content">
                <label>估计:</label>
                <span id='activityInfo'> </span>
            </div>
        </div>

        <div class="tile mt-30">
            <div class="tile-header">
                奖品信息
            </div>
            <div class="tile-content">
                <div class="">
                    <div class="table-responsive mt-20">
                        <table class="table table-bordered text-center award-table">
                            <thead>
                            <tr>
                                <th width="10%">选择产品</th>
                                <th width="20%">产品名称</th>
                                <th width="20%">产品编码</th>
                                <th width="10%">产品大小</th>
                                <th width="10%">售出价格</th>
                                <th width="15%">使用范围</th>
                                <th width="15%">漫游范围</th>
                            </tr>
                            </thead>
                            <tbody>
                            <#if cmProduct??>
                            <tr>
                                <td>
                                    <span style="width:80px;">移动</span>
                                </td>
                                <td>
                                    <span class="cmProdName" name="cmProdName" id="cmProdName"
                                          style="width:80px;">${cmProduct.name!}</span>
                                </td>
                                <td>
                                    <span class="cmProdCode" name="cmProdCode" id="cmProdCode"
                                          style="width:80px;">${cmProduct.productCode!}</span>
                                </td>
                                <td>
                                    <span class="cmProdSize" name="cmProdSize" id="cmProdSize"
                                          style="width:80px;">${cmProductSize!}</span>
                                </td>
                                <td>
                                    <span class="cmProdPrice" name="cmProdPrice" id="cmProdPrice"
                                          style="width:80px;">${cmPrice!}元</span>
                                </td>
                                <td>
                                    <span class="cmOwnership" name="cmOwnership" id="cmOwnership"
                                          style="width:80px;">${cmProduct.ownershipRegion!}</span>
                                </td>
                                <td>
                                    <span class="cmRoaming" name="cmRoaming" id="cmRoaming"
                                          style="width:80px;">${cmProduct.roamingRegion!}</span>
                                </td>
                            </tr>
                            </#if>

                            <#if cuProduct??>
                            <tr>
                                <td>
                                    <span style="width:80px;">联通</span>
                                </td>
                                <td>
                                    <span class="cuProdName" name="cuProdName" id="cuProdName"
                                          style="width:80px;">${cuProduct.name!}</span>
                                </td>
                                <td>
                                    <span class="cuProdCode" name="cuProdCode" id="cuProdCode"
                                          style="width:80px;">${cuProduct.productCode!}</span>
                                </td>
                                <td>
                                                <span class="cuProdSize" name="cuProdSize" id="cuProdSize"
                                                      style="width:80px;">
                                                ${cuProductSize!}
                                                </span>
                                </td>
                                <td>
                                                <span class="cuProdPrice" name="cuProdPrice" id="cuProdPrice"
                                                      style="width:80px;">
                                                ${cuPrice!}
                                                </span>
                                </td>
                                <td>
                                    <span class="cuOwnership" name="cuOwnership" id="cuOwnership"
                                          style="width:80px;">${cuProduct.ownershipRegion!}</span>
                                </td>
                                <td>
                                    <span class="cuRoaming" name="cuRoaming" id="cuRoaming"
                                          style="width:80px;">${cuProduct.roamingRegion!}</span>
                                </td>
                            </tr>
                            </#if>

                            <#if ctProduct??>
                            <tr>
                                <td>
                                    <span style="width:80px;">电信</span>
                                </td>
                                <td>
                                    <span class="ctProdName" name="ctProdName" id="ctProdName"
                                          style="width:80px;">${ctProduct.name!}</span>
                                </td>
                                <td>
                                    <span class="ctProdCode" name="ctProdCode" id="ctProdCode"
                                          style="width:80px;">${ctProduct.productCode!}</span>
                                </td>
                                <td>
                                                <span class="ctProdSize" name="ctProdSize" id="ctProdSize"
                                                      style="width:80px;">
                                                ${ctProductSize!}
                                                </span>
                                </td>
                                <td>
                                                <span class="ctProdPrice" name="ctProdPrice" id="ctProdPrice"
                                                      style="width:80px;">
                                                ${ctPrice!}
                                                </span>
                                </td>
                                <td>
                                    <span class="ctOwnership" name="ctOwnership" id="ctOwnership"
                                          style="width:80px;">${ctProduct.ownershipRegion!}</span>
                                </td>
                                <td>
                                    <span class="ctRoaming" name="ctRoaming" id="ctRoaming"
                                          style="width:80px;">${ctProduct.roamingRegion!}</span>
                                </td>
                            </tr>
                            </#if>
                            </tbody>
                        </table>
                        <span class="error-tip" id="award-error"></span>
                    </div>
                </div>
            </div>
        </div>
    </form>

    <form id="errorPhone"
          action="${contextPath}/manage/flowcard/downloadErrorPhones.html?${_csrf.parameterName}=${_csrf.token}"
          method="post">
        <input type="hidden" id="errorPhones" name="errorPhones" value="${invalidMobiles!}"/>
    </form>

    <div class="mt-30 text-center">
        <a href="javascript:void(0)" class="btn btn-warning btn-sm mr-10 dialog-btn cancel-btn" id="cancel-btn">上一步</a>&nbsp;&nbsp;
        <a href="javascript:void(0)" class="btn btn-warning btn-sm mr-10 dialog-btn save-btn" id="save-btn">保存</a>
        <span style='color:red' id="error_msg">${errorMsg!}</span>
    </div>

</div>

<div class="modal fade dialog-sm" id="tip-dialog">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content"></span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal">确 定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>

<script>
    /**
     * 更新活动规模
     */
    function updateActivityInfo() {

        var cmProdId = $("#cmProductId").val();
        var cuProdId = $("#cuProductId").val();
        var ctProdId = $("#ctProductId").val();

        var correctMobiles = $("#correctMobiles").val();

        var activityId = $("#activityId").val();
        var entId = $("#entId").val();

        $.ajax({
            type: "POST",
            data: {
                cmProdId: cmProdId,
                cuProdId: cuProdId,
                ctProdId: ctProdId,
                correctMobiles: correctMobiles,
                activityId: activityId,
                entId: entId
            },
            url: "${contextPath}/manage/flowcard/getActivityInfoAjax.html?${_csrf.parameterName}=${_csrf.token}",
            dataType: "json", //指定服务器的数据返回类型，
            success: function (res) {
                if (res.success && res.success == "success") {
                    var activityInfo = res.totalPrice + " 元=》 " + res.totalSize + " 流量=》 " + res.prizeCount + " 份奖品=》" + res.userCount + " 个中奖用户";
                    $("#activityInfo").html(activityInfo);
                }
                else {
                    $("#activityInfo").html("0 元=》 0MB 流量=》 0 份奖品=》0 个中奖用户");
                }
            }
        });
    }

    /**
     * 价格
     * */
    function pricefun(price) {
        if (price != 0) {
            return (price / 100.00).toFixed(2) + "";
        }
        return "0.00";
    }
    ;

    /**
     * 流量包大小
     * */
    function sizefun(size) {
        if (size == null) {
            return "-";
        }
        if (size < 1024) {
            return size + "KB";
        }
        if (size >= 1024 && size < 1024 * 1024) {
            return (size * 1.0 / 1024) + "MB";
        }
        if (size >= 1024 * 1024) {
            return (size * 1.0 / 1024 / 1024) + "GB";
        }

        return size * 1.0 / 1024 + "MB";
    }
    ;


    require(["common", "bootstrap", "daterangepicker"], function () {

        listeners();

        formValidate();

        //保存
        $("#save-btn").on("click", function () {
            $("#save-btn").addClass("disabled");
            showToast();
            $("#isSaved").val("1");
            $("#dataForm").submit();
        });

        //取消
        $("#cancel-btn").on("click", function () {
             history.go(-1);
        });
    });

    function listeners() {
        updateActivityInfo();
    }

    /**
     * 模拟文件选择器
     */
    function fileBoxHelper() {
        $(".file-helper").on("change", function () {
            var path = this.value;
            var lastsep = path.lastIndexOf("\\");
            var filename = path.substring(lastsep + 1);
            $(this).parent().children(".file-input").val(filename);
        });
    }

    /**
     *
     * @param msg
     */
    function showTip(msg) {
        $("#tip-dialog .message-content").html(msg);
        $("#tip-dialog").modal("show");
    }

    /**
     * 表单验证
     */
    function formValidate() {

        $("#dataForm").validate({
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
                entId: {
                    required: true
                },
                name: {
                    required: true,
                    cmaxLength: 64
                },
                startTime: {
                    required: true
                }
            },
            messages: {
                entId: {
                    required: "请选择企业!"
                },
                name: {
                    required: "请填写活动名称!",
                    cmaxLength: "活动名称不允许超过64个字符!"
                },
                startTime: {
                    required: "请选择活动时间!"
                }
            }
        });
    }

    function checkTimeInput() {
        var startTime = $('#startTime').val();
        var endTime = $('#endTime').val();
        if (startTime == '' || endTime == '') {
            $("#time_tip").show();
            $("#time_tip").html("请输入起止时间");
            return false;
        }
        return true;
    }

    function downloadErrorPhone() {
        $("#errorPhone").submit();
    }
</script>
</body>
</html>