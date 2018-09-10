package aq.controller.restful.management;

import aq.common.util.HttpUtil;
import aq.service.statement.StatementService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Controller
@RequestMapping("/statement")
public class StatementController extends aq.controller.restful.System {

    @Resource
    protected StatementService statementService;

    //新建声明
    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    @ResponseBody
    public void insertStatement(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,statementService.insertStatement(requestJson));
    }

    //更新声明
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public void updateStatement(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,statementService.updateStatement(requestJson));
    }

    //声明列表
    @ResponseBody
    @RequestMapping(value = "/list", method=RequestMethod.GET)
    public void  statementList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception{
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,statementService.selectStatementList(jsonObject));
    }

    //删除声明
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public void deleteStatement(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,statementService.deleteStatement(requestJson));
    }

}
