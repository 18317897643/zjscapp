(function (doc, win) {
    var docEl = doc.documentElement,
        resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
        recalc = function () {
            var clientWidth = docEl.clientWidth;
            if (!clientWidth) return;
            if(clientWidth>=750){
                docEl.style.fontSize = '100px';
            }else{
                docEl.style.fontSize = 100 * (clientWidth / 750) + 'px';
            }
        };

    if (!doc.addEventListener) return;
    win.addEventListener(resizeEvt, recalc, false);
    doc.addEventListener('DOMContentLoaded', recalc, false);
})(document, window);
$(function() {
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
    if (theRequest.token != "") {
        token = theRequest.token;
        $('.newpeople-btn').on('click',function () {
            $.post('/zjapp/v1/PersonalCenter/drawNewExclusive/' + token , function (res) {
                if ( res.error_code == 0 ){
                    $('.tip').css('display','block');
                    $('.tip').html('领取成功！');
                    setTimeout('$(".tip").fadeOut()', 1500);
                }else if ( res.error_code == 1 ){
                    $('.tip').css('display','block');
                    $('.tip').html('该红包仅限新人领取');
                    setTimeout('$(".tip").fadeOut()', 1500);
                }else {
                    $('.tip').css('display','block');
                    $('.tip').html('领取失败！');
                    setTimeout('$(".tip").fadeOut()', 1500);
                }
            });
        })
    }else{
        $('.newpeople-btn').on('click',function () {
            $('.tip').css('display','block');
            $('.tip').html('领取失败！请登陆！');
            setTimeout('$(".tip").fadeOut()', 1500);
        })
    }
});