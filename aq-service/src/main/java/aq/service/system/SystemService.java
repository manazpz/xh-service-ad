package aq.service.system;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by ywb on 2017-02-23.
 */
public interface SystemService extends BaseService {

    //登录
    JsonObject queryLogin(JsonObject jsonObject);

    //刷新token
    JsonObject refreshToken(JsonObject jsonObject);

    //查询用户信息
    JsonObject queryUserInfo(JsonObject jsonObject);

    //获取token
    JsonObject queryToken(JsonObject jsonObject);

    //查询用户权限
    JsonObject querySysPermissionUser(JsonObject jsonObject);

    //查询用户信息
    JsonObject querySysPermissionInfo(JsonObject jsonObject);

    //更新用户密码
    JsonObject updatePassword(JsonObject jsonObject);

    //重置用户密码
    JsonObject resetPassword(JsonObject jsonObject);

    //上传头像
    JsonObject uploadImg(HttpServletRequest request, HttpServletResponse response) throws IOException;

    //开关操作
    JsonObject uploadSwitch(JsonObject jsonObject);

    //查询开关
    JsonObject querySwitch(JsonObject jsonObject);

    //查询客服信息
    JsonObject queryCustomService(JsonObject jsonObject);

}
