package aq.controller.restful.api;

import aq.common.util.HttpUtil;
import aq.service.coupon.CouponApiService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Controller
@RequestMapping("/api/coupon")
public class CouponApiController extends aq.controller.restful.System {

    @Resource
    protected CouponApiService couponApiService;

    //查询优惠券
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public void couponList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,couponApiService.queryCoupon(jsonObject));
    }

    //领取优惠券
    @RequestMapping(value = "/get",method = RequestMethod.POST)
    @ResponseBody
    public void getCoupon(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,couponApiService.insertCoupon(requestJson));
    }

    //查询用户已领取优惠券
    @RequestMapping(value = "/listuser",method = RequestMethod.GET)
    public void couponListUser(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,couponApiService.queryCouponListUser(jsonObject));
    }
}
