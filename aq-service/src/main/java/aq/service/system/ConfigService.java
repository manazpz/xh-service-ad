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

    //查询配置表
    List<Map<String, Object>> selectTppConfig(Map<String, Object> map);

    //修改配置表
    void updateTppConfig(Map<String, Object> map);
}
