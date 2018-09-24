package aq.service.goods.Impl;

import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.*;
import aq.dao.goods.ClassifyDao;
import aq.dao.goods.GoodsDao;
import aq.dao.goods.SpecDao;
import aq.dao.resource.ResourceDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.goods.GoodsApiService;
import aq.service.system.Func;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
    private SpecDao specDao;

    @Resource
    private ClassifyDao classifyDao;

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

    @Override
    public JsonObject queryLableGoods(JsonObject jsonObject) {
        jsonObject.addProperty("service","goods");
        return query(jsonObject,(map)->{
            Map res = new HashMap();
            if(StringUtil.isEmpty(map.get("model"))) {
                ArrayList req = new ArrayList();
                map.put("model","01");
                List<Map<String, Object>> newGoods = goodsDao.selectGoods(map);
                map.put("model","02");
                PageHelper.startPage(1,1);
                PageInfo oldPageInfo = new PageInfo(goodsDao.selectGoods(map));
                res.clear();
                res.put("oldGoods",oldPageInfo.getList());
                res.put("newGoods",newGoods);
                req.add(res);
                return req;

            }else {
                return goodsDao.selectGoods(map);
            }
        });
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryChoices(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Goods");
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> rest = new HashMap<>();
        List<Map<String, Object>> req3 =new ArrayList<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        //查询分类信息
        rest.put("model",res.get("model"));
        rest.put("cascade",'Y');
        List<Map<String, Object>> maps = classifyDao.selectClassify(rest);
        for(Map obj1:maps) {
            rest.clear();
            rest.put("parentId",obj1.get("id"));
            List<Map<String, Object>> req2 = classifyDao.selectClassify(rest);
            for(Map obj2:req2) {
                rest.clear();
                rest.put("parentId",obj2.get("id"));
                req3 = classifyDao.selectClassify(rest);
                for(Map obj3:req3) {
                    rest.clear();
                    rest.put("classifyId",obj3.get("id"));
                    rest.put("status","01");
                    rest.put("model",StringUtil.isEmpty(jsonObject.get("model"))?"":jsonObject.get("model").getAsString());
                    List<Map<String, Object>> goods = goodsDao.selectGoods(rest);
                    obj3.put("goods",goods);
                }

            }
            obj1.put("detail",req3);
        }
//        maps.forEach(obj->{
//            resa.clear();
//            resa.put("brandId",obj.get("id"));
//            if(!StringUtil.isEmpty(jsonObject.get("model")) && "01".equals(jsonObject.get("model").getAsString())) {
//                resa.put("status","01");
//            }
//            resa.put("model",StringUtil.isEmpty(jsonObject.get("model"))?"":jsonObject.get("model").getAsString());
//            List<Map<String, Object>> goods = goodsDao.selectGoods(resa);
//            obj.put("goods",goods);
//        });
        rtn.setCode(200);
        rtn.setMessage("success");
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(maps)).getAsJsonArray();
        data.addProperty("total",maps.size());
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
        List<Map> specParameter = new ArrayList();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        List<Map<String, Object>> goods = goodsDao.selectGoods(res);
        for(Map obj : goods) {
            if(!StringUtil.isEmpty(obj.get("specParameter"))) {
                specParameter = GsonHelper.getInstance().fromJson(obj.get("specParameter").toString(), List.class);
                for(Map spec : specParameter) {
                    res.clear();
                    res.put("id",spec.get("id"));
                    List<Map<String, Object>> ls = specDao.selectSpec(res);
                    if(ls.size() > 0) {
                        spec.put("tipsType", StringUtil.isEmpty(ls.get(0).get("tipsType"))?"":ls.get(0).get("tipsType"));
                        spec.put("tipsText", StringUtil.isEmpty(ls.get(0).get("tipsText"))?"":ls.get(0).get("tipsText"));
                        spec.put("tipsImg", StringUtil.isEmpty(ls.get(0).get("tipsImg"))?"":ls.get(0).get("tipsImg"));
                        spec.put("obligate", StringUtil.isEmpty(ls.get(0).get("obligate"))?"01":ls.get(0).get("obligate"));
                    }
                }
            }
        }
        if(goods.size() > 0 && "02".equals(goods.get(0).get("model"))) {
            if(specParameter.size() > 0) {
                CollectionsUtil.listMapSort(specParameter,"px");
            }
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
        res.put("userId",jsonObject.get("openId").getAsString());
        res.put("goodsId",jsonObject.get("goodsId").getAsString());
        res.put("model",jsonObject.get("model").getAsString());
        res.put("parameter",jsonObject.get("parameter").toString());
        if("01".equals(jsonObject.get("model").getAsString())) {
            res.put("price",jsonObject.get("price").getAsString());
        }
        if("02".equals(jsonObject.get("model").getAsString())) {
            Double sum = 0.0;
            Double banPrice = Double.parseDouble(jsonObject.get("banPrice").getAsString());
            for (int i=0;i<jsonObject.get("parameter").getAsJsonArray().size();i++) {
                JsonArray asJsonArray = jsonObject.get("parameter").getAsJsonArray().get(i).getAsJsonObject().get("spec").getAsJsonArray();
                for (int j=0;j<asJsonArray.size();j++) {
                    Double correntPrice = Double.parseDouble(asJsonArray.get(j).getAsJsonObject().get("correntPrice").getAsString());
                    Double minPrice = Double.parseDouble(asJsonArray.get(j).getAsJsonObject().get("minPrice").getAsString());
                    Double cappedPrice = Double.parseDouble(asJsonArray.get(j).getAsJsonObject().get("cappedPrice").getAsString());
                    Double price = banPrice + correntPrice;
                    sum = sum + NumUtil.compareDouble(price,minPrice,cappedPrice)-banPrice;
                }
            }
            res.put("price",sum + banPrice);
        }
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
        res.put("parameter",jsonObject.get("parameter").toString());
        if("01".equals(jsonObject.get("model").getAsString())) {
            res.put("price",jsonObject.get("price").getAsString());
        }
        if("02".equals(jsonObject.get("model").getAsString())) {
            Double sum = 0.0;
            Double banPrice = Double.parseDouble(jsonObject.get("banPrice").getAsString());
            for (int i=0;i<jsonObject.get("parameter").getAsJsonArray().size();i++) {
                JsonArray asJsonArray = jsonObject.get("parameter").getAsJsonArray().get(i).getAsJsonObject().get("spec").getAsJsonArray();
                for (int j=0;j<asJsonArray.size();j++) {
                    Double correntPrice = Double.parseDouble(asJsonArray.get(j).getAsJsonObject().get("correntPrice").getAsString());
                    Double minPrice = Double.parseDouble(asJsonArray.get(j).getAsJsonObject().get("minPrice").getAsString());
                    Double cappedPrice = Double.parseDouble(asJsonArray.get(j).getAsJsonObject().get("cappedPrice").getAsString());
                    Double price = banPrice + correntPrice;
                    sum = sum + NumUtil.compareDouble(price,minPrice,cappedPrice)-banPrice;
                }
            }
            res.put("price",sum + banPrice);
        }
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
        List newGoods = new ArrayList();
        List oldGoods = new ArrayList();
//        JsonObject data = new JsonObject();
//        JsonArray jsonArray = new JsonArray();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        if(!StringUtil.isEmpty(res.get("ids"))) {
            String[] ids = res.get("ids").toString().split(",");
            res.clear();
            res.put("ids",ids);
        }
        List<Map<String, Object>> blls = goodsDao.selectReplacementCar(res);
        blls.forEach(obj->{
            if(!StringUtil.isEmpty(obj.get("goodsParameter"))) {
                List goodsParameter = GsonHelper.getInstance().fromJson(obj.get("goodsParameter").toString(), List.class);
                obj.put("goodsParameter",goodsParameter);
            }
            if(!StringUtil.isEmpty(obj.get("bllParameter"))) {
                String str = "";
                List<Map> bllParameter = GsonHelper.getInstance().fromJson(obj.get("bllParameter").toString(), List.class);
                for(Map map:bllParameter){
                    List<Map> parameter = (List) map.get("spec");
                    if(parameter != null)
                        str += parameter.get(0).get("spec_value_name").toString() + " ";
                }
                obj.put("bllParameterStr",str);
                obj.put("bllParameter",bllParameter);
            }
            Map<String,Object> ress = new HashMap<>();
            ress.put("type","GI");
            ress.put("refId",obj.get("goodsId"));
            List imgs = resourceDao.selectResource(ress);
            obj.put("imgs",imgs);
            if("01".equals(obj.get("model"))){
                newGoods.add(obj);
            }
            if("02".equals(obj.get("model"))){
                oldGoods.add(obj);
            }
        });
        res.clear();
        res.put("newGoods",newGoods);
        res.put("oldGoods",oldGoods);
//        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(blls)).getAsJsonArray();
//        data.addProperty("total",jsonArray.size());
//        data.add("items",jsonArray);
        rtn.setCode(200);
        rtn.setMessage("success");
        rtn.setData(GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(res)).getAsJsonObject());
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
