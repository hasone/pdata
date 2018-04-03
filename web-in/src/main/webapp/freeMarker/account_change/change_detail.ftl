<!DOCTYPE html>

<#assign contextPath = rc.contextPath >

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-企业充值</title>
    <meta name="keywords" content="流量平台 企业充值"/>
    <meta name="description" content="流量平台 企业充值"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .form input[type='text'],
        .form input[type='number'],
        .form select {
            background: transparent;
            border: 1px solid #ccc;
            outline: none;
            border-radius: 3px;
            padding: 3px 10px;
            width: 120px;
        }

        span.error {
            color: red;
            margin-left: 10px;
        }

        .form .form-group .prompt {
            margin-left: 0px;
            color: #bbb;
            padding-bottom: 0;
            margin-bottom: 0;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>
			<#if approvalType?? && approvalType == 2>
				企业充值
	    	<#elseif approvalType?? && approvalType == 9>
				最小额度变更
	    	<#elseif approvalType?? && approvalType == 10>
				预警值变更
	    	<#elseif approvalType?? && approvalType == 11>
				暂停值变更
	    	</#if>
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>

    <div class="tile mt-30">
        <div class="tile-header">
            审批意见
        </div>
        <div class="tile-content">
            <div role="tabpanel" class="tab-pane active table-responsive" id="table_wrap1">
                <div class="table-responsive">
                    <table class="table table-indent text-center table-bordered-noheader mb-0">
                        <thead>
                        <tr>
                            <th>审批状态</th>
                            <th>审批用户</th>
                            <th>用户职位</th>
                            <th>审批时间</th>
                            <th>审批意见</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#if approvalRecords??>
                            <#list approvalRecords as record>
                            <tr>
                                <td>${record.description!}</td>
                                <td>${record.userName!}</td>
                                <td>${record.managerName!}${record.roleName!}</td>
                                <td>${(record.updateTime?datetime)!}</td>
                                <td title="${record.comment!}">${record.comment!}</td>
                            </tr>
                            </#list>
                        </#if>

                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <div class="tile mt-30">
        <div class="tile-header">
            企业信息
        </div>
        <div class="tile-content">
            <div class="row form">
                <form id="dataForm" action="${contextPath}/manage/accountChange/saveChange.html" method="post">
                    <div class="col-md-6 col-md-offset-4">
                    	<input type="hidden" id="approvalType" name="approvalType" value="${approvalType!}"/>
                        <input type="hidden" id="entCode" name="entCode" value="${enterprise.code!}"/>
                        <input type="hidden" id="accountChangeRequestId" name="accountChangeRequestId"
                               value="${accountChangeRequestId!}"/>

                        <div class="form-group">
                            <label>企业名称：</label>
                            <span>${enterprise.name}</span>
                        </div>
                        <div class="form-group">
                            <label>企业简称：</label>
                            <span>${enterprise.entName}</span>
                        </div>
                        <div class="form-group">
                            <label>地区：</label>
                            <span>${enterprise.cmManagerName!}</span>
                        </div>
                        <div class="form-group">
                            <label>企业编码：</label>
                                <span>
                                ${enterprise.code!}
                                </span>
                        </div>
                        <#if isShowPhone?? && isShowPhone == true>
                        <div class="form-group">
                            <label>集团编码：</label>
                                <span>
                                ${(enterprise.phone)!}
                                </span>
                        </div>
                		</#if>
                        <div class="form-group">
                            <label>创建时间：</label>
                            <span>${enterprise.createTime?datetime}</span>
                        </div>
                        <div class="form-group">
                            <label>账户名称：</label>
                            <span>${(enterprise.productName)!}</span>
                        </div>                      
                        <div class="form-group">
                            <label>账户类型：</label>
                            <span>${(enterprise.productTypeName)!}</span>
                        </div>
                        <div class="form-group">
                            <label>余额：</label>
                            <span><#if enterprise.productType ?? && enterprise.productType == 1>${(account / 1024)!} MB<#else>${(account/100)?string('0.##')} 元</#if></span>
                        </div>
                        <div class="form-group">
                            <label>
                            	        <#if approvalType?? && approvalType == 2>
											充值额度：
								    	<#elseif approvalType?? && approvalType == 9>
											最小额度：
								    	<#elseif approvalType?? && approvalType == 10>
											预警值：
								    	<#elseif approvalType?? && approvalType == 11>
											暂停值：
								    	</#if>
							</label>	    	
                            <span><#if enterprise.productType ?? && enterprise.productType == 1>${requestCount!} MB<#else>${requestCount?string('0.##')} 元</#if></span>
                        </div>
                        <#if provinceFlag ?? && provinceFlag == "gansu">
							<div class="form-group">
                            	<label>优惠类型：</label>
                            	<span>${discountType!}</span>
                        	</div>
							<div class="form-group">
                            	<label>优惠值：</label>
                            	<span><#if discountType ?? && discountType != "无">${discountValue!}<#else>无</#if></span>
                        	</div>
                        </#if>
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
            </div>

        </div>
    </div>


</div>

<!--[if lt IE 9]>
<script src="${contextPath}/assets/lib/es5-shim.min.js"></script>
<script src="${contextPath}/assets/lib/es5-sham.min.js"></script>
<![endif]-->
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>

<script>
    require(["common", "bootstrap"], function () {

    });

</script>
</body>
</html>
