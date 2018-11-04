package aq.service.statistics.Impl;

import aq.common.access.AbsAccessUser;
import aq.common.access.Factory;
import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.DateTime;
import aq.common.util.GsonHelper;
import aq.common.util.StringUtil;
import aq.dao.shop.ShopDao;
import aq.dao.statistics.StatisticsDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.statistics.StatisticsBfService;
import aq.service.statistics.StatisticsService;
import aq.service.system.Func;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Created by ywb on 2017-02-23.
 */
@Service("serviceBfStatistics")
@DyncDataSource
public class StatisticsBfServiceImpl extends BaseServiceImpl implements StatisticsBfService {

    @Resource
    private StatisticsDao statisticsDao;

    @Resource
    private ShopDao shopDao;

    @Override
    public JsonObject statisticsOrderType(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("Statistics");
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
        }else {
            Map<String,Object> res = new HashMap<>();
            res.clear();
            res.put("userId",user.getUserId());
            List<Map<String, Object>> maps = shopDao.selectShop(res);
            if(maps.size() > 0) {
                String shopId = (String) ((Map) maps.get(0)).get("id");
                //所有
                res.clear();
                res.put("shopId",shopId);
                res.put("flag",false);
                Map m1 = statisticsDao.selectOrderType(res);
                //待付款
                res.clear();
                res.put("shopId",shopId);
                res.put("flag",false);
                res.put("type","01");
                res.put("payStatus","01");
                Map m2 = statisticsDao.selectOrderType(res);
                //待发货
                res.clear();
                res.put("shopId",shopId);
                res.put("flag",false);
                res.put("deliveryStatus","02");
                Map m3 = statisticsDao.selectOrderType(res);
                //待收货
                res.clear();
                res.put("shopId",shopId);
                res.put("flag",false);
                res.put("deliveryStatus","01");
                Map m4 = statisticsDao.selectOrderType(res);
                //待评论
                res.clear();
                res.put("shopId",shopId);
                res.put("flag",true);
                Map m5 = statisticsDao.selectOrderType(res);
                //待退款
                res.clear();
                res.put("shopId",shopId);
                res.put("flag",false);
                res.put("orderStatus","05");
                Map m6 = statisticsDao.selectOrderType(res);
                //完成
                res.clear();
                res.put("shopId",shopId);
                res.put("flag",false);
                res.put("orderStatus","01");
                Map m7 = statisticsDao.selectOrderType(res);
                //取消
                res.clear();
                res.put("shopId",shopId);
                res.put("flag",false);
                res.put("orderStatus","02");
                Map m8 = statisticsDao.selectOrderType(res);
                res.clear();
                res.put("m1",m1);
                res.put("m2",m2);
                res.put("m3",m3);
                res.put("m4",m4);
                res.put("m5",m5);
                res.put("m6",m6);
                res.put("m7",m7);
                res.put("m8",m8);
                rtn.setCode(200);
                rtn.setMessage("success");
                rtn.setData(GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(res)).getAsJsonObject());
            }else {
                rtn.setCode(404);
                rtn.setMessage("用户下无店铺！");
            }
        }
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject queryZrSaleGoods(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("Statistics");
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
        }else {
            Map<String,Object> res = new HashMap<>();
            res.clear();
            res.put("userId",user.getUserId());
            List<Map<String, Object>> maps = shopDao.selectShop(res);
            if(maps.size() > 0) {
                String shopId = (String) ((Map) maps.get(0)).get("id");
                res.clear();
                List goodsNameMap = new ArrayList();
                List saleMap = new ArrayList<>();
                String time = DateTime.dateFormat(DateTime.addTime(new Date(), ChronoUnit.DAYS, -1),"yyyy-MM-dd");
                res.put("time",time);
                res.put("shopId",shopId);
                List<Map<String, Object>> zrLists = statisticsDao.selectZrSaleGoods(res);
                for (Map obj : zrLists) {
                    goodsNameMap.add(obj.get("goodsName"));
                    saleMap.add(obj.get("goodsCount"));
                }
                res.clear();
                res.put("goodsName",goodsNameMap);
                res.put("goodsCount",saleMap);
                rtn.setCode(200);
                rtn.setMessage("success");
                rtn.setData(GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(res)).getAsJsonObject());
            }else {
                rtn.setCode(404);
                rtn.setMessage("用户下无店铺！");
            }
        }
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject queryZrStatus(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("Statistics");
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
        }else {
            Map<String,Object> res = new HashMap<>();
            res.clear();
            res.put("userId",user.getUserId());
            List<Map<String, Object>> maps = shopDao.selectShop(res);
            if(maps.size() > 0) {
                String shopId = (String) ((Map) maps.get(0)).get("id");
                String time = DateTime.dateFormat(DateTime.addTime(new Date(), ChronoUnit.DAYS, -1),"yyyy-MM-dd");
                res.clear();
                res.put("time",time);
                res.put("shopId",shopId);
                Map m1 = statisticsDao.selectZrIncome(res);
                if(StringUtil.isEmpty(m1.get("price"))) {
                    m1.put("price",0);
                }
                Map m2 = statisticsDao.selectZrOrder(res);
                res.clear();
                res.put("m1",m1);
                res.put("m2",m2);
                Map m3 = new HashMap<>();
                m3.put("count",0);
                Map m4 = new HashMap<>();
                m4.put("count",0);
                res.put("m3",m3);
                res.put("m4",m4);
                rtn.setCode(200);
                rtn.setMessage("success");
                rtn.setData(GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(res)).getAsJsonObject());
            }else {
                rtn.setCode(404);
                rtn.setMessage("用户下无店铺！");
            }
        }
        return Func.functionRtnToJsonObject.apply(rtn);
    }
}
