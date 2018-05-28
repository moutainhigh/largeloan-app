package com.xianjinxia.cashman.response;

public class MQResponse {

    private String code;

    public MQResponse(){
        this.code = Code.SUCCESS.getCode();
    }

    public MQResponse(String code){
        this.code = code;
    }

    public enum Code{
        SUCCESS("00"),
        ERROR("-1");

        private String code;

        Code(String code){
            this.code = code;
        }

        public String getCode(){
            return this.code;
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
