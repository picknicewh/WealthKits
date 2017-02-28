package com.cfjn.javacf.modle;

import java.util.List;

/**
 * Created by DELL on 2016/1/19.
 */
public class GetKeyInfoVo extends MenberCent {

    /**
     * source : 0
     * token : 4531d98fea3442919013de574484d8b5
     * tokenSecret : 6276087cb46b4ff699c35becb5203e8a
     * recordTimeList : [{"startTime":"2016-01-12","objective":1}]
     */

    private List<DateEntity> date;

    public void setDate(List<DateEntity> date) {
        this.date = date;
    }

    public List<DateEntity> getDate() {
        return date;
    }

    public static class DateEntity {
        private String source;
        private String token;
        private String tokenSecret;
        /**
         * startTime : 2016-01-12
         * objective : 1
         */

        private List<RecordTimeListEntity> recordTimeList;

        public void setSource(String source) {
            this.source = source;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public void setTokenSecret(String tokenSecret) {
            this.tokenSecret = tokenSecret;
        }

        public void setRecordTimeList(List<RecordTimeListEntity> recordTimeList) {
            this.recordTimeList = recordTimeList;
        }

        public String getSource() {
            return source;
        }

        public String getToken() {
            return token;
        }

        public String getTokenSecret() {
            return tokenSecret;
        }

        public List<RecordTimeListEntity> getRecordTimeList() {
            return recordTimeList;
        }

        public static class RecordTimeListEntity {
            private String startTime;
            private int objective;

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public void setObjective(int objective) {
                this.objective = objective;
            }

            public String getStartTime() {
                return startTime;
            }

            public int getObjective() {
                return objective;
            }
        }
    }
}
