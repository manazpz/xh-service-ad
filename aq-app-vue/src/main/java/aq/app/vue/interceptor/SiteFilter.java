package aq.app.vue.interceptor;


import aq.common.access.*;
import aq.common.access.AccessUser;
import aq.common.annotation.Permission;
import aq.common.util.GsonHelper;
import aq.common.util.StringUtil;
import aq.service.system.SystemService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.web.method.HandlerMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class SiteFilter extends aq.common.interceptor.RequestFilter {

    @Resource
    private SystemService systemService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean isAccessed =  super.preHandle(request,response,handler);
        //请求参数
        JsonObject res = new JsonObject();
        //存放权限列表
        if(isAccessed) {
            //判断站点
            res.addProperty("type","WS");
            JsonObject webJson = systemService.querySwitch(res);
            if(webJson != null && ((JsonObject)webJson.get("data")).get("items").getAsJsonArray().size() > 0) {
                JsonObject item = (JsonObject) ((JsonObject)webJson.get("data")).get("items").getAsJsonArray().get(0);
                if("N".equals(item.get("isSwitch").getAsString())) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Web Close");
                    return false;
                }
            }
        }
        return isAccessed;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws IOException {
        PrintWriter out = response.getWriter();

        //记录日志  +  异常处理
        if (ex !=null){
            out.println("{\"result\":4}");   //4 发生异常
        }

        out.close();
    }

    //输出JsonObject至客户端
    public  void writerClient(HttpServletResponse response, JsonObject object) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json; charset=UTF-8");
        out.print(object);
        out.close();
    }

}
