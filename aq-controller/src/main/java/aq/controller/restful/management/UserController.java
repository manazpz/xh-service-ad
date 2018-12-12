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
    @RequestMapping(value = "/userPermission/{userId}",method = RequestMethod.GET)
    @Permission(RequireLogin=true, PermissionType = PermissionType.DATA, value = {"7496770D-6772-4CC1-9508-D07B9DD880CA"},name = {"权限-查询"})
    @ResponseBody
    public void userPermission(@PathVariable(value = "userId") String userId,HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,userService.selectUserPermission(StringUtil.toJsonObject("userId",userId)));
    }

    //用户分配权限
    @RequestMapping(value = "/permission/userAllot",method = RequestMethod.POST)
    @Permission(RequireLogin=true, PermissionType = PermissionType.ACTION, value = {"7496770D-6772-4CC1-9508-D07B9DD880CD"},name = {"权限-分配"})
    @ResponseBody
    public void updateUserPermission(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,userService.updateUserPermission(requestJson));
    }

    //查询回收方式列表
    @RequestMapping(value = "/recoveryList",method = RequestMethod.GET)
    @ResponseBody
    public void recoveryList(HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,userService.selectRecoveryList(jsonObject));
    }


    //新增回收方式
    @RequestMapping(value = "/createRecovery",method = RequestMethod.POST)
    @ResponseBody
    public void insertRecovery(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,userService.insertRecovery(requestJson));
    }

    //修改回收方式
    @RequestMapping(value = "/updateRecovery",method = RequestMethod.POST)
    @ResponseBody
    public void updateRecovery(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,userService.updateRecovery(requestJson));
    }

    //查询客服信息
    @RequestMapping(value = "/customerList",method = RequestMethod.GET)
    @ResponseBody
    public void customerList(HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,userService.queryCustomService(jsonObject));
    }

    //查询所有客服信息
    @RequestMapping(value = "/customerAllList",method = RequestMethod.GET)
    @ResponseBody
    public void customerAllList(HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,userService.queryAllCustomService(jsonObject));
    }

    //新增回收方式
    @RequestMapping(value = "/updateCustomer",method = RequestMethod.POST)
    @ResponseBody
    public void updateCustomer(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,userService.updateCustomService(requestJson));
    }


    //查询客服信息
    @RequestMapping(value = "/querySuggestion",method = RequestMethod.GET)
    @ResponseBody
    public void querySuggestion(HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,userService.querySuggestion(jsonObject));
    }

    //新增角色
    @RequestMapping(value = "/insertRole",method = RequestMethod.POST)
    @ResponseBody
    public void insertRole(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,userService.insertRole(requestJson));
    }

    //更新角色
    @RequestMapping(value = "/updateRole",method = RequestMethod.POST)
    @ResponseBody
    public void updateRole(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,userService.updateRole(requestJson));
    }

    //角色列表
    @RequestMapping(value = "/roles",method = RequestMethod.GET)
    @ResponseBody
    public void roleList(HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,userService.queryRoleList(jsonObject));
    }

    //用户角色列表
    @RequestMapping(value = "/userRoles",method = RequestMethod.GET)
    @ResponseBody
    public void userRoles(HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,userService.queryUserRoleList(jsonObject));
    }

    //查询角色权限
    @RequestMapping(value = "/rolePermission/{roleId}",method = RequestMethod.GET)
    @ResponseBody
    public void rolePermission(@PathVariable(value = "roleId") String roleId,HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,userService.selectRolePermission(StringUtil.toJsonObject("roleId",roleId)));
    }

    //角色分配权限
    @RequestMapping(value = "/permission/roleAllot",method = RequestMethod.POST)
    @ResponseBody
    public void updateRolePermission(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,userService.updateRolePermission(requestJson));
    }

    //用户分配角色
    @RequestMapping(value = "/role/allot",method = RequestMethod.POST)
    @ResponseBody
    public void updateUserRole(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,userService.updateUserRole(requestJson));
    }

}
