package aq.controller.restful.management;

import aq.common.util.HttpUtil;
import aq.service.goods.GoodsService;
import aq.service.goods.OrderService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Controller
@RequestMapping("/order")
public class OrderController extends aq.controller.restful.System {
    @Resource
    protected OrderService orderService;

    //查询商品分类
    @ResponseBody
    @RequestMapping(value = "/orderList",method = RequestMethod.GET)
    public void orderList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,orderService.queryorderList(jsonObject));
    }



}
