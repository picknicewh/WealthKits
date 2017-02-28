package com.cfjn.javacf.modle;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/22.
 */
public class TransactionRecordsVo implements Serializable{
    /**
     * 用户ID
     */
    private String secretKey;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    /**
     * 基金来源
     */
    private String source;
    /**
     * 基金代码
     */
    private String FundCode;
    /**
     * 基金名称
     */
    private String FundName;
    /**
     * 银行编号
     */
    private String BankSerial;

    /**
     * 银行名称
     */
    private String BankName;

    /**
     * 银行卡号
     */
    private String BankAccount;

    /**
     * 交易帐号<br>
     */
    private String TradeAccount;
    /**
     * 份额类别 见字典帮助类，查询
     */
    private String ShareType;

    /**
     * 收费方式 中文
     */
    private String ShareTypeToCN;

    /**
     * 申请时间<br>
     * 输出为 C# DateTime.ToString()<br>
     * java使用DateFormat格式 yyyy-MM-dd'T'HH:mm:ss
     */
    private String ApplyDateTime;

    /**
     * 确认时间<br>
     * 输出为 C# DateTime.ToString()<br>
     * java使用DateFormat格式 yyyy-MM-dd'T'HH:mm:ss
     */
    private String ConfirmDate;

    /**
     * 赎回到账时间
     */
    private String RedeemAccountDate;

    /**
     * 业务类型 见字典帮助类，查询
     */
    private String BusinessType;

    /**
     * 业务类型（中文）
     */
    private String BusinessTypeToCN;
    /**
     * 申请金额
     */
    private Double Amount;
    /**
     * 申请份额
     */
    private Double Shares;
    /**
     * 确认状态<br>
     * 见字典帮助类，查询
     */
    private Integer Status;

    /**
     * 确认状态 中文
     */
    private String StatusToCN;

    /**
     * 申请流水号
     */
    private String ApplySerial;
    /**
     * 是否可撤单
     */
    private Integer CanCancel;
    /**
     * 支付结果 扣款状态<br>
     * 见字典帮助类，查询
     */
    private Integer PayResult;

    /**
     * 支付结果 中文
     */
    private String PayStatusToCN;

    /**
     * 手续费
     */
    private Double PoundAge;

    /**
     * 大额赎回状态
     */
    private String HugeWithdrawalStatus;

    /**
     * 大额赎回状态（中文）
     */
    private String HugeWithdrawalStatusToCN;

    /**
     * 定投协议编号
     */
    private String AggreementNo;

    /**
     * 均线类型
     */
    private String ExpType;

    /**
     * 是否是现金宝基金
     */
    private Integer IsCash;

    /**
     * 是否是现金宝赎回转购的
     */
    private Integer IsCashBuy;

    public String getExpectedToConfirm() {
        return expectedToConfirm;
    }

    public void setExpectedToConfirm(String expectedToConfirm) {
        this.expectedToConfirm = expectedToConfirm;
    }

    /**
     * 预计确认时间
     */
    private String expectedToConfirm;
    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getFundCode() {
        return FundCode;
    }

    public void setFundCode(String fundCode) {
        FundCode = fundCode;
    }

    public String getFundName() {
        return FundName;
    }

    public void setFundName(String fundName) {
        FundName = fundName;
    }

    public String getBankSerial() {
        return BankSerial;
    }

    public void setBankSerial(String bankSerial) {
        BankSerial = bankSerial;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public String getBankAccount() {
        return BankAccount;
    }

    public void setBankAccount(String bankAccount) {
        BankAccount = bankAccount;
    }

    public String getTradeAccount() {
        return TradeAccount;
    }

    public void setTradeAccount(String tradeAccount) {
        TradeAccount = tradeAccount;
    }

    public String getShareType() {
        return ShareType;
    }

    public void setShareType(String shareType) {
        ShareType = shareType;
    }

    public String getShareTypeToCN() {
        return ShareTypeToCN;
    }

    public void setShareTypeToCN(String shareTypeToCN) {
        ShareTypeToCN = shareTypeToCN;
    }

    public String getApplyDateTime() {
        return ApplyDateTime;
    }

    public void setApplyDateTime(String applyDateTime) {
        ApplyDateTime = applyDateTime;
    }

    public String getConfirmDate() {
        return ConfirmDate;
    }

    public void setConfirmDate(String confirmDate) {
        ConfirmDate = confirmDate;
    }

    public String getRedeemAccountDate() {
        return RedeemAccountDate;
    }

    public void setRedeemAccountDate(String redeemAccountDate) {
        RedeemAccountDate = redeemAccountDate;
    }

    public String getBusinessType() {
        return BusinessType;
    }

    public void setBusinessType(String businessType) {
        BusinessType = businessType;
    }

    public String getBusinessTypeToCN() {
        return BusinessTypeToCN;
    }

    public void setBusinessTypeToCN(String businessTypeToCN) {
        BusinessTypeToCN = businessTypeToCN;
    }

    public Double getAmount() {
        return Amount;
    }

    public void setAmount(Double amount) {
        Amount = amount;
    }

    public Double getShares() {
        return Shares;
    }

    public void setShares(Double shares) {
        Shares = shares;
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }

    public String getStatusToCN() {
        return StatusToCN;
    }

    public void setStatusToCN(String statusToCN) {
        StatusToCN = statusToCN;
    }

    public String getApplySerial() {
        return ApplySerial;
    }

    public void setApplySerial(String applySerial) {
        ApplySerial = applySerial;
    }

    public Integer getCanCancel() {
        return CanCancel;
    }

    public void setCanCancel(Integer canCancel) {
        CanCancel = canCancel;
    }

    public Integer getPayResult() {
        return PayResult;
    }

    public void setPayResult(Integer payResult) {
        PayResult = payResult;
    }

    public String getPayStatusToCN() {
        return PayStatusToCN;
    }

    public void setPayStatusToCN(String payStatusToCN) {
        PayStatusToCN = payStatusToCN;
    }

    public Double getPoundAge() {
        return PoundAge;
    }

    public void setPoundAge(Double poundAge) {
        PoundAge = poundAge;
    }

    public String getHugeWithdrawalStatus() {
        return HugeWithdrawalStatus;
    }

    public void setHugeWithdrawalStatus(String hugeWithdrawalStatus) {
        HugeWithdrawalStatus = hugeWithdrawalStatus;
    }

    public String getHugeWithdrawalStatusToCN() {
        return HugeWithdrawalStatusToCN;
    }

    public void setHugeWithdrawalStatusToCN(String hugeWithdrawalStatusToCN) {
        HugeWithdrawalStatusToCN = hugeWithdrawalStatusToCN;
    }

    public String getAggreementNo() {
        return AggreementNo;
    }

    public void setAggreementNo(String aggreementNo) {
        AggreementNo = aggreementNo;
    }

    public String getExpType() {
        return ExpType;
    }

    public void setExpType(String expType) {
        ExpType = expType;
    }

    public Integer getIsCash() {
        return IsCash;
    }

    public void setIsCash(Integer isCash) {
        IsCash = isCash;
    }

    public Integer getIsCashBuy() {
        return IsCashBuy;
    }

    public void setIsCashBuy(Integer isCashBuy) {
        IsCashBuy = isCashBuy;
    }

    public Integer getIsExchange() {
        return IsExchange;
    }

    public void setIsExchange(Integer isExchange) {
        IsExchange = isExchange;
    }

    /**
     * 是否可兑换米币
     */
    private Integer IsExchange;


}
