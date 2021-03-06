<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-新建卡模板</title>
    <meta name="keywords" content="流量平台 新建卡模板"/>
    <meta name="description" content="流量平台 新建卡模板"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

    <!-- easy-ui-->
    <link rel="stylesheet" type="text/css" href="${contextPath}/assets/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="${contextPath}/assets/easyui/themes/icon.css">

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
            padding: 4px 5px;
            width: 350px;
        }

        .form .form-group label {
            width: 300px;
            text-align: right;
        }

        .form .form-group .prompt {
            padding-left: 305px;
            color: #bbb;
            padding-bottom: 0;
            margin-bottom: 0;
        }
    </style>

    <script>
        function toggle() {
            $("#theme").toggle(500, function () {
                var display = $(this).css("display");
                if (display == "none") {
                    $(this).val("");
                    $("#theme-error").remove();
                    $(this).attr("disabled", "disabled");
                } else {
                    $(this).removeAttr("disabled");
                }
            });
            $("#themeList").toggle(500, function () {
                var display = $(this).css("display");
                if (display == "none") {
                    $(this).attr("disabled", "disabled");
                    $("#themeList-error").remove();
                } else {
                    $(this).removeAttr("disabled");
                }
            });

        }
    </script>


</head>

<body>
<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>新建卡模板
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back"
               onclick="javascript:window.location.href='${contextPath}/manage/mdrc/template/index.html'">返回</a>
        </h3>
    </div>

    <form action="${contextPath}/manage/mdrc/template/save.html" method="post" name="adminForm" id="table_validate">
        <div class="tile mt-30">

            <div class="tile-content">
                <div class="row form">
                    <div class="form-group">
                        <label>模板名称：</label>
                        <input type="text" name="name" id="name" value="${name!}" class="hasPrompt" maxlength="64"
                               required/>
                    </div>

                    <div class="form-group">
                        <label>流量包大小：</label>
                        <div class="btn-group">
                            <select class="btn btn-default lg-btn-input" name="proSize" id="proSize">
                            <#if (products?size>0) >
                                <#list products as p>
                                    <#if p.productSize??>
                                        <option value="${p.productSize}">
                                            <#if p.productSize??&&(p.productSize<1024)>
                                            ${(p.productSize)?string("#.##")}KB
                                            </#if>
                                            <#if p.productSize?? && (p.productSize>=1024) && (p.productSize<1024*1024)>
                                            ${(p.productSize/1024.0)?string("#.##")}MB
                                            </#if>
                                            <#if p.productSize?? && (p.productSize>=1024*1024)>
                                            ${(p.productSize/1024.0/1024.0)?string("#.##")}GB
                                            </#if>
                                        </option>
                                    </#if>
                                </#list>
                            </#if>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label>卡面主题：</label>
                        <div class="btn-group">
                            <select name="theme" class="theme" id="themeList" required>
                            <#if (themes?size>0) >
                                <#list themes as m>
                                    <option value="${m}">${m}</option>
                                </#list>
                            <#else>
                                <option value="">---暂无主题---</option>
                            </#if>
                            </select>
                            <input type="text" name="theme" class="theme" id="theme" maxlength="20" style="display: none;"
                                   disabled="disabled" placeholder=""/>
                            <a href="javascript:void(0);" onclick="toggle()">手工输入卡面主题</a>
                        </div>
                    </div>
                    <div class="form-group">
                        <label>上传正面：</label>
                        <span class="file-box">
                            <a class="btn btn-sm btn-default cyan-text">上传图片</a>
                            <input type="text" name="frontName" class="file-text"
                                   style="width:0; height:0; padding: 0; opacity: 0;">
                            <input type="file" name="mdrcCommonTemplateFront" id="mdrcCommonTemplateFront" class="file-helper imge-check"
                                   accept="*/*" required>
                        </span>
                        <#--<div class="prompt">支持jpg，jpeg，png图片格式，图片大小不超过1M</div>-->
                    </div>

                    <div class="form-group">
                        <label>上传背面：</label>
                        <span class="file-box">
                            <a class="btn btn-sm btn-default cyan-text">上传图片</a>
                            <input type="text" name="rearName" class="file-text"
                                   style="width:0; height:0; padding: 0; opacity: 0;">
                            <input type="file" name="mdrcCommonTemplateBack" id="mdrcCommonTemplateBack" class="file-helper imge-check"
                                   accept="*/*" required>
                        </span>
                        <#--<div class="prompt">支持jpg，jpeg，png图片格式，图片大小不超过1M</div>-->
                    </div>
                    <div class="form-group">
                        <label>正面预览：</label>
                        <div class="btn-group">
                            <img class="mdrcCommonTemplateFront" src="" style="max-width:360px;"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label>背面预览：</label>
                        <div class="btn-group">
                            <img class="mdrcCommonTemplateBack" src="" style="max-width:360px;"/>
                        </div>
                    </div>
                </div>

                <div class="text-center mt-30">
                    <a href="javascript:void(0)" class="btn btn-sm btn-warning dialog-btn" id="save-btn">保 存</a>&nbsp;&nbsp;&nbsp;
                    <span style="color:red" id="error_msg">${msg!}</span>
                </div>
            </div>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
