cordova.define("com.openhunme.cordova.idCardScaner.IDCardScanner", function(require, exports, module) {
//
// Copyright (c) 2014 天风. All rights reserved.
//
               
/**
 * This class exposes the IDScanner SDK functionality to javascript.
 *
 * @constructor
 */
   function IDCardScanner (){};

   var exec = require('cordova/exec');
/**
 * @Author: 王伟
 * 弹出身份证扫描窗口，完成扫描并返回相应信息：
 * 
 * @param {Function} success: 成功回调函数。回到函数参数：json字符串：json键名含义：{base64str: 图片base64，
 *																					 type：正反面类型(1:正面 0:反面),
 *																					  code: 身份证号，
 *																					  name:姓名，
 *																					  gender:性别,
 *																					  nation：名族，
 *																					   address：住址，
 *																					    签发机关：issue，
 *																					     valid:有效期  格式：YYYY.MM.DD-YYYY.MM.DD
 *																					     starttime: 起始时间；格式：YYYYMMDD
 *																					     endtime: 结束时间}；格式：YYYYMMDD/长期
 * 注意：json字符串参数，包含且仅包含这些键名。
 * */
    IDCardScanner.prototype.popScanner = function(success) {
        document.addEventListener(
        "deviceready"
        , function (){cordova.exec(success, null, "CardScan", "popScanner", []);}
        , false);
    };
               
               
/**
 * Plugin setup boilerplate.
 */
    module.exports = new IDCardScanner();
    });
