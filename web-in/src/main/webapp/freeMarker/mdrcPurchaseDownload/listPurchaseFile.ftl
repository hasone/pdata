<#import "../Util.ftl" as Util>
<#global  contextPath = rc.contextPath />

<!DOCTYPE html>
<html>
<head>
<title>文件列表</title>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="${contextPath}/manage2/assets/css/bootstrap.min.css" />
		<link rel="stylesheet" href="${contextPath}/manage2/assets/css/font-awesome.min.css" />
		
		<#-- artDialog -->
		<link rel="stylesheet" type="text/css" href="${contextPath}/manage2/Js/artDialog/skins/default.css">
		<script src="${contextPath}/manage2/Js/artDialog/artDialog.js"></script>
		<script src="${contextPath}/manage2/Js/artDialog/jquery.artDialog.js"></script>
		
		
		<!--[if IE 7]>
	  	<link rel="stylesheet" href="${contextPath}/manage2/assets/css/font-awesome-ie7.min.css" />
		<![endif]-->
		
		<!-- ace styles -->
	    <link rel="stylesheet" href="${contextPath}/manage2/assets/css/ace.min.css" />
	    <link rel="stylesheet" href="${contextPath}/manage2/assets/css/ace-rtl.min.css" />
	    <link rel="stylesheet" href="${contextPath}/manage2/assets/css/ace-skins.min.css" />
	    
	    <!--[if lte IE 8]>
		  <link rel="stylesheet" href="${contextPath}/manage2/assets/css/ace-ie.min.css" />
		<![endif]-->
		
		<script src="${contextPath}/manage2/assets/js/ace-extra.min.js"></script>
		
		<!--[if lt IE 9]>
			<script src="${contextPath}/manage2/assets/js/html5shiv.js"></script>
			<script src="${contextPath}/manage2/assets/js/respond.min.js"></script>
		<![endif]-->
		
	
		<script type="text/javascript" src="${contextPath}/manage2/Js/jquery.js"></script>
		<script type="text/javascript" src="${contextPath}/manage2/assets/js/js.cookie.js"></script>
		<script type="text/javascript" src="${contextPath}/manage2/Js/bootstrap.js"></script>
		    
		<script type="text/javascript">
			if("ontouchend" in document) document.write("<script src='${contextPath}/manage2/assets/js/jquery.mobile.custom.min.js'>"+"<"+"/script>");
		</script>
		<script src="${contextPath}/bootstrap/js/bootstrap.min.js"></script>
		<script src="${contextPath}/manage2/assets/js/typeahead-bs2.min.js"></script>

		<!-- page specific plugin scripts -->

		<!-- ace scripts -->
		<#--  日期组件 -->
		<link rel="stylesheet" type="text/css" href="${contextPath}/manage2/assets/css/bootstrap-datetimepicker.min.css" />
		<link rel="stylesheet" href="${contextPath}/manage2/assets/css/style.css" />
		<script type="text/javascript" src="${contextPath}/manage2/Js/bootstrap-datetimepicker.js"></script>
		<script type="text/javascript" src="${contextPath}/manage2/Js/bootstrap-datetimepicker.zh-CN.js"></script>
		<script type="text/javascript" src="${contextPath}/manage2/Js/jquery.validate.js"></script>
		<script type="text/javascript" src="${contextPath}/manage2/Js/Utility.js"></script>
		<script type="text/javascript" src="${contextPath}/manage2/Js/common.js"></script>   
		<script src="${contextPath}/manage2/assets/js/ace-elements.min.js"></script>
		<script src="${contextPath}/manage2/assets/js/ace.min.js"></script>
    <link rel="stylesheet" href="${contextPath}/manage2/assets/css/base.css"/>
    <link rel="stylesheet" href="${contextPath}/manage2/assets/js/daterangepicker/daterangepicker.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/manage2/assets/js/html5shiv.js"></script>
    <script src="${contextPath}/manage2/assets/js/respond.min.js"></script>
    <![endif]-->
    
    <script type="text/javascript" src="${contextPath}/manage2/Js/knockout-3.2.0.js"></script>
    <style>
        body{
            font-family: "Microsoft YaHei";
            background: #F3F7FA;
        }
        .page-content{
        	background: #F3F7FA;
        }
        .form input[type='text'],
        .form input[type='number'],
        .form select{
            background: transparent;
            border: 1px solid #a3a3a3;
            outline: none;
            border-radius: 3px!important;
            padding: 3px 10px;
            width: 190px;
        }
        .form .table input[type='text'],
        .form .table input[type='number'],
        .form .table select{
            width: auto;
            max-width: 100px;
        }
        textarea.rule-info{
            height: 150px;
            width: 350px;
        }

        .award-table.table tbody>tr>td{
            padding: 14px 0 30px;
            position: relative;
        }
        .award-table .error-tip{
            position: absolute;
            top: 45px;
            left: 55px;
        }
        .input-checkbox{
		    user-select: none;
		    -webkit-user-select: none;
		}
		
		.input-checkbox input{
		    opacity: 0;  filter: alpha(opacity=0);  width: 0;  height: 0; outline: none; border: 0;
		}
		
		.c-checkbox{
		    display: inline-block;
		    background: url(${contextPath}/manage2/assets/images/icon-checkbox.png) no-repeat;
		    width: 12px;
		    height: 12px;
		    cursor: pointer;
		    margin: 0;
		}
		input:checked+.c-checkbox{
		    background: url(${contextPath}/manage2/assets/images/icon-checked.png) no-repeat;
		}
		.c-checkbox.checked{
		    background: url(${contextPath}/manage2/assets/images/icon-checked.png) no-repeat;
		}
		.tdCumTitle,.tdCumInstruction{
		    vertical-align: top;
		}
		.tdCumInstruction{
		    width: 270px;
		}
		
		.bg-white {
		    background-color: #ffffff;
		}
		.cm-tip {
		    display: none;
		    position: absolute;
		    top: 50px;
		    left: 50px;
		    text-align: center;
		    border-radius: 6px;
		    z-index: 9999;
		    padding: 10px 25px;
		    border: 1px solid #ccc;
		}
		
		.cm-tip:after {
		    content: "";
		    position: absolute;
		    border: 8px solid transparent;
		}
		
		.cm-tip.bottom:after {
		    left: 50%;
		    top: -16px;
		    margin-left: -9px;
		    border-bottom-color: #46c37b;
		}
		
		.cm-tip-content {
		    display: inline-block;
		    -webkit-user-select: none;
		    user-select: none;
		    cursor: default;
		}
		.form-group input[type='text'],input[type='number'],select{
			margin-left:5px;
		}
		.tile{
			min-width: 1200px;
		}
    </style>
