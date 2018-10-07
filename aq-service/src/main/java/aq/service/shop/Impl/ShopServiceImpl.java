package aq.service.shop.Impl;

import aq.common.access.AbsAccessUser;
import aq.common.access.Factory;
import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.GsonHelper;
import aq.common.util.StringUtil;
import aq.dao.shop.ShopDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.shop.ShopService;
import aq.service.system.Func;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ywb on 2017-02-23.
 */
@Service("serviceShop")
@DyncDataSource
public class ShopServiceImpl extends BaseServiceImpl  implements ShopService {

    @Resource
    private ShopDao shopDao;


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryShop(JsonObject jsonObject) {
        jsonObject.addProperty("service","shop");
        return query(jsonObject,(map)->{
            return shopDao.selectShop(map);
        });
    }

    @Override
    public JsonObject updateShop(JsonObject jsonObject) {
        Rtn rtn = new Rtn("shop");
        Map<String,Object> res = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        res.put("lastCreateTime",new Date());
        shopDao.updateShop(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject deleteShop(JsonObject jsonObject) {
        Rtn rtn = new Rtn("shop");
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res.put("id",jsonObject.get("id").getAsString());
        shopDao.deleteShop(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }
}
