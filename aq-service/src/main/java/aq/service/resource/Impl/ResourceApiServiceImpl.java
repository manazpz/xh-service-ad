package aq.service.resource.Impl;

import aq.common.annotation.DyncDataSource;
import aq.dao.config.ConfigDao;
import aq.dao.resource.ResourceDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.resource.ResourceApiService;
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
@Service("serviceResourceApi")
@DyncDataSource
public class ResourceApiServiceImpl extends BaseServiceImpl  implements ResourceApiService {

    @Resource
    private ResourceDao resourceDao;

    @Resource
    private ConfigDao configDao;

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryaResource(JsonObject jsonObject) {
        jsonObject.addProperty("service","Resource");
        Map<String,Object> res = new HashMap<>();
        //获取配置参数
        res.clear();
        res.put("keyWord",jsonObject.get("type").getAsString());
        List<Map<String, Object>> configs = configDao.selectConfig(res);
        if(configs.size() >= 0)
            jsonObject.addProperty("pageSize",configs.get(0).get("num").toString());
        else
            jsonObject.addProperty("pageSize",3);
        return query(jsonObject, (map) -> {
            return resourceDao.selectResource(map);
        });
    }

}
