package aq.service.search.Impl;

import aq.common.annotation.DyncDataSource;
import aq.dao.config.ConfigDao;
import aq.dao.goods.SearchDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.search.SearchService;
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
@Service("serviceSearch")
@DyncDataSource
public class SearchServiceImpl extends BaseServiceImpl  implements SearchService {

    @Resource
    private SearchDao searchDao;

    @Resource
    private ConfigDao configDao;

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject querySearchSpec(JsonObject jsonObject) {
        jsonObject.addProperty("service","Search");
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res.put("keyWord",jsonObject.get("type").getAsString());
        List<Map<String, Object>> configs = configDao.selectConfig(res);
        if(configs.size() >= 0)
            jsonObject.addProperty("pageSize",configs.get(0).get("num").toString());
        else
            jsonObject.addProperty("pageSize",3);

        return query(jsonObject, (map) -> {
            return searchDao.selectSearchSpec(map);
        });
    }
}
