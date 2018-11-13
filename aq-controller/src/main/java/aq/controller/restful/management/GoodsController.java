package aq.controller.restful.management;

import aq.common.util.HttpUtil;
import aq.common.util.StringUtil;
import aq.service.goods.GoodsService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Controller
@RequestMapping("/goods")
public class GoodsController extends aq.controller.restful.System {
    @Resource
    protected GoodsService goodsService;

    //查询商品
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public void goodsList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,goodsService.queryGoods(jsonObject));
    }

    //更新商品
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public void goodsUpdate(@RequestBody JsonObject requestJson,HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        if(StringUtil.isEmpty(requestJson.get("id"))) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND,"id is no null!");
        }
        writerJson(response,out,goodsService.updateGoods(requestJson));
    }

    //删除商品
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public void goodsDelete(@RequestBody JsonObject requestJson,HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        if(StringUtil.isEmpty(requestJson.get("id"))) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND,"id is no null!");
        }
        writerJson(response,out,goodsService.deleteGoods(requestJson));
    }

    //新增商品分类
    @RequestMapping(value = "/insertClassify",method = RequestMethod.POST)
    @ResponseBody
    public void insertClassify(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,goodsService.insertGoodsClassify(requestJson));
    }

    //查询商品分类
    @ResponseBody
    @RequestMapping(value = "/classifyList",method = RequestMethod.GET)
    public void classifyList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,goodsService.queryclassify(jsonObject));
    }

    //编辑商品分类
    @RequestMapping(value = "/updateClassify",method = RequestMethod.POST)
    @ResponseBody
    public void updateClassify(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,goodsService.updateClassify(requestJson));
    }

    //添加品牌
    @RequestMapping(value = "/insertBrand",method = RequestMethod.POST)
    @ResponseBody
    public void insertBrand(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,goodsService.insertBrand(requestJson));
    }

    //查询商品品牌
    @ResponseBody
    @RequestMapping(value = "/brandList",method = RequestMethod.GET)
    public void brandList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,goodsService.querybrandList(jsonObject));
    }

    //更新品牌
    @RequestMapping(value = "/updateBrand",method = RequestMethod.POST)
    @ResponseBody
    public void updateBrand(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,goodsService.updateBrand(requestJson));
    }


    //删除品牌
    @RequestMapping(value = "/deleteBrand",method = RequestMethod.POST)
    @ResponseBody
    public void deleteBrand(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,goodsService.deleteBrand(requestJson));
    }


    //查询规格分类
    @ResponseBody
    @RequestMapping(value = "/specList",method = RequestMethod.GET)
    public void specList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,goodsService.queryspec(jsonObject));
    }


    //添加规格
    @RequestMapping(value = "/insertSpec",method = RequestMethod.POST)
    @ResponseBody
    public void insertSpec(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,goodsService.insertSpec(requestJson));
    }


    //更新规格
    @RequestMapping(value = "/updateSpec",method = RequestMethod.POST)
    @ResponseBody
    public void updateSpec(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,goodsService.updateSpec(requestJson));
    }


    //删除规格
    @RequestMapping(value = "/deleteSpec",method = RequestMethod.POST)
    @ResponseBody
    public void deleteSpec(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,goodsService.deleteSpec(requestJson));
    }


    //添加规格
    @RequestMapping(value = "/insertSpecValue",method = RequestMethod.POST)
    @ResponseBody
    public void insertSpecValue(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,goodsService.insertSpecValue(requestJson));
    }


    //查询规格分类
    @ResponseBody
    @RequestMapping(value = "/specValueList",method = RequestMethod.GET)
    public void specValueList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,goodsService.queryspecValue(jsonObject));
    }

    //更新/删除规格
    @RequestMapping(value = "/updateSpecValue",method = RequestMethod.POST)
    @ResponseBody
    public void updateSpecValue(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,goodsService.updateSpecValue(requestJson));
    }


    //更新后台品牌
    @RequestMapping(value = "/updateClassifyBrand",method = RequestMethod.POST)
    @ResponseBody
    public void updateClassifyBrand(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,goodsService.updateClassifyBrand(requestJson));
    }

    //删除分类
    @RequestMapping(value = "/deleteClassify",method = RequestMethod.POST)
    @ResponseBody
    public void deleteClassify(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,goodsService.deleteClassify(requestJson));
    }
}
