<!DOCTYPE html>

<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title></title>
</head>

<body>
<script>
    $("body").css("opacity", 0);
    $("body").animate({"opacity": 1}, 1000);
    var forwad = $("#navigation>li:first .submenu>li:first>a").attr("href");
    window.location.href = forwad;
</script>
</body>
</html>