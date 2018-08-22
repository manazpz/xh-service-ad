package aq.common.other;

import com.google.gson.JsonObject;

/*
* Created by yangwanbin on 2017-05-12
* 自定义返回值类型 服务端与前端约定好的返回值规则，后端和UI层均按约定提供对应的解析方法
* controller层返回Rtn对象的Json格式
* */
public class Rtn {

    //服务模块
    private String service;

    //状态码  200:success 500:Exception
    private Integer code;

    //消息
    private String message;

    //日志ID
    private String logId;

    //来源
    private String from;

    //数据对象
    private JsonObject data;

    public Rtn(){ }

    public Rtn(String service){
        this.service = service;
    }

    public  void setCode(int code){
        this.code = code;
    }

    public void setService(String service){
        this.service = service;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public void setLogId(String logId){
        this.logId = logId;
    }

    public void setFrom(String from){ this.from = from;}

    public  void setData(JsonObject data){
        this.data = data;
    }

    public Integer getCode(){
        return this.code;
    }

    public String getService(){ return this.service;}

    public String getMessage() {
        return this.message;
    }

    public String getLogId() {
        return this.logId;
    }

    public String getFrom(){ return this.from;}

    public JsonObject getData() {
        return this.data;
    }

}
