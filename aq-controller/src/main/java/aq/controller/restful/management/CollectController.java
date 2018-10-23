package aq.controller.restful.management;

import aq.common.util.HttpUtil;
import aq.service.collect.CollectService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Controller
@RequestMapping("/collect")
public class CollectController extends aq.controller.restful.System {

    @Resource
    protected CollectService collectService;

    //查询商品
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public void collectList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,collectService.queryCollect(jsonObject));
    }

}
