/**
 * Created by cxj on 2016/1/7.
*/
window.onload = function () {

  //var url = window.location.href;

    $('.neck').hide();
    if(getQueryString('token')){
        setCookie("token",getQueryString('token'));
    }
    if(getQueryString('secretKey')){
        setCookie("secretKey",getQueryString('secretKey'));
    }
    if(getQueryString('phone')){
        setCookie('SetNewPhone',getQueryString('phone'));
    }
//
//    var html = '<div id="loadingBox" style="width: 20%;height: 12%;position: absolute;z-index: 4;top: 40%;left: 40%;border-radius: 10px;">'+
//        '<img src="../images/red-loading.png" style="width: 100%;height: 100%;;border-radius: 10px;" id="loadingPic" />'+
//        '<span style="color: #ff0000;">加载中...</span>'+
//        '</div>';
//    $('body').append(html);
//    var deg = 20;
//
//    setInterval(function(){
//
//        //alert(1);
//        $('#loadingPic').css('transform','rotate('+deg+'deg)');
//        deg+=20;
//    },100);

 // console.log($.md5("111111"));
  // var ip = 'http://192.168.1.200:8080/wealthKits';
  // setCookie("ip",ip);
  var ip="http://zhu.hunme.net:8080/wealthKits"
    setCookie("ip",ip);
    var bottomHtml =  '<div class="bottom">基金交易服务由上海长量基金销售投资顾问有限公司提供</br>资格证书编号：000000306 </div>';
    $('body').append(bottomHtml);
};


