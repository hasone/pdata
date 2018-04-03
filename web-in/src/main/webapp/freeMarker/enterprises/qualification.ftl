<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-企业认证</title>
    <meta name="keywords" content="流量平台 企业认证"/>
    <meta name="description" content="流量平台 企业认证"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css"/>

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

        .form span {
            word-break: break-all;
        }

        .file-box {
            display: inline-block;
        }

        .btn-save {
            margin-left: 350px;
        }
    </style>
</head>
<body>
<div class="main-container">
    <input type="hidden" name="id" id="id" value="${enterprise.id!}"/>

    <div class="module-header mt-30 mb-20">
        <h3>企业认证信息
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back"
               onclick="javascript:window.location.href='${contextPath}/manage/potentialCustomer/indexPotential.html'">返回</a>
        </h3>
    </div>
<#if edit??&&edit==true>
    <div class="tile mt-30">
        <div class="tile-header">审批信息查看</div>
        <div class="tile-content" style="padding: 0;">
            <div role="tabpanel" class="tab-pane active table-responsive" id="table_wrap1">
                <div class="table-responsive">
                    <table class="table table-indent text-center table-bordered-noheader mb-0">
                        <thead>
                        <tr>
                            <th>企业名称</th>
                            <th>审批时间</th>
                            <th>审批状态</th>
                            <th>审批意见</th>
                        </tr>
                        </thead>
                        <tbody>
                            <#if entApprovalRecords??>
                                <#list entApprovalRecords as entApprovalRecord>
                                <tr>
                                    <td>${entApprovalRecord.name!}</td>
                                    <td>${entApprovalRecord.createTime?datetime!}</td>
                                    <td>${entApprovalRecord.description!}</td>
                                    <td title="${entApprovalRecord.operatorComment!}">${entApprovalRecord.operatorComment!}</td>
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


    <form action="${contextPath}/manage/enterprise/saveQualification.html?${_csrf.parameterName}=${_csrf.token}"
          method="post" name="enterprisesForm"
          id="table_validate" enctype="multipart/form-data">
        <input type="hidden" id="id" name="id" value="${enterprise.id!}">

        <div class="tile mt-30">
            <!--企业认真信息-->
            <div class="tile-content" id="qualificationInfo" style="display:">
                <div class="row form">
                    <div class="form-group">
                        <label>企业工商营业执照：</label>

                        <div class="file-box">
                            <input type="text" class="file-text" value="">
                            <a href="javascript:void(0)" class="btn btn-sm btn-cyan">浏览</a>
                            <input type="file" class="file-helper imge-check approveImage" id="licenceImage"
                                   name="licenceImage" accept="image/*" required>
                        </div>
                        <div class="prompt">企业工商营业执照清晰彩色原件扫描件,大小不超过5M，支持jpeg,jpg,png</div>
                    </div>

                    <div class="form-group">
                        <label>营业执照有效期：</label>
	                    <span id="search-time-range1" class="daterange-wrap">
		                    <input type="text" style="width:165px" class="search-startTime" name="licenceStartTime"
                                   id="licenceStartTime" placeholder=""> 至
		                    <input type="text" style="width:165px" class="search-endTime" name="licenceEndTime"
                                   id="licenceEndTime" placeholder="" required>
	                    </span>
                    </div>

                    <div class="form-group">
                        <label>企业管理员授权证明：</label>

                        <div class="file-box">
                            <input type="text" class="file-text" value="">
                            <a href="javascript:void(0)" class="btn btn-sm btn-cyan">浏览</a>
                            <input type="file" class="file-helper" id="authorization" name="authorization" required>

                        </div>
                        <a onclick="downLoadTemplate(this)" style="width: 242px;">模板下载</a>

                        <div class="prompt">请按照模板填写相应信息并上传文件，大小不超过5M</div>
                    </div>

                    <div class="form-group">
                        <label>企业管理员身份证（正面）：</label>

                        <div class="file-box">
                            <input type="text" class="file-text" value="">
                            <a href="javascript:void(0)" class="btn btn-sm btn-cyan">浏览</a>
                            <input type="file" class="file-helper imge-check" id="identification" name="identification"
                                   accept="image/*" required>
                        </div>
                        <div class="prompt">请上传身份证正面扫描件，大小不超过5M，支持jpeg,jpg,png</div>
                    </div>

                    <div class="form-group">
                        <label>企业管理员身份证（反面）：</label>

                        <div class="file-box">
                            <input type="text" class="file-text" value="">
                            <a href="javascript:void(0)" class="btn btn-sm btn-cyan">浏览</a>
                            <input type="file" class="file-helper imge-check" id="identificationBack"
                                   name="identificationBack" accept="image/*" required>
                        </div>
                        <div class="prompt">请上传身份证反面扫描件，大小不超过5M，支持jpeg,jpg,png</div>
                    </div>

                </div>

            </div>
        </div>

        <div class="btn-save mt-30 " id="buttons2" style="display:">
        <#--<a href="javascript:void(0)" class="btn btn-sm btn-warning dialog-btn" id="before">上一步</a>-->
            <a href="javascript:void(0)" class="btn btn-sm btn-warning dialog-btn" id="refuse-btn">取消</a>
            <a href="javascript:void(0)" class="btn btn-warning btn-sm mr-10 dialog-btn" id="save-btn1">保存</a>&nbsp;&nbsp;&nbsp;&nbsp;<span
                style='color:red'>${errorMsg!}</span>
        </div>
    </form>

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

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>

    require(["moment", "common", "bootstrap", "daterangepicker"], function (mm, a, b, c) {
        //初始化
        init();
        window["moment"] = mm;
        //初始化日期组件
        initDateRangePicker1();

        checkFormValidate();
        goback();

    });
