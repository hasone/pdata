<!DOCTYPE html>

<#global  contextPath = rc.contextPath />

<html>
<head>
    <title>失败提示页面</title>
    <meta charset="UTF-8">

    <script type="text/javascript">
        function doback() {
            window.history.go(-1);
        }
    </script>
</head>

<body>
<table class="table table-bordered table-hover definewidth m10">
    <tr>
    </tr>
    <tr>
        <td colspan="2" class="tableleft">
        ${errorMsg!}
        </td>

        <td>
            <button type="button" class="btn btn-success" onclick="doback()">返回</button>
        </td>
    </tr>
</table>
</body>
</html>