function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}
$(function(){
	//alert(getCookie(url));
    $("#name").change(function(){
        if($(this).val()==""){
            $(this).attr("placeholder","姓名不能为空");
            $(".btn").attr("disabled",true);
            $(".btn").css("background","#B9B9B9");
            return;
        }
        else{
            var name = $(this).val();
            var reg = !!name.match(/^[\u4e00-\u9fa5]{2,4}$/);
            if(!reg){
                $(this).attr("value","").attr("placeholder","请输入正确的姓名");
                $(".btn").attr("disabled",true);
                $(".btn").css("background","#B9B9B9");
                return;
            }
           /* if($("#id_card").val()!=''){
                $(".btn").attr("disabled",false);
                $(".btn").css("background","#FF3B2F");*/

            // }
        }
    });
	$('#name').bind('input propertychange', function() {
	    //$('#content').html($(this).val().length + ' characters');
		//alert("*************");
		console.log("********");
		 if($(this).val()==""){
	            $(this).attr("placeholder","姓名不能为空");
	            // $(".btn").attr("disabled",true);
	            // $(".btn").css("background","#B9B9B9");
	            return;
	        }
	        else{
	        	 var name = $(this).val();
	             var reg = !!name.match(/^[\u4e00-\u9fa5]{2,4}$/);
	            if(reg&&$("#id_card").val()!=''){
	            	 $(".btn").attr("disabled",false);
	                 $(".btn").css("background","#FF3B2F");
	            }
	            else{
	            	 $(".btn").attr("disabled",true);
	 	            $(".btn").css("background","#B9B9B9");
	            }
	        }
	});

    $("#id_card").change(function(){
        if($(this).val()==""){
            $(this).attr("placeholder","请输入正确的身份证号码");
            return;
        }
        else{
            var card = $(this).val();
            var Reg = !!card.match(/^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/);
            if(!Reg){
                $(this).attr("value","").attr("placeholder","身份证格式不正确");
                $(".btn").attr("disabled",true);
                $(".btn").css("background","#B9B9B9");
                return;
            }
            /*if($("#name").val()!=''){
                $(".btn").attr("disabled",false);
                $(".btn").css("background","#FF3B2F");

            }*/
        }
    });
    
/*	$('#id_card').bind('input propertychange', function() {
	    //$('#content').html($(this).val().length + ' characters');
		//alert("*************");
		 if($(this).val()==""){
	            $(this).attr("placeholder","请输入正确的身份证号码");
	            return;
	      }else {
	        	var card = $(this).val();
	            var Reg = !!card.match(/^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/);
	            if(!Reg){
	            	
	            /*		$(".btn").attr("disabled",false);
	            		$(".btn").css("background","#FF3B2F");
	            
	            		//alert("未满十八岁不允许开户");
	          
	            	
	                return;
	            }
	            else{*/
/*	            	 $(".btn").attr("disabled",true);
	                 $(".btn").css("background","#B9B9B9");
	            }
	        }
	});*/
	
	
	$('#email_num').change(function() {
	    //$('#content').html($(this).val().length + ' characters');
		//alert("*************");
        if($(this).val()==""){
            $(this).attr("placeholder","请输入邮箱号");
            return;
        }else{
		var card = $(this).val();
        var Reg = !!card.match(/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/);
		//alert(reg);
	          if(!Reg){
	            	 $(this).attr("value","").attr("placeholder","邮箱格式不正确");
	                 $(".btn").attr("disabled",true);
	                 $(".btn").css("background","#B9B9B9");
	            		//alert("未满十八岁不允许开户");

	                
	          }else if(Reg&&$("#name").val()!=''&&$('#id_card').val()!=''){
                     $(".btn").attr("disabled",false);
                     $(".btn").css("background","#FF3B2F");
              }else{
                     $(".btn").attr("disabled",true);
                     $(".btn").css("background","#B9B9B9");
                     return;
              }

                 
	           /* if(Reg&&$("#name").val()!=''&&$('#id_card').val()!=''){
	                $(".btn").attr("disabled",false);
	                $(".btn").css("background","#FF3B2F");

	            }*/
            }

	});

	$('#email_num').bind('input propertychange', function() {
	    //$('#content').html($(this).val().length + ' characters');
		//alert("*************");
		var card = $(this).val();
		var reg = !!card.match( /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/);
		//alert(reg);
	            if(reg&&$("#name").val()!=''&&$('#id_card').val()!=''){

	            		$(".btn").attr("disabled",false);
	            		$(".btn").css("background","#FF3B2F");

	            		//alert("未满十八岁不允许开户");


	                return;
	            }
	            else{
	            	 $(".btn").attr("disabled",true);
	                 $(".btn").css("background","#B9B9B9");
	            }

	});
    //has_card_people

    $("#has_card_people").change(function(){
        if($(this).val()==""){
            $(this).attr("placeholder","持卡人姓名不能为空");
            $(".btn").attr("disabled",true);
            $(".btn").css("background","#B9B9B9");
        }
        else{
            var name = $(this).val();
            var reg = !!name.match(/^[\u4e00-\u9fa5]{2,4}$/);
            if(!reg){
                $(this).attr("value","").attr("placeholder","请输入正确的持卡人姓名");
                $(".btn").attr("disabled",true);
                $(".btn").css("background","#B9B9B9");
                return;
            }
            if($("#bank_id").val()!=''){
                $(".btn").attr("disabled",false);
                $(".btn").css("background","#FF3B2F");
            }

        }
    });
    
    
    $('#has_card_people').bind('input propertychange', function() {
    	if($(this).val()==""){
            $(this).attr("placeholder","持卡人姓名不能为空");
            $(".btn").attr("disabled",true);
            $(".btn").css("background","#B9B9B9");
        }
        else{
            var name = $(this).val();
            var reg = !!name.match(/^[\u4e00-\u9fa5]{2,4}$/);
            if(reg&&$("#bank_id").val()!=''){
            	 $(".btn").attr("disabled",false);
                 $(".btn").css("background","#FF3B2F");
                return;
            }
            else{
            	$(".btn").attr("disabled",true);
                $(".btn").css("background","#B9B9B9");
            }

        }
    });
    //bank_id

    $("#bank_id").change(function(){
        var id_value = $(this).val();
      // alert(Trim(id_value,'g').trim());
        if(id_value==""){
            $(this).attr("placeholder","银行卡号不能为空");
            $(".btn").attr("disabled",true);
            $(".btn").css("background","#B9B9B9");
        }
        else if(Trim(id_value,'g').trim().length<16){
           // alert($.trim($(this).val()));
            //var a = a.replace(/[ ]/g,"                你                   好                  ");
            //alert(a);
            $(this).attr("value","").attr("placeholder","银行卡长度不正确");
            $(".btn").attr("disabled",true);
            $(".btn").css("background","#B9B9B9");
            return;
        }
        if($("#has_card_people").val()!=''){
            $(".btn").attr("disabled",false);
            $(".btn").css("background","#FF3B2F");
        }

    });
    
    
    $('#bank_id').bind('input propertychange', function() {
        var id_value = $(this).val();
        // alert(Trim(id_value,'g').trim());
          if(id_value==""){
              $(this).attr("placeholder","银行卡号不能为空");
              $(".btn").attr("disabled",true);
              $(".btn").css("background","#B9B9B9");
          }
          else if(Trim(id_value,'g').trim().length>=16){
             // alert($.trim($(this).val()));
              //var a = a.replace(/[ ]/g,"                你                   好                  ");
              //alert(a);
              //$(this).attr("value","").attr("placeholder","银行卡长度不正确");
        	  $(".btn").attr("disabled",false);
              $(".btn").css("background","#FF3B2F");
              return;
          }
          if($("#has_card_people").val()!=''){
        	  $(".btn").attr("disabled",true);
              $(".btn").css("background","#B9B9B9");
          }
    });
    //tel_phone
    $("#tel_phone").change(function(){
        //alert("999");
        if($(this).val()==""){
            $(this).attr("placeholder","手机号码不能为空");
            $("#validate_num").attr("disabled",true);
            $("#validate_num").css("background","#B9B9B9");
            $("#validate_num_in").attr("disabled",true);
            return;
        }
        else{
            var phone = $(this).val();
            var Reg = !!phone.match(/^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/);
            if(!Reg){
                $(this).attr("value","").attr("placeholder","手机号格式不正确");
                $("#validate_num").attr("disabled",true);
                $("#validate_num").css("background","#B9B9B9");
                $("#validate_num_in").attr("disabled",true);
                return;
            }
            else{
            		$("#validate_num").attr("disabled",false);
            		$("#validate_num").css("background","#FF3B2F");
            		$("#validate_num_in").attr("disabled",false);
            	 
            }
           
            //$("#validate_num_in").focus();
            //$("#validate_num_in").css("disabled",false);
        }
    });
    
    $('#tel_phone').bind('input propertychange', function() {
    	if($(this).val()==""){
            $(this).attr("placeholder","手机号码不能为空");
            $("#validate_num").attr("disabled",true);
            $("#validate_num").css("background","#B9B9B9");
            $("#validate_num_in").attr("disabled",true);
            return;
        }
        else{
            var phone = $(this).val();
            var Reg = !!phone.match(/^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/);
            if(Reg){
            	if($("#validate_num_in").val()!=''&&$("#bankName").val()!=''){
            		$("#validate_num").attr("disabled",false);
            		$("#validate_num").css("background","#FF3B2F");
            		$("#validate_num_in").attr("disabled",false);
            		 $(".s_top_btn").attr("disabled",false);
                	 $(".s_top_btn").css("background","#FE3B30");
            	}
            	else{
            		$("#validate_num").attr("disabled",false);
            		$("#validate_num").css("background","#FF3B2F");
            		$("#validate_num_in").attr("disabled",false);
            	}
            	
                return;
            }
            else{
            	 $("#validate_num").attr("disabled",true);
                 $("#validate_num").css("background","#B9B9B9");
                 $("#validate_num_in").attr("disabled",true);
                 $(".s_top_btn").attr("disabled",true);
                 $(".s_top_btn").css("background","#B9B9B9");
            }
           
            //$("#validate_num_in").focus();
            //$("#validate_num_in").css("disabled",false);
        }
    });
    
    $("#validate_num_in").change(function(){
        var cheNum = $(this).val();
        var reg = !!cheNum.match(/^\d{6}$/);
        //alert("value"+$("#bankName").val());
        if(!reg){
            $(this).attr("value","").attr("placeholder","请输入正确的六位识别码");
            $(".s_top_btn").attr("disabled",true);
            $(".s_top_btn").css("background","#B9B9B9");
            return;
        }
        if($("#bankName").val()==''){
        	 $(".s_top_btn").attr("disabled",true);
             $(".s_top_btn").css("background","#B9B9B9");
            // $("#validate_num_in").attr("disabled",true);
        }
        else{
        	 $(".s_top_btn").attr("disabled",false);
        	 $(".s_top_btn").css("background","#FE3B30");
        }
        
       
    });
    $('#validate_num_in').bind('input propertychange', function() {
        var cheNum = $(this).val();
        var reg = !!cheNum.match(/^\d{6}$/);
        //alert("value"+$("#bankName").val());
        if(reg){
        	if($("#bankName").val()!=''&&$('#tel_phone').val()!=''){
        		$(".s_top_btn").attr("disabled",false);
        		$(".s_top_btn").css("background","#FE3B30");
        	}
            
          
        }
        else{
        	$(".s_top_btn").attr("disabled",true);
            $(".s_top_btn").css("background","#B9B9B9");
        }
    });

    $("#password").change(function(){
        if($(this).val().length<6||$(this).val().length>16){
            $(this).attr("value","").attr("placeholder","密码为6~16位字符，请再次输入交易密码");
            $(".btn").attr("disabled",true);
            $(".btn").css("background","#B9B9B9");
            $(".s_top_btn").attr("disabled",true);
            $(".s_top_btn").css("background","#B9B9B9");
            return;
        }
        if($(".agree_btn").hasClass("default_agree")){
           if(document.getElementById("repassword")!=undefined){
              // alert("yes");
               if($("#repassword").val()!=''){
                   if($(this).val().length!=$("#repassword").val().length){
                       $("#repassword").attr("value","").attr("placeholder","两次密码输入不一致，请重新输入");
                       $(".btn").attr("disabled",true);
                       $(".btn").css("background","#B9B9B9");
                       $(".s_top_btn").attr("disabled",true);
                       $(".s_top_btn").css("background","#B9B9B9");
                   }
                   else{
                       $(".btn").attr("disabled",false);
                       $(".btn").css("background","#FF3B2F");
                       $(".s_top_btn").attr("disabled",false);
                       $(".s_top_btn").css("background","#FF3B2F");
                   }
               }

           }
           else{
               $(".btn").attr("disabled",false);
               $(".btn").css("background","#FF3B2F");
               $(".s_top_btn").attr("disabled",false);
               $(".s_top_btn").css("background","#FF3B2F");
           }
        }
    });
    
    
    $('#password').bind('input propertychange', function() {
    	if($(".agree_btn").hasClass("default_agree")){
    		if($(this).val().length>=6&&$(this).val().length<=16){
        	if(document.getElementById("repassword")!=undefined){
        		if($("#repassword").val()!=''){
                    if($(this).val().length==$("#repassword").val().length){
                    	$(".btn").attr("disabled",false);
                        $(".btn").css("background","#FF3B2F");
                        $(".s_top_btn").attr("disabled",false);
                        $(".s_top_btn").css("background","#FF3B2F");
                    }
                    else{
                    	 $(".btn").attr("disabled",true);
                         $(".btn").css("background","#B9B9B9");
                         $(".s_top_btn").attr("disabled",true);
                         $(".s_top_btn").css("background","#B9B9B9");
                    }
                }
        	}
        	else{
        		$(".btn").attr("disabled",false);
                $(".btn").css("background","#FF3B2F");
                $(".s_top_btn").attr("disabled",false);
                $(".s_top_btn").css("background","#FF3B2F");
        	}
        	
    	}else{
                $(".btn").attr("disabled",true);
                $(".btn").css("background","#B9B9B9");
                $(".s_top_btn").attr("disabled",true);
                $(".s_top_btn").css("background","#B9B9B9");
            }
        
      }
    });

    $("#repassword").change(function(){
        if($(this).val().length!=$("#repassword").val().length){
            $(this).attr("value","").attr("placeholder","两次密码输入不一致，请重新输入");
            $(".btn").attr("disabled",true);
            $(".btn").css("background","#B9B9B9");
            $(".s_top_btn").attr("disabled",true);
            $(".s_top_btn").css("background","#B9B9B9");
            return;
        }
        else{
            if($("#password").val()!=''&&$(".agree_btn").hasClass("default_agree")){
                $(".btn").attr("disabled",false);
                $(".btn").css("background","#FF3B2F");
                $(".s_top_btn").attr("disabled",false);
                $(".s_top_btn").css("background","#FF3B2F");
            }
        }
    });
    
    
    $('#repassword').bind('input propertychange', function() {
        if($(this).val().length==$("#password").val().length&&$(".agree_btn").hasClass("default_agree")){
        	$(".btn").attr("disabled",false);
            $(".btn").css("background","#FF3B2F");
            $(".s_top_btn").attr("disabled",false);
            $(".s_top_btn").css("background","#FF3B2F");
            return;
        }
        else{
        	 $(".btn").attr("disabled",true);
             $(".btn").css("background","#B9B9B9");
             $(".s_top_btn").attr("disabled",true);
             $(".s_top_btn").css("background","#B9B9B9");
           
        }
    });

//    $(".close").click(function(){
//
//        //alert("close");
//
//            //window.close();
//            //window.opener=null;window.open('','_self');window.close();
//    	//alert("close");
//    	//CloseWebPage();
//    	//CloseWebPage();
//
//    });

    $(".agree_btn").click(function(){
        //alert($(".agree_btn").css("color"));//=="rgb(255,59,47)"
    	if($(".agree_btn").hasClass("ccc")){
    		//alert("ccc");
            if($(".agree_btn").hasClass("default_agree")){
                $(".agree_btn").removeClass("default_agree");
                $(".agree_btn").addClass("other_agree");
                $(".btn").attr("disabled",true);
                $(".btn").css("background","#B9B9B9");
                $(".s_top_btn").attr("disabled",true);
                $(".s_top_btn").css("background","#B9B9B9");
            }
            else{
                $(".agree_btn").removeClass("other_agree");
                $(".agree_btn").addClass("default_agree");
                if( $("#bankName").val()!=""&& $("#tel_phone").val()!="" && $("#validate_num_in").val()!=""){
                  /* if(document.getElementById("repassword")!=undefined){
                        if($("#repassword").val()!=''){
                            $(".btn").attr("disabled",false);
                            $(".btn").css("background","#FF3B2F");
                            $(".s_top_btn").attr("disabled",false);
                            $(".s_top_btn").css("background","#FF3B2F");
                        }
                       else{
                            $(".btn").attr("disabled",true);
                            $(".btn").css("background","#B9B9B9");
                            $(".s_top_btn").attr("disabled",true);
                            $(".s_top_btn").css("background","#B9B9B9");
                        }
                    }else{*/
                       $(".btn").attr("disabled",false);
                       $(".btn").css("background","#FF3B2F");
                       $(".s_top_btn").attr("disabled",false);
                       $(".s_top_btn").css("background","#FF3B2F");
                   //}

                }
                else{
                    $(".btn").attr("disabled",true);
                    $(".btn").css("background","#B9B9B9");
                    $(".s_top_btn").attr("disabled",true);
                    $(".s_top_btn").css("background","#B9B9B9");
                }

            }
    	}else{
            if($(".agree_btn").hasClass("default_agree")){
                $(".agree_btn").removeClass("default_agree");
                $(".agree_btn").addClass("other_agree");
                $(".btn").attr("disabled",true);
                $(".btn").css("background","#B9B9B9");
                $(".s_top_btn").attr("disabled",true);
                $(".s_top_btn").css("background","#B9B9B9");
            }
            else{
                $(".agree_btn").removeClass("other_agree");
                $(".agree_btn").addClass("default_agree");
                if( $("#password").val()!=""){
                   if(document.getElementById("repassword")!=undefined){
                        if($("#repassword").val()!=''&&$("#repassword").val().length==$("#password").val().length){
                            $(".btn").attr("disabled",false);
                            $(".btn").css("background","#FF3B2F");
                            $(".s_top_btn").attr("disabled",false);
                            $(".s_top_btn").css("background","#FF3B2F");
                        }
                       else{
                            $(".btn").attr("disabled",true);
                            $(".btn").css("background","#B9B9B9");
                            $(".s_top_btn").attr("disabled",true);
                            $(".s_top_btn").css("background","#B9B9B9");
                        }
                    }else{
                       $(".btn").attr("disabled",false);
                       $(".btn").css("background","#FF3B2F");
                       $(".s_top_btn").attr("disabled",false);
                       $(".s_top_btn").css("background","#FF3B2F");
                   }

                }

            }
    	}

    });
    
    $(".selectBank").click(function(){
    	var index =  $(".selectBank").index($(this));
        //setCookie("bankType",$("input[name='bankType']").eq(index).val());
    	//$("form").eq(index).submit();
        $("#box2").hide();
        $("#box1").show();
        setCookie("bankType",$("input[name='bankType']").eq(index).val());
        getbankname();
        if($("#validate_num_in").val()!=''){
        	 $(".s_top_btn").attr("disabled",false);
        	 $(".s_top_btn").css("background","#FF3B2F");
       
        }
       
    });
    
    $(".selectBank2").click(function(){
    	var index =  $(".selectBank2").index($(this));
        //setCookie("bankType",$("input[name='bankType']").eq(index).val());
    	//$("form").eq(index).submit();
        $("#box2").hide();
        $("#box1").show();
        setCookie("newbankType",$("input[name='bankType']").eq(index).val());
       // alert($("input[name='bankType']").eq(index).val());
        getbankname2();
        if($("#validate_num_in").val()!=''&&$(".agree_btn").hasClass("default_agree")&&$("#tel_phone").val()!=''){
        	 $(".s_top_btn").attr("disabled",false);
        	 $(".s_top_btn").css("background","#FF3B2F");
       
        }
       
    });
    
    $(".go_back_home").click(function(){
       // alert("-1");
        history.go(-1);
    });
    
    $(".jianpan_head_x").click(function(){
    	 $(".bigbox").hide();
         $(".jianpan").hide();
    });
    
    $(".jianpan_button").click(function(){
    	if($("#inputId").val()==''){
    		//alert("lll");
    	}
    	else{
    		//alert("qqq");
    		setCookie("newpassword",$("#inputId").val());
    		huanka();
    	}
    	
    	
    });

});
/*******************************************************************************************/
function formatBankNo (BankNo){
    if (BankNo.value == "") return;
    var account = new String (BankNo.value);
    account = account.substring(0,25); /*帐号的总数, 包括空格在内 */
    if (account.match (".[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{7}") == null){
        /* 对照格式 */
        if (account.match (".[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{7}|" + ".[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{7}|" +
            ".[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{7}|" + ".[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{7}") == null){
            var accountNumeric = accountChar = "", i;
            for (i=0;i<account.length;i++){
                accountChar = account.substr (i,1);
                if (!isNaN (accountChar) && (accountChar != " ")) accountNumeric = accountNumeric + accountChar;
            }
            account = "";
            for (i=0;i<accountNumeric.length;i++){    /* 可将以下空格改为-,效果也不错 */
                if (i == 4) account = account + " "; /* 帐号第四位数后加空格 */
                if (i == 8) account = account + " "; /* 帐号第八位数后加空格 */
                if (i == 12) account = account + " ";/* 帐号第十二位后数后加空格 */
                if (i == 16) account = account + " ";
                account = account + accountNumeric.substr (i,1);
            }
        }
    }
    else
    {
        account = " " + account.substring (1,5) + " " + account.substring (6,10) + " " + account.substring (14,18) + "-" + account.substring(18,25);
    }
    if (account != BankNo.value) BankNo.value = account;
}
/***************************************************8*/
function Trim(str,is_global)
{
    var result;
    result = str.replace(/(^\s+)|(\s+$)/g,'');
    if(is_global.toLowerCase()=="g")
    {
        result = result.replace(/\s/g,"");
    }
    return result;
};
function loadpage(e,p){
    //document.charset='utf-8';
    //window.location.href = e;
	/*if(e=='bankType'){
		alert("type");
		var parm = Trim($("#bank_id").val(),'g');
		$.ajax({
			type: 'post',
			url : "http://zhu.hunme.net:8080/wealthKits/sg/getBankCardType",
			data:{  
				cardNumber:parm
				  },  

			dataType : "json",
			//contentType : "application/json",
			success : function(data) {
				alert(data);
			},
			error : function() {
				alert(parm);
				//alert("系统异常,加载失败1");
			}
		});
	}
	if(e=='last'){
		//alert("last");
		//var parm = Trim($("#bank_id").val(),'g');
		$.ajax({
			type: 'post',
			url : "http://zhu.hunme.net:8080/wealthKits/sg/openAnAccount",
			data:{  
				cardNumber:parm
				  },  

			dataType : "json",
			//contentType : "application/json",
			success : function(data) {
				alert(data);
			},
			error : function() {
				alert(parm);
				//alert("系统异常,加载失败1");
			}
		});
	}
	*/
    if(e=='id_validate'){
    	//alert('id_validate');
        removeLoading();
        setCookie("name",$("#name").val());
        setCookie("id",$("#id_card").val());
        setCookie("email",$("#email_num").val());
      //  var url = $("form").attr("action");
      //  $("form").attr("action",encodeURI(url));
      //  alert(encodeURI(url)+"urlurlurl22222222");
        showloading();
//        setTimeout(function(){
//            alert(document.getElementById('loadingPic'));
//            //document.getElementById('loadingPic').
//        },100);
        if($("#name").val()!=''&&$("#id_card").val()!=''){
        	var card = $("#id_card").val();
        	if(IdCard(card,3)>=18){
        		iskaihu();
        		return;
        	}
        	else{
     		//alert("未满十八周岁不允许开户");
        		alertInner("对不起，监证会不允许未满十八岁的用户开户。届时恭候您的到来！",'知道了');
        		//return;
        		return;
        	}
        	
        	
        }
 
        
    }
    if(e=='bankType'){
        setCookie("has_card_people",$("#has_card_people").val());
       
        var bankid = Trim($("#bank_id").val(),'g');
         setCookie("bank_id",bankid);
        //alert(bankid);
        
        $.ajax({
            type: 'post',
            url : getCookie("ip")+"/sg/getBankCardType",//银行卡类型
            data:{
            	secretKey:getCookie("secretKey"),
                cardNumber:bankid
            },
            dataType: 'json',
            success: function(data){
            	//alert(200);
            	
            	//var data2 = eval_r("(" + data + ")");

            	//console.log();
            	var obj = JSON.parse(data.date);
                //alert(obj[0].bank_name);
            	//alert(obj[0].bank_name);
            	if(obj[0].bank_name==undefined||obj[0].bank_name==''||obj[0].bank_name==null){
            		setCookie("bankName",'');
            		$("form").submit();
            	}
            	else{
            		setCookie("bankName",obj[0].bank_name);//bankType
                    setCookie("bankType",obj[0].bank_type);
	            	//getbankType1(obj[0].bank_name);
	            	$("form").submit();
            	}

            	return;
            	
            },
            error: function(xhr, type){
                alert('Ajax error!');
                $("form").submit();
                return;
            }
        });
        //$("form").submit();
        //return;
    }
    if(e=='select_bank'){
         setCookie("bankName",$("#bankName").val());
        //setCookie("bank_id",$("#bank_id").val());
        //window.location.href = 'select_bank';
        //$("form").eq(0).submit();
         getbankType1(getCookie("bankName"));
        $("#box1").hide();
        $("#box2").show();
        
        //getbankname();
        return;
    }
    if(e=='select_bank2'){
        //setCookie("bankName",$("#bankName").val());
       //setCookie("bank_id",$("#bank_id").val());
       //window.location.href = 'select_bank';
       //$("form").eq(0).submit();
       $("#box1").hide();
       $("#box2").show();
       //getbankname();
       return;
   }
    //bank_card_infobank_card_info
    if(e=='bank_card_info'){
        //setCookie("bank_id",$("#bank_id").val());
        //if(getCookie("resultCode")!=null&&getCookie("resultCode").length>0){
            //if($("#validate_num_in").val()==getCookie("resultCode")){
        setCookie("identifyingCode",$("#validate_num_in").val());//tel_phone
        setCookie("tel_phone",$("#tel_phone").val());
       // alert($.md5($("#validate_num_in").val()));
        console.log($.md5('111111'));
        $("form").eq(0).submit();
            //}
        //}else{1`
           // alert("获取验证码出错，请重新获取");
            return;
       // }
       // window.location.href = 'select_bank.html';
    }//setPassWord
    if(e=='setPassWord'){
        setCookie("password",$.md5($("#password").val()));
        //alert(getCookie("password"));
        //alert(getCookie("bankType")+"[][][]");
        //setCookie("bank_id",$("#bank_id").val());
        //window.location.href = 'select_bank.html';
        //alert($("#password").val()!=$("#repassword").val());
        showloading();
        if($("#password").val()!=$("#repassword").val()){
            //alert('1111111');
            removeLoading();
            alertInner('两次密码不一致，请重新输入','确定');
            return;
        }else{
            kaihu();
        }

        return;
    }
    if(e=='replace_bankcard1'){
    	//getUserInfo();
       setCookie("newbank_id",$("#bank_id").val());
       setCookie("newhas_card_people",$("#has_card_people").val());
       //window.location.href = 'replace_bankcard2';
       //alert(getCookie("newbank_id"));
       //alert(getCookie("newhas_card_people"));
       $("form").submit();
       return;
   }
    //replace_bankcard2
    if(e=='replace_bankcard2'){
    	//alert("replace_bankcard2");
    	var newBankName = $("#bankName").val();//tel_phone
        setCookie("newBankName",newBankName);
        setCookie("newtel_phone",$("#tel_phone").val());
        setCookie("newidentifyingCode",$("#validate_num_in").val());
        getbankType(newBankName);
        $(".bigbox").show();
        $(".jianpan").show();
        document.getElementById("inputId").focus();
        //alert(getCookie("newBankName"));
        //alert(getCookie("newtel_phone")+"object?");
        //alert(getCookie("newidentifyingCode"));
        //alert(getCookie("newbankType"));
        //$("form").submit();/sg/replacementOfBankCardInformation
        //alert(getCookie("newBankName")+",,,"+getCookie("newidentifyingCode")+",,,"+getCookie("newbankType")+"..."+getCookie("newbank_id")+",,,"+getCookie("newhas_card_people"));        
        return;
    }
	//$("form").submit();
}

