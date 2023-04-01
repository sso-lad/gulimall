package com.zhou.common.constant;

public class WareContant {

    public enum  PurchaseEnum{
        CREATED(0,"新建"),ASSIGNED(1,"已分配")
        ,RECEIVE(2,"已领取"),FINISH(3,"已完成"),HASERROR(4,"有异常");
        PurchaseEnum(int code,String msg){
            this.code = code;
            this.msg = msg;
        }
        private int code;
        private String msg;

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }

    public enum  PurchaseDetailEnum{
        CREATED(0,"新建"),ASSIGNED(1,"已分配")
        ,BUYING(2,"正在采购"),FINISH(3,"已完成"),HASERROR(4,"采购失败");
        PurchaseDetailEnum(int code,String msg){
            this.code = code;
            this.msg = msg;
        }
        private int code;
        private String msg;

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }
}
