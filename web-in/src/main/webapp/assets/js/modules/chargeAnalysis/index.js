/**
 * Created by yovino on 2017-07-20.
 */
var comboTree;
// var topNum = 5;
var chart_count_platform;
var chart_total_platform;
var chart_type_platform;
var chart_success_platform;
var chart_amount_platform;

var chart_count_enterprise;
var chart_rank_enterprise;
var chart_money_enterprise;
var chart_distribution_enterprise;
var chart_type_enterprise;
var chart_profit_enterprise;

var chart_rank_region;
var chart_package_region;
var chart_pool_region;
var chart_price_region;

var chart_count_production;
var chart_total_production;
var chart_distribution_production;
//var isShanDong = true;//判断是否是山东
//var isXinJiang = false;//判断是否是新疆
//var isPlus = true;//判断是否是Plus

function isEmptyObject(e) {      
	var t;      
	for (t in e)
		return !1;      
	return !0;  
}
function renderPlatform() {
    showToast();
    var params = getSearchParams();
    if(!params){
        params = null;
    }
    renderTable('platform',path + '/manage/chargeAnalyse/getPlatformKeyDate.html',params);
    renderLineChart(chart_count_platform,path +'/manage/chargeAnalyse/getPlatformChargeCountDate.html',params);
    renderLineChart(chart_total_platform,path +'/manage/chargeAnalyse/getPlatformChargeCapacityDate.html',params);
    renderverticalChart(chart_type_platform,path +'/manage/chargeAnalyse/getPlatformChargeTypeDate.html',params);
    renderLineChart(chart_success_platform,path +'/manage/chargeAnalyse/getPlatformChargeSuccessDate.html',params);
    if(!isXinJiang) {
        renderLineChart(chart_amount_platform, path +'/manage/chargeAnalyse/getPlatformChargeMoneyDate.html', params);
    }
    window.setTimeout(hideToast(), 1000);
}

function renderEnterprise() {
	console.log("boolean:"+isShanDong+isXinJiang+isPlus);
    showToast();
    var params = getSearchParams();
    if(!params){
        params = null;
    }
    renderTable('enterprise',path +'/manage/chargeAnalyse/getPlatformKeyDate.html',params);
    renderLineChart(chart_count_enterprise,path +'/manage/chargeAnalyse/getEnterChargeCountData.html',params);
    renderHorizontalChart(chart_rank_enterprise,path +'/manage/chargeAnalyse/getEnterChargeRankData.html',params);
    if(!isXinJiang) {
    	renderLineChart(chart_money_enterprise, path +'/manage/chargeAnalyse/getEnterChargeSoldeData.html', params);
    }
    renderPieReal(chart_distribution_enterprise,path +'/manage/chargeAnalyse/getEntPrdDistribuerData.html',params);
    renderHorizontalChart(chart_type_enterprise,path +'/manage/chargeAnalyse/getEnterTypeData.html',params);
    //if(isPlus){
        //renderLine(chart_profit_enterprise, path +'/manage/chargeAnalyse/getLineChartData.html',params);
    //}
    window.setTimeout(hideToast(), 1000);
}

function renderRegion() {
    showToast();
    var params = getSearchParams();
    if(!params){
        params = null;
    }
    renderTable('region',path +'/manage/chargeAnalyse/getPlatformKeyDate.html',params);
    renderHorizontalChart(chart_rank_region,path +'/manage/chargeAnalyse/getRegionCountLine.html',params);
    if(!isXinJiang){
        renderPiesReal(chart_package_region,path +'/manage/chargeAnalyse/getRegionDistributionData.html?type=2',params);
    }
    if(isXinJiang || isShanDong){
    	renderPiesReal(chart_pool_region,path +'/manage/chargeAnalyse/getRegionDistributionData.html?type=1',params);
    }
    if(!isXinJiang){
    	renderHorizontalChart(chart_price_region, path +'/manage/chargeAnalyse/getRegionMoneyLine.html',params);
    }
    window.setTimeout(hideToast(), 1000);
}

