<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>统一流量平台-编辑流量众筹</title>
    <meta name="keywords" content="统一流量平台 编辑流量众筹"/>
    <meta name="description" content="统一流量平台 编辑流量众筹"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .form input[type='text'],
        .form input[type='number']
         {
            background: transparent;
            border: 1px solid #ccc;
            outline: none;
            border-radius: 3px;
            padding: 3px 10px;
            width: 323px;
        }

        .form label{
            margin-bottom: 0;
        }

        .file-box {
            display: inline-block;
        }

        .form .table input[type='text'],
        .form .table input[type='number'],
        .form .table select {
            width: auto;
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
            margin-left: 82px;
        }

        #setting-dialog label {
            width: 125px;
            text-align: right;
        }

        .form-group label {
            width: 79px;
        }

        .preview {
            margin-left: 115px;
            margin-top: -21px;
            background: #ebebeb;
            display: block;
            overflow: hidden;
        }

        .preview img {
            max-width: 100%;
        }

        .limit-tip {
            color: red;
            margin-left: 116px;
        }

        .upload-tip{
            color: #46c37b;
        }

        .discount-tip {
            color: red;
            margin: 0 auto;
        }

        #rules-error{
            vertical-align: top;
        }

        .btn-disabled {
            pointer-events: none;
            cursor: not-allowed;
            background-color: #999;
            border-color: #999;
            color: #fff;
        }
        .btn-yy{
            pointer-events: none;
        }
        
        .error-tip-setting {
            display: block;
            position: absolute;
            left: 98px;
            color: red;
        }

        .form-group {
            margin-bottom: 20px;
        }

        @media(max-width: 1190px) {
            .banner-btn{
                display: block;
                padding-left: 378px;
            }
        }
        
        .form .form-group select {
            border: 1px #ccc solid;
            height: 30px;
            vertical-align: top;
            outline:0;
        }
        
        .url-head {
            display:inline-block;
            vertical-align:top;
            text-align: middle;
            line-height:30px;
            height:35px;
        }

    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>编辑流量众筹
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>


    <div class="tile mt-30">
        <div class="tile-header">
            活动信息
        </div>
        <div class="tile-content">
            <form class="row form" id="dataForm">
                <div class="col-md-6">
                
                	<input type="hidden" class="form-control" name="invalidMobiles" id="invalidMobiles">
                    <#--上传黑白名单手机号-->
                    <input type="hidden" class="form-control" name="correctMobiles" id="correctMobiles">
                    <input type="hidden" class="form-control" name="activityId" id="activityId" value="${activities.activityId!}">
                    
                    <div class="form-group">
                        <label>活动名称：</label>
                        <input type="text" name="activeName" id="activeName" value="${activities.name!}" 
                        	maxlength="64" required data-bind="value: activeName, valueUpdate: 'afterkeydown'">
                        <div class="promote">活动名称支持汉字、英文字符、数字、特殊字符，长度不超过64个字符</div>
                    </div>
                    <div class="form-group">
                        <label>企业名称：</label>
                        <div class="btn-group btn-group-sm enterprise-select">
                            <input name="entId" id="entId" value="${enter.id!}" style="width: 0; height: 0;padding: 0; opacity: 0;" required>
                            <button type="button" class="btn btn-default btn-select" style="width: 299px">${enter.name!}</button>
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
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

                    <div class="form-group">
                        <label style="vertical-align: top;">活动BANNER：</label>
                        <img id="bannerImage" style="max-width: 326px; max-height: 151px;"
                        src="${contextPath}/manage/crowdFunding/getImage.html?activityId=${activities.activityId}&type=banner"/>
                        <div class="file-box banner-btn" style="vertical-align: bottom;">
                            <a href="javascript:void(0)" >修改</a>
                            <input type="file" class="file-helper imge-check" id="banner" name="banner"
                                   accept="image/*" >
                        </div>
                        <div class="promote">建议尺寸长宽710×305,最多不超过100KB,支持jpeg,jpg,png</div>
                    </div>

                    <div class="form-group">
                        <label style="vertical-align: top;">企业LOGO：</label>
                        <img id="logoImage" style="max-width: 160px; max-height: 160px;"
                        src="${contextPath}/manage/crowdFunding/getImage.html?activityId=${activities.activityId}&type=logo"/>
                        <div class="file-box" style="vertical-align: bottom;">
                            <a href="javascript:void(0)">修改</a>
                            <input type="file" class="file-helper imge-check " id="logo" name="logo"
                                   accept="image/*" >
                        </div>
                        <div class="promote">建议尺寸长宽160×160，最多不超过100KB,支持jpeg,jpg,png</div>
                    </div>

                    <#--
                    <div class="form-group">
                        <label>活动BANNER：</label>
                        <span class="file-box">
                            <a class="btn btn-sm btn-default cyan-text">上传图片</a>
                            <input type="text" name="fileName1" class="hidden">
                            <input type="file" id="myfile1" name="imgBanner" class="file-helper" accept="*/*">
                            <input name="value_imgBanner" style="width: 0; height: 0;padding: 0; opacity: 0;" required>
                        </span>
                        <div class="promote">建议尺寸长宽710×305，最多不超过100KB</div>
                    </div>

                    <div class="form-group">
                        <label>预览：</label>
                        <span class='w-100'>
                            <input type="text" class="hidden" required>
                            <span class="preview" id="imgBanner" style="width: 355px; height: 151px;">
                                <img src=""/>
                            </span>
                        </span>
                        <div class="limit-tip imgBanner"></div>
                    </div>

                    <div class="form-group">
                        <label>企业LOGO：</label>
                        <span class="file-box">
                            <a class="btn btn-sm btn-default cyan-text">上传图片</a>
                            <input type="text" name="fileName2" class="hidden">
                            <input type="file" id="myfile2" name="imgLogo" class="file-helper" accept="*/*">
                            <input name="value_imgLogo" style="width: 0; height: 0;padding: 0; opacity: 0;" required>
                        </span>
                        <div class="promote">建议尺寸长宽160×160，最多不超过100KB</div>
                    </div>

                    <div class="form-group">
                        <label>预览：</label>
                        <span class='w-100'>
                            <input type="text" class="hidden" required>
                            <span class="preview" id="imgLogo" style="width: 160px; height: 160px;">
                                <img src=""/>
                            </span>
                        </span>
                        <div class="limit-tip imgLogo"></div>
                    </div>
                    -->
                </div>
                

                <div class="col-md-6">
                    <div class="form-group">
                        <label>活动时间：</label>
                            <span id="activeTime" style="display: inline-block">
                                <input type="text" name="startTime" id="startTime" value="${(activities.startTime?datetime)!}">&nbsp;
                                至&nbsp;<input type="text" id="endTime" name="endTime" value="${(activities.endTime?datetime)!}" required>
                            </span>
                    </div>
                    <div class="form-group">
                        <label>充值时间：</label>
                            <span class="input-checkbox">
                                <input type="radio" value="1" name="chargeType" id="immediate_1" checked="checked"
                                       class="hidden" required>
                                <label for="immediate_1" class="c-checkbox mr-15 rt-1"></label>
                                <span>当月生效</span>
                            </span>
                        <#--
                            <span class="input-checkbox ml-30">
                                <input type="radio" value="2" name="immediate" id="immediate_2" class="hidden">
                                <label for="immediate_2" class="c-checkbox mr-15 rt-1"></label>
                                <span>次月生效</span>
                            </span>
                            -->
                    </div>

                <#--
                <div class="form-group">
                    <label>支付方式：</label>
                        <span class="input-checkbox">
                            <input type="radio" value="1" name="joinType" id="enterprise" checked="checked"
                                   class="hidden" required>
                            <label for="enterprise" class="c-checkbox mr-15 rt-1"></label>
                            <span>企业统付</span>
                        </span>

                        <span class="input-checkbox ml-30">
                            <input type="radio" value="2" name="joinType" id="individual" class="hidden">
                            <label for="individual" class="c-checkbox mr-15 rt-1"></label>
                            <span>个人支付</span>
                        </span>
                </div>
                -->
                    
                    <div class="form-group">
                        <label>目标人数：</label>
                        <input type="text" name="targetCount" id="targetCount" value="${crowdfundingActivityDetail.targetCount!}"maxlength="8" class="mobileOnly" required
                               data-bind="value: goalCount, valueUpdate: 'afterkeydown'">
                    </div>
                    <div class="form-group">
                        <label style="vertical-align: top;">活动规则：</label>
                        <textarea rows="10" cols="100" id="rules" name="rules" maxlength="300" placeholder="活动规则说明……"
                                  required>${crowdfundingActivityDetail.rules!}</textarea>
                        <div class="promote">活动规则长度不超过300个字符</div>
                    </div>

                    <div class="form-group">
                        <label>审核附件：</label>
                        <a id="appendixFile" class="file-text" onclick="downLoad(this)"
                           style="width: 242px;">${(crowdfundingActivityDetail.appendix)!}</a>&nbsp;&nbsp;&nbsp;
                        <div class="file-box">
                            <a href="javascript:void(0)">修改</a>
                            <input type="file" class="file-helper no-check" id="appendix" name="appendix">
                        </div>
                        <div class="promote">文件大小不超过5M</div>
                    </div>
                    
                    <#--
                    <div class="form-group">
                        <label>审核附件：</label>
                        <span class="file-box">
                            <a class="btn btn-sm btn-default cyan-text">上 传</a>
                            <input name="verify" class="hidden" value="XXX">
                            <input type="file" name="verifyFile" class="file-helper" accept="*/*" required>
                            <input name="value_verifyFile" style="width: 0; height: 0;padding: 0; opacity: 0;" required>
                            <span class="upload-tip"></span>
                        </span>
                    </div>
                    -->
					
					<#--
                    <div class="form-group">
                        <label style="font-weight:700 ">名单类型：</label>
                        <div class="btn-group btn-group-sm phoneList-select">
                            <input style="width: 0; font-size: 0; height: 0; padding: 0; opacity: 0"
                                   class="form-control searchItem"
                                   name="hasWhiteOrBlack" id="hasWhiteOrBlack"
                                   value="${crowdfundingActivityDetail.hasWhiteOrBlack!}" required>
                            <button type="button" class="btn btn-default" style="width: 299px">&nbsp;</button>
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                    aria-haspopup="true" aria-expanded="false">
                                <span class="caret"></span>
                                <span class="sr-only">Toggle Dropdown</span>
                            </button>
                            <ul class="dropdown-menu">
                                <li data-value="0"><a href="#">无黑白名单</a></li>
                                <li data-value="1"><a href="#">白名单</a></li>
                                <li data-value="2"><a href="#">黑名单</a></li>
                            </ul>
                        </div>
                    </div>
                    
                    
                    <div class="form-group" id="phoneList">
                        <label>用户列表：</label>
                        <input type="hidden" id="phones" name="phones" value="" readOnly="true" style="width: 268px;">
                        <a class="btn btn-sm btn-default cyan-text" href="${contextPath}/manage/crowdFunding/downloadPhoneList.html?activityId=${activities.activityId!}">下载原名单</a>
                        &nbsp;&nbsp;
                        <span class="file-box">
                            <input type="file" name="file" id="file" class="file-helper">
                            <a>修改</a>
                        </span>
                        <div>
                        	<span class="promote">可参加活动的手机号码文件,支持txt,每行一个手机号码</span>
						</div>
                        <p class="text-left" style="padding-left: 112px; margin-bottom: 0;" id="modal_error_msg"></p>
                    </div>
                    -->
                    <div class="form-group" id="phoneList">
                       <label>用户列表：</label>
                       <div class="btn-group btn-group-sm userList-select">
                            <input style="width: 0; font-size: 0; height: 0; padding: 0; opacity: 0"
                                   class="form-control searchItem"
                                   name="list" id="list" value="" required>
                            <button type="button" class="btn btn-default" style="width: 299px">请选择</button>
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                    aria-haspopup="true" aria-expanded="false">
                                <span class="caret"></span>
                                <span class="sr-only">Toggle Dropdown</span>
                            </button>
                            <ul class="dropdown-menu userList">
                                <li data-value=""><a href="#">请选择</a></li>
                                <li data-value="1"><a href="#">上传TXT文档</a></li>
                                <li data-value="2"><a href="#">ADC接口查询</a></li>
                                <#if enterprisesExtInfo.abilityConfig?? && enterprisesExtInfo.abilityConfig == 1><li data-value="3"><a href="#">企业接口查询</a></li></#if>
                            </ul>
                        </div>
                    </div>
                    <div class="form-group">
                    <label style="vertical-align: top;text-align: right;"></label>
                    <#if crowdfundingActivityDetail.userList?? && crowdfundingActivityDetail.userList==1>
                    <div id="old-upload" style="display:inline-block;">
                        <!-- <input type="hidden" id="phones" name="phones" value="" readOnly="true" style="width: 268px;"> -->
                        <a class="btn btn-sm btn-default cyan-text" href="${contextPath}/manage/crowdFunding/downloadPhoneList.html?activityId=${activities.activityId!}">下载原名单</a>
                        &nbsp;&nbsp;
