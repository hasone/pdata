<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-营销卡管理-激活详情</title>
    <meta name="keywords" content="流量平台 营销卡详情"/>
    <meta name="description" content="流量平台 营销卡详情"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <style>
        .table > thead > tr > th {
            line-height: initial;
        }

        .table > thead > tr > th, .table > tbody > tr > td {
            vertical-align: middle;
        }

        .icon-detail {
            color: #ca95db;
            background-image: url(${contextPath}/assets/imgs/icon-detail.png);
        }

        .icon-edit {
            color: #5faee3;
            background-image: url(${contextPath}/assets/imgs/icon-edit.png);
        }

        .icon-del {
            color: #fa8564;
            background-image: url(${contextPath}/assets/imgs/icon-del.png);
        }

        .icon-down {
            color: #aec785;
            background-image: url(${contextPath}/assets/imgs/icon-down.png);
        }

        #previewWrap {
            background: #F3F7FA;
            padding: 20px;
            display: block;
            position: relative;
            max-width: 350px;
            margin-left: 70px;
        }

        #imgPreview {
            max-width: 100%;
        }

    </style>

    <!--[if lt IE 9]-->
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <!--[endif]-->
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>激活详情
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" 
            onclick="javascript:window.location.href='${contextPath}/manage/mdrc/active/index.html'">返回</a>
        </h3>
    </div>

    <#if wheatherNeedApproval??&&wheatherNeedApproval=="true">
        <div class="tile mt-30">
            <div class="tile-header">历史审批记录</div>
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
    </#if>

    <div class="tile mt-30">
        <div class="tile-header">提交信息</div>
        <div class="tile-content">
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label>企业名称:</label>
                        <span class="detail-value">${enterprise.name!}</span>
                    </div>

                    <div class="form-group">
                        <label>企业编码:</label>
                        <span class="detail-value">${enterprise.code!}</span>
                    </div>

                    <div class="form-group">
                        <label>激活数量:</label>
                        <span class="detail-value">${mdrcActiveDetail.count!}&nbsp;张</span>
                    </div>

                    <div class="form-group">
                        <label>卡模板:</label>
                        <span class="detail-value">${mdrcTemplate.name!}</span>
                    </div>

                    <div class="form-group">
                        <label>卡面主题:</label>
                        <span class="detail-value">${mdrcTemplate.theme!}</span>
                    </div>

                    <div class="form-group">
                        <label>卡面值:</label>
                        <span class="detail-value" id="productSize"></span>
                    </div>

                    <div class="form-group">
                        <label>产品名称:</label>
                        <span class="detail-value">${product.name!}</span>
                    </div>

                    <div class="form-group">
                        <label>产品编码:</label>
                        <span class="detail-value">${product.productCode!}</span>
                    </div>

                    <div class="form-group">
                        <label>状态:</label>
                        <span class="detail-value">
                        <#if approvalRequest??>
                            <#if approvalRequest.result==0>
                                审批中
                            </#if>

                            <#if approvalRequest.result==1>
                                <#if mdrcActiveDetail.activeStatus==0>
                                    未处理
                                </#if>
                                <#if mdrcActiveDetail.activeStatus==1>
                                    已处理
                                </#if>
                            </#if>

                            <#if approvalRequest.result==2>
                                已驳回
                            </#if>
                        </#if>

                        </span>
                    </div>


                </div>

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

                <#--
                <#if mdrcActiveDetail?? && mdrcActiveDetail.image??>
                    <div class="form-group">
                        <label>企业签收：</label>
                        <a name="image" id="image" onclick="downLoad(this)">${(mdrcActiveDetail.image)!}</a>
                    </div>

                    <div class="form-group" id="previewWrap">
                        <img src="${contextPath}/manage/mdrc/active/getImageFile.html?requestId=${mdrcActiveDetail.requestId!}"
                             id="imgPreview">
                    </div>
                </#if>
                -->

                </div>
            </div>
        </div>
    </div>

    <#if mdrcBatchConfig?? && mdrcActiveRequestConfigs??>
        <div class="tile mt-30">
            <div class="tile-header">卡信息</div>
            <div class="tile-content">
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label>批次名称:</label>
                            <span class="detail-value">${mdrcBatchConfig.configName!}</span>
                        </div>

                        <div class="form-group">
                            <label>批次号:</label>
                            <span class="detail-value">${mdrcBatchConfig.serialNumber!}</span>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <#list mdrcActiveRequestConfigs as item>
                            <div class="form-group">
                                <label>激活时间:</label>
                                <span class="detail-value">${(item.createTime?datetime)!}</span>
                            </div>

                            <div class="form-group">
                                <label>激活起始序列号:</label>
                                <span class="detail-value">${item.startSerial!}</span>
                            </div>

                            <div class="form-group">
                                <label>激活终止序列号:</label>
                                <span class="detail-value">${item.endSerial!}</span>
                            </div>
                        </#list>
                    </div>
                </div>
            </div>
        </div>
    </#if>

</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    require(["common", "bootstrap"], function () {

        init();
    });

    /**
     * 初始化
     */
    function init() {

        getProductSize();

        $("input[type='file']").change(function () {
            readFileName(this);

            //审批截图
            if ($(this).hasClass("approveImage")) {
                preview(this);
            }
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

    function downLoad(obj) {
        var type = obj.id;
        var fileName = obj.innerHTML;
        location.href = "${contextPath}/manage/mdrc/active/downloadFile.html?requestId=${mdrcActiveDetail.requestId!}" + "&type=entImage";
    }
</script>
</body>
</html>