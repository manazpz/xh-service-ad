package aq.controller.restful;

import aq.service.resource.ResourceService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Controller
@RequestMapping("/resources")
public class Resources extends Base {

    @Resource
    protected ResourceService resourceService;
    //单文件上传
    @RequestMapping(value = "uploadFile", method=RequestMethod.POST)
    public void  uploadFile(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception{
        writerJson(response,out,resourceService.uploadFile(request,response));
    }

    //删除资源
    @RequestMapping(value = "deleteFile", method=RequestMethod.POST)
    @ResponseBody
    public void deleteFile(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,resourceService.deleteFile(requestJson));
    }
}
