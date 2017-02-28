package com.cfjn.javacf.modle;


import java.util.List;

/**
 * Created by DELL on 2016/1/13.
 */
public class AccountManagementVo {

    private UserInformationVo userInformation;
    private List<UserBankcardListVo> userBankCardList;
    private String securitiesAccountName;
    private String prompt;

    public UserInformationVo getUserInformation() {
        return userInformation;
    }

    public void setUserInformation(UserInformationVo userInformation) {
        this.userInformation = userInformation;
    }

    public List<UserBankcardListVo> getUserBankCardList() {
        return userBankCardList;
    }

    public void setUserBankCardList(List<UserBankcardListVo> userBankCardList) {
        this.userBankCardList = userBankCardList;
    }

    public String getSecuritiesAccountName() {
        return securitiesAccountName;
    }

    public void setSecuritiesAccountName(String securitiesAccountName) {
        this.securitiesAccountName = securitiesAccountName;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}
