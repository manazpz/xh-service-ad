package aq.controller.restful.management;

import aq.common.util.HttpUtil;
import aq.common.util.StringUtil;
import aq.service.shop.ShopService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Controller
@RequestMapping("/shop")
public class ShopController extends aq.controller.restful.System {

    @Resource
    protected ShopService shopService;


    //新增店铺
    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    public void shopInsert(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,shopService.insertShop(request,jsonObject));
    }

    //查询店铺
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public void shopList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,shopService.queryShop(jsonObject));
    }

    //查询店铺详情
    @RequestMapping(value = "/detail",method = RequestMethod.GET)
    public void shopDetail(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,shopService.queryShopDetail(jsonObject));
    }

    //更新店铺
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public void shopUpdate(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,shopService.updateShop(request,jsonObject));
    }

    //删除店铺
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public void shopDelete(@RequestBody JsonObject requestJson,HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        if(StringUtil.isEmpty(requestJson.get("id"))) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND,"id is no null!");
        }
        writerJson(response,out,shopService.deleteShop(requestJson));
    }

    //删除店铺
    @RequestMapping(value = "/settlementUpdate",method = RequestMethod.POST)
    @ResponseBody
    public void settlementUpdate(@RequestBody JsonObject requestJson,HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        writerJson(response,out,shopService.updateSettlement(requestJson));
    }

    //查询店铺结算账单
    @RequestMapping(value = "/settlementList",method = RequestMethod.GET)
    public void settlementList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,shopService.querySettlement(jsonObject));
    }

}
