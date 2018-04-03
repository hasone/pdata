<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-修改潜在客户</title>
    <meta name="keywords" content="流量平台 修改潜在客户"/>
    <meta name="description" content="流量平台 修改潜在客户"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Tree.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

    <!-- easy-ui-->
    <link rel="stylesheet" type="text/css" href="${contextPath}/assets/easyui/themes/default/easyui.css">

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .form {
            width: 800px;
            text-align: left;
        }

        .form label {
            width: 180px;
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

        .form .error-tip {
            display: block;
            margin-left: 193px;
        }

        .form .form-group .prompt {
            margin-left: 193px;
            color: #bbb;
            padding-bottom: 0;
            margin-bottom: 0;
        }

        .btn-save {
            margin-left: 550px;
        }


    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>编辑潜在客户
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back"
               onclick="window.location.href='${contextPath}/manage/potentialCustomer/indexPotential.html'">返回</a>
        </h3>
    </div>

<#if hasApproval??&&hasApproval=="true">
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
                            <th>用户职位</th>
                            <th>审批时间</th>
                            <th>审批意见</th>
                        </tr>
                        </thead>
                        <tbody>
                            <#if approvalRecords??>
                                <#list approvalRecords as approvalRecord>
                                <tr>
                                    <td>${approvalRecord.description!}</td>
                                    <td>${approvalRecord.userName!}</td>
                                    <td>${approvalRecord.managerName!}</td>
                                    <td>${(approvalRecord.updateTime?datetime)!}</td>
                                    <td>${approvalRecord.comment!}</td>
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

        <div class="tile-content text-center">
            <form class="form" id="potentialCustomer" style="display: inline-block;"
                  action="${contextPath}/manage/potentialCustomer/saveEditPotential.html" method="post">
                <input name="id" <#if pc.id??>value="${pc.id}"</#if> type="hidden">

                <div class="form-group form-inline">
                    <label>企业名称：</label>
                    <input required name="name" id="name" type="text" class="form-control" maxLength="64"
                           <#if pc.name??>value="${pc.name}"</#if>>
                </div>

                <div class="form-group form-inline">
                    <label>企业简称：</label>
                    <input required name="entName" id="entName" type="text" class="form-control" maxLength="64"
                           <#if pc.entName??>value="${pc.entName}"</#if>>
                </div>

                <div class="form-group form-inline">
                    <label>企业联系邮箱：</label>
                    <input required name="email" id="email" type="text" class="form-control" maxLength="64"
                           <#if pc.email??>value="${pc.email}"</#if>>
                    <div class="prompt">平台所有通知信息将发送至该邮箱</div>
                </div>

                <div class="form-group form-inline">
                    <label>企业联系电话：</label>
                    <input required name="phone" id="phone" type="text" class="form-control" maxLength="64"
                           <#if pc.phone??>value="${pc.phone}"</#if>>
                    <div class="prompt"> 请填写企业联系电话，要求是座机</div>
                </div>

                <div class="form-group form-inline">
                    <label>企业管理员姓名：</label>
                    <input required name="enterpriseManagerName" id="enterpriseManagerName" type="text" maxLength="64"
                           class="form-control"
                           <#if pc.enterpriseManagerName??>value="${pc.enterpriseManagerName}"</#if>>
                </div>

                <div class="form-group form-inline">
                    <label>企业管理员手机号码：</label>
                    <input required name="enterpriseManagerPhone" maxlength="11" id="enterpriseManagerPhone" type="text"
                           class="form-control"
                           <#if pc.enterpriseManagerPhone??>value="${pc.enterpriseManagerPhone}"</#if>>
                </div>

                <div class="form-group form-inline">
                    <label>地区：</label>
                    <div class="btn-group btn-group-sm" id="tree" required></div>
                    <input id="managerId" name="managerId" style="width: 0; height: 0; opacity: 0"
                           value="${cmManager.id!}">
                </div>

                <div class="form-group form-inline">
                    <label>客户经理手机号码(可选)：</label>
                    <input name="customerManagerPhone" maxlength="11" id="customerManagerPhone" type="text"
                           class="form-control" onblur="getPhoneArea()"
                           <#if pc.cmPhone??>value="${(pc.cmPhone)!}"</#if>>
                </div>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
        </div>
    </div>

    <div class="mt-30 btn-save">
        <a href="javascript:void(0)" class="btn btn-sm btn-warning dialog-btn" style="width: 114px;"
           id="refuse-btn">取消</a>
        <a href="javascript:void(0)" class="btn btn-warning btn-sm dialog-btn" style="width: 114px;"
           id="save-btn">保 存</a><span style='color:red' id="errorMsg">${errorMsg!}</span>
    </div>

</div>


<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>

<script>
    var comboTree;
    require(["common", "bootstrap", "daterangepicker", "easyui", "ComboTree", "react", "react-dom"], function (a, b, c, d, ComboTree, React, ReactDOM) {
        //初始化日期组件
        initDateRangePicker();

        checkFormValidate();

        //保存企业信息
        $("#refuse-btn").on("click", function () {
            window.location.href = "${contextPath}/manage/potentialCustomer/indexPotential.html";
            return false;
        });

        //保存
        $("#save-btn").on("click", function () {
            if ($("#potentialCustomer").validate().form()) {
                $("#potentialCustomer").submit();
            }
            return false;
        });

        comboTree = ReactDOM.render(React.createElement(ComboTree, {
            name: "managerId",
            url: "${contextPath}/manage/potentialCustomer/getManagerRoot.html"
        }), $("#tree")[0]);
        comboTree.setValue({"id": "${cmManager.id!}", "text": "${cmManager.name!}"});
        var tree = comboTree.getReference();
        tree.on("open", function (item) {
            if (item.open) {
                tree.deleteChildItems(item);
                ajaxData("${contextPath}/manage/potentialCustomer/getChildNode.html", {"parentId": item.id}, function (ret) {
                    if (ret) {
                        tree.loadDynamicJSON(item, ret);
                    }
                });

            }
        });

        //选中区域，回填号码
        tree.on("select", function (item) {
            $("#customerManagerPhone").val("");
            $("#managerId").val(item.id);
            ajaxData("${contextPath}/manage/potentialCustomer/getAreaPhone.html", {"managerId": item.id}, function (ret) {
                if (ret && ret.phone) {
                    $("#customerManagerPhone").val(ret.phone);
                }
            });
        });
    });

    //选中号码，回填区域
    function getPhoneArea() {
        var phone = $("#customerManagerPhone").val();
        var url = "${contextPath}/manage/potentialCustomer/getCustomerPhoneArea.html";
        ajaxData(url, {phone: phone}, function (ret) {
            if (ret && ret.managerId && ret.managerName) {
                comboTree.setValue({"id": ret.managerId, "text": ret.managerName});
                $("#managerId").val(ret.managerId);
            }
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

    function checkFormValidate() {
        $("#potentialCustomer").validate({
            errorPlacement: function (error, element) {
                error.addClass("error-tip");
                if ($(element).siblings('div').hasClass('prompt')) {
                    $(element).siblings('div.prompt').before(error);
                } else {
                    $(element).parent().append(error);
                }

                if (element.attr("name") === 'districtId') {
                    $(element).parent().parent().append(error);
                }
            },
            errorElement: "span",
            rules: {
                name: {
                    required: true,
                    cmaxLength: 64
                },
                entName: {
                    required: true,
                    cmaxLength: 64
                },
                customerManagerPhone: {
                    mobile: true,
                    remote: {
                        url: "checkCustomerManagerValid.html",
                        data: {
                            customerManagerPhone: function () {
                                return $('#customerManagerPhone').val()
                            }
                        }
                    }
                },
                enterpriseManagerPhone: {
                    required: true,
                    mobile: true,
                },
                enterpriseManagerName: {
                    required: true,
                    cmaxLength: 64
                },
                email: {
                    required: true,
                    email: true,
                    maxlength: 64
                },
                email: {
                    required: true,
                    email: true,
                    maxlength: 64
                },
                managerId: {
                    required: true
                }
            },
            messages: {
                name: {
                    required: "请填写企业名称"
                },
                entName: {
                    required: "请填写企业简称"
                },
                customerManagerPhone: {
                    remote: "号码对应的客户经理不存在",
                    required: "请填写客户经理手机号码"
                },
                enterpriseManagerName: {
                    required: "请填写企业管理员姓名"
                },
                email: {
                    required: "请填写企业联系邮箱"
                },
                phone: {
                    required: "请填写企业联系电话"
                },
                enterpriseManagerPhone: {
                    required: "请填写企业管理员手机号码"
                },
                managerId: {
                    required: "请选择地区"
                }
            }
        });
    }


    /**
     * 根据地区id回填客户经理号码
     * @param districtId
     */
    function getCustomerPhone(districtId) {
        var url = "${contextPath}/server.html";

        ajaxData(url, {districtId: districtId}, function (ret) {
            if (ret && ret.phone) {
                $("#customerManagerPhone").val(ret.phone);
            } else {
                $("#customerManagerPhone").val(null);
            }
        });
    }
</script>

</body>
</html>