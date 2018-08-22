package aq.controller.restful.management;

import aq.common.access.PermissionType;
import aq.common.annotation.Permission;
import aq.common.util.HttpUtil;
import aq.common.util.StringUtil;
import aq.service.system.UserService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Controller
@RequestMapping("/user")
public class UserController extends aq.controller.restful.System {
    @Resource
    protected UserService userService;

    //添加用户
    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    @Permission(RequireLogin=true, PermissionType = PermissionType.ACTION, value = {"7496770D-6772-4CC1-9508-D07B9DD880BA"},name = {"用户-新增"})
    @ResponseBody
    public void insertUser(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,userService.insertUserInfo(requestJson));
    }

    //用户列表
    @ResponseBody
    @Permission(RequireLogin=true, PermissionType = PermissionType.DATA, value = {"7496770D-6772-4CC1-9508-D07B9DD880BB"},name = {"用户-查询"})
    @RequestMapping(value = "list", method=RequestMethod.GET)
    public void  userList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception{
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,userService.queryUserInfo(jsonObject));
    }

    //更新用户
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @Permission(RequireLogin=true, PermissionType = PermissionType.ACTION, value = {"7496770D-6772-4CC1-9508-D07B9DD880BC"},name = {"用户-更新"})
    @ResponseBody
    public void updateUser(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,userService.updateUserInfo(requestJson));
    }

    //删除用户
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @Permission(RequireLogin=true, PermissionType = PermissionType.ACTION, value = {"7496770D-6772-4CC1-9508-D07B9DD880BD"},name = {"用户-删除"})
    @ResponseBody
    public void deleteUser(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,userService.deleteUser(requestJson));
    }

    //查询权限
    @RequestMapping(value = "/permissions",method = RequestMethod.GET)
    @Permission(RequireLogin=true, PermissionType = PermissionType.DATA, value = {"7496770D-6772-4CC1-9508-D07B9DD880CA"},name = {"权限-查询"})
    @ResponseBody
    public void permissionList(HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,userService.selectPermissionList(jsonObject));
    }

    //添加权限
    @RequestMapping(value = "/permission/insert",method = RequestMethod.POST)
    @Permission(RequireLogin=true, PermissionType = PermissionType.ACTION, value = {"7496770D-6772-4CC1-9508-D07B9DD880CB"},name = {"权限-新增"})
    @ResponseBody
    public void insertPermission(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,userService.insertPermission(requestJson));
    }

    //更新权限
    @RequestMapping(value = "/permission/update",method = RequestMethod.POST)
    @Permission(RequireLogin=true, PermissionType = PermissionType.ACTION, value = {"7496770D-6772-4CC1-9508-D07B9DD880CC"},name = {"权限-更新"})
    @ResponseBody
    public void updatePermission(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,userService.updatePermission(requestJson));
    }

    //查询用户权限
    @RequestMapping(value = "/permissions/{userId}",method = RequestMethod.GET)
    @Permission(RequireLogin=true, PermissionType = PermissionType.DATA, value = {"7496770D-6772-4CC1-9508-D07B9DD880CA"},name = {"权限-查询"})
    @ResponseBody
    public void userPermission(@PathVariable(value = "userId") String userId,HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,userService.selectUserPermission(StringUtil.toJsonObject("userId",userId)));
    }

    //分配权限
    @RequestMapping(value = "/permission/allot",method = RequestMethod.POST)
    @Permission(RequireLogin=true, PermissionType = PermissionType.ACTION, value = {"7496770D-6772-4CC1-9508-D07B9DD880CD"},name = {"权限-分配"})
    @ResponseBody
    public void updateUserPermission(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,userService.updateUserPermission(requestJson));
    }

}
