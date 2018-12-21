package aq.service.jobs.Impl;

import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.*;
import aq.dao.goods.GoodsDao;
import aq.dao.goods.SpecDao;
import aq.dao.order.OrderDao;
import aq.dao.resource.ResourceDao;
import aq.dao.shop.ShopDao;
import aq.dao.user.UserDao;
import aq.dao.yanji.YanJiDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.jobs.JobsService;
import aq.service.system.Func;
import aq.service.yanji.YanJiApiService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by ywb on 2017-02-23.
 */
@Service("serviceJobs")
@DyncDataSource
public class JobsServiceImpl extends BaseServiceImpl  implements JobsService {

    @Resource
    private OrderDao orderDao;

    @Resource
    private ShopDao shopDao;


    @Override
    public JsonObject queryOrderCollect(JsonObject jsonObject) {
        Rtn rtn = new Rtn("jobs");
        JsonObject data = new JsonObject();
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> rest = new HashMap<>();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 7);
        Date lastday = calendar.getTime();
        res.put("lastCreateTime",lastday);
        List<Map<String, Object>> orders = orderDao.selectorderList(res);
        for (Map m : orders) {
            if("01".equals(m.get("payStatus"))&& !"03".equals(m.get("orderStatus")) ){//超时未付款
                rest.put("paystatus","03");
                rest.put("orderstatus","02");
            }
            if("04".equals(m.get("deliveryStatus"))){ //超时未收货
                rest.put("deliverystatus","05");
            }
            rest.put("id",m.get("id"));
            rest.put("lastCreateTime",new Date());
            orderDao.updateOrder(rest);
        }
        rtn.setCode(200);
        rtn.setMessage("success");
        rtn.setData(data);
        return  Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject settlement() throws Exception {
        Map<String,Object> res = new HashMap<>();
        List<Map<String, Object>> shops = shopDao.selectShop(res);
        String yearMoon = DateTime.getCurrentYearMonth();
        if(shops.size() > 0) {
            for (Map shopObj : shops) {
                double sum = 0;
                res.clear();
                res.put("moon",yearMoon);
                res.put("shopId",shopObj.get("id"));
                List<Map<String, Object>> settlements = shopDao.selectSettlement(res);
                if (settlements.size() < 1) {
                    res.put("orderStatus","01");
                    res.put("shopId",shopObj.get("id"));
                    String startTime = DateTime.getBeforeFirstMonthdate();
                    String endTime = DateTime.getBeforeLastMonthdate();
                    res.put("startTime",startTime);
                    res.put("endTime",endTime);
                    List<Map<String, Object>> orders = orderDao.selectorderList(res);
                    if(orders.size() > 0) {
                        for (Map orderObj : orders) {
                            sum += Double.parseDouble(orderObj.get("price").toString());
                        }
                        res.clear();
                        res.put("id",UUIDUtil.getUUID());
                        res.put("shopId",shopObj.get("id"));
                        res.put("estimatePrice",sum);
                        res.put("moon",yearMoon);
                        res.put("num",orders.size());
                        res.put("flag","N");
                        shopDao.insertSettlement(res);
                    }
                }
            }
        }
        return null;
    }

}