function renderProduction() {
    showToast();
    var params = getSearchParams();
    if(!params){
        params = null;
    }
    renderTable('production',path +'/manage/chargeAnalyse/getPlatformKeyDate.html',params);
    renderLineChart(chart_count_production,path +'/manage/chargeAnalyse/getProductCountLine.html',params);
    renderLineChart(chart_total_production, path +'/manage/chargeAnalyse/getProductCapacityLine.html',params);
    // if(isShanDong){
    	renderPiesReal(chart_distribution_production,path +'/manage/chargeAnalyse/getProductMultiDistribution.html',params);
    // }else{
    //     renderPieReal(chart_distribution_production,path +'/manage/chargeAnalyse/getProductMultiDistribution.html',params);
    // }
    window.setTimeout(hideToast(), 1000);
}

function getSearchParams() {
	var name = $("a","li[role='presentation'].active").attr("href").substr(1);
    var params = {};
    $('#'+name+' .searchItem').each(function () {
        var name = $(this).attr('name');
        params[name] = $(this).val();
    });
    return params;
}

/** 
* 判断是否null 
* @param data 
*/
function isNull(data){ 
	return (data == "" || data == undefined || data == null) ? true : false; 
}
//更新表格数据
function renderTable(module,url,params){
    ajaxData(url, params, function (ret) {
        var p = $('#' + module + ' .items li:nth-child(odd) p:last-child');//参数对应的位置
        var length = p.length;
        var data;
        if(ret && ret.data){
            data = ret.data[0];
        }
        if (length == 4){//不是新疆
            p.each(function(index,item){
                switch(index){
                    case 0:
                        $(item).html(isNull(data)?'-':data.totalSuccessCount);
                        break;
                    case 1:
                        $(item).html(isNull(data)?'-':data.totalSuccessCapacity);
                        break;
                    case 2:
                        $(item).html(isNull(data)?'-':data.totalSuccessMoney);
                        break;
                    case 3:
                        $(item).html(isNull(data)?'-':data.successRate);
                        break;

                }
            });
        }else if(length == 3){//新疆
            p.each(function(index,item){
                switch(index){
                    case 0:
                        $(item).html(isNull(data)?'-':data.totalSuccessCount);
                        break;
                    case 1:
                        $(item).html(isNull(data)?'-':data.totalSuccessCapacity);
                        break;
                    case 2:
                        $(item).html(isNull(data)?'-':data.successRate);
                        break;
                }
            });
        }
    });
}
//折线图和柱状图公共数据
var renderLineData = {
    title: {
        text: ''
    },
    tooltip: {
        trigger: 'axis'
    },
    grid: {
        left: '80px',
        right: '80px'
    },
    xAxis: [
        {
            type: 'category',
            boundaryGap: false,
            data: [],
            splitLine: {
                lineStyle: {color: '#eee'}
            }
        }
    ],
    yAxis: [
        {
            name:'',
            type: 'value',
            splitLine: {
                lineStyle: {color: '#eee'}
            }
        }
    ],
    toolbox: {
        show: true,
        feature: {
            saveAsImage: {show: true}
        }
    },
    noDataLoadingOption:{
        text: '暂无数据',
        effect: 'bubble',
        effectOption: {
            effect: {
                n: 0
            }
        }
    },
    series: [
        {
            name: '',
            type: '',
            data: [],
            itemStyle: {
                normal: {    //图形颜色
                    color: '#ff6600',
                    label: {
                        show: true,   //显示文本
                        position: 'right',  //数据值位置
                        textStyle: {
                            color: '#000',
                            fontSize: '13'
                        }
                    }
                }
            }
        }
    ]
};
//饼图公共数据
var renderPieData = {
    title : {
        text: '',
        x:'center'
    },
    tooltip : {
        trigger: 'item',
        formatter: '{a} <br/>{b} : {c} ({d}%)'
    },
    toolbox: {
        show : true,
        feature : {
            saveAsImage : {show: true}
        }
    },
    noDataLoadingOption:{
        text: '暂无数据',
        effect: 'bubble',
        effectOption: {
            effect: {
                n: 0
            }
        }
    },
    calculable : false,
    series : [
        {
            name:'',
            type:'pie',
            radius : '55%',
            center: ['50%', '60%'],
            itemStyle : {
                normal: {
                    label: {
                        position: 'inner'
                    },
                    labelLine:{
                        show: false
                    }
                }
            },
            data:[]
        }
    ]
};

