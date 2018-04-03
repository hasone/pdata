<#-- 页面顶部的内容，欢迎你，退出之类的 -->
<div class="navbar navbar-default" id="navbar">
    <script type="text/javascript">
        try {
            ace.settings.check('navbar', 'fixed')
        } catch (e) {
        }
    </script>


    <div class="navbar-container" id="navbar-container" style="width:expression(document.body.clientWidth <= 960? '960px': 'auto');  >
        <div class=" navbar-header pull-left
    ">
    <a href="#" class="navbar-brand">
        <div id="logo-img"></div>
    </a><!-- /.brand -->
</div><!-- /.navbar-header -->

<div class="navbar-header pull-right" role="navigation">
    <ul class="nav ace-nav">
        <li class="light-grey">
            <p>欢迎您，<a href="/web-in/manage/userInfo/showCurrentUserDetails.html">${Session["currentUserName"]! }</a> |
                <a href="${contextPath}/j_spring_security_logout">退出</a></p>
        </li>
    </ul><!-- /.ace-nav -->
</div><!-- /.navbar-header -->
</div><!-- /.container -->
</div>