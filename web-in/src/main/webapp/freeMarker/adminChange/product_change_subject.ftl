<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-用户修改变更审批</title>
    <meta name="keywords" content="流量平台 用户修改变更审批"/>
    <meta name="description" content="流量平台 用户修改变更审批"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .form {
            width: 350px;
            text-align: left;
        }

        .form label {
            width: 70px;
            text-align: right;
            margin-right: 10px;
        }

        .form .form-control {
            width: 230px;
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
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>用户修改审批
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>
    
     <div class="tile mt-30">
        <div class="tile-header">
            审批详情
        </div>
        <div class="tile-content">
            <div class="row">
                <div class="col-md-8 col-md-offset-2">
                    <table class="table text-center table-striped">
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
                        <#if opinions??>
                            <#list opinions as opinion>
                            <tr>
                                <td>${opinion.description!}</td>
                                <td>${opinion.userName!}</td>
                                <td>${opinion.managerName!}${opinion.roleName!}</td>
                                <td>${(opinion.updateTime?datetime)!}</td>
                                <td title="${opinion.comment!}">${opinion.comment!}</td>
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
          修改操作信息
        </div>
        <div class="tile-content">
            <div class="row">
                <div class="col-md-8 col-md-offset-2">
                    <div class="form-group">
                        <span class="detail-label">修改申请人:</span>
                        <span class="detail-value">${adminChangeDetail.creatorName!}</span>
                    </div>
                    <div class="form-group" style="word-wrap: break-word;">
                        <span class="detail-label">修改备注:</span>
                        <span class="detail-value">${adminChangeDetail.comment!}</span>
                    </div>
                </div>
            </div>
        </div>
    </div>

   

    <div class="tile mt-30">
        <div class="tile-header">
          用户修改信息
        </div>
        <div class="tile-content">
            <div class="row">
               <div class="col-md-8 col-md-offset-2">
                    <div class="form-group">
                        <span class="detail-label">用户角色:</span>
                        <span class="detail-value">企业关键人</span>
                    </div>
                    <div class="form-group">
                        <span class="detail-label">姓名:</span>
                        <span class="detail-value">${adminChangeDetail.destName!}</span>
                    </div>
                    <div class="form-group">
                        <span class="detail-label">手机号码：</span>
                        <span class="detail-value">${adminChangeDetail.destPhone!}</span>
                    </div>
                     <div class="form-group">
                        <span class="detail-label">企业名称：</span>
                        <span class="detail-value">${adminChangeDetail.enterName!}</span>
                    </div>
                    <div class="form-group">
                        <span class="detail-label">所属地区：</span>
                        <span class="detail-value">${adminChangeDetail.districtName!}</span>
                    </div>
                </div>

                <div class="col-md-8 col-md-offset-2">
                    <form id="dataForm" action="${contextPath}/manage/approval/saveApprovalAdminChange.html"
                          method="post">
                        <input type="hidden" name="requestId" id="requestId" value="${requestId!}"/>
                        <input type="hidden" name="approvalStatus" id="approvalStatus"/>
                        <input type="hidden" name="processId" id="processId" value="${processId!}"/>
                        <input type="hidden" name="approvalRecordId" id="approvalRecordId"
                               value="${approvalRecordId!}"/>
                        <div class="form-group">
                            <label style="vertical-align: top; display:block;">审批意见:</label>
                            <textarea style="resize: none; max-width: 100%; width: 100%" rows="8" name="comment"
                                      id="comment" class="prompt" maxlength="500"></textarea>
                        </div>
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
                </div>
            </div>
        </div>
    </div>

    <div class="text-center mt-30">
        <a class="btn btn-sm btn-success dialog-btn" id="agree_btn">审批通过</a>
        <a class="btn btn-sm btn-warning dialog-btn" id="reject_btn">驳回申请</a>
        <span style='color:red'>${errorMsg!}</span>
    </div>


</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>

    require(["knockout", "common", "bootstrap"], function (ko) {
        initFormValidate();

        submitForm();
    });

    /**
     * 表单验证
     */
    function initFormValidate() {
        $("#dataForm").validate({
            errorPlacement: function (error, element) {
                error.addClass("error-tip");
                element.parent().append(error);
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

    function submitForm() {
        $("#agree_btn").on("click", function () {
            if ($("#dataForm").validate().form()) {
                $("#approvalStatus").val(1);
                $("#dataForm").submit();
            }
            return false;
        });

        $("#reject_btn").on("click", function () {
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