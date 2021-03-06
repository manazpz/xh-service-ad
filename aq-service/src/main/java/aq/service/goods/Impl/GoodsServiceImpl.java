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
import aq.service.goods.GoodsService;
import aq.service.system.Func;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mongodb.util.Hash;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by ywb on 2017-02-23.
 */
@Service("serviceGoods")
@DyncDataSource
public class GoodsServiceImpl extends BaseServiceImpl  implements GoodsService {

    @Resource
    private GoodsDao goodsDao;
    @Resource
    private SpecDao specDao;
    @Resource
    private ResourceDao resourceDao;
    @Resource
    private StockDao stockDao;

    @Resource
    private ClassifyDao classifyDao;

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
            if(Boolean.valueOf(res.get("choice").toString())){
                res.put("obligate1","Y");
            }else if(!Boolean.valueOf(res.get("choice").toString())){
                res.put("obligate1","R");
                res.put("obligate2",res.get("msg"));
            }
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
    public JsonObject insertGoodsClassify(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("goods");
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> rest = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        if(0 != Integer.parseInt(res.get("index").toString())) {
            rest.put("parentId", res.get("parentId"));
        }
        rest.put("id", UUIDUtil.getUUID());
        rest.put("name", res.get("name"));
        rest.put("model", res.get("model"));
        rest.put("px", res.get("px"));
        rest.put("obligate1",Integer.parseInt(res.get("index").toString())+1);
        rest.put("remarks", res.get("remarks"));
        rest.put("createTime",new Date());
        rest.put("lastCreateTime",new Date());
        rest.put("createUser",user.getUserId());
        rest.put("lastCreateUser",user.getUserId());
        classifyDao.insertClassify(rest);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryclassify(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Goods");
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> rest = new HashMap<>();
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        if (StringUtil.isEmpty(jsonObject.get("cascade"))) {
            res.put("cascade", 'Y');
        }
        if (!StringUtil.isEmpty(jsonObject.get("model"))) {
            res.put("model", jsonObject.get("model").getAsString());
        }
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
    public JsonObject queryclassifyTree(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Goods");
        Map<String,Object> res = new HashMap<>();
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        res.put("obligate1","1");
        List<Map<String, Object>> req1 = classifyDao.selectClassifyTree(res);
        for(Map obj1:req1) {
            res.clear();
            res.put("parentId",obj1.get("id"));
            List<Map<String, Object>> req2 = classifyDao.selectClassifyTree(res);
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
    public JsonObject queryBrandTree(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Goods");
        Map<String,Object> res = new HashMap<>();
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        List<Map<String, Object>> req1 = goodsDao.selectBrandClass(res);
        for(Map obj1:req1) {
            res.clear();
            res.put("classId",obj1.get("id"));
            List<Map<String, Object>> req2 = goodsDao.selectBrandCorr(res);
            obj1.put("children",req2);
            obj1.put("parentId","");
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
    public JsonObject updateClassify(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("goods");
        JsonObject jsonObjects = new JsonObject();
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> rest = new HashMap<>();
        Map<String,Object> rests = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        if(res.get("parameter")!= null){
            jsonObjects =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(res.get("parameter"))).getAsJsonObject();
            res.put("parameter",jsonObjects.toString());
        }
        res.put("updateTime",new Date());
        res.put("lastCreateUser",user.getUserId());
        classifyDao.updateClassify(res);
        if(res.get("parentId") == null){
            rest.clear();
            rest.put("parentId",res.get("parentId"));
            List<Map<String, Object>> maps = classifyDao.selectClassify(rest);
            if(maps.size()>0){
                for(Map obj:maps) {
                    rests.put("id",obj.get("id"));
                    rests.put("model",res.get("model"));
                    classifyDao.updateClassify(rests);
                    rest.clear();
                    rest.put("parentId",rests.get("id"));
                    List<Map<String, Object>> maps1 = classifyDao.selectClassify(rest);
                    if(maps1.size()>0){
                        for(Map obj1:maps1) {
                            rests.clear();
                            rests.put("id",obj1.get("id"));
                            rests.put("model",res.get("model"));
                            classifyDao.updateClassify(rests);
                        }
                    }
                }
            }
        }
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject querybrandList(JsonObject jsonObject) {
        jsonObject.addProperty("service","goods");
        return query(jsonObject,(map)->{
            List<Map<String, Object>> maps = goodsDao.selectBrand(map);
            for (Map obj : maps) {
                Map<String,Object> res = new HashMap<>();
                res.put("type","PP");
                res.put("refId",obj.get("id"));
                List<Map<String, Object>> resource = resourceDao.selectResource(res);
                res.clear();
                res.put("id",obj.get("id"));
                List<Map<String, Object>> maps1 = goodsDao.selectBrandCorr(res);
                if(resource.size()>0){
                    obj.put("imgUrl",resource.get(0).get("url"));
                }
                if(maps1.size()>0){
                    obj.put("checkList",maps1);
                }else{
                    obj.put("checkList","");
                }
            }
            return maps;
        });
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject querybrandClassList(JsonObject jsonObject) {
        jsonObject.addProperty("service","goods");
        return query(jsonObject,(map)->{
            return goodsDao.selectBrandClass(map);
        });
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject insertBrand(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("goods");
        Map<String,Object> res = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        String uuid = UUIDUtil.getUUID();
        res.put("id", uuid);
        res.put("official", res.get("officialWebsite"));
        res.put("createTime",new Date());
        res.put("lastCreateTime",new Date());
        res.put("createUser",user.getUserId());
        res.put("lastCreateUser",user.getUserId());
        goodsDao.insertBrand(res);
        if(res.get("classId") instanceof List){
            List classId = (List) res.get("classId");
            for(int i=0; i<classId.size(); i++){
                HashMap maps = new HashMap();
                maps.put("brand_id",uuid);
                maps.put("classId",classId.get(i));
                goodsDao.insertBrandCorr(maps);
            }
        }
        if (res.get("file")!= null) {
            HashMap resourceMap = new HashMap();
            resourceMap.clear();
            Map file = GsonHelper.getInstance().fromJson(GsonHelper.getInstance().toJson(res.get("file")), Map.class);
            if(file.size()>0){
                resourceMap.put("id",file.get("id"));
                resourceMap.put("name", file.get("name"));
                resourceMap.put("url",file.get("url"));
                resourceMap.put("extend",file.get("extend"));
                resourceMap.put("size",file.get("size"));
                resourceMap.put("type","PP");
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
        return Func.functionRtnToJsonObject.apply(rtn);
    }


    @Override
    public JsonObject insertBrandClass(JsonObject jsonObject) {
        Rtn rtn = new Rtn("goods");
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        res.put("id",UUIDUtil.getUUID());
        res.put("createTime",new Date());
        goodsDao.insertBrandClass(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject updateBrand(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("goods");
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> rest = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        res.put("id", res.get("id"));
        res.put("official", res.get("officialWebsite"));
        res.put("updateTime",new Date());
        res.put("lastCreateUser",user.getUserId());
        goodsDao.updateBrand(res);
        rest.clear();
        rest.put("type","PP");
        rest.put("refId",res.get("id"));
        if(res.get("classId") instanceof List){
            List classId = (List) res.get("classId");
            HashMap map = new HashMap();
            map.put("brand_id",res.get("id"));
            goodsDao.deleteBrandCorr(map);
            for(int i=0; i<classId.size(); i++){
                map.put("classId",classId.get(i));
                goodsDao.insertBrandCorr(map);
            }
        }
        if (res.get("file")!= null) {
            HashMap resourceMap = new HashMap();
            Map file = GsonHelper.getInstance().fromJson(GsonHelper.getInstance().toJson(res.get("file")), Map.class);
            if(file.size()>0){
                List<Map<String, Object>> maps = resourceDao.selectResource(rest);
                if(maps.size()>0){
                    HashMap map = new HashMap();
                    map.put("id",maps.get(0).get("id"));
                    resourceDao.deleteResource(map);
                }
                resourceMap.clear();
                resourceMap.put("id",file.get("id"));
                resourceMap.put("name", file.get("name"));
                resourceMap.put("url",file.get("url"));
                resourceMap.put("extend",file.get("extend"));
                resourceMap.put("size",file.get("size"));
                resourceMap.put("type","PP");
                resourceMap.put("refId",res.get("id"));
                resourceMap.put("createUserId", user.getUserId());
                resourceMap.put("lastCreateUserId", user.getUserId());
                resourceMap.put("createTime", new Date());
                resourceMap.put("lastCreateTime", new Date());
                resourceDao.insertResourcet(resourceMap);
            }
        }
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject updateBrandClass(JsonObject jsonObject) {
        Rtn rtn = new Rtn("goods");
        Map<String, Object> res = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject, Map.class);
        res.put("id", res.get("id"));
        res.put("name", res.get("label"));
        goodsDao.updateBrandClass(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);

    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject deleteBrand(JsonObject jsonObject) {
        Rtn rtn = new Rtn("goods");
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        goodsDao.deleteBrand(res);
        res.put("brand_id",res.get("id"));
        goodsDao.deleteBrandCorr(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject deleteBrandClass(JsonObject jsonObject) {
        Rtn rtn = new Rtn("goods");
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        goodsDao.deleteBrandClass(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryspec(JsonObject jsonObject) {
        jsonObject.addProperty("service","Goods");
        return query(jsonObject,(map)->{
            return specDao.selectSpec(map);
        });
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject insertSpec(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("goods");
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> rest = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        rest.put("id",res.get("grouName"));
        List<Map<String, Object>> classify = classifyDao.selectClassify(rest);
        if(classify.size()>0){
            res.put("obligate2",res.get("grouName"));
            res.put("grouName",classify.get(0).get("name"));
        }
        res.put("id", UUIDUtil.getUUID());
        res.put("createTime",new Date());
        res.put("lastCreateTime",new Date());
        res.put("createUser",user.getUserId());
        res.put("lastCreateUser",user.getUserId());
        specDao.insertSpec(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject updateSpec(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("Goods");
        Map<String,Object> res = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        res.put("id", res.get("id"));
        res.put("updateTime",new Date());
        res.put("lastCreateUser",user.getUserId());
        specDao.updateSpec(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject deleteSpec(JsonObject jsonObject) {
        Rtn rtn = new Rtn("goods");
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        specDao.deleteSpec(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject insertSpecValue(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("goods");
        JsonArray jsonArray = new JsonArray();
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> rest = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        res.put("id", res.get("id"));
        rest.put("spec_value_name",res.get("spec_value_name").toString());
        rest.put("spec_sort",res.get("spec_sort").toString());
        rest.put("correntPrice","0");
        rest.put("minPrice","0");
        rest.put("cappedPrice","2147483647");
        rest.put("tipsImg","");
        rest.put("tipsText","");
        rest.put("tipsType","");

        List<Map<String, Object>> maps = specDao.selectSpec(res);
        if(maps.size()>0){
            if(StringUtil.isEmpty(maps.get(0).get("parameter"))){
                list.add(rest);
                jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(list)).getAsJsonArray();
                res.put("parameter", jsonArray.toString());
            }else{
                List specParameter = GsonHelper.getInstance().fromJson(maps.get(0).get("parameter").toString(), List.class);
                specParameter.add(rest);
                jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(specParameter)).getAsJsonArray();
                res.put("parameter", jsonArray.toString());
            }
            res.put("lastCreateTime",new Date());
            res.put("lastCreateUser",user.getUserId());
            specDao.updateSpec(res);
            rtn.setCode(200);
            rtn.setMessage("success");
        }else{
            rtn.setCode(404);
            rtn.setMessage("参数不符合！");
        }
        return Func.functionRtnToJsonObject.apply(rtn);
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryspecValue(JsonObject jsonObject) {
        Rtn rtn = new Rtn("goods");
        Map<String,Object> res = new HashMap<>();
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        JsonObject json =new JsonObject();
        res.clear();
        res.put("id",jsonObject.get("id").getAsString());
        List<Map<String, Object>> list = specDao.selectSpec(res);
        if(list.size()>0) {
            String parameterStr = (String) ((Map) list.get(0)).get("parameter");
            if(!StringUtil.isEmpty(parameterStr)) {
                List specParameter = GsonHelper.getInstance().fromJson(parameterStr, List.class);
                jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(specParameter)).getAsJsonArray();
            }
            data.addProperty("tipsType",list.get(0).get("tipsType").toString());
            data.addProperty("groupName",list.get(0).get("grouName").toString());
            data.addProperty("specName",list.get(0).get("specName").toString());
        }
        rtn.setCode(200);
        rtn.setMessage("success");
        data.add("items",jsonArray);
        rtn.setData(data);
        return  Func.functionRtnToJsonObject.apply(rtn);
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject updateSpecValue(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("Goods");
        Map<String,Object> res = new HashMap<>();
        JsonArray jsonArray = new JsonArray();
        res.put("id",jsonObject.get("id").getAsString());
        List<Map<String, Object>> list = specDao.selectSpec(res);
        if(list.size()>0) {
            String parameterStr = (String) ((Map) list.get(0)).get("parameter");
            if(!StringUtil.isEmpty(parameterStr)) {
                List specParameter = GsonHelper.getInstance().fromJson(parameterStr, List.class);
                if(jsonObject.get("del")!= null){
                    if("Y".equals(jsonObject.get("del").getAsString())){
                        specParameter.remove(Integer.parseInt(jsonObject.get("index").getAsString()));
                    }
                }
                else{
                    Map<String,Object> specmap = (Map<String, Object>) specParameter.get(Integer.parseInt(jsonObject.get("index").getAsString()));
                    specmap.put("spec_value_name",jsonObject.get("spec_value_name").getAsString());
                    specmap.put("spec_sort",jsonObject.get("spec_sort").getAsString());
                    specmap.put("correntPrice","0");
                    specmap.put("minPrice","0");
                    specmap.put("cappedPrice","2147483647");
                    specmap.put("tipsImg","");
                    specmap.put("tipsText","");
                    specmap.put("tipsType","");
                }
                jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(specParameter)).getAsJsonArray();
            }
        }
        res.put("id", res.get("id"));
        res.put("parameter", jsonArray.toString());
        res.put("updateTime",new Date());
        res.put("lastCreateUser",user.getUserId());
        specDao.updateSpec(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject updateClassifyBrand(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("Goods");
        JsonObject jsonObjects = new JsonObject();
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> rest = new HashMap<>();
        Map parameter = new HashMap();
        List<Map<String, Object>> listbrand = new ArrayList<>();
        List<Map<String, Object>> listspec = new ArrayList<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        rest.put("id", res.get("id"));
        List<Map<String, Object>> maps = classifyDao.selectClassify(rest);
        rest.clear();
        if(maps.size()>0){
            if(maps.get(0).get("parameter")!= null){
                parameter = GsonHelper.getInstance().fromJson(maps.get(0).get("parameter").toString(), Map.class);
            }
            if(res.get("name") != null ){
                List brandid = GsonHelper.getInstance().fromJson(res.get("name").toString(), List.class);
                brandid.forEach(obj->{
                    Map<String,Object> rests = new HashMap<>();
                    rest.put("id",obj);
                    List<Map<String, Object>> mapsbrand = goodsDao.selectBrand(rest);
                    if(mapsbrand.size()>0){
                        rests.put("name",mapsbrand.get(0).get("name"));
                    }
                    rests.put("id",rest.put("id",obj));
                    listbrand.add(rests);
                });
                parameter.put("brand", listbrand);
            }
            if(res.get("specId") != null ){
                List specId = GsonHelper.getInstance().fromJson(res.get("specId").toString(), List.class);
                specId.forEach(obj->{
                    Map<String,Object> rests = new HashMap<>();
                    rest.put("id",obj);
                    List<Map<String, Object>> mapsbrand = specDao.selectSpec(rest);
                    if(mapsbrand.size()>0){
                        rests.put("specName",mapsbrand.get(0).get("specName"));
                    }
                    rests.put("id",rest.put("id",obj));
                    listspec.add(rests);
                });
                parameter.put("spec", listspec);
            }
            rest.clear();
            jsonObjects =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(parameter)).getAsJsonObject();
            rest.put("parameter",jsonObjects.toString());
            rest.put("id", res.get("id"));
            rest.put("lastCreateTime",new Date());
            rest.put("lastCreateUser",user.getUserId());
            classifyDao.updateClassify(rest);
            rtn.setCode(200);
            rtn.setMessage("success");
        }else{
            rtn.setCode(404);
            rtn.setMessage("参数不符合！");
        }
        return Func.functionRtnToJsonObject.apply(rtn);
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject deleteClassify(JsonObject jsonObject) {
        Rtn rtn = new Rtn("goods");
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        classifyDao.deleteClassify(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }
}
