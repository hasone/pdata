<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>统一流量平台-开户配置</title>
    <meta name="keywords" content="统一流量平台-开户配置"/>
    <meta name="description" content="统一流量平台-开户配置"/>

    <link rel="stylesheet" href="../../assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="../../assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="../../assets/css/base.min.css"/>
    <link rel="stylesheet" href="../../assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="../../assets/css/innerbase.min.css"/>

    <!--[if lt IE 9]>
    <script src="../../assets/lib/html5shiv.js"></script>
    <script src="../../assets/lib/respond.min.js"></script>
    <![endif]-->
    <![endif]-->

    <style>
        .form input[type='text'] {
            background: transparent;
            border: 1px solid #ccc;
            outline: none;
            border-radius: 3px;
            padding: 3px 10px;
            width: 100%;
        }

        .form input[type='checkbox'] {
            vertical-align: top;
        }

        .form .table input[type='text'] {
            width: auto;
        }

        .table-content th, .table-content td {
            padding: 10px 20px;
            text-align: center;
        }

        .promote {
            font-size: 14px;
            color: #999;
            line-height: 35px;
            padding: 0 20px;
        }

        .tile-header {
            font-size: 14px;
            color: #333;
            line-height: 35px;
            padding: 0 20px;
            border-bottom: 1px solid #e8e8e8;
            background-color: #e8e8e8;
        }


    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>开户配置
        <#--<a class="btn btn-primary btn-sm pull-right btn-icon icon-back"-->
        <#--onclick="history.go(-1)">返回</a>-->
        </h3>
    </div>


    <div class="tile mt-30">
        <div class="tile-content">
            <form class="form" id="dataForm">
                <div class="tile-header">
                    必填字段
                </div>
                <table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td align="center" valign="middle">
                            <table width="90%" border="1" align="center" valign="middle"
                                   cellpadding="0" cellspacing="0"
                                   class="table-content mt-20 mb-40" id="mandatoryProps">
                                <tr>
                                    <th>字段名称</th>
                                    <th>描述文案</th>
                                    <th>字段说明</th>
                                </tr>
                                <tr class="form-group">
                                    <td><input type="text" value="${(config.name.name)!'企业名称'}"
                                               name="name" required maxlength="32"></td>
                                    <td><input type="text"
                                               maxlength="64"
                                               value="${(config.name.description)!}"></td>
                                    <td>平台显示的企业名称</td>
                                </tr>

                                <tr class="form-group">
                                    <td><input type="text"
                                               required maxlength="32"
                                               value="${(config.district.name)!'地区'}"
                                               name="district"></td>
                                    <td><input type="text"
                                               maxlength="64"
                                               value="${(config.district.description)!}"></td>
                                    <td>企业所属地区</td>
                                </tr>

                                <tr class="form-group">
                                    <td><input type="text" value="${(config.code.name)!'企业编码'}"
                                               name="code" required maxlength="32"></td>
                                    <td><input type="text"
                                               maxlength="64"
                                               value="${(config.code.description)!'客户集团编码(280)'}"
                                               name=""></td>
                                    <td>与BOSS交互时的企业集团编码</td>
                                </tr>

                                <tr class="form-group">
                                    <td><input type="text"
                                               required maxlength="32"
                                               value="${(config.enterpriseManagerPhone.name)!'企业管理员手机号码'}"
                                               name="enterpriseManagerPhone"></td>
                                    <td><input type="text"
                                               maxlength="64"
                                               value="${(config.enterpriseManagerPhone.description)!}"
                                               name=""></td>
                                    <td>企业管理员账号</td>
                                </tr>

                                <tr class="form-group">
                                    <td><input type="text"
                                               required maxlength="32"
                                               value="${(config.enterpriseManagerName.name)!'企业管理员姓名'}"
                                               name="enterpriseManagerName"></td>
                                    <td><input type="text"
                                               maxlength="64"
                                               value="${(config.enterpriseManagerName.description)!}"
                                               name=""></td>
                                    <td>企业管理员姓名</td>
                                </tr>

                                <tr class="form-group">
                                    <td><input type="text"
                                               required maxlength="32"
                                               value="${(config.customerManagerPhone.name)!'客户经理手机号码'}"
                                               name="customerManagerPhone"></td>
                                    <td><input type="text"
                                               maxlength="64"
                                               value="${(config.customerManagerPhone.description)!}"
                                               name=""></td>
                                    <td>客户经理账号</td>
                                </tr>

                                <tr class="form-group">
                                    <td><input type="text"
                                               required maxlength="32"
                                               value="${(config.customerManagerName.name)!'客户经理姓名'}"
                                               name="customerManagerName"></td>
                                    <td><input type="text"
                                               maxlength="64"
                                               value="${(config.customerManagerName.description)!}"
                                               name=""></td>
                                    <td>客户经理姓名</td>
                                </tr>

                                <tr class="form-group">
                                    <td><input type="text"
                                               required maxlength="32"
                                               value="${(config.cooperationTime.name)!'合作时间'}"
                                               name="cooperationTime"></td>
                                    <td><input type="text"
                                               maxlength="64"
                                               value="${(config.cooperationTime.description)!}"
                                               name=""></td>
                                    <td>企业合约约束时间，校验企业状态的参数之一</td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>

                <div class="tile-header">
                    扩展字段
                </div>
                <p class="promote">可通过勾选控制字段是否在开户页面展示，描述文案建议不超过25个汉字</p>
                <table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td align="center" valign="middle">
                            <table width="90%" border="1" align="center" valign="middle"
                                   cellpadding="0" cellspacing="0" class="table-content mb-20">
                                <thead>
                                <tr>
                                    <th>全选<input type="checkbox" id="selectAll"></th>
                                    <th>字段名称</th>
                                    <th>描述文案</th>
                                    <th>字段说明</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr class="form-group">
                                    <td><input type="checkbox"></td>
                                    <td><input type="text"
                                               required maxlength="32"
                                               value="${(config.entBlacklist.name)!'平台企业黑名单'}"
                                               name="entBlacklist"></td>
                                    <td><input type="text" value="${(config.entBlacklist.description)!}"
                                               maxlength="64"
                                               name=""></td>
                                    <td>平台企业黑名单显示</td>
                                </tr>
                                <tr class="form-group">
                                    <td><input type="checkbox"></td>
                                    <td><input type="text"
                                               required maxlength="32"
                                               
                                               value="${(config.entName.name)!'企业简称'}"
                                               name="entName"></td>
                                    <td><input type="text" value="${(config.entName.description)!}"
                                               maxlength="64"
                                               name=""></td>
                                    <td>短信模板中替换的企业名称</td>
                                </tr>
                                <tr class="form-group">
                                    <td><input type="checkbox"></td>
                                    <td><input type="text"
                                               required maxlength="32"
                                               value="${(config.email.name)!'企业联系邮箱'}"
                                               name="email"></td>
                                    <td><input type="text"
                                               value="${(config.email.description)!'平台所有通知信息将发送至该邮箱'}"
                                               maxlength="64"
                                               name=""></td>
                                    <td>联系邮箱展示</td>
                                </tr>
                                <tr class="form-group">
                                    <td><input type="checkbox"></td>
                                    <td><input type="text"
                                               required maxlength="32"
                                               value="${(config.phone.name)!'企业联系电话'}"
                                               name="phone"></td>
                                    <td><input type="text"
                                               value="${(config.phone.description)!'请填写企业联系电话，要求是座机'}"
                                               maxlength="64"
                                               name=""></td>
                                    <td>联系电话展示</td>
                                </tr>
                                <tr class="form-group">
                                    <td><input type="checkbox"></td>
                                    <td><input type="text"
                                               required maxlength="32"
                                               value="${(config.licenceImage.name)!'企业工商营业执照'}"
                                               name="licenceImage"></td>
                                    <td><input type="text"
                                               value="${(config.licenceImage.description)!'清晰彩色原件扫描件，不超过5M，支持jpg,jpeg,png'}"
                                               maxlength="64"
                                               name=""></td>
                                    <td>工商营业执照上传</td>
                                </tr>
                                <tr class="form-group">
                                    <td><input type="checkbox"></td>
                                    <td><input type="text"
                                               required maxlength="32"
                                               value="${(config.licenceTime.name)!'营业执照有效期'}"
                                               name="licenceTime"></td>
                                    <td><input type="text"
                                               maxlength="64"
                                               value="${(config.licenceTime.description)!}" name="">
                                    </td>
                                    <td>检验企业状态的参数之一</td>
                                </tr>
                                <tr class="form-group">
                                    <td><input type="checkbox"></td>
                                    <td><input type="text" required maxlength="32"
                                               value="${(config.authorization.name)!'企业管理员授权证明'}"
                                               name="authorization"></td>
                                    <td><input type="text"
                                               maxlength="64"
                                               value="${(config.authorization.description)!'请按照模板填写并上传文件，不超过5M'}"
                                               name=""></td>
                                    <td>证明文件上传</td>
                                </tr>
                                <tr class="form-group">
                                    <td><input type="checkbox"></td>
                                    <td><input type="text"
                                               required maxlength="32"
                                               value="${(config.identification.name)!'企业管理员身份证'}"
                                               name="identification"></td>
                                    <td><input type="text"
                                               maxlength="64"
                                               value="${(config.identification.description)!'请上传身份证扫描件，不超过5M，支持jpeg,jpg,png'}"
                                               name=""></td>
                                    <td>扫描图上传</td>
                                </tr>
                                <tr class="form-group">
                                    <td><input type="checkbox"></td>
                                    <td><input type="text"
                                               required maxlength="32"
                                               value="${(config.customerManagerEmail.name)!'客户经理邮箱'}"
                                               name="customerManagerEmail"></td>
                                    <td><input type="text"
                                               maxlength="64"
                                               value="${(config.customerManagerEmail.description)!}"
                                               name=""></td>
                                    <td>联系方式展示</td>
                                </tr>
                                <tr class="form-group">
                                    <td><input type="checkbox"></td>
                                    <td><input type="text"
                                               required maxlength="32"
                                               value="${(config.customerTypeId.name)!'客户分类'}"
                                               name="customerTypeId"></td>
                                    <td><input type="text"
                                               maxlength="64"
                                               value="${(config.customerTypeId.description)!}"
                                               name=""></td>
                                    <td>分类展示</td>
                                </tr>
                                <tr class="form-group">
                                    <td><input type="checkbox"></td>
                                    <td><input type="text"
                                               required maxlength="32"
                                               value="${(config.businessTypeId.name)!'流量类型'}"
                                               name="businessTypeId"></td>
                                    <td><input type="text"
                                               maxlength="64"
                                               value="${(config.businessTypeId.description)!}"
                                               name=""></td>
                                    <td>流量类型展示</td>
                                </tr>
                                <tr class="form-group">
                                    <td><input type="checkbox"></td>
                                    <td><input type="text"
                                               required maxlength="32"
                                               value="${(config.prdType.name)!'产品类型'}"
                                               name="prdType"></td>
                                    <td><input type="text" value="${(config.prdType.description)!}"
                                               maxlength="64"
                                               name=""></td>
                                    <td>产品类型展示</td>
                                </tr>
                                <tr class="form-group">
                                    <td><input type="checkbox"></td>
                                    <td><input type="text"
                                               required maxlength="32"
                                               value="${(config.payTypeId.name)!'支付类型'}"
                                               name="payTypeId"></td>
                                    <td><input type="text"
                                               maxlength="64"
                                               value="${(config.payTypeId.description)!}" name="">
                                    </td>
                                    <td>支付类型展示</td>
                                </tr>
                                <tr class="form-group">
                                    <td><input type="checkbox"></td>
                                    <td><input type="text"
                                               required maxlength="32"
                                               value="${(config.discount.name)!'折扣'}"
                                               name="discount"></td>
                                    <td><input type="text" value="${(config.discount.description)!}"
                                               maxlength="64"
                                               name=""></td>
                                    <td>折扣设置</td>
                                </tr>
                                <tr class="form-group">
                                    <td><input type="checkbox"></td>
                                    <td><input type="text"
                                               required maxlength="32"
                                               value="${(config.giveMoneyId.name)!'存送'}"
                                               name="giveMoneyId"></td>
                                    <td><input type="text"
                                               maxlength="64"
                                               value="${(config.giveMoneyId.description)!}" name="">
                                    </td>
                                    <td>存送设置</td>
                                </tr>
                                <tr class="form-group">
                                    <td><input type="checkbox"></td>
                                    <td><input type="text"
                                               required maxlength="32"
                                               value="${(config.ec.name)!'EC开通'}"
                                               name="ec"></td>
                                    <td><input type="text" value="${(config.ec.description)!}"
                                               maxlength="64"
                                               name=""></td>
                                    <td>默认状态展示</td>
                                </tr>
                                <tr class="form-group">
                                    <td><input type="checkbox"></td>
                                    <td><input type="text"
                                               required maxlength="32"
                                               value="${(config.customerFile.name)!'客户说明附件'}"
                                               name="customerFile"></td>
                                    <td><input type="text"
                                               maxlength="64"
                                               value="${(config.customerFile.description)!'大小不超过5M'}"
                                               name=""></td>
                                    <td>文件上传</td>
                                </tr>
                                <tr class="form-group">
                                    <td><input type="checkbox"></td>
                                    <td><input type="text"
                                               required maxlength="32"
                                               value="${(config.contract.name)!'合作协议文件'}"
                                               name="contract"></td>
                                    <td><input type="text"
                                               maxlength="64"
                                               value="${(config.contract.description)!'大小不超过5M'}"
                                               name=""></td>
                                    <td>文件上传</td>
                                </tr>
                                <tr class="form-group">
                                    <td><input type="checkbox"></td>
                                    <td><input type="text"
                                               required maxlength="32"
                                               value="${(config.image.name)!'审批截图'}"
                                               name="image"></td>
                                    <td><input type="text"
                                               maxlength="64"
                                               value="${(config.image.description)!'大小不超过5M'}"
                                               name=""></td>
                                    <td>文件上传</td>
                                </tr>
                                <tr class="form-group">
                                    <td><input type="checkbox"></td>
                                    <td><input type="text"
                                               required maxlength="32"
                                               value="${(config.entCredit.name)!'企业信用额度'}"
                                               name="entCredit"></td>
                                    <td><input type="text"
                                               maxlength="64"
                                               value="${(config.entCredit.description)!}" name="">
                                    </td>
                                    <td>信用额度设置</td>
                                </tr>
                                </tbody>

                            </table>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>


    <div class="mt-30 text-center">
        <a class="btn btn-warning btn-sm mr-10 dialog-btn" id="save-btn" style="width: 114px;">保
            存</a>
    </div>

    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>

