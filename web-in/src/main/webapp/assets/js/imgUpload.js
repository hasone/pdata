
/**
 * 上传文件选择变化事件
 */
function fileChangeListener(){
    $(".file-helper").change(function(){
        var name = $(this).attr('name');
        if(name == "verifyFile" || name == "listFile"){
            var value = "value_" + name;
            var filePath = $(this).val();
            var index = filePath.lastIndexOf("\\");
            var filename = filePath.substr(index+1);
            if(!!filename){
                $(this).prev("input").val(filename);
                $('input[name= '+ value +']').val('success');
                $(this).parent().find('.upload-tip').html(filename);
            }else{
                $(this).prev("input").val('');
                $('input[name= '+ value +']').val('');
                $(this).parent().find('.upload-tip').html('');
                cloneUpload($(this));
                // $('input[name="verify"]').val('');
            }
        }else{
            choiceFile($(this),name);
        }
    });
}

/**
 * 选择文件
 */
function choiceFile(ele, name){
    var imgValue = 'value_'+name;
    var accept = ele.attr("accept");
    var suffix = accept.split("\/")[1];
    suffix = suffix === "*" ? null : new RegExp("("+suffix+")$");
    if(checkFileType(ele, suffix, name)){
        checkImageSize(ele, name);
    }else{
        cloneUpload(ele);
        $('input[name= '+ imgValue +']').val('');
    }
}

/**
 * 检查文件类型
 * @param ele
 * @param suffix
 */
function checkFileType(ele, suffix, name){
    if(ele.val()){
        var parts = ele.val().split(/\./);
        var ext = parts[parts.length - 1];
        if(ext){
            ext = ext.toLowerCase();
            var reg = suffix || /(gif|jpg|jpeg|png)$/;
            if(!reg.test(ext)){
                $('.limit-tip.'+ name).html("图片格式不正确，只支持gif，jpg，jpeg，png");
                return false;
            }
            $('.limit-tip.'+ name).html("");
            return true;
        }else{
            return false;
        }
    }else{
        return false;
    }
}

/**
 * 验证图片尺寸
 * @param ele
 */
function checkImageSize(ele, name){
    var file = ele[0];
    var imgValue = 'value_'+name;
    var filePath = ele.val();
    var index = filePath.lastIndexOf("\\");
    var filename = filePath.substr(index+1);

    if (file.files && file.files[0]) {// FileReader,IE10+、FF22/23、Chrome28/29支持
        if (typeof FileReader != 'undefined') {
            var reader = new FileReader();
            reader.onload = function(event) {
                event = event || window.event;
                var image = new Image();
                image.onload = function(){
                    if(checkSizeValid(ele, Math.floor(file.files[0].size/1024), name)){
                        $('img', '#'+name).width($('#'+name).width());
                        $('img', '#'+name).height($('#'+name).height());
                        $('img', '#'+name).attr("src", event.target.result);
                        ele.prev("input").val(filename);
                        $('input[name= '+ imgValue +']').val('success');
                    }else{
                        $('img', '#'+name).attr("src", '');
                        cloneUpload(ele);
                        $('input[name= '+ imgValue +']').val('');
                    }
                };
                image.src = event.target.result;
            };
            reader.readAsDataURL(file.files[0]);
        }
    }else{
        // var preload = document.createElement("div"),
        //     body = document.body,
        //     data, oriWidth, oriHeight, ratio;
        //
        // preload.style.cssText = "width:100px;height:100px;visibility: visible;position: absolute;left: 0px;top: 0px";
        // // 设置sizingMethod='image'
        // preload.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='image')";
        // body.insertBefore(preload, body.childNodes[0]);
        // file.select();
        // try {
        //     data = document.selection.createRange().text;
        // } finally {
        //     document.selection.empty();
        // }
        //
        // if ( !! data) {
        //     data = data.replace(/[)'"%]/g, function( s ) {
        //         return escape(escape( s ));
        //     });
        //     //预载图片
        //     try {
        //         preload.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = data;
        //     } catch (e) {
        //         //console.log(e.description);
        //         return;
        //     }
        // }
        //
        // oriWidth = preload.offsetWidth;
        // oriHeight = preload.offsetHeight;
        //
        // document.body.removeChild(preload);
        // preload = null;
        //
        // if(checkSizeValid(ele, oriWidth)){
        //     var target = $(ele.data("target"));
        //     var parent = target.parent();
        //     var div = $("<div>");
        //     parent.prepend(div);
        //     div.css({
        //         "filter":"progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)",
        //         position: "absolute",
        //         left: '0px',
        //         top: '0px'
        //     });
        //     div[0].filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = data;
        //     var w = parent.width();
        //     var h = w/oriWidth * oriHeight;
        //     div[0].style.width = w +"px";
        //     div[0].style.height = h +"px";
        //     target.css({height: h+"px"});
        // }
    }
}

/**
 * 验证图片尺寸大小
 * @param ele
 * @param w
 */
function checkSizeValid(ele, size, name){
    var id = ele.attr("id");
    if(id !== "img-lottery") {
        if (size > 100) {
            $('.limit-tip.'+ name).html("图片的大小为" + size + "kb，应不超过100kb");
            return false;
        }else{
            return true;
        }
    }else{
        return false;
    }
}

function cloneUpload(ele){
    var clone = ele.clone();
    ele.after(clone);
    ele.remove();
    fileChangeListener();
}