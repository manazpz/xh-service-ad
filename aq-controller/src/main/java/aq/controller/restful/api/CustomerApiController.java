package aq.controller.restful.api;

import aq.common.util.HttpUtil;
import aq.common.util.StringUtil;
import aq.service.customer.CustomerApiService;
import aq.service.system.AddressService;
import aq.service.system.SystemService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Controller
@RequestMapping("/api/customer")
public class CustomerApiController extends aq.controller.restful.System {

    @Resource
    protected CustomerApiService CustomerApiService;

    //查询用户信息
    @RequestMapping(value = "/info",method = RequestMethod.GET)
    public void queryUserInfo(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        if(StringUtil.isEmpty(jsonObject.get("openId"))) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND,"openId is no null!");
        }
        writerJson(response,out,CustomerApiService.queryCustomer(jsonObject));
    }

}