var renderPiesData = {
    tooltip : {
        trigger: 'axis'
    },
    toolbox: {
        show : true,
        feature : {
            saveAsImage : {show: true}
        }
    },
    calculable : false,
    xAxis : [
        {
            type : 'category',
            splitLine : {show : false},
            data : []
        }
    ],
    yAxis : [
        {
            type : 'value',
            position: 'left'
        }
    ],
    noDataLoadingOption:{
        text: '暂无数据',
        effect: 'bubble',
        effectOption: {
            effect: {
                n: 0
            }
        }
    },
    series : []
};

function addObj(oldObj,newObj){
    for(var key in newObj){
        oldObj[key] = newObj[key];
    }
}
//折线、柱状图渲染,example
function renderLine(chartArea,url,params){
    ajaxData(url, params, function (ret) {
        if(!ret){
            return false;
        }else{
            var renderData =  JSON.parse(JSON.stringify(renderLineData));
            //以下是折线图的数据，后面大括号里的内容是后台返回的数据；
             addObj(renderData.xAxis[0],{data: ret.xAxisNames});
             addObj(renderData.yAxis[0],{name:ret.unit});
             addObj(renderData.series[0],{type:'line', data:ret.xAxisValues});
            //以下是横向柱状图的数据，后面大括号里的内容是后台返回的数据；
            //Axis的type：坐标轴数值类型，1)category:类目，2)value：数值;
            // series的type：1)bar：柱状图 2)line:折线图
            addObj(renderData.xAxis[0],{name:'单位(个)',boundaryGap:[0, 0.01],type:'value'});
             addObj(renderData.yAxis[0],{type:'category',data:['接口调用', '赠送', '红包', '砸金蛋', '转盘']});
             addObj(renderData.series[0],{name:'',type:'bar', barWidth: 30, data:[19325, 23438, 31000, 121594, 134141]});

            //以下是纵向柱状图的数据，后面大括号里的内容是后台返回的数据；
             addObj(renderData.xAxis[0],{type:'category',boundaryGap:true, data:['日照', '济南', '红包', '砸金蛋', '转盘']});
             addObj(renderData.yAxis[0],{name:'个',type:'value',boundaryGap:[0, 0.01]});
             addObj(renderData.series[0],{name:'',type:'bar',barWidth: 30, data:[19325, 23438, 31000, 121594, 134141]});
            renderData.series[0].itemStyle.normal.label.position= 'top';//标注的字显示的位置
            chartArea.setOption(renderData,true);
        }

    });
}
//折线,真实版
function renderLineChart(chartArea,url,params){
    ajaxData(url, params, function (ret) {
        if(isEmptyObject(ret)){
            return false;
        }else{
        	chartArea.hideLoading();
            var renderData =  JSON.parse(JSON.stringify(renderLineData));
            //以下是折线图的数据，后面大括号里的内容是后台返回的数据；
             addObj(renderData.xAxis[0],{data: ret.xAxisNames});
             addObj(renderData.yAxis[0],{name: ret.unit});
             addObj(renderData.series[0],{type:'line', data:ret.xAxisValues});
            renderData.series[0].itemStyle.normal.label.position= 'top';//标注的字显示的位置
            chartArea.setOption(renderData,true);
        }

    });
}

