<!DOCTYPE html>

<#assign contextPath = rc.contextPath >

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
        <h3>信息修改审批详情
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
        <div class="tile-header">修改备注</div>
        <div class="tile-content">
            <div style="word-break: break-all; display: inline-block;">${enterprise.comment!}</div>
        </div>
    </div>

    <div class="tile mt-30">
        <div class="tile-header">
            修改详情
        </div>
        <div class="tile-content" style="padding: 10px 80px;">

            <div class="form" id="firstStep" hidden>
                <div class="form-group">
                    <label id="name">企业名称：</label>
                    <span>${enterprise.name!}</span>
                </div>
                <div class="form-group">
                    <label id="entName">企业简称：</label>
                    <span>${enterprise.entName!}</span>
                </div>
                <div class="form-group">
                    <label id="code">企业编码：</label>
                    <span>${enterprise.code!}</span>
                </div>
                <div class="form-group">
                    <label id="district">地区：</label>
                    <span>${enterprise.cmManagerName!}</span>
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
                        <label id="discount">折扣：</label>
                        <span>${enterprise.discountName}</span>
                    </div>
                    <div class="form-group">
                        <label id="giveMoneyId">存送：</label>
                        <span>${enterprise.giveMoneyName}</span>
                    </div>
                </#if>

                <#if flag??&&flag==1>
                    <div class="form-group">
                        <label id="entCredit">信用额度：</label>
                        <span>${minCount!}</span>
                    </div>
                </#if>

                <div class="form-group">
                    <label id="email">企业联系邮箱：</label>
                    <span>${enterprise.email!}</span>
                </div>

                <div class="form-group">
                    <label id="phone">企业联系电话：</label>
                    <span>${enterprise.phone!}</span>
                </div>

                <#list enterpriseManagers as enterpriseManager>
                    <div class="form-group">
                        <label id="enterpriseManagerName">企业管理员姓名：</label>
                        <span type="text" name="userName" id="userName"> ${(enterpriseManager.userName)!} </span>
                    </div>
                    <div class="form-group">
                        <label id="enterpriseManagerPhone">企业管理员手机号码：</label>
                        <span type="text" name="mobilePhone" id="mobilePhone">${(enterpriseManager.mobilePhone)!}</span>
                    </div>
                </#list>

                <div class="form-group">
                    <label id="customerManagerName">客户经理姓名：</label>
                    <span type="text" name="userName" id="userName"> ${(customerManager.userName)!} </span>
                </div>


                <div class="form-group">
                    <label id="customerManagerPhone">客户经理手机号码：</label>
                    <span type="text" name="mobilePhone" id="mobilePhone">${(customerManager.mobilePhone)!}</span>
                </div>

                <#if enterprise?? && enterprise.cmEmail??>
                    <div class="form-group">
                        <label id="customerManagerEmail">客户经理邮箱：</label>
                        <span type="text" name="email" id="email"> ${(enterprise.cmEmail!)!} </span>
                    </div>
                </#if>

                <div class="btn-save mt-30 text-center">
                    <a href="javascript:void(0)" class="btn btn-sm btn-warning dialog-btn" onclick="nextStep()"
                       id="next-btn">下一步</a>
                </div>
            </div>


            <!--第二页-->
            <div style="display: none;" id="secondStep">
                <div class="form">
                    <#if enterprise?? && enterprise.startTime?? && enterprise.endTime??>
                        <div class="form-group">
                            <label id="cooperationTime">合作时间：</label>
                            <span>${(enterprise.startTime?datetime)!}</span>&nbsp;&nbsp;~&nbsp;&nbsp;<span>${(enterprise.endTime?datetime)!}</span>
                        </div>
                    </#if>

                    <div class="form-group">
                        <label id="licenceImage">工商营业执照：</label>
                        <a name="businessLicence" id="businessLicence" onclick="downLoad(this)"
                           style="width: 242px;">${businessLicence!}</a>
                    </div>

                    <div class="form-group">
                        <label id="licenceTime">营业执照有效期：</label>
                        <span>${(enterprise.licenceStartTime?datetime)!}</span>&nbsp;&nbsp;~&nbsp;&nbsp;<span>${(enterprise.licenceEndTime?datetime)!}</span>
                    </div>

                    <div class="form-group">
                        <label>企业管理员授权证明：</label>
                        <a name="authorization" id="authorization" onclick="downLoad(this)"
                           style="width: 242px;">${authorization!}</a>
                    </div>

                    <div class="form-group">
                        <label>企业管理员身份证扫描件（正面）：</label>
                        <a name="identification" id="identification" onclick="downLoad(this)"
                           style="width: 242px;">${identification!}</a>
                    </div>

                    <div class="form-group">
                        <label>企业管理员身份证扫描件（反面）：</label>
                        <a name="identificationBack" id="identificationBack" onclick="downLoad(this)"
                           style="width: 242px;">${identificationBack!}</a>
                    </div>

                    <#if customerfile??>
                        <div class="form-group">
                            <label>客服说明附件：</label>
                            <a name="customerfile" id="customerfile" onclick="downLoad(this)"
                               style="width: 242px;">${customerfile!}</a>
                        </div>
                    </#if>

                    <#if contract??>
                        <div class="form-group">
                            <label>合作协议文件：</label>
                            <a name="contract" id="contract" onclick="downLoad(this)"
                               style="width: 242px;">${contract!}</a>
                        </div>
                    </#if>

                    <#if image??>
                        <div class="form-group">
                            <label>审批截图：</label>
                            <a name="image" id="image" onclick="downLoad(this)" style="width: 242px;">${image!}</a>
                        </div>
                    </#if>
                </div>

                <form id="dataForm" action="${contextPath}/manage/approval/saveEntInfoChangeApproval.html" method="post" class="mt-30">
                    <#--<div class="form-group">-->
                        <input type="hidden" name="enterId" id="enterId" value="${(enterprise.id)!}">
                        <input type="hidden" name="approvalRecordId" id="approvalRecordId"
                               value="${approvalRecordId!}">
                        <input type="hidden" name="requestId" id="requestId" value="${requestId!}">
                        <input type="hidden" name="processId" id="processId" value="${processId!}">
                        <input type="hidden" name="approvalStatus" id="approvalStatus">
                        <label style="vertical-align: top">审批意见：</label>
                        <textarea name="comment" cols="20" id="comment" maxlength="300"></textarea>
                    <#--</div>-->


                    <div class="mt-30 text-center">
                        <a href="javascript:void(0)" class="btn btn-sm btn-warning dialog-btn"
                           onclick="lastStep()"
                           id="last-btn">上一步</a>&nbsp;&nbsp;&nbsp;&nbsp;
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

        function nextStep() {
            $("#firstStep").hide();
            $("#secondStep").show();
        }

        function lastStep() {
            $("#firstStep").show();
            $("#secondStep").hide();
        }

        var convertTable={
        };

        var renderDataUrl = "${contextPath}/manage/entPropsInfo/query.html?${_csrf.parameterName}=${_csrf.token}";

        require(["common", "bootstrap"], function () {
            //渲染页面
            render(renderDataUrl, convertTable, "firstStep");

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

        function downLoad(obj) {
            var type = obj.id;
            var fileName = obj.innerHTML;
            location.href = "${contextPath}/manage/enterprise/downloadFile.html?enterpriseId=${enterprise.id}" + "&&type=" + type;
        }
    </script>
</body>
</html>