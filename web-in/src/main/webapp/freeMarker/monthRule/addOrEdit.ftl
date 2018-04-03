<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-新增赠送</title>
    <meta name="keywords" content="流量平台 新增赠送"/>
    <meta name="description" content="流量平台 新增赠送"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>


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
            width: 323px;
        }

        .form .table input[type='text'],
        .form .table input[type='number'],
        .form .table select {
            width: auto;
        }

        .form-group label{
            width: 105px;
            text-align: right;
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
            height: 300px;
            border: 1px solid #ccc;
            margin: 0 auto;
        }

        #flowsize-error {
            display: block;
            margin-left: 95px;
        }

        .promote {
            font-size: 12px;
            color: #959595;
            margin-left: 109px;
        }
        .btn-disabled{
            pointer-events:none;
            background-color:#a7a7a7;
            border-color:#a7a7a7;
        }
        
        .icon-question{
            display: inline-block;
            width: 20px;
            height: 20px;
            margin-left: 5px;
            cursor:pointer;
        }
        .question-tip{
            position: absolute;
		    top: -225px;
		    left: 466px;
		    width: 320px;
        }
        
        .tip-content{
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 5px;
            background-color: #fff;
         }
        
         .arrow {
			  position: absolute;
			  left: 28px;
			  display: inline-block;
			  width: 0;
			  height: 0;
			  border-width: 6px;
			  border-style: dashed;
			  border-color: transparent;
			  border-bottom-width: 0;
			  border-top-style: solid;
		}
	    .arrow_out {
		  bottom: -5px;
		  border-top-color: #ccc;
		}
		.arrow_in {
		  bottom: -4px;
		  border-top-color: #fff;
		}
    </style>
</head>
<body>

