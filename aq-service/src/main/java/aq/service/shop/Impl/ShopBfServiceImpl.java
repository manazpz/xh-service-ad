package aq.service.shop.Impl;


import aq.common.access.AbsAccessUser;
import aq.common.access.Factory;
import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.GsonHelper;
import aq.dao.shop.ShopDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.shop.ShopBfService;
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
@Service("serviceBfShop")
@DyncDataSource
public class ShopBfServiceImpl extends BaseServiceImpl  implements ShopBfService {

    @Resource
    private ShopDao shopDao;


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryShop(JsonObject jsonObject) {
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        Rtn rtn = new Rtn("shop");
        AbsAccessUser user = Factory.getContext().user();
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
        }else {
            Map<String,Object> res = new HashMap<>();
            res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
            res.put("userId", user.getUserId());
            List<Map<String, Object>> lists = shopDao.selectShop(res);
            rtn.setCode(200);
            rtn.setMessage("success");
            jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(lists)).getAsJsonArray();
            data.add("items",jsonArray);
            rtn.setData(data);
        }
        return  Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject updateShop(JsonObject jsonObject) {
        Rtn rtn = new Rtn("shop");
        Map<String,Object> res = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        res.put("lastCreateTime",new Date());
        if(((List)res.get("files")).size() > 0) {
            res.put("img",((Map)((List)res.get("files")).get(0)).get("url"));

        }
        shopDao.updateShop(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

}
