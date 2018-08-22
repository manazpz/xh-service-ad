package aq.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

/**
 * JSON工具类Gson的帮助类，考虑到到需要调用Gson对象进行JSON格式转换的地方比较多，如果在每个需要的地方都new Gson()
 * 的话，相对是比较影响性能的，故封装此类采用Gson的单例模式
 * Created by ywb on 2017-02-17.
 */
public class GsonHelper {


    //定义Builder
    private static  GsonBuilder INSTANCE_BUILD = new GsonBuilder()
                                                      .enableComplexMapKeySerialization()      //当Map的key为复杂对象时,需要开启该方法
                                                      .serializeNulls()                        //当字段值为空或null时，依然对该字段进行转换
                                                      .setDateFormat("yyyy-MM-dd HH:mm:ss:SSS")//时间转化为特定格式
                                                      .disableHtmlEscaping();                  //防止特殊字符出现乱码

    //模式-建议模式 （本框架常用）
    private static final Gson INSTANCE = new GsonBuilder()
                                        .enableComplexMapKeySerialization()      //当Map的key为复杂对象时,需要开启该方法
                                        .serializeNulls()                        //当字段值为空或null时，依然对该字段进行转换
                                        .setDateFormat("yyyy-MM-dd HH:mm:ss:SSS")//时间转化为特定格式
                                        .disableHtmlEscaping()                   //防止特殊字符出现乱码
                                        .create();

    //模式-默认
    private static final Gson INSTANCE_DEFAULT = new Gson();

    //定义JsonParser
    private static final JsonParser INSTANCE_JSONPARSER = new JsonParser();

    //模式-特定对象模式   针对自定义对象的序列化和反序列化方法时使用  VIP模式
    public static Gson getInstanceVip(Class<?> baseType,Object SerializerInstance){
        INSTANCE_BUILD = INSTANCE_BUILD.registerTypeHierarchyAdapter(baseType,SerializerInstance);
       return  INSTANCE_BUILD.create();
    }


    public static Gson getInstance() {
        return INSTANCE;
    }

    public static Gson getInstanceDefault(){
        return INSTANCE_DEFAULT;
    }

    public static JsonParser getInstanceJsonparser(){
        return INSTANCE_JSONPARSER;
    }
}