function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}


//写cookies
function setCookie(name,value)
{
    var Days = 30;
    var exp = new Date();
    exp.setTime(exp.getTime() + Days*24*60*60*1000);
    document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
}
//获取cookies
function getCookie(name)
{
    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
    if(arr=document.cookie.match(reg))
        return unescape(arr[2]);
    else
        return null;
}

function gohistory(){
    history.back();
}

function CloseWebPage() {
    Messenger.send('close');
  //  alert('doclose111');
   //alert(222);

};



//function getOs()
//{
//    var OsObject = "";
//   if(navigator.userAgent.indexOf("MSIE")>0) {
//        return "MSIE";
//   }
//   if(isFirefox=navigator.userAgent.indexOf("Firefox")>0){
//        return "Firefox";
//   }
//   if(isSafari=navigator.userAgent.indexOf("Safari")>0) {
//        return "Safari";
//   }
//   if(isCamino=navigator.userAgent.indexOf("Camino")>0){
//        return "Camino";
//   }
//   if(isMozilla=navigator.userAgent.indexOf("Gecko/")>0){
//        return "Gecko";
//   }
//
//}
 //alert("您的浏览器类型为:"+getOs());

getbankname = function(){
	var bankType = getCookie("bankType");
$.ajax({
    type: 'GET',
    url: 'json/bank.json',
    dataType: 'json',
    success: function(data){
    	//alert("success");
        for(var i = 0; i < data.banklists.length; i++){
            if(bankType==data.banklists[i].bankType){
                $("#bankName").attr("value",data.banklists[i].bankName);
                setCookie("bankName",data.banklists[i].bankName);
                //alert(getCookie("bankName"));
                //alert(getCookie("bankType")+":::::::::"+getCookie("bankName"));
            }
        }
    },
    error: function(xhr, type){
        alert('Ajax error!!!!');
    }
});
};