<!--                         <span class="file-box">
                            <input type="file" name="file" id="file" class="file-helper">
                            <a>修改</a>
                        </span> -->
                        <div>
                            <span class="promote" style="margin-left:0;">可参加活动的手机号码文件,支持txt,每行一个手机号码</span>
                        </div>
                    </div>
                    </#if>
                    <div id="upload" hidden>
                         <div class="file-box">
                             <input type="text" id="phones" name="phones" value="" readOnly="true" style="width: 268px;">
                             <input type="file" name="file" id="file" class="file-helper">
                             <a class="btn-sm btn-cyan" style="margin-top: 10px;">批量上传</a>
                         </div>
                         <div class="promote" style="margin-left:0;">可参加活动的手机号码文件,支持txt,每行一个手机号码</div>
                         <p class="text-left" style="margin-bottom: 0;" id="modal_error_msg"></p>
                    </div>
                    <#if crowdfundingActivityDetail.userList?? && crowdfundingActivityDetail.userList==3>
                   
                    <div id="old-ecSearch" style="display:inline-block;">
                        <select id="head">
                            <option <#if prefix?? && prefix=='http'>selected</#if>>http</option>
                            <option <#if prefix?? && prefix=='https'>selected</#if>>https</option>
                        </select>
                        <span class="url-head">://</span><input value="${crowdfundingQueryUrl}" maxlength="500" name="" id="callbackUrl" style="height: 30px; width: 250px; border: 1px #ccc solid; outline:0;">
                    </div>
                    </#if>
                    <div id="ecSearch" hidden>
                        <select id="head">
                            <option>http</option>
                            <option>https</option>
                        </select>
                        <span class="url-head">://</span><input value="" maxlength="500" name="callbackUrl" id="callbackUrl" style="height: 30px; width: 250px; border: 1px #ccc solid; outline:0;">
                    </div>
                    <#--<span class="promote">上传txt格式文件，每行一个手机号码，号码换行操作</span>-->
                    </div>
                </div>
            </form>
        </div>
    </div>

    <div class="tile mt-30">
        <div class="tile-header">
            产品配置
        </div>
        <div class="tile-content">
            <div class="table-responsive mt-30 text-center">
                <table class="table table-bordered" id="awardsTable">
                    <thead>
                    <tr>
                        <td>产品名称</td>
                        <td>产品编码</td>
                        <td>流量包大小</td>
                        <td>售出价格</td>
                        <td>使用范围</td>
                        <td>漫游范围</td>
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
    </div>

    <div class="mt-30 text-center">
        <a class="btn btn-warning btn-sm mr-10 dialog-btn" id="save-btn" style="width: 114px;">保 存</a>
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
                <input type="hidden" class="searchItem" name="enterpriseId" id="enterpriseId">
                <a id="search-btn" class="hidden"></a>
                <div id="product-table" role="table" style="max-height: 200px; overflow: auto;"></div>
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

