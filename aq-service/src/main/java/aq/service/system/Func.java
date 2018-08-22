package aq.service.system;

import aq.common.other.Rtn;
import aq.common.serializer.SerializerRtn;
import aq.common.util.GsonHelper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


import java.util.function.Function;

/*
* 常用匿名函数封装
* create by ywb 2018-05-23
* */
public class Func {

    //Rtn 对象转JsonObject
    public static Function<Rtn,JsonObject> functionRtnToJsonObject = (p)->{
        Gson gson = GsonHelper.getInstanceVip(Rtn.class,new SerializerRtn());
        return GsonHelper.getInstanceJsonparser().parse(gson.toJson(p)).getAsJsonObject();
    };

}
