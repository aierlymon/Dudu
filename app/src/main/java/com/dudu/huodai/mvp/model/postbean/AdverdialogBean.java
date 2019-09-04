package com.dudu.huodai.mvp.model.postbean;

public class AdverdialogBean {
    private int type;
    private boolean isSuccess;//用于答题的

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
