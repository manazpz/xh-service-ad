package aq.service.goods.Impl;

import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.*;
import aq.dao.goods.ClassifyDao;
import aq.dao.goods.GoodsDao;
import aq.dao.goods.SpecDao;
import aq.dao.resource.ResourceDao;
import aq.dao.user.CustomerDao;
import aq.dao.user.UserDao;
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

    @Resource
    private UserDao userDao;

    @Resource
    private CustomerDao customerDao;

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
        Rtn rtn = new Rtn("Goods");
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> ress = new HashMap<>();
        Map<String,Object> req = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        res.put("model","01");
        req.clear();
        List<Map<String, Object>> newHomeGoods = goodsDao.selectHomeGoods(res);
        if(newHomeGoods.size()>0) {
            ress.clear();
            ress.put("id",newHomeGoods.get(0).get("goodsId"));
            req.put("newGoods",goodsDao.selectGoods(ress));
        }else {
            PageHelper.startPage(1,1);
            PageInfo newPageInfo = new PageInfo(goodsDao.selectGoods(res));
            req.put("newGoods",newPageInfo.getList());
        }

        res.put("model","02");
        List<Map<String, Object>> oldHomeGoods = goodsDao.selectHomeGoods(res);
        if(oldHomeGoods.size()>0) {
            ress.clear();
            ress.put("id",oldHomeGoods.get(0).get("goodsId"));
            req.put("oldGoods",goodsDao.selectGoods(ress));
        }else {
            PageHelper.startPage(1,1);
            PageInfo oldPageInfo = new PageInfo(goodsDao.selectGoods(res));
            req.put("oldGoods",oldPageInfo.getList());
        }
        rtn.setCode(200);
        rtn.setMessage("success");
        JsonObject data = GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(req)).getAsJsonObject();
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
                    rest.put("obligate1","Y");
                    rest.put("model",StringUtil.isEmpty(jsonObject.get("model"))?"":jsonObject.get("model").getAsString());
                    List<Map<String, Object>> goods = goodsDao.selectGoods(rest);
                    obj2.put("goods",goods);
                }

            }
            obj1.put("detail",req2);
        }
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
                List<Map> bllParameter = GsonHelper.getInstance().fromJson(obj.get("bllParameter").toString(), List.class);
                String str = "";
                for(Map map:bllParameter){
                    List<Map> parameter = (List) map.get("spec");
                    for(Map map1:parameter){
                        if(map1 != null)
                            str += map1.get("spec_value_name").toString() + " ";
                    }
                }
                obj.put("bllParameterStr",str);
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
        Map<String,Object> rest = new HashMap<>();
        List bllId = new ArrayList();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        bllId.add(res.get("id"));
        rest.put("id",bllId);
        goodsDao.deleteReplacementCar(rest);
        rtn.setCode(200);
        rtn.setMessage("success");
        return  Func.functionRtnToJsonObject.apply(rtn);
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject recoveryList(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Goods");
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        List<Map<String, Object>> recoveryList = userDao.selectRecoveryList(res);
        res.put("id",jsonObject.get("openId").getAsString());
        List<Map<String, Object>> maps = userDao.selectRecoveryUserList(res);
        if(maps.size()>0){
            jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(maps)).getAsJsonArray();
            data.add("check",jsonArray);
        }
        rtn.setCode(200);
        rtn.setMessage("success");
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(recoveryList)).getAsJsonArray();
        data.addProperty("total",recoveryList.size());
        data.add("items",jsonArray);
        rtn.setData(data);
        return  Func.functionRtnToJsonObject.apply(rtn);
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject insertrecoveryListUser(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Goods");
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res.put("id",jsonObject.get("openId").getAsString());
        res.put("recoveryId",jsonObject.get("prete").getAsString());
        List<Map<String, Object>> maps = userDao.selectRecoveryUserList(res);
        if(maps.size()>0){
            userDao.updaterecoveryListUser(res);
        }else{
            userDao.insertrecoveryListUser(res);
        }
        rtn.setCode(200);
        rtn.setMessage("success");
        return  Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryrecoveryList(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Goods");
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        Map<String,Object> res = new HashMap<>();
        List<Map<String, Object>> mapall = null;
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        res.put("id",jsonObject.get("openId").getAsString());
        List<Map<String, Object>> maps = userDao.selectRecoveryUserList(res);
        if(maps.size()>0){
            mapall = maps;
        }else{
            List<Map<String, Object>> recoveryList = userDao.selectRecoveryList(res);
            mapall = recoveryList;
        }
        rtn.setCode(200);
        rtn.setMessage("success");
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(mapall)).getAsJsonArray();
        data.addProperty("total",mapall.size());
        data.add("items",jsonArray);
        rtn.setData(data);
        return  Func.functionRtnToJsonObject.apply(rtn);
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryGoodsComment(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Goods");
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> comment = new HashMap<>();
        Map<String,Object> replay = new HashMap<>();
        List<Map<String, Object>> commentList = new ArrayList();
        List<Map<String, Object>> replayList = new ArrayList();
        List list = new ArrayList();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        List<Map<String, Object>> goodsComment = goodsDao.selectGoodsComment(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(goodsComment)).getAsJsonArray();
        data.addProperty("total",goodsComment.size());
        data.add("items",jsonArray);
        rtn.setData(data);
        return  Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject updateHomeGoods(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Goods");
        Map<String,Object> res = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        List<Map<String, Object>> homeGoods = goodsDao.selectHomeGoods(res);
        if(homeGoods.size()>0) {
            res.put("id",homeGoods.get(0).get("id"));
            goodsDao.updateHomeGoods(res);
        }else {
            res.put("id",UUIDUtil.getUUID());
            goodsDao.insertHomeGoods(res);
        }
        rtn.setCode(200);
        rtn.setMessage("success");
        return  Func.functionRtnToJsonObject.apply(rtn);
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryForecastList(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Goods");
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> rest = new HashMap<>();
        int prices = 0;
        List datas = new ArrayList();
        List price = new ArrayList();
        List all = new ArrayList();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        List<Map<String, Object>> maps = goodsDao.selectForecastList(res);
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH )+1;
        String now = (month< 10 ? "0" +month: month+"")+ "月";
        datas.add(now);
        for(int i=0; i< 5; i++){
            cal.setTime(new Date());
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - i);
            datas.add((cal.get(Calendar.MONTH)< 10 ? "0" +cal.get(Calendar.MONTH)+"月": cal.get(Calendar.MONTH)+"月"));
        }
        Collections.reverse(datas);
        for(int i=0; i< maps.size(); i++){
            rest.put(maps.get(i).get("month").toString().split("-")[1]+"月",maps.get(i).get("price"));
        }
        for(int i=0; i< datas.size(); i++){
            if(rest.get(datas.get(i))==null){
                if(prices == 0){
                    price.add(i,0);
                }else{
                    price.add(i,prices);
                }
            }else{
                price.add(i, Integer.parseInt(rest.get(datas.get(i)).toString()));
                prices = Integer.parseInt(rest.get(datas.get(i)).toString());
            }
        }
        res.clear();
        res.put("datas",datas);
        res.put("price",price);
        all.add(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(all)).getAsJsonArray();
        data.addProperty("total",all.size());
        data.add("items",jsonArray);
        rtn.setData(data);
        return  Func.functionRtnToJsonObject.apply(rtn);
    }


}