//纵向柱状图
function renderHorizontalChart(chartArea,url,params){
	ajaxData(url, params, function (ret) {
        if(isEmptyObject(ret)){
            return false;
        }else{
        	chartArea.hideLoading();
            var renderData =  JSON.parse(JSON.stringify(renderLineData));
            
            addObj(renderData.xAxis[0],{name:'单位('+ret.unit+')',boundaryGap:[0, 0.01],type:'value'});
            addObj(renderData.yAxis[0],{type:'category',data:ret.xAxisNames});
            addObj(renderData.series[0],{name:'',type:'bar', barWidth: 30, data:ret.xAxisValues});
            
            
            renderData.series[0].itemStyle.normal.label.position= 'top';//标注的字显示的位置
            chartArea.setOption(renderData,true);
        }

    });
}
//横向柱状图
function renderverticalChart(chartArea,url,params){
	ajaxData(url, params, function (ret) {
        if(isEmptyObject(ret)){
            return false;
        }else{
        	chartArea.hideLoading();
            var renderData =  JSON.parse(JSON.stringify(renderLineData));
            //以下是折线图的数据，后面大括号里的内容是后台返回的数据；
            addObj(renderData.xAxis[0],{name:ret.unit,boundaryGap:[0, 0.01],type:'value'});
            addObj(renderData.yAxis[0],{type:'category',data:ret.xAxisNames});
            addObj(renderData.series[0],{name:'',type:'bar', barWidth: 30, data:ret.xAxisValues});
            renderData.series[0].itemStyle.normal.label.position= 'top';//标注的字显示的位置
            chartArea.setOption(renderData,true);
        }

    });
}


//纵向柱状图
function renderVerticalChart(chartArea,url,params){
	ajaxData(url, params, function (ret) {
      if(isEmptyObject(ret)){
          return false;
      }else{
      	chartArea.hideLoading();
          var renderData =  JSON.parse(JSON.stringify(renderLineData));
          //以下是折线图的数据，后面大括号里的内容是后台返回的数据；
          
          addObj(renderData.xAxis[0],{name:ret.unit,boundaryGap:[0, 0.01],type:'value'});
          addObj(renderData.yAxis[0],{type:'category',data:ret.xAxisNames});
          addObj(renderData.series[0],{name:'',type:'bar', barWidth: 30, data:ret.xAxisValues});
         renderData.series[0].itemStyle.normal.label.position= 'top';//标注的字显示的位置
         chartArea.setOption(renderData,true);
      }

  });
}

//单个饼图渲染,examle
function renderPie(chartArea,url,params){
    ajaxData(url, params, function (ret) {
        if(!ret){
            return false;
        }else{
        	chartArea.hideLoading();
            var renderData =  JSON.parse(JSON.stringify(renderPieData));
            addObj(renderData.series[0],{data:[
                {value:335, name:'70M'},
                {value:310, name:'500M'},
                {value:234, name:'100M'},
                {value:135, name:'300M'},
                {value:1548, name:'1G'}
            ]});
            renderData.series[0].itemStyle.normal.label.formatter = function (params) {
                return params.name + ' ' + (params.percent - 0).toFixed(0) + '%'
            };
            chartArea.setOption(renderData);
        }
    });
}

//单个饼图渲染,real
function renderPieReal(chartArea,url,params){
    ajaxData(url, params, function (ret) {
        if(isEmptyObject(ret)){
            return false;
        }else{
        	chartArea.hideLoading();
            var renderData =  JSON.parse(JSON.stringify(renderPieData));
            addObj(renderData.series[0],{data:ret.datas});
            renderData.series[0].itemStyle.normal.label.formatter = function (params) {
                return params.name + ' ' + (params.percent - 0).toFixed(0) + '%'
            };
            chartArea.setOption(renderData,true);
        }
    });
}

