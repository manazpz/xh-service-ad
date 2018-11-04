package aq.controller.restful.management;

import aq.common.util.HttpUtil;
import aq.service.statistics.StatisticsService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Controller
@RequestMapping("/statistics")
public class StatisticsController extends aq.controller.restful.System {

    @Resource
    protected StatisticsService statisticsService;

    //总统计信息
    @RequestMapping(value = "/info",method = RequestMethod.GET)
    public void info(HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,statisticsService.statisticsInfo(jsonObject));
    }

    //周总统计信息
    @RequestMapping(value = "/weekInfo",method = RequestMethod.GET)
    public void weekInfo(HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,statisticsService.statisticsWeekInfo(jsonObject));
    }

    //订单类型统计信息
    @RequestMapping(value = "/orderTypeInfo",method = RequestMethod.GET)
    public void orderTypeInfo(HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,statisticsService.statisticsOrderType(jsonObject));
    }

    //半年完结订单统计
    @RequestMapping(value = "/endOrderMoonInfo",method = RequestMethod.GET)
    public void endOrderMoonInfo(HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,statisticsService.statisticsendOrderMoon(jsonObject));
    }

    //3年数据统计
    @RequestMapping(value = "/endOrderYearInfo",method = RequestMethod.GET)
    public void endOrderYearInfo(HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,statisticsService.statisticsendOrderYear(jsonObject));
    }

    //获取最新客服
    @RequestMapping(value = "/customers",method = RequestMethod.GET)
    public void customers(HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,statisticsService.queryCustomer(jsonObject));
    }

    //获取标签商品
    @RequestMapping(value = "/lableGoods",method = RequestMethod.GET)
    public void lableGoods(HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,statisticsService.queryLableGoods(jsonObject));
    }

    //昨日热销商品
    @RequestMapping(value = "/zrSaleGoods",method = RequestMethod.GET)
    public void zrSaleGoods(HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,statisticsService.queryZrSaleGoods(jsonObject));
    }

}
