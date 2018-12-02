package aq.service.yanji.Impl;

import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.*;
import aq.dao.goods.GoodsDao;
import aq.dao.goods.SpecDao;
import aq.dao.order.OrderDao;
import aq.dao.resource.ResourceDao;
import aq.dao.user.UserDao;
import aq.dao.yanji.YanJiDao;
import aq.service.base.Impl.BaseServiceImpl;
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
@Service("serviceApiYanJi")
@DyncDataSource
public class YanJiApiServiceImpl extends BaseServiceImpl  implements YanJiApiService {

    @Resource
    private OrderDao orderDao;

    @Resource
    private GoodsDao goodsDao;

    @Resource
    private SpecDao specDao;

    @Resource
    private UserDao userDao;

    @Resource
    private YanJiDao yanJiDao;

    @Resource
    private ResourceDao resourceDao;

    @Override
    public JsonObject yanJi(JsonObject jsonObject) {
        Rtn rtn = new Rtn("yanji");
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        Map<String,Object> res = new HashMap<>();
        List<Map> specParameter = new ArrayList();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        List<Map<String, Object>> info = orderDao.selectorderDetailList(res);
        if(info.size() > 0) {
            res.clear();
            res.put("id",info.get(0).get("goodsId"));
            List<Map<String, Object>> goods = goodsDao.selectGoods(res);
            for(Map obj : goods) {
                if(!StringUtil.isEmpty(obj.get("specParameter"))) {
                    specParameter = GsonHelper.getInstance().fromJson(obj.get("specParameter").toString(), List.class);
                    for(Map spec : specParameter) {
                        res.clear();
                        res.put("id",spec.get("id"));
                        List<Map<String, Object>> ls = specDao.selectSpec(res);
                        if(ls.size() > 0) {
                            spec.put("tipsType", StringUtil.isEmpty(ls.get(0).get("tipsType"))?"":ls.get(0).get("tipsType"));
                            spec.put("tipsText", StringUtil.isEmpty(ls.get(0).get("tipsText"))?"":ls.get(0).get("tipsText"));
                            spec.put("tipsImg", StringUtil.isEmpty(ls.get(0).get("tipsImg"))?"":ls.get(0).get("tipsImg"));
                            spec.put("obligate", StringUtil.isEmpty(ls.get(0).get("obligate"))?"01":ls.get(0).get("obligate"));
                        }
                    }
                }
            }
            if(goods.size() > 0 && "02".equals(goods.get(0).get("model"))) {
                if(specParameter.size() > 0) {
                    CollectionsUtil.listMapSort(specParameter,"px");
                }
            }
            rtn.setCode(200);
            rtn.setMessage("success");
            jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(specParameter)).getAsJsonArray();
            data.addProperty("total",jsonArray.size());
            data.add("items",jsonArray);
            data.addProperty("bllParameter",info.get(0).get("parameter").toString());
            rtn.setData(data);
            return  Func.functionRtnToJsonObject.apply(rtn);
        }else {
            rtn.setCode(404);
            rtn.setMessage("errer");
            return  Func.functionRtnToJsonObject.apply(rtn);
        }
    }

    @Override
    public JsonObject inseryYanJi(JsonObject jsonObject) {
        Rtn rtn = new Rtn("order");
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> ress = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        ress.clear();
        ress.put("openid",res.get("openId"));
        List<Map<String, Object>> userInfos = userDao.selectUserInfos(ress);
        if(userInfos.size()>0) {
            ress.clear();
            String uuid = UUIDUtil.getUUID();
            ress.put("id",uuid);
            ress.put("orderNumber",res.get("orderNumber"));
            ress.put("no",res.get("no"));
            ress.put("parameter",res.get("parameter"));
            ress.put("bllPrice",res.get("bllPrice"));
            ress.put("createUserId",userInfos.get(0).get("id"));
            ress.put("createTime",new Date());
            yanJiDao.insertYanJi(ress);
            ress.clear();
            ress.put("checkStatus","02");
            ress.put("checkId",uuid);
            ress.put("id",res.get("orderNumber"));
            ress.put("no",res.get("no"));
            orderDao.updateOrderDetail(ress);
            rtn.setCode(200);
            rtn.setMessage("success");
        }else {
            rtn.setCode(404);
            rtn.setMessage("errer");
        }
        return  Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject updateYanJi(JsonObject jsonObject) {
        Rtn rtn = new Rtn("yanji");
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        yanJiDao.updateYanJi(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return  Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject amslerList(JsonObject jsonObject) {
        Rtn rtn = new Rtn("yanji");
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        List<Map<String, Object>> maps = yanJiDao.selectAmslerList(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(maps)).getAsJsonArray();
        data.addProperty("total",jsonArray.size());
        data.add("items",jsonArray);
        rtn.setData(data);
        return  Func.functionRtnToJsonObject.apply(rtn);
    }

}