<div class="main-container" style="position:relative;">
    <div class="module-header mt-30 mb-20">
        <h3>新增赠送
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>


    <div>
        <ul class="nav-tabs mb-0" role="tablist">
            <li role="presentation" id="single" class="tab-item active"> <a href="#panel-two" role="tab" data-toggle="tab">单独赠送</a></li>
            <li role="presentation" id="batch" class="tab-item"><a href="#panel-one" role="tab" data-toggle="tab">批量赠送</a></li>
        </ul>
    </div>

	<!-- 批量赠送信息 -->
    <div class="tab-content">
        <div role="tabpanel" class="tab-pane" id="panel-one">
            <div class="tile mt-30">
                <form action="${contextPath}/manage/monthRule/save.html" id="batchGive" method="post">
                    <div class="tile-header">活动信息</div>
                    <div class="tile-content">
                        <div class="row form">
                            <div class="col-md-12">
                                <input type="hidden" name="activityType" value="1"/>  
                                <div class="form-group">
                                    <label>企业名称：</label>
                                    <select required name="entId" id="entId1" onchange="chooseEnter(this,$('#eCode1'),$('#prdId1'))" <#if !(enterprises?? && (enterprises?size > 0))>disabled</#if>>
                                    		<#if enterprises?? && (enterprises?size > 0)>
                                        		<#list enterprises as e>
                                            		<option value="${e.id}" <#if (rule.activityType) ?? && (rule.activityType == 1) && (rule.entId)?? && e.id == rule.entId>selected</#if>>${e.name}</option>
                                       			 </#list>
                                    		</#if>
                                    </select>
                                </div>
                                
                                <div class="form-group">
                                    <label>企业编码：</label>
                                    <select required id="eCode1" name="eCode" disabled>
                                    <#if enterprises?? && (enterprises?size > 0)>
                                    	<#list enterprises as e>
                                			<option value="${e.code!}" <#if (rule.activityType) ?? && (rule.activityType == 1) && (rule.entId)?? && e.id == rule.entId>selected</#if>>${e.code!}</option>
                                       	</#list>
                                    </#if>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label>活动名称：</label>
                                    <input type="text" name="activityName" id="activityName1" maxlength="64" value="<#if (rule.activityType) ?? && (rule.activityType == 1)>${(rule.activityName)!}</#if>" class="hasPrompt" required/>
                                    <div class="promote">活动名称长度不超过64个字符</div>
                                </div>                                                           
                                <#if provinceFlag??&&provinceFlag=='sd'>
	                                <div class="form-group">
	                                    <label>赠送月数：</label>
	                                    <select required name="monthCount" id="monthCount">  
	                                         <option value="1" <#if (rule.activityType) ?? && (rule.activityType == 1) && (rule.monthCount)?? && rule.monthCount == 1>selected</#if>>1</option>
	                                         <option value="2" <#if (rule.activityType) ?? && (rule.activityType == 1) && (rule.monthCount)?? && rule.monthCount == 2>selected</#if>>2</option>
	                                         <option value="3" <#if (rule.activityType) ?? && (rule.activityType == 1) && (rule.monthCount)?? && rule.monthCount == 3>selected</#if>>3</option>
	                                         <option value="6" <#if (rule.activityType) ?? && (rule.activityType == 1) && (rule.monthCount)?? && rule.monthCount == 6>selected</#if>>6</option>
	                                         <option value="9" <#if (rule.activityType) ?? && (rule.activityType == 1) && (rule.monthCount)?? && rule.monthCount == 9>selected</#if>>9</option>
	                                         <option value="12" <#if (rule.activityType) ?? && (rule.activityType == 1) && (rule.monthCount)?? && rule.monthCount == 12>selected</#if>>12</option>
	                                    </select>
	                                </div>
                                <#else>
                                    <div class="form-group">
                                        <label>赠送月数：</label>
                                        <select required name="monthCount" id="monthCount">  
                                             <option value="1" <#if (rule.activityType) ?? && (rule.activityType == 1) && (rule.monthCount)?? && rule.monthCount == 1>selected</#if>>1</option>
                                             <option value="2" <#if (rule.activityType) ?? && (rule.activityType == 1) && (rule.monthCount)?? && rule.monthCount == 2>selected</#if>>2</option>
                                             <option value="3" <#if (rule.activityType) ?? && (rule.activityType == 1) && (rule.monthCount)?? && rule.monthCount == 3>selected</#if>>3</option>
                                             <option value="4" <#if (rule.activityType) ?? && (rule.activityType == 1) && (rule.monthCount)?? && rule.monthCount == 4>selected</#if>>4</option>
                                             <option value="5" <#if (rule.activityType) ?? && (rule.activityType == 1) && (rule.monthCount)?? && rule.monthCount == 5>selected</#if>>5</option>
                                             <option value="6" <#if (rule.activityType) ?? && (rule.activityType == 1) && (rule.monthCount)?? && rule.monthCount == 6>selected</#if>>6</option>
                                             <option value="7" <#if (rule.activityType) ?? && (rule.activityType == 1) && (rule.monthCount)?? && rule.monthCount == 7>selected</#if>>7</option>
                                             <option value="8" <#if (rule.activityType) ?? && (rule.activityType == 1) && (rule.monthCount)?? && rule.monthCount == 8>selected</#if>>8</option>
                                             <option value="9" <#if (rule.activityType) ?? && (rule.activityType == 1) && (rule.monthCount)?? && rule.monthCount == 9>selected</#if>>9</option>
                                             <option value="10" <#if (rule.activityType) ?? && (rule.activityType == 1) && (rule.monthCount)?? && rule.monthCount == 10>selected</#if>>10</option>
                                             <option value="11" <#if (rule.activityType) ?? && (rule.activityType == 1) && (rule.monthCount)?? && rule.monthCount == 11>selected</#if>>11</option>
                                             <option value="12" <#if (rule.activityType) ?? && (rule.activityType == 1) && (rule.monthCount)?? && rule.monthCount == 12>selected</#if>>12</option>
                                        </select>
                                    </div>
                                </#if>
								<div class="form-group">
                                    <label>产品名称：</label>
                                    <select name="prdId" id="prdId1" required>
                                    <#if (products?size>0) >
                                        <option value='-1' data-type="-1">---请选择产品---</option>
                                        <#list products as p>
                                            <option value="${p.id}" data-type="${p.type!}" data-id="${p.id!}"<#if (rule.activityType) ?? && (rule.activityType == 1) && (rule.prdId)?? && p.id == rule.prdId>selected</#if>>${p.name}</option>
                                        </#list>
                                    <#else>
                                        <option value='-1' data-type="-1">---该企业没有查询到产品---</option>
                                    </#if>
                                    </select>
                                </div>
                                
                               <div id="sizeDiv1" style="display: none">
                                    <div class="form-group">
                                        <label>赠送值：</label>
                                        <input type="text" name="size" id="size1" value="<#if (rule.activityType) ?? && (rule.activityType == 1)>${(rule.size)!}</#if>" required><span id="unit1">(MB)</span>
                                        <div class="promote">请填写赠送的流量池大小</div>
                                    </div>
                                </div>                                
                                                                
	                           
		                            <div class="form-group">
		                                <label>被赠送人号码：</label>
		                                <input type="text" id="phone1" name="phones" value="<#if (rule.activityType) ?? && (rule.activityType == 1)>${(rule.phones)!}</#if>" readOnly="true" required>
				                            <span class="file-box">                                
				                                <input type="file" name="file" id="file" class="file-helper">
				                                <a>批量上传</a>
				                            </span>
				                            <span class="red text-left" style="color:red" id="modal_error_msg"></span>
		                                	<span class="promote" style="display: block;">上传txt格式文件，每一行一个手机号码，号码换行操作，手机号码建议不超过20000个</span>
		                                </input>	
		                            </div>  
		                        <#if provinceFlag??&&provinceFlag!='sd'>  
	                                <div class="form-group" style="position:relative;">
	                                    <label>首次赠送时间：</label>
	                                    <input type="text" class="earch-startTime" name="startTime" value="" id="startTime1" placeholder="" required>
	                                    <img src='${contextPath}/assets/imgs/icon-question.png' class='icon-question' id='icon-question'>
	                                    <div class="question-tip" style='display:none;' id="question-tip">
		                                    <div class="tip-content"><span style="font-weight: 700;">首次赠送时间说明</span><br>
												1、首次赠送时间选择当前日期，点击赠送后则立即赠送一次，后续赠送从次月月初开始于每月1日凌晨进行。<br>
												2、选择非当前日期，点击赠送后，将在设定日期的凌晨进行首次赠送，后续赠送从次月月初开始于每月1日凌晨进行。<br>
												3、当赠送次数达到赠送月数的设置时，则赠送完成。
											</div>
		                                    <div class="arrow arrow_out"></div>
		                                    <div class="arrow arrow_in"></div>
	                                    </div>
	                                    <div class="promote">注：由于流量月末清零请尽量避免选择月末为首次赠送时间</div>
	                                </div>  
                                </#if>                 				
                            </div>
                            
                            	                        	
	                        <div class="col-md-6">
                                <div class="form-group">
                                    <label>同意事项：</label>
                                   	<input type="radio" id="notice" class="ml-5" name="agree" value='0' onclick="isAgree(this);" required/><span>流量赠送一旦创建成功，不可取消或终止</span>
                                </div>
                            </div>     
                        </div><!-- row-form -->

                    </div>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>            
                </form>
            </div>
            <div class="mt-30 text-center">
                <a href="javascript:void(0)" class="btn btn-warning btn-sm mr-10 dialog-btn btn-disabled" id="batch-save-btn">赠送</a>
                <span style="color:red" id="error_msg"><#if (rule.activityType) ?? && (rule.activityType == 1)>${errMsg!}</#if></span>
            </div>
        </div>

		<!-- 单独赠送 -->
        <div role="tabpanel" class="tab-pane active" id="panel-two">
            <form action="${contextPath}/manage/monthRule/save.html" id="singleGive"
                  method="post">
                <div class="tile mt-30">
                    <div class="tile-header">活动信息</div>
                    <div class="tile-content">
						<input type="hidden" name="activityType" value="2"/>  
                        <div class="row form">
                            <div class="col-md-12">
                                <input type="hidden" class="form-control" name="prdSize" id="prdSize">
                                <div class="form-group">
                                    <label>企业名称：</label>
                                    <select required name="entId" id="entId2" onchange="chooseEnter(this,$('#eCode2'),$('#prdId2'))" <#if !(enterprises?? && (enterprises?size > 0))>disabled</#if>>
                                    <#list enterprises as e>
                                        <option value="${e.id}" <#if (rule.activityType) ?? && (rule.activityType == 2) && (rule.entId)?? && e.id == rule.entId>selected</#if>>${e.name}</option>
                                    </#list>
                                    </select>
                                </div>
                                
                                <div class="form-group">
                                    <label>企业编码：</label>
                                    <select required id="eCode2" name="eCode" disabled>
                                    <#if enterprises?? && (enterprises?size > 0)>
                                    	<#list enterprises as e>
                                			<option value="${e.code}" <#if (rule.activityType) ?? && (rule.activityType == 2) && (rule.entId)?? && e.id == rule.entId>selected</#if>>${e.code!}</option>
                                       	</#list>
                                    </#if>
                                    </select>
                                </div>							
                                
                                <div class="form-group">
                                    <label>活动名称：</label>
                                    <input type="text" name="activityName" id="activityName2" value="<#if (rule.activityType) ?? && (rule.activityType == 2)>${(rule.activityName)!}</#if>" maxlength="64" class="hasPrompt" required/>
                                    <div class="promote">活动名称长度不超过64个字符</div>
                                </div>
                                
                                <div class="form-group">
                                    <label>产品名称：</label>
                                    <select name="prdId" id="prdId2" required>
                                        <option value='-1'>---请选择产品---</option>
                                    <#if (products?size>0) >
                                        <#list products as p>
                                            <option value="${p.id}" data-type="${p.type!}" data-id="${p.id}"
                                                <#if (rule.activityType) ?? && (rule.activityType == 2) && (rule.prdId)?? && p.id == rule.prdId>selected</#if>>${p.name}
                                            </option>
                                        </#list>
                                    <#else>
                                        <option value='-1'>---该企业没有查询到产品---</option>
                                    </#if>
                                    </select>
                                </div>
                                
                               <div id="sizeDiv2" style="display: none">
                                    <div class="form-group">
                                        <label>赠送值：</label>
                                        <input type="text" name="size" id="size2" value="<#if (rule.activityType) ?? && (rule.activityType == 2)>${(rule.size)!}</#if>" required><span id="unit2">(MB)</span>
                                        <div class="promote">请填写赠送的流量池大小</div>
                                    </div>
                                </div>  
                                                             
                                <div class="form-group">
                                    <label>赠送号码：</label>
                                    <input type="text" id="phone2" name="phones" class="mobileOnly" maxlength="11" value="<#if (rule.activityType) ?? && (rule.activityType == 2)>${(rule.phones)!}</#if>" required>
                                </div>
                                <#if provinceFlag??&&provinceFlag=='sd'>
	                                <div class="form-group">
	                                    <label>赠送月数：</label>
	                                    <select required name="monthCount" id="monthCount2">   
	                                         <option value="1" <#if (rule.activityType) ?? && (rule.activityType == 2) && (rule.monthCount)?? && rule.monthCount == 1>selected</#if>>1</option>
	                                         <option value="2" <#if (rule.activityType) ?? && (rule.activityType == 2) && (rule.monthCount)?? && rule.monthCount == 2>selected</#if>>2</option>
	                                         <option value="3" <#if (rule.activityType) ?? && (rule.activityType == 2) && (rule.monthCount)?? && rule.monthCount == 3>selected</#if>>3</option>
	                                         <option value="6" <#if (rule.activityType) ?? && (rule.activityType == 2) && (rule.monthCount)?? && rule.monthCount == 6>selected</#if>>6</option>
	                                         <option value="9" <#if (rule.activityType) ?? && (rule.activityType == 2) && (rule.monthCount)?? && rule.monthCount == 9>selected</#if>>9</option>
	                                         <option value="12" <#if (rule.activityType) ?? && (rule.activityType == 2) &&(rule.monthCount)?? && rule.monthCount == 12>selected</#if>>12</option>
	                                    </select>
	                                </div>
                                <#else>
                                    <div class="form-group">
                                        <label>赠送月数：</label>
                                        <select required name="monthCount" id="monthCount2">   
                                             <option value="1" <#if (rule.activityType) ?? && (rule.activityType == 1) && (rule.monthCount)?? && rule.monthCount == 1>selected</#if>>1</option>
                                             <option value="2" <#if (rule.activityType) ?? && (rule.activityType == 1) && (rule.monthCount)?? && rule.monthCount == 2>selected</#if>>2</option>
                                             <option value="3" <#if (rule.activityType) ?? && (rule.activityType == 1) && (rule.monthCount)?? && rule.monthCount == 3>selected</#if>>3</option>
                                             <option value="4" <#if (rule.activityType) ?? && (rule.activityType == 1) && (rule.monthCount)?? && rule.monthCount == 4>selected</#if>>4</option>
                                             <option value="5" <#if (rule.activityType) ?? && (rule.activityType == 1) && (rule.monthCount)?? && rule.monthCount == 5>selected</#if>>5</option>
                                             <option value="6" <#if (rule.activityType) ?? && (rule.activityType == 1) && (rule.monthCount)?? && rule.monthCount == 6>selected</#if>>6</option>
                                             <option value="7" <#if (rule.activityType) ?? && (rule.activityType == 1) && (rule.monthCount)?? && rule.monthCount == 7>selected</#if>>7</option>
                                             <option value="8" <#if (rule.activityType) ?? && (rule.activityType == 1) && (rule.monthCount)?? && rule.monthCount == 8>selected</#if>>8</option>
                                             <option value="9" <#if (rule.activityType) ?? && (rule.activityType == 1) && (rule.monthCount)?? && rule.monthCount == 9>selected</#if>>9</option>
                                             <option value="10" <#if (rule.activityType) ?? && (rule.activityType == 1) && (rule.monthCount)?? && rule.monthCount == 10>selected</#if>>10</option>
                                             <option value="11" <#if (rule.activityType) ?? && (rule.activityType == 1) && (rule.monthCount)?? && rule.monthCount == 11>selected</#if>>11</option>
                                             <option value="12" <#if (rule.activityType) ?? && (rule.activityType == 1) && (rule.monthCount)?? && rule.monthCount == 12>selected</#if>>12</option>
                                        </select>
                                    </div>
                                    <div class="form-group" style='position:relative;'>
	                                    <label>首次赠送时间：</label>
	                                    <input type="text" class="earch-startTime" name="startTime" value="" id="startTime" placeholder="" required>
	                                    <img src='${contextPath}/assets/imgs/icon-question.png'  class='icon-question' id='icon-question2'>
	                                    <div class="question-tip" style='display:none;' id="question-tip2">
	                                        <div class="tip-content"><span style="font-weight: 700;">首次赠送时间说明</span><br>
	                                            1、首次赠送时间选择当前日期，点击赠送后则立即赠送一次，后续赠送从次月月初开始于每月1日凌晨进行。<br>
	                                            2、选择非当前日期，点击赠送后，将在设定日期的凌晨进行首次赠送，后续赠送从次月月初开始于每月1日凌晨进行。<br>
	                                            3、当赠送次数达到赠送月数的设置时，则赠送完成。
	                                        </div>
	                                        <div class="arrow arrow_out"></div>
	                                        <div class="arrow arrow_in"></div>
	                                    </div>
	                                    <div class="promote">注：由于流量月末清零请尽量避免选择月末为首次赠送时间</div>
                                    </div>
                                </#if>
                                <div>
                                <div class="form-group">
                                    <label>同意事项：</label>
                                    <input type="radio" class="ml-5" name="agree" id="notice2" value='0' onclick="isAgree(this);" required/><span>流量赠送一旦创建成功，不可取消或终止</span>
                                </div>
								</div>
                            </div>
                        </div>

                    </div>
                </div>
                <div class="mt-30 text-center">
                    <a href="javascript:void(0)" class="btn btn-warning btn-sm mr-10 dialog-btn btn-disabled" id="single-save-btn">赠送</a>
                    <span style="color:red" id="error_msg"><#if (rule.activityType) ?? && (rule.activityType == 2)>${errMsg!}</#if></span>
                </div>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>
        </div>
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
    </div>
    <!-- loading end -->
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

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>

