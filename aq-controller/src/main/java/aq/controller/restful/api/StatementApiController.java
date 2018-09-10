package aq.controller.restful.api;

import aq.common.util.HttpUtil;
import aq.service.statement.StatementApiService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Controller
@RequestMapping("/api/statement")
public class StatementApiController extends aq.controller.restful.System {

    @Resource
    protected StatementApiService statementApiService;

    //声明列表
    @ResponseBody
    @RequestMapping(value = "/list", method=RequestMethod.GET)
    public void  statementList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception{
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,statementApiService.selectStatementList(jsonObject));
    }



}
