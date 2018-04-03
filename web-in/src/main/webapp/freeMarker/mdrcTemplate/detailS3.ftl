<!DOCTYPE html>
<#global  contextPath = rc.contextPath />

<#function getFileType filename>
    <#assign type="">
    <#if filename?? && (filename?index_of(".") > -1)  >
        <#assign list=filename?split(".")>
        <#assign type=list[list?size-1]>
    </#if>
    <#assign type=type?lower_case>
    <#return type>
</#function>

<#function isImage type>
    <#if type="jpg" || type="jpeg" || type="png" || type="gif" || type="bmp" || type="JPG" || type="JPEG" || type="PNG" || type="GIF" || type="BMP">
        <#return true>
    <#else>
        <#return false>
    </#if>
</#function>


<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-企业查看</title>
    <meta name="keywords" content="流量平台 企业编辑"/>
    <meta name="description" content="流量平台 企业编辑"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>
    <link rel="stylesheet" type="text/css" href="${contextPath}/manage/Js/artDialog/skins/default.css">


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
    </style>
</head>
<body>
<div class="main-container">

    <div class="module-header mt-30 mb-20">
        <h3>卡模板详情
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="back()">返回</a>
        </h3>
    </div>

    <form>
        <div class="tile mt-30">
            <div class="tile-header">
                卡模板信息
            </div>
            <div class="tile-content">
                <div class="row form">
                    <div class="form-group">
                        <label>模板名称：</label>
                        <span>${record.name!}</span>

                    </div>

                    <div class="form-group">
                        <label>流量包大小：</label>
                    <#if record.productSize??&&((record.productSize?number)<1024)>
                        <span>${((record.productSize?number))?string("#.##")}KB</span>
                    </#if>
                    <#if record.productSize?? && ((record.productSize?number)>=1024) && ((record.productSize?number)<1024*1024)>
                        <span>${((record.productSize?number)/1024.0)?string("#.##")}MB</span>
                    </#if>
                    <#if record.productSize?? && ((record.productSize?number)>=1024*1024)>
                        <span>${((record.productSize?number)/1024.0/1024.0)?string("#.##")}GB</span>
                    </#if>
                    </div>

                    <div class="form-group">
                        <label>卡面主题：</label>
                        <span>${record.theme!}</span>

                    </div>

                    <div class="form-group">
                        <label>创建日期：</label>
                    <#if record.createTime??>
                        <span>${record.createTime?string('yyyy-MM-dd HH:mm')} </span>
                    </#if>
                    </div>

                    <div class="form-group">
                        <label>资源文件数：</label>
                        <span>${record.resourcesCount!}</span>
                    </div>


                    <div class="form-group">
                        <label>正面预览图：</label>
                    <#if record.frontImage??>
                        <img src="${contextPath}/manage/mdrc/template/getFileS3.html?id=${record.id}&filename=${record.frontImage}"
                             style="max-width:360px;"/>
                    <#else>
                        -
                    </#if>

                    </div>

                    <div class="form-group">
                        <label>背面预览图：</label>
                    <#if record.rearImage??>
                        <img src="${contextPath}/manage/mdrc/template/getFileS3.html?id=${record.id}&filename=${record.rearImage}"
                             style="max-width:360px;"/>
                    <#else>
                        -
                    </#if>

                    </div>


                </div>
            </div>
        </div>


        <div class="tile mt-30">
            <div class="tile-header">
                上传卡图片
            </div>
            <div class="tile-content">
                <div class="row form">
                    <button type="button" class="btn btn-primary btn-sm pull-right btn-icon icon-back"
                            onclick="showUploader()">添加资源
                    </button>
                    <table class="table table-hover">
                        <tr>
                            <th width="20%" style="border-top:none;">预览</th>
                            <th width="20%" style="border-top:none;">文件名称</th>
                            <th width="20%" style="border-top:none;">大小</th>
                            <th width="20%" style="border-top:none;">类型</th>
                            <th width="20%" style="border-top:none;">操作</th>
                        </tr>
                    <#if files?? && (files?size>0)>
                        <#list files as file>
                            <tr>
                                <td align="center" style="text-align:center;">
                                    <#if isImage(file.fileType!)>
                                        <img src="${contextPath}/manage/mdrc/template/getFileS3.html?id=${record.id}&filename=${file.wholeName}"
                                             style="max-width:45px;"/>
                                    <#else>
                                        <img src="${contextPath}/icons/${file.fileType!}.png"/>
                                    </#if>
                                </td>
                                <td>${file.name}</td>
                                <td>${(file.length/1024)?int}KB</td>
                                <td>${file.fileType!}</td>
                                <td>
                                    <#if file.name??>
                                        <#if (record.frontImage?? && file.wholeName == record.frontImage) || (record.rearImage?? && file.wholeName == record.rearImage)>
                                            -
                                        <#else>
                                            <#if isImage(file.fileType)>
                                                <a href="${contextPath}/manage/mdrc/template/setCover.html?id=${record.id}&front=${file.wholeName}">设为封面</a>
                                                <a href="${contextPath}/manage/mdrc/template/setCover.html?id=${record.id}&rear=${file.wholeName}">设为背面</a>
                                            </#if>
                                            <a href="${contextPath}/manage/mdrc/template/delete.html?id=${record.id}&filename=${file.wholeName}"
                                               onclick="return confirm('是否确定删除 ${file.name} ？');">删除</a>
                                        </#if>
                                    </#if>
                                </td>
                            </tr>
                        </#list>
                    <#else>
                        <tr>
                            <td colspan="5">目前尚未上传资源文件，点击“添加资源”按钮添加模板图片。</td>
                        </tr>
                    </#if>
                    </table>


                </div>
            </div>
        </div>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
