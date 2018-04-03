<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>四川流量平台-编辑红包</title>
    <meta name="keywords" content="四川流量平台 编辑红包"/>
    <meta name="description" content="四川流量平台 编辑红包"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>

    <!--[if lt IE 9]-->
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <!--[endif]-->

    <style>
        .form input[type='text'],
        .form input[type='number'],
        .form select {
            background: transparent;
            border: 1px solid #ccc;
            outline: none;
            border-radius: 3px;
            padding: 3px 10px;
            width: 323px;
        }

        .form .table input[type='text'],
        .form .table input[type='number'],
        .form .table select {
            width: auto;
        }
        label{
        	width: 130px;
        	text-align: right;
        }
        #startTime {
            width: 150px;
        }

        #endTime {
            width: 150px;
        }

        textarea {
            width: 323px;
            resize: none;
            height: 75px;
            outline: none;
            border: 1px solid #ccc;
        }

        .preview {
            width: 190px;
            height: 336px;
            border: 1px solid #ccc;
            margin: 0 auto;
        }

        .dropdown-menu {
            left: 0;
            right: 4px;
        }

        th, td {
            vertical-align: middle !important;
        }

        input.count, input.size {
            width: 100px;
            text-align: center;
        }

        table .btn-sm {
            padding: 2px 10px !important;
        }

        .error-tip-setting {
            display: block;
            text-align: left;
            color: red;
        }

        .error-tip {
            display: block;
            margin-left: 133px;
            color: red;
        }

        .promote {
            font-size: 12px;
            color: #959595;
            margin-left: 133px;
        }

        .btn-visited{
            color: #fff;
            background-color: #919191;
            border-color: #919191;
        }

        .btn-visited:hover{
            color: #fff;
        }

        #setting-form label{
            vertical-align: top;
        }

        .right-wrap{
            display: inline-block;
        }

        .right-wrap .promote{
            margin-left: 0;
            text-align: left;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>编辑红包
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back"
               onclick="history.go(-1)">返回</a>
        </h3>
    </div>


    <div class="tile mt-30">
        <div class="tile-header">
            活动信息
        </div>
        <div class="tile-content">
            <form class="row form" id="dataForm">
                <div class="col-md-6">
                    <div class="form-group">
                        <label>企业名称：</label>
                        <div class="btn-group btn-group-sm">
                            <input name="entId" id="entId" value="${enter.id!}"
                                   style="width: 0; height: 0;padding: 0; opacity: 0;" required>
                            <button type="button" class="btn btn-default"
                                    style="width: 298px;">${enter.name!}</button>
                            <button type="button" class="btn btn-default dropdown-toggle"
                                    data-toggle="dropdown"
                                    aria-haspopup="true" aria-expanded="false">
                                <span class="caret"></span>
                                <span class="sr-only">Toggle Dropdown</span>
                            </button>
                            <ul class="dropdown-menu">
                            <#if enterprises?? && enterprises?size!=0>
                                <#list enterprises as e>
                                    <li data-value="${e.id!}"><a href="#">${e.name!}</a></li>
                                </#list>
                            <#else>
                                <li data-value=""><a href="#">没有可选的企业</a></li>
                            </#if>
                            </ul>
                        </div>
                    </div>
                    <#if showOrderList??&&showOrderList==1>
						<div class="form-group">
                            <label style="font-weight:700 ">订购组：</label>

                            <div class="btn-group btn-group-sm">
                                <input style="width: 0; font-size: 0; height: 0; padding: 0; opacity: 0"
                                       class="form-control searchItem"
                                       name="orderId" id="orderId" value="">
                                <button type="button" class="btn btn-default" style="width: 299px">
                                    &nbsp;</button>
                                <button type="button" class="btn btn-default dropdown-toggle"
                                        data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="false">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu">
                                <#if orderList?? && orderList?size!=0>
                                    <#list orderList as e>
                                        <li data-value="${e.id!}"><a href="#">${e.orderName!}</a></li>
                                    </#list>
                                <#else>
                                    <li data-value=""><a href="#">没有可选的订购组</a></li>
                                </#if>
                                </ul>
                            </div>
                        </div>
					</#if>
                    <div class="form-group">
                        <label>活动名称：</label>
                        <input type="text" name="name" id="name" value="${activities.name!}"
                               maxlength="64"
                               required data-bind="value: activeName, valueUpdate: 'afterkeydown'">
                        <div class="promote">活动名称支持汉字、英文字符、数字、特殊字符，长度不超过64个字符</div>
                    </div>

                    <div class="form-group">
                        <label>红包描述：</label>
                        <textarea rows="10" cols="100" maxlength="255" id="description"
                                  name="description"
                                  class="hasPrompt" placeholder="红包描述说明……" required
                                  style="vertical-align: top">${activityTemplate.description!}</textarea>
                        <div class="promote">活动描述长度不超过255个字符</div>
                    </div>

                    <div class="form-group">
                        <label>活动对象：</label>
                        <textarea rows="10" cols="100" maxlength="255" id="object" name="object"
                                  class="hasPrompt"
                                  placeholder="活动对象说明……" required
                                  style="vertical-align: top">${activityTemplate.object!}</textarea>
                        <div class="promote">活动对象描述长度不超过255个字符,需要换行的字符之间用"|"间隔</div>
                    </div>


                <#if cqFlag??&&cqFlag==0>
                    <div class="form-group">
                        <label>企业余额：</label>
                        <span class="ml-5" id="balance"></span>
                    </div>
                </#if>
                    <!--<div class="form-group">-->
                    <!--<label></label>-->
                    <!--<div class="file-box" style="display: inline-block">-->
                    <!--<input type="hidden" name="blackWhiteList" id="blackWhiteList">-->
                    <!--<a class="btn btn-sm btn-success">设置黑白名单</a>-->
                    <!--<input type="file" class="file-helper">-->
                    <!--</div>-->
                    <!--</div>-->
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <label>是否使用微信鉴权：</label>
                        <span class="input-checkbox">
                            <input type="radio" value="0" name="userType" id="nouseWechat"
                                   <#if activityTemplate.userType??&&activityTemplate.userType==0>checked="checked"</#if>
                                   class="hidden">
                            <label for="nouseWechat" class="c-checkbox mr-15 rt-1"></label>
                            <span>否</span>
                        </span>
                        <span class="input-checkbox ml-30">
                            <input type="radio" value="1" name="userType" id="useWechat"
                                   <#if activityTemplate.userType??&&activityTemplate.userType==1>checked="checked"</#if>
                                   class="hidden">
                            <label for="useWechat" class="c-checkbox mr-15 rt-1"></label>
                            <span>是</span>
                        </span>
                    </div>
                    <div class="form-group">
                        <label>活动时间：</label>
                        <span id="activeTime" style="display: inline-block">
                            <input type="text" name="startTime" id="startTime"
                                   value="${(activities.startTime?date)!}">&nbsp;至&nbsp;
                            <input type="text" id="endTime" name="endTime"
                                   value="${(activities.endTime?date)!}" required>
                        </span>
                    </div>
                    <div class="form-group">
                        <label>活动规则：</label>
                        <textarea rows="10" cols="100" maxlength="255" id="rules" name="rules"
                                  class="hasPrompt" placeholder="活动规则说明……" required
                                  style="vertical-align: top">${activityTemplate.rules!}</textarea>
                        <div class="promote">活动规则长度不超过255个字符,需要换行的字符之间用"|"间隔</div>
                    </div>

                </div>
            </form>

        </div>
    </div>

    <div class="tile mt-30">

        <div class="tile-content">
            <div>
                <a class="btn btn-sm btn-success dialog-btn" data-toggle="modal"
                   data-target="" id="manualSet" onclick="manualSet()">手动设置</a>
                <span></span>

                <a class="btn btn-sm btn-success dialog-btn" data-toggle="modal"
                   data-target="" id="randomSet" onclick="randomSet()">随机设置</a>
            </div>
        </div>

        <div class="tile-content manual-set" style="display:none;">
            <form id="setting-form">
                <div class="row form text-center">
                    <div class="form-group form-group-sm mt-10 col-md-6">
                        <label>抽奖类型:</label>
                        <span class="right-wrap">
                            <div class="btn-group btn-group-sm">
                                <input name="daily" id="daily" <#if activities??&&activities.type==2>value="${activityTemplate.daily!}"<#else>value=""</#if>
                                       style="width: 0; height: 0;padding: 0; opacity: 0;">
                                <button type="button" class="btn btn-default" style="width: 298px;">
                                <#if activities??&&activities.type==2&&activityTemplate.daily??&&activityTemplate.daily==0>
                                    活动期间游戏次数每天重置为0</#if>
                                <#if activities??&&activities.type==2&&activityTemplate.daily??&&activityTemplate.daily==1>
                                    活动期间游戏次数不重置</#if>
                                <#if activities??&&activities.type!=2>请选择</#if>
                                </button>
                                <button type="button" class="btn btn-default dropdown-toggle"
                                        data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="false">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu dailyList">
                                    <li data-value="0"><a href="#">活动期间游戏次数每天重置为0</a></li>
                                    <li data-value="1"><a href="#">活动期间游戏次数不重置</a></li>
                                </ul>
                            </div>
                        </span>
                    </div>
                    <div class="form-group form-group-sm mt-10 col-md-6">
                        <label>抽奖次数:</label>
                        <span class="right-wrap">
                            <input class="form-control mobileOnly" name="maxPlayNumber"
                                   id="maxPlayNumber" <#if activities?? && activities.type==2>value="${activityTemplate.maxPlayNumber!}"
                                   <#else>value=""</#if>
                                   style="width: 324px;">
                            <div class="promote">
                                请输入1-10之间的正整数
                            </div>
                        </span>
                    </div>
                </div>
            </form>
            <div class="table-responsive mt-10 text-center">
                <table class="table table-bordered" id="awardsTable">
                    <thead>
                    <tr>
                        <td>奖项</td>
                        <td>运营商</td>
                        <td>产品名称</td>
                        <td>产品编码</td>
                        <td>流量包大小</td>

                        <td <#if !(cqFlag??&&cqFlag==0)>hidden</#if>>售出价格</td>

                        <td>使用范围</td>
                        <td>漫游范围</td>
                        <td>数量</td>
                        <td>操作</td>
                    </tr>
                    </thead>
                    <tbody>

                    </tbody>
                </table>
            </div>
            <div class="text-left">
                <a class="btn btn-sm btn-success" id="btn-add"><i class="fa fa-plus mr-5"></i>新增</a>
            </div>
        </div>


        <div class="tile-content random-set" style="display:none;">
            <form id="random-set-form">
                <div class="form-group">
                    <label>产品名称：</label>
                    <div class="btn-group btn-group-sm flowPool-select">
                        <input name="flowPoolProductId" id="flowPoolProductId" <#if realProductId??>value="${realProductId!}"<#else>value=""</#if>
                               style="width: 0; height: 0;padding: 0; opacity: 0;" required>
                        <button type="button" class="btn btn-default" style="width: 298px;">
                            <#if realProductName??>${realProductName!}<#else>请选择</#if>
                        </button>
                        <button type="button" class="btn btn-default dropdown-toggle"
                                data-toggle="dropdown"
                                aria-haspopup="true" aria-expanded="false">
                            <span class="caret"></span>
                            <span class="sr-only">Toggle Dropdown</span>
                        </button>
                        <ul class="dropdown-menu">
                        </ul>
                    </div>
                </div>

                <div class="form form-group">
                    <label>产品大小：</label>
                    <input type="text" name="flowPoolTotalProductSize" maxlength="8"  class="mobileOnly" <#if activityPrize??>value="${activityPrize.productSize/1024!}"<#else>value=""</#if>
                           style="width: 298px;" data-bind="value: flowPoolTotalProductSize, valueUpdate: 'afterkeydown'" required>&nbsp;MB
                </div>

                <div class="form form-group">
                    <label>数&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;量：</label>
                    <input type="text" name="flowPoolPrizeCount" maxlength="8"  class="mobileOnly" <#if activityPrize??>value="${activityPrize.count!}"<#else>value=""</#if>
                           style="width: 298px;" data-bind="value: flowPoolPrizeCount, valueUpdate: 'afterkeydown'" required>
                </div>
            </form>
        </div>
    </div>

    <div class="mt-30 text-center save-set1" style="display:none">
        <a class="btn btn-warning btn-sm mr-10 dialog-btn" id="save-btn" style="width: 114px;">保存</a>
    </div>

    <div class="mt-30 text-center save-set2" style="display:none">
        <a class="btn btn-warning btn-sm mr-10 dialog-btn" id="save-btn2" style="width: 114px;">保存</a>
    </div>

</div>

<div class="modal fade dialog-lg" id="products-dialog" data-backdrop="static">
    <div class="modal-dialog" style="width: 800px;">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title text-center">选择产品</h5>
            </div>
            <div class="modal-body" style="padding: 40px 10px;">
                <input type="hidden" class="searchItem" name="searchType">
                <input type="hidden" class="searchItem" name="enterpriseId" id="enterpriseId">
                <a id="search-btn" class="hidden"></a>
                <div id="product-table" role="table"
                     style="max-height: 200px; overflow: auto;"></div>
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
                <button class="btn btn-warning btn-sm" data-dismiss="modal">确 定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<div class="modal fade dialog-sm" id="sure-dialog" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content">保存成功!</span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" id="sure-btn">确 定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<div class="modal fade dialog-sm" id="auth-dialog" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content" id="auth">活动保存成功,请点击按钮授权。</span>
            </div>
            <div class="modal-footer">
                <a class="btn btn-warning btn-sm text-center" id="auth-btn">一键授权</a>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>


<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>

<script>
    var activityId = "${activities.activityId!}";
    var gameType = "${activities.type!}"

    var ispFormat = function (value, column, row) {
        if (row.isp == "M") {
            return "移动"
        }
        if (row.isp == "U") {
            return "联通"
        }
        if (row.isp == "T") {
            return "电信"
        }
        if (row.isp == "A") {
            return "三网"
        }
        return "";
    }

    var productSizeFormat = function (value, column, row) {
        //新疆：流量池产品不显示价格
        if (row.type == 1) {
            return "-";
        }
        if (row.productSize == null) {
            return "-";
        }
        if (row.productSize < 1024) {
            return row.productSize + "KB";
        }
        if (row.productSize >= 1024 && row.productSize < 1024 * 1024) {
            return (row.productSize * 1.0 / 1024) + "MB";
        }
        if (row.productSize >= 1024 * 1024) {
            return (row.productSize * 1.0 / 1024 / 1024) + "GB";
        }

        return row.productSize * 1.0 / 1024 + "MB";
    }

    var priceFormat = function (value, column, row) {
        //新疆：流量池产品不显示价格
        if (row.type == 1) {
            return "-";
        }
        var price = row.price;
        return (price / 100.0).toFixed(2) + "元";
    }

    var MAXAWARDNUM = 6;
    var awardsText = ["一等奖", "二等奖", "三等奖", "四等奖", "五等奖", "六等奖"];
    var action = "${contextPath}/manage/redpacket/searchProduct.html?${_csrf.parameterName}=${_csrf.token}";
    var tableData = {};
    var columns = [
        {name: "isp", text: "运营商", format: ispFormat},
        {name: "name", text: "产品名称", tip: true},
        {name: "productCode", text: "产品编码", tip: true},
        {name: "productSize", text: "流量包大小", format: productSizeFormat},
        {name: "price", text: "售出价格", format: priceFormat},
        {name: "ownershipRegion", text: "使用范围"},
        {name: "roamingRegion", text: "漫游范围"},
        {
            name: "op", text: "操作", format: function (value, column, row) {
            tableData[row.id] = row;
            return '<a class="btn btn-sm btn-success btn-choice" data-id="' + row.id + '">选择</a>';
        }
        }
    ];
    require(["moment", "common", "bootstrap", "daterangepicker", "page/list"], function (mm) {
        window.moment = mm;
        //初始化日期组件
        initDateRangePicker2();

        initFormValidate();

        listeners();

        $(".dropdown-menu li.active a").click();

        initActivityInfo();

        initEnterpriseSelect();

        init();

        initGetBalance();

    });

    function initActivityInfo(){
        var activityType = ${activities.type!};
        if(activityType == 2){
            //普通红包
            //渲染奖品信息
            initParams();
        }
        if(activityType == 8){
            //随机红包（拼手气红包）
            initLuckyParams();
        }

    }

    function initDailySelect() {
        var parent = $(".daily-select");
        var val = $("input", parent).val();
        $("li[data-value='" + val + "']", parent).children("a").click();
    }

    function initGetBalance() {
        var entId = "${enter.id!}";
        $("#enterpriseId").val(entId);
        $("#entId").val(entId);
        getBalance(entId);
    }

    function init() {
        $(".enterprise-select a").on("click", function () {
            var entId = $(this).parent().attr("data-value");
            $("#enterpriseId").val(entId);
            getBalance(entId);
        });

        $(".daily-select a").on("click", function () {
            var daily = $(this).parent().attr("data-value");
            $("#daily").val(daily);
        });

		//非微信授权活动,保存成功成功
        $("#sure-btn").on("click", function () {
            window.location.href = "${contextPath}/manage/redpacket/index.html";
        });
        
        $("#auth-btn").on("click", function () {
        	var activityId = $("#auth").val();
    		//点击授权
    		window.open("${authorizeUrl}" + activityId);
    		//不知道会不会执行这一句
    		window.location.href = "${contextPath}/manage/redpacket/index.html";
        });
    }
    
    function clearManualItems(){
    	var trs = $("#awardsTable tbody tr");
    	if(trs.length > 0){
    		trs.each(function(){
    			$(this).remove();
    		});
    	}
    	$("a",".dailyList li[data-value='0']").click();	
    }

    //点击随机红包
    function randomSet(){
        var entId = $("#entId").val();
        getFlowPoolProducts(entId);

        var flowPoolTotalProductSize = "${prizeSize!}";
        var flowPoolPrizeCount = "${prizeCount!}";
        var activityType = "${activities.type!}";

        $("#randomSet").addClass("btn-visited").removeClass("btn-success");
        $("#manualSet").removeClass("btn-visited").addClass("btn-success");

        $("input[name='daily']").val("");
    	$("input[name='maxPlayNumber']").val("");
        	
    	//删除奖品
		clearManualItems();
        
        //getFlowPoolProducts();

        $(".save-set1").hide();
        $(".manual-set").hide();
        $(".random-set").show();
        $(".save-set2").show();
    }

    function manualSet(){
        $("#randomSet").removeClass("btn-visited").addClass("btn-success");

    	$("input[name='flowPoolTotalProductSize']").val("");
    	$("input[name='flowPoolPrizeCount']").val("");
        

        $(".random-set").hide();
        $(".save-set1").hide();
        $(".save-set2").hide();
//        $("#setting-dialog").modal("show");
        $(".manual-set").show();
        $(".save-set1").show();
        $("#manualSet").addClass("btn-visited").removeClass("btn-success");
    }

    function getFlowPoolProducts(entId){
        $.ajax({
            type: "POST",
            data: {
                entId: entId
            },
            url: "${contextPath}/manage/redpacket/getFlowPoolProducts.html?${_csrf.parameterName}=${_csrf.token}",
            dataType: "json", //指定服务器的数据返回类型，
            success: function (res) {
                if (res.result && res.result == "true") {
                    setFlowPoolSelect(res.flowPoolProducts);
                    var productId= "${realProductId!}";
                    if(productId && productId != ""){
                        $("a",".flowPool-select ul li[data-value="+productId+"]").click();
                    }
                }
                else {
                    var parent = $(".flowpool-select ul");
                    parent.empty();
                    $("input", parent.parent()).val("");
                    $(".btn", parent.parent()).eq(0).html("请选择");
                    parent.append('<li data-value=""><a href="javascript:void(0)">没有可以选择的流量池产品</a></li>');
                }
            }
        });
    }

    function setFlowPoolSelect(products){
        var parent = $(".flowPool-select ul");
        parent.empty();
        $("input", parent.parent()).val("");
        $(".btn", parent.parent()).eq(0).html("请选择");
        if(products && products.length>0){
            for(var i=0; i< products.length; i++){
                parent.append('<li data-value="'+products[i].id+'"><a href="javascript:void(0)">'+products[i].name+'</a></li>');
            }
        }
        else{
            parent.append('<li data-value=""><a href="javascript:void(0)">没有可以选择的流量池产品</a></li>');
        }
    }

    /**
     * 企业名称选择
     * */
    function enterpriseChoiceListener(ele, userType) {
        var entId = ele.val();
        if (entId == "") {
            $("#authorize").hide();
        }
        if (userType == "1") {
            //$("#authorize").show();
            isAuthorized(entId);
        }
        else {
            $("#authorize").hide();
        }
    }

    /**
     * 判断企业是否授权
     */
    function isAuthorized(activityId) {
        $.ajax({
           beforeSend: function (request) {
               var token1 = $(window.parent.document).find("meta[name=_csrf]")
                       .attr("content");
               var header1 = $(window.parent.document).find("meta[name=_csrf_header]")
                       .attr("content");
               request.setRequestHeader(header1, token1);
           },
           url: "${contextPath}/manage/redpacket/isAuthorized.html",
           type: "POST",
           dataType: "JSON",
           data: {activityId: activityId}
        }).then(function (data) {
            var result = data.result;
            if (result == "true") {
                //已经授权过,直接返回到列表页
                $("#sure-dialog").modal("show");
            } else {
                $("#auth-dialog").modal("show");
            }
        });
    }

    function initEnterpriseSelect() {
        var parent = $(".enterprise-select");
        var val = $("input", parent).val();
        $("li[data-value='" + val + "']", parent).children("a").click();
    }

    //随机红包信息初始化
    function initLuckyParams(){
        //step1
        $("#randomSet").addClass("btn-visited").removeClass("btn-success");
        $("#manualSet").removeClass("btn-visited").addClass("btn-success");

        //step2
        var entId = ${enter.id!};
        getFlowPoolProducts(entId);

        $(".random-set").show();
        $(".save-set2").show();
    }

    //普通红包信息初始化
    function initParams() {
        $("#manualSet").addClass("btn-visited").removeClass("btn-success");
        $("#randomSet").removeClass("btn-visited").addClass("btn-success");

        var firstRankPrize = ${firstRankPrize!};
        if (firstRankPrize && firstRankPrize.count) {
            addAwardData({
                 "M": {
                     type: firstRankPrize.cmccType,
                     productId: firstRankPrize.cmccId,
                     name: firstRankPrize.cmccName,
                     productCode: firstRankPrize.cmccProductCode,
                     size: firstRankPrize.cmccProductSize,
                     <#if cqFlag??&&cqFlag==0>
                         price: firstRankPrize.cmccPrice,
                     </#if>
                     ownershipRegion: firstRankPrize.cmccOwnershipRegion,
                     roamingRegion: firstRankPrize.cmccRoamingRegion
                 },
                 "U": {
                     type: firstRankPrize.cuccType,
                     productId: firstRankPrize.cuccId,
                     name: firstRankPrize.cuccName,
                     productCode: firstRankPrize.cuccProductCode,
                     size: firstRankPrize.cuccProductSize,
                     <#if cqFlag??&&cqFlag==0>
                         price: firstRankPrize.cuccPrice,
                     </#if>
                     ownershipRegion: firstRankPrize.cuccOwnershipRegion,
                     roamingRegion: firstRankPrize.cuccRoamingRegion
                 },
                 "T": {
                     type: firstRankPrize.ctccType,
                     productId: firstRankPrize.ctccId,
                     name: firstRankPrize.ctccName,
                     productCode: firstRankPrize.ctccProductCode,
                     size: firstRankPrize.ctccProductSize,
                     <#if cqFlag??&&cqFlag==0>
                         price: firstRankPrize.ctccPrice,
                     </#if>
                     ownershipRegion: firstRankPrize.ctccOwnershipRegion,
                     roamingRegion: firstRankPrize.ctccRoamingRegion
                 },
                 count: firstRankPrize.count,
                 probability: firstRankPrize.probability * 100
             });
        }

        var secondRankPrize = ${secondRankPrize!};
        if (secondRankPrize && secondRankPrize.count) {
            addAwardData({
                 "M": {
                     type: secondRankPrize.cmccType,
                     productId: secondRankPrize.cmccId,
                     name: secondRankPrize.cmccName,
                     productCode: secondRankPrize.cmccProductCode,
                     size: secondRankPrize.cmccProductSize,
                     <#if cqFlag??&&cqFlag==0>
                         price: secondRankPrize.cmccPrice,
                     </#if>
                     ownershipRegion: secondRankPrize.cmccOwnershipRegion,
                     roamingRegion: secondRankPrize.cmccRoamingRegion
                 },
                 "U": {
                     type: secondRankPrize.cuccType,
                     productId: secondRankPrize.cuccId,
                     name: secondRankPrize.cuccName,
                     productCode: secondRankPrize.cuccProductCode,
                     size: secondRankPrize.cuccProductSize,
                     <#if cqFlag??&&cqFlag==0>
                         price: secondRankPrize.cuccPrice,
                     </#if>
                     ownershipRegion: secondRankPrize.cuccOwnershipRegion,
                     roamingRegion: secondRankPrize.cuccRoamingRegion
                 },
                 "T": {
                     type: secondRankPrize.ctccType,
                     productId: secondRankPrize.ctccId,
                     name: secondRankPrize.ctccName,
                     productCode: secondRankPrize.ctccProductCode,
                     size: secondRankPrize.ctccProductSize,
                     <#if cqFlag??&&cqFlag==0>
                         price: secondRankPrize.ctccPrice,
                     </#if>
                     ownershipRegion: secondRankPrize.ctccOwnershipRegion,
                     roamingRegion: secondRankPrize.ctccRoamingRegion
                 },
                 count: secondRankPrize.count,
                 probability: secondRankPrize.probability * 100
             });
        }

        var thirdRankPrize = ${thirdRankPrize!};
        if (thirdRankPrize && thirdRankPrize.count) {
            addAwardData({
                 "M": {
                     type: thirdRankPrize.cmccType,
                     productId: thirdRankPrize.cmccId,
                     name: thirdRankPrize.cmccName,
                     productCode: thirdRankPrize.cmccProductCode,
                     size: thirdRankPrize.cmccProductSize,
                     <#if cqFlag??&&cqFlag==0>
                         price: thirdRankPrize.cmccPrice,
                     </#if>
                     ownershipRegion: thirdRankPrize.cmccOwnershipRegion,
                     roamingRegion: thirdRankPrize.cmccRoamingRegion
                 },
                 "U": {
                     type: thirdRankPrize.cuccType,
                     productId: thirdRankPrize.cuccId,
                     name: thirdRankPrize.cuccName,
                     productCode: thirdRankPrize.cuccProductCode,
                     size: thirdRankPrize.cuccProductSize,
                     <#if cqFlag??&&cqFlag==0>
                         price: thirdRankPrize.cuccPrice,
                     </#if>
                     ownershipRegion: thirdRankPrize.cuccOwnershipRegion,
                     roamingRegion: thirdRankPrize.cuccRoamingRegion
                 },
                 "T": {
                     type: thirdRankPrize.ctccType,
                     productId: thirdRankPrize.ctccId,
                     name: thirdRankPrize.ctccName,
                     productCode: thirdRankPrize.ctccProductCode,
                     size: thirdRankPrize.ctccProductSize,
                     <#if cqFlag??&&cqFlag==0>
                         price: thirdRankPrize.ctccPrice,
                     </#if>
                     ownershipRegion: thirdRankPrize.ctccOwnershipRegion,
                     roamingRegion: thirdRankPrize.ctccRoamingRegion
                 },
                 count: thirdRankPrize.count,
                 probability: thirdRankPrize.probability * 100
             });
        }

        var forthRankPrize = ${forthRankPrize!};
        if (forthRankPrize && forthRankPrize.count) {
            addAwardData({
                 "M": {
                     type: forthRankPrize.cmccType,
                     productId: forthRankPrize.cmccId,
                     name: forthRankPrize.cmccName,
                     productCode: forthRankPrize.cmccProductCode,
                     size: forthRankPrize.cmccProductSize,
                 <#if cqFlag??&&cqFlag==0>
                     price: forthRankPrize.cmccPrice,
                 </#if>
                     ownershipRegion: forthRankPrize.cmccOwnershipRegion,
                     roamingRegion: forthRankPrize.cmccRoamingRegion
                 },
                 "U": {
                     type: forthRankPrize.cuccType,
                     productId: forthRankPrize.cuccId,
                     name: forthRankPrize.cuccName,
                     productCode: forthRankPrize.cuccProductCode,
                     size: forthRankPrize.cuccProductSize,
                 <#if cqFlag??&&cqFlag==0>
                     price: forthRankPrize.cuccPrice,
                 </#if>
                     ownershipRegion: forthRankPrize.cuccOwnershipRegion,
                     roamingRegion: forthRankPrize.cuccRoamingRegion
                 },
                 "T": {
                     type: forthRankPrize.ctccType,
                     productId: forthRankPrize.ctccId,
                     name: forthRankPrize.ctccName,
                     productCode: forthRankPrize.ctccProductCode,
                     size: forthRankPrize.ctccProductSize,
                 <#if cqFlag??&&cqFlag==0>
                     price: forthRankPrize.ctccPrice,
                 </#if>
                     ownershipRegion: forthRankPrize.ctccOwnershipRegion,
                     roamingRegion: forthRankPrize.ctccRoamingRegion
                 },
                 count: forthRankPrize.count,
                 probability: forthRankPrize.probability * 100
             });
        }

        var fiveRankPrize = ${fiveRankPrize!};
        if (fiveRankPrize && fiveRankPrize.count) {
            addAwardData({
                 "M": {
                     type: fiveRankPrize.cmccType,
                     productId: fiveRankPrize.cmccId,
                     name: fiveRankPrize.cmccName,
                     productCode: fiveRankPrize.cmccProductCode,
                     size: fiveRankPrize.cmccProductSize,
                 <#if cqFlag??&&cqFlag==0>
                     price: fiveRankPrize.cmccPrice,
                 </#if>
                     ownershipRegion: fiveRankPrize.cmccOwnershipRegion,
                     roamingRegion: fiveRankPrize.cmccRoamingRegion
                 },
                 "U": {
                     type: fiveRankPrize.cuccType,
                     productId: fiveRankPrize.cuccId,
                     name: fiveRankPrize.cuccName,
                     productCode: fiveRankPrize.cuccProductCode,
                     size: fiveRankPrize.cuccProductSize,
                 <#if cqFlag??&&cqFlag==0>
                     price: fiveRankPrize.cuccPrice,
                 </#if>
                     ownershipRegion: fiveRankPrize.cuccOwnershipRegion,
                     roamingRegion: fiveRankPrize.cuccRoamingRegion
                 },
                 "T": {
                     type: fiveRankPrize.ctccType,
                     productId: fiveRankPrize.ctccId,
                     name: fiveRankPrize.ctccName,
                     productCode: fiveRankPrize.ctccProductCode,
                     size: fiveRankPrize.ctccProductSize,
                 <#if cqFlag??&&cqFlag==0>
                     price: fiveRankPrize.ctccPrice,
                 </#if>
                     ownershipRegion: fiveRankPrize.ctccOwnershipRegion,
                     roamingRegion: fiveRankPrize.ctccRoamingRegion
                 },
                 count: fiveRankPrize.count,
                 probability: fiveRankPrize.probability * 100
             });
        }

        var sixRankPrize = ${sixRankPrize!};
        if (sixRankPrize && sixRankPrize.count) {
            addAwardData({
                 "M": {
                     type: sixRankPrize.cmccType,
                     productId: sixRankPrize.cmccId,
                     name: sixRankPrize.cmccName,
                     productCode: sixRankPrize.cmccProductCode,
                     size: sixRankPrize.cmccProductSize,
                 <#if cqFlag??&&cqFlag==0>
                     price: sixRankPrize.cmccPrice,
                 </#if>
                     ownershipRegion: sixRankPrize.cmccOwnershipRegion,
                     roamingRegion: sixRankPrize.cmccRoamingRegion
                 },
                 "U": {
                     type: sixRankPrize.cuccType,
                     productId: sixRankPrize.cuccId,
                     name: sixRankPrize.cuccName,
                     productCode: sixRankPrize.cuccProductCode,
                     size: sixRankPrize.cuccProductSize,
                 <#if cqFlag??&&cqFlag==0>
                     price: sixRankPrize.cuccPrice,
                 </#if>
                     ownershipRegion: sixRankPrize.cuccOwnershipRegion,
                     roamingRegion: sixRankPrize.cuccRoamingRegion
                 },
                 "T": {
                     type: sixRankPrize.ctccType,
                     productId: sixRankPrize.ctccId,
                     name: sixRankPrize.ctccName,
                     productCode: sixRankPrize.ctccProductCode,
                     size: sixRankPrize.ctccProductSize,
                 <#if cqFlag??&&cqFlag==0>
                     price: sixRankPrize.ctccPrice,
                 </#if>
                     ownershipRegion: sixRankPrize.ctccOwnershipRegion,
                     roamingRegion: sixRankPrize.ctccRoamingRegion
                 },
                 count: sixRankPrize.count,
                 probability: sixRankPrize.probability * 100
             });
        }

        //这里怎么实现数组
    <#--
    var autoPrizesPojos = ${autoPrizesPojos!};
    var params = [];
    if(autoPrizesPojos && autoPrizesPojos.length>0){
        for(var i = 0; i < autoPrizesPojos.length; i++){
            params.push({
                "M": {
                    productId: autoPrizesPojos[i].cmccId,
                    name: autoPrizesPojos[i].cmccName,
                    productCode: autoPrizesPojos[i].cmccProductCode,
                    size: autoPrizesPojos[i].cmccProductSize,
                    price: autoPrizesPojos[i].cmccPrice,
                    ownershipRegion: autoPrizesPojos[i].cmccOwnershipRegion,
                    roamingRegion: autoPrizesPojos[i].cmccRoamingRegion
                },
                "U": {
                    productId: autoPrizesPojos[i].cuccId,
                    name: autoPrizesPojos[i].cuccName,
                    productCode: autoPrizesPojos[i].cuccProductCode,
                    size: autoPrizesPojos[i].cuccProductSize,
                    price: autoPrizesPojos[i].cuccPrice,
                    ownershipRegion: autoPrizesPojos[i].cuccOwnershipRegion,
                    roamingRegion: autoPrizesPojos[i].cuccRoamingRegion
                },
                "T": {
                    productId: autoPrizesPojos[i].ctccId,
                    name: autoPrizesPojos[i].ctccName,
                    productCode: autoPrizesPojos[i].ctccProductCode,
                    size: autoPrizesPojos[i].ctccProductSize,
                    price: autoPrizesPojos[i].ctccPrice,
                    ownershipRegion: autoPrizesPojos[i].ctccOwnershipRegion,
                    roamingRegion: autoPrizesPojos[i].ctccRoamingRegion
                },
                count: autoPrizesPojos[i].count
                    });
        }
        addAwardData(params);
    }
    -->
        $(".manual-set").show();
        $(".save-set1").show();
    }
    
    

    function listeners() {
        $("#btn-add").on("click", function () {

            var allowAllPlatformProduct = false;
        <#if allowAllPlatformProduct??  && allowAllPlatformProduct>
            allowAllPlatformProduct = true;
        </#if>

            if (allowAllPlatformProduct) {
                addAward();
            } else {
                addAward1();
            }
        });

        $("#awardsTable").on("click", ".btn-delete", function () {
            clearProduct($(this));
        });

//        $("#save-setting-btn").click(function () {
//            if ($("#setting-form").validate().form()) {
//                $("#setting-dialog").modal("hide");
//                $(".manual-set").show();
//                $(".save-set1").show();
//            }
//            $("#manualSet").addClass("btn-visited").removeClass("btn-success");
//        });

        $("#awardsTable").on("click", ".btn-yy", function () {
            var type = $(this).data("type");
            var tr = $(this).parent().parent();
            $("#products-dialog").data("tr", tr);
            $("input[name='searchType']").val(type);
            $("#search-btn").click();
            $("#products-dialog").modal("show");
        });

        $("#product-table").on("click", ".btn-choice", function () {
            var id = $(this).data("id");
            var row = tableData[id];
            appendToYY(row);
            $("#products-dialog").modal("hide");
        });

        $("#save-btn").on("click", function () {
            saveData();
        });

        $("#save-btn2").on("click", function () {
            saveData2();
        });

        initDailySelect();
    }

    //获取企业余额
    function getBalance(entId) {
        var entId = entId;
        if (entId == "") {
            $("#balance").html("");
            return;
        }

        $.ajax({
           type: "POST",
           data: {
               entId: entId
           },
           url: "${contextPath}/manage/enterprise/getBalanceAjax.html?${_csrf.parameterName}=${_csrf.token}",
           dataType: "json", //指定服务器的数据返回类型，
           success: function (res) {
               if (res.success && res.success == "success" && res.balance) {
                   $("#balance").html(res.balance);
               }
               else {
                   $("#balance").html("0元");
               }
           }
       });
    }

    //验证数字
    function validateNum(num) {
        var a = /^[0-9]*$/;
        if (!num.match(a) || num < 1) {
            return false;
        }
        return true;
    }

    //验证奖品数量，为数字并且大于0
    function validatePrizeNum(num) {
        var a = /^([1-9][0-9]*)$/;
        if (!num.match(a) || num < 1 || num > 99999999) {
            return false;
        }
        return true;
    }

    //随机设置保存
    function saveData2(){
        if ($("#dataForm").validate().form()) {
            if ($("#random-set-form").validate().form()) {
                var flowPoolPrizeCount = $("input[name = 'flowPoolPrizeCount']").val();
                var flowPoolTotalProductSize = $("input[name='flowPoolTotalProductSize']").val();
                if (parseInt(flowPoolPrizeCount) > parseInt(flowPoolTotalProductSize)) {
                    showTipDialog("数量不能大于产品大小");
                    return;
                }

                //是否微信鉴权
                var userType = $("input[name='userType']:checked").val();
            <#--
            if ((userType == '1') && ($("#authorize").css("display") == 'inline-block')) {
                showTipDialog("请一键授权");
                return;
            }
            -->

                var activity = {
                    activityId: activityId,
                    name: $("input[name='activeName']").val(),
                    entId: $("#entId").val(),
                    startTime: $("#startTime").val(),
                    endTime: $("#endTime").val(),
                    type: 8
                };

                var userType = $("input[name='userType']:checked").val();
                var activityTemplate = {
                    activityId: activityId,
                    rules: $("#rules").val(),
                    object: $("#object").val(),
                    description: $("#description").val(),
                    userType: userType,
                    givedNumber: 1,
                    daily: 1,
                    maxPlayNumber: 1
                };

                var count = $("input[name='flowPoolPrizeCount']").val(); //红包数量
                var prizeSize = $("input[name='flowPoolTotalProductSize']").val() * 1024; //产品大小
                var totalProductSize = prizeSize;

                var activityInfo = {
                    activityId: activityId,
                    prizeCount: count,
                    userCount: count,
                    totalProductSize: totalProductSize,
                    price: 0,
                    givedUserCount: 0,
                    usedProductSize: 0
                };

                var activityPrizes = [];

                var item = {};
                item["idPrefix"] = 0;
                item["rankName"] = "一等奖";
                item["productId"] = $("#flowPoolProductId").val();
                item["productSize"] = prizeSize;
                item["size"] = prizeSize;
                item["prizeName"] = "M流量";
                item["enterpriseId"] = $("#entId").val();
                item["probability"] = 100;
                item["activityId"] = activityId;
                item["count"] = count;

                //存取产品类型
                item["type"] = 1;
                activityPrizes.push(item);

                $("#save-btn2").addClass("disabled");

                ajaxData("${contextPath}/manage/redpacket/saveEditAjax.html", {
                    activity: JSON.stringify(activity),
                    activityTemplate: JSON.stringify(activityTemplate),
                    activityPrizes: JSON.stringify(activityPrizes),
                    activityInfo: JSON.stringify(activityInfo)
                }, function (ret) {
                    if (ret && ret.success && ret.activityId) {
                        if (userType == 1) {
                            //判断是否授权
                            $("#auth").val(ret.activityId);
                            isAuthorized(ret.activityId);
                        } else {
                            $("#sure-dialog").modal("show");
                        }
                    }
                    else {
                        showTipDialog(ret.fail)
                    }

                    $("#save-btn2").removeClass("disabled");
                });
            }
        }
    }

    function saveData() {
        if ($("#dataForm").validate().form()) {
            if ($("#setting-form").validate().form()) {

                var end = moment($("#endTime").val());
                var now = moment(moment(new Date()).format("YYYY-MM-DD"));

                if (end.isBefore(now)) {
                    showTipDialog("活动结束时间至少选择今天");
                    return;
                }

                if ($("#awardsTable tbody tr").length == 0) {
                    showTipDialog("请添加奖项");
                    return;
                }

                var trs = null;

                var allowAllPlatformProduct = false;
            <#if allowAllPlatformProduct??  && allowAllPlatformProduct>
                allowAllPlatformProduct = true;
            </#if>

                if (allowAllPlatformProduct) {
                    trs = $("#awardsTable tbody tr:nth-child(3n-2)");
                } else {
                    trs = $("#awardsTable tbody tr");
                }

                var valid = true;
                trs.each(function () {
                    var key = $(this).data("key");
                    if (isEmpty(key)) {
                        showTipDialog("请选择" + $(this).children("td:first-child").html() + "奖项的产品");
                        valid = false;
                        return false;
                    }
                    if (!$(this).find(".count").val()) {
                        showTipDialog("请填写" + $(this).children("td:first-child").html() + "奖品数量");
                        valid = false;
                        return false;
                    }
                    if (!$(this).find(".count").val()) {
                        showTipDialog("请填写" + $(this).children("td:first-child").html() + "奖品数量");
                        valid = false;
                        return false;
                    }

                    if (!validatePrizeNum($(this).find(".count").val())) {
                        showTipDialog(
                                $(this).children("td:first-child").html() + "的奖品数量为1-99999999之间的正整数");
                        valid = false;
                        return false;
                    }

                <#--
                if(!$(this).find(".probability").val()){
                    showTipDialog("请填写"+$(this).children("td:first-child").html()+"奖品概率");
                    valid = false;
                    return false;
                }
                -->

                    var $that = $(this);
                    $("tr[data-key='" + key + "']").each(function () {
                        var sizeEle = $(this).find(".size");
                        //存在自定义流量池大小的输入框判断是否输入了值
                        if (sizeEle.length) {
                            if (!sizeEle.val()) {
                                showTipDialog(
                                        "请填写" + $that.children("td:first-child").html() + "流量包大小");
                                valid = false;
                                return false;
                            }
                            if (!validateNum(sizeEle.val())) {
                                showTipDialog(
                                        "请填写正确的" + $that.children("td:first-child").html() + "流量包大小");
                                valid = false;
                                return false;
                            }
                            if (parseInt(sizeEle.val()) <= 0 || parseInt(sizeEle.val()) > 11264) {
                                showTipDialog(
                                        "请填写正确的" + $that.children("td:first-child").html() + "流量包大小");
                                valid = false;
                                return false;
                            }
                        }
                    });
                });

                //校验手动设置内容的填写
                if ($("#rules").val() == '') {
                    showTipDialog("请填写活动规则");
                    valid = false;
                    return false;
                }

                if ($("#object").val() == '') {
                    showTipDialog("请填写活动对象");
                    valid = false;
                    return false;
                }

                if ($("#description").val() == '') {
                    showTipDialog("请填写红包描述");
                    valid = false;
                    return false;
                }

//                if ($("#daily").val() == '') {
//                    showTipDialog("请选择抽奖类型");
//                    valid = false;
//                    return false;
//                }
//                if ($("#maxPlayNumber").val() == '') {
//                    showTipDialog("请填写抽奖次数");
//                    valid = false;
//                    return false;
//                }
//                if (!validateNum($("#maxPlayNumber").val())) {
//                    showTipDialog("请填写正确的抽奖次数");
//                    valid = false;
//                    return false;
//                }


                if (valid) {
                    var activity = {
                        activityId: activityId,
                        name: $("#name").val(),
                        entId: $("#entId").val(),
                        startTime: $("#startTime").val(),
                        endTime: $("#endTime").val(),
                        type: 2
                    };
					var userType = $("input[name='userType']:checked").val();
                    var activityTemplate = {
                        rules: $("#rules").val(),
                        object: $("#object").val(),
                        description: $("#description").val(),
                        givedNumber: 1,
                        daily: $("#daily").val(),
                        maxPlayNumber: $("#maxPlayNumber").val(),
                        userType: userType
                    };

                    var activityInfo = {
                        prizeCount: 0,
                        userCount: 0,
                        totalProductSize: 0,
                        price: 0,
                        givedUserCount: 0,
                        usedProductSize: 0
                    };

                    var activityPrizes = [];

                    trs.each(function (id) {
                        var key = $(this).data("key");
                        var award_trs = $('tr[data-key="' + key + '"]');

                        var $that = $(this);
                        award_trs.each(function (index) {
                            var type = $(this).find(".btn-yy").data("type");
                            var isEmpty = $(this).data("empty");
                            if (!isEmpty) {
                                var item = {};

                                //获取产品信息
                                var product = $(this).data("product");
                                if (product.id) {

                                    item["idPrefix"] = id;
                                    if (id == 0) {
                                        item["rankName"] = "一等奖";
                                    }
                                    if (id == 1) {
                                        item["rankName"] = "二等奖";
                                    }
                                    if (id == 2) {
                                        item["rankName"] = "三等奖";
                                    }
                                    if (id == 3) {
                                        item["rankName"] = "四等奖";
                                    }
                                    if (id == 4) {
                                        item["rankName"] = "五等奖";
                                    }
                                    if (id == 5) {
                                        item["rankName"] = "六等奖";
                                    }

                                    item["productId"] = product.id;

                                    if (product.type == 2) {
                                        //流量包
                                        item["prizeName"] = sizeFun(product.productSize);
                                        item["productSize"] = product.productSize;
                                        item["size"] = product.productSize;  //存在奖品表里里，奖品的大小
                                    }
                                    else {
                                        item["prizeName"] = sizeFun($(this).find(".size").val() * 1024);

                                        //新疆：流量池产品，则需要获取对应输入框的产品大小
                                        var size = $(this).find(".size").val() * 1024;
                                        item["productSize"] = size;
                                        item["size"] = size;  //存在奖品表里里，奖品的大小
                                    }

                                    item["enterpriseId"] = $("#entId").val();

                                    item["probability"] = 100;
                                    item["count"] = $that.find(".count").val();

                                    //存取产品类型
                                    item["type"] = product.type;

                                    activityPrizes.push(item);

                                }
                            }
                        });
                    });

                    if (activityPrizes == '' || activityPrizes.length < 1) {
                        showTipDialog("请至少选择一个奖品!");
                        return false;
                    }

                    $("#save-btn").addClass("disabled");

                    ajaxData("${contextPath}/manage/redpacket/saveEditAjax.html", {
                        activity: JSON.stringify(activity),
                        activityTemplate: JSON.stringify(activityTemplate),
                        activityPrizes: JSON.stringify(activityPrizes),
                        activityInfo: JSON.stringify(activityInfo)
                    }, function (ret) {
                        if (ret && ret.success && ret.activityId) {
                            if(userType == 1){
                    		//判断是否授权
                    		$("#auth").val(ret.activityId);
                    		isAuthorized(ret.activityId);
	                    	}else{
	                    		$("#sure-dialog").modal("show");
	                    	}
                        }
                        else {
                            showTipDialog(ret.fail);
                        }

                        $("#save-btn").removeClass("disabled");
                    });
                }
            }
        }
    }

    function sizeFunButNoUnit(size) {
        if (size == null) {
            return "";
        }
        if (size < 1024) {
            return size;
        }
        if (size >= 1024 && size < 1024 * 1024) {
            return size * 1.0 / 1024;
        }
        if (size >= 1024 * 1024) {
            return size * 1.0 / 1024 / 1024;
        }

        return size * 1.0 / 1024;
    }

    function sizeFun(size) {
        if (size == null) {
            return "";
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

    function priceFun(price) {
        if (price == null) {
            return "";
        }
        return (price / 100.0).toFixed(2) + "元";
    }

    function appendToYY(data) {
        var tr = $("#products-dialog").data("tr");
        var tds = tr.children("td");
        if (tds.length == 10) {
            tds.eq(2).html(data["name"]);
            tds.eq(3).html(data["productCode"]);
            if (data["type"] == "1") {
                tds.eq(4).html('<input type="text" class="mobileOnly size">MB');
                tds.eq(5).html("-");
            } else {
                var size = sizeFun(data["productSize"]);
                tds.eq(4).html(size);

                var price = priceFun(data["price"]);
                tds.eq(5).html(price);
            }
            tds.eq(6).html(data["ownershipRegion"]);
            tds.eq(7).html(data["roamingRegion"]);
        } else {
            tds.eq(1).html(data["name"]);
            tds.eq(2).html(data["productCode"]);
            if (data["type"] == "1") {
                tds.eq(3).html('<input type="text" class="mobileOnly size">MB');
                tds.eq(4).html("-");
            } else {
                var size = sizeFun(data["productSize"]);
                tds.eq(3).html(size);

                var price = priceFun(data["price"]);
                tds.eq(4).html(price);
            }

            tds.eq(5).html(data["ownershipRegion"]);
            tds.eq(6).html(data["roamingRegion"]);
        }
        tr.data("product", data);
        tr.data("empty", false);
    }

    function clearProduct(ele) {
        var tr = ele.parent().parent();
        var key = tr.data("key");
        if (isEmpty(key)) {
            removeAward(key);
        } else {
            var tds = tr.children("td");
            if (tds.length == 10) {
                tr.children("td:lt(8):gt(1)").html("");
            } else {
                tr.children("td:lt(7):gt(0)").html("");
            }
            tr.data("empty", true);
        }
    }

    /**
     * 某个奖项是否都为空
     **/
    function isEmpty(key) {
        var isAllEmpty = true;
        var awards = $("tr[data-key='" + key + "']");
        awards.each(function () {
            if (!$(this).data("empty")) {
                isAllEmpty = false;
            }
        });

        return isAllEmpty;
    }

    function removeAward(key) {
        var awards = $("tr[data-key='" + key + "']");
        awards.remove();

        var allowAllPlatformProduct = false;
    <#if allowAllPlatformProduct??  && allowAllPlatformProduct>
        allowAllPlatformProduct = true;
    </#if>

        if (allowAllPlatformProduct) {
            $("#awardsTable tbody tr:nth-child(3n-2)").each(function (index) {
                $(this).children("td").eq(0).html(awardsText[index]);
            });
        } else {
            $("#awardsTable tbody tr").each(function (index) {
                $(this).children("td").eq(0).html(awardsText[index]);
            });
        }
    }

    function addAward1() {
        var parent = $("#awardsTable tbody");
        var index = parent.find("tr").length / 1;
        if (index == 6) {
            showTipDialog("不能超过六个奖项");
            return false;
        }

        var time = new Date().getTime();
        var trs = $('<tr data-key="' + time + '">' +
                    '<td rowspan="1" style="vertical-align: middle">' + awardsText[index] + '</td>'
                    +
                    '<td><a class="btn btn-sm btn-info btn-yy" data-type="M">移动</a></td>' +
                    getTds() +
                    '<td rowspan="1"  style="vertical-align: middle">' +
                    '<input class="count">' +
                    '</td>' +
                    '<td><a class="btn btn-sm btn-success btn-delete">删除</a></td>' +
                    '</tr>');
        trs.data("empty", true);
        parent.append(trs);
    }

    function getTds() {
    <#if cqFlag??&&cqFlag==0>
        return '<td></td><td></td><td></td><td></td><td></td><td></td>';
    <#else>
        return '<td></td><td></td><td></td><td hidden></td><td></td><td></td>';
    </#if>
    }

    function addAward() {
        var parent = $("#awardsTable tbody");
        var index = parent.find("tr").length / 3;
        if (index == 6) {
            showTipDialog("不能超过六个奖项");
            return false;
        }

        var time = new Date().getTime();
        var trs = $('<tr data-key="' + time + '">' +
                    '<td rowspan="3" style="vertical-align: middle">' + awardsText[index] + '</td>'
                    +
                    '<td><a class="btn btn-sm btn-info btn-yy" data-type="M">移动</a></td>' +
                    getTds() +
                    '<td rowspan="3"  style="vertical-align: middle">' +
                    '<input class="count">' +
                    '</td>' +
                    '<td><a class="btn btn-sm btn-success btn-delete">删除</a></td>' +
                    '</tr>' +
                    '<tr data-key="' + time + '">' +
                    '<td><a class="btn btn-sm btn-info btn-yy" data-type="U">联通</a></td>' +
                    getTds() +
                    '<td><a class="btn btn-sm btn-success btn-delete">删除</a></td>' +
                    '</tr>' +
                    '<tr data-key="' + time + '">' +
                    '<td><a class="btn btn-sm btn-info btn-yy" data-type="T">电信</a></td>' +
                    getTds() +
                    '<td><a class="btn btn-sm btn-success btn-delete">删除</a></td>' +
                    '</tr>');
        trs.data("empty", true);
        parent.append(trs);
    }

    function addAwardData(data) {
        var allowAllPlatformProduct = false;
    <#if allowAllPlatformProduct??  && allowAllPlatformProduct>
        allowAllPlatformProduct = true;
    </#if>

        var parent = $("#awardsTable tbody");

        var index = parent.find("tr").length;

    <#if allowAllPlatformProduct??  && allowAllPlatformProduct>
        index = parent.find("tr").length / 3;
    </#if>

        var time = parseInt(Math.random() * 100000000);
        var cmcc = appendRow(data, "M", index, time, true, allowAllPlatformProduct ? 3 : 1);
        var lt = appendRow(data, "U", index, time);
        var dx = appendRow(data, "T", index, time);

        parent.append(cmcc);

        if (allowAllPlatformProduct) {
            parent.append(lt);
            parent.append(dx);
        }
    }

    function appendRow(data, type, index, key, isFirst, rowSpan) {
        var hidden = '';
    <#if !(cqFlag??&&cqFlag==0)>
        hidden = ' hidden';
    </#if>

        var flowType = ${flowType!}; //1:流量包;2:流量池
        var record = data[type];
        var tr = $('<tr data-key="' + key + '"></tr>');
        if (isFirst) {
            tr.append(
                    '<td rowspan=' + rowSpan + 'style="vertical-align: middle">' + awardsText[index]
                    + '</td>');
        }
        var types = {"M": "移动", "U": "联通", "T": "电信"};
        var typeText = types[type];
        tr.append('<td><a class="btn btn-sm btn-info btn-yy" data-type="' + type + '">' + typeText
                  + '</a></td>');
        //产品名称
        tr.append('<td>' + (record["name"] || "") + '</td>');
        //产品编码
        tr.append('<td>' + (record["productCode"] || "") + '</td>');
        //流量包大小
        var flowType = record["type"];
        if (flowType && flowType == 1) {
            tr.append('<td><input type="text" class="size mobileOnly" value="' + sizeFunButNoUnit(
                              record["size"])                                    + '">MB</td>');
            tr.append('<td' + hidden +'>' + ("-") + '</td>');
        } else {
            //流量包
            var size = sizeFun(record["size"]);
            tr.append('<td>' + (size || "") + '</td>');

            //售出价格
            var price = priceFun(record["price"]);
            tr.append('<td' + hidden + '>' + (price || "") + '</td>');
        }

        //使用范围
        tr.append('<td>' + (record["ownershipRegion"] || "") + '</td>');
        //漫游范围
        tr.append('<td>' + (record["roamingRegion"] || "") + '</td>');
        //数量
        if (isFirst) {
            tr.append(
                    '<td rowspan=' + rowSpan
                    + ' style="vertical-align: middle"><input class="count mobileOnly" value="'
                    + (data["count"] || "") + '"></td>');
        }

        tr.append('<td><a class="btn btn-sm btn-success btn-delete">删除</a></td>');
        if (record) {
            tr.data("empty", false);
        } else {
            tr.data("empty", true);
        }

        record.productSize = record.size;
        record.id = record.productId;
        tr.data("product", record);
        return tr;
    }

    /**
     * 初始化日期选择器
     */
    function initDateRangePicker2() {

        var ele = $('#activeTime');

        var startEle = $('#startTime');
        var endEle = $('#endTime');
        ele.dateRangePicker({
                                startDate: new Date(),
                                separator: ' ~ ',
                                getValue: function () {
                                    if (startEle.val() && endEle.val()) {
                                        return startEle.val() + ' ~ ' + endEle.val();
                                    } else {
                                        return '';
                                    }
                                },
                                setValue: function (s, s1, s2) {
                                    startEle.val(s1);
                                    endEle.val(s2);
                                }
                            });
    }

    function initFormValidate() {
        $("#dataForm").validate({
            errorPlacement: function (error, element) {
                error.addClass("error-tip");
                element.closest(".form-group").append(error);
            },
            rules: {
                rules: {
                    required: true,
                    cmaxLength: 255,
                    remote: "${contextPath}/manage/sensitiveWords/checkSensitiveWords.html?name="
                            + $('#rules').val()
                },
                object: {
                    required: true,
                    cmaxLength: 255,
                    remote: "${contextPath}/manage/sensitiveWords/checkSensitiveWords.html?name="
                            + $('#object').val()
                },
                description: {
                    required: true,
                    cmaxLength: 255,
                    remote: "${contextPath}/manage/sensitiveWords/checkSensitiveWords.html?name="
                            + $('#description').val()
                },
                entName: {
                    required: true
                },
                flowType: {
                    required: true
                },
                name: {
                    required: true,
                    remote: "${contextPath}/manage/sensitiveWords/checkSensitiveWords.html?name="
                            + $('#name').val()
                },
                endTime: {
                    required: true
                }
            },
            errorElement: "span",
            messages: {
                rules: {
                    required: "请填写活动规则",
                    cmaxLength: "活动规则不允许超过255个字符",
                    remote: "文案中有不良信息，请删除后再次提交"
                },
                object: {
                    required: "请填写活动对象",
                    cmaxLength: "活动对象不允许超过255个字符",
                    remote: "文案中有不良信息，请删除后再次提交"
                },
                description: {
                    required: "请填写红包描述",
                    cmaxLength: "红包描述不允许超过255个字符",
                    remote: "文案中有不良信息，请删除后再次提交"
                },
                entName: {
                    required: "请选择企业"
                },
                flowType: {
                    required: "请选择流量类型"
                },
                name: {
                    required: "请输入活动名称",
                    remote: "文案中有不良信息，请删除后再次提交"
                },
                endTime: {
                    required: "请选择活动时间"
                }
            }
        });

        $("#setting-form").validate({
            errorPlacement: function (error, element) {
                error.addClass("error-tip-setting");
                element.closest(".form-group").find(".right-wrap").append(error);
            },
            rules: {
                maxPlayNumber: {
                    required: true,
                    positive: true,
                    max: 10,
                    min: 1
                },
                daily: {
                    required: true
                }
            },
            errorElement: "span",
            messages: {
                daily: {
                    required: "请选择抽奖类型"
                },
                maxPlayNumber: {
                    required: "请填写抽奖次数",
                    positive: "请填写正确的抽奖次数",
                    max: "请输入1-10之间的正整数",
                    min: "请输入1-10之间的正整数"
                }
            }
        });
        $("#random-set-form").validate({
            errorPlacement: function (error, element) {
                error.addClass("error-tip");
                element.closest(".form-group").append(error);
            },
            rules: {
                flowPoolProductId:{
                    required: true
                },
                flowPoolTotalProductSize: {
                    required: true,
                    positive: true
                },
                flowPoolPrizeCount: {
                    required: true,
                    positive: true
                }
            },
            errorElement: "span",
            messages: {
                flowPoolProductId: {
                    required: "请选择产品名称"
                },
                flowPoolTotalProductSize: {
                    required: "请输入产品大小"
                },
                flowPoolPrizeCount: {
                    required: "请输入数量"
                }
            }
        });
    }

</script>
</body>
</html>
