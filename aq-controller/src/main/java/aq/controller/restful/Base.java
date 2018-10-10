package aq.controller.restful;

import aq.common.util.GsonHelper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by ywb on 2017-02-15.
 * controller 层基类
 */
public class Base {

    //输出JsonObject至客户端
    public  void writerJson(HttpServletResponse response, PrintWriter out, JsonObject object){
        response.setContentType("application/json; charset=UTF-8");
        out.print(object);
        out.close();
    }

    //针对HttpGet方式 将Request参数封装成JsonObject对象
    private JsonObject toJsonObject(HttpServletRequest request){
       return  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(request.getParameterMap())).getAsJsonObject();
    }


    protected String responseJson(HttpServletResponse response, PrintWriter out, Map map) {
        response.setContentType("application/json;charset=utf-8");
        Gson gson = new Gson();
        out.println(gson.toJson(map));
        return null;
    }

}