getbankname2 = function(){
	var bankType = getCookie("newbankType");
$.ajax({
    type: 'GET',
    url: 'json/bank.json',
    dataType: 'json',
    success: function(data){
    	//alert("success");
        for(var i = 0; i < data.banklists.length; i++){
            if(bankType==data.banklists[i].bankType){
                $("#bankName").attr("value",data.banklists[i].bankName);
                setCookie("newbankName",data.banklists[i].bankName);
               // alert(getCookie("newbankName"));
                //alert(getCookie("bankType")+":::::::::"+getCookie("bankName"));
            }
        }
    },
    error: function(xhr, type){
        alert('Ajax error!!!!');
    }
});
};

getbankType = function(e){
	$.ajax({
	    type: 'GET',
	    url: 'json/bank.json',
	    dataType: 'json',
	    success: function(data){
	    	//alert("success");
	        for(var i = 0; i < data.banklists.length; i++){
	            if(e==data.banklists[i].bankName){
	                //$("#bankName").attr("value",data.banklists[i].bankName);
	                setCookie("newbankType",data.banklists[i].bankType);
	               // alert(getCookie("newbankName"));
	                //alert(getCookie("bankType")+":::::::::"+getCookie("bankName"));
	            }
	        }
	    },
	    error: function(xhr, type){
	        alert('Ajax error!!!!');
	    }
	});
};

