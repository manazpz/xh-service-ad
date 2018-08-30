package aq.controller.restful.bfManagement;

import aq.common.util.HttpUtil;
import aq.service.goods.GoodsApiService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Controller
@RequestMapping("/bf/goods")
public class GoodsBfController extends aq.controller.restful.System {

    @Resource
    protected GoodsApiService goodsService;

    //查询商品
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public void goodsList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,goodsService.queryGoods(jsonObject));
    }

}
