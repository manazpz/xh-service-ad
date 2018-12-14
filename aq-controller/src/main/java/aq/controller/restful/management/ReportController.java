package aq.controller.restful.management;

import aq.common.util.HttpUtil;
import aq.dao.report.ReportDao;
import aq.service.order.OrderService;
import aq.service.report.CustomerRService;
import aq.service.report.ReportService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Controller
@RequestMapping("/report")
public class ReportController extends aq.controller.restful.System {

    @Resource
    protected CustomerRService customerRService;

    @Resource
    protected ReportService reportService;

    //查询客户报表
    @ResponseBody
    @RequestMapping(value = "/customer",method = RequestMethod.GET)
    public void customerReport(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,customerRService.queryCustomerReport(jsonObject));
    }

    //查询订单报表
    @ResponseBody
    @RequestMapping(value = "/order",method = RequestMethod.GET)
    public void orderReport(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,reportService.queryOrderReport(jsonObject));
    }

}
