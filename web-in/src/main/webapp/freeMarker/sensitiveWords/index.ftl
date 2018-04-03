<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>敏感词词库</title>

    <meta name="keywords" content="四川流量平台" />
    <meta name="description" content="四川流量平台" />

    <link rel="stylesheet" href="../../assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="../../assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="../../assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="../../assets/css/base.min.css"/>
    <link rel="stylesheet" href="../../assets/css/innerbase.min.css"/>

    <!--[if lt IE 9]>
    <script src="../assets/lib/html5shiv.js"></script>
    <script src="../assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .icon-edit { color: #5faee3;background-image: url(../../assets/imgs/icon-edit.png); }
        .icon-del { color: #fa8564;background-image: url(../../assets/imgs/icon-del.png); }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>敏感词词库</h3>
    </div>

    <div class="tools row">
        <div class="col-sm-12 dataTables_filter text-right">
            <div class="pull-left">
                <div class="file-box">
                    <a class="btn btn-sm btn-danger">上传敏感词</a>
                    <input type="file" class="file-helper" id="file" name="file" accept=".txt">
                </div>
            </div>
            <div class="form-inline">
                <div class="form-group form-group-sm">
                    <label for="world">敏感词：</label>
                    <input type="text" name="name" id="name" class="form-control searchItem" autocomplete="off"
                           placeholder="" value=""
                           maxlength="255">
                </div>
                <div class="form-group form-group-sm">
                    <label for="creator">创建人：</label>
                    <input type="text" name="creatorName" id="creatorName" class="form-control searchItem" autocomplete="off"
                           placeholder="" maxlength="255">
                </div>
                <a class="btn btn-sm btn-primary" id="search-btn">确定</a>
                <a onclick="createFile()" class="btn btn-sm btn-warning">报表导出</a>
            </div>
        </div>
    </div>

    <div class="tile mt-30">
        <div class="tile-content" style="padding: 0;">

            <div class="table-responsive">
                <div role="table" id="table"></div>
                <div role="pagination"></div>
            </div>

        </div>
    </div>
</div>

<div class="modal fade dialog-sm" id="delete-dialog" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content">确认删除关键词?</span>
            </div>
            <div class="modal-footer">
                <a class="btn btn-warning btn-sm" data-dismiss="modal">取 消</a>
                <a class="btn btn-warning btn-sm" id="delete-btn">确 定</a>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<div class="modal fade dialog-sm" id="tip-dialog" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content">?</span>
            </div>
            <div class="modal-footer">
                <a class="btn btn-warning btn-sm" data-dismiss="modal">确 定</a>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
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
<!--[if lt IE 9]>
<script src="../../assets/lib/es5-shim.min.js"></script>
<script src="../../assets/lib/es5-sham.min.js"></script>
<![endif]-->
<script src="../../assets/lib/require.js"></script>
<script src="../../assets/js/config.js"></script>


<script>
    function createFile() {
        var creatorName = document.getElementById('creatorName').value;
        creatorName = creatorName.replace("%", "%25");
        var name = document.getElementById('name').value;
        name = name.replace("%", "%25");
        window.open("${contextPath}/manage/sensitiveWords/creatCSVfile.html?creatorName=" + creatorName + "&&name=" + name);
    }


    var action = "${contextPath}/manage/sensitiveWords/sensitiveWordsList.html?${_csrf.parameterName}=${_csrf.token}";
    var columns = [{name: "name", text: "敏感词", tip: true},
        {name: "creatorName", text: "创建人"},
        {name: "updateTime", text: "添加时间", format: "DateTimeFormat"},
        {name: "op", text: "操作", format: function(value, column, row){
            return '<a href="${contextPath}/manage/sensitiveWords/edit.html?id='+row.id+'" class="text-primary btn-icon icon-edit">编辑</a>' +
                    '<a data-id="'+row.id+'" href="javascript:void(0)" class="text-primary ml-10 btn-icon icon-del">删除</a>';
        }}];

    require(["page/list","common", "bootstrap", "upload"], function() {
        initListeners();
    });

    function initListeners(){
        uploadListener();

        $("#table").on("click", ".icon-del", function(){
            var id = $(this).data("id");
            $("#delete-dialog").modal("show");
            $("#delete-dialog").data("id", id);
        });

        $("#delete-btn").on("click", function(){
            var id = $("#delete-dialog").data("id");
            doDeleteWorld(id);
        });
    }

    /**
     * 后台删除
     * @param id
     */
    function doDeleteWorld(id){
        ajaxData("${contextPath}/manage/sensitiveWords/delete.html?${_csrf.parameterName}=${_csrf.token}", {id: id}, function(ret){
            if(ret && ret.success){
            	$("#delete-dialog").modal("hide");
                $("#search-btn").click();
            }else{
                showTipDialog("删除失败");
            }
        });
    }

    function uploadListener(){
        $("#file").on("change", function(){
        	$.ajaxFileUpload({
                url: '${contextPath}/manage/sensitiveWords/insertSensitiveWords.html?${_csrf.parameterName}=${_csrf.token}',
                secureuri: false,
                fileElementId: 'file',
                dataType: 'text',
                success: function (data, status) {
                    data = data.indexOf("<") == -1 ? data : $.trim($(data).html());
                    var splitDatas = data.split(";");
                    if (splitDatas[0] == "1") {
                        showTipDialog(splitDatas[1]);
                    } else {
                        $("#search-btn").click();
                    }
                },
                error: function (data, status, e) {
                	showTipDialog("上传文件失败");
                },
                complete: function(){
                	var clone = $('<input type="file" class="file-helper" id="file" name="file" accept=".txt">');
                	$("#file").after(clone);
                	$("#file").remove();
                	uploadListener();
                }
            });
            return false;
        });
    }
</script>
</body>
</html>