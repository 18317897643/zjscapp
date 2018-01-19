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
$(function() {
    $('.waterPurifierText').bind('input propertychange',function () {
        if( $(this).val() == "" ){
            $('.waterPurifierBtn').css('background-color','silver');
        }else {
            $('.waterPurifierBtn').css('background-color','#5a97fd');
        }
    });
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
        $('.waterPurifierBtn').on('click',function () {
            $.post('/zjapp/v1/waterPurifier/drawCupon/' + token ,{codeNo: $('.waterPurifierText').val()}, function (res) {
                if ( res.error_code == 0 ){
                    alert("领取成功！");
                    setTimeout('$(".tip").fadeOut()', 1500);
                }else if ( res.error_code == 1 ){
                    alert("请使用正确的兑换码！");
                }else {
                    alert("无效兑换码！");
                }
            });
        })
    }
});