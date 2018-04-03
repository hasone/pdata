<#-- 获得权限项内容 -->
<#macro getItem textStr htmlAddr>
<li>
    <a href="${htmlAddr}">
        <i class="icon-double-angle-right"></i>
    ${textStr}
    </a>
</li>
</#macro>

<#-- 获得数据统计项内容 -->
<#macro getDataItem textStr htmlAddr>
<li>
    <a href="#" class="dropdown-toggle">
        <i class="icon-list-alt">
        </i>
                <span class="menu-text">
                    	数据统计
                </span>
        <b class="arrow icon-angle-down">
        </b>
    </a>

    <ul class="submenu">
        <li>
            <a href="${htmlAddr}">
                <i class="icon-dashboard">
                </i>
		                <span class="menu-text">
                        ${textStr}
		                </span>
            </a>
        </li>
    </ul>
</li>
</#macro>

<#assign authNames = Session["authNames"]! >

<#-- 页面上侧边栏的内容 -->
<div class="sidebar" id="sidebar">
    <script type="text/javascript">
        try {
            ace.settings.check('sidebar', 'fixed')
        } catch (e) {
        }
    </script>

    <ul id="navigation" class="nav nav-list">

    <#if authNames?seq_contains("ROLE_MDRC_STATISTIC_SUPERADMIN")
    || authNames?seq_contains("ROLE_MDRC_STATISTIC_MANAGER")
    ||  authNames?seq_contains("ROLE_MDRC_STATISTIC_ENTMANAGER")
    ||  authNames?seq_contains("ROLE_TOTAL_STATISTIC_MANAGER")>
        <#if authNames?seq_contains("ROLE_MDRC_STATISTIC_SUPERADMIN")>
            <@getDataItem "数据统计" "${contextPath}/manage/dataStatistics/pieChart.html" />
        <#elseif authNames?seq_contains("ROLE_MDRC_STATISTIC_MANAGER")>
            <@getDataItem "数据统计" "${contextPath}/manage/flowChart/flowPieChart.html" />
        <#elseif authNames?seq_contains("ROLE_MDRC_STATISTIC_ENTMANAGER")>
            <@getDataItem "数据统计" "${contextPath}/manage/entFlowChart/index.html" />
        <#elseif authNames?seq_contains("ROLE_TOTAL_STATISTIC_MANAGER")>
            <@getDataItem "数据统计" "${contextPath}/manage/totalityStatistics/totalityChart.html" />
        </#if>
    </#if>

    <#if authNames?seq_contains("ROLE_PRODUCT")>

        <li>
            <a href="#" class="dropdown-toggle">
                <i class="icon-list-alt">
                </i>
	                <span class="menu-text">
	                    	产品管理
	                </span>
                <b class="arrow icon-angle-down">
                </b>
            </a>
            <ul class="submenu">
                <#if authNames?seq_contains("ROLE_PRODUCT")>
						<@getItem "产品管理" "${contextPath}/manage/product/index.html" />
					</#if>
            </ul>
        </li>
    </#if>

    <#if authNames?seq_contains("ROLE_PRODUCT")>

        <li>
            <a href="#" class="dropdown-toggle">
                <i class="icon-list-alt">
                </i>
	                <span class="menu-text">
	                    	产品管理
	                </span>
                <b class="arrow icon-angle-down">
                </b>
            </a>
            <ul class="submenu">
                <#if authNames?seq_contains("ROLE_PRODUCT")>
						<@getItem "产品管理" "${contextPath}/manage/product/index.html" />
					</#if>
            </ul>
        </li>
    </#if>
    <#if authNames?seq_contains("ROLE_USER")
    || authNames?seq_contains("ROLE_ROLE")
    ||  authNames?seq_contains("ROLE_USER_INFO")>
        <li>
            <a href="#" class="dropdown-toggle">
                <i class="icon-list-alt">
                </i>
	                <span class="menu-text">
	                    	用户管理
	                </span>
                <b class="arrow icon-angle-down">
                </b>
            </a>

            <ul class="submenu">
                <#if authNames?seq_contains("ROLE_USER")>
                    <@getItem "用户管理" "${contextPath}/manage/user/index.html" />
                </#if>
	
						<#if authNames?seq_contains("ROLE_ROLE")>
                <@getItem "角色管理" "${contextPath}/manage/role/index.html" />
            </#if>
	
						<#if authNames?seq_contains("ROLE_USER_INFO")>
                <@getItem "账户管理" "${contextPath}/manage/userInfo/showCurrentUserDetails.html" />
            </#if>
            </ul>
        </li>
    </#if>

    <#if authNames?seq_contains("ROLE_SMS_TEMPLATE")
    || authNames?seq_contains("ROLE_COIN")
    || authNames?seq_contains("ROLE_INTEGRATION")
    || authNames?seq_contains("ROLE_REDPACKET_TEMPLATE")>
        <li>
            <a href="#" class="dropdown-toggle">
                <i class="icon-list-alt">
                </i>
	                <span class="menu-text">
	                    	规则管理
	                </span>
                <b class="arrow icon-angle-down">
                </b>
            </a>
            <ul class="submenu">
                <#if authNames?seq_contains("ROLE_SMS_TEMPLATE")>
                    <@getItem "短信模板管理" "${contextPath}/manage/rule_sms_template/index.html" />
                </#if>
						
						<#if authNames?seq_contains("ROLE_COIN")>
                <@getItem "流量币规则" "${contextPath}/manage/flowcoin_rule/index.html" />
            </#if>
						
						<#if authNames?seq_contains("ROLE_INTEGRATION")>
                <@getItem "积分兑换规则" "${contextPath}/manage/integration_rule/index.html" />
            </#if>
						
						<#if authNames?seq_contains("ROLE_REDPACKET_TEMPLATE")>
                <@getItem "红包模板管理" "${contextPath}/manage/rule_template/index.html" />
            </#if>


            </ul>
        </li>
    </#if>

    <#if authNames?seq_contains("ROLE_LOGS_QUERY")
    || authNames?seq_contains("ROLE_LOGS_DOWNLOAD")>
        <li>
            <a href="#" class="dropdown-toggle">
                <i class="icon-list-alt">
                </i>
	                <span class="menu-text">
	                    	日志管理
	                </span>
                <b class="arrow icon-angle-down">
                </b>
            </a>
            <ul class="submenu">
                <#if authNames?seq_contains("ROLE_LOGS_QUERY")>
                    <@getItem "日志查询" "${contextPath}/manage/logsQuery/index.html" />
                </#if>
						
						<#if authNames?seq_contains("ROLE_LOGS_DOWNLOAD")>
                <@getItem "日志下载" "${contextPath}/manage/logsDownload/index.html" />
            </#if>
            </ul>
        </li>
    </#if>

    <#if authNames?seq_contains("ROLE_CHECK_ACCOUNT")
    || authNames?seq_contains("ROLE_CHECK_RECORD")>

        <li>
            <a href="#" class="dropdown-toggle">
                <i class="icon-list-alt">
                </i>
	                <span class="menu-text">
	                    	对账管理
	                </span>
                <b class="arrow icon-angle-down">
                </b>
            </a>
            <ul class="submenu">

                <#if authNames?seq_contains("ROLE_CHECK_ACCOUNT")>
                    <@getItem "对账管理" "${contextPath}/manage/check_account/check.html" />
                </#if>
						
						<#if authNames?seq_contains("ROLE_CHECK_RECORD")>
                <@getItem "对账记录" "${contextPath}/manage/check_record/index.html" />
            </#if>
            </ul>
        </li>
    </#if>

    <#-- 根据用户的权限项显示内容 -->
    <#if authNames??>
        <#if authNames?seq_contains("ROLE_FLOW_QUERY")
        ||authNames?seq_contains("ROLE_REDPACKET")
        || authNames?seq_contains("ROLE_REDPACKET_MONTH_GIVE")
        || authNames?seq_contains("ROLE_REDPACKET_COMMON_GIVE")
        || authNames?seq_contains("ROLE_FLOWCARD")
        || authNames?seq_contains("ROLE_PRODUCT")
        || authNames?seq_contains("ROLE_SMS_TEMPLATE")
        || authNames?seq_contains("ROLE_LOTTERY_TEMPLATE")>
            <li>
                <a href="#" class="dropdown-toggle">
                    <i class="icon-list-alt">
                    </i>
		                <span class="menu-text">
		                    业务管理
		                </span>
                    <b class="arrow icon-angle-down">
                    </b>
                </a>

                <ul class="submenu">

                    <#if authNames?seq_contains("ROLE_FLOW_QUERY")>
                        <@getItem "实时流量包查询" "${contextPath}/manage/entproquery/indexPlus.html" />
                    </#if>
						
						<#if authNames?seq_contains("ROLE_REDPACKET")>
                    <@getItem "红包管理" "${contextPath}/manage/entRedpacket/index.html" />
                </#if>
						
						<#if authNames?seq_contains("ROLE_REDPACKET_MONTH_GIVE")>
                    <@getItem "包月赠送管理" "${contextPath}/manage/monthRule/index.html" />
                </#if>
						<#if authNames?seq_contains("ROLE_REDPACKET_COMMON_GIVE")>
                    <@getItem "普通赠送管理" "${contextPath}/manage/giveRuleManager/index.html" />
                </#if>
						<#if authNames?seq_contains("ROLE_FLOWCARD")>
                    <@getItem "卡券管理" "${contextPath}/manage/flowcard/index.html" />
                </#if>
						
						<#if authNames?seq_contains("ROLE_LOTTERY_TEMPLATE")>
                    <@getItem "大转盘管理" "${contextPath}/manage/lotteryActivity/index.html" />
                </#if>
	
						<#if authNames?seq_contains("ROLE_PRODUCT")>
                    <@getItem "产品管理" "${contextPath}/manage/product/index.html" />
                </#if>
	
						<#if authNames?seq_contains("ROLE_CONFIGURATION")>
                    <@getItem "配置项列表" "${contextPath}/manage/globalConfig/index.html" />
                </#if>

                </ul>
            </li>
        </#if>
    </#if>


    <#if authNames?seq_contains("ROLE_MDRC_DATADL")
    || authNames?seq_contains("ROLE_MDRC_CFG")
    || authNames?seq_contains("ROLE_MDRC_CARDMAKER_MGMT")>
        <li>
            <a href="#" class="dropdown-toggle">
                <i class="icon-list-alt">
                </i>
	                <span class="menu-text">
	                    	营销卡管理
	                </span>
                <b class="arrow icon-angle-down">
                </b>
            </a>

            <ul class="submenu">

                <#if authNames?seq_contains("ROLE_MDRC_CFG")>
                    <@getItem "营销卡数据" "${contextPath}/manage/mdrc/config/index.html" />
                </#if>

						<#if authNames?seq_contains("ROLE_MDRC_DATADL")>
                <@getItem "制卡数据下载" "${contextPath}/manage/mdrc/download/listRecord.html" />
            </#if>
	
						<#if authNames?seq_contains("ROLE_MDRC_CARDMAKER_MGMT")>
                <@getItem "制卡商管理" "${contextPath}/manage/mdrc/cardmaker/list.html" />
            </#if>
            </ul>
        </li>
    </#if>


    <#if authNames?seq_contains("ROLE_MDRC_TEMPLATE")
    || authNames?seq_contains("ROLE_MDRC_ENTERPRISE")
    || authNames?seq_contains("ROLE_MDRC_CFG")
    || authNames?seq_contains("ROLE_MDRC_DATADL")
    || authNames?seq_contains("ROLE_MDRC_CARDMAKER_MGMT")>
        <li>
            <a href="#" class="dropdown-toggle">
                <i class="icon-list-alt">
                </i>
	                <span class="menu-text">
	                    	制卡管理
	                </span>
                <b class="arrow icon-angle-down">
                </b>
            </a>

            <ul class="submenu">
                <#if authNames?seq_contains("ROLE_MDRC_TEMPLATE")>
                    <@getItem "营销卡模板管理" "${contextPath}/manage/mdrc/template/index.html" />
                </#if>
						
						<#if authNames?seq_contains("ROLE_MDRC_ENTERPRISE")>
                <@getItem "营销卡企业管理" "${contextPath}/manage/mdrcEnterprises/index.html" />
            </#if>
						
						<#if authNames?seq_contains("ROLE_MDRC_CFG")>
                <@getItem "制卡规则管理" "${contextPath}/manage/mdrcBatchConfig/index.html" />

            </#if>

						<#if authNames?seq_contains("ROLE_MDRC_DATADL")>
                <@getItem "制卡数据下载" "${contextPath}/manage/mdrcBatchConfig/listRecord.html" />
            </#if>
	
						<#if authNames?seq_contains("ROLE_MDRC_CARDMAKER_MGMT")>
                <@getItem "制卡商管理" "${contextPath}/manage/mdrc/cardmaker/list.html" />
            </#if>
            </ul>
        </li>
    </#if>


        <!-- /.nav-list -->
        <div class="sidebar-collapse" id="sidebar-collapse">
            <i class="icon-double-angle-left" data-icon1="icon-double-angle-left" data-icon2="icon-double-angle-right">
            </i>
        </div>

        <script type="text/javascript">
            try {
                ace.settings.check('sidebar', 'collapsed')
            } catch (e) {
            }
        </script>

        <script>
            var navbarstatus = Cookies.get('navbarstatus');

            var url = window.location.pathname;
            var arr = url.split("/");
            arr.splice(arr.length - 1, 1);
            url = arr.join("/");

            var links = $("#navigation a");
            var found = false;
            links.each(function () {
                var href = $(this).attr("href");
                if (href.indexOf(url) != -1) {
                    found = true;
                    var activeele = $(this);
                    $("#navigation li").removeClass("active open");
                    var li = activeele.parent();
                    if (li.parent().hasClass("submenu")) {
                        var parent = li.parent();
                        parent.show();
                        if (navbarstatus == "1") {
                            ace.settings.sidebar_collapsed(false);
                        }
                        if (navbarstatus == "2") {
                            ace.settings.sidebar_collapsed(true);
                        }
                        parent.parent().addClass("open");
                    }
                    li.addClass("active");
                    return false;
                }
            });
            if (!found) {
                ace.settings.sidebar_collapsed(false);
            }

            $("#sidebar-collapse>i").on("click", function () {
                if ($(this).hasClass("icon-double-angle-right")) {
                    Cookies.set('navbarstatus', '1');
                } else {
                    Cookies.set('navbarstatus', '2');
                }
            });
        </script>
</div>