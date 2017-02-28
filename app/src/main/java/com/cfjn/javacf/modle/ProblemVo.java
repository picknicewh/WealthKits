package com.cfjn.javacf.modle;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/5/9.
 */
public class ProblemVo {
    private  String sign;
    private  String resultCode;
    private String resultDesc;
    private  String createTime;
    private List<Date> date;
    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


    public List<Date> getDate() {
        return date;
    }

    public void setDate(List<Date> date) {
        this.date = date;
    }
   public  class  Date{
       private  String question;
       private  String answer;
       private  int  questionNo;
       private int level;
       public int getLevel() {
           return level;
       }

       public void setLevel(int level) {
           this.level = level;
       }

       public int getQuestionNo() {
           return questionNo;
       }

       public void setQuestionNo(int questionNo) {
           this.questionNo = questionNo;
       }

       public String getQuestion() {
           return question;
       }

       public void setQuestion(String question) {
           this.question = question;
       }

       public String getAnswer() {
           return answer;
       }

       public void setAnswer(String answer) {
           this.answer = answer;
       }
    }

}
