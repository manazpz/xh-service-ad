package aq.service.order.Impl;

import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.*;
import aq.dao.goods.GoodsDao;
import aq.dao.goods.SpecDao;
import aq.dao.news.NewsDao;
import aq.dao.order.OrderDao;
import aq.dao.resource.ResourceDao;
import aq.dao.stock.StockDao;
import aq.dao.user.UserDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.order.OrderApiService;
import aq.service.system.Func;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by ywb on 2017-02-23.
 */
@Service("serviceApiOrder")
@DyncDataSource
public class OrderApiServiceImpl extends BaseServiceImpl  implements OrderApiService {

    @Resource
    private OrderDao orderDao;

    @Resource
    private UserDao userDao;

    @Resource
    private NewsDao newsDao;

    @Resource
    private GoodsDao goodsDao;

    @Resource
    private StockDao stockDao;

    @Resource
    private ResourceDao resourceDao;

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryorderList(JsonObject jsonObject) {
        Rtn rtn = new Rtn("order");
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> ress = new HashMap<>();
        String falg = "";
        List<Map<String, Object>> req = new ArrayList();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        List<Map<String, Object>> orders = orderDao.selectorderList(res);
        for (Map obj : orders) {
            String goodsName = "";
            ArrayList<Map> newOrder = new ArrayList();
            ArrayList<Map> oldOrder = new ArrayList();
            res.put("id", obj.get("id"));
            List<Map<String, Object>> orderPs = orderDao.selectorderDetailList(res);
            for (Map obj1 : orderPs) {
                ress.clear();
                ress.put("refId", obj1.get("goodsId"));
                List imgs = resourceDao.selectResource(ress);
                obj1.put("imgs", imgs);
                String str = "";
                Double banPrice = Double.parseDouble(obj1.get("banPrice").toString());
                if (!StringUtil.isEmpty(obj1.get("parameter"))) {
                    Double sum = 0.0;
                    List<Map> parameter = GsonHelper.getInstance().fromJson(obj1.get("parameter").toString(), List.class);
                    for (Map obj2 : parameter) {
                        List<Map> spec = (List<Map>) obj2.get("spec");
                        for (Map obj3 : spec) {
                            str += obj3.get("spec_value_name") + " ";
                            if ("02".equals(obj1.get("goodsModel"))) {
                                Double correntPrice = Double.parseDouble(obj3.get("correntPrice").toString());
                                Double minPrice = Double.parseDouble(obj3.get("minPrice").toString());
                                Double cappedPrice = Double.parseDouble(obj3.get("cappedPrice").toString());
                                Double price = banPrice + correntPrice;
                                sum = sum + NumUtil.compareDouble(price, minPrice, cappedPrice) - banPrice;
                            }
                        }
                        if ("01".equals(obj1.get("goodsModel"))) {
                            obj1.put("guJia", obj2.get("price"));
                        }
                    }
                    if ("02".equals(obj1.get("goodsModel"))) {
                        obj1.put("guJia", 0- sum - banPrice);
                    }
                }
                obj1.put("parameterStr", str);
                if ("01".equals(obj1.get("goodsModel"))) {
                    newOrder.add(obj1);
                }
                if ("02".equals(obj1.get("goodsModel"))) {
                    oldOrder.add(obj1);
                }
                goodsName += obj1.get("goodsName") + ";";
            }
            Double newSum = 0.0;
            Double oldSum = 0.0;
            HashMap newMap = new HashMap();
            HashMap oldMap = new HashMap();
            for (Map m : newOrder) {
                newSum += Double.parseDouble(m.get("guJia").toString());
            }
            for (Map m : oldOrder) {
                oldSum += Double.parseDouble(m.get("guJia").toString());
//                if("02".equals(m.get("checkStatus"))){
//                    falg = true;
//                }else{
//                    falg = false;
//                }
                falg = m.get("checkStatus").toString();//01 ： 未检验   02：已检验   03：同意检测结果
            }
            newMap.put("item", newOrder);
            newMap.put("sum", newSum);
            oldMap.put("item", oldOrder);
            oldMap.put("sum", oldSum);
            obj.put("newOrder", newMap);
            obj.put("oldOrder", oldMap);
            obj.put("checkStatus", falg);
            List address = GsonHelper.getInstance().fromJson(obj.get("address").toString(), List.class);
            if(address != null){
                obj.put("address",address.get(0));
            }
            if (goodsName != "") {
                obj.put("goodsName", goodsName);
            }
            if (orderPs.size() > 0) {
                req.add(obj);
            }
        }
//        Collections.sort(req, new Comparator<Map<String, Object>>() {
//            @Override
//            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
//
//                String type1 = o1.get("type").toString() ;//name1是从你list里面拿出来的一个
//                String type2 = o2.get("type").toString() ; //name1是从你list里面拿出来的第二个name
//
//                String address1 = ((Map)o1.get("address")).get("areaString").toString() ;//name1是从你list里面拿出来的一个
//                String addresse2 = ((Map)o2.get("address")).get("areaString").toString() ; //name1是从你list里面拿出来的第二个name
//                int i1 = type1.compareTo(type2);
//                int i2 = address1.compareTo(addresse2);
//
//            }
//        });
        rtn.setCode(200);
        rtn.setMessage("success");
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(req)).getAsJsonArray();
        data.addProperty("total",jsonArray.size());
        data.add("items",jsonArray);
        rtn.setData(data);
        return  Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject instertorderList(JsonObject jsonObject) {
        Rtn rtn = new Rtn("order");
        DecimalFormat df = new DecimalFormat("#.00");
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> rest = new HashMap<>();
        Map<String,Object> restdetail = new HashMap<>();
        List<Map<String,Object>> listold = new ArrayList();
        List<Map<String,Object>> listnew = new ArrayList();
        List<Map<String, Object>> req = new ArrayList();
        List bllId = new ArrayList();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        rest.put("openid",res.get("openId"));
        List<Map<String, Object>> userinfo = userDao.selectUserInfos(rest);
        listnew = GsonHelper.getInstance().fromJson(new Gson().toJson(res.get("newGoods")), new TypeToken<List<Map<String,Object>>>(){}.getType());
        listold = GsonHelper.getInstance().fromJson(new Gson().toJson(res.get("oldGoods")), new TypeToken<List<Map<String,Object>>>(){}.getType());
        rest.put("id", UUIDUtil.getUUID());
        if(res.get("orderId") == null){
            rest.put("number", UUIDUtil.getRandomOrderId());
        }else{
            rest.put("number", res.get("orderId"));
        }
        if(listold.size()>0){
            rest.put("shopid", listold.get(0).get("shopid"));
        }else{
            rest.put("shopid", res.get("shopid"));
        }
        if(listold.size()>0  && listnew.size()>0){
            rest.put("type", "03"); //type : 01: 购买新机   02： 出售旧机   03：新旧置换
            rest.put("paystatus", "01");//付款状态 :01：未付款    02：已付款  03：已取消
        }else if(listold.size() == 0 && listnew.size() >0){
            rest.put("type", "01");
            rest.put("paystatus", "01");//付款状态 :01：未付款    02：已付款  03：已取消
        }else if(listold.size()>0 && listnew.size() == 0){
            rest.put("type", "02");
        }
        if(userinfo.size()>0){
            rest.put("buyer",userinfo.get(0).get("id"));
        }
        rest.put("orderstatus","03");//订单状态:01：已完成    02：已取消  03：进行中   04：售后中
        rest.put("deliverystatus","02");//收/发货状态://01：已发货    02：未发货    03：已发货用户未收货    04：已收货
        rest.put("price",df.format(Double.parseDouble(res.get("price").toString())));
        rest.put("recovery",res.get("recovery"));
        rest.put("address",res.get("address").toString());
        rest.put("createTime",new Date());
        rest.put("lastCreateTime",new Date());
        //插入订单抬头信息
        orderDao.insertOrder(rest);
        //插入订单明细信息
        if(res.get("newGoods") != null){
            restdetail.put("id",rest.get("id"));
            List<Map<String, Object>> maps = orderDao.selectorderDetailList(restdetail);
            for(int i=0; i< listnew.size(); i++) {
                if(maps.size()>0){
                    restdetail.put("no",(maps.size()+i)*10+10);
                }else{
                    restdetail.put("no",i*10 +10);
                }
                bllId.add(listnew.get(i).get("bllId"));
                restdetail.put("goodsid",listnew.get(i).get("goodsId"));
                restdetail.put("parameter",listnew.get(i).get("bllParameter"));
                restdetail.put("createTime",new Date());
                restdetail.put("lastCreateTime",new Date());
                orderDao.insertOrderDetail(restdetail);
            }
        }
        if(res.get("oldGoods") != null){
            listold = GsonHelper.getInstance().fromJson(new Gson().toJson(res.get("oldGoods")), new TypeToken<List<Map<String,Object>>>(){}.getType());
            restdetail.put("id",rest.get("id"));
            List<Map<String, Object>> maps = orderDao.selectorderDetailList(restdetail);
            for(int i=0; i< listold.size(); i++) {
                if(maps.size()>0){
                    restdetail.put("no",(maps.size()+i)*10+10);
                }else{
                    restdetail.put("no",i*10 +10);
                }
                bllId.add(listold.get(i).get("bllId"));
                restdetail.put("goodsid",listold.get(i).get("goodsId"));
                restdetail.put("parameter",listold.get(i).get("bllParameter"));
                restdetail.put("checkstatus","01");
                restdetail.put("createTime",new Date());
                restdetail.put("lastCreateTime",new Date());
                orderDao.insertOrderDetail(restdetail);
            }
        }
        res.clear();
        res.put("orderId",rest.get("id"));
        res.put("id",UUIDUtil.getUUID());
        if(userinfo.size()>0){
            res.put("buyer",userinfo.get(0).get("id"));
        }
        res.put("type","01");//01: 订单  02 ： 物流   03： 优惠券   04 ：  活动
        res.put("status","01");//01: 未付款 02: 已付款  03： 取消订单 04： 已收款  05:已收货  06：确认出售
        res.put("createTime",new Date());
        newsDao.insertOrderLog(res);
        res.clear();
        res.put("id",bllId);
        goodsDao.deleteReplacementCar(res);
        res.clear();
        res.put("orderId",rest.get("id"));
        req.add(res);
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(req)).getAsJsonArray();
        data.add("items",jsonArray);
        rtn.setData(data);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject updateOrder(JsonObject jsonObject) {
        Rtn rtn = new Rtn("order");
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> rest = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        orderDao.updateOrder(res);
        if("03".equals(res.get("paystatus"))){
            List<Map> newOrder = (List) res.get("newOrder");
            newOrder.forEach(obj->{
                Map<String,Object> ress = new HashMap<>();
                ress.put("goodsId",obj.get("goodsId"));
                List<Map<String, Object>> maps = stockDao.selectStock(ress);
                ress.clear();
                ress.put("currentStock",Integer.parseInt(maps.get(0).get("currentStock").toString())+1);
                ress.put("lastCreateTime",new Date());
                ress.put("goodsId",obj.get("goodsId"));
                stockDao.updateStock(ress);
            });
        }
        rest.put("openid",res.get("openId"));
        List<Map<String, Object>> maps = userDao.selectUserInfos(rest);
        if(maps.size()> 0 ){
            rest.put("buyer",maps.get(0).get("id"));
        }
        rest.put("id",UUIDUtil.getUUID());
        rest.put("orderId",res.get("id"));
        rest.put("status",res.get("status"));
        rest.put("type","01");
        rest.put("createTime",new Date());
        newsDao.insertOrderLog(rest);
        rtn.setCode(200);
        rtn.setMessage("success");
        return  Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject updateOrderDetail(JsonObject jsonObject) {
        Rtn rtn = new Rtn("order");
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> rest = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        orderDao.updateOrderDetail(res);
        rest.put("openid",res.get("openId"));
        List<Map<String, Object>> maps = userDao.selectUserInfos(rest);
        if(maps.size()> 0 ){
            rest.put("buyer",maps.get(0).get("id"));
        }
        rest.put("id",UUIDUtil.getUUID());
        rest.put("orderId",res.get("id"));
        rest.put("status",res.get("status"));
        rest.put("type","01");
        rest.put("createTime",new Date());
        newsDao.insertOrderLog(rest);
        rtn.setCode(200);
        rtn.setMessage("success");
        return  Func.functionRtnToJsonObject.apply(rtn);
    }



    @Override
    public JsonObject insertRate(JsonObject jsonObject) {
        Rtn rtn = new Rtn("order");
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> rest = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        if(!"".equals(res.get("openId"))){
            rest.put("openid",res.get("openId"));
            List<Map<String, Object>> userinfo = userDao.selectUserInfos(rest);
            if(userinfo.size()>0){
                res.put("revierer",userinfo.get(0).get("id"));
                res.put("star",(new Double(Double.parseDouble(res.get("star").toString()))).intValue());
                List<Map<String, Object>> maps = orderDao.selectRate(res);
                if(maps.size()>0){
                    res.put("no",maps.size()*10 + 10);
                }else{
                    res.put("no",10);
                }
                res.put("createTime",new Date());
                res.put("status","N");
                orderDao.insertRate(res);
                rtn.setCode(200);
                rtn.setMessage("success");
            }else{
                rtn.setCode(500);
                rtn.setMessage("error");
            }
        }
        return  Func.functionRtnToJsonObject.apply(rtn);
    }


    @Override
    public JsonObject insertBlance(JsonObject jsonObject) {
        Rtn rtn = new Rtn("order");
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> rest = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        orderDao.insertBlance(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return  Func.functionRtnToJsonObject.apply(rtn);
    }


    @Override
    public JsonObject queryLogistical(JsonObject jsonObject) {
        Rtn rtn = new Rtn("order");
        HashMap req = new HashMap();
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        req.clear();
        req.put("id",jsonObject.get("id").getAsString());
        List list = orderDao.selectLogistical(req);
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(list)).getAsJsonArray();
        data.addProperty("total",list.size());
        data.add("items",jsonArray);
        rtn.setCode(200);
        rtn.setMessage("success");
        rtn.setData(data);
        return  Func.functionRtnToJsonObject.apply(rtn);
    }


    @Override
    public JsonObject insertReturn(JsonObject jsonObject) {
        Rtn rtn = new Rtn("order");
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> rest = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        if(!"".equals(res.get("openId"))){
            rest.put("openid",res.get("openId"));
            List<Map<String, Object>> userinfo = userDao.selectUserInfos(rest);
            if(userinfo.size()>0){
                rest.clear();
                rest.put("id",res.get("id"));
                List<Map<String, Object>> maps = orderDao.selectorderList(rest);
                if(maps.size()>0){
                    rest.put("price", maps.get(0).get("price"));
                    rest.put("shopId", maps.get(0).get("shopId"));
                }
                List<Map<String, Object>> maps1 = orderDao.selectorderReturn(rest);
                if(maps1.size()>0){
                    rest.put("no", 10*maps1.size()+10);
                }else{
                    rest.put("no", 10);
                }
                rest.put("number", "R"+UUIDUtil.getRandomOrderId());
                rest.put("reason", res.get("reason"));
                rest.put("createTime", new Date());
                rest.put("createUserId",userinfo.get(0).get("id"));
                orderDao.insertReturn(rest);
                rtn.setCode(200);
                rtn.setMessage("success");
            }
        }
        return  Func.functionRtnToJsonObject.apply(rtn);
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public List<Map<String, Object>> selectOrder(Map<String, Object> map) {
        return  orderDao.selectorderList(map);
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public int updateOrder(Map<String, Object> map) {
        return  orderDao.updateOrder(map);
    }

}
