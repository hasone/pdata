<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-营销卡制卡</title>
    <meta name="keywords" content="流量平台 营销卡制卡"/>
    <meta name="description" content="流量平台 营销卡制卡"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>

    <!-- easy-ui-->
<#--<link rel="stylesheet" type="text/css" href="${contextPath}/assets/easyui/themes/default/easyui.css">-->
<#--<link rel="stylesheet" type="text/css" href="${contextPath}/assets/easyui/themes/icon.css">-->

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .form {
            width: 1500px;
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
            width: 150px;
            text-align: right;
        }

        .form .form-group .prompt {
            padding-left: 305px;
            color: #bbb;
            padding-bottom: 0;
            margin-bottom: 0;
        }

        .btn-save {
            margin-left: 300px;
        }

        .dropdown-menu {
            max-height: 360px;
        }
    </style>

    <link rel="stylesheet" type="text/css" href="${contextPath}/manage2/Js/artDialog/skins/default.css">
    <script src="${contextPath}/manage2/Js/artDialog/artDialog.js"></script>
    <script src="${contextPath}/manage2/Js/artDialog/jquery.artDialog.js"></script>
</head>

<body>
<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>新建营销卡数据
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back"
               onclick="javascript:window.location.href='${contextPath}/manage/mdrc/batchconfig/cardmakeIndex.html'">返回</a>
        </h3>
    </div>

    <#if wheatherNeedApproval??&&wheatherNeedApproval=="true">
        <div class="tile mt-30">
            <div class="tile-header">历史审批记录</div>
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

    <div class="tile mt-30">
        <div class="tile-header"></div>
        <div class="tile-content">
            <div class="row form">
                <form action="${contextPath}/manage/mdrc/batchconfig/save.html?${_csrf.parameterName}=${_csrf.token}" method="post"
                      name="adminForm" id="table_validate">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label>卡名称：</label>
                            <span>${mdrcCardmakeDetail.configName!}</span>
                        </div>

                        <div class="form-group">
                            <label>制卡数量：</label>
                            <span>${mdrcCardmakeDetail.amount!}</span>
                        </div>

                        <div class="form-group">
                            <label>有效期：</label>
                            <span>${(mdrcCardmakeDetail.startTime?datetime)!}&nbsp;~&nbsp;
                            ${(mdrcCardmakeDetail.endTime?datetime)!}
                                </span>
                        </div>

                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label>卡模板：</label>
                            <span>${mdrcTemplate.name!}</span>
                        </div>

                        <div class="form-group">
                            <label>卡面主题:</label>
                            <span>${mdrcTemplate.theme!}</span>
                        </div>

                        <div class="form-group">
                            <label>卡面值:</label>
                            <span id="productSize"></span>
                        </div>


                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
            </div><!--row form -->
        </div> <!--tile-content -->
    </div>


    <div id="dialogContent" style="display:none;width:600px;">
        <div>
            <font style="font-weight:bold;font-size:14px;">正在生成数据，请稍候。。。</font>
            <img src="${contextPath}/manage2/assets/images/load-16-16.gif"/>
        </div>
    </div>

    <div class="mt-30 text-center">
        <a class="btn btn-sm btn-warning dialog-btn" id="submit-btn">生成卡数据</a>
        &nbsp;&nbsp;&nbsp;&nbsp;<span style='color:red' id="errorTip"></span>
    </div>

</div>



<div class="modal fade dialog-sm" id="submit-dialog">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content">一旦确认，则生成卡数据！</span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal" id="sure">确 定</button>
                <button class="btn btn-warning btn-sm" data-dismiss="modal">取 消</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<div class="modal fade dialog-sm" id="tips-dialog">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content" id="tips"></span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal" id="tips-btn">确 定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    require(["moment", "common", "bootstrap", "easyui", "daterangepicker"], function (mm) {
        window.moment = mm;

        init();
        
        $("#tips-btn").on("click", function () {
            var code = $("#tips").val();
            //由于已经被处理，直接返回列表页面
            if(code == 2){
            	toIndex();
            }
        });

    });

    function init() {
        //提交表单
        $("#sure").on("click", function () {

            doSubmit();
            //$("#table_validate").submit();
        });

        submitForm();

        getProductSize();
    }

    function getProductSize() {
        var productSize = ${mdrcTemplate.productSize!};
        $("#productSize").html(sizeFun(productSize));
    }

    function sizeFun(size) {
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

    function submitForm() {
        $("#submit-btn").on("click", function () {
            if ($("#table_validate").validate().form()) {
                $("#submit-dialog").modal("show");
            }
            return false;
        });
    }

    function toIndex() {
        window.location.href = "${contextPath}/manage/mdrc/batchconfig/cardmakeIndex.html";
    }

    function doSubmit() {
        var configName = "${mdrcCardmakeDetail.configName!}";
        var cardmakerId = "${mdrcCardmakeDetail.cardmakerId!}";
        var templateId = "${mdrcCardmakeDetail.templateId}";
        var amount = "${mdrcCardmakeDetail.amount!}";
        var startTime = "${(mdrcCardmakeDetail.startTime?datetime)!}";
        var endTime = "${(mdrcCardmakeDetail.endTime?datetime)!}";

        var requestId = "${mdrcCardmakeDetail.requestId!}";

        var data = {
            'configName': configName,
            'cardmakerId': cardmakerId,
            'templateId': templateId,
            'amount': amount,
            'startTime': startTime,
            'endTime': endTime,
            'requestId': requestId
        };

        showDialog();
        doSave(data);

        return false;
    }

    var dialog = null;

    function showDialog() {
        if (dialog == null) {
            dialog = art.dialog({
                lock: true,
                title: '营销卡数据',
                content: $('#dialogContent')[0],
                drag: false,
                resize: false,
                close: function () {
                    this.hide();
                    return false;
                }
            });
        } else {
            dialog.show();
        }
    }

    function doSave(data) {
        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            url: "${contextPath}/manage/mdrc/batchconfig/saveAjax.html",
            type: "POST",
            dataType: "json",
            async: true,
            data: data,
            success: function (data) {
                if (data && data.code && data.code === 'ok') {
                    toIndex();
                } else {
                    if (dialog != null) {
                        dialog.close();
                    }
                    
                	$("#tips").html(data.msg);
                	$("#tips").val(data.code);
                	$("#tips-dialog").modal("show");
                    
                    <#--
                    if(data && data.code && data.code == '1'){
                        $("#errorTip").text("该制卡商已被删除,生成卡数据失败!");
                    }
                    else {
                        $("#errorTip").text("配置信息有误，请检查后重新提交。");
                    }
                    -->
                }
            },
            error: function () {
                <#-- $("#errorTip").text("网络错误，请稍候重新尝试。"); -->
                $("#tips").html(data.msg);
                $("#tips").val(data.code);
                $("#tips-dialog").modal("show");
            }
        });
    }

</script>

</body>
</html>