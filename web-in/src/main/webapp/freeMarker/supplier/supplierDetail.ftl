<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-供应商详情</title>
    <meta name="keywords" content="流量平台 供应商详情"/>
    <meta name="description" content="流量平台 供应商详情"/>

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
        <h3>供应商详情
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>


    <div class="tile mt-30">
        <div class="tile-content">
            <div class="row form">

                <div class="col-md-6 col-md-offset-3">
                    <div class="form-group">
                        <label>供应商：</label>
                        <span>${supplier.name!}</span>
                    </div>
                    <#--
                    <div class="form-group">
                        <label>企业名称：</label>
                        <span>${supplier.enterName!}</span>
                    </div>
                    <div class="form-group">
                        <label>企业编码：</label>
                        <span>${supplier.enterCode!}</span>
                    </div>
                    -->
                    <div class="form-group">
                        <label>供应商指纹：</label>
                        <span>${supplier.fingerprint!}</span>
                    </div>

                    <div class="form-group">
                        <label>状态：</label>
                    <#if supplier.status?? && supplier.status == 0>
                        <span>下架</span>
                    </#if>
                    <#if supplier.status?? && supplier.status == 1>
                        <span>上架</span>
                    </#if>
                    </div>
                    
					<div class="form-group">
                        <label>是否有充值查询接口：</label>
                    <#if supplier.isQueryChargeResult?? && supplier.isQueryChargeResult == 0>
                        <span>无</span>
                    </#if>
                    <#if supplier.isQueryChargeResult?? && supplier.isQueryChargeResult == 1>
                        <span>有</span>
                    </#if>
                    </div>
                    
                    <div class="form-group">
                        <label>创建时间：</label>
                        <span>${supplier.createTime?datetime}</span>
                    </div>
                    <div class="form-group">
                        <label>修改时间：</label>
                        <span>${supplier.updateTime?datetime}</span>
                    </div>
                    
                    <div class="form-group">
                        <label>付款方式：</label>
                        <span>${supplier.payType!}</span>
                    </div>
                    
                    <div class="form-group">
                        <label>合同编号：</label>
                        <span>${supplier.contractCode!}</span>
                    </div>
                    
                    <#--
                    <div class="form-group">
                        <label>企业余额：</label>
                        <span></span>
                    </div>
                    -->

                </div>
            </div>
        </div>
    </div>
    
    <div class="tile mt-30">
        <div class="table-responsive mt-30 text-center">
            <table class="table table-bordered" id="awardsTable">
                <thead>
                <tr>
                    <td>产品名称</td>
                    <td>产品编码</td>
                    <td>产品大小</td>
                    <td>数量</td>
                </tr>
                </thead>
                <tbody>
                    <#if supplierProducts??>
                        <#list supplierProducts as item>
                            
                            <td>${(item.name)!}</td>

                            <td>${(item.code)!}</td>
                            
                            <#if item.type?? && item.type==4>
                            	<td>${item.size!}个</td>
                            <#else>
                            	<#if (item.size<1024)>
                                	<td>${(item.size)?string('#.##')}KB</td>
                            	</#if>
                            	<#if (item.size>=1024) && (item.size<1024*1024)>
                                	<td>${(item.size/1024.0)?string('#.##')}MB</td>
                            	</#if>
                            	<#if (item.size>=1024*1024)>
                                	<td>${(item.size/1024.0/1024.0)?string('#.##')}GB</td>
                            	</#if>
                            </#if>
                            
                            <#if item.leftCount??>
                            	<td>${(item.leftCount)!}</td>
                            <#else>
                            	<td>-</td>
                            </#if>
                        </tr>
                        </#list>
                    </#if>
                </tbody>
            </table>
        </div>
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