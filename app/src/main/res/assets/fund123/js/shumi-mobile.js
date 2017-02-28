$(document).ready(function(){
    $("#back").live('click', function() {
//        if(history.length > 1){
//            history.go(-1);
//            return false;
//        }else{
//            smbEvent('quitSDk', undefined, function(){});
//            return false;
//        }
        smbEvent('webGoBack', undefined, function(){});
        return false;
    });
    $("#quit").live('click', function() {
        smbEvent('quitSDk', undefined, function(){});
        return false;
    });
});

function android() {
    return /android/i.test(navigator.userAgent.toLowerCase());
}

function iphone() {
    return /ip(hone|od)/i.test(navigator.userAgent.toLowerCase());
}

function ipad() {
    return /ipad/i.test(navigator.userAgent.toLowerCase());
}

function trim(txtValue) {
	txtValue = txtValue.replace(/\/\*((\n|\r|.)*?)\*\//mg, ""); //去掉多行注释/*..*/
	txtValue = txtValue.replace(/(\s+)\/\/(.*)\n/g,""); //去掉单行注释//(前面有空格的注释)
	txtValue = txtValue.replace(/;\/\/(.*)\n/g,";"); //去掉单行注释//(前面是分号的注释)
	txtValue = txtValue.replace(/\/\/[^"][^']\n/g,""); //去掉单行注释//(//后面只有一个'或一个"的不替换)
	txtValue = txtValue.replace(/[\r]/g,""); //替换换行
	txtValue = txtValue.replace(/[\n]/g,""); //替换回车 
    return txtValue;
}

/// 页面跳转
function loadURL(url) {
    javascript: location.href = url;
}

function createOauthData(methodType, xhr) {
    var result;
    switch (methodType.toLowerCase()) {
        case 'post':
            result = urloauth.createPostData(xhr.url, xhr.data);
            break;

        case 'get':
            result = urloauth.createGetData(xhr.url, xhr.data);
            break;
    }
    return toObject(result);
}

//请求Oauth
function requestOauthApi(uri, methodType, dataType, params, onDone, onFail){
    $.when($.ajax({
        url: 'http://smbclient.oauth/'.concat(uri),
        type: methodType,
        'dataType': dataType,
        data: params,
        beforeSend: function(e, xhr) {
        // 非安卓客户端直接跳过处理
        if (!window.urloauth) {
            return;
        }
        var p = createOauthData(methodType, xhr);
        if (p != undefined && p.sendComfirmed == false) {
            return;
        }
        xhr.url = p.url;
        xhr.data = p.data;
        },
        success: function(json) { }
    }))
    .done(onDone)
    .fail(onFail);
}

// 请求openapi
// 参数methodType: post|get  requestURI:uri dataType:json params: params
function requestOpenApi(uri, methodType, dataType, params, onDone, onFail) {
    $.when($.ajax({
        url: 'http://smbclient.openapi/'.concat(uri),
        type: methodType,
        'dataType': dataType,
        data: params,
        beforeSend: function(e, xhr) {
            // 非安卓客户端直接跳过处理
            if (!window.urloauth) {
                return;
            }
            var p = createOauthData(methodType, xhr);
            if (p != undefined && p.sendComfirmed == false) {
                return;
            }
            xhr.url = p.url;
            xhr.data = p.data;
        },
        success: function(json) { }
    }))
    .done(onDone)
    .fail(onFail);
}

// 请求fundapi
// 参数methodType: post|get  requestURI:uri dataType:json params: params
function requestFundApi(uri, methodType, dataType, params, onDone, onFail) {
    $.when($.ajax({
        url: 'https://smbclient.fundapi/'.concat(uri),
        type: methodType,
        'dataType': dataType,
        data: params,
        beforeSend: function(e, xhr) {
            // 非安卓客户端直接跳过处理
            if (!window.urloauth) {
                return;
            }
            var p;
            // 根据不同方法调用
            if (methodType == 'post') {
                p = toObject(urloauth.createPostData(xhr.url, xhr.data));
            } else if (methodType == 'get') {
                p = toObject(urloauth.createGetData(xhr.url, xhr.data));
            }

            if (p != undefined && p.sendComfirmed == false) {
                return;
            }
            xhr.url = p.url;
            xhr.data = p.data;
        },
        success: function(json) { }
    }))
    .done(onDone)
    .fail(onFail);
}

// 请求native数据
function smbClient(uri, methodType, params, onDone, onFail) {
    if (!window.smbproxy) {
        $.when($.ajax({
            url: 'http://smbclient/'.concat(uri),
            type: methodType,
            data: params,
            success: function(json) { }
        }))
        .done(onDone)
        .fail(onFail);
    } else {
        smbproxy.smbClient(uri, JSON.stringify(params), trim(onDone.valueOf().toString()), trim(onFail.valueOf().toString()));
    }
}


// 产生事件通知native
function smbEvent(uri, params, onAlways) {
    if (!window.smbproxy) {
        $.when($.ajax({
            url: 'http://smbclient.event/'.concat(uri),
            type: 'post',
            data: params,
            success: function(json) { }
        }))
        .always(onAlways);
    } else {
        smbproxy.smbEvent(uri, JSON.stringify(params), trim(onAlways.valueOf().toString()));
    }
}


// 从LocalStorage中获取全局参数
function getLocalStorageValue(name) {
    return localStorage.getItem(name);
}

function getSessionStorageValue(name) {
    return sessionStorage.getItem(name);
}

function setLocalStorageValue(name, value) {
    localStorage.setItem(name, value);
}

function setSessionStorageValue(name, value) {
    sessionStorage.setItem(name, value);
}

// Json字符串转成javascript对象
function toObject(str) {
    try {
        return eval("(" + str + ")");
    }
    catch (e) {
        console.warn(e.message);
        return null;
    }
}

// 获得手机号码
function getRegisterMobileNumber() {
    return getSessionStorageValue("register.mobilenumber");
}

// 设置手机号码
function setRegisterMobileNumber() {
    return setSessionStorageValue("register.mobilenumber");
}

// 是否是开户状态
function isOpenAcco() {
    var is_opening = sessionStorage.getItem("register.is_open_acco");
    return (!is_opening) || (is_opening == '0');
}

// 禁用input
function disable_inputs(inputID,disabled){
    $("#"+inputID).attr("disabled", disabled);
}

function PreventMultiSubmitButton(element, options) {
    options = options || {};
    var disabledButtonCssClass = options.disabledClass;
    var mask = _buildMask(element);
    function _buildMask(target) {
        var mask = $('<div></div>');
        var targetOffset = target.offset();
        mask.css({
                 cursor: 'wait',
                 position: 'absolute',
                 zIndex: '2147483584',
                 top: targetOffset.top + 'px',
                 left: targetOffset.left + 'px',
                 width: target.outerWidth() + 'px',
                 height: target.outerHeight() + 'px',
                 backgroundColor: 'transparent'});
        target.addClass(disabledButtonCssClass);
        $(document.body).append(mask);
        $(window).resize(function() {
                         var targetOffset = target.offset();
                         mask.offset({
                                     top: targetOffset.top,
                                     left: targetOffset.left});
                         });
        return mask;
        
    }
    
    this.cancelMask = function() {
        mask.hide();
        
    }
    
}
