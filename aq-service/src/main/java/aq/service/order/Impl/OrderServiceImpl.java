package aq.service.order.Impl;

import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.GsonHelper;
import aq.common.util.NumUtil;
import aq.common.util.StringUtil;
import aq.dao.order.OrderDao;
import aq.dao.resource.ResourceDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.order.OrderService;
import aq.service.system.Func;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ywb on 2017-02-23.
 */
@Service("serviceOrder")
@DyncDataSource
public class OrderServiceImpl extends BaseServiceImpl  implements OrderService {

    @Resource
    private OrderDao orderDao;

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
        rtn.setCode(200);
        rtn.setMessage("success");
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(req)).getAsJsonArray();
        data.addProperty("total",jsonArray.size());
        data.add("items",jsonArray);
        rtn.setData(data);
        return  Func.functionRtnToJsonObject.apply(rtn);
    }


    @Override
    public JsonObject queryOrderDetail(JsonObject jsonObject) {
        Rtn rtn = new Rtn("order");
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        Map<String,Object> res = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        List<Map<String, Object>> orderPs = orderDao.selectorderDetailList(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(orderPs)).getAsJsonArray();
        data.addProperty("total",jsonArray.size());
        data.add("items",jsonArray);
        rtn.setData(data);
        return  Func.functionRtnToJsonObject.apply(rtn);
    }
}
