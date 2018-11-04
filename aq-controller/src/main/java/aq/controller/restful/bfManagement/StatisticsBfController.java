package aq.controller.restful.bfManagement;

import aq.common.util.HttpUtil;
import aq.service.statistics.StatisticsBfService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Controller
@RequestMapping("/bf/statistics")
public class StatisticsBfController extends aq.controller.restful.System {

    @Resource
    protected StatisticsBfService statisticsBfService;

    //各类型订单统计
    @RequestMapping(value = "/orderType",method = RequestMethod.GET)
    public void orderType(HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,statisticsBfService.statisticsOrderType(jsonObject));
    }

    //昨日热销商品
    @RequestMapping(value = "/zrSaleGoods",method = RequestMethod.GET)
    public void zrSaleGoods(HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,statisticsBfService.queryZrSaleGoods(jsonObject));
    }

    //昨日状况
    @RequestMapping(value = "/zrStatus",method = RequestMethod.GET)
    public void zrStatus(HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,statisticsBfService.queryZrStatus(jsonObject));
    }

}
