package aq.controller.restful.api.resourceApi;

import aq.common.util.HttpUtil;
import aq.common.util.StringUtil;
import aq.service.resource.ResourceService;
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
    protected ResourceService resourceService;

    //查询资源
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public void permissionList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        if(StringUtil.isEmpty(jsonObject.get("type"))) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND,"type is no null!");
        }
        jsonObject.addProperty("way","API");
        writerJson(response,out,resourceService.queryaResource(jsonObject));
    }


}