<div class="modal fade dialog-sm" id="sure-dialog" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content" id="tips"></span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" id="sure-btn">确 定</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<script type="text/javascript" src="${contextPath}/assets/lib/require.js"></script>
<script type="text/javascript" src="${contextPath}/assets/js/config.js"></script>
<script type="text/javascript" src="${contextPath}/assets/js/imgUpload.js"></script>
<script type="text/javascript" src="${contextPath}/assets/lib/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="${contextPath}/assets/js/up.js"></script>

<script>
    var productSizeFormat = function (value, column, row) {
        //新疆：流量池产品不显示价格
        if (row.type == 1) {
            return "-";
        }
        if (row.productSize == null) {
            return "-";
        }
        return sizeFun(row.productSize);
    }

    //转化产品大小显示格式
    function sizeFun(size){
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

    //价格显示转化
    function priceFun(price){
        return (price / 100.0).toFixed(2) + "元";
    }

    var priceFormat = function (value, column, row) {
        //新疆：流量池产品不显示价格
        if (row.type == 1 || row.price==null) {
            return "-";
        }
        return priceFun(row.price);
    }

    //价格显示转化
    function discountFun(discount){
        return (discount / 10.0).toFixed(1);
    }
    var discountFormat = function(value, column, row) {
        //新疆：流量池产品不显示
        if(row.type == 1 || row.discount == null){
            return "-";
        }
        return discountFun(row.discount);
    }

    var lastHasBlackAndWhite = '';

    var MAXAWARDNUM = 20;
    var action = "${contextPath}/manage/crowdFunding/searchProduct.html?${_csrf.parameterName}=${_csrf.token}";
    var tableData = {};
    var columns = [
        {name: "name", text: "产品名称", tip:true},
        {name: "productCode", text: "产品编码", tip:true},
        {name: "productSize", text: "流量包大小", format: productSizeFormat},
        {name: "prize", text: "售出价格", format: priceFormat},
        {name: "ownershipRegion", text: "使用范围"},
        {name: "roamingRegion", text: "漫游范围"},
        {
            name: "op", text: "操作", format: function (value, column, row) {
                tableData[row.id] = row;
                var trs = $("#awardsTable tbody tr");
                var btn = '<a class="btn btn-sm btn-success btn-choice" data-id="' + row.id + '">选择</a>';
                trs.each( function(index, item) {
                    if ($(item).data("id") == row.id) {
                        btn = '<a class="btn btn-sm btn-chosen btn-disabled" data-id="' + row.id + '">已选择</a>';
                    }
                });
                return btn;
        }
        }
    ];

    require(["common", "bootstrap", "daterangepicker", "page/list"], function () {
        //初始化日期组件
        initDateRangePicker2();

        initFormValidate();

        fileListener();
        
        fileChangeListener();

        init();

        listeners();

        initEnterpriseSelect();
        
        //initPhoneListSelect();

        $(".form img").on("click", function () {
            var src = $(this).attr("src");
            $("#img-dialog img").attr("src", src);
            $("#img-dialog").modal("show");
        });
        
        initParams();

    });
    
    function initParams() {
        var activityPrizeList = ${activityPrizeList!};
        if(activityPrizeList && activityPrizeList.length>0){
            for(var i = 0; i < activityPrizeList.length; i++){
            	var data = activityPrizeList[i];
                addAwardData({
                	id: data.productId,
                	productSize: data.productSize,
                	type: data.type,
                	name: data.productName,
                	productCode: data.productCode,
                	price: data.price,
                	ownershipRegion: data.ownershipRegion,
                	roamingRegion: data.roamingRegion,
                	discount: data.discount
            	});
            }
        }
    }
    
    function addAwardData(data){
        var parent = $("#awardsTable tbody");
        //var index = parent.find("tr").length;

        var time = parseInt(Math.random()*100000000);
        var tr = appendRow(data, time, true);

        parent.append(tr);
    }
    
    <#--
    function initPhoneListSelect() {
        var parent = $(".phoneList-select");
        var val = $("input", parent).val();
        $("li[data-value='" + val + "']", parent).children("a").click();
    }
    -->
    
    
    //转化产品大小显示格式
    function sizeFun(size){
    	if(size==""){
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

    //价格显示转化
    function priceFun(price){
    	if(price==""){
    		return "-";
    	}
        return (price / 100.0).toFixed(2) + "元";
    }
    
    //折扣显示转化
    function discountFun(discount){
    	if(discount==""){
    		return "";
    	}
        return (discount / 10.0).toFixed(1);
    }
    
    function appendRow(data, key, isFirst){
        var productSize = sizeFun(data["productSize"]);
        var price = priceFun(data["price"]);
        var discount = discountFun(data["discount"]); 
     
        var tr = $('<tr data-key="' + key + '" data-id="' + data["id"] + '" data-size="' + data["productSize"] + '" data-type="' + data["type"] + '">' +
                '<td>' + data["name"] + '</td>'+
                '<td>' + data["productCode"] + '</td>' +
                '<td>' + productSize + '</td>' +
                '<td>' + price + '</td>' +
                '<td>' + data["ownershipRegion"] + '</td>' +
                '<td>' + data["roamingRegion"] + '<input class="count" type="hidden" value="10"></td>' +
                '</td>' +
                '<td><a class="btn btn-sm btn-success btn-delete">删除</a></td>' +
                '</tr>');
        return tr;
    }
    

    function init(){
    	$("#btn-add").addClass('btn-disabled');
    	var userListId = ${crowdfundingActivityDetail.userList};
    	$("a",".userList li[data-value="+ userListId+"]").click();
        $(".enterprise-select a").on("click", function () {
            var entId = $(this).parent().attr("data-value");
            $("#enterpriseId").val(entId);
        });

       	//$("input[type='file']").change(function () {
        //    fileChoiceHandler(this);
       	//});

        $("#sure-btn").on("click", function () {
        	var flag = $("#tips").val();
        	if(flag && flag=="true"){
        		window.location.href = "${contextPath}/manage/crowdFunding/index.html";
        	}else{
        		$("#save-btn").removeClass("disabled");
        		$("#sure-dialog").modal("hide");


                //处理图片显示
                $("#bannerImage").attr("src","${contextPath}/manage/crowdFunding/getImage.html?activityId=${activities.activityId}&type=banner");
                $("#logoImage").attr("src","${contextPath}/manage/crowdFunding/getImage.html?activityId=${activities.activityId}&type=logo");
                $(".file-text").replaceWith('<a id="appendixFile" class="file-text" onclick="downLoad(this)" style="width: 242px;">${(crowdfundingActivityDetail.appendix)!}</a>&nbsp;&nbsp;&nbsp;');
        		
        		$("input[name='banner']").val("");
        		$("input[name='logo']").val("");
        		$("input[name='appendix']").val("");
        	}
        });
    }

    function initEnterpriseSelect() {
        var parent = $(".enterprise-select");
        var val = $("input", parent).val();
        $("li[data-value='" + val + "']", parent).children("a").click();
    }

<#--
    function initPhoneListSelect() {
        var parent = $(".phoneList-select");
        var val = $("input", parent).val();
        $("li[data-value='" + val + "']", parent).children("a").click();
    }
    -->

    function listeners() {
    <#--
        //监听黑白名单类型选择
        $(".phoneList-select a").on("click", function () {
            var phoneListType = $(this).parent().attr("data-value");

            if (lastHasBlackAndWhite == '') {
                //第一次，初始化
                lastHasBlackAndWhite = phoneListType;
            } else {
                //切换名单类型，原上传手机号不保存
                $("#phones").val("");
                lastHasBlackAndWhite = phoneListType;
            }
            if (phoneListType == 0) {
                $("#phoneList").hide();
            }
            if (phoneListType == 1 || phoneListType == 2) {
                $("#phoneList").show();
            }
        });
        -->

        $("#targetCount").on("input",function(){
            var val = $(this).val();
            if(val){
                val = parseInt(val);
                $(this).val(val);
            }
        });

        //监听用户列表变化
        $('.dropdown-menu.userList li').on("click",'a',function(){
            var value = $(this).parent().attr("data-value");
            if(value == "1"){
            	$("#old-upload").hide();
                $("#ecSearch").hide();
                $("#upload").css('display','inline-block');
                $("#phones").attr("required",true);
                $("#callbackUrl").attr("required",false);
                $("#callbackUrl-error").html('');
                $("#upload").show();
                $("#callbackUrl").val("");
                $("#old-ecSearch").hide();
            }else if(value == "3"){
            	$("#old-upload").hide();
                $("#upload").hide();
                $("#ecSearch").css('display','inline-block');
                $("#phones").attr("required",false);
                $("#callbackUrl").attr("required",true);
                $("#ecSearch").show();
                $("#phones-error").html('');
                $("#phones").val("");
                $("#old-ecSearch").hide();
            }else if(value == "2"||value == ""){
            	$("#old-upload").hide();
                $("#phones").attr("required",false);
                $("#callbackUrl").attr("required",false);
                $("#upload").hide();
                $("#ecSearch").hide();
                $("#callbackUrl-error").html('');
                $("#phones-error").html('');
                $("#callbackUrl").val("");
                $("#phones").val("");
                $("#old-ecSearch").hide();
            }
        });

        $("#btn-add").on("click", function () {
            addAward();
        });

        $("#awardsTable").on("click", ".btn-delete", function () {
            clearProduct($(this));
            if ($("#awardsTable tbody tr").length === MAXAWARDNUM) {
                $("#btn-add").addClass('hide');
            } else {
                $("#btn-add").removeClass('hide');
            }
            $("#btn-add").removeClass('btn-disabled');
        });

//        $("#awardsTable").on("click", ".btn-yy", function () {
//            var tr = $(this).parent().parent();
//            $("#products-dialog").modal("show");
//            $("#products-dialog").data("tr", tr);
//        });

        $("#product-table").on("click", ".btn-choice", function () {
            var id = $(this).data("id");
            var row = tableData[id];
            $(this).removeClass("btn-choice btn-success")
                    .addClass("btn-chosen btn-disabled").html("已选择");
            appendToYY(row);
            $("#btn-add").addClass("btn-disabled");
            $("#products-dialog").modal("hide");
            if ($("#awardsTable tbody tr").length === MAXAWARDNUM) {
                $("#btn-add").addClass('hide');
            } else {
                $("#btn-add").removeClass('hide');
            }
        });

        $("#save-btn").on("click", function () {
            saveData();
        });

        <#--
        $('.dropdown-menu.listType-list li a').on('click',function(){
            var val_name_id = $(this).parent().attr('data-value');
            if(val_name_id == '2' || val_name_id == '3'){
                $(".list-file").removeClass("hide");
            }else{
                $(".list-file").addClass("hide");
            }

            var ele = $('.list-file .file-helper');
            $('input[name="value_listFile"]').val('');
            $('.list-file .upload-tip').html('');
            cloneUpload(ele);
        });
        $(".dropdown-menu li.active a").click();
        $('img','#imgBanner').attr('src',imgSrc1);
        $('img','#imgLogo').attr('src',imgSrc2);
        $('input[name="value_imgBanner"]').val('default');
        $('input[name="value_imgLogo"]').val('default');
        $('input[name="value_verifyFile"]').val('default');
        $('input[name="value_listFile"]').val('default');
        $(".dropdown-menu li.active").removeClass('active');
        -->
    }


    function saveData() {
        $("#modal_error_msg").html("");
        if ($("#dataForm").validate().form()) {
        
        	var length = $("#awardsTable tbody tr").length;
        	
            if ($("#awardsTable tbody tr").length === 0) {
                showTipDialog("请添加众筹产品");
                return;
            }
            var trs = $("#awardsTable tbody tr");
            var valid = true;            
			
            if (valid) {
                var activity = {
                	activityId: $("#activityId").val(),
                    name: $("input[name='activeName']").val(),
                    entId: $("#entId").val(),
                    startTime: $("input[name='startTime']").val(),
                    endTime: $("input[name='endTime']").val()
                };
				
                var crowdfundingActivityDetail = {
                activityId: $("#activityId").val(),
                    banner: $("input[name='banner']").val(),
                    logo: $("input[name='logo']").val(),
                    chargeType: $("input[name='chargeType']").val(),
                    rules: $("textarea[name='rules']").val(),
                    appendix: $("input[name='appendix']").val(),
                    hasWhiteOrBlack: 1,  
                    targetCount: $("input[name='targetCount']").val(),
                    userList:$("input[name='list']").val()
                    //joinType: $("input[name='joinType']").val()
                };

                var activityPrizes = [];
                trs.each(function(id){
                    var item = {};
                    
                    item["activityId"] = $("#activityId").val();
                    
                    item["idPrefix"] = id;
                    //获取产品信息

                    item["productId"] = $(this).data("id");

                    //流量包
                    var size = $(this).data("size");
                    item["prizeName"] = sizeFun(size);
                    item["size"] = size;

                    item["enterpriseId"] = $("#entId").val();
                    item["type"] = $(this).data("type");
                    
                    //获取折扣信息
                	item["discount"] = $(this).find(".count").val()*10;

                    //默认
                    item["rankName"] = "-1";
                    item["count"] = "-1";
                    item["probability"] = "-1";

                    activityPrizes.push(item);
                });

                var formData = {
                    phones: $("#correctMobiles").val(),
                    activity: JSON.stringify(activity),
                    crowdfundingActivityDetail: JSON.stringify(crowdfundingActivityDetail),
                    activityPrizes: JSON.stringify(activityPrizes),
                    queryurl:$("#ecSearch").is(":visible")?$("#ecSearch #head").val() + '://' + $("#ecSearch #callbackUrl").val():$("#old-ecSearch #head").val() + '://' + $("#old-ecSearch #callbackUrl").val()
                };

                //console.log($("#phones").val());
                //console.log(JSON.stringify(activity));
                //console.log(JSON.stringify(crowdfundingActivityDetail));
                //console.log(JSON.stringify(activityPrizes));
				$("#save-btn").addClass("disabled");
                $.ajaxFileUpload({
                    type: 'post',
                    secureuri: false,
                    fileElementId: ['logo','appendix','banner'],
                    data: formData,
                    url: "${contextPath}/manage/crowdFunding/saveEditAjax.html?${_csrf.parameterName}=${_csrf.token}",
                    dataType: 'json',
                    success: function (ret) {
                        if (ret && ret.success) {
                            $("#tips").html("保存成功!");
                            $("#tips").val(ret.success);
                            $("#sure-dialog").modal("show");
                        }
                        else{
                            //showTipDialog(ret.fail);
                            $("#tips").val("false");
                            $("#tips").html(ret.fail);
                            $("#sure-dialog").modal("show");
                            fileChangeListener();
                        }
                    },
                    error: function () {
                        //showTipDialog("网络异常");
                        $("#tips").val("false");
                        $("#tips").html("网络异常");
                        $("#sure-dialog").modal("show");
                        fileChangeListener();
                    }
                });
            }

        }
    }

    function appendToYY(data) {
        var parent = $("#awardsTable tbody");
        var time = new Date().getTime();

        var productSize = sizeFun(data["productSize"]);
        var price = priceFun(data["price"]);

        var tr = $('<tr data-key="' + time + '" data-id="' + data["id"] + '" data-size="' + data["productSize"] + '" data-type="' + data["type"] + '">' +
                '<td>' + data["name"] + '</td>'+
                '<td>' + data["productCode"] + '</td>' +
                '<td>' + productSize + '</td>' +
                '<td>' + price + '</td>' +
                '<td>' + data["ownershipRegion"] + '</td>' +
                '<td>' + data["roamingRegion"] + '<input class="count" type="hidden" value="10"></td>' +
                '<td><a class="btn btn-sm btn-success btn-delete">删除</a></td>' +
                '</tr>');
        parent.append(tr);
    }

    function clearProduct(ele) {
        var tr = ele.parent().parent();
        var key = tr.data("key");
        var id = tr.data("id");
        var btn = $(".btn-chosen[data-id='"+id+"']");
        btn.removeClass("btn-chosen btn-disabled")
                .addClass("btn-choice btn-success").html("选择");
        removeAward(key);
    }


    function removeAward(key) {
        var awards = $("tr[data-key='" + key + "']");
        awards.remove();

    }

    function addAward() {
        var entId = $("#enterpriseId").val();

        $("#search-btn").click();
        $("#products-dialog").modal("show");
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
            format: 'YYYY-MM-DD HH:mm:ss',
            time: {
                enabled: true
            },
            getValue: function () {
                if (startEle.val() && endEle.val())
                    return startEle.val() + ' ~ ' + endEle.val();
                else
                    return '';
            },
            setValue: function (s, s1, s2) {
                startEle.val(s1);
                endEle.val(s2);
                var validator = $("#dataForm").validate();
                if (validator.check(startEle[0])) {
                    var err = validator.errorsFor(startEle[0]);
                    if (err && err.length) {
                        err.hide();
                    }
                }

                if (validator.check(endEle[0])) {
                    var err = validator.errorsFor(endEle[0]);
                    if (err && err.length) {
                        err.hide();
                    }
                }

                $("#time_tip").hide();
                $("#time_tip").html("");
            }
        });
    }

    function initFormValidate() {
        $("#dataForm").validate({
            errorPlacement: function (error, element) {
                error.addClass("error-tip-setting");
                element.closest(".form-group").append(error);
            },
            rules: {
                entId: {
                    required: true
                },
                activeName: {
                    required: true
                },
                startTime: {
                    required: true
                },
                targetCount: {
                    required: true,
                    positive: true,
                    range: [1, 99999999]
                },
                rules: {
                    required: true
                },
                hasWhiteOrBlack: {
                    required: true
                },
                list:{
                    required: true
                },
                phones:{
                    required: true
                }
            },
            errorElement: "span",
            messages: {
                entId: {
                    required: "请选择企业"
                },
                activeName: {
                    required: "请输入活动名称"
                },
                startTime: {
                    required: "请选择活动时间"
                },
                rules: {
                    required: "请输入规则"
                },
                targetCount: {
                    required: "请输入目标人数",
                    positive: "请输入1-99999999的正整数",
                    range: "请输入1-99999999的正整数"
                },
                hasWhiteOrBlack: {
                    required: "请选择名单类型"
                },
                list:{
                    required: "请选择用户列表"
                },
                phones:{
                    required: "请上传用户列表"
                },
                callbackUrl:{
                    required: "请输入地址"
                }
            }
        });
    }

    /**
     * 文件上传
     */
    function fileChangeListener() {
    	
        $(".file-helper").on("change", function () {
            fileChoiceHandler(this);
            //upload();

            //var clone = $(this).clone();
            //$(this).parent().append(clone);
            //clone.remove();

           // fileListener();
        });
    }
    
    /**
     * 手机号码名单文件上传
     */
    function fileListener() {
        $("#file").on("change", function () {
            if (checkFileType($(this),/(txt)$/)) {
                upload();

                var clone = $(this).clone();
                $(this).parent().append(clone);
                clone.remove();

                fileListener();

            }else{
                $("#phones").val("");
                $("#correctMobiles").val("");
            }
        });
    }

    /**
     * 批量上传
     */
    function upload() {
        $.ajaxFileUpload({
            url: '${contextPath}/manage/crowdFunding/uploadPhones.html?${_csrf.parameterName}=${_csrf.token}',
            secureuri: false,
            fileElementId: 'file',
            dataType: 'json',
            success: function (data) {
                if (data) {
                    if (data.successMsg == "success") {
                        $("#correctMobiles").val(data.correctMobiles);
                        //$("#invalidMobiles").val(data.invalidMobiles);

                        $("#modal_error_msg").html("上传手机号成功!").css('color','#959595');
                        $("#phones").val(data.correctMobiles);
                        $("#phones-error").hide();
                    }

                    if (data.successMsg == "fail") {
                        $("#invalidMobiles").val(data.invalidMobiles);
                        $("#modal_error_msg").html("请至少上传一个合法的手机号!").css('color','red');
                        $("#phones").val("");
                        $("#correctMobiles").val("");
                        $("#phones-error").hide();
                    }

                    if (data.errorMsg) {
                        $("#modal_error_msg").html(data.errorMsg).css('color','red');
                        $("#phones").val("");
                        $("#correctMobiles").val("");
                        $("#phones-error").hide();
                    }
                }
            },
            error: function (data) {
                $("#modal_error_msg").html("上传文件失败").css('color','red');
                $("#phones-error").hide();
            }
        });
        return false;
    }

    /**
     * 文件选择事件监听
     */
    function fileChoiceHandler(ele) {
        if($(ele).hasClass("imge-check")) {
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
        }else if($(ele).hasClass("no-check")) {
            readFileName(ele);
        }else{
            if(checkFileType($(ele),/(txt)$/)){
                readFileName(ele);
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
                    if(!suffix){
                        showTipDialog("图片格式不正确，只支持jpg，jpeg，png");
                    }else{
//                        showTipDialog("文件格式不正确，只支持txt");
                        $("#phones-error").hide();
                        $("#modal_error_msg").html("文件格式不正确，只支持txt").css('color', 'red');
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
        if (ele.files && ele.files[0]) {// FileReader,IE10+、FF22/23、Chrome28/29支持
            if (typeof FileReader != 'undefined') {
                var reader = new FileReader;
                reader.onload = function (event) {
                    event = event || window.event;
                    var img = new Image();
                    img.onload = function () {
                        $(ele).parent().prev("img").attr("src", event.target.result);
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

            var target = $(ele).parent().prev("img");
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

    /**
     * 截取文件名称
     * @param ele
     */
    function readFileName(ele) {
        var path = ele.value;
        var lastsep = path.lastIndexOf("\\"); 
        var filename = path.substring(lastsep + 1);
        var nameEle = $(ele).parent().parent().children(".file-text");
        nameEle.replaceWith('<span class="file-text">' + filename + '</span>');
    }
    
    function downLoad(obj) {
        location.href = "${contextPath}/manage/crowdFunding/downloadFile.html?activityId=${activities.activityId!}&type=appendix";
    }


</script>


<!-- 实例化编辑器 -->
<script type="text/javascript">



</script>
</body>
</html>