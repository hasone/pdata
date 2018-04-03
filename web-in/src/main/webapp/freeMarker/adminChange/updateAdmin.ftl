<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-用户管理</title>
    <meta name="keywords" content="流量平台 用户管理"/>
    <meta name="description" content="流量平台 用户管理"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->
    <script type="text/ecmascript" src="${contextPath}/manage/Js/md5.js"></script>
    <style>
        .form {
            width: 1200px;
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

        .btn-save {
            margin-left: 300px;
        }
        
         textarea {
            width: 350px;
            resize: none;
            outline: none;
            border: 1px solid #ccc;
            border-radius: 3px;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>账号信息
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" href="${contextPath}/manage/adminChange/index.html">返回</a>
        </h3>
    </div>

    <form name="adminForm" role="form" id="table_validate" action="${rc.contextPath}/manage/adminChange/saveAdmin.html" method="post">
        <input type="hidden" id="adminId" name="adminId" value='${admin.id!}' />
        <div class="tile mt-30">
            <div class="tile-header">
                用户信息修改
            </div>
            <div class="tile-content">
                <div class="row form">
                    <div class="form-group">
                        <label>用户角色:</label>
                        <span>企业关键人</span>
                    </div>
                    <div class="form-group">
                        <label>姓名:</label>
                       <input type="text" name="destName" id="destName" maxlength="64"
                               value='${admin.userName!}'/>
                    </div>
                
                
                    <div class="form-group">
                        <label>手机号码:</label>
                        <input type="text" name="destPhone" id="destPhone" maxlength="11" 
                               value='${admin.mobilePhone!}'/>
                    </div>
                    <div class="form-group">
                        <label>企业名称:</label>
                        <span>${enterName!}</span>
                    </div>
                    <div class="form-group">
                        <label>创建时间:</label>
                        <span>${admin.createTime?datetime}</span>
                    </div>
                    <div class="form-group">
                        <label>修改时间:</label>
                        <span>${admin.updateTime?datetime}</span>
                    </div>
                 
                    <div class="form-group">
                        <label style="vertical-align:top;">修改备注:</label>
                        <textarea name="comment" id="comment" maxlength="500" rows="10" cols="100">${comment!}</textarea>
                    </div>
                </div>
            </div>
        </div>

        <div class="btn-save mt-30">
            <a class="btn btn-sm btn-warning dialog-btn" id="save-btn" href="javascript:void(0);">保存</a>&nbsp;&nbsp;&nbsp;
            <span style="color:red" id="error_msg">${errMsg!}</span>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>

</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    require(["common", "bootstrap"], function () {
        checkFormValidate();

        $("#save-btn").on("click", function () {

           
            if ($("#table_validate").validate().form()) {
                sumitAdd();
            }

            return false;
        });
    });
</script>


<script>
     function checkFormValidate() {
        $("#table_validate").validate({
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
                destName: {
                    required: true
                },
                destPhone: {
                    required: true,
                    mobile: true
                },
                comment: {
                    required: true
                }
            },
            messages: {
                destName: {
                    required: "未输入企业管理员名称"
                },
                destPhone: {
                    required: "请填写企业管理员手机号码",
                },
                comment: {
                    required: "修改备注不能为空"
                }
            }
        });
    }
    

    function isEmptyOrNull(str) {
        return (str == null || str.trim().length == 0);
    }


    function sumitAdd() {
        flag = true;

        var temp = $("input[name='destName']").val();

        if (isEmptyOrNull(temp)) {
            flag = false;
            return flag;
        }
       

        var temp = $("input[name='destPhone']").val();

        if (isEmptyOrNull(temp)) {
            flag = false;
            return flag;
        }
       
        $('#table_validate').submit();
        return false;
    }
   
</script>
</body>
</html>