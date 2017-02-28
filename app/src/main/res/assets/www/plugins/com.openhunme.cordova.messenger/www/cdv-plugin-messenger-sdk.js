cordova.define("com.openhunme.cordova.messenger.Messenger", function(require, exports, module) {
//
// Copyright (c) 2014 HUNME. All rights reserved.
//
               
/**
 * This class exposes the Messenger SDK functionality to javascript.
 *
 * @constructor
 */
   function Messenger (){};
   
   var exec = require('cordova/exec');
/**
 * 给原生系统发送消息，比如关闭当前窗口
 * Messenger.send("close")
 * @param msgs a string message
 */
    Messenger.prototype.send = function(msgs) {
    if(typeof msgs=='string' || msgs.constructor == String)
        exec(null, null, "Messenger", "handleMessage", [{"msg":msgs}]);
    };
/**
 * 给原生系统发送消息对象，并等待
 * @param type a string message 消息名称
 * @param {Object} msg: set of user params for making request
 * @param {Function} completionCallback: a callback function accepting a js object with response
 * @param {Function} failureCallback: a callback function accepting a reason string, called when the plugin failure callback
 * 1、升级安装应用程序（安卓版包含下载apk功能）
 * Messenger.sendMsg("updateApp",{"type":1,//1:可选升级；2:强制升级
                      "appName":"天风小财迷",
                      "versionName":"1.0.8",
                      "versionCode":2056,
                      "content":"新版本\n1.新增A功能\n2.新增B功能\n3.新增C功能",
                      "downloadUrl":"http://km.tfzq.com/tflc/internetDownload/android.apk"}, 
                      onUpdateExit, null);//\n表示换行
 * 返回：(用户取消或者失败时返回，正常安装原程序直接关闭)
 *   {
 *    "ret": -1,
 *    "message": "用户取消升级",
 *   }
 *  或
 *   {
 *    "ret": -2,
 *    "message": "下载升级程序失败",
 *   }
 */
    Messenger.prototype.sendMsg = function(type, msg, completionCallback, failureCallback) {
       exec(completionCallback, failureCallback, "Messenger", "handleMessageObject", [{"msg":type,"obj":msg}]);
    };
/**
 * 给原生系统发送消息，并等待
 * @param {Object} msgs: set of user params for making request
 * @param {Function} completionCallback: a callback function accepting a js object with response
 * @param {Function} failureCallback: a callback function accepting a reason string, called when the plugin failure callback
 *
 * 1、查询壳版本
 * Messenger.request("shellVersion", onShellVersion, null);
 * 返回：
 *   {
 *    "version": "1.0.3",
 *    "buildNo": "1001"
 *   }
 * 2、查询手机设备信息
 * Messenger.request("deviceExt", onDeviceExt, null);
 * 返回：
 *   {
 *    "mac": "020000000000",
 *    "platform": "iOS",
 *    "version": "8.1.1",
 *    "uuid": "GHC705WQ-D990-4853-9494-2638686692AO"
 *   }
 */
   Messenger.prototype.request = function(msgs, completionCallback, failureCallback) {
       exec(completionCallback, failureCallback, "Messenger", "handleMessage", [{"msg":msgs}]);
   };
/**
 * Plugin setup boilerplate.
 */
   module.exports = new Messenger();
});