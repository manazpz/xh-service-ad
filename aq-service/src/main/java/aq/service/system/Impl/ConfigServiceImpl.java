package aq.service.system.Impl;

import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.GsonHelper;
import aq.dao.config.ConfigDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.system.ConfigService;
import aq.service.system.Func;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ywb on 2017-02-23.
 */
@Service("serviceConfig")
@DyncDataSource
public class ConfigServiceImpl extends BaseServiceImpl  implements ConfigService {

    @Resource
    private ConfigDao configDao;

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryaConfig(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Config");
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        Map<String,Object> map = new HashMap<>();
        map = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        List<Map<String, Object>> results = configDao.selectConfig(map);
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(results)).getAsJsonArray();
        rtn.setCode(200);
        rtn.setMessage("success");
        data.add("items",jsonArray);
        rtn.setData(data);
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject insertConfig(JsonObject jsonObject) {
        return null;
    }

    @Override
    public JsonObject deleteConfig(JsonObject jsonObject) {
        return null;
    }

    @Override
    public JsonObject updateConfig(JsonObject jsonObject) {
        return null;
    }
}
