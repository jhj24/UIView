package com.jhj.uiview.bean;

import java.util.List;

public class BimBean {


    public BimBean(String section_short, int excellent, int qualified) {
        this.section_short = section_short;
        this.excellent = excellent;
        this.qualified = qualified;
    }

    private int key;
    private String section_short;
    private int excellent;
    private int qualified;
    private int un_evaluation;
    private String excellent_percent;
    private List<TimeBean> time;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getSection_short() {
        return section_short;
    }

    public void setSection_short(String section_short) {
        this.section_short = section_short;
    }

    public int getExcellent() {
        return excellent;
    }

    public void setExcellent(int excellent) {
        this.excellent = excellent;
    }

    public int getQualified() {
        return qualified;
    }

    public void setQualified(int qualified) {
        this.qualified = qualified;
    }

    public int getUn_evaluation() {
        return un_evaluation;
    }

    public void setUn_evaluation(int un_evaluation) {
        this.un_evaluation = un_evaluation;
    }

    public String getExcellent_percent() {
        return excellent_percent;
    }

    public void setExcellent_percent(String excellent_percent) {
        this.excellent_percent = excellent_percent;
    }

    public List<TimeBean> getTime() {
        return time;
    }

    public void setTime(List<TimeBean> time) {
        this.time = time;
    }

    public static class TimeBean {
        /**
         * month : 2018-09
         * excellent_rate : 0
         */

        private String month;
        private int excellent_rate;

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public int getExcellent_rate() {
            return excellent_rate;
        }

        public void setExcellent_rate(int excellent_rate) {
            this.excellent_rate = excellent_rate;
        }
    }
}
