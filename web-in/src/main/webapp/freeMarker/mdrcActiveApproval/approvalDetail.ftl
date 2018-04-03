<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-激活审批</title>
    <meta name="keywords" content="流量平台 激活审批"/>
    <meta name="description" content="流量平台 激活审批"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css"/>

    <!--[if lt IE 9]-->
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <!--[endif]-->

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

        .form label {
            width: 300px;
            text-align: right;
        }

        .form select {
            text-align: right;
            margin-left: 10px;
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

        #previewWrap {
            background: #F3F7FA;
            padding: 20px;
            display: block;
            position: relative;
            max-width: 350px;
            margin-left: 300px;
        }

        #imgPreview {
            max-width: 100%;
        }

        .form-group span {
            word-break: break-all;
        }

    </style>
</head>
<body>
<div class="main-container">
    <!-- <input type="hidden" name="id" value="1" /> 暂时用1替代  -->

    <div class="module-header mt-30 mb-20">
        <h3>激活申请详情
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>

    <div class="tile mt-30">
        <div class="tile-header">审批情况</div>
        <div class="tile-content" style="padding: 0;">
            <div role="tabpanel" class="tab-pane active table-responsive" id="table_wrap1">
                <div class="table-responsive">
                    <table class="table table-indent text-center table-bordered-noheader mb-0">
                        <thead>
                        <tr>
                            <th>审批状态</th>
                            <th>审批用户</th>
                        <#if provinceFlag??&&provinceFlag=="sc">
                            <th>用户</th>
                        <#else>
                            <th>用户职位</th>
                        </#if>
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
                                <td title="<#if opinion.comment??>${opinion.comment}</#if>">
                                    <#if opinion.comment??>
													${opinion.comment!}
												</#if>
                                </td>
                            </tr>
                            </#list>
                        </#if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <div class="tile mt-30" id="firstStep">
        <div class="tile-header">激活信息</div>
        <div class="tile-content">
            <div class="row">
                <div class="col-md-6">

                    <div class="form-group">
                        <label>企业名称:</label>
                        <span>${enterprise.name!}</span>
                    </div>

					<div class="form-group">
                        <label>卡名称:</label>
                        <span>${mdrcActiveDetail.configName!}</span>
                    </div>

                    <div class="form-group">
                        <label>批次号:</label>
                        <span>${(mdrcActiveDetail.serialNumber)!}</span>
                    </div>

                    <div class="form-group">
                        <label>流量套餐:</label>
                        <span>${product.name!}</span>
                    </div>

                    <div class="form-group">
                        <label>流量值:</label>
                        <span>${(product.productSize / 1024)!} MB</span>
                    </div> 
                                      
                    <div class="form-group">
                        <label>制卡数量:</label>
                        <span>${(mdrcActiveDetail.amount)!}</span>
                    </div>	
	
                    <div class="form-group">
                        <label>激活数量:</label>
                        <span>${mdrcActiveDetail.count!}</span>
                    </div>



                    <div class="form-group">
                        <label>起始卡序列号:</label>
                        <span>${mdrcActiveDetail.startCardNumber!}</span>
                    </div>

                    <div class="form-group">
                        <label>终止卡序列号:</label>
                        <span>${mdrcActiveDetail.endCardNumber!}</span>
                    </div>

                </div>
 <#--
                <div class="col-md-6">

                    <div class="form-group">
                        <label>正面预览图：</label>
                    <#if mdrcTemplate.frontImage??>
                        <img src="${contextPath}/manage/mdrc/template/getFileS3.html?id=${mdrcTemplate.id}&filename=${mdrcTemplate.frontImage}"
                             style="max-width:250px;"/>
                    <#else>
                        -
                    </#if>
                    </div>

                    <div class="form-group">
                        <label>背面预览图：</label>
                    <#if mdrcTemplate.rearImage??>
                        <img src="${contextPath}/manage/mdrc/template/getFileS3.html?id=${mdrcTemplate.id}&filename=${mdrcTemplate.rearImage}"
                             style="max-width:250px;"/>
                    <#else>
                        -
                    </#if>
                    </div>

                </div>

          
            <#if mdrcActiveDetail?? && mdrcActiveDetail.image??>
                    <div class="form-group">
                        <label>企业签收：</label>
                        <a name="entImage" id="entImage" onclick="downLoad(this)">${(mdrcActiveDetail.image)!}</a>
                    </div>

                    <div class="form-group" id="previewWrap">
                        <img src="${contextPath}/manage/mdrc/active/getImageFile.html?requestId=${mdrcActiveDetail.requestId!}"
                             id="imgPreview">
                    </div>
                </#if>
                -->

            </div>
        </div>
		<#if canOperate?? && canOperate == 1>
        <div class="tile-content">
            <form action="${contextPath}/manage/approval/saveApprovalForMdrcActive.html" method="post" name="mdrcActiveForm"
                  id="table_validate">

                <input type="hidden" name="approvalRecordId" id="approvalRecordId" value="${approvalRecordId!}">
                <input type="hidden" name="requestId" id="requestId" value="${requestId!}">
                <input type="hidden" name="processId" id="processId" value="${processId!}">

                <div class="tile-content">
                    <label>审批意见：</label>
                    <textarea rows="10" cols="100" maxlength="300" name="comment" id="comment"
                              class="hasPrompt"></textarea>
                </div>
                <div class="btn-save mt-30">
                    <input type="hidden" id="approvalStatus" name="approvalStatus" value="">
                    <a href="javascript:void(0)" class="btn btn-sm btn-warning dialog-btn" id="save-btn1">通过</a>&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="javascript:void(0)" class="btn btn-sm btn-warning dialog-btn" id="save-btn2">驳回</a>&nbsp;&nbsp;&nbsp;&nbsp;<span
                        style='color:red'>${errorMsg!}</span>
                </div>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>
        </div>
        </#if> 
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
        <p class="weui_toast_content">更新中</p>
    </div>
