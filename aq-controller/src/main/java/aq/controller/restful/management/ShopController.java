package aq.controller.restful.management;

import aq.common.util.HttpUtil;
import aq.common.util.StringUtil;
import aq.dao.shop.ShopDao;
import aq.service.resource.ResourceService;
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

    //查询店铺
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public void shopList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,shopService.queryShop(jsonObject));
    }

    //更新店铺
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public void shopUpdate(@RequestBody JsonObject requestJson,HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        if(StringUtil.isEmpty(requestJson.get("id"))) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND,"id is no null!");
        }
        writerJson(response,out,shopService.updateShop(requestJson));
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

}
