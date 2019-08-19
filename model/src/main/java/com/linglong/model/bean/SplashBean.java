package com.linglong.model.bean;

import java.util.List;

public class SplashBean {

    /**
     * data : ["http://tang.rontloan.cn/yuan?inviteCode=xiaxia1123","http://tang.rontloan.cn/yuan?inviteCode=xiaxia1123"]
     * statusCode : 200
     * msg : 请求成功！
     */

    private int statusCode;
    private String msg;
    private List<String> data;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