getbankType1 = function(e){
	$.ajax({
	    type: 'GET',
	    url: '../json/bank.json',
	    dataType: 'json',
	    success: function(data){
	    	//alert("success");
	        for(var i = 0; i < data.banklists.length; i++){
	            if(e==data.banklists[i].bankName){
	                //$("#bankName").attr("value",data.banklists[i].bankName);
	                setCookie("bankType",data.banklists[i].bankType);
	               // alert(getCookie("newtel_phone"));
	               // alert(getCookie("newbankName"));
	                //alert(getCookie("bankType")+":::::::::"+getCookie("bankName"));
	            }
	        }
	    },
	    error: function(xhr, type){
	        alert('Ajax error!!!!');
	    }
	});
   // alert(getCookie('bankType'));
};



function huanka(){
	// alert("token:"+getCookie("token")+"secretKey:"+getCookie("secretKey")+
	// "loginName:"+getCookie("oldtel_phone")+"bankMobile:"+getCookie("newtel_phone")+
	// "cardNumber:"+Trim(getCookie("newbank_id"),'g')+"cardType:"+getCookie("newbankType")+"realName:"+
	// getCookie("newhas_card_people")+"idNumber:"+getCookie("id_number")+"password:"+getCookie("newpassword")+
	// "identifyingCode:"+getCookie("newidentifyingCode"));
	$.ajax({
    type: 'post',//replacementOfBankCardInformation
    url : getCookie("ip")+"/sg/replacementOfBankCardInformation",//换卡
    data:{
        token:getCookie("token"),
        secretKey:getCookie("secretKey"),
        loginName:getCookie("oldtel_phone"),
        bankMobile:getCookie("newtel_phone"),
        cardName:getCookie("newBankName"),
        cardNumber:Trim(getCookie("newbank_id"),'g'),
        cardType:getCookie("newbankType"),               
        realName:getCookie("newhas_card_people"),
        idNumber:getCookie("id_number"),
        password:$.md5(getCookie("newpassword")),
        identifyingCode:getCookie("newidentifyingCode")
    },
    dataType: 'json',
    success: function(data){
    	
    	//var str = data.toJSONString(); 
    	//var str = JSON.stringify(data); 
    	//JSON.parse(data);
    	//alert(data.resultDesc);
    	if(data.resultCode=='0'){
    		//$("form").submit();
    	}
    	else{
    		$(".jianpan").hide();
    		$(".bigbox").hide();
    		// alert(data.resultDesc+"");
    		alertInner(data.resultDesc,"确定");
    		
    	}
        
    },
    error: function(xhr, type){
        alert('Ajax error!');
        return;
    }
});
}


