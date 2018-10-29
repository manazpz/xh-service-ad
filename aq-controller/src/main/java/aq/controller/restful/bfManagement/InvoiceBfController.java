package aq.controller.restful.bfManagement;

import aq.common.util.HttpUtil;
import aq.service.invoice.InvoiceBfService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Controller
@RequestMapping("/bf/invoice")
public class InvoiceBfController extends aq.controller.restful.System {
    @Resource
    protected InvoiceBfService invoiceBfService;

    //查询订单列表
    @ResponseBody
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public void orderList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,invoiceBfService.selectInvoice(jsonObject));
    }

    //插入发票
    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    @ResponseBody
    public void insertInvoice(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,invoiceBfService.insertInvoice(requestJson));
    }

    //更新发票
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public void updateInvoice(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,invoiceBfService.updateInvoice(requestJson));
    }
}
