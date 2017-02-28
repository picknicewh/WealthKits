/**
 * Created by cxj on 2016/1/15.
 */
$(function(){
$("#no_validatein").click(function(){
    $(".alert_validate_num").show();
});
    $(".x").click(function(){
        $(".alert_validate_num").hide();
    });
    $("#info_2_2").click(function(){
        $("#phone_num_info").show();
    });
    $(".phone_num_info_footer").click(function(){
        $("#phone_num_info").hide();
    });
});