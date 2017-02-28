package com.cfjn.javacf.modle;

/**
 * Created by DELL on 2016/1/13.
 */
public class PersonalVo extends MenberCent {

    private PersonalManger date;

    public PersonalManger getDate() {
        return date;
    }

    public void setDate(PersonalManger date) {
        this.date = date;
    }

  public  class PersonalManger{
        private String loginName;
        private String name;
        private String idNumber;

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIdNumber() {
            return idNumber;
        }

        public void setIdNumber(String idNumber) {
            this.idNumber = idNumber;
        }
    }
}
