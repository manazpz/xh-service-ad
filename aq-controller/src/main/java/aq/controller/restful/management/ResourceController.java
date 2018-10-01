package aq.controller.restful.management;

import aq.common.util.HttpUtil;
import aq.service.resource.ResourceService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Controller
@RequestMapping("/resource")
public class ResourceController extends aq.controller.restful.System {

    @Resource
    protected ResourceService resourceService;

    //查询资源列表
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public void resourceList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,resourceService.queryaResource(jsonObject));
    }

    //插入资源
    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    @ResponseBody
    public void insertResource(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,resourceService.insertResource(requestJson));
    }

    //更新资源
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public void updateResource(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,resourceService.updateResource(requestJson));
    }

    //删除资源
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public void deleteResource(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,resourceService.deleteResource(requestJson));
    }


}
