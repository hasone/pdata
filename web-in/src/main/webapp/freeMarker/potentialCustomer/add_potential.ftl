<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-新增潜在客户</title>
    <meta name="keywords" content="流量平台 新增潜在客户"/>
    <meta name="description" content="流量平台 新增潜在客户"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Tree.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css"/>

    <!-- easy-ui-->
    <link rel="stylesheet" type="text/css" href="${contextPath}/assets/easyui/themes/default/easyui.css">

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .form {
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

        .form-group {
            position: relative;
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

        .dropdown-menu > li > a {
            padding: 6px 20px;
        }

        .form .error-tip {
            display: block;
            margin-left: 303px;
        }

        .btn-save {
            margin-left: 600px;
        }

        .cm-select-value{
            width: 350px;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>
        <#if pc.name??>
            编辑潜在客户
        <#else>
            新增潜在客户
        </#if>
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back"
               onclick="window.location.href='${contextPath}/manage/potentialCustomer/indexPotential.html'">返回</a>

        </h3>
    </div>


    <div class="tile mt-30">

        <div class="tile-content text-center">
            <form class="form" id="potentialCustomer"
                  action="${contextPath}/manage/potentialCustomer/savePotential.html" method="post" hidden>
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
                    <input required name="enterpriseManagerPhone" maxLength="11" id="enterpriseManagerPhone" type="text"
                           class="form-control"
                           <#if pc.enterpriseManagerPhone??>value="${pc.enterpriseManagerPhone}"</#if>>
                </div>

                <div class="form-group form-inline">
                    <label>地区：</label>
                    <input id="managerId" name="managerId" style="width: 0; height: 0; opacity: 0">
                    <div class="btn-group btn-group-sm" id="tree" required style="width: 350px;"></div>
                    <span id="area_error" style='color:red'></span>
                </div>

                <div class="form-group form-inline">
                    <label>客户经理手机号码：</label>
                    <input required name="customerManagerPhone" maxLength="11" id="customerManagerPhone" type="text"
                           class="form-control" onblur="getPhoneArea()"
                           <#if pc.customerManagerPhone??>value="${pc.customerManagerPhone}"</#if>>
                </div>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
        </div>
    </div>

    <div class="mt-30 btn-save">
        <a href="javascript:void(0)" class="btn btn-sm btn-warning dialog-btn" style="width: 114px;"
           id="refuse-btn">取消</a>
        <a href="javascript:void(0)" class="btn btn-warning btn-sm mr-10 dialog-btn" style="width: 114px;"
           id="save-btn">保 存</a><span style='color:red' id="errorMsg">${errorMsg!}</span>
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
        <p class="weui_toast_content">数据加载中</p>
    </div>
</div>
<!-- loading end -->


<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>

<script>
    var convertTable={
        "district": "managerId"
    };

    var renderDataUrl = "${contextPath}/manage/entPropsInfo/query.html?${_csrf.parameterName}=${_csrf.token}";
    var comboTree;

    require(["common", "bootstrap", "daterangepicker", "easyui", "ComboTree", "react", "react-dom"], function (a, b, c, d, ComboTree, React, ReactDOM) {
        //渲染页面
        render(renderDataUrl, convertTable);

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
                showToast();
                $("#potentialCustomer").submit();
            }
            return false;
        });

        //保存并继续
        $("#save-btn-next").on("click", function () {
            if ($("#potentialCustomer").validate().form()) {
                showToast();
                $("#next").val(1);
                $("#potentialCustomer").submit();
            }
            return false;
        });

        comboTree = ReactDOM.render(React.createElement(ComboTree, {
            name: "managerId",
            url: "${contextPath}/manage/potentialCustomer/getManagerRoot.html"
        }), $("#tree")[0]);
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
            $("#area_error").html("");

            $("#managerId").val(item.id);

            ajaxData("${contextPath}/manage/potentialCustomer/getAreaPhone.html", {"managerId": item.id}, function (ret) {
                if (ret && ret.phone) {
                    $("#customerManagerPhone").val(ret.phone);
                }
                /*if(!ret||ret.phone==null||ret.phone==""){
                    $("#area_error").html("无效的上级别管理员");
                }*/
            });
        });

        var phone = $("#customerManagerPhone").val();
        var url = "${contextPath}/manage/potentialCustomer/getCustomerPhoneArea.html";
        ajaxData(url, {phone: phone}, function (ret) {
            if (ret && ret.managerId && ret.managerName) {
                comboTree.setValue({"id": ret.managerId, "text": ret.managerName});
            }
            $("#managerId").val(ret.managerId);
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
            /*else{
                $("#area_error").html("无效的上级别管理员");
            }*/
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
            },
            errorElement: "span",
            rules: {
                name: {
                    required: true,
                    cmaxLength: 64,
                    noSpecial: true
                },
                entName: {
                    required: true,
                    cmaxLength: 64,
                    noSpecial: true
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
                    remote: {
                        url: "checkEnterpriseManagerValid.html",
                        data: {
                            enterpriseManagerPhone: function () {
                                return $('#enterpriseManagerPhone').val()
                            }
                        }
                    }
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
                phone: {
                    required: true,
                    maxlength: 64
                },
                managerId: {
                    required: true
                }
            },
            messages: {
                name: {
                    required: "请填写企业名称",
                    noSpecial: "企业名称只能由汉字、英文字符、下划线或数字组成!"
                },
                entName: {
                    required: "请填写企业简称",
                    noSpecial: "企业简称只能由汉字、英文字符、下划线或数字组成!"
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
                    remote: "该用户为其他角色或已关联至其他企业!",
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
        var url = "${contextPath}/district/searchCMPhome.html";

        ajaxData(url, {districtId: districtId}, function (ret) {
            if (ret && ret.phone) {
                $("#customerManagerPhone").val(ret.phone);
            } else {
                $("#customerManagerPhone").val(ret.phone);
            }
        });
    }
</script>

</body>
</html>