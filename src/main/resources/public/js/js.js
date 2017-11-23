function checked() {
    var div = $("#divcheck");
    if (div.hasClass("divchecked")) {
        div.removeClass("divchecked").addClass("divcheckbox");
    }
    else {
        div.removeClass("divcheckbox").addClass("divchecked");
    }
}
var timeFlag = 60;
var timeHandler;
function timeFun() {
    timeFlag--;
    $('#sendVer').html(timeFlag + '秒后可重发');

    if (timeFlag <= 0) {
        clearInterval(timeHandler);
        $('#sendVer').html('发送验证码');
        //$('#sendVer').removeClass('gray');

    }
}


/*获取请求参数*/
function GetQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}

/*隐藏和启用按钮*/
function hideload() {
    $("#loadgif").hide();
    $("#btnenter").attr('disabled', false);
}

function hideloadgetcode() {
    $("#loadgif").hide();
    $("#btngetcode").attr('disabled', false);
}



/*隐藏加载动画*/
function onload() { $("#loadgif").hide(); }
/*跳转应用商店下载*/
function jumpDown() {
    if (!!navigator.userAgent.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/)) {
        window.location = "http://itunes.apple.com/app/id1244107665";
    } else if (navigator.userAgent.indexOf("Android" > -1) || navigator.userAgent.indexOf("Adr" > -1)) {
        window.location = "http://sj.qq.com/myapp/detail.htm?apkName=com.hykj.distributioncenter";
    } else {
        window.location = "http://www.365hy.com/";
    }
}

/* 提交注册*/
function reguser() {
    $("#btnenter").attr('disabled', true);
    var usercode = GetQueryString("usercode");
    var phone = $("#tbphone").val();
    var newpassword = $("#tbpassword").val();
    var surepassword = $("#tbsurepassword").val();
    var tbcode = $("#tbcode").val();
    if (tbcode.length > 0) {
        if (surepassword.length > 0 && newpassword.length > 0) {
            if (newpassword.length > 5 && surepassword.length > 5) {
                if (newpassword == surepassword) {
                    $("#loadgif").show();
                    $.ajax({
                        url: "/zhongjian/LoginAndRegister/RegisterUser",
                        type: "post",
                        contentType: "application/json;charset=utf-8",
                        data: JSON.stringify({'phoneNum': phone,'password': surepassword,'verifyCode': tbcode,'inviteCode': usercode}),
                        datatype: "json",
                        cache: false,
                        success: function (data) {
                            if (data.error_code == 0) {
                                hideload();
                                alert('恭喜您注册成功');
                                jumpDown();
                            }
                            else {
                                hideload();
                                alert(data.error_message);
                            }
                        }
                    });
                }
                else {
                    hideload();
                    alert("2次密码不一致");
                }
            }
            else {
                hideload();
                alert("密码必须大于等于6位");
            }
        }
        else {
            hideload();
            alert("请输入密码");
        }
    }
    else {
        hideload();
        alert("请输入验证码");
    }
}

/* 获取验证码*/
function sendcode() {
    $("#btngetcode").attr('disabled', true);
    var phone = $("#tbphone").val();
    var che = $('#weuiAgree').prop("checked");
    if (timeFlag != 60) return;
    if (che == true) {
     if (phone.length == 11) {
         $("#loadgif").show();
         $.ajax({
             async: true,
             cache: false,
             url: "/zhongjian/LoginAndRegister/SendRegisterVerifyCode",
             contentType: "application/json;charset=utf-8",
             type: "post",
             data: JSON.stringify({'phoneNum': phone,}),
             datatype: "json",
             success: function (data) {
                 if (data.error_code == 0) {
                     timeHandler = setInterval(timeFun, 1000);
                     hideloadgetcode();
          
                 }
                 else {
                     hideloadgetcode();
                     alert(data.error_message);
                 }
             }
         });
     }
     else {
         hideloadgetcode();
         alert("请输入正确的手机号");
     }
 }
 else {
     hideloadgetcode();
     alert("必须同意用户协议");
 }
}
