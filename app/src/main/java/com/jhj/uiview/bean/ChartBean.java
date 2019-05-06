package com.jhj.uiview.bean;

import java.util.List;

public class ChartBean {
    private int maxLength;
    private String title;
    private List<ItemBean> list;

    public class ItemBean {
        private String type;
        private int excellentNum;
        private int qualifiedNum;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getExcellentNum() {
            return excellentNum;
        }

        public void setExcellentNum(int excellentNum) {
            this.excellentNum = excellentNum;
        }

        public int getQualifiedNum() {
            return qualifiedNum;
        }

        public void setQualifiedNum(int qualifiedNum) {
            this.qualifiedNum = qualifiedNum;
        }
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ItemBean> getList() {
        return list;
    }

    public void setList(List<ItemBean> list) {
        this.list = list;
    }
}
