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
import java.util.List;
import java.util.Map;

public class RequestFilter extends aq.common.interceptor.RequestFilter {

    @Resource
    private SystemService systemService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Context context = (Context)Factory.getContext();
        boolean isAccessed =  super.preHandle(request,response,handler);
        //请求参数
        JsonObject res = new JsonObject();
        //token数据
        JsonObject tokens = new JsonObject();
        //存放用户信息
        JsonObject users = new JsonObject();
        //存放权限列表
        if(isAccessed) {
            String token = request.getHeader("X-Token");
            HandlerMethod method = (HandlerMethod)handler;
            Permission permission = method.getMethodAnnotation(Permission.class);

            //判断token是否有效
            if(!StringUtil.isEmpty(token)){
                //校验token
                res.addProperty("token", token);
                JsonObject tokenJson = systemService.queryToken(res);
                if (!"200".equals(tokenJson.get("code").getAsString())){
                    writerClient(response, tokenJson);
                    return false;
                }else {
                    tokens = tokenJson.get("data").getAsJsonObject();
                }

                //获取用户信息
                JsonObject userJson = systemService.queryUserInfo(res);
                if (!"200".equals(userJson.get("code").getAsString())){
                    writerClient(response, userJson);
                    return false;
                }else {
                    users = userJson.get("data").getAsJsonObject();
                }

//                //判断站点
//                if (!"AD".equals(users.get("statusKey").getAsString())) {
//                    res.addProperty("type","WS");
//                    JsonObject webJson = systemService.querySwitch(res);
//                    if(webJson != null && ((JsonObject)webJson.get("data")).get("items").getAsJsonArray().size() > 0) {
//                        JsonObject item = (JsonObject) ((JsonObject)webJson.get("data")).get("items").getAsJsonArray().get(0);
//                        if("N".equals(item.get("isSwitch").getAsString())) {
//                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Web Close");
//                            return false;
//                        }
//                    }
//                }

                //获取权限
                PermissionCollection ap = null;
                res.addProperty("administratorId", users.get("id").getAsString());
                JsonObject permissionJson = systemService.querySysPermissionUser(res);
                JsonArray apArray = permissionJson.get("data").getAsJsonObject().get("items").getAsJsonArray();
                List<Map<String, Object>> aps = GsonHelper.getInstance().fromJson(apArray, List.class);
                HashMap<String, aq.common.access.Permission> permissionItemHashMap = new HashMap<>();
                if(aps.size() > 0) {
                    for (Map<String, Object> m : aps) {
                        permissionItemHashMap.put(m.get("name").toString(), new aq.common.access.Permission(aps.indexOf(m), m.get("type").toString(),"", m.get("module").toString(), m.get("name").toString(), m.get("id").toString()));
                    }
                }
                ap = new PermissionCollection(permissionItemHashMap);
                ap.addAllPermission();

                //创建令牌，有待优化
                request.setAttribute("token", tokens.get("token").getAsString());
                request.setAttribute("userId", users.get("id").getAsString());
                request.setAttribute("sessionId", users.get("id").getAsString());
                Ticket ticket = context.ticket();
                AccessUser accessUser = new AccessUser("",ticket, users.get("userName").getAsString(), users.get("nickName").getAsString(), ap, AuthType.ALIPAY, "");
                Factory.getContext().login(accessUser);
            }

            //校验权限
            if(permission != null) {
                Boolean flag = false;
                AccessUser accessUser = (AccessUser) context.user();
                PermissionCollection permissionCollection = accessUser.getPermissionCollection();
                if (permissionCollection == null) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "No authority");
                }else {
                    List<aq.common.access.Permission> allPermission = permissionCollection.getAllPermission();
                    for (aq.common.access.Permission obj : allPermission) {
                        if("AM".equals(obj.getModule())) {
                            flag = true;
                            break;
                        }
                    }
                    if(flag){
                        isAccessed = true;
                    }else {
                        if (permissionCollection.containAny(permission.value()) == false){
                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Permission check failed");
                            return false;
                        }else {
                            String[] value = permission.value();
                            //过滤条件
                            for (String str : value) {
                                res.addProperty("id",str);
                                JsonObject jsonObject = systemService.querySysPermissionInfo(res);
                            }
                        }
                    }
                }
                request.setAttribute("AM",flag);
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
