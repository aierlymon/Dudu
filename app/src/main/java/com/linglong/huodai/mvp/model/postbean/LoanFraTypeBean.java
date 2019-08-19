package com.linglong.huodai.mvp.model.postbean;

import com.linglong.huodai.ui.adapter.base.BaseMulDataModel;

public class LoanFraTypeBean extends BaseMulDataModel {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int  itemI_pos;

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "LoanFraTypeBean{" +
                "name='" + name + '\'' +
                ", itemI_pos=" + itemI_pos +
                ", id=" + id +
                '}';
    }
}
