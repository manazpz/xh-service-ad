package aq.controller.restful.bfManagement;

import aq.common.util.HttpUtil;
import aq.service.order.OrderBfService;
import aq.service.order.OrderService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Controller
@RequestMapping("/bf/order")
public class OrderBfController extends aq.controller.restful.System {
    @Resource
    protected OrderBfService orderBfService;

    //查询订单列表
    @ResponseBody
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public void orderList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,orderBfService.queryorderList(jsonObject));
    }

    //查询订单明细列表
    @ResponseBody
    @RequestMapping(value = "/detail",method = RequestMethod.GET)
    public void orderDetail(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,orderBfService.queryorderList(jsonObject));
    }

    //更新订单
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public void updateOrder(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,orderBfService.updateOrder(requestJson));
    }

    //新增物流信息
    @RequestMapping(value = "/insertLogistics",method = RequestMethod.POST)
    @ResponseBody
    public void insertLogistics(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,orderBfService.insertLogistics(requestJson));
    }

}