</script>

<script>
    /**
     * 初始化
     */
    function init() {
        //企业基本信息中的下一步
        //$("#next").on("click", function () {
        //    $("#qualificationInfo").show();
        //    $("#buttons2").show();
        //   $("#baseInfo").hide();
        //    $("#buttons1").hide();
        //     return false;
        //});

        //企业合作信息的上一步
        // $("#before").on("click", function () {
        //     $("#baseInfo").show();
        //     $("#buttons1").show();
        //    $("#qualificationInfo").hide();
        //     $("#buttons2").hide();
        //     return false;
        //  });


        $("input[type='file']").change(function () {
            fileChoiceHandler(this);
        });

        //保存企业信息
        $("#save-btn1").on("click", function () {
            if ($("#table_validate").validate().form()) {
                showToast();
                $("#table_validate").submit();
                //hideToast();
            }
            return false;
        });

        //保存企业信息
        $("#refuse-btn").on("click", function () {
            window.location.href = "${contextPath}/manage/potentialCustomer/indexPotential.html";
            return false;
        });


    }

    /**
     * 文件选择事件监听
     */
    function fileChoiceHandler(ele) {
        readFileName(ele);

        if ($(ele).hasClass("imge-check")) {
            if (checkFileType($(ele))) {
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

    function checkFormValidate() {
        $("#table_validate").validate({
            errorPlacement: function (error, element) {

                $(element).parent().find('.error-tip').remove();

                error.addClass("error-tip");
                if ($(element).siblings('div').hasClass('prompt')) {
                    $(element).siblings('div.prompt').before(error);
                } else {
                    $(element).parent().append(error);
                }
            },
            errorElement: "span",
            rules: {
                licenceStartTime: {
                    required: true
                },
                licenceEndTime: {
                    required: true
                },
                licenceImage: {
                    required: true
                },
                authorization: {
                    required: true
                },
                identification: {
                    required: true
                },
                identificationBack: {
                    required: true
                }
            },
            messages: {
                licenceStartTime: {
                    required: "营业执照有效期不能为空"
                },
                licenceEndTime: {
                    required: "营业执照有效期不能为空"
                },
                licenceImage: {
                    required: "请上传企业工商营业执照"
                },
                authorization: {
                    required: "请上传授权证明"
                },
                identificationBack: {
                    required: "请上传身份证反面"
                },
                identification: {
                    required: "请上传身份证正面"
                }
            }
        });
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

    function initDateRangePicker1() {
        var ele = $('#search-time-range1');

        var startEle = $('#licenceStartTime');
        var endEle = $('#licenceEndTime');

        ele.dateRangePicker({
            separator: ' ~ ',
            showShortcuts: true,
            container: $("#search-time-range1").parent(),
            customShortcuts: [
                {
                    name: '半年内',
                    dates: function () {
                        var end = new Date();
                        var start = new Date();
                        start.setDate(start.getDate() + 182);
                        return [start, end];
                    }
                },
                {
                    name: '一年内',
                    dates: function () {
                        var end = new Date();
                        var start = new Date();
                        start.setDate(start.getDate() + 360);
                        return [start, end];
                    }
                }
            ],
            getValue: function () {
                if (startEle.val() && endEle.val())
                    return startEle.val() + ' ~ ' + endEle.val();
                else
                    return '';
            },
            setValue: function (s, s1, s2) {
                startEle.val(s1);
                endEle.val(s2);
            }
        });
    }

    function downLoadTemplate(obj) {
        var type = obj.id;
        var fileName = obj.innerHTML;
        location.href = "${contextPath}/manage/enterprise/exportTemplate.html";
    }

</script>
</body>
</html>