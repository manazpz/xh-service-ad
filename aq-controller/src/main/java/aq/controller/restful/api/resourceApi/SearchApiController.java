package aq.controller.restful.api.resourceApi;

import aq.common.util.HttpUtil;
import aq.common.util.StringUtil;
import aq.service.goods.SearchService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Controller
@RequestMapping("/api/search")
public class SearchApiController extends aq.controller.restful.System {

    @Resource
    protected SearchService searchService;

    //搜索列表
    @RequestMapping(value = "/specs",method = RequestMethod.GET)
    public void searchSpecList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        if(StringUtil.isEmpty(jsonObject.get("type"))) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND,"type is no null!");
        }
        writerJson(response,out,searchService.querySearchSpec(jsonObject));
    }



}
