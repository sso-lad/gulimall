package com.zhou.common.exception;

public enum BizCodeErume {
    UNKNOW_EXCEPTION(10000,"系统未知异常"),
    VALUE_EXCEPTION(10001,"参数格式校验失败"),
    PRODUCT_UP_EXCEPTION(11000,"商品上架错误");

    private  int code;
    private  String msg;
    BizCodeErume(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