</div>
<!-- loading end -->

    <script src="${contextPath}/assets/lib/require.js"></script>
    <script src="${contextPath}/assets/js/config.js"></script>

    <script>

        require(["moment", "common", "bootstrap", "daterangepicker"], function (mm, a, b, c) {
            //初始化
            init();
            window["moment"] = mm;
            goback();
            checkFormValidate();

        });
    </script>

    <script>
        /**
         * 初始化
         */
        function init() {

            getProductSize();

            <#--
            $("input[type='file']").change(function () {
                readFileName(this);

                //审批截图
                if ($(this).hasClass("approveImage")) {
                    preview(this);
                }
            });
            -->

            //保存审批
            $("#save-btn1").on("click", function () {
                if ($("#table_validate").validate().form()) {
                	showToast();
                    $("#approvalStatus").val(1);
                    $("#table_validate").submit();
                }			
                return false;
            });
            $("#save-btn2").on("click", function () {
                if ($("#table_validate").validate().form()) {
                    $("#approvalStatus").val(0);
                    $("#table_validate").submit();
                }

                return false;
            });
        }

        function getProductSize() {
            var productSize = ${mdrcTemplate.productSize!};
            $("#productSize").html(sizeFun(productSize));
        }

        function sizeFun(size) {
            if (size == null) {
                return "-";
            }
            if (size < 1024) {
                return size + "KB";
            }
            if (size >= 1024 && size < 1024 * 1024) {
                return (size * 1.0 / 1024) + "MB";
            }
            if (size >= 1024 * 1024) {
                return (size * 1.0 / 1024 / 1024) + "GB";
            }

            return size * 1.0 / 1024 + "MB";
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

        /**
         * 预览上传的图片
         */
        function preview(ele) {
            $("#previewWrap").show();
            if (ele.files && ele.files[0]) {// FileReader,IE10+、FF22/23、Chrome28/29支持
                if (typeof FileReader != 'undefined') {
                    var reader = new FileReader;
                    reader.onload = function (event) {
                        event = event || window.event;
                        var img = new Image();
                        img.onload = function () {
                            $("#imgPreview").attr("src", event.target.result);
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

                var target = $("#imgPreview");
                var parent = target.parent();
                var div = $("<div>");
                parent.prepend(div);
                div.css({
                    "filter": "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)",
                    position: "absolute",
                    left: '20px',
                    top: '20px'
                });
                div[0].filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = data;
                var w = parent.width();
                var h = w / oriWidth * oriHeight;
                div[0].style.width = w + "px";
                div[0].style.height = h + "px";
                target.css({height: h + "px"});
            }
        }


        function goback() {
            if ($('#isModifyChanged').val() == 'true') {
                return confirm("是否确定不保存记录退出？");
            }
            else {
                return true;
            }
        }
        ;


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

<#--
        function downLoad(obj) {
            var type = obj.id;
            var fileName = obj.innerHTML;
            location.href = "${contextPath}/manage/mdrc/active/downloadFile.html?requestId=${mdrcActiveDetail.requestId!}" + "&type=entImage";
        }
        -->
    </script>
</body>
</html>