//多个饼图渲染
function renderPiesReal(chartArea, url,params){
    ajaxData(url, params, function (ret) {
        if(!ret.titles.length){
            var renderData = JSON.parse(JSON.stringify(renderPiesData));
            var series_item = {
                name:'',
                type:'pie',
                tooltip : {
                    trigger: 'item',
                    formatter: '{a} <br/>{b} : {c} ({d}%)'
                },
                center: ['','50%'],
                radius : '40%',
                itemStyle :{
                    normal : {
                        label: {
                            position: 'inner',
                            formatter: function (params) {
                                return params.name
                            }
                        },
                        labelLine : {
                            show: false
                        }
                    }
                },
                data:[]
            };
            renderData.series.push(series_item);
            chartArea.setOption(renderData,true);
            return false;
        }else {
            chartArea.hideLoading();
            var renderData = JSON.parse(JSON.stringify(renderPiesData));
            var titles = ret.titles;
            var dataArr = ret.datas;
            var pos = 20;
            for (var i = 0; i < dataArr.length; i++) {
                addObj(renderData.xAxis[0],{data:titles});
                var series_item = {
                    name:'',
                    type:'pie',
                    tooltip : {
                        trigger: 'item',
                        formatter: '{a} <br/>{b} : {c} ({d}%)'
                    },
                    center: ['','50%'],
                    radius : '40%',
                    itemStyle : {
                        normal : {
                            label: {
                                position: 'inner',
                                formatter: function (params) {
                                    return params.name
                                }
                            },
                            labelLine : {
                                show: false
                            }
                        }
                    },
                    data:[]
                };
                series_item.name = titles[i];
                series_item.data = dataArr[i];
                renderData.series.push(series_item);
                if(dataArr.length == 5 || dataArr.length == 4 || dataArr.length == 3){
                    if(i == 0){
                        pos = 20;
                    }else{
                        pos = pos + 60 / (dataArr.length - 1);
                    }
                    renderData.series[i].center[0] = pos + '%';
                }else if(dataArr.length == 2){
                    if(i == 0){
                        pos = 30;
                    }else{
                        pos = pos + 40 / (dataArr.length - 1);
                    }
                    renderData.series[i].center[0] = pos + '%';
                }else{
                    pos = 50;
                    renderData.series[i].center[0] = pos + '%';
                }
            }
            chartArea.setOption(renderData,true);
        }
    });
}

function myContainer(name){
    var contents = $('#'+name+' .analysis-items li .chart');
    var oWidth = document.body.offsetWidth / 2 - 80 + 'px';
    for (var i = 0;i < contents.length; i++){
        contents[i].style.width = oWidth;
    }
    if(name == 'region'){
    	if(!isXinJiang){
    		var chart_package = $('#region .chart_package');
    	}
    	if(isXinJiang || isShanDong){
            var chart_pool = $('#region .chart_pool');
    	}
        var eWidth = (document.body.offsetWidth - 100) + 'px';
	        if(!isXinJiang){
	            chart_package[0].style.width = eWidth;
	        }
	    	if(isXinJiang || isShanDong){
	    		chart_pool[0].style.width = eWidth;
	    	}
            
    }

    if(name == 'production'){
        var chart_distribution = $('#production .chart_distribution');
        var aWidth = (document.body.offsetWidth  - 100) + 'px';
        chart_distribution[0].style.width = aWidth;
    }
}

function showBefore(name){
    myContainer(name);
    switch(name){
        case 'platform':
            $(window).resize(chart_count_platform.resize).trigger('resize');
            $(window).resize(chart_total_platform.resize).trigger('resize');
            $(window).resize(chart_type_platform.resize).trigger('resize');
            $(window).resize(chart_success_platform.resize).trigger('resize');
            if(!isXinJiang) {
                $(window).resize(chart_amount_platform.resize).trigger('resize');
            }
            break;
        case 'enterprise':
            $(window).resize(chart_count_enterprise.resize).trigger('resize');
            $(window).resize(chart_rank_enterprise.resize).trigger('resize');
            if(!isXinJiang){
                $(window).resize(chart_money_enterprise.resize).trigger('resize');
            }
            $(window).resize(chart_distribution_enterprise.resize).trigger('resize');
            $(window).resize(chart_type_enterprise.resize).trigger('resize');
            if(isPlus){
                $(window).resize(chart_profit_enterprise.resize).trigger('resize');
            }
            break;
        case 'region':
            $(window).resize(chart_rank_region.resize).trigger('resize');
            if(!isXinJiang){
            	$(window).resize(chart_package_region.resize).trigger('resize');
            }
            if(isXinJiang || isShanDong){
                $(window).resize(chart_pool_region.resize).trigger('resize');
            }
            if(!isXinJiang){
                $(window).resize(chart_price_region.resize).trigger('resize');
            }
            break;
        case 'production':
            $(window).resize(chart_count_production.resize).trigger('resize');
            $(window).resize(chart_total_production.resize).trigger('resize');
            $(window).resize(chart_distribution_production.resize).trigger('resize');
            break;
    }
}

