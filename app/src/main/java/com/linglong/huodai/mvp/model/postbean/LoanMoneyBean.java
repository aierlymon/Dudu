package com.linglong.huodai.mvp.model.postbean;

import com.linglong.huodai.ui.adapter.base.BaseMulDataModel;

public class LoanMoneyBean extends BaseMulDataModel {
    private String name;
    private int limit;
    private int max;

    public String getName() {
        return name;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "LoanMoneyBean{" +
                "name='" + name + '\'' +
                ", limit=" + limit +
                ", max=" + max +
                '}';
    }
}
