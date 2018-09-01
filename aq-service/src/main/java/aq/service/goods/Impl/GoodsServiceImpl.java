package aq.service.goods.Impl;

import aq.common.access.AbsAccessUser;
import aq.common.access.Factory;
import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.*;
import aq.dao.goods.GoodsDao;
import aq.dao.goods.SpecDao;
import aq.dao.resource.ResourceDao;
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

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject insertGoodsClassify(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("Goods");
        Map<String,Object> res = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        res.put("id", UUIDUtil.getUUID());
        res.put("createTime",new Date());
        res.put("updateTime",new Date());
        res.put("createUser",user.getUserId());
        res.put("lastCreateUser",user.getUserId());
//        goodsDao.insertUserInfo(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryclassify(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Goods");
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        Map<String,Object> res = new HashMap<>();
        //查询分类明细
        List<Map<String, Object>> classifyFrist = goodsDao.selectClassifyFrist(res);
        classifyFrist.forEach(obj->{
            res.clear();
            res.put("parent_id",obj.get("id"));
            //查询二级分类明细
            List<Map<String, Object>> classifySecond = goodsDao.selectClassify(res);
            obj.put("secondClassify",classifySecond);
            classifySecond.forEach(objs->{
                res.clear();
                res.put("parent_id",objs.get("id"));
                //查询三级分类明细
                List<Map<String, Object>> classifyThird = goodsDao.selectClassify(res);
                objs.put("third",classifyThird);
            });
        });
        rtn.setCode(200);
        rtn.setMessage("success");
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(classifyFrist)).getAsJsonArray();
        data.addProperty("total",classifyFrist.size());
        data.add("items",jsonArray);
        rtn.setData(data);
        return  Func.functionRtnToJsonObject.apply(rtn);
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject querybrandList(JsonObject jsonObject) {
        jsonObject.addProperty("service","Goods");
        return query(jsonObject,(map)->{
            return goodsDao.selectBrandList(map);
        });
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject insertBrand(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("Goods");
        Map<String,Object> res = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        res.put("id", UUIDUtil.getUUID());
        res.put("createTime",new Date());
        res.put("lastCreateTime",new Date());
        res.put("createUser",user.getUserId());
        res.put("lastCreateUser",user.getUserId());
        goodsDao.insertBrand(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject updateBrand(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("Goods");
        Map<String,Object> res = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        res.put("id", res.get("id"));
        res.put("updateTime",new Date());
        res.put("lastCreateUser",user.getUserId());
        goodsDao.updateBrand(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject deleteBrand(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Goods");
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        goodsDao.deleteBrand(res);
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
        Rtn rtn = new Rtn("Goods");
        Map<String,Object> res = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
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
        Rtn rtn = new Rtn("Goods");
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
        Rtn rtn = new Rtn("Goods");
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> rest = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        res.put("id", res.get("id"));
        rest.put("spec_value_name",res.get("spec_value_name"));
        rest.put("spec_sort",res.get("spec_sort"));
        rest.put("specImg",res.get("specImg"));
        List<Map<String, Object>> maps = specDao.selectSpec(res);
        if(maps.size()>0){
            if(StringUtil.isEmpty(maps.get(0).get("parameter"))){
                list.add(rest);
                res.put("parameter", list.toString());
            }else{
                List specParameter = GsonHelper.getInstance().fromJson(maps.get(0).get("parameter").toString(), List.class);
                specParameter.add(rest);
                res.put("parameter",specParameter.toString());
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
        Rtn rtn = new Rtn("Goods");
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        List<Map<String, Object>> specValue = specDao.selectSpec(res);
        specValue.forEach(obj->{
            Map parameter = GsonHelper.getInstance().fromJson(obj.get("parameter").toString(), Map.class);
        });
        rtn.setCode(200);
        rtn.setMessage("success");
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(specValue)).getAsJsonArray();
        data.addProperty("total",specValue.size());
        data.add("items",jsonArray);
        rtn.setData(data);
        return  Func.functionRtnToJsonObject.apply(rtn);
    }
}
