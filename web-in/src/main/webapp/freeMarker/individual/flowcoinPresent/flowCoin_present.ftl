<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>赠送</title>

    <link rel="stylesheet" href="${contextPath}/assets_individual/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/platform/iconfont.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/weui_toast.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/lib/daterangepicker/daterangepicker.css"/>

    <link rel="stylesheet" href="${contextPath}/assets_individual/theme/default/theme.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/main.min.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets_individual/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets_individual/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .form-group label {
            width: 80px;
        }

        #upload-count {
            display: inline-block;
            width: 147px;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mb-20">
        <h3>业务办理-赠送-<span id="nav-text">活动创建</span></h3>
    </div>

    <div class="mt-30">
        <ul class="nav nav-tabs" role="tablist">
            <li role="presentation" class="active"><a href="#createActivity" role="tab" data-toggle="tab">活动创建</a></li>
            <li role="presentation"><a href="#activityList" role="tab" data-toggle="tab">活动列表</a></li>
        </ul>

        <div class="tab-content">
            <div role="tabpanel" class="tab-pane active" id="createActivity">
                <div class="tile gray-border tile-noTopBorder">
                    <div class="form-group">
                        <label>流量币余额:</label>
                        <span class="green-text-lg">10000</span>&nbsp;&nbsp;个
                    </div>
                    <hr>

                    <form class="form" id="dataForm">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input type="hidden" class="form-control searchItem" id="managerId" name="managerId"
                               value="${managerId!}">

                        <div class="form-group form-group-sm form-inline">
                            <label>活动名称:</label>
                            <input class="form-control" id="activityName" name="activityName">
                        </div>
                        <div class="form-group form-group-sm form-inline">
                            <label>赠送号码:</label>
                            <input class="form-control mobileOnly" maxlength="11" name="presentTel" id="presentTel">
                            <span class="hidden" id="filename" style="display: inline-block;width: 153px;"></span>
                            <span class="file-box">
                                <a class="ml-10" id="upload-btn">批量上传</a>
                                <input type="file" class="file-helper" name="file" id="file">
                            </span>
                            <div class="promote"><span id="upload-count">请填写正确的11位手机号码</span><span class="ml-15">（可导入txt,xls,xlxs文件，一行为一个手机号码）</span>
                            </div>
                            <p style="color:red" class="red text-left" id="modal_error_msg"></p>
                        </div>
                        <div class="form-group form-group-sm form-inline">
                            <label>单个号码赠送:</label>
                            <input class="form-control mobileOnly" name="coins" id="coins"> 个流量币
                            <div class="promote">请填写正整数</div>
                        </div>
                    </form>

                </div>

                <div class="text-center mt-20">
                    <a class="btn btn-primary btn-sm btn-width" id="present-btn">赠 送</a>
                </div>


                <div class="mt-30"><h3>流量币赠送须知</h3></div>
                <hr>

                <div class="mt-20">
                    1、什么是流量币赠送？<br>
                    答：用户可将账户内的流量币赠送至其他用户账户中，其他用户登录平台即可进行兑换。<br>
                    2、什么是批量上传？<br>
                    答：赠送号码为多个时，可导入txt文件，文件一行为一个手机号码。相同一个文件的手机号码将被赠送相同数量的流量币。<br>
                    3、赠送出去的流量币有效期如何计算？<br>
                    答：用户通过赠送、红包、营销活动等形式获得的流量币将在获赠时间的次月月底失效。<br>
                    4、赠送对象是否必须为四川移动手机号码？<br>
                    答：是的，若为非四川移动手机号码将会赠送失败。<br>
                </div>
            </div>
            <div role="tabpanel" class="tab-pane" id="activityList">

                <div class="tile gray-border tile-noTopBorder">
                    <div class="mt-20">
                        <div class="form-inline text-right">
                            <span>创建日期:</span>
                            <div class="form-group form-group-sm form-inline" style="position: relative;">
                                <span id="search-time-range" class="date-range-wrap valign-middle">
                                    <input class="searchItem form-control" name="startDate" id="startDate"
                                           readonly="readonly">-
                                    <input class="searchItem form-control" name="endDate" id="endDate"
                                           readonly="readonly">
                                </span>
                            </div>
                            <a class="btn btn-sm btn-default btn-width ml-20" id="search-btn">查询</a>
                        </div>
                        <div id="table_wrap" class="mt-10 table-wrap text-center"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>


    <div id="messageBox"></div>
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
</div><!-- loading end -->

