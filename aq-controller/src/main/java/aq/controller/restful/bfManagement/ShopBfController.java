package aq.controller.restful.bfManagement;

import aq.common.util.HttpUtil;
import aq.common.util.StringUtil;
import aq.service.resource.ResourceBfService;
import aq.service.shop.ShopBfService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Controller
@RequestMapping("/bf/shop")
public class ShopBfController extends aq.controller.restful.System {

    @Resource
    protected ShopBfService shopBfService;

    @Resource
    protected ResourceBfService resourceBfService;

    //查询店铺信息
    @RequestMapping(value = "/info",method = RequestMethod.GET)
    public void shopList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,shopBfService.queryShop(jsonObject));
    }

    //更新店铺
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public void shopUpdate(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        JsonObject fileObject = resourceBfService.uploadFiles(request, response);
        if (fileObject.get("data").getAsJsonObject().get("files") != null)
            jsonObject.add("files",fileObject.get("data").getAsJsonObject().get("files").getAsJsonArray());
//        if(StringUtil.isEmpty(requestJson.get("id"))) {
//            response.sendError(HttpServletResponse.SC_NOT_FOUND,"id is no null!");
//        }
        writerJson(response,out,shopBfService.updateShop(jsonObject));
    }

}
