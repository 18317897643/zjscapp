(function (doc, win) {
    var docEl = doc.documentElement,
        resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
        recalc = function () {
            var clientWidth = docEl.clientWidth;
            if (!clientWidth) return;
            if(clientWidth >= 750){
                docEl.style.fontSize = '100px';
            }else{
                docEl.style.fontSize = 100 * (clientWidth / 750) + 'px';
            }
        };
    if (!doc.addEventListener) return;
    win.addEventListener(resizeEvt, recalc, false);
    doc.addEventListener('DOMContentLoaded', recalc, false);
})(document, window);

var calUtil = {
    //当前日历显示的年份
    showYear:2017,
    //当前日历显示的月份
    showMonth:1,
    //当前日历显示的天数
    showDays:1,
    eventName:"load",
    //初始化日历
    init:function(signList){
        calUtil.setMonthAndDay();
        calUtil.draw(signList);
        //calUtil.bindEnvent();
    },
    draw:function(signList){
        //绑定日历
        var str = calUtil.drawCal(calUtil.showYear,calUtil.showMonth,signList);
        $(".calendarParent").html(str);
        //绑定日历表头
        var calendarName=calUtil.showYear+"年"+calUtil.showMonth+"月";
        $(".calendar_month_span").html(calendarName);
    },
    //绑定事件
    /*bindEnvent:function(){
        //绑定上个月事件
        $(".calendar_month_prev").click(function(){
            //ajax获取日历json数据
            var signList=[{"signDay":"10"},{"signDay":"11"},{"signDay":"12"},{"signDay":"13"}];
            calUtil.eventName="prev";
            calUtil.init(signList);
        });
        //绑定下个月事件
        $(".calendar_month_next").click(function(){
            //ajax获取日历json数据
            var signList=[{"signDay":"10"},{"signDay":"11"},{"signDay":"12"},{"signDay":"13"}];
            calUtil.eventName="next";
            calUtil.init(signList);
        });
    },*/
    //获取当前选择的年月
    setMonthAndDay:function(){
        switch(calUtil.eventName)
        {
            case "load":
                var current = new Date();
                calUtil.showYear=current.getFullYear();
                calUtil.showMonth=current.getMonth() + 1;
                break;
            case "prev":
                var nowMonth=$(".calendar_month_span").html().split("年")[1].split("月")[0];
                calUtil.showMonth=parseInt(nowMonth)-1;
                if(calUtil.showMonth==0)
                {
                    calUtil.showMonth=12;
                    calUtil.showYear-=1;
                }
                break;
            case "next":
                var nowMonth=$(".calendar_month_span").html().split("年")[1].split("月")[0];
                calUtil.showMonth=parseInt(nowMonth)+1;
                if(calUtil.showMonth==13)
                {
                    calUtil.showMonth=1;
                    calUtil.showYear+=1;
                }
                break;
        }
    },
    getDaysInmonth : function(iMonth, iYear){
        var dPrevDate = new Date(iYear, iMonth, 0);
        return dPrevDate.getDate();
    },
    bulidCal : function(iYear, iMonth) {
        var aMonth = new Array();
        aMonth[0] = new Array(7);
        aMonth[1] = new Array(7);
        aMonth[2] = new Array(7);
        aMonth[3] = new Array(7);
        aMonth[4] = new Array(7);
        aMonth[5] = new Array(7);
        aMonth[6] = new Array(7);
        var dCalDate = new Date(iYear, iMonth - 1, 1);
        var iDayOfFirst = dCalDate.getDay();
        var iDaysInMonth = calUtil.getDaysInmonth(iMonth, iYear);
        var iVarDate = 1;
        var d, w;
        aMonth[0][0] = "日";
        aMonth[0][1] = "一";
        aMonth[0][2] = "二";
        aMonth[0][3] = "三";
        aMonth[0][4] = "四";
        aMonth[0][5] = "五";
        aMonth[0][6] = "六";
        for (d = iDayOfFirst; d < 7; d++) {
            aMonth[1][d] = iVarDate;
            iVarDate++;
        }
        for (w = 2; w < 7; w++) {
            for (d = 0; d < 7; d++) {
                if (iVarDate <= iDaysInMonth) {
                    aMonth[w][d] = iVarDate;
                    iVarDate++;
                }
            }
        }
        return aMonth;
    },
    ifHasSigned : function(signList,day){
        var signed = false;
        $.each(signList,function(index,item){
            if(item.signDay == day) {
                signed = true;
                return false;
            }
        });
        return signed;
    },
    drawCal : function(iYear, iMonth ,signList) {
        var myMonth = calUtil.bulidCal(iYear, iMonth);
        var htmls = new Array();
        htmls.push("<div class='sign_main' id='sign_layer'>");
        htmls.push("<div class='sign_succ_calendar_title'>");
        // htmls.push("<div class='calendar_month_next'>下月</div>");
        //htmls.push("<div class='calendar_month_prev'>上月</div>");
        htmls.push("<div class='calendar_month_span'></div>");
        htmls.push("</div>");
        htmls.push("<div class='sign' id='sign_cal'>");
        htmls.push("<ul class='calendarTitle'>");
        htmls.push("<li>" + myMonth[0][0] + "</li>");
        htmls.push("<li>" + myMonth[0][1] + "</li>");
        htmls.push("<li>" + myMonth[0][2] + "</li>");
        htmls.push("<li>" + myMonth[0][3] + "</li>");
        htmls.push("<li>" + myMonth[0][4] + "</li>");
        htmls.push("<li>" + myMonth[0][5] + "</li>");
        htmls.push("<li>" + myMonth[0][6] + "</li>");
        htmls.push("</ul>");
        var d, w;
        for (w = 1; w < 6; w++) {
            htmls.push("<ul>");
            for (d = 0; d < 7; d++) {
                var ifHasSigned = calUtil.ifHasSigned(signList,myMonth[w][d]);
                if(ifHasSigned){
                    htmls.push("<li class='on'>" + (!isNaN(myMonth[w][d]) ? myMonth[w][d] : " ") + "</li>");
                } else {
                    htmls.push("<li>" + (!isNaN(myMonth[w][d]) ? myMonth[w][d] : " ") + "</li>");
                }
            }
            htmls.push("</ul>");
        }
        htmls.push("</div>");
            htmls.push("</div>");
        return htmls.join('');
    }
};
$(function(){
    //获取url中的参数
    var token;
    var url = location.search;
    var theRequest = new Object();
    if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        strs = str.split("&");
        for (var i = 0; i < strs.length; i++) {
            theRequest[strs[i].split("=")[0]] = (strs[i].split("=")[1]);
        }
    }
    if(theRequest.token != ""){
        token = theRequest.token;
    }
    //console.log(token);
    //token = "6ffdc8f9-3d9c-4c10-85f7-9e6199d39f16";
    //签到
    $(".signButton").on('click',function () {
       $.post("/zjapp/v1/SignIn/Signing/" + token,function (res) {
            if(res.error_code==0) {
                $(".bg-model").css("display", "block");
            }else if(res.error_code==1){
                $('.signButton').css("background","url(../image/alSignButton.png)");
                $('.signButton').css("background-size","1.92rem 0.54rem");
                $('.signButton').css("cursor","default");
                $('.signButton').css("pointer-events","none");
            }else {
                alert("签到失败,请重新签到！");
            }
        })
    });
    $(".closeBtn").on('click',function () {
        $(".bg-model").css("display","none");
        window.location.reload();
    });
    $(".closeBtn1").on('click',function () {
        $(".bg-model-1").css("display","none");
        window.location.reload();
    });
    //ajax获取日历json数据
    $.ajax({
        type: 'get',
        url: '/zjapp/v1/SignIn/init/' + token,
        data: {},
        dataType: 'json',
        success: doSign
    });
    function doSign(res) {
        var signList = [];
        var currentDate = res.data.currentDate;
        var year = currentDate.substring(0, 4);
        var month = currentDate.substring(5, 7);
        var day = new Date(year,month,0);
        $('.lastData').html(day.getDate());
        for(var i = 0; i < res.data.signData.length; i++){
            var a = {};
            if (res.data.signData[i] == currentDate ){
                $('.signButton').css("background","url(../image/alSignButton.png)");
                $('.signButton').css("background-size","1.92rem 0.54rem");
                $('.signButton').css("cursor","default");
                $('.signButton').css("pointer-events","none");
            }
            a.signDay = res.data.signData[i].substring( res.data.signData[i].length - 2 );
            signList.push(a);
        }
        calUtil.init(signList);
        if(res.data.signinAward.sevendaysaward == 1){
            $(".sevendaysAward").css("background","url(../image/getButton.png)");
            $('.sevendaysAward').css("cursor","pointer");
            $('.sevendaysAward').css("background-size","1.44rem 0.5rem");
            $('.sevendaysAward').css("pointer-events","auto");
        }
        if(res.data.signinAward.fourteendaysaward == 1){
            $(".fourteendaysAward").css("background","url(../image/getButton.png)");
            $('.fourteendaysAward').css("cursor","pointer");
            $('.fourteendaysAward').css("background-size","1.44rem 0.5rem");
            $('.fourteendaysAward').css("pointer-events","auto");
        }
        if(res.data.signinAward.thirtydaysaward == 1){
            $(".thirtydaysAward").css("background","url(../image/getButton.png)");
            $('.thirtydaysAward').css("cursor","pointer");
            $('.thirtydaysAward').css("background-size","1.44rem 0.5rem");
            $('.thirtydaysAward').css("pointer-events","auto");
        }
    };
    //获取奖励
    $('.thirtydaysAward').on('click',function () {
        $.post('/zjapp/v1/SignIn/drawAward/' + token + '?awardType=thirty',function (res) {
            if( res.error_code == 1 ){
            }else if( res.error_code == 0 ){
                var reward = $('.thirtydaysAward').prev().text().substring(2, 4);
                $(".bg-model-1").css("display", "block");
                $(".bg-model-1 .bg-model-content span").html('+'+reward+'元');
            }else{
            }
        })
    });
    $('.sevendaysAward').on('click',function () {
        $.post('/zjapp/v1/SignIn/drawAward/' + token + '?awardType=seven',function (res) {
            if( res.error_code == 1 ){
            }else if( res.error_code == 0 ){
                var reward = $('.sevendaysAward').prev().text().substring(2, 4);
                $(".bg-model-1").css("display", "block");
                $(".bg-model-1 .bg-model-content span").html('+'+reward+'元');
            }else {
            }
        })
    });
    $('.fourteendaysAward').on('click',function () {
        $.post('/zjapp/v1/SignIn/drawAward/' + token + '?awardType=fourteen',function (res) {
            if( res.error_code == 1 ){
            }else if( res.error_code == 0 ){
                var reward = $('.fourteendaysAward').prev().text().substring(2, 4);
                $(".bg-model-1").css("display", "block");
                $(".bg-model-1 .bg-model-content span").html('+'+reward+'元');
            }else{
            }
        })
    });
});