</div>

<div id="dialogContent" style="display:none;width:600px;">
    <div>
        <input type="file" name="file_upload" id="file_upload"/>
        <p>支持.gif .jpg .jpeg .png .bmp图片格式，图片大小不超过5M。</p>
        <p id="successTip"></p>
    </div>
</div>


<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    var hasUploaded = false;
    var successCount = 0;
    var selectedFilesNum = 0;


    require(["moment", "common", "bootstrap", "daterangepicker", "JQArtDialog", "uploadify"], function (mm, a, b, c) {
        //初始化
        init();
        // window["moment"] = hn;
        //初始化日期组件
        // initDateRangePicker();

        goback();

    });


    function init() {
        $('#file_upload').uploadify({
            'swf': '${contextPath}/assets/uploadify/uploadify.swf',
            'uploader': '${contextPath}/manage/mdrc/template/upload.html',
            'auto': false,
            'buttonClass': 'btn btn-info',
            'buttonText': '选择模板图片',
            'fileSizeLimit': '5MB',
            'preventCaching': true,
            'removeCompleted': false,
            'formData': {id: '${record.id}', 'userId': '${userId}'},
            //'fileTypeDesc' : '图片格式及平面设计文件格式',
            'fileTypeExts': '*.gif; *.jpg; *.jpeg; *.png; *.psd; *.ai; *.bmp',
            'onUploadComplete': function (file) {

            },

            'onDialogClose': function (queue) {
                selectedFilesNum = selectedFilesNum + queue.filesSelected;
            },

            'onClearQueue': function (queueItemCount) {
                /*		selectedFilesNum=0;*/
            },

            'onCancel': function (queueData) {
                selectedFilesNum--;
            },
            'onUploadSuccess': function (file, data, response) {
                if (data === "ok") {
                    successCount++;
                }
                if (data == "failed") {
                    alert("该文件已存在");
                }
            },
            'onQueueComplete': function (queueData) {
                var text = "已成功上传 " + queueData.uploadsSuccessful + " 个文件";
                if (queueData.uploadsErrored > 0) {
                    text += "，上传失败 <font color=\"red\">" + queueData.uploadsErrored + "</font> 个文件";
                }
                hasUploaded = true;
                $("#successTip").html(text);

                window.location = '${contextPath}/manage/mdrc/template/detail.html?id=${record.id}';
            }
        });
    }

    function back() {
        window.location.href = "${contextPath}/manage/mdrc/template/index.html";
    }
</script>

<script>

    var dialog = null;
    function showUploader() {
        //$("#successTip").text("");
        if (dialog == null) {
            art.dialog({
                lock: true,
                title: '添加资源文件',
                content: $('#dialogContent')[0],
                drag: false,
                resize: false,

                close: function () {
                    $('#file_upload').uploadify("cancel", "*");
                    selectedFilesNum = 0;
                    return true;
                },

                okVal: "确定上传",
                ok: function () {
                    if (selectedFilesNum == 0) {
                        alert("没有可以上传的图片资源，请添加模板图片！");
                        return false;
                    }
                    $('#file_upload').uploadify("upload", "*");
                    return false;
                }
            });
        } else {
            dialog.show();
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


</script>
</body>
</html>