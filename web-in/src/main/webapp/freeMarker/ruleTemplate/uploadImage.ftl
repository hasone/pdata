<#global contextPath = rc.contextPath >

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
    <#if type="jpg" || type="jpeg" || type="png">
        <#return true>
    <#else>
        <#return false>
    </#if>
</#function>

<!DOCTYPE html>
<html>
<head>
    <title>活动模板详情</title>
    <meta charset="UTF-8">
    <script src="${contextPath}/manage/assets/js/ajaxfileupload.js"></script>
    <link rel="stylesheet" type="text/css" href="${contextPath}/assets/uploadify/uploadify.css">
    <link rel="stylesheet" type="text/css" href="${contextPath}/manage/Js/artDialog/skins/default.css">
    <style>
        div#file_upload-button {
            padding-top: 0;
        }

        .uploadify-queue-item .cancel a {
            outline: none;
        }

        .add-res {
            position: relative;
        }

        #imgFile {
            width: 98px;
            height: 28px;
            position: absolute;
            top: 0;
            left: 0;
            opacity: 0;
            filter: alpha(opacity=0);
            z-index: 1;
        }
    </style>
    <script type="text/javascript" src="${contextPath}/assets/uploadify/jquery.uploadify.js"></script>
    <script src="${contextPath}/manage/Js/artDialog/artDialog.js"></script>
    <script src="${contextPath}/manage/Js/artDialog/jquery.artDialog.js"></script>
    <script type="text/javascript">
        /*var hasUploaded = false;
        var successCount = 0;
        var selectedFilesNum = 0;
            $(function() {
                $('#file_upload').uploadify({
                    'swf'      : '${contextPath}/assets/uploadify/uploadify.swf',
			'uploader' : '${contextPath}/manage/rule_template/upload.html',
			'auto' : false,
			'buttonClass' : 'btn btn-info', 
			'buttonText' : '选择模板图片',
			'fileSizeLimit' : '2MB',
			'preventCaching' : true,
			'removeCompleted' : false,
			'formData' : {id: '${ruleTemplate.id}','userId': '${userId}'},
			//'fileTypeDesc' : '图片格式及平面设计文件格式',
			'fileTypeExts' : '*.jpg; *.jpeg; *.png,
			'onUploadComplete' : function(file) {
			
			},
		
			'onDialogClose' : function(queue){
				selectedFilesNum = selectedFilesNum + queue.filesSelected;
			},
			
			'onClearQueue' : function(queueItemCount){

			},
			
			'onCancel' : function(queueData){
				selectedFilesNum --;
			},
			'onUploadSuccess' : function(file, data) {
				if(data === "ok") {
					successCount++;
				}
				
			},
			'onError' : function(file,data){
				if(data == "repeat"){
					alert("上传失败，该文件已存!");
				}
				if(data == "abnormal"){
					alert("上传失败，文件读取异常！");
				}
				if(data == "forbit"){
					alert("对不起，该条记录不是由您创建，您无权限上传图片！");
				}
			},
			'onQueueComplete' : function(queueData) {
				var text = "已成功上传 " + queueData.uploadsSuccessful + " 个文件";
				if (queueData.uploadsErrored > 0) {
					text += "，上传失败 <font color=\"red\">" + queueData.uploadsErrored + "</font> 个文件";
				}
				hasUploaded = true;
				$("#successTip").html(text);
				
				window.location = '${contextPath}/manage/rule_template/uploadImage.html?id=${ruleTemplate.id}'; 
			}
		});
	});

var dialog = null;
function showUploader(){
	$("#successTip").text("");	
	if(dialog == null) {
	art.dialog({
			lock: true,
		    title: '添加资源文件',
		    content: $('#dialogContent')[0],
		    drag: false,
		    resize: false,
		    
		    close: function(){
		    	$('#file_upload').uploadify("cancel", "*");
		    	selectedFilesNum=0;
		    	return true;
		    },
		    
		    okVal: "确定上传",
		    ok: function() {
		    	if(selectedFilesNum == 0){
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
}*/
    </script>
</head>
<body>
<div class="page-header">
    <h1>活动模板详情<a class="btn btn-white pull-right"
                 href="${contextPath}/manage/rule_template/index.html?pageNum=${pageNum!}">返回</a></h1>
</div>
<div class="row">
    <div class="col-xs-12">
        <form class="form-horizontal" role="form">
            <div class="form-group">
                <label class="col-sm-3 control-label">模板名称</label>
                <div class="col-sm-9">
                    <label>${ruleTemplate.name!}</label>
                </div>
            </div>
        <#--
            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label">创建用户</label>
                <div class="col-sm-9">
                    <label> ${ruleTemplate.creatorName!} </label>
                </div>
            </div>
        -->
            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label">创建日期</label>
                <div class="col-sm-9">
                    <label>${ruleTemplate.createTime?string('yyyy-MM-dd HH:mm:ss')}</label>
                </div>
            </div>

            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label">图片个数</label>
                <div class="col-sm-9">
                    <label>${ruleTemplate.imageCnt!}</label>
                </div>
            </div>

            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label">图片预览</label>
                <div class="col-sm-9">
                <#if ruleTemplate.image??>
                    <img src="${contextPath}/manage/rule_template/getFile.html?id=${ruleTemplate.id}&filename=${ruleTemplate.image}"
                         style="max-width:360px;"/>
                <#else>
                    -
                </#if>
                </div>
            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
    </div>
