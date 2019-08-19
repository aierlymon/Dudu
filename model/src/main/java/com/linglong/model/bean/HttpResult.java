package com.linglong.model.bean;

/**
 * createBy ${huanghao}
 * on 2019/6/28
 * data 这个是作为如果有相同的数据请求返回，可以外面再包一层
 */
public class HttpResult<T> {

    /**
     * data : {"banners":[{"id":3,"title":"快速下款","icon":"/group1/default/20190630/22/32/8/微信图片_20190417112246.png","url":"http://tang.rontloan.cn/yuan?inviteCode=xiaxia1123","open":true,"createTime":1561905152},{"id":5,"title":"身份证审核，快速放款","icon":"/group1/default/20190715/13/54/8/49066eb29cc24f95b9354a897fecc1f8_th.png","url":"http://tuershiting.com/h5/#/?from=neibutest","open":true,"createTime":1563170284},{"id":6,"title":"低利息","icon":"/group1/default/20190715/14/20/8/微信图片_20190715141959.jpg","url":"","open":true,"createTime":1563171647}]}
     * statusCode : 200
     * msg : 获取成功！
     */

    private T data;
    private int statusCode;
    private String msg;


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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
