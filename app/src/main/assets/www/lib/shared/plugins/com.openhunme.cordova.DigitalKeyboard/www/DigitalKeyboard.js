cordova.define("com.openhunme.cordova.DigitalKeyboard.DigitalKeyboard", function(require, exports, module) {
//
// Copyright (c) 2014 TFZQ. All rights reserved.
//
               
/**
 * This class exposes the FileLoad SDK functionality to javascript.
 *
 * @constructor
 */
	function DigitalKeyboard (){};
	   
	var exec = require('cordova/exec');
/**
 * @Author: 罗成毅
 * 启动密码键盘：
 * DigitalKeyboard.active({"length": 6, "text": ""})  其中length：文本最大长度，text：文本框中已有的内容
 * @param {Function} completionCallback: 接受一个object对象 对象：{text:""}; text属性对应键盘输入的文本内容；
 */
DigitalKeyboard.prototype.active = function(obj, completionCallback) {
    if(typeof obj=='object' || obj.constructor == Object)
           exec(completionCallback, null, "DigitalKeyboard", "active", [obj]);
    };
    
/**
 * @Author: 罗成毅
 * 关闭密码键盘：
 *  * @param {Function} completionCallback: 接受一个object对象 对象：{"isok":BOOl}; isok为是否成功，true，成功，false：失败；
 */
DigitalKeyboard.prototype.close = function( completionCallback) {
          exec(completionCallback, null, "DigitalKeyboard", "close",[]);
    };
    
//DigitalKeyboard.prototype.keyBack = function() {
//    if(typeof txtObj=='object' || txtObj.constructor == Object)
//           txtObj.val(data);
//    }; 
    
    /**
     * Plugin setup boilerplate.
     */
    module.exports = new DigitalKeyboard();
});


