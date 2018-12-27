package com.jhj.uiview.bean;

public class HistogramBean {

    private String name;
    private float percent;

    public HistogramBean(String name, float percent) {
        this.name = name;
        this.percent = percent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }
}
