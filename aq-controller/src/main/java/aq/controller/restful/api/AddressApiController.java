package aq.controller.restful.api;

import aq.common.util.HttpUtil;
import aq.service.system.AddressService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Controller
@RequestMapping("/api/address")
public class AddressApiController extends aq.controller.restful.System {

    @Resource
    protected AddressService addressService;

    //新增地址
    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    @ResponseBody
    public void insertAdress(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,addressService.insertAddress(requestJson));
    }


    //查询回收方式
    @RequestMapping(value = "/queryAddress",method = RequestMethod.GET)
    public void queryAddress(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,addressService.queryAddress(jsonObject));
    }

    //删除地址
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public void deleteAdress(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,addressService.deleteAdress(requestJson));
    }

    //新增地址
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public void updateAdress(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,addressService.updateAdress(requestJson));
    }
}
