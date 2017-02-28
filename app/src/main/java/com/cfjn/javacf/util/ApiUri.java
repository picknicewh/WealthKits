package com.cfjn.javacf.util;

/**
 * 作者： ZLL
 * 时间： 2016/7/1
 * 名称： 访问服务端的URI
 * 版本说明：
 * 附加注释：项目所有的URI
 * 主要接口：
 */
public class ApiUri {

    /**
     * 常见问题
     */
    public static final String QUESTION = "/wealthKits/userMessage/question";
    /**
     * 查询版本号
     */
    public static final String QUERY_VERSION = "/authuser/queryVersion";
    /**
     * 意见反馈
     */
    public static final String USER_ADVICE = "/user/userAdvice";
    /**
     * 查询记账信息
     */
    public static final String USER_ACCOUNT_MAIN = "/wealthKits/userAccount/main";
    /**
     * 查询语义解析
     */
    public static final String QUERY_SEGMENT = "/wealthKits/userAccount/querySegment";
    /**
     * 提交记账信息
     */
    public static final String SUBMIT_MESSAGE = "/wealthKits/userAccount/submitMessage";
    /**
     * 按月查询记账信息
     */
    public static final String QUERY_STATISTICS_BY_MONTH = "/wealthKits/userAccount/queryStatisticsByMonth";
    /**
     * 按天查询记账信息
     */
    public static final String QUERY_MESSAGE_BY_DAY = "/wealthKits/userAccount/queryMessageByDay";
    /**
     * 按年查询图表信息
     */
    public static final String QUERY_CHART_BY_YEAR = "/wealthKits/userAccount/queryChartByYear";
    /**
     * 图表详细信息
     */
    public static final String QUERY_CLASSIFY_STATISTICS_BY_DATE = "/wealthKits/userAccount/queryClassifyStatisticsByDate";
    /**
     * 查询预算列表
     */
    public static final String QUERY_USER_BUDGET = "/wealthKits/userBudge/queryUserBudget";
    /**
     * 提交预算
     */
    public static final String USER_BUDGE_SUBMIT_MESSAGE = "/wealthKits/userBudge/submitMessage";
    /**
     * 提交总预算
     */
    public static final String USER_TOTLTBUDGE_SUBMIT_MESSAGE = "/wealthKits/userBudge/addTotalBudget";
    /**
     * 查询走势图
     */
    public static final String GET_CURVE = "/wealthKits/funds/getCurve";
    /**
     * 获取验证码
     */
    public static final String MOBILECODE = "/userCenter/user/checkMobileCode.do";
    /**
     * 登录注册
     */
    public static final String LOGINCHECK = "/userCenter/user/loginCheck.do";
    /**
     * 投资风格测试
     */
    public static final String ASSTESTING = "/wealthKits/userMessage/assessmentTesting";
    /**
     * 投资风格反馈
     */
    public static final String VETTING = "/wealthKits/userMessage/vetting";
    /**
     * 基金超市
     */
    public static final String QUERY_FUND = "/wealthKits/funds/queryFundsList";
    /**
     * 获取对应类型基金总数
     */
    public static final String FUND_TOTAL = "/wealthKits/funds/queryFundsListSize";
    /**
     * 查询用户信息
     */
    public static final String GAINVETTING = "/wealthKits/userMessage/gainVetting";
    /**
     * 判断用户是否开户
     */
    public static final String FUND_REGISTER = "/wealthKits/userMessage/accountRegistrationStatus";
    /**
     * 关注或者取消基金
     */
    public static final String ATTENTION = "/wealthKits/funds/attention";
    /**
     * 获取关注基金
     */
    public static final String QUERYATTERN = "/wealthKits/funds/queryAttention";
    /**
     * 获取我的资产
     */
    public static final String MY_TOTAL = "/wealthKits/userMessage/queryUserAssets";
    /**
     * 获取基金经理
     */
    public static final String MANAGER = "/wealthKits/funds/getFundManager";
    /**
     * 查询基金经理旗下的基金
     */
    public static final String MANAGER_FUND = "/wealthKits/funds/getFundsByManager";
    /**
     * 松果开户界面
     */
    public static final String SONGGUO_VIEW = "/wealthKits/id_validate";
    /**
     * 松果买入
     */
    public static final String SONGGUO_BUY = "/wealthKits/sg/applyForThePurchase";

    /**
     * 松果取现
     */
    public static final String SONGGUO_OUT = "/wealthKits/sg/redeem";

