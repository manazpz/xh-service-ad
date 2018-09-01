package aq.service.goods.Impl;

import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.*;
import aq.dao.goods.GoodsDao;
import aq.dao.resource.ResourceDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.goods.GoodsApiService;
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
@Service("serviceGoodsApi")
@DyncDataSource
public class GoodsApiServiceImpl extends BaseServiceImpl  implements GoodsApiService {

    @Resource
    private GoodsDao goodsDao;

    @Resource
    private ResourceDao resourceDao;

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryGoods(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Goods");
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        List<Map<String, Object>> goods = goodsDao.selectGoods(res);
        goods.forEach(obj->{
            if(!StringUtil.isEmpty(obj.get("specParameter"))) {
                List specParameter = GsonHelper.getInstance().fromJson(obj.get("specParameter").toString(), List.class);
                obj.put("specParameter",specParameter);
            }
            if(!StringUtil.isEmpty(obj.get("id"))) {
                Map<String,Object> ress = new HashMap<>();
                ress.put("type","GI");
                ress.put("refId",obj.get("id"));
                List imgs = resourceDao.selectResource(ress);
                obj.put("imgs",imgs);
            }
        });
        rtn.setCode(200);
        rtn.setMessage("success");
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(goods)).getAsJsonArray();
        data.addProperty("total",goods.size());
        data.add("items",jsonArray);
        rtn.setData(data);
        return  Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryChoices(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Goods");
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        Map<String,Object> res = new HashMap<>();
        //查询品牌
        List<Map<String, Object>> brands = goodsDao.selectBrand(res);
        brands.forEach(obj->{
            res.clear();
            res.put("brandId",obj.get("id"));
            List<Map<String, Object>> goods = goodsDao.selectGoods(res);
            obj.put("goods",goods);
        });
        rtn.setCode(200);
        rtn.setMessage("success");
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(brands)).getAsJsonArray();
        data.addProperty("total",brands.size());
        data.add("items",jsonArray);
        rtn.setData(data);
        return  Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject querygoodsSpecs(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Goods");
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        Map<String,Object> res = new HashMap<>();
        List specParameter = new ArrayList();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        List<Map<String, Object>> goods = goodsDao.selectGoods(res);
        for(Map obj : goods) {
            if(!StringUtil.isEmpty(obj.get("specParameter"))) {
                specParameter = GsonHelper.getInstance().fromJson(obj.get("specParameter").toString(), List.class);
            }
        }
        if(specParameter.size() > 0) {
            CollectionsUtil.listMapSort(specParameter,"px");
        }
        rtn.setCode(200);
        rtn.setMessage("success");
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(specParameter)).getAsJsonArray();
        data.addProperty("total",jsonArray.size());
        data.add("items",jsonArray);
        rtn.setData(data);
       return  Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject evaluates(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Goods");
        Map<String,Object> res = new HashMap<>();
        double sum = 0;
        if(StringUtil.isEmpty(jsonObject.get("goodsId").getAsString())) {
            rtn.setCode(404);
            rtn.setMessage("商品主键不为空！");
        }else {
            res.clear();
            res.put("id",jsonObject.get("goodsId").getAsString());
            List<Map<String, Object>> goods = goodsDao.selectGoods(res);
            if(goods.size() > 0) {
                Double banPrice = Double.parseDouble(goods.get(0).get("banPrice").toString());
                List<Map> specParameter = GsonHelper.getInstance().fromJson(goods.get(0).get("specParameter").toString(), List.class);
                if(jsonObject.get("parameter").getAsJsonArray() != null) {
                    List<Map> params = GsonHelper.getInstance().fromJson(jsonObject.get("parameter").getAsJsonArray(), List.class);
                    for (Map obj : params) {
                        for (Map obj1 : specParameter) {
                            if(obj.get("id").equals(obj1.get("id"))) {
                                List<Map> list1 = (List) obj.get("parameter");
                                List<Map> list2 = (List) obj1.get("parameter");
                                for (Map obj2 : list1) {
                                    for (Map obj3 : list2) {
                                        if(obj2.get("value").equals(obj3.get("value"))) {
                                            Double correntPrice = Double.parseDouble(obj3.get("correntPrice").toString());
                                            Double minPrice = Double.parseDouble(obj3.get("minPrice").toString());
                                            Double cappedPrice = Double.parseDouble(obj3.get("cappedPrice").toString());
                                            Double price = banPrice + correntPrice;
                                            sum = sum + NumUtil.compareDouble(price,minPrice,cappedPrice)-banPrice;
                                            break;
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                    JsonArray jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(specParameter)).getAsJsonArray();
                    jsonObject.add("allParameter", jsonArray);
                    jsonObject.addProperty("price",sum + banPrice);
                    rtn.setCode(200);
                    rtn.setMessage("success");
                }else {
                    rtn.setCode(404);
                    rtn.setMessage("评估参数不符合！");
                }
            }else {
                rtn.setCode(404);
                rtn.setMessage("无此商品！");
            }
        }
        rtn.setData(jsonObject);
        return  Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject insertReplacementCar(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Goods");
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res.put("id",UUIDUtil.getUUID());
        res.put("goodsId",jsonObject.get("goodsId").getAsString());
        res.put("parameter",jsonObject.get("parameter").getAsJsonArray().toString());
        res.put("price",jsonObject.get("price").getAsString());
        res.put("tips",jsonObject.get("tips").getAsString());
        res.put("createUser",jsonObject.get("openId").getAsString());
        res.put("createTime",new Date());
        res.put("lastCreateTime",new Date());
        goodsDao.insertReplacementCar(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return  Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject updateReplacementCar(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Goods");
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res.put("id",jsonObject.get("id").getAsString());
        res.put("parameter",jsonObject.get("parameter").getAsJsonArray().toString());
        res.put("price",jsonObject.get("price").getAsString());
        res.put("tips",jsonObject.get("tips").getAsString());
        res.put("lastCreateTime",new Date());
        goodsDao.updateReplacementCar(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return  Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryReplacementCar(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Goods");
        Map<String,Object> res = new HashMap<>();
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        List<Map<String, Object>> blls = goodsDao.selectReplacementCar(res);
        blls.forEach(obj->{
            if(!StringUtil.isEmpty(obj.get("goodsParameter"))) {
                List goodsParameter = GsonHelper.getInstance().fromJson(obj.get("goodsParameter").toString(), List.class);
                obj.put("goodsParameter",goodsParameter);
            }
            if(!StringUtil.isEmpty(obj.get("bllParameter"))) {
                List bllParameter = GsonHelper.getInstance().fromJson(obj.get("bllParameter").toString(), List.class);
                obj.put("bllParameter",bllParameter);
            }
        });
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(blls)).getAsJsonArray();
        data.addProperty("total",jsonArray.size());
        data.add("items",jsonArray);
        rtn.setCode(200);
        rtn.setMessage("success");
        rtn.setData(data);
        return  Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject deleteReplacementCar(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Goods");
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        goodsDao.deleteReplacementCar(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return  Func.functionRtnToJsonObject.apply(rtn);
    }

}