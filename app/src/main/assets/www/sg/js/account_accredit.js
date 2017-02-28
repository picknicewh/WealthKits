/**
 * 
 */
window.onload = function(){
    $.ajax({
        type: 'post',
        url : getCookie("ip")+"/userMessage/getUserInfo",//获取用户真实信息
        data:{
            cardNumber:getCookie("id")
        },
        dataType: 'json',
        success: function(data){
        	alert(JSON.stringify(data));
        	$(".customer_id").html(data.date.loginName);
        	$(".customer_peopleid").html(data.date.idNumber);
            $(".customer_name").html(data.date.name);
            $('#newPhoneVal').html(getCookie('SetNewPhone'));
        },
        error: function(xhr, type){
        	//window.location.href = 'account_accredit';
            alert("error");
        }
    });
};