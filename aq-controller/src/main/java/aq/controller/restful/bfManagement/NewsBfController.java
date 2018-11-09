package aq.controller.restful.bfManagement;

import aq.common.util.HttpUtil;
import aq.service.news.NewsBfService;
import aq.service.order.OrderBfService;
import aq.service.order.OrderService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Controller
@RequestMapping("/bf/news")
public class NewsBfController extends aq.controller.restful.System {
    @Resource
    protected NewsBfService newsBfService;

    //查询评论列表
    @ResponseBody
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public void commentList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,newsBfService.queryCommentList(jsonObject));
    }

    //回复评论
    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    @ResponseBody
    public void insertComment(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,newsBfService.insertComment(requestJson));
    }

}
