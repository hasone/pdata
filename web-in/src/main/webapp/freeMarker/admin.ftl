<#import "spring.ftl" as spring />
<html>
<body>
<h1>Title : ${title}</h1>
<h1>Message : ${message}</h1>

<#if Session.SPRING_SECURITY_CONTEXT?? && Session.SPRING_SECURITY_CONTEXT.authentication.principal.username?? >
<h2>
    Welcome : ${Session.SPRING_SECURITY_CONTEXT.authentication.principal.username}
</h2>
</#if>
<a href="<@spring.url "/j_spring_security_logout" />"> Logout</a>
</body>
</html>