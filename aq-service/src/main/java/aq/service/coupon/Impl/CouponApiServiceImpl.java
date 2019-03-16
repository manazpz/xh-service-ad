package aq.service.coupon.Impl;

import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.GsonHelper;
import aq.common.util.UUIDUtil;
import aq.dao.coupon.CouponDao;
import aq.dao.user.UserDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.coupon.CouponApiService;
import aq.service.system.Func;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by ywb on 2017-02-23.
 */
@Service("serviceCouponApi")
@DyncDataSource
public class CouponApiServiceImpl extends BaseServiceImpl  implements CouponApiService {

    @Resource
    private CouponDao couponDao;

    @Resource
    private UserDao userDao;

    @Override
    public JsonObject queryCoupon(JsonObject jsonObject) {
        Rtn rtn = new Rtn("coupon");
        Map<String,Object> res = new HashMap<>();
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        List list = couponDao.selectCoupon(res);
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(list)).getAsJsonArray();
        data.addProperty("total",list.size());
        data.add("items",jsonArray);
        rtn.setCode(200);
        rtn.setMessage("success");
        rtn.setData(data);
        return  Func.functionRtnToJsonObject.apply(rtn);
    }


    @Override
    public JsonObject insertCoupon(JsonObject jsonObject) {
        Rtn rtn = new Rtn("coupon");
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> rest = new HashMap<>();
        Map<String,Object> rests = new HashMap<>();
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        if(res.get("no") != null){
            rests.put("no",res.get("no"));
            List<Map<String,Object>> list = couponDao.selectCoupon(rests);
            if(list.size()>0){
                rest.put("id",list.get(0).get("id"));
            }else{
                rtn.setCode(502);
                rtn.setMessage("error");
            }
        }else{
            rest.put("id",res.get("id"));
        }
        if(rest.get("id")!= null){
            rests.clear();
            rests.put("openid",res.get("openId")== null ? "": res.get("openId"));
            List<Map<String, Object>> maps = userDao.selectUserInfos(rests);
            if(maps.size()>0){
                List<Map<String, Object>> maps1 = couponDao.selectUserCoupon(rest);
                if(maps1.size()<=0){
                    rests.clear();
                    rests.put("id",rest.get("id"));
                    rests.put("userId",maps.get(0).get("id"));
                    rests.put("types", "01");//01：未使用  02：已使用  03： 已过期
                    rests.put("createUserId",maps.get(0).get("id"));
                    rests.put("createTime",new Date());
                    rests.put("updateTime",new Date());
                    couponDao.instertCouponUser(rests);
                    rtn.setCode(200);
                    rtn.setMessage("success");
                    rtn.setData(data);
                }else{
                    rtn.setCode(501);
                    rtn.setMessage("error");
                }
            }else{
                rtn.setCode(500);
                rtn.setMessage("error");
            }
        }
        return  Func.functionRtnToJsonObject.apply(rtn);
    }


    @Override
    public JsonObject queryCouponListUser(JsonObject jsonObject) {
        Rtn rtn = new Rtn("coupon");
        Map<String,Object> res = new HashMap<>();
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        List list = couponDao.selectUserCoupon(res);
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(list)).getAsJsonArray();
        data.addProperty("total",list.size());
        data.add("items",jsonArray);
        rtn.setCode(200);
        rtn.setMessage("success");
        rtn.setData(data);
        return  Func.functionRtnToJsonObject.apply(rtn);
    }
}
