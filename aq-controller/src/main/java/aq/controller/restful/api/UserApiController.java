package aq.controller.restful.api;

import aq.common.util.HttpUtil;
import aq.common.util.StringUtil;
import aq.service.resource.ResourceApiService;
import aq.service.system.UserApiService;
import aq.service.yanji.YanJiApiService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Controller
@RequestMapping("/api/user")
public class UserApiController extends aq.controller.restful.System {

    @Resource
    protected UserApiService userApiService;

    //查询已验机数据
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public void amslerList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        if(StringUtil.isEmpty(jsonObject.get("openId")) || StringUtil.isEmpty(jsonObject.get("openId"))) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND,"openId and no is no null!");
        }
        writerJson(response,out,userApiService.queryUserInfos(jsonObject));
    }

    //删除地址
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public void deleteAdress(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,userApiService.updateUserInfos(requestJson));
    }

    //发送短信验证码
    @RequestMapping(value = "/send",method = RequestMethod.POST)
    public void sendCode(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,userApiService.sendCode(requestJson));
    }

    //新增意见反馈
    @RequestMapping(value = "/insertSuggestion",method = RequestMethod.POST)
    public void insertSuggestion(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,userApiService.insertSuggestion(requestJson));
    }
}
