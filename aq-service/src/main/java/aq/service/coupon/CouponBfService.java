package aq.service.coupon;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

/**
 * Created by ywb on 2017-02-23.
 */
public interface CouponBfService extends BaseService {

    //查询优惠券
    JsonObject queryCoupon(JsonObject jsonObject);

    //插入优惠券
    JsonObject insertCoupon(JsonObject jsonObject);

    //更新优惠券
    JsonObject updateCoupon(JsonObject jsonObject);

    //删除优惠券
    JsonObject deleteCoupon(JsonObject jsonObject);

}
