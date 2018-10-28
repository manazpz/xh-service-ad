package aq.dao.coupon;

import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

@MapperScan("daoCoupon")
public interface CouponDao {

    //查询优惠券
    List<Map<String,Object>> selectCoupon(Map<String,Object> map);

    //新增优惠券
    int insertCoupon(Map<String,Object> map);

    //更新优惠券
    int updateCoupon(Map<String,Object> map);

    //删除优惠券
    int deleteCoupon(Map<String,Object> map);

    //用户领取优惠券
    int instertCouponUser(Map<String,Object> map);

    //查询用户已领取优惠券
    List<Map<String,Object>> selectUserCoupon(Map<String,Object> map);

}
