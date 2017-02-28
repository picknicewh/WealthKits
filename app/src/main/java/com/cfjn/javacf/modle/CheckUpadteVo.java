package com.cfjn.javacf.modle;

/**
 * Created by DELL on 2016/1/19.
 */
public class CheckUpadteVo extends MenberCent {

    /**
     * url : http://zhu.hunme.net:8080/bestlock/apk/BestLock-library.apk
     * version : 2
     * action : 2
     * explain : 版本更新说明
     */

    private DateEntity date;

    public void setDate(DateEntity date) {
        this.date = date;
    }

    public DateEntity getDate() {
        return date;
    }

    public static class DateEntity {
        private String url;
        private String version;
        private int action;
        private String explain;

        public void setUrl(String url) {
            this.url = url;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public void setAction(int action) {
            this.action = action;
        }

        public void setExplain(String explain) {
            this.explain = explain;
        }

        public String getUrl() {
            return url;
        }

        public String getVersion() {
            return version;
        }

        public int getAction() {
            return action;
        }

        public String getExplain() {
            return explain;
        }
    }
}
