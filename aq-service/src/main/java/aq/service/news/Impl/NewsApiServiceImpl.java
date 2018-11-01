package aq.service.news.Impl;

import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.*;
import aq.dao.goods.GoodsDao;
import aq.dao.news.NewsDao;
import aq.dao.order.OrderDao;
import aq.dao.resource.ResourceDao;
import aq.dao.user.UserDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.news.NewsApiService;
import aq.service.system.Func;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by ywb on 2017-02-23.
 */
@Service("serviceApiNews")
@DyncDataSource
public class NewsApiServiceImpl extends BaseServiceImpl  implements NewsApiService {

    @Resource
    private OrderDao orderDao;

    @Resource
    private NewsDao newsDao;

    @Resource
    private UserDao userDao;

    @Resource
    private GoodsDao goodsDao;

    @Resource
    private ResourceDao resourceDao;

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryOrderNewsList(JsonObject jsonObject) {
        Rtn rtn = new Rtn("news");
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> ress = new HashMap<>();
        List<Map<String, Object>> req = new ArrayList();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        List<Map<String, Object>> maps = newsDao.selectorderLogList(res);
        for (Map obj : maps) {
            String goodsName = "";
            if("01".equals(obj.get("type"))){
                ress.clear();
                ress.put("id",obj.get("orderId"));
                List<Map<String, Object>> orders = orderDao.selectorderList(ress);
                ArrayList<Map> newOrder = new ArrayList();
                ArrayList<Map> oldOrder = new ArrayList();
                for (Map obj1 : orders) {
                    List<Map<String, Object>> orderPs = orderDao.selectorderDetailList(ress);
                    for (Map obj2 : orderPs) {
                        ress.clear();
                        ress.put("refId", obj2.get("goodsId"));
                        List imgs = resourceDao.selectResource(ress);
                        obj2.put("imgs", imgs);
                        String str = "";
                        if (!StringUtil.isEmpty(obj2.get("parameter"))) {
                            List<Map> parameter = GsonHelper.getInstance().fromJson(obj2.get("parameter").toString(), List.class);
                            for (Map obj3 : parameter) {
                                List<Map> spec = (List<Map>) obj3.get("spec");
                                for (Map obj4 : spec) {
                                    str += obj4.get("spec_value_name") + " ";
                                }
                            }
                        }
                        obj2.put("parameterStr", str);
                        if ("01".equals(obj2.get("goodsModel"))) {
                            newOrder.add(obj2);
                        }
                        if ("02".equals(obj2.get("goodsModel"))) {
                            oldOrder.add(obj2);
                        }
                        goodsName += obj2.get("goodsName") + ";";
                    }
                }
                obj.put("number",orders.get(0).get("number"));
                obj.put("newOrder", newOrder);
                obj.put("oldOrder", oldOrder);
                if (goodsName != "") {
                    obj.put("goodsName", goodsName);
                }
            }
            if (maps.size() > 0) {
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
    public int instertorderLog(Map<String, Object> map) {
        return  newsDao.insertOrderLog(map);
    }

}
