<#assign contextPath = rc.contextPath >
<!DOCTYPE html>
<html>
<head>
    <title>添加用户</title>
    <script type="text/javascript" src="${contextPath}/manage/Js/knockout-3.2.0.js"></script>
    <script>

    </script>

    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="stylesheet" type="text/css" href="${contextPath}/manage/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/manage/assets/css/font-awesome.min.css"/>

    <!--[if IE 7]>
    <link rel="stylesheet" href="${contextPath}/manage/assets/css/font-awesome-ie7.min.css"/>
    <![endif]-->

    <!-- ace styles -->
    <link rel="stylesheet" href="${contextPath}/manage/assets/css/ace.min.css"/>
    <link rel="stylesheet" href="${contextPath}/manage/assets/css/ace-rtl.min.css"/>
    <link rel="stylesheet" href="${contextPath}/manage/assets/css/ace-skins.min.css"/>

    <!--[if lte IE 8]>
    <link rel="stylesheet" href="${contextPath}/manage/assets/css/ace-ie.min.css"/>
    <![endif]-->

    <script src="${contextPath}/manage/assets/js/ace-extra.min.js"></script>

    <!--[if lt IE 9]>
    <script src="${contextPath}/manage/assets/js/html5shiv.js"></script>
    <script src="${contextPath}/manage/assets/js/respond.min.js"></script>
    <![endif]-->


    <script type="text/javascript" src="${contextPath}/manage/Js/jquery.js"></script>
    <script type="text/javascript" src="${contextPath}/manage/assets/js/js.cookie.js"></script>
    <script type="text/javascript" src="${contextPath}/manage/Js/bootstrap.js"></script>


    <!-- basic scripts -->
    <!--[if !IE]> -->
    <script src="http://libs.useso.com/js/jquery/2.1.1/jquery.min.js"></script>
    <!-- <![endif]-->

    <!--[if IE]>
    <script src="http://libs.useso.com/js/jquery/1.11.1/jquery.min.js"></script>
    <![endif]-->

    <!--[if !IE]> -->

    <script type="text/javascript">
        window.jQuery || document.write("<script src='${contextPath}/manage/assets/js/jquery-2.0.3.min.js'>" + "<" + "/script>");
    </script>

    <!-- <![endif]-->

    <!--[if IE]>
    <script type="text/javascript">
        window.jQuery || document.write("<script src='${contextPath}/manage/assets/js/jquery-1.10.2.min.js'>" + "<" + "/script>");
    </script>
    <![endif]-->

    <script type="text/javascript">
        if ("ontouchend" in document) document.write("<script src='${contextPath}/manage/assets/js/jquery.mobile.custom.min.js'>" + "<" + "/script>");
    </script>
    <script src="${contextPath}/manage/assets/js/bootstrap.min.js"></script>
    <script src="${contextPath}/manage/assets/js/typeahead-bs2.min.js"></script>

    <!-- page specific plugin scripts -->

    <!-- ace scripts -->
</head>

<body>
<table class="table table-bordered table-hover definewidth m10">
    <form action="${contextPath}/manage/month_record/upload.html"
          method="post"
          enctype="multipart/form-data"
          id="upload_form">
        <input type="hidden" name="phones" id="phones" value=""/>
        <tr>
            <th width="60" class="tableleft">上传文件</th>
            <td nowrap="nowrap">
                <input name="file" style=" width=50px;" type="file"/>
                <button type="button" class="btn btn-primary" onclick="doSubmit()">批量导入</button>
                支持格式txt、xls和xlsx，每行一个号码
            </td>
        </tr>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
    <tr>
        <th class="tableleft">手机号码</th>
        <td>
            <input id="phone" type="text" maxlength="11"/>
            <button type="button" class="btn btn-primary" type="button" onclick="add()">添加</button>
            &nbsp;
            <button type="button" class="btn btn-success" onclick="doComplete()">完成</button>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <span style="color: red" id="error_msg"><#if returnMsg??>${returnMsg}</#if></span>
        </td>
    </tr>
</table>
<table id="append_table" class="table table-bordered table-hover definewidth m10">
    <thead>
    <tr>
        <th width="60">序号</th>
        <th width="300">被赠送人号码</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody data-bind="foreach: phones">
    <tr>
        <td data-bind="text: $index()+1"></td>
        <td><label data-bind="text: phone"></label>
            <input type="hidden" name="phone_num" data-bind="value: phone"/></td>
        <td>
            <button data-bind="click: $parent.removePerson">删除</button>
        </td>
    </tr>
    </tbody>

</table>
<script type="text/javascript">

    <#if phoneList?? && phoneList?size gt 0>
    var arr = [<#list phoneList as phone>${phone}<#if phone_has_next>,</#if></#list>];
    var phoneList = $.map(arr, function (value, index) {
        return {'phone': value};
    });
    <#else>
    var phoneList = [];
    </#if>

    var viewModel = {
        phones: ko.observableArray(phoneList),
        removePerson: function () {
            viewModel.phones.remove(this);
        }
    }
    ko.applyBindings(viewModel);

    $(document).ready(function () {
        var phoneNums = $("input[name='phone_num']");
        if (phoneNums != null && phoneNums != '' && phoneNums.length > 0)
            return;
        var phones = $("#phones", window.opener.document).val();
        if (phones != "") {
            var phoneArray = phones.split(",");
            $.each(phoneArray, function (index, value) {
                viewModel.phones.push({'phone': value});
                //console.log( index + ": " + value );
            });
        }
    });

    //添加
    function add() {
        $("#error_msg").empty();
        var phone = $("#phone").val();
        var rex = /^1[3-8][0-9]{9}$/;
        if (!rex.test(phone)) {
            $("#error_msg").append("手机号码格式不正确");
            return;
        }
        viewModel.phones.push({'phone': phone});
        $("#phone").val("");
    }

    function doSubmit() {
        var phoneNums = $("input[name='phone_num']");
        if (phoneNums != null && phoneNums != '') {
            var phoneStr = "";
            for (var i = 0; i < phoneNums.length; i++) {
                phoneStr += phoneNums[i].value;
                if (i < phoneNums.length - 1)
                    phoneStr += ",";
            }
            $("#phones").val(phoneStr);//支持多次上传
        }
        $("#upload_form").submit();
    }

    //设置父页面的值
    function doComplete() {
        var phoneNums = $("input[name='phone_num']");
        if (phoneNums != null && phoneNums != '') {//有数据再处理
            var phoneStr = "";
            for (var i = 0; i < phoneNums.length; i++) {
                phoneStr += phoneNums[i].value;
                if (i < phoneNums.length - 1)
                    phoneStr += ",";
            }
            //window.opener.document.getElementById('').value=phoneStr;
            $("#phones", window.opener.document).val(phoneStr);
            $("#phone_count", window.opener.document).empty();
            $("#phone_count", window.opener.document).append(phoneNums.length);
        }
        window.close();
    }

</script>
</body>
</html>