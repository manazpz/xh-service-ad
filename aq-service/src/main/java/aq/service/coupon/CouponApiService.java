package aq.service.coupon;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

/**
 * Created by ywb on 2017-02-23.
 */
public interface CouponApiService extends BaseService {

    //查询优惠券
    JsonObject queryCoupon(JsonObject jsonObject);

    //查询优惠券
    JsonObject insertCoupon(JsonObject jsonObject);

    //查询已领取优惠券
    JsonObject queryCouponListUser(JsonObject jsonObject);

}
