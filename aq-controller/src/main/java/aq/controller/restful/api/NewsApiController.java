package aq.controller.restful.api;

import aq.common.util.HttpUtil;
import aq.common.util.StringUtil;
import aq.service.news.NewsApiService;
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
@RequestMapping("/api/news")
public class NewsApiController extends aq.controller.restful.System {

    @Resource
    protected NewsApiService newsApiService;

    //查询订单日志
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public void orderNewsList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        if(StringUtil.isEmpty(jsonObject.get("openId"))) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND,"openId is no null!");
        }
        writerJson(response,out,newsApiService.queryOrderNewsList(jsonObject));
    }
}
