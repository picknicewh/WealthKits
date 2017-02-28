/**
 * Created by cxj on 2016/1/11.
 */
window.onload = function(){
    //alert("load");
	//getbankname();
	// setCookie("token",'ydM281F1d9Y9PWzoI0Kr6mD46sL23AO4');
	// setCookie("secretKey",'f9376852719c34b87d6056fb5f925e15');
    //alert(bank_s[0].length);
    // alert(parm);
    $('.neck').hide();
    var bottomHtml =  '<div class="bottom">基金交易服务由上海长量基金销售投资顾问有限公司提供</br>资格证书编号：000000306 </div>';
    $('body').append(bottomHtml);
	$("#bankName").attr("value",getCookie("bankName"));
};

function validate_num_click(){
    setCookie("tel_phone",$("#tel_phone").val());
sendWait(60);
//alert(getCookie("secretKey")+"11111");
    console.log(getCookie("secretKey")+getCookie("token")+'bbbbbbbbbbbbbbbb');
    $.ajax({
        type: 'post',
        url : getCookie("ip")+"/sg/shortMessageSending",//验证码
        data:{
            secretKey:getCookie("secretKey"),
            token:getCookie("token"),
            loginName:getCookie("tel_phone"),
            purpose:'01'
        },

        dataType : "json",
        //contentType : "application/json",
        success : function(data) {
           // alert(data.resultCode);
            //sendWait(60);
           // setCookie("resultCode",data.resultCode);
        	
            if(data.resultCode==0){
                alert("发送成功");
                // alertInner("发送成功","确定");
            }
            else{
                alert("发送短信出错");
                // alertInner("发送短信出错","确定");
                //alert(JSON.stringify(data));
                $("#validate_num").attr("disabled",false);
                $("#validate_num").css("background","#FF3B2F");
                $("#validate_num").html('获取验证码');
                //t=null;
                clearTimeout(t);
            }

        },
        error : function() {
            alert("ajax.error");
            //alert("系统异常,加载失败1");
        }
    });

}

function validatenumclick2(){
    setCookie("newtel_phone",$("#tel_phone").val());
sendWait(60);
//alert(getCookie("secretKey"));
    $.ajax({
        type: 'post',
        url : getCookie("ip")+"/sg/shortMessageSending",//验证码
        data:{
            secretKey:getCookie("secretKey"),
            token:getCookie("token"),
            loginName:getCookie("newtel_phone"),
            purpose:'02'
        },

        dataType : "json",
        //contentType : "application/json",
        success : function(data) {
           // alert(data.resultCode);
            //sendWait(60);
           // setCookie("resultCode",data.resultCode);
        	
            if(data.resultCode==0){
                alert("发送成功");
                // alertInner("发送成功","确定");
            }
            else{
                alert("发送短信出错");
                // alertInner("发送成功","确定");
                $("#validate_num").attr("disabled",false);
                $("#validate_num").css("background","#FF3B2F");
                $("#validate_num").html('获取验证码');
                //t=null;
                clearTimeout(t);
            }

        },
        error : function() {
            alert("ajax.error");
            //alert("系统异常,加载失败1");
        }
    });

}

function sendWait(time){
   t = setTimeout(function(){
        //$("#validate_num").attr("disabled",true);
      wait(time);


    },1000);
}
function wait(time){
    $("#validate_num").attr("disabled",true);
    $("#validate_num").css("background","#B9B9B9");
    $("#validate_num").html('还剩'+time+"s");
    if(time==0){
        $("#validate_num").attr("disabled",false);
        $("#validate_num").css("background","#FF3B2F");
        $("#validate_num").html('获取验证码');
        return;
    }
    time--;

    sendWait(time);
}

getbankname = function(){
	var bankType = getCookie("bankType");
$.ajax({
    type: 'GET',
    url: '../json/bank.json',
    dataType: 'json',
    success: function(data){
    	//alert("success");
        for(var i = 0; i < data.banklists.length; i++){
            if(bankType==data.banklists[i].bankType){
                $("#bankName").attr("value",data.banklists[i].bankName);
            }
        }
    },
    error: function(xhr, type){
        alert('Ajax error!!!!');
    }
});
};




