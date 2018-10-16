package aq.controller.restful.api;

import aq.common.util.HttpUtil;
import aq.common.util.StringUtil;
import aq.service.resource.ResourceApiService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Controller
@RequestMapping("/api/resource")
public class ResourceApiController extends aq.controller.restful.System {

    @Resource
    protected ResourceApiService resourceApiService;

    //查询资源
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public void permissionList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        if(StringUtil.isEmpty(jsonObject.get("type"))) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND,"type is no null!");
        }
        writerJson(response,out,resourceApiService.queryaResource(jsonObject));
    }

    //上传文件
    @RequestMapping(value = "/uploadFiles",method = RequestMethod.POST)
    public void uploadFiles(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject fileObject = resourceApiService.uploadFiles(request, response);
        writerJson(response,out,fileObject);
    }

    //删除资源
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public void deleteResource(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,resourceApiService.deleteResource(requestJson));
    }


}
