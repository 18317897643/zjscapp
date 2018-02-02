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
            $('.close').css('display','none');
        }else {
            $('.waterPurifierBtn').css('background-color','#5a97fd');
            $('.close').css('display','block');
        }
    });
    $('.close').on('click',function () {
        $('.waterPurifierText').val("");
        $('.close').css('display','none');
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
                    alert(res.error_message);
                }else {
                    alert(res.error_message);
                }
            });
        })
    }
});