package aq.service.coupon.Impl;

import aq.common.access.AbsAccessUser;
import aq.common.access.Factory;
import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.DateTime;
import aq.common.util.GsonHelper;
import aq.common.util.StringUtil;
import aq.common.util.UUIDUtil;
import aq.dao.coupon.CouponDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.coupon.CouponBfService;
import aq.service.system.Func;
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
@Service("serviceCouponBf")
@DyncDataSource
public class CouponBfServiceImpl extends BaseServiceImpl  implements CouponBfService {

    @Resource
    private CouponDao couponDao;

    @Override
    public JsonObject queryCoupon(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("Coupon");
        jsonObject.addProperty("service","Config");
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
            return Func.functionRtnToJsonObject.apply(rtn);
        } else {
            jsonObject.addProperty("createUser",user.getUserId());
            return query(jsonObject,(map)->{
                return couponDao.selectCoupon(map);
            });
        }
    }

    @Override
    public JsonObject insertCoupon(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Map res = new HashMap();
        Rtn rtn = new Rtn("Coupon");
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
        } else {
            res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
            res.put("id", UUIDUtil.getUUID());
            res.put("no", UUIDUtil.getRandomOrderId());
            if(!StringUtil.isEmpty(res.get("yxq")) && res.get("yxq") instanceof List) {
                res.put("beginTime",DateTime.compareDate(((List) res.get("yxq")).get(0).toString()));
                res.put("endTime",DateTime.compareDate(((List) res.get("yxq")).get(1).toString()));
            }
            res.put("createUserId", user.getUserId());
            res.put("createTime",new Date());
            res.put("lastCreateUserId", user.getUserId());
            res.put("lastCreateTime",new Date());
            couponDao.insertCoupon(res);
            rtn.setCode(200);
            rtn.setMessage("success");
        }
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject updateCoupon(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Map res = new HashMap();
        Rtn rtn = new Rtn("Coupon");
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
        } else {
            res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
            if(!StringUtil.isEmpty(res.get("yxq")) && res.get("yxq") instanceof List) {
                res.put("beginTime",DateTime.compareDate(((List) res.get("yxq")).get(0).toString()));
                res.put("endTime",DateTime.compareDate(((List) res.get("yxq")).get(1).toString()));
            }
            res.put("lastCreateUserId", user.getUserId());
            res.put("lastCreateTime",new Date());
//            if(StringUtil.isEmpty(res.get("obligate1")))
//                res.put("obligate1",'N');
            couponDao.updateCoupon(res);
            rtn.setCode(200);
            rtn.setMessage("success");
        }
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject deleteCoupon(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Config");
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        couponDao.deleteCoupon(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return  Func.functionRtnToJsonObject.apply(rtn);
    }
}
