package aq.controller.restful.bfManagement;

import aq.common.util.HttpUtil;
import aq.common.util.StringUtil;
import aq.service.goods.GoodsApiService;
import aq.service.goods.GoodsBfService;
import aq.service.resource.ResourceBfService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Controller
@RequestMapping("/bf/goods")
public class GoodsBfController extends aq.controller.restful.System {

    @Resource
    protected GoodsBfService goodsBfService;

    @Resource
    protected ResourceBfService resourceBfService;

    //发布旧商品
    @RequestMapping(value = "/old/push",method = RequestMethod.POST)
    public void oldGoodsPush(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        JsonObject fileObject = resourceBfService.uploadFiles(request, response);
        if (fileObject.get("data").getAsJsonObject().get("files") != null)
            jsonObject.add("files",fileObject.get("data").getAsJsonObject().get("files").getAsJsonArray());
        JsonObject delFileObject = resourceBfService.deleteFiles(jsonObject);
        if (delFileObject.get("data").getAsJsonObject().get("delFiles") != null)
            jsonObject.add("delFiles",delFileObject.get("data").getAsJsonObject().get("delFiles").getAsJsonArray());
        writerJson(response,out,goodsBfService.insertOldGoods(jsonObject));
    }

    //发布新商品
    @RequestMapping(value = "/new/push",method = RequestMethod.POST)
    public void newGoodsPush(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        JsonObject fileObject = resourceBfService.uploadFiles(request, response);
        if (fileObject.get("data").getAsJsonObject().get("files") != null)
            jsonObject.add("files",fileObject.get("data").getAsJsonObject().get("files").getAsJsonArray());
        JsonObject delFileObject = resourceBfService.deleteFiles(jsonObject);
        if (delFileObject.get("data").getAsJsonObject().get("delFiles") != null)
            jsonObject.add("delFiles",delFileObject.get("data").getAsJsonObject().get("delFiles").getAsJsonArray());
        writerJson(response,out,goodsBfService.insertNewGoods(jsonObject));
    }

    //查询商品
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public void goodsList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,goodsBfService.queryGoods(jsonObject));
    }

    //批量更新
    @RequestMapping(value = "/batchUpdate",method = RequestMethod.POST)
    @ResponseBody
    public void batchUpdateGoods(@RequestBody JsonObject requestJson,HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        if(StringUtil.isEmpty(requestJson.get("ids"))) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND,"ids is no null!");
        }
        writerJson(response,out,goodsBfService.batchUpdateGoods(requestJson));
    }

    //删除商品（硬删除）
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public void goodsDelete(@RequestBody JsonObject requestJson,HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        if(StringUtil.isEmpty(requestJson.get("id"))) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND,"id is no null!");
        }
        writerJson(response,out,goodsBfService.deleteGoods(requestJson));
    }

    //删除/回复仓库商品（软删除）
    @RequestMapping(value = "/warehourseGoods/deleteRecovery",method = RequestMethod.POST)
    @ResponseBody
    public void warehourseGoodsDeleteRecovery(@RequestBody JsonObject requestJson,HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        if(StringUtil.isEmpty(requestJson.get("id"))) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND,"id is no null!");
        }
        writerJson(response,out,goodsBfService.updateGoods(requestJson));
    }

    //更新库存
    @RequestMapping(value = "/stock/update",method = RequestMethod.POST)
    @ResponseBody
    public void updateGoodsStock(@RequestBody JsonObject requestJson,HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        if(StringUtil.isEmpty(requestJson.get("id"))) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND,"stockId is no null!");
        }
        writerJson(response,out,goodsBfService.updateStock(requestJson));
    }

    //上下架
    @RequestMapping(value = "/upperLower",method = RequestMethod.POST)
    @ResponseBody
    public void upperLowerGoods(@RequestBody JsonObject requestJson,HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        if(StringUtil.isEmpty(requestJson.get("id"))) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND,"id is no null!");
        }
        writerJson(response,out,goodsBfService.updateGoods(requestJson));
    }

    //分类级联数据
    @RequestMapping(value = "/classify/cascade",method = RequestMethod.GET)
    public void classifyCascade(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,goodsBfService.selectClassifyCascade(jsonObject));
    }

    //商品分类级联数据
    @RequestMapping(value = "/classify/cascade/{goodsId}",method = RequestMethod.GET)
    public void classifyCascade(@PathVariable(value = "goodsId") String goodsId,HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        writerJson(response,out,goodsBfService.selectGoodsClassifyCascade(StringUtil.toJsonObject("goodsId",goodsId)));
    }

    //分类规格参数
    @RequestMapping(value = "/classify/specs/param/{id}",method = RequestMethod.GET)
    public void selectClassifySpecParam(@PathVariable(value = "id") String id,HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        writerJson(response,out,goodsBfService.selectClassifySpecParam(StringUtil.toJsonObject("id",id)));
    }

    //品牌列表
    @RequestMapping(value = "/brands",method = RequestMethod.GET)
    public void brandList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,goodsBfService.selectBrand(jsonObject));
    }

    //查询标签列表
    @RequestMapping(value = "/lables",method = RequestMethod.GET)
    public void lableList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,goodsBfService.queryLable(jsonObject));
    }

}
