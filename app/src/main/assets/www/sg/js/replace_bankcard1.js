/**
 * 
 */
$(function(){
	 //setCookie("token",'6ZsC86310m513v10Nk2N32ayG356w084')  ;
	  //setCookie("secretKey",'4fec776b2a084c2283727424bea01150');
	//alert("yes");alert(getCookie("token"));alert(getCookie("secretKey"));
    $.ajax({
        type: 'post',
        url : getCookie("ip")+"/userMessage/getUserInfo",//获取用户真实信息
        data:{
        	 token:getCookie("token"),
             secretKey:getCookie("secretKey")
        },
        dataType: 'json',
        success: function(data){
        	//alert(data.date.idNumber);
        	//alert(data.date.name+"1");
        	if(data.date.name=='null'){
        		//alert('name');
        		$("#has_card_people").attr("value",'');
        	}
        	else{
        		$("#has_card_people").attr("value",data.date.name);
        	}
        	
        	setCookie("oldtel_phone",data.date.loginName);
        	setCookie("id_number",data.date.idNumber);
        	//$(".customer_peopleid").html(data.date.idNumber);
        },
        error: function(xhr, type){
        	//window.location.href = 'account_accredit';
            alert("error");
        }
    });
});
	