<!--[if lt IE 9]>
<script src="${contextPath}/assets_individual/lib/es5-shim.min.js"></script>
<script src="${contextPath}/assets_individual/lib/es5-sham.min.js"></script>
<![endif]-->

<!--[if lt IE 10]>
<script>
    Object.getPrototypeOf = function getPrototypeOf(object) {
        return object.__proto__;
    };
</script>
<![endif]-->

<script>
    if (window.ActiveXObject) {
        var reg = /10\.0/;
        var str = navigator.userAgent;
        if (reg.test(str)) {
            Object.getPrototypeOf = function getPrototypeOf(object) {
                return object.__proto__;
            };
        }
    }
</script>


<script src="${contextPath}/assets_individual/lib/require.js"></script>
<script src="${contextPath}/assets_individual/dist/config.js"></script>
<script>
    var messageBox;
    require(["react", "react-dom", "../js/listData", "MessageBox", "common", "bootstrap", "daterangepicker", "upload"], function (React, ReactDOM, ListData, MessageBox) {
        renderTable(React, ReactDOM, ListData);
        messageBox = renderWidget(React, ReactDOM, MessageBox, {
            grid: 0.35,
            confirm: function () {
                $('a[href="#activityList"]').tab('show');
                //重新查询
                $("#search-btn").click();
                return true;
            }
        }, $("#messageBox")[0]);

        initSearchDateRangePicker();

        //文件上传
        fileListener();
        initFormValidate();

        listeners();
    });


    var opFormat = function (value, column, row) {
        return ["<a class='btn-icon icon-down mr-5' href='${contextPath}/individual/flowcoinpresent/detail.html?${_csrf.parameterName}=${_csrf.token}&id=" + row.id + "'>详情</a>"];
    };
    var COLORS = {
        "1": "#377BC4",
        "2": "#EA6500",
        "4": "#8FC31C"
    };
    var renderTable = function (React, ReactDOM, ListData) {
        var ele = React.createElement(ListData, {
            action: "${contextPath}/individual/flowcoinpresent/search.html?${_csrf.parameterName}=${_csrf.token}",
            columns: [
                {name: "name", text: "活动名称"},
                {name: "createTime", text: "创建时间", format: "DateTimeFormat"},
                {name: "prizeCount", text: "赠送号码数"},
                {
                    name: "op", text: "操作", format: function (value, column, row) {
                    return "<a href='${contextPath}/individual/flowcoinpresent/detail.html?activityId=" + row.activityId + "' style='color: " + COLORS["1"] + "'><i class='iconfont icon-detail'></i>详情</a>"

                }
                }
            ]
        });
        ReactDOM.render(ele, $("#table_wrap")[0]);
    };

    function initSearchDateRangePicker() {
        var ele = $('#search-time-range');
        var startEle = $('#startDate');
        var endEle = $('#endDate');
        ele.dateRangePicker({
            separator: '~',
            container: ele.parent()[0],
            getValue: function () {
                if (startEle.val() && endEle.val())
                    return startEle.val() + '~' + endEle.val();
                else
                    return '';
            },
            setValue: function (s, s1, s2) {
                startEle.val(s1);
                endEle.val(s2);
            }
        });
    }

    /**
     * 表单验证
     */
    function initFormValidate() {
        $("#dataForm").validate({
            errorPlacement: function (error, element) {
                error.addClass("error-tip");
                element.closest(".form-group").append(error);
            },
            rules: {
                activityName: {
                    required: true,
                    cmaxLength: 64
                },
                coins: {
                    required: true,
                    max: 1000000,
                    min: 1,
                    positive: true
                }
            },
            errorElement: "span",
            messages: {
                activityName: {
                    required: "请填写活动名称",
                    cmaxLength: "活动名称长度不超过64个字符"
                },
                coins: {
                    required: "请填写流量币数量",
                    max: "请正确填写1-1000000的正整数",
                    min: "请正确填写1-1000000的正整数",
                    positive: "请正确填写1-1000000的正整数"
                }
            }
        });
    }

    /**
     * 监听
     */
    function listeners() {
        $("#present-btn").on("click", function () { 
        	$("#modal_error_msg").html("");      	
        	if ($("#dataForm").validate().form()) {
	            
	            //todo
	            var activityName = $("#activityName").val();
	            var presentTel = $("#presentTel").val();
	            var coins = $("#coins").val();
	            
	            //校验输入一个号码时输入的是正确的号码
	            if(presentTel.length<11){
	            	$("#upload-count").html("请填写正确的手机号码").addClass("error-tip");
	            	return;
	            }
	            else{
	            	var a = /^((\(\d{3}\))|(\d{3}\-))?13\d{9}|14[57]\d{8}|15\d{9}|18\d{9}$/;
			        if (!presentTel.match(a)) {
			            $("#upload-count").html("请填写正确的手机号码").addClass("error-tip");
			            return;
			        }
	            }
				
				showToast();
	            //ajax
	            $.ajax({
	                type: "POST",
	                url: "${contextPath}/individual/flowcoinpresent/presentCoins.html?${_csrf.parameterName}=${_csrf.token}",
	                data: {
	                    activityName: activityName,
	                    presentTel: presentTel,
	                    coins: coins
	                },
	                dataType: "json", //指定服务器的数据返回类型，
	                success: function (res) {
	                    if (res.success && res.success == "success") {
	                        //逻辑完成后
	                        hideToast();
	
	                        formReset();
	
	                        //弹框提示
	                        messageBox.show('<div class="modal-body text-center">' +
	                                '<div class="">流量币赠送已成功受理，具体赠送情况您可在活动列表中的详情或流量币余额收支明细中查询，谢谢！</div>' +
	                                '</div>');
	                    }
	                    else {
	                        hideToast();
	                        alert("生成活动失败");
	                        //showTip(res.errorMsg);
	                    }
	                }
	            });

            }
        });
        
        $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
            $("#nav-text").html($(this).text());
        });
    }

    function formReset() {
        $("#dataForm")[0].reset();
    }

    /**
     * 文件上传
     */
    function fileListener() {
        $("#file").on("change", function () {
            var filename = $("#file").val();
            var index = filename.lastIndexOf("\\");
            filename = filename.substr(index + 1);

            upload(filename);
            var clone = $(this).clone();
            $(this).parent().append(clone);
            clone.remove();

            fileListener();
        });
    }

    /**
     * 批量上传
     */
    function upload(filename) {
    	$("#modal_error_msg").html("");
    	$("#upload-count").html("");
        $.ajaxFileUpload({
            url: '${contextPath}/individual/flowcoinpresent/uploadPhones.html?${_csrf.parameterName}=${_csrf.token}',
            secureuri: false,
            fileElementId: 'file',
            dataType: 'json',
            success: function (data) {
                if (data) {
                    if (data.successMsg == "success") {
                        console.log(data.correctMobiles);
                        $("#presentTel").hide();
                        $("#filename").removeClass("hidden").html(filename);

                        $("#presentTel").val(data.correctMobiles);
                        $("#upload-count").html("已成功导入" + data.correctMobiles.length + "个手机号码").addClass("error-tip");
                    }

                    if (data.successMsg == "fail") {
                        $("#modal_error_msg").html("请至少上传一个合法的手机号!");
                    }

                    if (data.errorMsg) {
                        $("#modal_error_msg").html(data.errorMsg);
                    }
                }
            },
            error: function (data) {
                $("#modal_error_msg").html("上传文件失败");
            }
        });
        return false;
    }

</script>
</body>
</html>