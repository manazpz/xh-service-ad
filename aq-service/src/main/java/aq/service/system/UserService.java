package aq.service.system;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

/**
 * Created by ywb on 2017-02-23.
 */
public interface UserService extends BaseService {

    //新增用户
    JsonObject insertUserInfo(JsonObject jsonObject);

    //获取用户信息
    JsonObject queryUserInfo(JsonObject jsonObject);

    //更新用户信息
    JsonObject updateUserInfo(JsonObject jsonObject);

    //删除/恢复用户
    JsonObject deleteUser(JsonObject jsonObject);

    //权限列表
    JsonObject selectPermissionList(JsonObject jsonObject);

    //更新权限
    JsonObject updatePermission(JsonObject jsonObject);

    //新增权限
    JsonObject insertPermission(JsonObject jsonObject);

    //用户权限列表
    JsonObject selectUserPermission(JsonObject jsonObject);

    //用户权限分配
    JsonObject updateUserPermission(JsonObject jsonObject);

    //回收方式
    JsonObject selectRecoveryList(JsonObject jsonObject);

    //回收方式
    JsonObject insertRecovery(JsonObject jsonObject);

    //回收方式
    JsonObject updateRecovery(JsonObject jsonObject);

    //新增用户信息
    void insertUserInfos(Map<String, Object> map);

    //查询用户信息
    List<Map<String, Object>> queryUserInfos(Map<String, Object> map);

    //查询客服信息
    JsonObject queryCustomService(JsonObject jsonObject);

    //更新客服
    JsonObject updateCustomService(JsonObject jsonObject);
}
