package aq.service.collect.Impl;

import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.GsonHelper;
import aq.common.util.StringUtil;
import aq.dao.collect.CollectDao;
import aq.dao.goods.GoodsDao;
import aq.dao.resource.ResourceDao;
import aq.dao.user.CustomerDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.collect.CollectService;
import aq.service.system.Func;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ywb on 2017-02-23.
 */
@Service("serviceCollect")
@DyncDataSource
public class CollectServiceImpl extends BaseServiceImpl  implements CollectService {

    @Resource
    private CollectDao collectDao;

    @Resource
    private GoodsDao goodsDao;

    @Resource
    private ResourceDao resourceDao;

    @Override
    public JsonObject queryCollect(JsonObject jsonObject) {
        Rtn rtn = new Rtn("collect");
        jsonObject.addProperty("service","collect");
        HashMap req = new HashMap();
        req.clear();
        req.put("userId",jsonObject.get("id").getAsString());
        List<Map<String, Object>> collects = collectDao.selectCollect(req);
        if(collects.size() > 0)  {
            return query(jsonObject,(map)->{
                map.clear();
                map.put("ids",collects);
                List<Map<String, Object>> goods = goodsDao.selectGoods(map);
                goods.forEach(obj->{
                    if(!StringUtil.isEmpty(obj.get("specParameter"))) {
                        String str = "";
                        List<Map> specParameter = GsonHelper.getInstance().fromJson(obj.get("specParameter").toString(), List.class);
                        obj.put("specParameter",specParameter);
                        if(specParameter.size()>0) {
                            List<Map> spec = (List<Map>) specParameter.get(0).get("spec");
                            for (Map m1 : spec) {
                                str += m1.get("spec_value_name").toString() + " ";
                            }
                            obj.put("bllPrice",specParameter.get(0).get("price"));
                        }
                        obj.put("bllParameterStr",str);
                    }
                    collects.forEach(obj1 -> {
                        if(obj1.get("basicId").equals(obj.get("id"))) {
                            obj.put("collectId",obj1.get("id"));
                        }
                    });
                    if(!StringUtil.isEmpty(obj.get("id"))) {
                        Map<String,Object> ress = new HashMap<>();
                        ress.put("type","GI");
                        ress.put("refId",obj.get("id"));
                        List imgs = resourceDao.selectResource(ress);
                        obj.put("imgs",imgs);
                    }
                });
                return goods;
            });
        }else {
            JsonObject data = new JsonObject();
            rtn.setCode(200);
            rtn.setMessage("success");
            rtn.setData(data);
            return  Func.functionRtnToJsonObject.apply(rtn);
        }
    }
}
