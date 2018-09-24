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
import aq.dao.shop.ShopDao;
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
    private ShopDao shopDao;

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
    public JsonObject insertOldGoods(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("Goods");
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
        }else {
            Map<String,Object> res = new HashMap<>();
            res.clear();
            res.put("userId",user.getUserId());
            List<Map<String, Object>> maps = shopDao.selectShop(res);
            if(maps.size() > 0) {
                res.clear();
                String uuid = StringUtil.isEmpty(jsonObject.get("id").getAsString())?UUIDUtil.getUUID():jsonObject.get("id").getAsString();
                res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
                res.put("id",uuid);
                res.put("createUserId", user.getUserId());
                res.put("createTime",new Date());
                res.put("lastCreateUserId", user.getUserId());
                res.put("lastCreateTime",new Date());
                //格式化规格参数
                if(!StringUtil.isEmpty(jsonObject.get("specParameter"))) {
                    List<Map> datas = new ArrayList();
                    List<Map> specParameters = GsonHelper.getInstance().fromJson(jsonObject.get("specParameter").getAsString(), List.class);
                    for (Map obj : specParameters) {
                        if(datas.size() > 0) {
                            List parameters = null;
                            for (Map obj1 : datas) {
                                if(obj1.get("id").equals(obj.get("id"))) {
                                    parameters = (List) obj1.get("parameter");
                                    break;
                                }
                            }
                            if(parameters != null) {
                                parameters.add(setParameter(obj));
                            }else {
                                Map data = new HashMap<>();
                                parameters = new ArrayList();
                                data.put("id",obj.get("id"));
                                data.put("name",obj.get("name"));
                                data.put("px",obj.get("px"));
                                data.put("obligate",obj.get("obligate"));
                                parameters.add(setParameter(obj));
                                data.put("parameter",parameters);
                                datas.add(data);
                            }
                        }else {
                            Map data = new HashMap<>();
                            List parameters = new ArrayList();
                            data.put("id",obj.get("id"));
                            data.put("name",obj.get("name"));
                            data.put("px",obj.get("px"));
                            data.put("obligate",obj.get("obligate"));
                            parameters.add(setParameter(obj));
                            data.put("parameter",parameters);
                            datas.add(data);
                        }
                    }
                    JsonArray jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(datas)).getAsJsonArray();
                    res.put("specParameter",jsonArray.toString());
                }
                res.put("shopId",((Map)maps.get(0)).get("id"));
                if(StringUtil.isEmpty(jsonObject.get("id").getAsString())) {
                    goodsDao.insertGoods(res);
                }else {
                    goodsDao.updateGoods(res);
                    if (res.get("delFiles") instanceof List) {
                        HashMap resourceMap = new HashMap();
                        List<Map> delResources = (List<Map>) res.get("delFiles");
                        for (Map obj : delResources) {
                            resourceMap.clear();
                            resourceMap.put("id",obj.get("id"));
                            resourceDao.deleteResource(resourceMap);
                        }
                    }
                }
                if (res.get("files") instanceof List) {
                    HashMap resourceMap = new HashMap();
                    List<Map> resources = (List<Map>) res.get("files");
                    for (Map obj : resources) {
                        resourceMap.clear();
                        resourceMap.put("id",obj.get("id"));
                        resourceMap.put("name", obj.get("name"));
                        resourceMap.put("url",obj.get("url"));
                        resourceMap.put("extend",obj.get("extend"));
                        resourceMap.put("size",obj.get("size"));
                        resourceMap.put("type","GI");
                        resourceMap.put("refId",uuid);
                        resourceMap.put("createUserId", user.getUserId());
                        resourceMap.put("lastCreateUserId", user.getUserId());
                        resourceMap.put("createTime", new Date());
                        resourceMap.put("lastCreateTime", new Date());
                        resourceDao.insertResourcet(resourceMap);
                    }
                }
                rtn.setCode(200);
                rtn.setMessage("success");
            }else {
                rtn.setCode(404);
                rtn.setMessage("用户下无店铺！");
            }
        }
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject insertNewGoods(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("Goods");
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
        }else {
            Map<String,Object> res = new HashMap<>();
            res.clear();
            res.put("userId",user.getUserId());
            List<Map<String, Object>> maps = shopDao.selectShop(res);
            if(maps.size() > 0) {
                res.clear();
                String uuid = StringUtil.isEmpty(jsonObject.get("id").getAsString())?UUIDUtil.getUUID():jsonObject.get("id").getAsString();
                res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
                res.put("id",uuid);
                res.put("createUserId", user.getUserId());
                res.put("createTime",new Date());
                res.put("lastCreateUserId", user.getUserId());
                res.put("lastCreateTime",new Date());
                res.put("specParameter", jsonObject.get("goodsSpec").getAsString());
                res.put("shopId",((Map)maps.get(0)).get("id"));
                res.put("banPrice",0);
                if(StringUtil.isEmpty(jsonObject.get("id").getAsString())) {
                    goodsDao.insertGoods(res);
                    HashMap stockMap = new HashMap();
                    stockMap.put("id",UUIDUtil.getUUID());
                    stockMap.put("createUserId", user.getUserId());
                    stockMap.put("createTime",new Date());
                    stockMap.put("lastCreateUserId", user.getUserId());
                    stockMap.put("lastCreateTime",new Date());
                    stockMap.put("shopId",((Map)maps.get(0)).get("id"));
                    stockMap.put("basicId",uuid);
                    stockMap.put("type","01");
                    stockMap.put("currentStock",StringUtil.isEmpty(res.get("stock"))?0:res.get("stock"));
                    stockMap.put("useableStock",StringUtil.isEmpty(res.get("stock"))?0:res.get("stock"));
                    stockDao.insertStock(stockMap);
                }else {
                    goodsDao.updateGoods(res);
                    if (res.get("delFiles") instanceof List) {
                        HashMap resourceMap = new HashMap();
                        List<Map> delResources = (List<Map>) res.get("delFiles");
                        for (Map obj : delResources) {
                            resourceMap.clear();
                            resourceMap.put("id",obj.get("id"));
                            resourceDao.deleteResource(resourceMap);
                        }
                    }
                }
                if (res.get("files") instanceof List) {
                    HashMap resourceMap = new HashMap();
                    List<Map> resources = (List<Map>) res.get("files");
                    for (Map obj : resources) {
                        resourceMap.clear();
                        resourceMap.put("id",obj.get("id"));
                        resourceMap.put("name", obj.get("name"));
                        resourceMap.put("url",obj.get("url"));
                        resourceMap.put("extend",obj.get("extend"));
                        resourceMap.put("size",obj.get("size"));
                        resourceMap.put("type","GI");
                        resourceMap.put("refId",uuid);
                        resourceMap.put("createUserId", user.getUserId());
                        resourceMap.put("lastCreateUserId", user.getUserId());
                        resourceMap.put("createTime", new Date());
                        resourceMap.put("lastCreateTime", new Date());
                        resourceDao.insertResourcet(resourceMap);
                    }
                }
                rtn.setCode(200);
                rtn.setMessage("success");
            }else {
                rtn.setCode(404);
                rtn.setMessage("用户下无店铺！");
            }
        }
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    public Map setParameter(Map map) {
        Map<String,Object> parameter = new HashMap<>();
        parameter.put("spec_value_name",map.get("spec_value_name"));
        parameter.put("spec_sort",map.get("spec_sort"));
        parameter.put("correntPrice",map.get("correntPrice"));
        parameter.put("minPrice",map.get("minPrice"));
        parameter.put("cappedPrice",map.get("cappedPrice"));
        parameter.put("tipsType",map.get("tipsType"));
        parameter.put("tipsText",StringUtil.isEmpty(map.get("tipsText"))?"":map.get("tipsText"));
        parameter.put("tipsImg",StringUtil.isEmpty(map.get("tipsImg"))?"":map.get("tipsImg"));
        return parameter;
    }

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
                    obj.put("afileList",imgs);
                }
            });
            return goods;
        });
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
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
    public JsonObject batchUpdateGoods(JsonObject jsonObject) {
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

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
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

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
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

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
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

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject selectClassifySpecParam(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Goods");
        ArrayList reqList = new ArrayList();
        Map<String,Object> res = new HashMap<>();
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        res.clear();
        res.put("id",jsonObject.get("id").getAsString());
        List<Map<String, Object>> classifyList = classifyDao.selectClassify(res);
        for (Map obj:classifyList) {
            String classifyStr = obj.get("parameter").toString();
            if(!StringUtil.isEmpty(classifyStr)) {
                Map classifyMap = GsonHelper.getInstance().fromJson(classifyStr, Map.class);
                if(!StringUtil.isEmpty(classifyMap.get("spec"))) {
                    List<Map> specs = GsonHelper.getInstance().fromJson(classifyMap.get("spec").toString(), List.class);
                    for (Map obj1:specs) {
                        if(!StringUtil.isEmpty(obj1.get("id"))) {
                            res.clear();
                            res.put("id",obj1.get("id"));
                            List<Map<String, Object>> specList = specDao.selectSpec(res);
                            for (Map obj2:specList) {
                                String specStr = obj2.get("parameter").toString();
                                if(!StringUtil.isEmpty(specStr)) {
                                    List<Map> specParamList = GsonHelper.getInstance().fromJson(specStr, List.class);
                                    Map reqMap = new HashMap();
                                    reqMap.put("name",obj2.get("specName"));
                                    reqMap.put("id",obj2.get("id"));
                                    reqMap.put("px",obj2.get("px"));
                                    for (Map obj3:specParamList) {
                                        obj3.put("name",obj2.get("specName"));
                                        obj3.put("id",obj2.get("id"));
                                        obj3.put("px",obj2.get("px"));
                                    }
                                    reqMap.put("param",specParamList);
                                    reqList.add(reqMap);
                                }
                            }
                        }
                    }
                }
            }
        }
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(reqList)).getAsJsonArray();
        rtn.setCode(200);
        rtn.setMessage("success");
        data.add("items",jsonArray);
        rtn.setData(data);
        return  Func.functionRtnToJsonObject.apply(rtn);
    }

    //勿修改此方法
    @Override
    public JsonObject selectGoodsClassifyCascade(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Goods");
        JsonObject data = new JsonObject();
        Map res = new HashMap();
        res.clear();
        res.put("cascade", 'Y');
        List<Map<String, Object>> req = classifyDao.selectClassify(res);
        //获取所有分类层级数据
        for(Map obj1:req) {
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
        res.clear();
        res.put("goodsId",jsonObject.get("goodsId").getAsString());
        List classify123s = classifyDao.selectGoodsClassify123(res);
        if(classify123s.size() > 0) {
            String text = "";
            Map history = new HashMap();
            Map result = new HashMap();
            Map result1 = new HashMap();
            Map result2 = new HashMap();
            Map result3 = new HashMap();
            history.put("isShow2",true);
            history.put("isShow3",true);
            List<Map> children2 = new ArrayList();
            List<Map> children3 = new ArrayList();
            Map classify123Map = (Map) classify123s.get(0);
            //一级分类历史数据
            if(!StringUtil.isEmpty(classify123Map.get("id1"))) {
                history.put("classifyCascades1",req);
                for(int i=0; i< req.size(); i++) {
                    if(req.get(i).get("id").equals(classify123Map.get("id1"))) {
                        children2 = (List) req.get(i).get("children");
                        history.put("getIndex1",i);
                        text += req.get(i).get("name");
                        result1.putAll(req.get(i));
                    }
                }
            }
            //二级分类历史数据
            if(!StringUtil.isEmpty(classify123Map.get("id2"))) {
                history.put("classifyCascades2",children2);
                for(int i=0; i< children2.size(); i++) {
                    if(children2.get(i).get("id").equals(classify123Map.get("id2"))) {
                        children3 = (List) children2.get(i).get("children");
                        history.put("getIndex2",i);
                        text += " > " + children2.get(i).get("name");
                        result2.putAll(children2.get(i));
                    }
                }
            }
            //三级分类历史数据
            if(!StringUtil.isEmpty(classify123Map.get("id3"))) {
                history.put("classifyCascades3",children3);
                for(int i=0; i< children3.size(); i++) {
                    if(children3.get(i).get("id").equals(classify123Map.get("id3"))) {
                        history.put("getIndex3",i);
                        text += " > " + children3.get(i).get("name");
                        result3.putAll(children3.get(i));
                    }
                }
            }
            history.put("text",text);
            //整合数据
            result.clear();
            if(result3.size() > 0) {
                result.putAll(result3);
            }else if(result2.size() > 0) {
                result.putAll(result2);
            }else {
                result.putAll(result1);
            }

            result.put("history",history);
            data =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(result)).getAsJsonObject();
            rtn.setData(data);
            rtn.setCode(200);
            rtn.setMessage("success");
        }else {
            rtn.setCode(404);
            rtn.setMessage("分类不存在！");
        }
        return  Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
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

    @Override
    public JsonObject queryLable(JsonObject jsonObject) {
        jsonObject.addProperty("service","goods");
        return query(jsonObject,(map)->{
            return goodsDao.selectLable(map);
        });
    }
}