function listeners() {
    $(window).resize(function () {
        var name = $("a","li[role='presentation'].active").attr("href").substr(1);
        myContainer(name);
        switch (name) {
            case 'platform':
                chart_count_platform.resize();
                chart_total_platform.resize();
                chart_type_platform.resize();
                chart_success_platform.resize();
                if(!isXinJiang) {
                    chart_amount_platform.resize();
                }
                break;
            case 'enterprise':
                chart_count_enterprise.resize();
                chart_rank_enterprise.resize();
                if(!isXinJiang){
                    chart_money_enterprise.resize();
                }
                chart_distribution_enterprise.resize();
                chart_type_enterprise.resize();
                if(isPlus){
                    chart_profit_enterprise.resize();
                }
                break;
            case 'region':
                chart_rank_region.resize();
                if(!isXinJiang){
                	chart_package_region.resize();
                }
                if(isXinJiang || isShanDong){
                	chart_pool_region.resize();
                }               
                if(!isXinJiang){
                    chart_price_region.resize();
                }
                break;
            case 'production':
                chart_count_production.resize();
                chart_total_production.resize();
                chart_distribution_production.resize();
                break;
        }
    });

    $('a[data-toggle="tab"]').on("show.bs.tab",function(){
        var module = $(this).attr('href').substr(1);
        showBefore(module);
    });

    $('a','li[role="presentation"]').on('click',function(){
        var module = $(this).attr('href').substr(1);
        switch(module){
            case 'platform':
                renderPlatform();
                break;
            case 'enterprise':
                renderEnterprise();
                break;
            case 'region':
                renderRegion();
                break;
            case 'production':
                renderProduction();
                break;
        }
    });

    $('#platform-search-btn').on('click',function(){
        renderPlatform();
    });

    $('#enterprise-search-btn').on('click',function(){
        renderEnterprise();
    });

    $('#region-search-btn').on('click',function(){
        renderRegion();
    });

    $('#production-search-btn').on('click',function(){
        renderProduction();
    });

    $('.dropdown-menu.timeList').on('click', 'a:not(.customRange)', function () {
        var now = new Date(new Date().getTime() - 86400000);//截止时间为昨天
        var nowadays = now.getFullYear() + '-' + (now.getMonth() + 1) + '-' + now.getDate();
        var before7days = new Date(now);
        before7days.setDate(now.getDate() - 6);
        before7days =
            before7days.getFullYear() + '-' + (before7days.getMonth() + 1) + '-'
            + before7days.getDate();
        var before30days = new Date(now);
        before30days.setDate(now.getDate() - 29);
        before30days =
            before30days.getFullYear() + '-' + (before30days.getMonth() + 1) + '-'
            + before30days.getDate();

        var finalDays;
        if ($(this).parents('li').attr('data-value') == '1') {
            finalDays = before7days;
        } else if ($(this).parents('li').attr('data-value') == '2') {
            finalDays = before30days;
        }

        $('.btn', $(this).parents('.btn-group')).eq(0).html('<span>' + finalDays + '</span>~<span>' + nowadays + '</span>');
        $('input', $(this).parents('.btn-group')).eq(0).val(finalDays).blur();
        $('input', $(this).parents('.btn-group')).eq(1).val(nowadays).blur();
    });
}

function initSearchDateRangePicker(name) {
    var ele = $('#'+name+'-search-time-range');
    ele.dateRangePicker({
        separator: ' ~ ',
        showShortcuts: true,
        shortcuts: {
            'prev': ['week', 'month']
        },
        maxDays: 90,
        beforeShowDay: function (t) {
            var valid = t.getTime() < new Date().getTime() - 86400000;
            return [valid, '', ''];
        },
        setValue: function (s, s1, s2) {
            $('.btn', ele.parents('.btn-group')).eq(0).html('<span>' + s1 + '</span>~<span>' + s2 + '</span>');
            $('input', $(this).parents('.btn-group')).eq(0).val(s1).blur();
            $('input', $(this).parents('.btn-group')).eq(1).val(s2).blur();
        }
    }).bind('datepicker-closed', function () {
        /* This event will be triggered after date range picker close animation */
        // $('#search-btn').click();
    });
}
