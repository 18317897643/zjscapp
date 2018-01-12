$(function () {
    //获取url中的参数
    var id;
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
        id = theRequest.id;
        $.get('/zjapp/v1/TitleManager/getProtocol?id='+ id ,function (res) {
            if( res.error_code == 0){
                $('.src').html( res.data.createtime +'<span style="margin-left:7px;">众健商城</span>' );
                $('title').html( res.data.title );
                $('.title').html( res.data.title );
                $('.container').html( res.data.memo );
            }
        })
    }
});