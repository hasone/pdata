<!DOCTYPE html>

<#assign contextPath = rc.contextPath >

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-审批充值</title>
    <meta name="keywords" content="流量平台 审批充值"/>
    <meta name="description" content="流量平台 审批充值"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>

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
            width: 200px;
            resize: none;
            height: 80px;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>审批充值
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
                            <th>时间</th>
                            <th>角色</th>
                            <th>用户名</th>
                            <th>意见</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#if approvalRecords??>
                            <#list approvalRecords as record>
                            <tr>
                                <td>${record.createTime?datetime}</td>
                                <td>${record.roleName!}</td>
                                <td>${record.userName!}</td>
                                <td title="${record.operatorComment!}">${record.operatorComment!}</td>
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
            企业信息
        </div>
        <div class="tile-content">
            <div class="row form">
                <form id="dataForm" action="${contextPath}/manage/accountChange/saveAccountChangeApproval.html"
                      method="post">
                    <div class="col-md-6 col-md-offset-4">
                        <div class="form-group">
                            <label>申请充值时间：</label>
                            <span>${accountChangeRequest.createTime?datetime}</span>
                        </div>
                        <div class="form-group">
                            <label>充值额度：</label>
                            <span>${accountChangeRequest.count}</span>
                        </div>
                        <div class="form-group">
                            <label>客户经理：</label>
                            <span>${customerManager.userName!}</span>
                        </div>
                        <div class="form-group">
                            <label>客户经理手机号：</label>
                            <span>${customerManager.mobilePhone!}</span>
                        </div>
                        <div class="form-group">
                            <label>企业名称：</label>
                            <span>${Enterprises.name!}</span>
                        </div>
                        <div class="form-group">
                            <label>企业编码：</label>
                            <span>${Enterprises.code!}</span>
                        </div>
                        <div class="form-group">
                            <label>所属地区：</label>
                            <span>${fullDistrictName!}</span>
                        </div>

                        <div class="form-group">
                            <label>创建时间：</label>
                            <span>${Enterprises.createTime?datetime}</span>
                        </div>
                        <div class="form-group">
                            <label>余额：</label>
                            <span>${(account.count/100.0)?string("0.00")}</span>
                        </div>


                        <div class="form-group">
                            <input type="hidden" value="${accountChangeRequest.id}" name="accountChangeRequestId"
                                   id="accountChangeRequestId">
                            <input type="hidden" name="approvalStatus" id="approvalStatus">
                    		<input type="hidden" id="approvalType" name="approvalType" value="${approvalType!}"/>                   	
                            <label style="vertical-align: top">审批意见：</label>
                            <textarea name="comment" id="comment" maxlength="300"></textarea>
                        </div>
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
            </div>

        </div>
    </div>


    <div class="mt-30 text-center">
        <a class="btn btn-sm btn-warning dialog-btn" id="reject-btn">驳回</a>
        <a class="btn btn-sm btn-warning dialog-btn" id="submit-btn">提交审批</a>
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
                $("#approvalStatus").val(2);
                $("#dataForm").submit();
            }
            return false;
        });
    }
</script>
</body>
</html>