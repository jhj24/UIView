package com.jhj.uiview.bean;

import java.math.BigDecimal;

public class HistogramBean {

    private String content;
    private double frequency;

    public HistogramBean(String content, float frequency) {
        this.content = content;
        this.frequency = frequency;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getFrequency() {
        BigDecimal bigDecimal = new BigDecimal(frequency);
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }
}
