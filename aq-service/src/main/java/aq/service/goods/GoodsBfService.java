package aq.service.goods;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

/**
 * Created by ywb on 2017-02-23.
 */
public interface GoodsBfService extends BaseService {

    //插入旧商品
    JsonObject insertOldGoods(JsonObject jsonObject);

    //插入新商品
    JsonObject insertNewGoods(JsonObject jsonObject);

    //查询商品
    JsonObject queryGoods(JsonObject jsonObject);

    //更新商品
    JsonObject updateGoods(JsonObject jsonObject);

    //删除商品
    JsonObject deleteGoods(JsonObject jsonObject);

    //更新库存
    JsonObject updateStock(JsonObject jsonObject);

    //查询分类，返回级联
    JsonObject selectClassifyCascade(JsonObject jsonObject);

    //查询分类规格参数
    JsonObject selectClassifySpecParam(JsonObject jsonObject);

    //查询分类品牌参数
    JsonObject selectClassifyBrandParam(JsonObject jsonObject);

    //查询商品分类规格参数
    JsonObject selectGoodsClassifyCascade(JsonObject jsonObject);

    //查询品牌
    JsonObject selectBrand(JsonObject jsonObject);

    //查询标签
    JsonObject queryLable(JsonObject jsonObject);

    //添加标签商品
    JsonObject addGoodsLable(JsonObject jsonObject);

    //删除标签商品
    JsonObject deleteGoodsLable(JsonObject jsonObject);

    //查询商品分类
    JsonObject queryTreeList(JsonObject jsonObject);

    //新增商品价格预测
    JsonObject insertForecast(JsonObject jsonObject);

    //查询商品价格预测
    JsonObject queryForecastList(JsonObject jsonObject);

    //删除商品价格预测
    JsonObject deleteForecast(JsonObject jsonObject);

    //更新商品价格预测
    JsonObject updateForecast(JsonObject jsonObject);

}
