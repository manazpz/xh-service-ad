package aq.controller.restful.api;

import aq.common.util.HttpUtil;
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



}
