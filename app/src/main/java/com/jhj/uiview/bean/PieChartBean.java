package com.jhj.uiview.bean;

import java.math.BigDecimal;

public class PieChartBean {

    private String content;
    private double frequency;
    private int color;

    public PieChartBean(String content, double frequency, int color) {
        this.content = content;
        this.frequency = frequency;
        this.color = color;
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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
