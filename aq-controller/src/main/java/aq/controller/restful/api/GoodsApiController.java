package aq.controller.restful.api;

import aq.common.util.HttpUtil;
import aq.common.util.StringUtil;
import aq.service.goods.GoodsApiService;
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
    protected GoodsApiService goodsService;

    //查询商品
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public void goodsList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,goodsService.queryGoods(jsonObject));
    }

    //查询标签商品
    @RequestMapping(value = "/lableList",method = RequestMethod.GET)
    public void goodsLableList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,goodsService.queryLableGoods(jsonObject));
    }

    //查询商品详情
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public void goodsDetail(@PathVariable(value = "id") String id,HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        jsonObject.addProperty("id",id);
        writerJson(response,out,goodsService.queryGoods(jsonObject));
    }

    //查询商品选择列表
    @RequestMapping(value = "/choiceList",method = RequestMethod.GET)
    public void choiceList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,goodsService.queryChoices(jsonObject));
    }

    //获取商品规格列表
    @RequestMapping(value = "/specList/{goodsId}",method = RequestMethod.GET)
    public void specList(@PathVariable(value = "goodsId") String goodsId,HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        jsonObject.addProperty("id",goodsId);
        writerJson(response,out,goodsService.querygoodsSpecs(jsonObject));
    }

    //商品估价
    @RequestMapping(value = "/evaluates",method = RequestMethod.POST)
    @ResponseBody
    public void evaluates(@RequestBody JsonObject requestJson,HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        if(StringUtil.isEmpty(requestJson.get("goodsId"))) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND,"goodsId is no null!");
        }
        writerJson(response,out,goodsService.evaluates(requestJson));
    }

    //添加置换车
    @RequestMapping(value = "/replacementCar/insert",method = RequestMethod.POST)
    @ResponseBody
    public void replacementCarInsert(@RequestBody JsonObject requestJson,HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        if(StringUtil.isEmpty(requestJson.get("openId"))) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND,"openId is no null!");
        }
        if(StringUtil.isEmpty(requestJson.get("goodsId"))) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND,"goodsId is no null!");
        }
        writerJson(response,out,goodsService.insertReplacementCar(requestJson));
    }

    //更新置换车
    @RequestMapping(value = "/replacementCar/update",method = RequestMethod.POST)
    @ResponseBody
    public void replacementCarUpdate(@RequestBody JsonObject requestJson,HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        if(StringUtil.isEmpty(requestJson.get("id"))) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND,"id is no null!");
        }
        writerJson(response,out,goodsService.updateReplacementCar(requestJson));
    }

    //置换车数据
    @RequestMapping(value = "/replacementCarList",method = RequestMethod.GET)
    public void replacementCarList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        if(StringUtil.isEmpty(jsonObject.get("ids"))) {
            if(StringUtil.isEmpty(jsonObject.get("openId"))) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,"openId is no null!");
            }
        }
        writerJson(response,out,goodsService.queryReplacementCar(jsonObject));
    }

    //删除置换车
    @RequestMapping(value = "/replacementCar/delete",method = RequestMethod.POST)
    @ResponseBody
    public void replacementCarDelete(@RequestBody JsonObject requestJson,HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        if(StringUtil.isEmpty(requestJson.get("id"))) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND,"id is no null!");
        }
        writerJson(response,out,goodsService.deleteReplacementCar(requestJson));
    }

    //查询默认回收方式
    @RequestMapping(value = "/queryrecoveryList",method = RequestMethod.GET)
    public void queryrecoveryList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,goodsService.queryrecoveryList(jsonObject));
    }

    //查询回收方式
    @RequestMapping(value = "/recoveryList",method = RequestMethod.GET)
    public void recoveryList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,goodsService.recoveryList(jsonObject));
    }

    //新增回收方式
    @RequestMapping(value = "/recoveryListUser/insert",method = RequestMethod.POST)
    @ResponseBody
    public void insertrecoveryListUser(@RequestBody JsonObject requestJson,HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        writerJson(response,out,goodsService.insertrecoveryListUser(requestJson));
    }


    //查询商品评论信息
    @RequestMapping(value = "/queryGoodsComment",method = RequestMethod.GET)
    public void queryGoodsComment(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,goodsService.queryGoodsComment(jsonObject));
    }

    //更新首页商品历史记录
    @RequestMapping(value = "/homeGoods/update",method = RequestMethod.POST)
    @ResponseBody
    public void updateHomeGoods(@RequestBody JsonObject requestJson,HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        writerJson(response,out,goodsService.updateHomeGoods(requestJson));
    }

    //查询商品价格预测列表
    @ResponseBody
    @RequestMapping(value = "/forecastList",method = RequestMethod.GET)
    public void forecastList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,goodsService.queryForecastList(jsonObject));
    }

    //更新商品库存
    @RequestMapping(value = "/updateStock",method = RequestMethod.POST)
    @ResponseBody
    public void updateStock(@RequestBody JsonObject requestJson,HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        writerJson(response,out,goodsService.updateStock(requestJson));
    }
}
