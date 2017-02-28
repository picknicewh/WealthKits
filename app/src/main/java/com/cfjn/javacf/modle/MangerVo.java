package com.cfjn.javacf.modle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DELL on 2016/1/25.
 */
public class MangerVo extends MenberCent implements Serializable{
    private List<SystemNotification>date;

    public List<SystemNotification> getDate() {
        return date;
    }

    public void setDate(List<SystemNotification> date) {
        this.date = date;
    }

   public class SystemNotification implements Serializable {
        private String source;
        private String title;
        private String content;
        private String dateTime;
        private int alreadySent;
        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

       public int getAlreadySent() {
           return alreadySent;
       }

       public void setAlreadySent(int alreadySent) {
           this.alreadySent = alreadySent;
       }
   }
}
