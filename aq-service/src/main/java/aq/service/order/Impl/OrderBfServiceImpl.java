package aq.service.order.Impl;

import aq.common.access.AbsAccessUser;
import aq.common.access.Factory;
import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.GsonHelper;
import aq.common.util.NumUtil;
import aq.common.util.StringUtil;
import aq.dao.invoice.InvoiceDao;
import aq.dao.order.OrderDao;
import aq.dao.resource.ResourceDao;
import aq.dao.shop.ShopDao;
import aq.dao.user.UserDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.order.OrderBfService;
import aq.service.system.Func;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ywb on 2017-02-23.
 */
@Service("serviceBfOrder")
@DyncDataSource
public class OrderBfServiceImpl extends BaseServiceImpl  implements OrderBfService {

    @Resource
    private OrderDao orderDao;

    @Resource
    private ResourceDao resourceDao;

    @Resource
    private ShopDao shopDao;

    @Resource
    private UserDao userDao;

    @Resource
    private InvoiceDao invoiceDao;

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryorderList(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("order");
        jsonObject.addProperty("service","order");
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
        }else {
            Map<String,Object> res = new HashMap<>();
            Map<String,Object> ress = new HashMap<>();
            ress.clear();
            ress.put("userId",user.getUserId());
            List<Map<String, Object>> shops = shopDao.selectShop(ress);
            if(shops.size() < 1) {
                rtn.setCode(10000);
                rtn.setMessage("无店铺！");
            }else {
                jsonObject.addProperty("shopId",shops.get(0).get("id").toString());
                return query(jsonObject,(map)->{
                    List<Map<String, Object>> orders = orderDao.selectorderList(map);
                    for (Map obj : orders) {
                        String flag = "";
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
                                Double sum = 0.0;
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
                                    if(StringUtil.isEmpty(flag)) {
                                        flag = "02";
                                    }
                                    obj1.put("guJia", - sum - banPrice);
                                }
                            }
                            if ("01".equals(obj1.get("checkStatus"))) {
                                flag = "01";
                            }
                            obj1.put("parameterStr", str);
                        }
                        obj.put("checkStatus", flag);
                        obj.put("goods",orderPs);
                        List address = GsonHelper.getInstance().fromJson(obj.get("address").toString(), List.class);
                        if(address != null){
                            obj.put("address",address.get(0));
                        }
                        res.clear();
                        res.put("user_id",obj.get("buyerId"));
                        List<Map<String, Object>> banks = userDao.selectUserBank(res);
                        if(banks.size()>0){
                            obj.put("bank",banks.get(0));
                        }
                        res.clear();
                        res.put("orderId", obj.get("id"));
                        List<Map<String, Object>> invoice = invoiceDao.selectInvoice(res);
                        if(invoice.size()>0){
                            obj.put("invoice",invoice.get(0));
                        }
                    }
                    return orders;
                });
            }
        }
        return  Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject updateOrder(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("order");
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
        }else {
            Map<String,Object> res = new HashMap<>();
            res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
            res.put("lastCreateUser",user.getUserId());
            res.put("lastCreateTime",new Date());
            orderDao.updateOrder(res);
            rtn.setCode(200);
            rtn.setMessage("success");
        }
        return  Func.functionRtnToJsonObject.apply(rtn);
    }


    @Override
    public JsonObject insertLogistics(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("order");
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
        }else {
            Map<String,Object> res = new HashMap<>();
            res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
            orderDao.insertLogistics(res);
            rtn.setCode(200);
            rtn.setMessage("success");
        }
        return  Func.functionRtnToJsonObject.apply(rtn);
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryOrderReturnList(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("order");
        jsonObject.addProperty("service","order");
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
        }else {
            Map<String,Object> res = new HashMap<>();
            Map<String,Object> ress = new HashMap<>();
            ress.clear();
            ress.put("userId",user.getUserId());
            List<Map<String, Object>> shops = shopDao.selectShop(ress);
            if(shops.size() < 1) {
                rtn.setCode(10000);
                rtn.setMessage("无店铺！");
            }else {
                jsonObject.addProperty("shopId",shops.get(0).get("id").toString());
                return query(jsonObject,(map)->{
                    List<Map<String, Object>> orders = orderDao.selectorderReturn(map);
                    return orders;
                });
            }
        }
        return  Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject updataOrderReturn(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("order");
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
        }else {
            Map<String,Object> res = new HashMap<>();
            res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
            orderDao.updataOrderReturn(res);
            rtn.setCode(200);
            rtn.setMessage("success");
        }
        return  Func.functionRtnToJsonObject.apply(rtn);
    }
}
