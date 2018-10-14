package aq.controller.restful.api;

import aq.common.util.HttpUtil;
import aq.common.util.StringUtil;
import aq.service.yanji.YanJiApiService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Controller
@RequestMapping("/api/yanji")
public class YanJiApiController extends aq.controller.restful.System {

    @Resource
    protected YanJiApiService yanJiApiService;

    //验机
    @RequestMapping(value = "/yanji",method = RequestMethod.GET)
    public void yanJi(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        if(StringUtil.isEmpty(jsonObject.get("id")) || StringUtil.isEmpty(jsonObject.get("id"))) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND,"id and no is no null!");
        }
        writerJson(response,out,yanJiApiService.yanJi(jsonObject));
    }
}