function iskaihu(){
	// alert(":::::::::::::::::::::"+getCookie("token"));
	// alert(getCookie("secretKey"));
	// alert(getCookie("id"));
    //setCookie("ip","http://zhu.hunme.net:8080/wealthKits");
    // var ip="http://zhu.hunme.net:8080/wealthKits"
	console.log('token:'+getCookie("token")+'secretKey'+getCookie("secretKey")+'cardNumber'+getCookie("id"));
    
    $.ajax({
        type: 'post',
        url : getCookie("ip")+"/userMessage/accountRegistrationStatus",//查询是否开过户
        // url : ip+"/userMessage/accountRegistrationStatus",//查询是否开过户
        data:{
        	// token:getCookie("token"),
             //secretKey:getCookie("secretKey"),
             cardNumber:getCookie("id"),
             source:'2'
        },
        dataType:'json',

        success: function(data){
        	//alert(JSON.stringify(data));
        	if(data.resultCode==0){
        		//window.location.href = 'account_accredit';
                window.location.href = 'bank_card_validate.html';
        		// $("form").submit();
        	}
        	else if(data.resultCode==1){
        		
        		alert("已开户");
        		window.location.href = 'sgIsOpen.html';
        	}
        		
        },

        error:function(xhr, type){
            // alert(JSON.stringify(data));
        	// window.location.href = 'account_accredit';
        	//alert(JSON.stringify(xhr));
        	//alert(JSON.stringify(type));
            alert("error");
        }
    });
}

