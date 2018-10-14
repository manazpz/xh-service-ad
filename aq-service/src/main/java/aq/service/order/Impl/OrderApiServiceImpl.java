package aq.service.order.Impl;

import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.*;
import aq.dao.order.OrderDao;
import aq.dao.resource.ResourceDao;
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
    private ResourceDao resourceDao;


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryorderList(JsonObject jsonObject) {
        Rtn rtn = new Rtn("order");
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> ress = new HashMap<>();
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
                    List<Map> parameter = GsonHelper.getInstance().fromJson(obj1.get("parameter").toString(), List.class);
                    for (Map obj2 : parameter) {
                        Double sum = 0.0;
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
                        if ("02".equals(obj1.get("goodsModel"))) {
                            obj1.put("guJia", sum + banPrice);
                        }
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
            }
            newMap.put("item", newOrder);
            newMap.put("sum", newSum);
            oldMap.put("item", oldOrder);
            oldMap.put("sum", oldSum);
            obj.put("newOrder", newMap);
            obj.put("oldOrder", oldMap);
            obj.put("sum", newSum - oldSum);
            if (goodsName != "") {
                obj.put("goodsName", goodsName);
            }
            if (orderPs.size() > 0) {
                req.add(obj);
            }
        }
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
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> rest = new HashMap<>();
        Map<String,Object> restdetail = new HashMap<>();
        List<Map<String,Object>> listold = new ArrayList();
        List<Map<String,Object>> listnew = new ArrayList();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        rest.put("openId",res.get("openId"));
        List<Map<String, Object>> userinfo = userDao.selectUserInfos(rest);
        rest.put("id", UUIDUtil.getUUID());
        if(res.get("orderId") == null){
            rest.put("number", UUIDUtil.getRandomOrderId());
        }else{
            rest.put("number", res.get("orderId"));
        }
        if(res.get("oldGoods") != null){
            listold = GsonHelper.getInstance().fromJson(new Gson().toJson(res.get("oldGoods")), new TypeToken<List<Map<String,Object>>>(){}.getType());
            rest.put("shopid", listold.get(0).get("shopid"));
        }else{
            rest.put("shopid", res.get("shopid"));
        }
        if(res.get("oldGoods") != null && res.get("newGoods") != null){
            rest.put("type", "03");
        }else if(res.get("oldGoods") == null && res.get("newGoods") != null){
            rest.put("type", "01");
        }else if(res.get("oldGoods") != null && res.get("newGoods") == null){
            rest.put("type", "02");
        }
        if(userinfo.size()>0){
            rest.put("buyer",userinfo.get(0).get("id"));
        }
        rest.put("paystatus", "01");//付款状态 :01：未付款    02：已付款  03：已取消
        rest.put("orderstatus","03");//订单状态:01：已完成    02：已取消  03：进行中   04：售后中
        rest.put("deliverystatus","02");//收/发货状态://01：已发货    02：未发货    03：已收货
        rest.put("price",res.get("price"));
        rest.put("createTime",new Date());
        rest.put("lastCreateTime",new Date());
        //插入订单抬头信息
        orderDao.insertOrder(rest);
        //插入订单明细信息
        if(res.get("newGoods") != null){
            listnew = GsonHelper.getInstance().fromJson(new Gson().toJson(res.get("newGoods")), new TypeToken<List<Map<String,Object>>>(){}.getType());
            restdetail.put("id",rest.get("id"));
            List<Map<String, Object>> maps = orderDao.selectorderDetailList(restdetail);
            for(int i=0; i< listnew.size(); i++) {
                if(maps.size()>0){
                    restdetail.put("no",(maps.size()+i)*10+10);
                }else{
                    restdetail.put("no",i*10 +10);
                }
                restdetail.put("goodsid",listnew.get(i).get("goodsId"));
                restdetail.put("parameter",listnew.get(i).get("bllParameter"));
                restdetail.put("checkstatus","01");
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
                restdetail.put("goodsid",listold.get(i).get("goodsId"));
                restdetail.put("parameter",listold.get(i).get("bllParameter"));
                restdetail.put("checkstatus","01");
                restdetail.put("createTime",new Date());
                restdetail.put("lastCreateTime",new Date());
                orderDao.insertOrderDetail(restdetail);
            }
        }
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
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
