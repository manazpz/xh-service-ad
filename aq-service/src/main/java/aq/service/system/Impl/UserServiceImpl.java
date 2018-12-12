package aq.service.system.Impl;

import aq.common.access.AbsAccessUser;
import aq.common.access.Factory;
import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.*;
import aq.dao.user.CustomerDao;
import aq.dao.user.UserDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.system.Func;
import aq.service.system.UserService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by ywb on 2017-02-23.
 */
@Service("serviceUser")
@DyncDataSource
public class UserServiceImpl extends BaseServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Resource
    private CustomerDao customerDao;

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject insertUserInfo(JsonObject jsonObject) {
        Rtn rtn = new Rtn("User");
        Map<String,Object> res = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        res.put("id", UUIDUtil.getUUID());
        res.put("createTime",new Date());
        res.put("updateTime",new Date());
        res.put("password", MD5.getMD5String(res.get("password").toString()));
        userDao.insertUserInfo(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryUserInfo(JsonObject jsonObject) {
        jsonObject.addProperty("service","User");
        return query(jsonObject,(map)->{
            return userDao.selectUserInfo(map);
        });
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject updateUserInfo(JsonObject jsonObject) {
        Rtn rtn = new Rtn("User");
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        res.put("updateTime",new Date());
        userDao.updateUserInfo(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject deleteUser(JsonObject jsonObject) {
        Rtn rtn = new Rtn("User");
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        userDao.deleteUser(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject selectPermissionList(JsonObject jsonObject) {
        jsonObject.addProperty("service","User");
        return query(jsonObject,(map)->{
            return userDao.selectPermissionList(map);
        });
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject updatePermission(JsonObject jsonObject) {
        Rtn rtn = new Rtn("User");
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        userDao.updatePermission(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject insertPermission(JsonObject jsonObject) {
        Rtn rtn = new Rtn("User");
        Map<String,Object> res = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        res.put("id", UUIDUtil.getUUID());
        res.put("createTime",new Date());
        userDao.insertPermission(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject selectUserPermission(JsonObject jsonObject) {
        Rtn rtn = new Rtn("User");
        String key = "";
        String name = "";
        JsonObject data = new JsonObject();
        JsonArray jsonArray1 = new JsonArray();
        JsonArray jsonArray2 = new JsonArray();
        Map<String,Object> res = new HashMap<>();
        List list = new ArrayList();
        List rep = new ArrayList();
        res.clear();
        List<Map<String, Object>> allPermissions = userDao.selectPermissionList(res);
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        List<Map<String, Object>> userPermissions = userDao.selectUserPermission(res);

        for (Map<String, Object> obj2 : allPermissions) {
            if(StringUtil.isEmpty(key)){
                list.clear();
                res.clear();
                key = obj2.get("moduleKey").toString();
                name = obj2.get("moduleName").toString();
            }

            if(!key.equals(obj2.get("moduleKey"))){
                res.put("desc",list);
                res.put("name",name);
                res.put("key",key);
                list = new ArrayList();
                key = obj2.get("moduleKey").toString();
                name = obj2.get("moduleName").toString();
                rep.add(res);
                res = new HashMap<>();
            }
            list.add(obj2);
        }

        if(list.size()>0) {
            res.put("desc",list);
            res.put("name",name);
            res.put("key",key);
            rep.add(res);
            list = new ArrayList();
        }

        for (Map<String, Object> obj : userPermissions) {
            list.add(obj.get("PERMISSION_ID"));
        }
        jsonArray1 =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(rep)).getAsJsonArray();
        jsonArray2 =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(list)).getAsJsonArray();
        rtn.setCode(200);
        rtn.setMessage("success");
        data.add("items",jsonArray1);
        data.add("checkedCities",jsonArray2);
        rtn.setData(data);
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject updateUserPermission(JsonObject jsonObject) {
        Rtn rtn = new Rtn("User");
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> data = new HashMap<>();
        List<String> add = new ArrayList();
        List<Map<String, Object>> delete = new ArrayList();
        data = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        if(StringUtil.isEmpty(data.get("userId"))){
            rtn.setCode(60003);
            rtn.setMessage("用户id不存在!");
        }else {
            String userId = data.get("userId").toString();
            List<String> permissions = (List) data.get("permissions");
            add.addAll(permissions);
            if(permissions.size()<=0) {
                res.clear();
                res.put("userId",userId);
                userDao.deleteUserPermission(res);
            }else {
                res.clear();
                res.put("userId",userId);
                List<Map<String, Object>> userPermissions = userDao.selectUserPermission(res);
                delete.addAll(userPermissions);
                for (String obj1 : permissions) {
                    for (Map obj2 : userPermissions) {
                        if(obj1.equals(obj2.get("PERMISSION_ID"))) {
                            add.remove(obj1);
                            delete.remove(obj2);
                        }
                    }
                }

                for (String obj: add) {
                    Map map = new HashMap();
                    map.put("userId",userId);
                    map.put("permissionId",obj);
                    userDao.insertUserPermission(map);
                }

                for (Map obj: delete) {
                    Map map = new HashMap();
                    map.put("userId",userId);
                    map.put("permissionId",obj.get("PERMISSION_ID"));
                    userDao.deleteUserPermission(map);
                }
            }
        }
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject selectRecoveryList(JsonObject jsonObject) {
        jsonObject.addProperty("service","User");
        return query(jsonObject,(map)->{
            return userDao.selectRecoveryList(map);
        });
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject insertRecovery(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("User");
        Map<String,Object> res = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        res.put("id", UUIDUtil.getUUID());
        res.put("createUserId", user.getUserId());
        res.put("createTime",new Date());
        res.put("lastCreateUserId", user.getUserId());
        res.put("lastCreateTime",new Date());
        userDao.insertRecovery(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject updateRecovery(JsonObject jsonObject) {
        Rtn rtn = new Rtn("User");
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        userDao.updateRecovery(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public void insertUserInfos(Map<String, Object> map) {
        Map<String,Object> res = new HashMap<>();
        res.put("id", UUIDUtil.getUUID());
        res.put("createTime",new Date());
        res.put("updateTime",new Date());
        res.put("nickName",map.get("nickname"));
        res.put("openid",map.get("openid"));
        res.put("headPortrait",map.get("head_portrait"));
        res.put("sex",map.get("sex"));
        res.put("country",map.get("country"));
        res.put("province",map.get("province"));
        res.put("city",map.get("city"));
        userDao.insertUsers(res);
    }

    @Override
    public void updateUserInfos(Map<String, Object> map) {
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res.put("nickName",map.get("nickname"));
        res.put("openid",map.get("openid"));
        res.put("headPortrait",map.get("head_portrait"));
        res.put("sex",map.get("sex"));
        res.put("country",map.get("country"));
        res.put("province",map.get("province"));
        res.put("city",map.get("city"));
        userDao.updateUserInfos(res);
        System.out.print("=================执行更新用户信息================");
    }

    @Override
    public List<Map<String, Object>> queryUserInfos(Map<String, Object> map) {
        return  userDao.selectUserInfos(map);
    }

    @Override
    public JsonObject queryCustomService(JsonObject jsonObject) {
        jsonObject.addProperty("service","user");
        return query(jsonObject,(map)->{
            return customerDao.selectCustomerInfo(map);
        });
    }

    @Override
    public JsonObject queryAllCustomService(JsonObject jsonObject) {
        Rtn rtn = new Rtn("user");
        Map<String,Object> res = new HashMap<>();
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        List<Map<String, Object>> lists = customerDao.selectCustomerInfo(res);
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(lists)).getAsJsonArray();
        rtn.setCode(200);
        rtn.setMessage("success");
        data.add("items",jsonArray);
        rtn.setData(data);
        return  Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject updateCustomService(JsonObject jsonObject) {
        Rtn rtn = new Rtn("user");
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        customerDao.updateCustomer(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject querySuggestion(JsonObject jsonObject) {
        jsonObject.addProperty("service","user");
        return query(jsonObject,(map)->{
            return userDao.selectSuggestion(map);
        });
    }

    @Override
    public JsonObject insertRole(JsonObject jsonObject) {
        Rtn rtn = new Rtn("user");
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        res.put("id",UUIDUtil.getUUID());
        res.put("createTime",new Date());
        userDao.insertRole(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject updateRole(JsonObject jsonObject) {
        Rtn rtn = new Rtn("user");
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        userDao.updateRole(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject queryRoleList(JsonObject jsonObject) {
        jsonObject.addProperty("service","user");
        return query(jsonObject,(map)->{
            return userDao.selectRoleList(map);
        });
    }

    @Override
    public JsonObject queryUserRoleList(JsonObject jsonObject) {
        Rtn rtn = new Rtn("User");
        Map<String,Object> res = new HashMap<>();
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        List lists = userDao.selectUserRoleList(res);
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(lists)).getAsJsonArray();
        rtn.setCode(200);
        rtn.setMessage("success");
        data.add("items",jsonArray);
        rtn.setData(data);
        return  Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject selectRolePermission(JsonObject jsonObject) {
        Rtn rtn = new Rtn("User");
        String key = "";
        String name = "";
        JsonObject data = new JsonObject();
        JsonArray jsonArray1 = new JsonArray();
        JsonArray jsonArray2 = new JsonArray();
        Map<String,Object> res = new HashMap<>();
        List list = new ArrayList();
        List rep = new ArrayList();
        res.clear();
        List<Map<String, Object>> allPermissions = userDao.selectPermissionList(res);
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        List<Map<String, Object>> rolePermissions = userDao.selectRolePermission(res);

        for (Map<String, Object> obj2 : allPermissions) {
            if(StringUtil.isEmpty(key)){
                list.clear();
                res.clear();
                key = obj2.get("moduleKey").toString();
                name = obj2.get("moduleName").toString();
            }

            if(!key.equals(obj2.get("moduleKey"))){
                res.put("desc",list);
                res.put("name",name);
                res.put("key",key);
                list = new ArrayList();
                key = obj2.get("moduleKey").toString();
                name = obj2.get("moduleName").toString();
                rep.add(res);
                res = new HashMap<>();
            }
            list.add(obj2);
        }

        if(list.size()>0) {
            res.put("desc",list);
            res.put("name",name);
            res.put("key",key);
            rep.add(res);
            list = new ArrayList();
        }

        for (Map<String, Object> obj : rolePermissions) {
            list.add(obj.get("PERMISSION_ID"));
        }
        jsonArray1 =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(rep)).getAsJsonArray();
        jsonArray2 =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(list)).getAsJsonArray();
        rtn.setCode(200);
        rtn.setMessage("success");
        data.add("items",jsonArray1);
        data.add("checkedCities",jsonArray2);
        rtn.setData(data);
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject updateRolePermission(JsonObject jsonObject) {
        Rtn rtn = new Rtn("User");
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> data = new HashMap<>();
        List<String> add = new ArrayList();
        List<Map<String, Object>> delete = new ArrayList();
        data = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        if(StringUtil.isEmpty(data.get("roleId"))){
            rtn.setCode(60003);
            rtn.setMessage("角色id不存在!");
        }else {
            String roleId = data.get("roleId").toString();
            List<String> permissions = (List) data.get("permissions");
            add.addAll(permissions);
            if(permissions.size()<=0) {
                res.clear();
                res.put("roleId",roleId);
                userDao.deleteRolePermission(res);
            }else {
                res.clear();
                res.put("roleId",roleId);
                List<Map<String, Object>> rolePermissions = userDao.selectRolePermission(res);
                delete.addAll(rolePermissions);
                for (String obj1 : permissions) {
                    for (Map obj2 : rolePermissions) {
                        if(obj1.equals(obj2.get("PERMISSION_ID"))) {
                            add.remove(obj1);
                            delete.remove(obj2);
                        }
                    }
                }

                for (String obj: add) {
                    Map map = new HashMap();
                    map.put("roleId",roleId);
                    map.put("permissionId",obj);
                    userDao.insertRolePermission(map);
                }

                for (Map obj: delete) {
                    Map map = new HashMap();
                    map.put("roleId",roleId);
                    map.put("permissionId",obj.get("PERMISSION_ID"));
                    userDao.deleteRolePermission(map);
                }
            }
        }
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject updateUserRole(JsonObject jsonObject) {
        Rtn rtn = new Rtn("User");
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> data = new HashMap<>();
        List<String> add = new ArrayList();
        List<Map<String, Object>> delete = new ArrayList();
        data = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        if(StringUtil.isEmpty(data.get("id"))){
            rtn.setCode(60003);
            rtn.setMessage("用户id不存在!");
        }else {
            String userId = data.get("id").toString();
            List<String> roles = (List) data.get("roles");
            add.addAll(roles);
            if(roles.size()<=0) {
                res.clear();
                res.put("userId",userId);
                userDao.deleteUserRole(res);
            }else {
                res.clear();
                res.put("userId",userId);
                List<Map<String, Object>> userRoles = userDao.selectUserRole(res);
                delete.addAll(userRoles);
                for (String obj1 : roles) {
                    for (Map obj2 : userRoles) {
                        if(obj1.equals(obj2.get("ROLE_ID"))) {
                            add.remove(obj1);
                            delete.remove(obj2);
                        }
                    }
                }

                for (String obj: add) {
                    Map map = new HashMap();
                    map.put("roleId",obj);
                    map.put("userId",userId);
                    userDao.insertUserRole(map);
                }

                for (Map obj: delete) {
                    Map map = new HashMap();
                    map.put("userId",userId);
                    map.put("roleId",obj.get("ROLE_ID"));
                    userDao.deleteUserRole(map);
                }
            }
        }
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }
}