</div>

<!--错误提示弹窗-->
<div class="modal fade dialog-sm" id="tip-dialog">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content"></span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal">确 定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script src="${contextPath}/assets/lib/jquery-1.11.3.min.js"></script>
<script src="${contextPath}/assets/js/up.js"></script>
<script>
    require(["common", "bootstrap", "easyui"], function () {
        //验证
        checkFormValidate();
        init();
    });

    function init() {
        //保存企业信息
        $("#save-btn").on("click", function () {
            if ($("#table_validate").validate().form()) {
                doSubmit();
            }
            return false;
        });

        $("input[type='file']").on('change', function () {
            fileChoiceHandler(this);
        });
    }

    /**
     * 文件选择事件监听
     */
    function fileChoiceHandler(ele) {
        if ($(ele).hasClass("imge-check")) {
            if (checkFileType($(ele))) {
                preview(ele);
            } else {
                var oldFile = $(ele);
                $(ele).parent().children(".file-text").val("");
                var newFile = $(ele).clone(true);
                newFile.val("");
                oldFile.after(newFile);
                oldFile.remove();
            }
        }
    }

    /**
     * 检查文件类型
     * @param ele
     * @param suffix
     */
    function checkFileType(ele, suffix) {
        var name = ele.attr("name");
        if (ele.val()) {
            var parts = ele.val().split(/\./);
            var ext = parts[parts.length - 1];
            if (ext) {
                ext = ext.toLowerCase();
                var reg = suffix || /(jpg|jpeg|png)$/;
                if (!reg.test(ext)) {
                    if (!suffix) {
                        showTipDialog("图片格式不正确，只支持jpg，jpeg，png");
                        $("." + name).removeAttr("src").hide();
                        ele.val("");
                    }
                    return false;
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 预览上传的图片
     */
    function preview(ele) {
        var name = $(ele).attr("name");
        if (ele.files && ele.files[0]) {// FileReader,IE10+、FF22/23、Chrome28/29支持
            if (typeof FileReader != 'undefined') {
                var reader = new FileReader;
                reader.onload = function (event) {
                    event = event || window.event;
                    var img = new Image();
                    img.onload = function () {
                        if (ele.files[0].size > 1 * 1024 * 1024) {
                            showTipDialog("要求图片大小不超过1M");
                            $(ele).val("");
                            $("." + name).removeAttr("src").hide();
                        } else {
                            $('#' + name + '-error').hide();
                            $("." + name).attr("src", event.target.result).show();
                        }
                    };
                    img.src = event.target.result;
                };
                reader.readAsDataURL(ele.files[0]);
            }
        } else {
            var preload = document.createElement("div"),
                    body = document.body,
                    data, oriWidth, oriHeight, ratio;

            preload.style.cssText = "width:100px;height:100px;visibility: visible;position: absolute;left: 0px;top: 0px";
            // 设置sizingMethod='image'
            preload.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='image')";
            body.insertBefore(preload, body.childNodes[0]);
            ele.select();
            try {
                data = document.selection.createRange().text;
            } finally {
                document.selection.empty();
            }

            if (!!data) {
                data = data.replace(/[)'"%]/g, function (s) {
                    return escape(escape(s));
                });
                //预载图片
                try {
                    preload.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = data;
                } catch (e) {
                    //console.log(e.description);
                    return;
                }
            }

            oriWidth = preload.offsetWidth;
            oriHeight = preload.offsetHeight;

            document.body.removeChild(preload);
            preload = null;
            var name = $(ele).attr("name");

            var target = $("." + name);//$(ele).parent().prev("img");
            var parent = target.parent();
            var div;
            if (parent.children(".prev_img").length) {
                div = parent.children(".prev_img");
            } else {
                div = $("<div class='prev_img'></div>");
                parent.prepend(div);
            }
            div.css({
                "filter": "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)",
                position: "absolute",
                left: target.position().left,
                top: target.position().top
            });
            div[0].filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = data;
            var w = target.width();
            var h = w / oriWidth * oriHeight;
            div[0].style.width = w + "px";
            div[0].style.height = h + "px";
            target.css({height: h + "px"});
        }
    }

    function checkFormValidate() {
        $("#table_validate").validate({
            errorPlacement: function (error, element) {
                error.addClass("error-tip");
                $(element).parent().append(error);

            },
            errorElement: "span",
            rules: {
                name: {
                    required: true,
                    maxlength: 64,
                    searchBox: true,
                    noSpecial: true,
                    space: true,
                    remote: {
                        url: "check.html",
                        data: {
                            name: function () {
                                return $('#name').val()
                            }
                        }
                    }
                },
                theme: {
                    required: true,
                    searchBox: true,
                    maxlength: 20,
                    noSpecial: true,
                    space: true
                },
                mdrcCommonTemplateFront:{
                    required: true
                },
                mdrcCommonTemplateBack:{
                    required:true
                }
            },
            messages: {
                name: {
                    required: "请填写模板名称",
                    searchBox: "只能输入汉字、字母和数字!",
                    remote: "模板名称已经存在!",
                    maxlength: "模板名称不超过64个字符",
                    space: "只能输入汉字、字数和数字!",
                    noSpecial: "只能输入汉字、字母和数字!"
                },
                theme: {
                    required: "请填写卡面主题",
                    searchBox: "只能输入汉字、字母和数字!",
                    maxlength: "卡面主题不超过20个字符!",
                    noSpecial: "只能输入汉字、字母和数字!",
                    space: "只能输入汉字、字母和数字!"
                },
                mdrcCommonTemplateFront:{
                    required: "请上传图片正面"
                },
                mdrcCommonTemplateBack:{
                    required: "请上传图片背面"
                }
            }
        });
    }

    //编辑保存
    function doSubmit() {
        var mdrcTemplate = {
            name: $('#name').val(),
            productSize: $('#proSize').val(),
            theme: $('.theme:visible').val()
        };

        var formData = {
            mdrcTemplate: JSON.stringify(mdrcTemplate)
        };

        var fileDataId = ['mdrcCommonTemplateFront', 'mdrcCommonTemplateBack'];

        $.ajaxFileUpload({
            type: 'post',
            secureuri: false,
            fileElementId: fileDataId,
            data: formData,
            url: "${contextPath}/manage/mdrc/template/saveAjax.html?${_csrf.parameterName}=${_csrf.token}",
            dataType: 'json',
            success: function (data) {
                if (data && data.code && data.code === 'success') {
                    window.location.href = "${contextPath}/manage/mdrc/template/index.html";
                } else {
                    $("#errorTip").text(data.msg);
                    $("input[type='file']").on('change', function () {
                        fileChoiceHandler(this);
                    });
                }
            },
            error: function () {
                $("#errorTip").text("网络错误，请稍候重新尝试。");
                $("input[type='file']").on('change', function () {
                    fileChoiceHandler(this);
                });
            }
        });
    }

</script>
</body>
</html>

