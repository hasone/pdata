<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-潜在客户详情</title>
    <meta name="keywords" content="流量平台 修改潜在客户"/>
    <meta name="description" content="流量平台 修改潜在客户"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

    <!-- easy-ui-->
    <link rel="stylesheet" type="text/css" href="${contextPath}/assets/easyui/themes/default/easyui.css">

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        form {
            width: 900px;
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
            width: 300px;
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

        .form-group span {
            word-break: break-all;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>企业详情
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>
    <div class="tile mt-30">
        <div class="tile-content text-center">
            <form class="form" id="potentialCustomer"
                  action="${contextPath}/manage/potentialCustomer/saveEditPotential.html" method="post" hidden>
                <input name="id" <#if pc.id??>value="${pc.id}"</#if> type="hidden">

                <div class="form-group form-inline">
                    <label id="name">企业名称：</label>
                    <span>${(pc.name)!}</span>
                </div>

                <div class="form-group form-inline">
                    <label id="entName">企业简称：</label>
                    <span>${(pc.entName)!}</span>
                </div>

                <div class="form-group form-inline">
                    <label id="email">企业联系邮箱：</label>
                    <span>${(pc.email)!}</span>
                </div>

                <div class="form-group form-inline">
                    <label id="phone">企业联系电话：</label>
                    <span>${(pc.phone)!}</span>
                </div>

                <div class="form-group form-inline">
                    <label id="enterpriseManagerName">企业管理员姓名：</label>
                    <span>${(pc.enterpriseManagerName)!}</span>
                </div>

                <div class="form-group form-inline">
                    <label id="enterpriseManagerPhone">企业管理员手机号码：</label>
                    <span>${(pc.enterpriseManagerPhone)!}</span>
                </div>

                <div class="form-group form-inline">
                    <label id="customerManagerPhone">客户经理手机号码：</label>
                    <span>${(pc.cmPhone)!}</span>
                </div>

                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>

        </div>
    </div>


</div>


<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>

<script>
    var convertTable={
    };

    var renderDataUrl = "${contextPath}/manage/entPropsInfo/query.html?${_csrf.parameterName}=${_csrf.token}";

    require(["common", "bootstrap", "daterangepicker", "easyui"], function (a, b, c) {
        //渲染页面
        render(renderDataUrl, convertTable);

        //初始化日期组件
        initDateRangePicker();
        initTree();

//        //保存
//        $("#save-btn:not('.disabled')").on("click", function () {
//            $(this).addClass("disabled");
//            $("#potentialCustomer").submit();
//            return false;
//        });
    });

    /**
     *
     * @param msg
     */
    function showTip(msg) {
        $("#tip-dialog .message-content").html(msg);
        $("#tip-dialog").modal("show");
    }

    /**
     * combotree构建
     */
    function initTree() {
        $('#districtId').combotree({
            url: "${contextPath}/manage/enterprise/getDistrictAjax.html?${_csrf.parameterName}=${_csrf.token}&districtId=${(pc.districtId)!}",
            onBeforeExpand: function (node) {
                $('#districtId').combotree("tree").tree("options").url = "${contextPath}/manage/enterprise/getDistrictAjax.html?${_csrf.parameterName}=${_csrf.token}&parentId=" + node.id;
            },
            onSelect: function (node) {
                $("#districtIdSelect").val(node.id);
            }
        });
    }

    /**
     * 初始化日期选择器
     */
    function initDateRangePicker() {
        var ele = $('#activeTime');

        var startEle = $('#startTime');
        var endEle = $('#endTime');
        ele.dateRangePicker({
            startDate: new Date(),
            separator: ' ~ ',
            getValue: function () {
                if (startEle.val() && endEle.val())
                    return startEle.val() + ' ~ ' + endEle.val();
                else
                    return '';
            },
            setValue: function (s, s1, s2) {
                startEle.val(s1);
                endEle.val(s2);
                var body = getTemplateRoot();
                $(".startDate", body).html(s1);
                $(".endDate", body).html(s2);

                var validator = $("#dataForm").validate();
                if (validator.check(startEle[0])) {
                    var err = validator.errorsFor(startEle[0]);
                    if (err && err.length) {
                        err.hide();
                    }
                }

                if (validator.check(endEle[0])) {
                    var err = validator.errorsFor(endEle[0]);
                    if (err && err.length) {
                        err.hide();
                    }
                }
            }
        });
    }
</script>

</body>
</html>