package aq.service.order.Impl;

import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.*;
import aq.dao.order.OrderDao;
import aq.dao.user.UserDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.order.OrderService;
import aq.service.system.Func;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by ywb on 2017-02-23.
 */
@Service("serviceOrder")
@DyncDataSource
public class OrderServiceImpl extends BaseServiceImpl  implements OrderService {

    @Resource
    private OrderDao orderDao;

    @Resource
    private UserDao userDao;


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryorderList(JsonObject jsonObject) {
        jsonObject.addProperty("service","order");
        return query(jsonObject,(map)->{
            return orderDao.selectorderList(map);
        });
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
            rest.put("payer",userinfo.get(0).get("id"));
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
                restdetail.put("createTime",new Date());
                restdetail.put("lastCreateTime",new Date());
                orderDao.insertOrderDetail(restdetail);
            }
        }
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }


}
