package aq.controller.restful.api;

import aq.common.util.HttpUtil;
import aq.service.collect.CollectApiService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Controller
@RequestMapping("/api/collect")
public class CollectApiController extends aq.controller.restful.System {

    @Resource
    protected CollectApiService collectApiService;

    //添加收藏
    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    @ResponseBody
    public void insertCollect(@RequestBody JsonObject requestJson,HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        writerJson(response,out,collectApiService.insertCollect(requestJson));
    }

    //查询商品
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public void collectList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,collectApiService.queryCollect(jsonObject));
    }

    //删除收藏
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public void deleteCollect(@RequestBody JsonObject requestJson,HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        writerJson(response,out,collectApiService.deleteCollect(requestJson));
    }

}
