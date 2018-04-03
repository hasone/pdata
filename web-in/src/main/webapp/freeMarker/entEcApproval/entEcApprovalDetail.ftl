<!DOCTYPE html>

<#assign contextPath = rc.contextPath >

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-EC更改审批详情</title>
    <meta name="keywords" content="流量平台 EC更改审批详情"/>
    <meta name="description" content="流量平台 EC更改审批详情"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .form input[type='text'],
        .form input[type='number'],
        .form select {
            background: transparent;
            border: 1px solid #ccc;
            outline: none;
            border-radius: 3px;
            padding: 3px 10px;
            width: 220px;
        }

        span.error {
            color: red;
            margin-left: 10px;
        }

        #comment {
            width: 400px;
            resize: none;
            height: 80px;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>EC更改审批详情
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>


    <div class="tile mt-30">
        <div class="tile-header">
            审批意见
        </div>
        <div class="tile-content">
            <div role="tabpanel" class="tab-pane active table-responsive" id="table_wrap1">
                <div class="table-responsive">
                    <table class="table table-indent text-center table-bordered-noheader mb-0">
                        <thead>
                        <tr>
                            <th>审批状态</th>
                            <th>审批用户</th>
                            <th>用户职位</th>
                            <th>审批时间</th>
                            <th>审批意见</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#if approvalRecords??>
                            <#list approvalRecords as record>
                            <tr>
                                <td>${record.description!}</td>
                                <td>${record.userName!}</td>
                                <td>${record.managerName!}${record.roleName!}</td>
                                <td>${(record.updateTime?datetime)!}</td>
                                <td title="${record.comment!}">${record.comment!}</td>
                            </tr>
                            </#list>
                        </#if>

                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <div class="tile mt-30">
        <div class="tile-header">
            EC更改详情
        </div>
        <div class="tile-content" style="padding: 10px 80px;">

            <div class="form" id="firstStep">
                <div class="form-group">
                    <label>企业IP白名单地址1：</label>
                    <span>${ecApprovalDetail.ip1!}</span>
                </div>
                <div class="form-group">
                   <label>企业IP白名单地址2：</label>
                    <span>${ecApprovalDetail.ip2!}</span>
                </div>
                <div class="form-group">
                    <label>企业IP白名单地址3：</label>
                    <span>${ecApprovalDetail.ip3!}</span>
                </div>
                <div class="form-group" style="word-break: break-all;">
                    <label>企业回调地址：</label>
                    <span style="word-break: break-all; display: inline-block;">${ecApprovalDetail.callbackUrl!}</span>
                </div>
                
                <form id="dataForm" action="${contextPath}/manage/approval/saveEnterpriseEcApproval.html" method="post" class="mt-30">
                    <div class="form-group">
                        <input type="hidden" name="enterId" id="enterId" value="${(ecApprovalDetail.entId)!}">
                        <input type="hidden" name="approvalRecordId" id="approvalRecordId"
                               value="${approvalRecordId!}">
                        <input type="hidden" name="requestId" id="requestId" value="${requestId!}">
                        <input type="hidden" name="processId" id="processId" value="${processId!}">
                        <input type="hidden" name="approvalStatus" id="approvalStatus">
                        <div style="vertical-align: top">审批意见：</div>
                        <textarea name="comment" cols="20" id="comment" maxlength="300"></textarea>
                    </div>
                    
                     <div class="mt-30 text-center">
                        <a class="btn btn-sm btn-warning dialog-btn" id="reject-btn">驳回</a>
                        <a class="btn btn-sm btn-warning dialog-btn" id="submit-btn">通过</a>
                    </div>

                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
            </div>

        </div>
    </div>


    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/es5-shim.min.js"></script>
    <script src="${contextPath}/assets/lib/es5-sham.min.js"></script>
    <![endif]-->
    <script src="${contextPath}/assets/lib/require.js"></script>
    <script src="${contextPath}/assets/js/config.js"></script>

    <script>

        require(["common", "bootstrap"], function () {

            initFormValidate();

            submitForm();
        });

        /**
         * 表单验证
         */
        function initFormValidate() {
            $("#dataForm").validate({
                rules: {
                    comment: {
                        required: true,
                        maxlength: 300
                    }
                },
                errorElement: "span",
                messages: {
                    comment: {
                        required: "请填写审批意见",
                        maxlength: "审批意见不超过300个字符"
                    }
                }
            });
        }

        function submitForm() {
            $("#submit-btn").on("click", function () {
                if ($("#dataForm").validate().form()) {
                    $("#approvalStatus").val(1);
                    $("#dataForm").submit();
                }
                return false;
            });

            $("#reject-btn").on("click", function () {
                if ($("#dataForm").validate().form()) {
                    $("#approvalStatus").val(0);
                    $("#dataForm").submit();
                }
                return false;
            });
        }

        
    </script>
</body>
</html>