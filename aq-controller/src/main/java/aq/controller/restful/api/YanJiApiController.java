package aq.controller.restful.api;

import aq.common.util.HttpUtil;
import aq.common.util.StringUtil;
import aq.service.resource.ResourceApiService;
import aq.service.yanji.YanJiApiService;
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
@RequestMapping("/api/yanji")
public class YanJiApiController extends aq.controller.restful.System {

    @Resource
    protected YanJiApiService yanJiApiService;

    @Resource
    protected ResourceApiService resourceApiService;

    //查询验机数据
    @RequestMapping(value = "/yanji",method = RequestMethod.GET)
    public void yanJi(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        if(StringUtil.isEmpty(jsonObject.get("id")) || StringUtil.isEmpty(jsonObject.get("id"))) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND,"id and no is no null!");
        }
        writerJson(response,out,yanJiApiService.yanJi(jsonObject));
    }

    //插入验机数据
    @RequestMapping(value = "/push",method = RequestMethod.POST)
    @ResponseBody
    public void pushYanJi(@RequestBody JsonObject requestJson,HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        writerJson(response,out,yanJiApiService.inseryYanJi(requestJson));
    }

    //更新验机数据
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public void updateYanJi(@RequestBody JsonObject requestJson,HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        writerJson(response,out,yanJiApiService.updateYanJi(requestJson));
    }

    //查询已验机数据
    @RequestMapping(value = "/amsler",method = RequestMethod.GET)
    public void amslerList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,yanJiApiService.amslerList(jsonObject));
    }
}
