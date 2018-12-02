package aq.service.system;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

/**
 * Created by ywb on 2017-02-23.
 */
public interface ConfigService extends BaseService {

    //查询配置表
    JsonObject queryaConfig(JsonObject jsonObject);

    //新增配置表
    JsonObject insertConfig(JsonObject jsonObject);

    //删除配置表
    JsonObject deleteConfig(JsonObject jsonObject);

    //修改配置表
    JsonObject updateConfig(JsonObject jsonObject);

    //新增第三方配置表
    JsonObject insertTppConfig(JsonObject jsonObject);

    //查询第三方配置表
    List<Map<String, Object>> selectTppConfig(Map<String, Object> map);

    //微信统一下单
    JsonObject pay(JsonObject jsonObject);

    //修改第三方平台配置表
    JsonObject updateTppConfig(JsonObject jsonObject);

    //删除第三方平台配置表
    JsonObject deleteTppConfig(JsonObject jsonObject);

    //新增首页标签
    JsonObject insertHomeLabel(JsonObject jsonObject);

    //查询首页标签
    JsonObject selectHomeLabel(JsonObject jsonObject);

    //修改首页标签
    JsonObject updateHomeLabel(JsonObject jsonObject);

    //删除首页标签
    JsonObject deleteHomeLabel(JsonObject jsonObject);

}
