package aq.controller.restful.bfManagement;

import aq.common.util.HttpUtil;
import aq.common.util.StringUtil;
import aq.service.coupon.CouponBfService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Controller
@RequestMapping("/bf/coupon")
public class CouponBfController extends aq.controller.restful.System {
    @Resource
    protected CouponBfService couponBfService;

    //查询优惠券
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public void couponList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,couponBfService.queryCoupon(jsonObject));
    }

    //新增优惠券
    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    @ResponseBody
    public void insertCoupon(@RequestBody JsonObject requestJson,HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        writerJson(response,out,couponBfService.insertCoupon(requestJson));
    }

    //更新优惠券
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public void updateCoupon(@RequestBody JsonObject requestJson,HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        writerJson(response,out,couponBfService.updateCoupon(requestJson));
    }

    //删除优惠券
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public void deleteCoupon(@RequestBody JsonObject requestJson,HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        writerJson(response,out,couponBfService.deleteCoupon(requestJson));
    }

}