<script>
    var entId = "${enterprises[0].id}";
    var maxLimit = "${productSizeLimit}";
    require(["moment", "common", "bootstrap", "daterangepicker", "upload"], function (mm, a, b, c) {
        window["moment"] = mm;
        //事件监听
        listeners();
        //文件上传
        fileListener();
        
        checkFormValidate();
        initSingleDatePicker();
    });

	/**
	*	校验规则
	*/
	function checkFormValidate() {
    	$("#singleGive").validate({
        	errorPlacement: function (error, element) {
            	error.addClass("error-tip");
                if (element.parent().find(".promote").length) {
                	element.parent().find(".promote").before(error);
                } else {
                    element.closest(".form-group").append(error);
                }
            },
            errorElement: "span",
                                      rules: {
                                          activityName: {
                                              required: true,
                                              maxlength: 64,
                                              remote: "${contextPath}/manage/sensitiveWords/checkSensitiveWords.html?name="
                                                      + $('#activityName1').val()
                                          },
                                          phones: {
                                              required: true,
                                              mobile: true
                                          },
                                          prdId: {
                                              required: true,
                                              positive: true
                                          },
                                          size:{
                                          	  required: true,
                                              positive: true,
                                              max:parseInt(maxLimit)
                                          },
                                          agree:{
                                          	  required: true,
                                              positive: true	
                                          }
                                      },
                                      messages: {
                                          activityName: {
                                              required: "请填写活动名称",
                                              maxlength: "活动名称不超过64个字符",
                                              remote: "文案中有不良信息，请删除后再次提交"
                                          },
                                          phones: {
                                              required: "请填写赠送号码"
                                          },
                                          prdId: {
                                              required: "请选择产品",
                                              positive: "请选择产品"
                                          },
                                          size:{
                                          	  required: "请填写大小",
                                              positive: "请输入正整数"
                                          },
                                          agree:{
                                          	  required: "请勾选",
                                              positive: "请勾选"	
                                          }
                                      }
                                  });

        $("#batchGive").validate({
                                     errorPlacement: function (error, element) {
                                         error.addClass("error-tip");
                                         if (element.parent().find(".promote").length) {
                                             element.parent().find(".promote").before(error);
                                         } else {
                                             element.closest(".form-group").append(error);
                                         }
                                     },
                                     errorElement: "span",
                                     rules: {
                                          activityName: {
                                              required: true,
                                              maxlength: 64,
                                              remote: "${contextPath}/manage/sensitiveWords/checkSensitiveWords.html?name="
                                                      + $('#activityName2').val()
                                          },
                                          phones: {
                                              required: true
                                          },
                                          prdId: {
                                              required: true,
                                              positive: true
                                          },
                                          size:{
                                          	  required: true,
                                              positive: true,
                                              max:parseInt(maxLimit)
                                          },
                                          agree:{
                                          	  required: true,
                                              positive: true	
                                          }
                                      },
                                      messages: {
                                          activityName: {
                                              required: "请填写活动名称",
                                              maxlength: "活动名称不超过64个字符",
                                              remote: "文案中有不良信息，请删除后再次提交"
                                          },
                                          phones: {
                                              required: "请上传手机号码"
                                          },
                                          prdId: {
                                              required: "请选择产品",
                                              positive: "请选择产品"
                                          },
                                          size:{
                                          	  required: "请填写大小",
                                              positive: "请输入正整数"
                                          },
                                          agree:{
                                          	  required: "请勾选",
                                              positive: "请勾选"	
                                          }
                                      }
                                 });
    }

    /**
     * 事件监听
     */
    function listeners() {
    	var activityType = "${(rule.activityType)!}";
    	if(activityType == 1){//批量赠送
    		$("#single").removeClass("active");//单独赠送
    		$("#panel-two").removeClass("active");
    		$("#batch").addClass("active");//批量赠送
    		$("#panel-one").addClass("active");
    	}else{
    		$("#single").addClass("active");//单独赠送
    		$("#panel-two").addClass("active");
    		$("#batch").removeClass("active");//批量赠送
    		$("#panel-one").removeClass("active");
    	}

        $("#notice").on('click',function(){
            if($(this).is(':checked')){
                $("#batch-save-btn").removeClass("btn-disabled");
            }else{
                $("#batch-save-btn").addClass("btn-disabled");
            }
        });

        $("#notice2").on('click',function(){
            if($(this).is(':checked')){
                $("#single-save-btn").removeClass("btn-disabled");
            }else{
                $("#single-save-btn").addClass("btn-disabled");
            }
        });
    
        $("#single-save-btn").on("click", function () {
        	if ($("#singleGive").validate().form()) {
        		showToast();
            	$("#singleGive").submit();
            }	
        });
        
       $("#batch-save-btn").on("click", function () {
       		if ($("#batchGive").validate().form()) {
        		showToast();
            	$("#batchGive").submit();
            }          
        });
        
        
        $("input[name='effect']").on('change',function(){
            if($(this).attr('value')=="2"){
                $("#effectTip").show();
            }else{
                $("#effectTip").hide();
            }
        });
        
        $("input[name='batchEffect']").on('change',function(){
            if($(this).attr('value')=="2"){
                $("#effectTip2").show();
            }else{
                $("#effectTip2").hide();
            }
        });
        
        //包月赠送
        $("#prdId2").on("change", function () {
           	var type = $("option:selected", this).data("type");
            //流量池产品要增加数量输入框
            if (type == "1" || type == "3" || type == "4") {//流量池产品、话费产品、虚拟币产品
            	//getAccountInfo($('#entId2'),this,$('#account2'));
               	if(type == "1"){
            		$('#unit2').html("(MB)");
            	}else if(type == "3"){
            		$('#unit2').html("(元)");
            	}else{
            		$('#unit2').html("(个)");
            	}
                $('#sizeDiv2').show();               
            } else {//流量包产品
                $('#sizeDiv2').hide();
            }
        });

		//单独赠送
        $("#prdId1").on("change", function () {
            var type = $("option:selected", this).data("type");
            //流量池产品要增加数量输入框
            if (type == "1" || type == "3" || type == "4") {//流量池产品、话费产品、虚拟币产品
            	//getAccountInfo($('#entId1'),this,$('#account1'));
               	if(type == "1"){
            		$('#unit1').html("(MB)");         		
            	}else if(type == "3"){
            		$('#unit1').html("(元)");
            	}else{
            		$('#unit1').html("(个)");
            	}
                $('#sizeDiv1').show();               
            } else {//流量包产品
                $('#sizeDiv1').hide();
            }          	        
        });
        
       	//同意
        $("intput[name='agree']").on("click", function () {
            var radioCheck= $(this).val();  
        	if("1"==radioCheck){  
            	$(this).attr("checked",false);  
            	$(this).val("0");          
        	}else{   
            	$(this).val("1");               
        	}  
        });
       	
       	$('#icon-question').on('mouseover',function(){
       		$('#question-tip').show();
       	});
       	
       	$('#icon-question').on('mouseout',function(){
            $('#question-tip').hide();
        });
       	
        $('#icon-question2').on('mouseover',function(){
            $('#question-tip2').show();
        });
        
        $('#icon-question2').on('mouseout',function(){
            $('#question-tip2').hide();
        });
    }

    /**
     * 文件上传
     */
    function fileListener() {
        $("#file").on("change", function () {
            upload();
            var clone = $(this).clone();
            $(this).parent().append(clone);
            clone.remove();
            fileListener();
        });
    }
    
   /**
	*	校验手机号码
	*/		
    function upload() {
        $.ajaxFileUpload({
        	url: '${contextPath}/manage/giveRuleManager/uploadPhones.html?${_csrf.parameterName}=${_csrf.token}',
            secureuri: false,
            fileElementId: 'file',
            dataType: 'text',
            success: function (data, status) {
            	data = data.indexOf("<") == -1 ? data : $.trim($(data).html());
                var splitDatas = data.split(";");
                if (splitDatas[0] == "0") {
                	var phonenums = splitDatas[2].split(",");
                    $("#phone1").val(splitDatas[2]);
                    $("#modal_error_msg").html("成功导入" + phonenums.length + "条手机号码");
                    $("#phone1-error").html("");
                }else if (splitDatas[0] == "1") {
                     $("#modal_error_msg").html(splitDatas[1]);
                     $("#phone1-error").html("");
                }else {
                     $("#modal_error_msg").html("上传文件失败");
                     $("#phone1-error").html("");
                }
             },
             error: function (data, status, e) {
                $("#error_msg").html("上传文件失败");
                $("#phone1-error").html("");
             }
        });
        return false;
    }
    
   /**
	*	获取企业信息
	*/	
    function chooseEnter(entIdEle,entCodeEle,showEle){       	
    	var entId = entIdEle.value;
    	entCodeEle.get(0).selectedIndex = entIdEle.selectedIndex
        $.ajax({
                   beforeSend: function (request) {
                       var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                       var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                       request.setRequestHeader(header1, token1);
                   },
                   type: "GET",
                   url: "${contextPath}/manage/product/getProductsAjax.html",
                   data: {
                       enterpriseId: entId
                   },
                   dataType: "json", //指定服务器的数据返回类型，
                   success: function (data) {
                       var listProducts = data.products;
                       showEle.empty();
                       if (listProducts.length == 0) {
                           showEle.append("<option value='-1'>---该企业没有查询到产品---</option>");
                       }
                       else {
                           showEle.append("<option value='-1'>---请选择产品---</option>");
                           for (var i = 0; i < listProducts.length; i++) {
                               var id = listProducts[i].id;
                               var name = listProducts[i].name;
                               showEle.append("<option value='" + id + "' data-type='"+ listProducts[i].type + "'>" + name + "</option>");
                           }
                       }
                   },
                   error: function () {
                       showEle.empty();
                   }
               });
    }
    
    function getAccountInfo(entId,prdId,showEle){			
        $.ajax({
             beforeSend: function (request) {
                       var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                       var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                       request.setRequestHeader(header1, token1);
              },
              type: "GET",
              url: "${contextPath}/manage/giveRuleManager/getAccountInfo.html",
              data: {
                    entId: entId,
                    prdId: prdId
              },
              dataType: "json", //指定服务器的数据返回类型，
              success: function (data) {
					showEle.html(data.result);
              },
              error: function () {

              }
        });
    }

	function isAgree(radioObj){  
          
        var radioCheck= $(radioObj).val();  
        if("1"==radioCheck){  
            $(radioObj).attr("checked",false);
            $(radioObj).val("0");               
        }else{   
            $(radioObj).val("1");  
              
        }  
    } 
	
	function initSingleDatePicker() {
        var ele = $('#startTime');
        ele.dateRangePicker({
        	autoClose: true,
            singleDate : true,
            showShortcuts: false,
            startDate:new Date(),
            time: {
                enabled: true
            },
            container: '.main-container'
        });
        ele = $('#startTime1');
        ele.dateRangePicker({
            autoClose: true,
            singleDate : true,
            showShortcuts: false,
            startDate:new Date(),
            time: {
                enabled: true
            },
            container: '.main-container'
        });
    }
</script>
</body>
</html>