    /**
     * 松果赎回
     */
    public static final String SONGGUO_OUT_SH = "/wealthKits/sg/fundRedeem";

    /**
     * 松果短信验证
     */
    public static final String SONGGUO_SMS = "/wealthKits/sg/shortMessageSending";
    /**
     * 松果交易修改密码
     */
    public static final String SONGGUO_MODIFY_PASS = "/wealthKits/sg/modifyTransactionPassword";

    /**
     * 查询单一基金
     */
    public static final String SOLE_FUND = "/wealthKits/funds/getFundBaseInfo";

    /**
     * 获取用户真实信息
     */
    public static final String GETUSERINFO = "/wealthKits/userMessage/getUserInfo";
    /**
     * 柜台管理
     */
    public static final String ACCOUNTMANGER = "/wealthKits/userMessage/getSecuritiesAccountManagement";
    /**
     * 修改手机号码获取验证码
     */
    public static final String UPDATEPHONE = "/userCenter/user/getUpdatePhoneNumber.do";
    /**
     * 修改手机号
     */
    public static final String SETUPDATEPHONE = "/userCenter/user/setUpdatePhoneNumber.do";
    /**
     * 查询用户是否开过户
     */
    public static final String STATUS = "/wealthKits/userMessage/getKeyInfo";
    /**
     * 检测版本更新
     */
    public static final String CHECKUPADTE = "/wealthKits/userMessage/checkUpdate";
    /**
     * 提交资产，交易记录，银行卡
     */
    public static final String CLIENTDATE = "/wealthKits/userMessage/clientSubmitData";

    /**
     * 资产明细
     */
    public static final String TOTAL_DETAIL = "/wealthKits/userMessage/queryUserAssetsDetails";

    /**
     * 获取单个基金交易记录
     */
    public static final String TRANSATION = "/wealthKits/userMessage/getTransactionRecords";

    /**
     * 获取单个基金收益记录
     */
    public static final String FUND_RECORD = "/wealthKits/userMessage/getAFundCodeRevenueRecord";

    /**
     * 获取资产走势图
     */
    public static final String ANALYSIS_CHART = "/wealthKits/userMessage/assetAnalysisChart";
    /**
     * 数米开户后保存用户信息
     */
    public static final String SAVEUSERINFO = "/wealthKits/userMessage/saveUserInfo";
    /**
     * 系统消息
     */
    public static final String SYSTMEMANGER = "/wealthKits/userMessage/systemNotification";

    /**
     * 资产分析
     */
    public static final String ANALYSIS = "/wealthKits/userMessage/assetsAnalysis";

    /**
     * 提交推荐码
     */
    public static final String RECOMMEND = "/wealthKits/userMessage/setRecommendation";

    /**
     * 查询推荐码
     */
    public static final String GETRECOMMEND = "/wealthKits/userMessage/getRecommendation";

    /**
     * 获取全部基金交易记录
     */
    public static final String ALLTRANSATION = "/wealthKits/userMessage/getAllTransactionRecords";

    /**
     * 松果修改修改银行卡
     */
    public static final String REPLACEBANKCARD = "/wealthKits/replace_bankcard1";

    /**
     * 获取对应柜台银行卡列表
     */
    public static final String QUERYBANKCARD = "/wealthKits/userMessage/bankCardInquiry";

    /**
     * 获取广告图
     */
    public static final String RECOMMENDTION = "/wealthKits/load/getAdvertisingChart";

    /**
     * 天风是否需要登录
     */
    public static final String TFLOGIN = "/wealthKits/auxiliary/isTfLogin";

    /**
     * 查询是否与需要提交数米交易记录
     */
    public static final String TRAdEDCCOUNT = "/wealthKits/auxiliary/isUpdateTradeAccount";
    /**
     * 修改手机号码传给服务端
     */
    public static final String UPDATEFUNDMODEILE = "/wealthKits/userMessage/updateFundMobile";

    /**
     * 查询现金宝余额
     */
    public static final String SG_QUERYBALANCE = "/wealthKits/sg/getBalance";

    /**
     * 松果撤单
     */
    public static final String SG_REVOKE = "/wealthKits/sg/killAnOrder";
    /**
     * 资源包下载
     */
    public static final String SOURCE_DOWNLOAD = "/wealthKits/auxiliary/resourcePackDownload/";
    /**
     * 消息推送接口
     */
    public static  final String JPUSH = "/userCenter/user/setRegistrationId.do";

}
