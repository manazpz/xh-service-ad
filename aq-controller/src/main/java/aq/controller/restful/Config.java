package aq.controller.restful;

import aq.common.util.HttpUtil;
import aq.service.system.ConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Controller
@RequestMapping("/config")
public class Config extends Base {

    @Resource
    protected ConfigService configService;

    @ResponseBody
    @RequestMapping(value = "list", method=RequestMethod.GET)
    public void  configList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception{
        writerJson(response,out,configService.queryaConfig(HttpUtil.getParameterMap(request)));
    }

}
