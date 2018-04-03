/**
 * author：123
 *
 *
 * 初始化调用方法  在js的onload事件或jq的$(document).ready()里面调用stepBar.init(id, option)即可。
 * 第一个参数为stepBar容器的id，必填，允许传入的值包括如下：
 *     jQuery对象
 *     javascript对象
 *     DOM元素（可转化为ID的字符串，如 “stepBar” || “#stepBar”） 纠错：误把jQuery对象的“#”写成“.”也同样能识别出来，但是必须保证次参数能转化成元素ID
 * 第二个参数为一个对象直接量，选填，包含如下的零个或多个
 *     step                string number   目标进度  默认为1（第一步），选填
 *     bar                 object          所在目标
 *     item                object          每个节点
 *     itemCount           number          节点数量
 *     itemWidth           number          节点宽度
 *     processWidth        number          进程线宽度
 *
 *     PS：不合法的参数将强行使用默认值
 */

var stepBar = {
    bar : {},
    item : {},
    barWidth : 0,
    itemCount : 2,
    itemWidth : 0,
    processWidth : 0,
    curProcessWidth : 0,
    step : 1,
    triggerStep : 1,
    status : 0,
    nameTable: [],
    statusTable: [],
    timeTable: [],
    
    init : function(id, option, status, nameTable, statusTable, timeTable){
        if (typeof id == "object" || id.indexOf("#") == 0) {
            this.bar = $(id);
        } else {
            if (id.indexOf(".") == 0) {
                id = id.substring(1, id.length);
            }
            this.bar = $("#" + id);
        }
        this.layout();
        this.item = this.bar.find(".ui-stepInfo");
        if (this.item.length < 2) {
            return;
        }
        this.bar.show();
        this.itemCount = this.item.length;
        this.step = !isNaN(option.step) && option.step <= this.itemCount && option.step > 0 ? option.step : 1;
        this.triggerStep = this.step;
        this.status = status;
        this.nameTable = nameTable;
        this.statusTable = statusTable;
        this.timeTable = timeTable;
        this.stepInfoWidthFun();
    },
   
    layout : function(){
        this.bar.find(".ui-stepInfo .ui-stepSequence").addClass("judge-stepSequence-hind");
        this.bar.find(".ui-stepInfo:first-child .ui-stepSequence").addClass("judge-stepSequence-pre");
    },
    stepInfoWidthFun : function(){
        if(this.itemCount > 0){
            this.barWidth = this.bar.width();
            this.itemWidth = Math.floor((this.barWidth) / (this.itemCount));//Math.floor((this.barWidth * 0.8) / (this.itemCount - 1));
            this.bar.find(".ui-stepLayout").width(Math.floor(this.barWidth));//Math.floor(this.barWidth * 0.8 + this.itemWidth)
            this.item.width(this.itemWidth);
            // this.bar.find(".ui-stepLayout").css({"margin-left": -Math.floor(this.itemWidth / 2) + 10 });
           
            this.percent();
        }
    },
    percent : function(){
        var _this = this;
        var calc = 100 / (_this.itemCount - 1);
        _this.processWidth = calc * (_this.triggerStep - 1) + "%";

        if(_this.curProcessWidth != _this.processWidth){
            _this.curProcessWidth = _this.processWidth;
            _this.bar.find(".ui-stepProcess").width(_this.processWidth);
            _this.jump();
        }
    },
    jump : function(){
        //改变该点和后一点的状态显示
        var _this = this;
        _this.bar.find(".ui-stepInfo .ui-stepName").each(function(index, item){
            $(item).html(_this.nameTable[_this.status][index]);
        });
        _this.bar.find(".ui-stepInfo .ui-stepStatus").each(function(index, item){
            $(item).html(_this.statusTable[_this.status][index]);
        });
        _this.bar.find(".ui-stepInfo .ui-stepTime").each(function(index, item){
            $(item).html(_this.timeTable[index]);
        });
        if(parseInt(_this.status) == 1){
            _this.bar.find(".ui-stepInfo .ui-stepSequence").removeClass("judge-stepSequence-pre").addClass("judge-stepSequence-hind");
            _this.bar.find(".ui-stepInfo:nth-child(-n+" + this.triggerStep + ") .ui-stepSequence").removeClass("judge-stepSequence-hind").addClass("judge-stepSequence-pre-failure");
            _this.bar.find(".ui-stepInfo:nth-child(-n+" + (this.triggerStep - 1) + ") .ui-stepSequence").removeClass("judge-stepSequence-pre-failure").addClass("judge-stepSequence-pre");
        }else{
            _this.bar.find(".ui-stepInfo .ui-stepSequence").removeClass("judge-stepSequence-pre").addClass("judge-stepSequence-hind");
            _this.bar.find(".ui-stepInfo:nth-child(-n+" + this.triggerStep + ") .ui-stepSequence").removeClass("judge-stepSequence-hind").addClass("judge-stepSequence-pre");
        }
    }
};