function IdCard(UUserCard,num){
	   if(num==1){
	       //获取出生日期
	       birth=UUserCard.substring(6, 10) + "-" + UUserCard.substring(10, 12) + "-" + UUserCard.substring(12, 14);
	    return birth;
	   }
	   if(num==2){
	       //获取性别
	       if (parseInt(UUserCard.substr(16, 1)) % 2 == 1) {
	           //男
	     return "男";
	       } else {
	           //女
	     return "女";
	       }
	   }
	   if(num==3){
	        //获取年龄
	        var myDate = new Date();
	        var month = myDate.getMonth() + 1;
	        var day = myDate.getDate();
	        var age = myDate.getFullYear() - UUserCard.substring(6, 10) - 1;
	        if (UUserCard.substring(10, 12) < month || UUserCard.substring(10, 12) == month && UUserCard.substring(12, 14) <= day) {
	            age++;
	        }
	  return age;
	 }
	}

function alertInner(text,click_){
	//alert("1");
	if(document.getElementById("msg_bg")==undefined){
		//alert("2");
		var result = '<div class="msg_bg" id="msg_bg"></div><div class="msg"><div class="text">'+text+'</div><div class="text_btn" onclick="msg_click()">'+click_+'</div></div>';
		$("body").append(result);
	}else{
        $('.text').html(text);
        $('text_btn').html(click_);
		$(".msg_bg").show();
		$(".msg").show();
	}
	
}

