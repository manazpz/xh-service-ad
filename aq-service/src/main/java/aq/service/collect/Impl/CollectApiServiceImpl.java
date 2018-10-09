package aq.service.collect.Impl;

import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.GsonHelper;
import aq.common.util.StringUtil;
import aq.common.util.UUIDUtil;
import aq.dao.collect.CollectDao;
import aq.dao.goods.GoodsDao;
import aq.dao.resource.ResourceDao;
import aq.dao.user.CustomerDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.collect.CollectApiService;
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
@Service("serviceCollectApi")
@DyncDataSource
public class CollectApiServiceImpl extends BaseServiceImpl  implements CollectApiService {

    @Resource
    private CustomerDao customerDao;

    @Resource
    private CollectDao collectDao;

    @Resource
    private GoodsDao goodsDao;

    @Resource
    private ResourceDao resourceDao;

    @Override
    public JsonObject insertCollect(JsonObject jsonObject) {
        Rtn rtn = new Rtn("collect");
        HashMap req = new HashMap();
        List userInfo = null;
        if(!StringUtil.isEmpty(jsonObject.get("openId"))) {
            req.clear();
            req.put("openId",jsonObject.get("openId").getAsString());
            userInfo = customerDao.selectCustomerInfo(req);
        }
        if(userInfo == null || userInfo.size() < 1){
            rtn.setCode(404);
            rtn.setMessage("用户不存在！");
        }
        req.clear();
        req.put("userId",((Map)userInfo.get(0)).get("id"));
        req.put("basicId",jsonObject.get("basicId").getAsString());
        List collect = collectDao.selectCollect(req);
        if(collect.size()>0) {
            rtn.setCode(404);
            rtn.setMessage("已收藏！");
        }else {
            req.put("id", UUIDUtil.getUUID());
            collectDao.insertCollect(req);
            rtn.setCode(200);
            rtn.setMessage("收藏成功！");
        }
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject deleteCollect(JsonObject jsonObject) {
        Rtn rtn = new Rtn("collect");
        if(!StringUtil.isEmpty(jsonObject.get("ids"))) {
            JsonArray ids = jsonObject.get("ids").getAsJsonArray();
            ids.forEach(obj-> {
                HashMap map = new HashMap();
                map.put("id",obj.getAsString());
                collectDao.deleteCollect(map);
            });
        }
        rtn.setCode(200);
        rtn.setMessage("操作成功！");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject queryCollect(JsonObject jsonObject) {
        Rtn rtn = new Rtn("collect");
        HashMap req = new HashMap();
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        List userInfo = null;
        if(!StringUtil.isEmpty(jsonObject.get("openId"))) {
            req.clear();
            req.put("openId",jsonObject.get("openId").getAsString());
            userInfo = customerDao.selectCustomerInfo(req);
        }
        if(userInfo == null || userInfo.size() < 1){
            rtn.setCode(404);
            rtn.setMessage("用户不存在！");
        }
        req.clear();
        req.put("userId",((Map)userInfo.get(0)).get("id"));
        List<Map<String, Object>> collects = collectDao.selectCollect(req);
        if(collects.size() > 0)  {
            req.clear();
            req.put("ids",collects);
            List<Map<String, Object>> goods = goodsDao.selectGoods(req);
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
            jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(goods)).getAsJsonArray();
            data.addProperty("total",goods.size());
            data.add("items",jsonArray);
        }
        rtn.setCode(200);
        rtn.setMessage("success");
        rtn.setData(data);
        return  Func.functionRtnToJsonObject.apply(rtn);
    }
}
