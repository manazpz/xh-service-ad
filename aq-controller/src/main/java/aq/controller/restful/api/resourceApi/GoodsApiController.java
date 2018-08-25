package aq.controller.restful.api.resourceApi;

import aq.common.util.HttpUtil;
import aq.service.goods.GoodsService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Controller
@RequestMapping("/api/goods")
public class GoodsApiController extends aq.controller.restful.System {

    @Resource
    protected GoodsService goodsService;

    //查询商品
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public void permissionList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,goodsService.queryGoods(jsonObject));
    }

    //查询机型
    @RequestMapping(value = "/modelList",method = RequestMethod.GET)
    public void modelList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,goodsService.queryModel(jsonObject));
    }


}
