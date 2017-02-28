/**
 * Created by cxj on 2016/1/11.
 */
window.onload = function () {
    //var parm = getQueryString("username");
    //alert(decodeURI(parm));
    $('.neck').hide();
    var bottomHtml =  '<div class="bottom">基金交易服务由上海长量基金销售投资顾问有限公司提供</br>资格证书编号：000000306 </div>';
    $('body').append(bottomHtml);
    var cookie = getCookie("name");
    $("#has_card_people").attr("value",cookie);
    // alert(i);

    $(".info_1_2").click(function(){
        $("#alert").show();
        $("#inner_alert").show();
    });
    $("#know").click(function(){
        $("#alert").hide();
        $("#inner_alert").hide();
    });
}