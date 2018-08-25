package aq.service.goods.Impl;

import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.GsonHelper;
import aq.dao.goods.GoodsDao;
import aq.dao.goods.SpecDao;
import aq.dao.resource.ResourceDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.goods.GoodsService;
import aq.service.system.Func;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ywb on 2017-02-23.
 */
@Service("serviceGoods")
@DyncDataSource
public class GoodsServiceImpl extends BaseServiceImpl  implements GoodsService {

    @Resource
    private GoodsDao goodsDao;

    @Resource
    private SpecDao specDao;

    @Resource
    private ResourceDao resourceDao;

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryGoods(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Goods");
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        Map<String,Object> res = new HashMap<>();
        Map<String, Object> ress = res;
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        List<Map<String, Object>> goods = goodsDao.selectGoods(res);
        goods.forEach(obj->{
            ress.clear();
            ress.put("goodsId",obj.get("id"));
            List<Map<String, Object>> specs = specDao.selectGoodsSpec(ress);
            obj.put("spec",specs);
            ress.clear();
            ress.put("refId",obj.get("id"));
            ress.put("type",obj.get("GI"));
            List<Map<String, Object>> imgs = resourceDao.selectResource(ress);
            obj.put("img",imgs);
        });
        rtn.setCode(200);
        rtn.setMessage("success");
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(goods)).getAsJsonArray();
        data.addProperty("total",goods.size());
        data.add("items",jsonArray);
        rtn.setData(data);
        return  Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryModel(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Goods");
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        Map<String,Object> res = new HashMap<>();
        Map<String, Object> ress = res;
        //查询品牌
        List<Map<String, Object>> brands = goodsDao.selectBrand(res);
        brands.forEach(obj->{
            ress.clear();
            ress.put("brandId",obj.get("id"));
            List<Map<String, Object>> specPs = specDao.selectSpecP(ress);
            obj.put("model",specPs);
        });
        rtn.setCode(200);
        rtn.setMessage("success");
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(brands)).getAsJsonArray();
        data.addProperty("total",brands.size());
        data.add("items",jsonArray);
        rtn.setData(data);
        return  Func.functionRtnToJsonObject.apply(rtn);
    }

}
