cordova.define("com.openhunme.cordova.china-pay.ChinaPay", function(require, exports, module) {
//
// Copyright (c) 2014 PayPal. All rights reserved.
//

/**
 * This class exposes the PayPal iOS SDK functionality to javascript.
 *
 * @constructor
 */
function ChinaPay(){};

var exec = require('cordova/exec');
/**
 * 获取银联支付SDK的版本号等信息
 *
 * @param {Function} completionCallback: a callback function accepting a string
 */
ChinaPay.prototype.version = function(completionCallback) {
    var failureCallback = function() {
        console.log("Could not retrieve ChinaPay library version");
    };
    
    exec(completionCallback, failureCallback, "ChinaPay", "version", []);
};


/**
 * 调用SDK前做的初始化工作
 * The ChinaPay Mobile SDK can operate in different environments to facilitate development and testing.
 *
 * @param {Object} clientIdsForEnvironments: set of client ids for environments
 * Example: var clientIdsForEnvironments = {
 *                    ChinaPayEnvironment: @"production"
 *                   }
 *  or
 *          var clientIdsForEnvironments = {
 *                    ChinaPayEnvironment: @"sandbox"
 *                   }
 * @param {Function} completionCallback: a callback function on success
 */
ChinaPay.prototype.init = function(clientIdsForEnvironments, completionCallback) {
    
    var failureCallback = function(error) {
        console.log(error);
    };
    
    exec(completionCallback, failureCallback, "ChinaPay", "init", [clientIdsForEnvironments]);
};

/**
 * 银联用户发起实名认证
 *
 * @param {Object} args: set of user params for making auth request
 * @param {Function} completionCallback: a callback function accepting a js object with future payment authorization
 * @param {Function} failureCallback: a callback function accepting a reason string, called when the plugin failure callback
 */
ChinaPay.prototype.auth = function(args, completionCallback, failureCallback) {
    exec(completionCallback, failureCallback, "ChinaPay", "auth", args);
};

/**
 * Plugin setup boilerplate.
 */
module.exports = ChinaPay;

});