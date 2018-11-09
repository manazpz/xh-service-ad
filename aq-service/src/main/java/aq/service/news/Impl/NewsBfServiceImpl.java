package aq.service.news.Impl;

import aq.common.access.AbsAccessUser;
import aq.common.access.Factory;
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
import aq.service.news.NewsBfService;
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
@Service("serviceBfNews")
@DyncDataSource
public class NewsBfServiceImpl extends BaseServiceImpl  implements NewsBfService {

    @Resource
    private GoodsDao goodsDao;

    @Resource
    private OrderDao orderDao;

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryCommentList(JsonObject jsonObject) {
        Rtn rtn = new Rtn("news");
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        Map<String,Object> res = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        List<Map<String, Object>> maps = goodsDao.selectGoodsComment(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(maps)).getAsJsonArray();
        data.addProperty("total",jsonArray.size());
        data.add("items",jsonArray);
        rtn.setData(data);
        return  Func.functionRtnToJsonObject.apply(rtn);
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject insertComment(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("news");
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> rest = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        rest.put("goodsId",res.get("goodsId"));
        List<Map<String, Object>> maps = orderDao.selectRate(rest);
        rest.put("no",res.get("no"));
        rest.put("status","Y");
        orderDao.updateRate(rest);
        rest.clear();
        if(maps.size()>0){
            rest.put("no",maps.size()*10 + 10);
        }else{
            rest.put("no",10);
        }
        rest.put("goodsId",res.get("goodsId"));
        rest.put("types",res.get("types"));
        rest.put("ref_no",res.get("no"));
        rest.put("revierer",user.getUserId());
        rest.put("content",res.get("content"));
        rest.put("orderId",res.get("orderId"));
        rest.put("createTime",new Date());
        orderDao.insertRate(rest);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

}
