package aq.service.statistics.Impl;

import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.DateTime;
import aq.common.util.GsonHelper;
import aq.dao.statement.StatementDao;
import aq.dao.statistics.StatisticsDao;
import aq.service.base.Impl.BaseServiceImpl;
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
@Service("serviceStatistics")
@DyncDataSource
public class StatisticsServiceImpl extends BaseServiceImpl implements StatisticsService {

    @Resource
    private StatisticsDao statisticsDao;

    @Override
    public JsonObject statisticsInfo(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Statistics");
        Map<String,Object> res = new HashMap<>();
        Map customerMap = statisticsDao.countCustomer();
        Map goodsMap = statisticsDao.countGoods();
        Map endOrderMap = statisticsDao.countEndOrder();
        Map messageMap = new HashMap();
        res.clear();
        res.put("customer",customerMap);
        res.put("goods",goodsMap);
        res.put("endOrder",endOrderMap);
        res.put("message",messageMap);
        rtn.setCode(200);
        rtn.setMessage("success");
        rtn.setData(GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(res)).getAsJsonObject());
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject statisticsWeekInfo(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Statistics");
        Map<String,Object> res = new HashMap<>();
        Map map1 = new HashMap();
        Map map2 = new HashMap();
        Map map3 = new HashMap();
        Map map4 = new HashMap();
        List<String> messages = new ArrayList<>();
        Map newestCustomerMap = statisticsDao.newestCustomer();
        if(newestCustomerMap != null) {
            List<String> countWeekCustomers = new ArrayList<>();
            List<String> weeks = new ArrayList<>();
            res.clear();
            res.put("startTime",DateTime.dateFormat(DateTime.addTime(DateTime.strToDateLong(newestCustomerMap.get("endTime").toString()), ChronoUnit.DAYS, -6),"yyyy-MM-dd"));
            res.put("endTime",DateTime.dateFormat(DateTime.addTime(DateTime.strToDateLong(newestCustomerMap.get("endTime").toString()), ChronoUnit.DAYS, 1),"yyyy-MM-dd"));
            List<Map<String, Object>> lists = statisticsDao.countWeekCustomer(res);
            for (int i=6;i>-1;i--) {
                Boolean flag = true;
                String sj = DateTime.dateFormat(DateTime.addTime(DateTime.strToDateLong(newestCustomerMap.get("endTime").toString()), ChronoUnit.DAYS, -i), "yyyy-MM-dd");
                for (Map obj : lists) {
                    if(sj.equals(obj.get("time"))) {
                        flag = false;
                        countWeekCustomers.add(obj.get("count").toString());
                        break;
                    }
                }
                if(flag) {
                    countWeekCustomers.add("0");
                }
                String week = DateTime.dateFormat(DateTime.addTime(DateTime.strToDateLong(newestCustomerMap.get("endTime").toString()), ChronoUnit.DAYS, -i),"EEEE");
                weeks.add(week);
            }
            map1.put("expectedData",countWeekCustomers);
            map1.put("weeks",weeks);
        }
        Map newestGoodsMap = statisticsDao.newestGoods();
        if(newestGoodsMap != null) {
            List<String> countWeekGoodss = new ArrayList<>();
            List<String> weeks = new ArrayList<>();
            res.clear();
            res.put("startTime",DateTime.dateFormat(DateTime.addTime(DateTime.strToDateLong(newestGoodsMap.get("endTime").toString()), ChronoUnit.DAYS, -6),"yyyy-MM-dd"));
            res.put("endTime",DateTime.dateFormat(DateTime.addTime(DateTime.strToDateLong(newestGoodsMap.get("endTime").toString()), ChronoUnit.DAYS, 1),"yyyy-MM-dd"));
            List<Map<String, Object>> lists = statisticsDao.countWeekGoods(res);
            for (int i=6;i>-1;i--) {
                Boolean flag = true;
                String sj = DateTime.dateFormat(DateTime.addTime(DateTime.strToDateLong(newestGoodsMap.get("endTime").toString()), ChronoUnit.DAYS, -i), "yyyy-MM-dd");
                for (Map obj : lists) {
                    if(sj.equals(obj.get("time"))) {
                        flag = false;
                        countWeekGoodss.add(obj.get("count").toString());
                        break;
                    }
                }
                if(flag) {
                    countWeekGoodss.add("0");
                }
                String week = DateTime.dateFormat(DateTime.addTime(DateTime.strToDateLong(newestGoodsMap.get("endTime").toString()), ChronoUnit.DAYS, -i),"EEEE");
                weeks.add(week);
            }
            map2.put("expectedData",countWeekGoodss);
            map2.put("weeks",weeks);
        }
        Map newestEndOrderMap = statisticsDao.newestEndOrder();
        if(newestEndOrderMap != null) {
            List<String> countWeekEndOrders = new ArrayList<>();
            List<String> weeks = new ArrayList<>();
            res.clear();
            res.put("startTime",DateTime.dateFormat(DateTime.addTime(DateTime.strToDateLong(newestEndOrderMap.get("endTime").toString()), ChronoUnit.DAYS, -6),"yyyy-MM-dd"));
            res.put("endTime",DateTime.dateFormat(DateTime.addTime(DateTime.strToDateLong(newestEndOrderMap.get("endTime").toString()), ChronoUnit.DAYS, 1),"yyyy-MM-dd"));
            List<Map<String, Object>> lists = statisticsDao.countWeekEndOrder(res);
            for (int i=6;i>-1;i--) {
                Boolean flag = true;
                String sj = DateTime.dateFormat(DateTime.addTime(DateTime.strToDateLong(newestEndOrderMap.get("endTime").toString()), ChronoUnit.DAYS, -i), "yyyy-MM-dd");
                for (Map obj : lists) {
                    if(sj.equals(obj.get("time"))) {
                        flag = false;
                        countWeekEndOrders.add(obj.get("sum").toString());
                        break;
                    }
                }
                if(flag) {
                    countWeekEndOrders.add("0");
                }
                String week = DateTime.dateFormat(DateTime.addTime(DateTime.strToDateLong(newestEndOrderMap.get("endTime").toString()), ChronoUnit.DAYS, -i),"EEEE");
                weeks.add(week);
            }
            map3.put("expectedData",countWeekEndOrders);
            map3.put("weeks",weeks);
        }

        map4.put("expectedData",new ArrayList<>());
        map4.put("weeks",new ArrayList<>());
        res.clear();
        res.put("customer",map1);
        res.put("goods",map2);
        res.put("endOrder",map3);
        res.put("message",map4);
        rtn.setCode(200);
        rtn.setMessage("success");
        rtn.setData(GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(res)).getAsJsonObject());
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject statisticsOrderType(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Statistics");
        Map<String,Object> res = new HashMap<>();
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        List<Map<String, Object>> countOrderTypes = statisticsDao.countOrderType(res);
        for (Map obj : countOrderTypes) {
            switch (obj.get("type").toString()) {
                case "01":
                    obj.put("name", "新机");
                    break;
                case "02":
                    obj.put("name", "新机换购");
                    break;
                case "03":
                    obj.put("name", "旧机");
                    break;
                default:
                    obj.put("name", "其它");
                    break;
            }
        }
        rtn.setCode(200);
        rtn.setMessage("success");
        jsonArray = GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(countOrderTypes)).getAsJsonArray();
        data.add("items",jsonArray);
        rtn.setData(data);
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject statisticsendOrderMoon(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Statistics");
        Map<String,Object> res = new HashMap<>();
        List<String> moons = new ArrayList<>();
        List<String> datas = new ArrayList<>();
        Map newestEndOrderMap = statisticsDao.newestEndOrder();
        if(newestEndOrderMap != null) {
            res.clear();
            res.put("startTime",DateTime.getFirstDayOfMonth(DateTime.addTime(DateTime.strToDateLong(newestEndOrderMap.get("endTime").toString()), ChronoUnit.MONTHS, -5)));
            res.put("endTime",DateTime.getLastDayOfMonth(DateTime.addTime(DateTime.strToDateLong(newestEndOrderMap.get("endTime").toString()), ChronoUnit.MONTHS, 0)));
            List<Map<String, Object>> lists = statisticsDao.countEndOrderMoon(res);
            for (int i=5;i>-1;i--) {
                Boolean flag = true;
                String sj = DateTime.dateFormat(DateTime.addTime(DateTime.strToDateLong(newestEndOrderMap.get("endTime").toString()), ChronoUnit.MONTHS, -i), "yyyy-MM");
                for (Map obj : lists) {
                    if(sj.equals(obj.get("moon"))) {
                        flag = false;
                        datas.add(obj.get("sum").toString());
                        break;
                    }
                }
                if(flag) {
                    datas.add("0");
                }
                moons.add(sj);
            }
        }
        res.clear();
        res.put("data",datas);
        res.put("moon",moons);
        rtn.setCode(200);
        rtn.setMessage("success");
        rtn.setData(GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(res)).getAsJsonObject());
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject statisticsendOrderYear(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Statistics");
        Map<String,Object> res = new HashMap<>();
        List<String> years = new ArrayList<>();
        List<String> datas = new ArrayList<>();
        Map newestEndOrderMap = statisticsDao.newestEndOrder();
        if(newestEndOrderMap != null) {
            res.clear();
            res.put("startTime",DateTime.getFirstDayOfYear(DateTime.addTime(DateTime.strToDateLong(newestEndOrderMap.get("endTime").toString()), ChronoUnit.YEARS, -2)));
            res.put("endTime",DateTime.getLastDayOfYear(DateTime.addTime(DateTime.strToDateLong(newestEndOrderMap.get("endTime").toString()), ChronoUnit.YEARS, 0)));
            List<Map<String, Object>> lists = statisticsDao.countEndOrderYear(res);
            for (int i=2;i>-1;i--) {
                Boolean flag = true;
                String sj = DateTime.dateFormat(DateTime.addTime(DateTime.strToDateLong(newestEndOrderMap.get("endTime").toString()), ChronoUnit.YEARS, -i), "yyyy");
                for (Map obj : lists) {
                    if(sj.equals(obj.get("moon"))) {
                        flag = false;
                        datas.add(obj.get("sum").toString());
                        break;
                    }
                }
                if(flag) {
                    datas.add("0");
                }
                years.add(sj);
            }
        }
        res.clear();
        res.put("data",datas);
        res.put("year",years);
        rtn.setCode(200);
        rtn.setMessage("success");
        rtn.setData(GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(res)).getAsJsonObject());
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject queryCustomer(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Statistics");
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        Map<String,Object> res = new HashMap<>();
        List<Map<String, Object>> lists = statisticsDao.selectCustomer(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        jsonArray = GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(lists)).getAsJsonArray();
        data.add("items",jsonArray);
        rtn.setData(data);
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject queryLableGoods(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Statistics");
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        Map<String,Object> res = new HashMap<>();
        List<Map<String, Object>> lists = statisticsDao.selectLableGoods(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        jsonArray = GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(lists)).getAsJsonArray();
        data.add("items",jsonArray);
        rtn.setData(data);
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject queryZrSaleGoods(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Statistics");
        Map<String,Object> res = new HashMap<>();
        List goodsNameMap = new ArrayList();
        List saleMap = new ArrayList<>();
        String time = DateTime.dateFormat(DateTime.addTime(new Date(), ChronoUnit.DAYS, -1),"yyyy-MM-dd");
        res.put("time",time);
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
        return Func.functionRtnToJsonObject.apply(rtn);
    }
}
