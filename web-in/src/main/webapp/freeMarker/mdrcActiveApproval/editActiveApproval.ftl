<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>编辑激活申请</title>
    <meta name="keywords" content="激活申请"/>
    <meta name="description" content="激活申请"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .form input[type='text'],
        .form input[type='number'],
        .form .form-control {
            width: 220px;

        }

        .form select {
            background: transparent;
            border: 1px solid #ccc;
            outline: none;
            border-radius: 3px;
            padding: 3px 10px;
            width: 220px;
        }

        .file-box {
            display: inline-block;
        }

        .form label {
            width: 80px;
            text-align: right
            margin-right: 10px;
        }

        span.error {
            color: red;
            margin-left: 10px;
        }

        .form .form-group .prompt {
            margin-left: 83px;
            color: #bbb;
            padding-bottom: 0;
            margin-bottom: 0;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>编辑激活申请
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>

    <div class="tile mt-30">
        <div class="tile-header"></div>
        <div class="tile-content">
            <div class="row form">
                <form id="dataForm" action="${contextPath}/manage/mdrc/active/saveEdit.html?${_csrf.parameterName}=${_csrf.token}"
                      method="post"  enctype="multipart/form-data">
                    <div class="col-md-12 col-md-offset-1">
                        <input type="hidden" name="requestId" value="${mdrcActiveDetail.requestId!}"/>
                        <input type="hidden" name="configId" value="${configId!}"/>

                        <div class="form-group form-group-sm form-inline">
                            <label>起始序列：</label>
                            <input type="hidden" class="form-control" id="startCardNumber" name="startCardNumber"
                                   value="${mdrcActiveDetail.startCardNumber!}">
                            <span>${mdrcActiveDetail.startCardNumber!}</span>
                        </div>
                        <div class="form-group form-group-sm form-inline">
                            <label>激活数量：</label>
                            <div class="btn-group btn-group-sm">
                                <input type="test" class="form-control" id="count" name="count"
                                       value="${mdrcActiveDetail.count!}">
                            </div>
                            <div class="prompt">
                                该批次卡数量为${totalCount!}张，还剩余${notActiveCount!}未激活。
                            </div>
                        </div>
                        <div class="form-group form-group-sm form-inline">
                            <label>结束序列：</label>
                            <input name="endCardNumber" id="endCardNumber" type="hidden">
                            <span id="endSerial"></span>
                            <div class="prompt" id="tips">
                            </div>
                        </div>

                        <#if mdrcActiveDetail?? && mdrcActiveDetail.image??>
                            <div class="form-group form-group-sm form-inline">
                                <label style="vertical-align: top">企业签收：</label>
                                <span class="editable-img">
                                    <img src="${contextPath}/manage/mdrc/active/getImage.html?requestId=${mdrcActiveDetail.requestId!}&type=entImage" class="prev-img" style="max-width: 130px">
                                </span>
                                <span class="file-box" style="vertical-align: top">
                                    <input class="file-helper imge-check" type="file" name="entImage" id="entImage"
                                           value="${mdrcActiveDetail.image!}" accept="image/*" >
                                    <a href="javascript:void(0)" class="primary-text ml-20 client-upload-btn">修改</a>
                                </span>
                                <div class="prompt">文件大小不超过5M，支持jpeg,jpg,png</div>
                            </div>
                        </#if>

                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
            </div>
        </div>
    </div>


    <div class="mt-30 text-center">
        <a class="btn btn-sm btn-warning dialog-btn" id="submit-btn">提交审批</a>
        &nbsp;&nbsp;&nbsp;&nbsp;<span style='color:red'>${errorMsg!}</span>
    </div>

</div>

<div class="modal fade dialog-sm" id="submit-dialog">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content">请确认是否提交审批，一旦提交不可修改！</span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal" id="sure">确 定</button>
                <button class="btn btn-warning btn-sm" data-dismiss="modal">取 消</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>


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
                <button class="btn btn-warning btn-sm" data-dismiss="modal" id="success-btn">确 定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<div class="modal fade dialog-lg" id="img-dialog">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">图片预览！</h5>
            </div>
            <div class="modal-body">
                <img id="flexBox-img" style="max-width: 100%; display: block; margin: 0 auto;"/>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal">确 定</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
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

<!--[if lt IE 9]-->
<script src="${contextPath}/assets/lib/es5-shim.min.js"></script>
<script src="${contextPath}/assets/lib/es5-sham.min.js"></script>
<!--[endif]-->
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>

<script>
    var isFirst = true;
    require(["moment", "common", "bootstrap"], function (mm) {

        window["moment"] = mm;

        initFormValidate();

        submitForm();

        listeners();

        init();

        initEndCardNum();

    });

    function initEndCardNum() {
        if(isFirst){
            var endCardNum = "${mdrcActiveDetail.endCardNumber!}";
            $("#endSerial").html(endCardNum);
            $("#endCardNumber").val(endCardNum);
            isFirst = false;
        }
    }

    function init() {

        $("input[type='file']").change(function () {
            fileChoiceHandler(this);
        });

        //提交表单
        $("#sure").on("click", function () {
            $("#dataForm").submit();
        });

        $(".form img").on("click", function () {
            var src = $(this).attr("src");
            $("#img-dialog img").attr("src", src);
            $("#img-dialog").modal("show");
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
        } else {
            readFileName(ele);
        }
    }

    /**
     * 预览上传的图片
     */
    function preview(ele){
        $("#previewWrap").show();
        if (ele.files && ele.files[0]) {// FileReader,IE10+、FF22/23、Chrome28/29支持
            if (typeof FileReader != 'undefined') {
                var reader = new FileReader;
                reader.onload = function(event) {
                    event = event || window.event;
                    var img = new Image();
                    img.onload = function(){
                        var imgEle = $(ele).parents(".form-group").find(".prev-img");
                        imgEle.attr("src", event.target.result);
                    };
                    img.src = event.target.result;
                };
                reader.readAsDataURL(ele.files[0]);
            }
        }else{
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

            if ( !! data) {
                data = data.replace(/[)'"%]/g, function(s) {
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

            var target = $("#imgPreview");
            var parent = target.parent();
            var div = $("<div>");
            parent.prepend(div);
            div.css({
                "filter":"progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)",
                position: "absolute",
                left: '20px',
                top: '20px'
            });
            div[0].filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = data;
            var w = parent.width();
            var h = w/oriWidth * oriHeight;
            div[0].style.width = w +"px";
            div[0].style.height = h +"px";
            target.css({height: h+"px"});
        }
    }


    /**
     * 表单验证
     */
    function initFormValidate() {
        $("#dataForm").validate({
            rules: {

                count: {
                    required: true,
                    min: 1,
                    max: ${notActiveCount}
                }
            },
            errorElement: "span",
            messages: {

                count: {
                    required: "请填写激活数量",
                    min: "激活数量在1~${notActiveCount}之间",
                    max: "激活数量在1~${notActiveCount}之间"
                }
            }
        });
    }

    function submitForm() {
        $("#submit-btn").on("click", function () {
            if ($("#dataForm").validate().form()) {
                $("#submit-dialog").modal("show");
            }
            return false;
        });
    }

    function listeners() {
        $("#startCardNumber").on("change", function () {
            updateEnd();
        });
        $("#count").on("input", function () {
            var val = $(this).val();
            if (val === "0") {
                val = "";
            }
            val = val.replace(/[^0-9]+/g, "");
            $(this).val(val);
        });
        $("#count").on("change", function () {
            updateEnd();
        });
    }

    function updateEnd() {
        var start = $("#startCardNumber").val();
        var count = $("#count").val();
        var leftCount =${notActiveCount!};

        //判断start是否存在
        if (!start) {
            return;
        }
        //判断start是否为正整数
        var te = /(^[1-9]([0-9]*)$|^[0-9]$)/;
        if (!te.test(count)) {
            return;
        }

        if (count > leftCount) {
            return;
        }

        count = count != "" ? parseInt(count) : 1;

        var length = 7;

        var pref = start.substring(0, start.length - length);
        var numStr = start.substr(start.length - length);
        var sum = parseInt(numStr) + count - 1;
        var sumStr = sum + "";
        var sumLength = sumStr.length;

        for (var i = 0; i < length - sumLength; i++) {
            sumStr = "0" + sumStr;
        }

        var ret = pref + sumStr;
        $("#endSerial").html(ret);
        $("#endCardNumber").val(ret);
    }

    /**
     * 检查文件类型
     * @param ele
     * @param suffix
     */
    function checkFileType(ele, suffix) {
        if (ele.val()) {
            var parts = ele.val().split(/\./);
            var ext = parts[parts.length - 1];
            if (ext) {
                ext = ext.toLowerCase();
                var reg = suffix || /(jpg|jpeg|png)$/;
                if (!reg.test(ext)) {
                    showTipDialog("图片格式不正确，只支持jpg，jpeg，png");
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
     * 截取文件名称
     * @param ele
     */
    function readFileName(ele) {
        var path = ele.value;
        var lastsep = path.lastIndexOf("\\");
        var filename = path.substring(lastsep + 1);
        $(ele).parent().children("input.file-text").val(filename);
    }

</script>
</body>
</html>