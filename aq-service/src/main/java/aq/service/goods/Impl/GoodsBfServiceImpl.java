package aq.service.goods.Impl;

import aq.common.access.AbsAccessUser;
import aq.common.access.Factory;
import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.*;
import aq.dao.goods.ClassifyDao;
import aq.dao.goods.GoodsDao;
import aq.dao.goods.SpecDao;
import aq.dao.resource.ResourceDao;
import aq.dao.stock.StockDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.goods.GoodsBfService;
import aq.service.system.Func;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by ywb on 2017-02-23.
 */
@Service("serviceGoodsBf")
@DyncDataSource
public class GoodsBfServiceImpl extends BaseServiceImpl  implements GoodsBfService {

    @Resource
    private GoodsDao goodsDao;

    @Resource
    private StockDao stockDao;

    @Resource
    private SpecDao specDao;

    @Resource
    private ClassifyDao classifyDao;

    @Resource
    private ResourceDao resourceDao;

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryGoods(JsonObject jsonObject) {
        jsonObject.addProperty("service","goods");
        return query(jsonObject,(map)->{
            Map res = new HashMap();
            List<Map<String, Object>> goods = goodsDao.selectGoods(map);
            goods.forEach(obj->{
                if(!StringUtil.isEmpty(obj.get("specParameter"))) {
                    List specParameter = GsonHelper.getInstance().fromJson(obj.get("specParameter").toString(), List.class);
                    obj.put("specParameter",specParameter);
                }
                if(!StringUtil.isEmpty(obj.get("id"))) {
                    res.clear();
                    res.put("type","GI");
                    res.put("refId",obj.get("id"));
                    List imgs = resourceDao.selectResource(res);
                    obj.put("imgs",imgs);
                }
            });
            return goods;
        });
    }

    @Override
    public JsonObject updateGoods(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("Goods");
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
        }else {
            Map<String,Object> res = new HashMap<>();
            res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
            res.put("lastCreateUserId", user.getUserId());
            res.put("lastCreateTime",new Date());
            res.put("specParameter",StringUtil.isEmpty(jsonObject.get("parameter"))?"":jsonObject.get("parameter").getAsJsonArray().toString());
            goodsDao.updateGoods(res);
            rtn.setCode(200);
            rtn.setMessage("success");
        }
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject deleteGoods(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Goods");
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res.put("id",jsonObject.get("id").getAsString());
        goodsDao.deleteGoods(res);
        res.clear();
        res.put("basicId",jsonObject.get("id").getAsString());
        stockDao.deleteStock(res);
        res.clear();
        res.put("stockMainId",jsonObject.get("id").getAsString());
        stockDao.deleteStockOut(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject updateStock(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("Goods");
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
        }else {
            Map<String,Object> res = new HashMap<>();
            res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
            res.put("lastCreateUserId", user.getUserId());
            res.put("lastCreateTime",new Date());
            stockDao.updateStock(res);
            rtn.setCode(200);
            rtn.setMessage("success");
        }
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject selectClassifyCascade(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Goods");
        Map<String,Object> res = new HashMap<>();
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        res.put("cascade", 'Y');
        List<Map<String, Object>> req1 = classifyDao.selectClassify(res);
        for(Map obj1:req1) {
            res.clear();
            res.put("parentId",obj1.get("id"));
            List<Map<String, Object>> req2 = classifyDao.selectClassify(res);
            for(Map obj2:req2) {
                res.clear();
                res.put("parentId",obj2.get("id"));
                List<Map<String, Object>> req3 = classifyDao.selectClassify(res);
                for(Map obj3:req3) {
                    if(!StringUtil.isEmpty(obj3.get("parameter"))) {
                        HashMap hashMap = new HashMap();
                        Map parameterMap = GsonHelper.getInstance().fromJson(obj3.get("parameter").toString(), Map.class);
                        if(!StringUtil.isEmpty(parameterMap.get("brand"))) {
                            List brands = GsonHelper.getInstance().fromJson(parameterMap.get("brand").toString(), List.class);
                            hashMap.put("brand",brands);
                        }
                        if(!StringUtil.isEmpty(parameterMap.get("spec"))) {
                            List specs = GsonHelper.getInstance().fromJson(parameterMap.get("spec").toString(), List.class);
                            hashMap.put("spec",specs);
                        }
                        obj3.put("parameter",hashMap);
                    }
                }
                obj2.put("children",req3);
            }
            obj1.put("children",req2);
        }
        rtn.setCode(200);
        rtn.setMessage("success");
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(req1)).getAsJsonArray();
        data.add("items",jsonArray);
        rtn.setData(data);
        return  Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject selectClassifySpec(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Goods");
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res.put("id",jsonObject.get("id").getAsString());
        List<Map<String, Object>> list = classifyDao.selectClassify(res);
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        if(list.size()>0) {
            String paraemeterStr = (String) ((Map) list.get(0)).get("parameter");
            if(!StringUtil.isEmpty(paraemeterStr)) {
                Map map = GsonHelper.getInstance().fromJson(paraemeterStr, Map.class);
                if(!StringUtil.isEmpty(map.get("spec"))) {
                    List specs = GsonHelper.getInstance().fromJson(map.get("spec").toString(), List.class);
                    jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(specs)).getAsJsonArray();
                }
            }
        }
        rtn.setCode(200);
        rtn.setMessage("success");
        data.add("items",jsonArray);
        rtn.setData(data);
        return  Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject selectClassifySpecParam(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Goods");
        Map<String,Object> res = new HashMap<>();
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        res.clear();
        res.put("id",jsonObject.get("id").getAsString());
        List<Map<String, Object>> list = specDao.selectSpec(res);
        if(list.size()>0) {
            String parameterStr = (String) ((Map) list.get(0)).get("parameter");
            if(!StringUtil.isEmpty(parameterStr)) {
                List specParameter = GsonHelper.getInstance().fromJson(parameterStr, List.class);
                jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(specParameter)).getAsJsonArray();
            }
        }
        rtn.setCode(200);
        rtn.setMessage("success");
        data.add("items",jsonArray);
        rtn.setData(data);
        return  Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject selectBrand(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Goods");
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        HashMap res = new HashMap();
        List req = goodsDao.selectBrand(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(req)).getAsJsonArray();
        data.add("items",jsonArray);
        rtn.setData(data);
        return  Func.functionRtnToJsonObject.apply(rtn);
    }
}