function showloading(){
   var html = '<div id="loadingBox" style="width: 20%;height: 12%;position: absolute;z-index: 4;top: 40%;left: 40%;border-radius: 10px;">'+
       '<img src="../images/red-loading.png" style="width: 100%;height: 100%;;border-radius: 10px;" id="loadingPic" />'+
        '<span style="color: #ff0000;">加载中...</span>'+
        '</div>';
    $('body').append(html);
    var deg = 20;

    setInterval(function(){

        //alert(1);
        $('#loadingPic').css('transform','rotate('+deg+'deg)');
        deg+=20;
    },100);
    //alert(document.getElementById('loadingPic')+'vvvvvvvvvv');
    //document.getElementById('loadingPic').rotate(30);
}

function msg_click(){
	$(".msg_bg").hide();
	$(".msg").hide();
}

function removeLoading(){
    $('#loadingBox').remove();
}
function kaihu(){
	console.log(
			'token:'+getCookie("token")+
            'secretKey:'+getCookie("secretKey")+
            'loginName'+getCookie("tel_phone")+
            'cardNumber:'+getCookie("bank_id")+
            'cardType:'+getCookie("bankType")+
            '+cardName:'+getCookie("bankName")+
            'realName:'+getCookie("has_card_people")+
            '+idNumber:'+getCookie("id")+
            '+password:'+getCookie("password")+
            '+identifyingCode:'+getCookie("identifyingCode")+
            '+email:'+getCookie("email")
	
	);
    $.ajax({
        type: 'post',
        url : getCookie("ip")+"/sg/openAnAccount",//开户
        data:{
            token:getCookie("token"),
            secretKey:getCookie("secretKey"),
            loginName:getCookie("tel_phone"),
            // e_mail:"836323697@qq.com",
            // e_email:getCookie("email"),
            cardNumber:getCookie("bank_id"),
            cardType:getCookie("bankType")+'',
            cardName:getCookie("bankName"),
            realName:getCookie("has_card_people"),
            idNumber:getCookie("id"),
            password:getCookie("password"),
            identifyingCode:getCookie("identifyingCode"),
            e_mail:getCookie("email")
        },
        dataType: 'json',
        success: function(data){
            removeLoading();
        	//var str = data.toJSONString(); 
        	//var str = JSON.stringify(data); 
        	//JSON.parse(data);
        	//alert(data.resultDesc);
        	if(data.resultCode=='0'){
        		//alertInner("开户成功！","知道了");
        		window.location.href = 'success.html';
        		//return;
        	}
        	else if(data.resultCode=='12'){
        		alertInner("系统繁忙，请稍后再试！","知道了");
        		return;
        	}
        	else if(data.resultCode=='10925'){
        		alertInner("银行卡号错误，请核实后重新输入！","重新输入");
        		window.location.href = 'bank_card_validate';
        	}
        	else if(data.resultCode=='10926'){//10926
        		alertInner("需绑定持卡人的储蓄卡，请重新输入！","知道了");
        		window.location.href = 'bank_card_validate';
        	}//10947
        	else if(data.resultCode=='10947'){//10926
        		alertInner("姓名与身份证号码不匹配，请核实后重新输入","重新输入");//id_validate
        		window.location.href = 'id_validate';
        	}//5009
        	else if(data.resultCode=='5009'){//10926
        		alertInner("test","重新输入");//id_validate
        		window.location.href = 'id_validate';
        	}
        	else if(data.resultCode=='10951'){//短信未发送到银行预留手机号
        		alertInner("短信未发送到银行预留手机号","重新输入");
        		window.location.href = 'bank_card_info';
        	}
        	else if(data.resultCode=='10938'){//短信未发送到银行预留手机号
        		alertInner("预留手机号验证码验证错误，请重新获取后填写","重新获取");
        		window.location.href = 'bank_card_info';
        	}
        	else if(data.resultCode=='10937'){//短信未发送到银行预留手机号
        		alertInner("预留手机号验证码已失效，请重新获取后填写","重新获取");
        		window.location.href = 'bank_card_info';
        	}
        	else if(data.resultCode=='10935'){//短信未发送到银行预留手机号
        		//http://192.168.31.135:8080/wealthKits/id_validate
        		window.location.href = 'bank_card_validate';
        	}
        	else{
        		alertInner(data.resultDesc,"知道了");
        		// alert(data.resultCode);
        		// alert(data.resultDesc);
        	}           
        },
        error: function(xhr, type){
            removeLoading();
            alert('Ajax error!');
        }
    });
}



