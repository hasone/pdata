<div class="data-page pull-right mt-30">


    <form name="frmGoPages" id="frmGoPages"
          action="${pageResult.callbackURL}"
          method="get"
          onsubmit="return checkFrmPages(${pageResult.pages});">

    <#-- 为表单增加隐藏域 -->
    <#list pageResult.queryObject.queryCriterias?keys as key>
        <input type="hidden" name="${key}" value="${pageResult.queryObject.queryCriterias[key]}">
    </#list>

        <ul class="pagination" style="float: left;">
            <li class="prev <#if pageResult.queryObject.pageNum == 1>disabled</#if>"><a
                    href="<#if pageResult.queryObject.pageNum == 1>javascript:void(0);<#else><@getQueryString pageResult pageResult.queryObject.pageNum-1 /></#if>"><i
                    class="fa fa-angle-left"></i></a></li>

        <#-- 第一页,包括当前页  -->
            <li  <#if pageResult.queryObject.pageNum ==1 >class="active"</#if>><a
                    href="<@getQueryString pageResult 1 />">1</a></li>

        <#-- 第二页,包括当前页 需要判断总页数是否在2及以上 -->
        <#if pageResult.pages gte 2 && pageResult.queryObject.pageNum gte 2>
            <li <#if pageResult.queryObject.pageNum ==2 >class="active"</#if>><a
                    href="<@getQueryString pageResult 2 />">2</a></li>
        </#if>

        <#-- 显示... 当前页数为6及以上会出现 -->
        <#if pageResult.queryObject.pageNum gte 6 >
            <li class="more"><span>...</span></li>
        </#if>

        <#-- 当前-2页  如果当前页是5及以上才出现  -->
        <#if pageResult.queryObject.pageNum gt 4 >
            <li>
                <a href="<@getQueryString pageResult pageResult.queryObject.pageNum-2 />">${pageResult.queryObject.pageNum-2}</a>
            </li>
        </#if>

        <#-- 当前-1页  如果当前页是4及以上才出现  -->
        <#if pageResult.queryObject.pageNum gt 3 >
            <li>
                <a href="<@getQueryString pageResult pageResult.queryObject.pageNum-1 />">${pageResult.queryObject.pageNum-1}</a>
            </li>
        </#if>

        <#--当前页 如果当前页是3及以上才出现 -->
        <#if pageResult.queryObject.pageNum gt 2 >
            <li class="active"><a
                    href="<@getQueryString pageResult pageResult.queryObject.pageNum />">${pageResult.queryObject.pageNum}</a>
            </li>
        </#if>

        <#-- 当前+1页  如果当前页少于总页数出现  -->
        <#if pageResult.queryObject.pageNum lt pageResult.pages>
            <li>
                <a href="<@getQueryString pageResult pageResult.queryObject.pageNum+1 />">${pageResult.queryObject.pageNum+1}</a>
            </li>
        </#if>

        <#-- 当前+2页  如果当前页少于总页数-1时出现  -->
        <#if pageResult.queryObject.pageNum lt pageResult.pages-1>
            <li>
                <a href="<@getQueryString pageResult pageResult.queryObject.pageNum+2 />">${pageResult.queryObject.pageNum+2}</a>
            </li>
        </#if>

        <#-- 显示...  如果当前页少于总页数-2时出现  -->
        <#if pageResult.queryObject.pageNum lt pageResult.pages-2>
            <li class="more"><span>...</span></li>
        </#if>


            <li class="next <#if pageResult.queryObject.pageNum == pageResult.pages || pageResult.pages==0 >disabled</#if>">
                <a href="<#if pageResult.queryObject.pageNum == pageResult.pages || pageResult.pages==0>javascript:void(0);<#else><@getQueryString pageResult pageResult.queryObject.pageNum+1 /></#if>"><i
                        class="fa fa-angle-right"></i></a></li>

        </ul>
        <div style="display: inline-block;float: left;margin: 20px 0;">
            <span class="ml-10">共<#if pageResult.pages?? && pageResult.pages gt 0>${pageResult.pages}<#else>
                1</#if>页，<#if pageResult.records??>${pageResult.records}<#else>0</#if>条记录</span>&nbsp;
				                <span class="page-code">每页
			                        <select name="selectSize" id="selectSize" onchange="chooseSize()">
                                        <option value="10" <#if pageResult.queryObject.pageSize==10>selected</#if>>10
                                        </option>
                                        <option value="50" <#if pageResult.queryObject.pageSize==50>selected</#if>>50
                                        </option>
                                        <option value="100" <#if pageResult.queryObject.pageSize==100>selected</#if>>
                                            100
                                        </option>
                                    </select> 条
				                </span>&nbsp;
            <span class="page-code">到第 <input name="pageNum" value="${pageResult.queryObject.pageNum}" maxlength="5"
                                              type="text" style="width: 40px;">页</span>&nbsp;
            <button class="btn btn-sm btn-info" onClick="return submitPageInfo(${pageResult.pages});">确定</button>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>


    <form name="frmPageNums" id="frmPageNums"
          action="${pageResult.callbackURL}"
          method="get">
    <#-- 为表单增加隐藏域 -->
    <#list pageResult.queryObject.queryCriterias?keys as key>
        <input type="hidden" name="${key}" value="${pageResult.queryObject.queryCriterias[key]}">
    </#list>
        <input type="hidden" id="pageSize" name="pageSize" value="${pageResult.queryObject.pageSize}"/>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>


    <script>
        function submitPageInfo(totalPage) {
            $("#page_errormsg").empty();
            var page = $(":text[name='pageNum']").val();
            var reg = new RegExp("^[1-9]+[0-9]*$");
            if (page == null || page == '') {
                $("#page_errormsg").append('页数不能为空');
                //$("#page_errormsg").show();
                return false;
            } else if (!reg.test(page)) {
                $("#page_errormsg").append('请输入合法页数');
                //$("#page_errormsg").show();
                return false;
            } else if (page > totalPage) {
                $("#page_errormsg").append('输入数字不能大于最大页数' + totalPage);
                //$("#page_errormsg").show();
                return false;
            }
            document.getElementById("frmGoPages").submit();
        }

        function checkFrmPages(totalPage) {

            var page = $(":text[name='pageNum']").val();

            var reg = new RegExp("^[1-9]+[0-9]*$");
            if (page == null || page == '') {
                $(".pagenum").html('页数不能为空');
                $(".pagenum").show();

                return false;
            } else if (!reg.test(page)) {
                $(".pagenum").html('请输入合法页数');
                $(".pagenum").show();

                return false;
            } else if (page > totalPage) {

                $(".pagenum").html('输入数字不能大于最大页数' + totalPage);
                $(".pagenum").show();

                return false;
            }

            return true;
        }

        function chooseSize() {
            var pageSize = jQuery("#selectSize option:selected").val();
            $("#pageSize").val(pageSize);
            document.getElementById("frmPageNums").submit();
        }
    </script>
</div>



<#-- 连接字符串,生成查询字符串 -->
<#macro getQueryString pageResult currentPageIndex>
${pageResult.callbackURL}?pageNum=${currentPageIndex}<#t>
    <#list pageResult.queryObject.queryCriterias?keys as key>
    &${key}=${pageResult.queryObject.queryCriterias[key]}<#t>
    </#list>
</#macro>