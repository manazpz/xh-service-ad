package aq.controller.restful;

import aq.common.other.Rtn;
import aq.common.util.GsonHelper;
import aq.common.util.HttpUtil;
import aq.service.system.ConfigService;
import aq.service.system.Func;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/config")
public class Config extends Base {

    @Resource
    protected ConfigService configService;

    //配置表列表
    @ResponseBody
    @RequestMapping(value = "list", method=RequestMethod.GET)
    public void  configList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception{
        writerJson(response,out,configService.queryaConfig(HttpUtil.getParameterMap(request)));
    }

    //新增配置表
    @RequestMapping(value = "/insertConfig",method = RequestMethod.POST)
    @ResponseBody
    public void insertConfig(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,configService.insertConfig(requestJson));
    }

    //更新配置表
    @RequestMapping(value = "/updateConfig",method = RequestMethod.POST)
    @ResponseBody
    public void updateConfig(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,configService.updateConfig(requestJson));
    }

    //删除配置表
    @RequestMapping(value = "/deleteConfig",method = RequestMethod.POST)
    @ResponseBody
    public void deleteConfig(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,configService.deleteConfig(requestJson));
    }

    //新增第三方配置
    @RequestMapping(value = "/insertTpp",method = RequestMethod.POST)
    @ResponseBody
    public void insertTpp(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,configService.insertTppConfig(requestJson));
    }

    //更新第三方配置
    @RequestMapping(value = "/updateTpp",method = RequestMethod.POST)
    @ResponseBody
    public void updateTpp(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,configService.updateTppConfig(requestJson));
    }

    @ResponseBody
    @RequestMapping(value = "tppList", method=RequestMethod.GET)
    public void  tppList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception{
        Rtn rtn = new Rtn("Config");
        JsonObject parameterMap = HttpUtil.getParameterMap(request);
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        Map map = GsonHelper.getInstance().fromJson(parameterMap, Map.class);
        List list = configService.selectTppConfig(map);
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(list)).getAsJsonArray();
        data.add("items",jsonArray);
        rtn.setCode(200);
        rtn.setMessage("success");
        rtn.setData(data);
        writerJson(response,out, Func.functionRtnToJsonObject.apply(rtn));
    }

    //删除第三方配置
    @RequestMapping(value = "/deleteTpp",method = RequestMethod.POST)
    @ResponseBody
    public void deleteTpp(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,configService.deleteTppConfig(requestJson));
    }

}