<link rel="stylesheet" type="text/css" href="${contextPath}/manage2/Js/artDialog/skins/default.css">
<script src="${contextPath}/manage2/Js/artDialog/artDialog.js"></script>
<script src="${contextPath}/manage2/Js/artDialog/jquery.artDialog.js"></script>
<script type="text/javascript">
$(function() {	
	$(".btnDownload").click(function() {
		$.ajax({beforeSend: function(request) {var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");request.setRequestHeader(header1, token1);},
			url:"${contextPath}/manage/logs/download.html",
			dataType : "json", //指定服务器的数据返回类型，
			success: function(data) {
				if(data && data.message){
					alert("data.message");
			    }else{
			    	alert("未知错误");
			    }	
			},
			cache: false
		});
	});	
});
</script>
</head>
<body>
	<table class="table table-hover">
			<tr>
				<th>文件名称</th>
				<th>文件大小</th>
				<th>操作</th>
			</tr>
			<#if files??>
			<#list files as file>
			<tr id="${configId}">
				<td>${file.name}</td>
				<td>${(file.length()/1024)?int}KB （${file.length()}字节）</td>
				<td><a target="_blank" id="download-btn" href="${contextPath}/manage/mdrc/purchaseDownload/file.html?configId=${configId}&fileName=${encoder.encode(file.name, 'utf-8')}&templateId=${templateId}">下载</a></td>
			</tr>
			</#list>
			<#else>
			<tr>
				<td colspan="3">没有找到可下载的文件。</td>
			</tr>
			</#if>
	</table>
    <iframe id="download_target" name="download_target" src="" style="display: none;" />

    <script>
        $("#download-btn").on("click", function(){
            var url = $(this).attr("href");
            $("#download_target").attr("src", url);
            $(this).remove();
            return false;
        });
    </script>
</body>
</html>