</div>

<div class="page-header">
    <h1>资源文件列表
        <!-- <button type="button" class="btn btn-info pull-right add-res" onclick="showUploader()">添加资源</button> -->
        <a class="btn btn-info pull-right">添加资源
            <input type="file" name="file" id="imgFile" accept="image/jpg,image/jpeg,image/png">
        </a>
    </h1>
</div>
<div class="row">
    <div class="col-xs-12">
        <div class="table-responsive">
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
                    <#assign fileType = getFileType(file.name) />
                    <tr>
                        <td align="center" style="text-align:center;">
                            <#if isImage(fileType)>
                                <img src="${contextPath}/manage/rule_template/getFile.html?id=${ruleTemplate.id}&filename=${file.name}"
                                     style="max-width:45px;"/>
                            <#else>
                                <img src="${contextPath}/icons/${fileType}.png"/>
                            </#if>
                        </td>
                        <td>${file.name}</td>
                        <td>${(file.length()/1024)?int}KB</td>
                        <td>${fileType}</td>
                        <td>
                            <#if file.name??>
                                <#if (ruleTemplate.image?? && file.name == ruleTemplate.image)>
                                    -
                                <#else>
                                    <#if isImage(fileType)>
                                        <a href="${contextPath}/manage/rule_template/setCover.html?id=${ruleTemplate.id}&front=${file.name}">设为模板</a>
                                    </#if>
                                    <a href="${contextPath}/manage/rule_template/delete.html?id=${ruleTemplate.id}&filename=${file.name}"
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
<div id="dialogContent" style="display:none;width:600px;">
    <div>
        <input type="file" name="file_upload" id="file_upload"/>
        <p>支持.jpg .jpeg .png文件格式</p>
        <p id="successTip"></p>
    </div>
</div>

<script>
    $(function () {
        $(".page-header").on("change", "#imgFile", function () {
            if ($(this).val()) {
                var parts = $(this).val().split(/\./);
                var ext = parts[parts.length - 1];
                if (ext) {
                    ext = ext.toLowerCase();
                    if (!/(jpg|jpeg|png)$/.test(ext)) {
                        alert("图片格式不正确，只支持jpg，jpeg，png");
                    }
                }
            }
            var file = this;
            if (file.files && file.files[0]) {
                // FileReader,IE10+、FF22/23、Chrome28/29支持
                if (typeof FileReader != 'undefined') {
                    var reader = new FileReader;
                    reader.onload = function (event) {
                        event = event || window.event;
                        var img = new Image();
                        img.onload = function () {
                            checkSizeValid(img.width, img.height);
                        }
                        img.src = event.target.result;
                    };
                    reader.readAsDataURL(file.files[0]);
                }
            } else {
                var preload = document.createElement("div"),
                        body = document.body,
                        data, oriWidth, oriHeight, ratio;

                preload.style.cssText = "width:1px;height:1px;visibility: hidden;position: absolute;left: 0px;top: 0px";
                // 设置sizingMethod='image'
                preload.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='image')";
                body.insertBefore(preload, body.childNodes[0]);
                file.select();
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

                checkSizeValid(oriWidth, oriHeight);
            }
        });
    });

    /**
     * 验证图片的尺寸
     */
    function checkSizeValid(w, h) {
        if (w != 1280) {
            alert("图片的长度为" + w + "px 不符合要求应为1280px");
            return false;
        }
        if (h != 720) {
            alert("图片的宽度为" + h + "px 不符合要求应为720px");
            return false;
        }

        doUpload();
    }

    /**
     * 都正确后上传文件
     */
    function doUpload() {
        var uploadPath = '${contextPath}/manage/rule_template/upload.html';
        $.ajaxFileUpload({
            url: uploadPath,
            secureuri: false,
            fileElementId: 'imgFile',
            dataType: 'json',
            data: {id: '${ruleTemplate.id}', 'userId': '${userId}'},
            success: function (data, status) {
                if (data) {
                    if (!data.success) {
                        alert(data.msg);
                    } else {
                        window.location = '${contextPath}/manage/rule_template/uploadImage.html?id=${ruleTemplate.id}';
                    }
                }
            },
            error: function (data, status, e) {
                alert("上传文件失败");
            },
            complete: function () {
                var parent = $("#imgFile").parent();
                $("#imgFile").remove();
                parent.append('<input type="file" name="file" id="imgFile" accept="image/jpg,image/jpeg,image/png">');
            }
        });
    }
</script>
</body>
</html>