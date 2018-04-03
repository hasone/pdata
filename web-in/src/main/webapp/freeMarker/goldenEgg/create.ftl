<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>四川流量平台-添加砸金蛋</title>
    <meta name="keywords" content="四川流量平台 添加砸金蛋"/>
    <meta name="description" content="四川流量平台 添加砸金蛋"/>

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

        input.count, input.probability, input.size {
            width: 100px;
            text-align: center;
        }

        table .btn-sm {
            padding: 2px 10px !important;
        }

        .promote {
            font-size: 12px;
            color: #959595;
            margin-left: 132px;
        }

        #setting-dialog label {
            width: 125px;
            text-align: right;
        }

        .error-tip-setting {
            display: block;
            position: absolute;
            left: 200px;
            color: red;
        }

        .error-tip {
            display: block;
            margin-left: 132px;
            color: red;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>新增砸金蛋
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

                        <div class="btn-group btn-group-sm enterprise-select">
                            <input name="entId" id="entId"
                                   style="width: 0; height: 0;padding: 0; opacity: 0;" required>
                            <button type="button" class="btn btn-default" style="width: 298px;">
                                请选择
                            </button>
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
                            <#--
                            <input type="button" id="authorize" value="一键授权"
                                   onclick="startAuthorize();"
                                   style="display: none;"/>
                                   -->
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
                        <input type="text" name="activeName" id="activeName" maxlength="64" required
                               data-bind="value: activeName, valueUpdate: 'afterkeydown'">

                        <div class="promote">活动名称支持汉字、英文字符、数字、特殊字符，长度不超过64个字符</div>
                    </div>

                    <div class="form-group">
                        <label>活动规则：</label>
                        <textarea rows="10" cols="100" maxlength="255" id="rules" name="rules"
                                  placeholder="活动规则说明……"
                                  required style="vertical-align: top"></textarea>

                        <div class="promote">活动规则长度不超过255个字符</div>
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
                                   checked="checked"
                                   class="hidden">
                            <label for="nouseWechat" class="c-checkbox mr-15 rt-1"></label>
                            <span>否</span>
                        </span>
                        <span class="input-checkbox ml-30">
                            <input type="radio" value="1" name="userType" id="useWechat"
                                   class="hidden">
                            <label for="useWechat" class="c-checkbox mr-15 rt-1"></label>
                            <span>是</span>
                        </span>
                    </div>

                    <div class="form-group">
                        <label>活动时间：</label>
                            <span id="activeTime" style="display: inline-block">
                                <input type="text" name="startTime"
                                       id="startTime">&nbsp;至&nbsp;<input type="text"
                                                                          id="endTime"
                                                                          name="endTime"
                                                                          required>
                            </span>
                    </div>
                </div>
            </form>

        </div>
    </div>

    <div class="tile mt-30">
        <div class="tile-header">
            流量配置
        </div>
        <div class="tile-content">
            <div>
                <a class="btn btn-sm btn-success dialog-btn" data-toggle="modal"
                   data-target="#setting-dialog">手动设置</a>
                <input type="hidden" name="price" id="price">
                <input type="hidden" name="totalProductSize" id="totalProductSize">
                <input type="hidden" name="userCount" id="userCount">
                <input type="hidden" name="prizeCount" id="prizeCount">
                <span id="scale"></span>
            </div>
            <div class="table-responsive mt-30 text-center">
                <table class="table table-bordered" id="awardsTable">
                    <thead>
                    <tr>
                        <td>奖项</td>
                        <td>运营商</td>
                        <td>产品名称</td>
                        <td>产品编码</td>
                        <td>产品大小</td>
                    <#if cqFlag??&&cqFlag==0>
                        <td>售出价格</td>
                    </#if>
                        <td>使用范围</td>
                        <td>漫游范围</td>
                        <td>数量</td>
                        <td>概率(%)</td>
                        <td>操作</td>
                    </tr>
                    </thead>
                    <tbody>

                    </tbody>
                </table>
                <div class="text-left">
                    <a class="btn btn-sm btn-success" id="btn-add"><i class="fa fa-plus mr-5"></i>新增</a>
                </div>
            </div>
        </div>
    </div>


    <div class="mt-30 text-center">
        <a class="btn btn-warning btn-sm mr-10 dialog-btn" id="save-btn" style="width: 114px;">保
            存</a>
    </div>

</div>

<div class="modal fade dialog-lg" id="setting-dialog" data-backdrop="static">
    <form id="setting-form">
        <div class="modal-dialog">
            <div class="modal-content bd-muted">
                <div class="modal-header bg-muted bd-muted">
                    <button type="button" class="close" data-dismiss="modal"
                            aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <h5 class="modal-title text-center">手动配置</h5>
                </div>
                <div class="modal-body" style="padding: 50px;">
                    <div class="form text-center">
                        <div class="form-group form-group-sm mt-10 daily-select">
                            <label>抽奖类型:</label>

                            <div class="btn-group btn-group-sm">
                                <input name="daily" id="daily"
                                       style="width: 0; height: 0;padding: 0; opacity: 0;">
                                <button type="button" class="btn btn-default" style="width: 298px;">
                                    请选择
                                </button>
                                <button type="button" class="btn btn-default dropdown-toggle"
                                        data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="false">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu">
                                    <li data-value="0"><a href="#">活动期间游戏次数每天重置为0</a></li>
                                    <li data-value="1"><a href="#">活动期间游戏次数不重置</a></li>
                                </ul>
                            </div>
                        </div>
                        <div class="form-group form-group-sm form-inline mt-10">
                            <label>抽奖次数:</label>
                            <input class="form-control mobileOnly" name="maxPlayNumber"
                                   id="maxPlayNumber"
                                   style="width: 322px;">

                            <div class="promote" style="margin-left: 151px; text-align: left;">
                                请输入1-10之间的正整数
                            </div>
                        </div>
                        <div class="form-group form-group-sm form-inline mt-10">
                            <label>活动期间最多中奖次数:</label>
                            <input class="form-control mobileOnly" name="givedNumber"
                                   id="givedNumber"
                                   style="width: 322px;">

                            <div class="promote" style="margin-left: 151px; text-align: left;">
                                请输入1-50之间的正整数
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <div class="text-center">
                        <a class="btn btn-success btn-sm dialog-btn" id="save-setting-btn">确 定</a>
                    </div>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal-dialog -->
    </form>
</div>

<#--選擇產品，刪選條件在此設置-->
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
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
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

<div class="modal fade dialog-sm" id="sure-dialog" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content" id="tips">保存成功!</span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" id="sure-btn">确 定</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
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
                <button class="btn btn-warning btn-sm" id="auth-btn">一键授权</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>


<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>

<script>

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
        if (row.type == 1
            || row.type == 3) {
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
        if (row.type == 1
            || row.type == 3) {
            return "-";
        }
        var price = row.price;
        return (price / 100.0).toFixed(2) + "元";
    }

    var MAXAWARDNUM = 6;
    var awardsText = ["一等奖", "二等奖", "三等奖", "四等奖", "五等奖", "六等奖"];
    var action = "${contextPath}/manage/goldenegg/searchProduct.html?${_csrf.parameterName}=${_csrf.token}";
    var tableData = {};
    var countFormat = function (value, coloumn, row) {
        if (row.type == 2) {
            return row.count;
        }
        else if(row.type == 6) {
            return row.count;
        }
        else {
            return "-";
        }
    };
    var columns = [
        {name: "isp", text: "运营商", format: ispFormat},
        {name: "name", text: "产品名称", tip: true},
        {name: "productCode", text: "产品编码", tip: true},
        {name: "productSize", text: "产品大小", format: productSizeFormat},
        {name: "price", text: "售出价格", format: priceFormat},
        {name: "ownershipRegion", text: "使用范围"},
        {name: "roamingRegion", text: "漫游范围"},
        {name: "count", text: "数量", format: countFormat},
        {
            name: "op", text: "操作", format: function (value, column, row) {
            tableData[row.id] = row;
            return '<a class="btn btn-sm btn-success btn-choice" data-id="' + row.id + '">选择</a>';
        }
        }
    ];
    require(["common", "bootstrap", "daterangepicker", "page/list"], function () {
        //初始化日期组件
        initDateRangePicker2();

        initFormValidate();

        listeners();

        initEnterpriseSelect();

        initDailySelect();

        init();

        //计算活动规模
        caculateActivityScale();
    });

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

        $("#save-setting-btn").click(function () {
            if ($("#setting-form").validate().form()) {
                $("#setting-dialog").modal("hide");
            }
        });

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
        
        //不用授权
        $("#sure-btn").on("click", function () {
            window.location.href = "${contextPath}/manage/goldenegg/index.html";
        });
        
        //授权
        $("#auth-btn").on("click", function () {
        	var activityId = $("#auth").val();
    		//点击授权
    		window.open("${authorizeUrl}" + activityId);
    		//不知道会不会执行这一句
    		window.location.href = "${contextPath}/manage/goldenegg/index.html";
        });
    }

    //计算活动规模
    function caculateActivityScale() {
        var prizeCount = 0;  //奖品总数
        var userCount = 0;   //用户数
        var totalProductSize = 0;  //流量总大小
        var price = 0;  //总价钱
    }

    function initDailySelect() {
        var parent = $(".daily-select");
        var val = $("input", parent).val();
        $("li[data-value='" + val + "']", parent).children("a").click();
    }

    function initEnterpriseSelect() {
        var parent = $(".enterprise-select");
        var val = $("input", parent).val();
        $("li[data-value='" + val + "']", parent).children("a").click();
    }

    function init() {
        $(".enterprise-select a").on("click", function () {
            var entId = $(this).parent().attr("data-value");
            $("#enterpriseId").val(entId);
            getBalance(entId);

            //判断是否微信授权               
            var userType = $("input[name='userType']:checked").val();
            enterpriseChoiceListener($("#enterpriseId"), userType);
        });

        $(".daily-select a").on("click", function () {
            var daily = $(this).parent().attr("data-value");
            $("#daily").val(daily);
        });

        $("#sure-btn").on("click", function () {
            window.location.href = "${contextPath}/manage/goldenegg/index.html";
        });

        //监听企业改变
        $("#entId").change(function () {
            //企业变更，清除奖品信息

            var userType = $("input[name='userType']:checked").val();

            enterpriseChoiceListener($("#entId"), userType);
        });

        //监听微信授权
        $("input[name='userType']").change(function () {
            var userType = $("input[name='userType']:checked").val();

            enterpriseChoiceListener($("#entId"), userType);
        });
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
     * 授权
     */
    function startAuthorize() {
        var entId = $("#entId").val();
        if (entId == "") {
            showTipDialog("请选择企业！");
            return false;
        }
        window.open("${authorizeUrl}" + entId);
    }

    /**
     * 判断企业是否授权
     */
    function isAuthorized(entId) {
        if (entId == "") {
            $("#authorize").hide();
            return false;
        }
        $.ajax({
                   beforeSend: function (request) {
                       var token1 = $(window.parent.document).find("meta[name=_csrf]")
                               .attr("content");
                       var header1 = $(window.parent.document).find("meta[name=_csrf_header]")
                               .attr("content");
                       request.setRequestHeader(header1, token1);
                   },
                   url: "${contextPath}/manage/goldenegg/isAuthorized.html",
                   type: "POST",
                   dataType: "JSON",
                   data: {entId: entId}
               }).then(function (data) {
            var result = data.result;
            if (result == "true") {
                $("#authorize").hide();
            } else {
                $("#authorize").show();
            }
        });
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
        if (!num.match(a)) {
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

    function saveData() {
        if ($("#dataForm").validate().form()) {
            if ($("#awardsTable tbody tr").length === 0) {
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
            //记录总概率
            var totalProb = 0;
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
                         if (!validatePrizeNum($(this).find(".count").val())) {
                             showTipDialog(
                                     $(this).children("td:first-child").html() + "的奖品数量为1-99999999之间的正整数");
                             valid = false;
                             return false;
                         }
                         var probability = $(this).find(".probability").val();
                         if (!probability) {
                             showTipDialog("请填写" + $(this).children("td:first-child").html() + "奖品概率");
                             valid = false;
                             return false;
                         }else{
                             if(parseFloat(probability) <= 0 || parseFloat(probability) > 100 || !(/^([1-9][\d]*|0)(\.[\d]{1,2})*$/.test(probability))){
                                 showTipDialog( $(this).children("td:first-child").html() + "的中奖概率格式错误");
                                 valid = false;
                                 return false;
                             }
                         }
                         totalProb = totalProb + parseFloat($(this).find(".probability").val());

                         var $that = $(this);
                         $("tr[data-key='" + key + "']").each(function () {
                             var sizeEle = $(this).find(".size");
                             //存在自定义流量池大小的输入框判断是否输入了值
                             if (sizeEle.length) {
                                 if (!sizeEle.val()) {
                                     showTipDialog("请填写" + $that.children("td:first-child").html() + "产品大小");
                                     valid = false;
                                     return false;
                                 }
                                 if (!validateNum(sizeEle.val())) {
                                     showTipDialog(
                                             "请填写正确的" + $that.children("td:first-child").html() + "产品大小");
                                     valid = false;
                                     return false;
                                 }
                                 if (parseInt(sizeEle.val()) <= 0 || parseInt(sizeEle.val()) > 11264) {
                                     showTipDialog(
                                             "请填写正确的" + $that.children("td:first-child").html() + "产品大小");
                                     valid = false;
                                     return false;
                                 }
                             }
                         });
                     }
            )
            ;

            //判断概率总和
            if (totalProb > 100) {
                showTipDialog("中奖概率之和不能大于100");
                valid = false;
                return false;
            }

            //校验手动设置内容的填写
            if ($("#rules").val() == '') {
                showTipDialog("请填写活动规则");
                valid = false;
                return false;
            }

            if ($("#daily").val() == '') {
                showTipDialog("请选择手动配置中的抽奖类型");
                valid = false;
                return false;
            }
            if ($("#givedNumber").val() == '') {
                showTipDialog("请填写活动期间最多中奖次数");
                valid = false;
                return false;
            }
            if ($("#maxPlayNumber").val() == '') {
                showTipDialog("请填写抽奖次数");
                valid = false;
                return false;
            }
            if (!validateNum($("#givedNumber").val())) {
                showTipDialog("请填写正确的活动期间最多中奖次数");
                valid = false;
                return false;
            }
            if (!validateNum($("#maxPlayNumber").val())) {
                showTipDialog("请填写正确的抽奖次数");
                valid = false;
                return false;
            }

            //中奖次数不能超过抽奖次数
            if ($("#maxPlayNumber").val() * calMultiplier() < $("#givedNumber").val()) {
                showTipDialog("中奖次数不能超过抽奖次数!");
                valid = false;
                return false;
            }

            //是否微信鉴权
            var userType = $("input[name='userType']:checked").val();
            <#--
            if ((userType == '1') && ($("#authorize").css("display") == 'inline-block')) {
                showTipDialog("请一键授权");
                valid = false;
                return false;
            }
            -->

            if (valid) {
                var activity = {
                    name: $("input[name='activeName']").val(),
                    entId: $("#entId").val(),
                    startTime: $("#startTime").val(),
                    endTime: $("#endTime").val()
                };

                var userType = $("input[name='userType']:checked").val();

                var activityTemplate = {
                    rules: $("#rules").val(),
                    userType: userType,
                    givedNumber: $("#givedNumber").val(),
                    daily: $("#daily").val(),
                    maxPlayNumber: $("#maxPlayNumber").val()
                };

                var activityInfo = {
                    prizeCount: 0,
                    userCount: 0,
                    totalProductSize: 0,
                    price: 0
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

                            //获取产品信息
                            var product = $(this).data("product");
                            item["productId"] = product.id;

                            if (product.type == 2) {
                                //流量包
                                item["prizeName"] = sizeFun(product.productSize);
                                item["productSize"] = product.productSize;
                            } else if (product.type == 3) {
                                //话费产品
                                item["prizeName"] = priceFun($(this).find(".size").val() * 100.0);
                                item["productSize"] = $(this).find(".size").val() * 100.0;
                            } else  if (product.type == 6) {
                                     //流量包
                                    item["prizeName"] = sizeFun(product.productSize);
                                    item["productSize"] = product.productSize;
                            }else {
                                item["prizeName"] = sizeFun($(this).find(".size").val() * 1024);

                                //新疆：流量池产品，则需要获取对应输入框的产品大小
                                item["productSize"] = $(this).find(".size").val() * 1024;
                            }
                            item["enterpriseId"] = $("#entId").val();

                            item["count"] = $that.find(".count").val();
                            item["probability"] = $that.find(".probability").val();

                            item["type"] = product.type;

                            activityPrizes.push(item);
                        }
                    });
                });

                $("#save-btn").addClass("disabled");

                ajaxData("${contextPath}/manage/goldenegg/saveAjax.html", {
                    activity: JSON.stringify(activity),
                    activityTemplate: JSON.stringify(activityTemplate),
                    activityPrizes: JSON.stringify(activityPrizes),
                    activityInfo: JSON.stringify(activityInfo)
                }, function (ret) {
                    if (ret && ret.success && ret.activityId) {
                        if(userType==1){
                    		//授权
                    		$("#auth").val(ret.activityId);
                    		$("#auth-dialog").modal("show");
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

    function calMultiplier() {
        var flag = $("#daily").val();

        if (flag == 1) { //活动期间次数不重置
            return 1;
        } else if (flag == 0) { //每天重置次数
            var startStr = $("#startTime").val();
            var endStr = $("#endTime").val();

            var startDate = new Date(startStr + " 00:00:00");
            var endDate = new Date(endStr + " 23:59:59");

            var timeDiff = Math.abs(endDate.getTime() - startDate.getTime());
            return Math.ceil(timeDiff / (1000 * 3600 * 24));
        } else {
            return 1;
        }
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

    function priceFun(price) {
        if (price == null) {
            return "-";
        }
        return (price / 100.0).toFixed(2) + "元";
    }

    function appendToYY(data) {
        var tr = $("#products-dialog").data("tr");
        var tds = tr.children("td");
        var cqFlag = "${cqFlag}";
        if (tds.length === 11) {
            tds.eq(2).html(data["name"]);
            tds.eq(3).html(data["productCode"]);
            if (data["type"] == "1") {
                tds.eq(4).html('<input type="text" class="mobileOnly size">MB');

                tds.eq(5).html('-');
            } else if (data["type"] == "3") {
                tds.eq(4).html('<input type="text" class="mobileOnly size">元');
                tds.eq(5).html('-');
                tds.eq(4).children("input").on("input", function () {
                    tds.eq(5).html(priceFun($(this).val() * 100.0))
                });
            } else {
                var size = sizeFun(data["productSize"]);
                tds.eq(4).html(size);
                if (cqFlag == 0) {
                    var price = priceFun(data["price"]);
                    tds.eq(5).html(price);
                } else {
                    tds.eq(5).html(data["count"]);
                }
            }

            tds.eq(6).html(data["ownershipRegion"]);
            tds.eq(7).html(data["roamingRegion"]);
        } else {
            tds.eq(1).html(data["name"]);
            tds.eq(2).html(data["productCode"]);
            if (data["type"] == "1") {
                tds.eq(3).html('<input type="text" class="mobileOnly size">MB');
                tds.eq(4).html('-');
            } else if (data["type"] == "3") {
                tds.eq(3).html('<input type="text" class="mobileOnly size">元');
                tds.eq(4).html('-');
                tds.eq(3).children("input").on("input", function () {
                    tds.eq(4).html(priceFun($(this).val() * 100.0))
                });
            } else {
                var size = sizeFun(data["productSize"]);
                tds.eq(3).html(size);
                if (cqFlag == 0) {
                    var price = priceFun(data["price"]);
                    tds.eq(4).html(price);
                } else {
                    tds.eq(4).html(data["count"]);
                }
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
            if (tds.length === 11) {
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
                    '<td rowspan="3"  style="vertical-align: middle">' +
                    '<input class="probability">' +
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
                    '<td rowspan="1"  style="vertical-align: middle">' +
                    '<input class="probability">' +
                    '</td>' +
                    '<td><a class="btn btn-sm btn-success btn-delete">删除</a></td>' +
                    '</tr>');
        trs.data("empty", true);
        parent.append(trs);
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
                                    endEle.blur();
                                }
                            });
    }

    function initFormValidate() {
        $("#dataForm").validate({
                                    errorPlacement: function (error, element) {
                                        error.addClass("error-tip");
                                        element.closest(".form-group").append(error);
                                        // if(element.parent().find(".promote").length){
                                        //     element.after(error);
                                        // }else {
                                        // element.closest(".form-group").append(error);
                                        // }
                                    },
                                    rules: {
                                        entId: {
                                            required: true
                                        },
                                        rules: {
                                            required: true,
                                            cmaxLength: 255,
                                            remote: "${contextPath}/manage/sensitiveWords/checkSensitiveWords.html?name="
                                                    + $('#rules').val()
                                        },
                                        entName: {
                                            required: true
                                        },
                                        activeName: {
                                            required: true,
                                            remote: "${contextPath}/manage/sensitiveWords/checkSensitiveWords.html?name="
                                                    + $('#activeName').val()
                                        },
                                        endTime: {
                                            required: true
                                        }
                                    },
                                    errorElement: "span",
                                    messages: {
                                        entId: {
                                            required: "请选择企业"
                                        },
                                        rules: {
                                            required: "请填写活动规则",
                                            cmaxLength: "活动规则不允许超过255个字符",
                                            remote: "文案中有不良信息，请删除后再次提交"
                                        },
                                        entName: {
                                            required: "请选择企业"
                                        },
                                        activeName: {
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
                                            element.closest(".form-group").append(error);
                                        },
                                        rules: {
                                            maxPlayNumber: {
                                                required: true,
                                                positive: true,
                                                max: 10,
                                                min: 1
                                            },
                                            givedNumber: {
                                                required: true,
                                                positive: true,
                                                max: 50,
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
                                            givedNumber: {
                                                required: "请填写活动期间最多中奖次数",
                                                positive: "请填写正确的活动期间最多中奖次数",
                                                max: "请输入1-50之间的正整数",
                                                min: "请输入1-50之间的正整数"
                                            },
                                            maxPlayNumber: {
                                                required: "请填写抽奖次数",
                                                positive: "请填写正确的抽奖次数",
                                                max: "请输入1-10之间的正整数",
                                                min: "请输入1-10之间的正整数"
                                            }
                                        }
                                    });
    }
</script>
</body>
</html>