</div>
<div class="modal fade dialog-sm" id="mod-dialog" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content">修改成功</span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" id='modelBtn' data-dismiss="modal">确 定
                </button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>


<script type="text/javascript" src="../../assets/lib/require.js"></script>
<script type="text/javascript" src="../../assets/js/config.js"></script>

<script>

    var action = "${contextPath}/manage/entPropsInfo/save.html";

    require(["common", "bootstrap"], function () {

        initFormValidate();

        var config = '${(configStr)!}' || '{}';
        listeners(JSON.parse(config));

        allChk();

    });

    function listeners(config) {

        $('#selectAll').on('click', function () {
            if (this.checked) {
                $('.table-content tbody :checkbox').prop('checked', true);
            } else {
                $('.table-content tbody :checkbox').prop('checked', false);
            }
            allChk();
        });

        $('.table-content tbody :checkbox').on('click', function () {
            allChk();
        });

        $("#save-btn").on('click', function () {
            saveData();
        });

        //模态对话框的确定按钮
        $("#modelBtn").on('click', reload);

        for (var prop in config) {
            $("input[name=" + prop + "]").parent().parent().find(':checkbox').prop('checked', true);
        }
    }

    function reload() {
        window.location.href = window.location.href;
    }

    function checkAll() {
        var elems = $("tr td input[required]").filter(":not([readonly])").get();

        //清空所有的错误元素
        $("tr td span.error-tip").remove();

        var flag = false;

        //遍历每个元素
        var length = elems.length;
        for (var i = 0; i < length; i++) {
            var elem = elems[i];

            var errorElems = [];

            //与其它元素相比
            for (var j = i + 1; j < length; j++) {
                var target = elems[j];
                if (checkedDuplicate(elem, target)) {
                    flag = true;

                    if (errorElems.length == 0) {
                        errorElems.push(elem);
                    }

                    errorElems.push(target);
                }
            }

            errorElems.forEach(function (curr) {
                addError(curr);
            });
        }

        return flag;
    }

    function checkedDuplicate(elem, target) {
        if (elem == target) {
            return false;
        }

        var currentValue = $(elem).val();
        var targetValue = $(target).val();
        if (!currentValue || !targetValue) { //null, undefined, ""
            return false;
        }

        return currentValue == targetValue;
    }

    function addError(elem) {
        var errorElem = $("<span></span>")
                .attr("id", $(elem).attr("name") + "-error")
                .text("字段名称不能重复!")
                .addClass("error error-tip")
                .css({
                         "display": "block",
                         "text-align": "left"
                     });

        //增加错误提示
        if ($(elem).closest("td").find("#" + errorElem.attr("id")).length == 0) {
            $(elem).after(errorElem);
        }
    }

    function allChk() {

        var chkNum = $(".table-content tbody :checkbox").size();
        var chk = 0;
        $(".table-content tbody :checkbox").each(function () {
            if ($(this).prop("checked") == true) {
                chk++;
                $(this).parent().parent().find(':text').removeAttr('readonly')
                        .css('backgroundColor', '#fff');
                $(this).parent().parent().find('input[type="text"]:first').attr('required', true);
            } else {
                $(this).parent().parent().find(':text').attr('readonly', 'readonly')
                        .css('backgroundColor', '#ccc');
                $(this).parent().parent().find('input[type="text"]:first').attr('required', false);
            }
        });
        if (chkNum == chk) {
            $("#selectAll").prop("checked", true);
        } else {
            $("#selectAll").prop("checked", false);
        }
    }

    function saveData() {
        if (checkAll()) {
            return;
        }

        if ($("#dataForm").validate().form()) {
            var formData = {};

            //必填项
            $("#mandatoryProps").find(".form-group").each(function () {
                var nameChild = $(this).find("input[type='text']:first");
                var descChild = $(this).find("input[type='text']:last");

                formData[nameChild.prop('name')] = {
                    name: nameChild.val(),
                    description: descChild.val()
                }
            });

            //可选项
            $(".table-content tbody :checkbox").each(function () {
                if ($(this).prop("checked") == true) {
                    var parent = $(this).parent().parent();
                    var nameChild = parent.find("input[type='text']:first");
                    var descChild = parent.find("input[type='text']:last");
                    formData[nameChild.prop('name')] = {
                        name: nameChild.val(),
                        description: descChild.val()
                    }
                }
            });

            $.ajax({
                       beforeSend: function (request) {
                           var token1 = $(window.parent.document).find("meta[name=_csrf]")
                                   .attr("content");
                           var header1 = $(window.parent.document).find("meta[name=_csrf_header]")
                                   .attr("content");
                           request.setRequestHeader(header1, token1);
                       },

                       type: 'post',
                       data: JSON.stringify(formData),
                       url: action,
                       contentType: "application/json; charset=utf-8",
                       dataType: 'json',
                       success: function (ret) {
                           if (ret && ret.success) {
                               $('#mod-dialog').modal('show');
                           }
                       },
                       error: function () {
                           alert("啊哦~有异常！");
                       }
                   });
        }
    }

    function initFormValidate() {
        $("#dataForm").validate({
                                    errorPlacement: function (error, element) {
                                        error.addClass("error-tip").css({
                                                                            "display": "block",
                                                                            "text-align": "left"
                                                                        });
                                        element.after(error);
                                    },
                                    rules: {
                                        name: {
                                            required: true
                                        },
                                        district: {
                                            required: true
                                        },
                                        code: {
                                            required: true
                                        },
                                        enterpriseManagerPhone: {
                                            required: true
                                        },
                                        enterpriseManagerName: {
                                            required: true
                                        },
                                        customerManagerPhone: {
                                            required: true
                                        },
                                        customerManagerName: {
                                            required: true
                                        },
                                        cooperationTime: {
                                            required: true
                                        }
                                    },
                                    errorElement: "span",
                                    messages: {
                                        name: {
                                            required: "企业名称字段名不能为空"
                                        },
                                        district: {
                                            required: "地区字段名称不能为空"
                                        },
                                        code: {
                                            required: "企业编码字段名称不能为空"
                                        },
                                        enterpriseManagerPhone: {
                                            required: "企业管理员手机号码字段名称不能为空"
                                        },
                                        enterpriseManagerName: {
                                            required: "企业管理员姓名字段不能为空"
                                        },
                                        customerManagerPhone: {
                                            required: "客户经理手机号字段不能为空"
                                        },
                                        customerManagerName: {
                                            required: "客户经理姓名字段不能为空"
                                        },
                                        cooperationTime: {
                                            required: "合作时间字段名称不能为空"
                                        }
                                    }
                                });
    }

</script>
</body>
</html>