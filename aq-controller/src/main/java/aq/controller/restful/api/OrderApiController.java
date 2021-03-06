package aq.controller.restful.api;

import aq.common.util.HttpUtil;
import aq.common.util.StringUtil;
import aq.service.order.OrderApiService;
import aq.service.order.OrderService;
import aq.service.system.AddressService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Controller
@RequestMapping("/api/order")
public class OrderApiController extends aq.controller.restful.System {

    @Resource
    protected OrderApiService orderApiService;

    //新增订单
    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    @ResponseBody
    public void insertOrder(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,orderApiService.instertorderList(requestJson));
    }

    //查询订单
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public void orderList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,orderApiService.queryorderList(jsonObject));
    }

    //更新订单
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public void updateOrder(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,orderApiService.updateOrder(requestJson));
    }


    //更新订单明细
    @RequestMapping(value = "/updateDetail",method = RequestMethod.POST)
    @ResponseBody
    public void updateOrderDetail(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,orderApiService.updateOrderDetail(requestJson));
    }


    //新增评论
    @RequestMapping(value = "/insertRate",method = RequestMethod.POST)
    @ResponseBody
    public void insertRate(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,orderApiService.insertRate(requestJson));
    }

    //查询订单
    @RequestMapping(value = "/logistics",method = RequestMethod.GET)
    public void logisticalList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,orderApiService.queryLogistical(jsonObject));
    }

    //新增退款信息
    @RequestMapping(value = "/insertReturn",method = RequestMethod.POST)
    @ResponseBody
    public void insertReturn(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,orderApiService.insertReturn(requestJson));
    }
}
