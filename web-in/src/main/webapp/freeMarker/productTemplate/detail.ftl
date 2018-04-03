<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-详情</title>
    <meta name="keywords" content="流量平台 详情"/>
    <meta name="description" content="流量平台 详情"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .form input[type='text'],
        .form input[type='number'],
        .form input[type='password'],
        .form select {
            background: transparent;
            border: 1px solid #ccc;
            outline: none;
            border-radius: 3px;
            padding: 3px 10px;
            width: 323px;
        }

        .form .form-group label {
            width: 180px;
            text-align: right;
        }

        .form .form-group .prompt {
            padding-left: 186px;
            color: #bbb;
            padding-bottom: 0;
            margin-bottom: 0;
        }
    </style>
</head>
<body>

<div class="main-container">


    <div class="module-header mt-30 mb-20">
        <h3>详情
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" href="${contextPath}/manage/productTemplate/index.html?back=1">返回</a>
        </h3>
    </div>


    <div class="tile mt-30">
        <div class="tile-content">
            <div class="row form">

                <div class="col-md-6 col-md-offset-3">
                    <div class="form-group">
                        <label>产品模板名称：</label>
                        <span>${productTemplate.name!}</span>
                    </div>
                    <div class="form-group">
                        <label>创建时间：</label>
                        <span>${(productTemplate.createTime?datetime)!}</span>
                    </div>
                    <div class="form-group">
                        <label>修改时间：</label>
                        <span>${(productTemplate.updateTime?datetime)!}</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <h5 class="h5">已包含平台产品列表: </h5>
    <div class="tile mt-30">
        <div class="table-responsive mt-30 text-center">
            <table class="table table-bordered">
                <thead>
                <tr>
                    <td>运营商</td>
                    <td>产品名称</td>
                    <td>产品编码</td>
                    <td>产品大小</td>
                    <td>售出价格</td>
                    <td>状态</td>
                    <td>使用范围</td>
                    <td>漫游范围</td>
                    <td>操作</td>
                </tr>
                </thead>
                <tbody>
                    <#if platformProducts??>
                        <#list platformProducts as item>
                            <#if item.isp=="M">
                                <td>移动</td>
                            </#if>
                            <#if item.isp=="U">
                                <td>联通</td>
                            </#if>
                            <#if item.isp=="T">
                                <td>电信</td>
                            </#if>
                            <#if item.isp=="A">
                                <td>三网</td>
                            </#if>

                            <td>${(item.name)!}</td>

                            <td>${(item.productCode)!}</td>

                            <#if (item.productSize<1024)>
                                <td>${(item.productSize)?string('#.##')}KB</td>
                            </#if>
                            <#if (item.productSize>=1024) && (item.productSize<1024*1024)>
                                <td>${(item.productSize/1024.0)?string('#.##')}MB</td>
                            </#if>
                            <#if (item.productSize>=1024*1024)>
                                <td>${(item.productSize/1024.0/1024.0)?string('#.##')}GB</td>
                            </#if>
                            <td>${(item.price/100.0)?string('#.##')}元</td>
                            
                            <#if item.status==0>
                            	<td>下架</td>
                            </#if>
                            <#if item.status==1>
                            	<td>上架</td>
                            </#if>
                            <td>${(item.ownershipRegion)!}</td>
                            <td>${(item.roamingRegion)!}</td>

                            <td>
                            	<a href="${contextPath}/manage/supplierProduct/platFormProductDetail.html?productId=${item.id}">详情</a>
                            </td>
                        </tr>
                        </#list>
                    </#if>
                </tbody>
            </table>
        </div>
    </div>
    
	<div class="mt-30 text-center">
		<a href="${contextPath}/manage/productTemplate/relatedEnterprises.html?${_csrf.parameterName}=${_csrf.token}&templateId=${productTemplate.id!}" class="btn btn-sm btn-warning">查看关联企业</a>
	</div>

</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    require(["common", "bootstrap"], function () {

    });

</script>

</body>
</html>